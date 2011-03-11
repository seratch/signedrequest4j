/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.github.seratch.signedrequest4scala

import scala.collection.JavaConverters._

import java.net._
import java.io._
import java.security._
import com.github.seratch.signedrequest4j.{Base64, OAuthEncoding, SignedRequestClientException}
import com.github.seratch.signedrequest4j.pem._
import javax.crypto.spec._
import javax.crypto.Mac
import spec.{KeySpec, PKCS8EncodedKeySpec}
import collection.mutable.ListBuffer

private[signedrequest4scala] class SignedRequestImpl
(
  val realm: OAuthRealm,
  val consumer: OAuthConsumer,
  val token: OAuthToken,
  val signatureMethod: SignatureMethod,
  val additionalParameters: Map[String, Any]
  )
  extends SignedRequest {

  val oAuthVersion: String = "1.0"
  var rsaPrivateKeyValue: String = null

  /**
   * 2 Legged OAuth Request
   */
  def this(realm: OAuthRealm,
           consumer: OAuthConsumer,
           signatureMethod: SignatureMethod) {
    this (realm, consumer, null, signatureMethod, null)
  }

  /**
   * 2 Legged OAuth Request
   */
  def this(realm: OAuthRealm,
           consumer: OAuthConsumer,
           signatureMethod: SignatureMethod,
           additionalParameters: Map[String, Any]) {
    this (realm, consumer, null, signatureMethod, additionalParameters)
  }

  def this(realm: OAuthRealm,
           consumer: OAuthConsumer,
           token: OAuthToken,
           signatureMethod: SignatureMethod) {
    this (realm, consumer, token, signatureMethod, null)
  }

  /**
   * {@inheritDoc}
   */
  override
  def setRsaPrivateKeyValue(rsaPrivateKeyValue: String): SignedRequest = {
    this.rsaPrivateKeyValue = rsaPrivateKeyValue
    this
  }

  /**
   * {@inheritDoc}
   */
  override
  def doDelete(url: String): HttpResponse = {
    doRequest(url, HttpMethod.DELETE, null, null)
  }

  /**
   * {@inheritDoc}
   */
  override
  def doGet(url: String, charset: String): HttpResponse = {
    doRequest(url, HttpMethod.GET, null, charset)
  }

  /**
   * {@inheritDoc}
   */
  override
  def doHead(url: String): HttpResponse = {
    doRequest(url, HttpMethod.HEAD, null, null)
  }

  /**
   * {@inheritDoc}
   */
  override
  def doOptions(url: String): HttpResponse = {
    doRequest(url, HttpMethod.OPTIONS, null, null)
  }

  /**
   * {@inheritDoc}
   */
  override
  def doPost(url: String, requestParameters: Map[String, Any], charset: String): HttpResponse = {
    doRequest(url, HttpMethod.POST, requestParameters, charset)
  }

  /**
   * {@inheritDoc}
   */
  override
  def doPut(url: String): HttpResponse = {
    doRequest(url, HttpMethod.PUT, null, null)
  }

  /**
   * {@inheritDoc}
   */
  override
  def doTrace(url: String): HttpResponse = {
    doRequest(url, HttpMethod.DELETE, null, null)
  }

  /**
   * {@inheritDoc}
   */
  override
  def doRequest(url: String, method: HttpMethod, requestParameters: Map[String, Any], charset: String): HttpResponse = {
    var _url = url
    (method, requestParameters) match {
      case (_, null) =>
      case (HttpMethod.GET, _) => requestParameters foreach {
        case (key, value) => _url += (if (url.contains("?")) "&" else "?") + key + "=" + value
      }
      case _ =>
    }
    val conn = getHttpURLConnection(_url, method)
    (method, requestParameters) match {
      case (_, null) =>
      case (HttpMethod.POST, _) => {
        var os: OutputStream = null
        var writer: OutputStreamWriter = null
        try {
          conn.setDoOutput(true)
          os = conn.getOutputStream
          writer = new OutputStreamWriter(os)
          requestParameters foreach {
            case (key, value) => {
              writer.append(key)
              writer.append("=")
              writer.append(value.toString)
            }
          }
        }
        finally {
          writer match {
            case null =>
            case _ => try writer.close catch {
              case _ =>
            }
          }
          os match {
            case null =>
            case _ => try os.close catch {
              case _ =>
            }
          }
        }
      }
      case _ =>
    }
    conn.connect
    try {
      val headersInJava = conn.getHeaderFields
      val mapBuffer = new collection.mutable.HashMap[String, List[String]]
      headersInJava.keySet.asScala foreach {
        case key => mapBuffer.update(key, headersInJava.get(key).asScala.toList)
      }
      HttpResponse(
        conn.getResponseCode,
        mapBuffer.toMap,
        getResponseCotent(conn, charset))
    }
    catch {
      case e: IOException => throw e
    }
  }

  /**
   * {@inheritDoc}
   */
  override
  def getHttpURLConnection(url: String, method: HttpMethod): HttpURLConnection = {
    val oAuthNonce = String.valueOf(new SecureRandom().nextLong)
    val oAuthTimestamp = System.currentTimeMillis / 1000
    val signature = getSignature(url, method, oAuthNonce, oAuthTimestamp)
    val conn = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    conn.setConnectTimeout(3000)
    conn.setReadTimeout(10000)
    conn.setRequestProperty("User-Agent",
      "SignedRequest4J HTTP Fetcher (+https://github.com/seratch/signedrequest4j)")
    conn.setRequestMethod(method.toString)
    conn.setRequestProperty("Authorization", getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp))
    conn
  }

  private[signedrequest4scala]
  case class Parameter(key: String, value: Any)

  /**
   * {@inheritDoc}
   */
  override
  def getSignatureBaseString(url: String, method: HttpMethod, oAuthNonce: String, oAuthTimestamp: Long): String = {
    val normalizedParamsBuf = new StringBuilder
    for (param <- getNormalizedParameters(oAuthNonce, oAuthTimestamp)) {
      if (normalizedParamsBuf.length > 0) {
        normalizedParamsBuf.append("&")
      }
      normalizedParamsBuf.append(param.key)
      normalizedParamsBuf.append("=")
      normalizedParamsBuf.append(param.value)
    }
    val baseStringBuf = new StringBuilder
    baseStringBuf.append(OAuthEncoding.encode(method.toString.toUpperCase))
    baseStringBuf.append("&")
    baseStringBuf.append(OAuthEncoding.encode(OAuthEncoding.normalizeURL(url)))
    baseStringBuf.append("&")
    baseStringBuf.append(OAuthEncoding.encode(normalizedParamsBuf.toString))
    baseStringBuf.toString
  }

  /**
   * {@inheritDoc}
   */
  override
  def getSignature(url: String, method: HttpMethod, oAuthNonce: String, oAuthTimestamp: Long): String = {
    val baseString = getSignatureBaseString(url, method, oAuthNonce, oAuthTimestamp)
    signatureMethod match {
      case SignatureMethod.HMAC_SHA1 => {
        val algorithm = "HmacSHA1"
        val key = consumer.getConsumerSecret + "&" +
          (if (token != null && token.getTokenSecret != null) token.getTokenSecret else "")
        try {
          val mac = Mac.getInstance(algorithm)
          mac.init(new SecretKeySpec(key.getBytes, algorithm))
          Base64.encode(mac.doFinal(baseString.getBytes))
        }
        catch {
          case e: NoSuchAlgorithmException =>
            throw new SignedRequestClientException("Invalid Alogrithm : " + e.getLocalizedMessage)
          case e: InvalidKeyException =>
            throw new SignedRequestClientException("Invalid key : " + e.getLocalizedMessage)
        }
      }
      case SignatureMethod.RSA_SHA1 => {
        if (rsaPrivateKeyValue == null || rsaPrivateKeyValue.length == 0) {
          throw new SignedRequestClientException("RSA Private Key value is required.")
        }
        try {
          val reader = new PEMReader(new ByteArrayInputStream(this.rsaPrivateKeyValue.getBytes("UTF-8")))
          val bytes: Array[Byte] = reader.getDerBytes
          var keySpec: KeySpec = null
          keySpec = reader.getBeginMarker match {
            case null => throw new SignedRequestClientException(
              "Invalid PEM file: Unknown marker " + "for private key null")
            case PEMReader.PRIVATE_PKCS1_MARKER => (new PKCS1EncodedKeySpec(bytes)).getKeySpec
            case PEMReader.PRIVATE_PKCS8_MARKER => new PKCS8EncodedKeySpec(bytes)
            case _ => throw new SignedRequestClientException(
              "Invalid PEM file: Unknown marker " + "for private key " + reader.getBeginMarker)
          }
          val signer = Signature.getInstance("SHA1withRSA")
          val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)
          signer.initSign(privateKey)
          signer.update(baseString.getBytes)
          Base64.encode(signer.sign)
        }
        catch {
          case e: Exception => throw new SignedRequestClientException("Cannot make a signature(RSA)", e)
        }
      }
      case SignatureMethod.PLAINTEXT => baseString
      case _ => throw new SignedRequestClientException(
        "Invalid Signature Method (oauth_signature_method) : " + signatureMethod.toString)
    }
  }

  private[signedrequest4scala]
  def getNormalizedParameters(oAuthNonce: String, oAuthTimestamp: Long): List[Parameter] = {
    val params = new ListBuffer[Parameter]
    params.append(Parameter("oauth_consumer_key", consumer.getConsumerKey))
    token match {
      case null =>
      case _ => params.append(Parameter("oauth_token", token.getToken))
    }
    params.append(Parameter("oauth_nonce", oAuthNonce))
    params.append(Parameter("oauth_signature_method", signatureMethod))
    params.append(Parameter("oauth_timestamp", oAuthTimestamp))
    params.append(Parameter("oauth_version", oAuthVersion))
    additionalParameters match {
      case null =>
      case _ => additionalParameters foreach {
        case (key, value) => params.append(Parameter(key, OAuthEncoding.encode(value)))
        case _ =>
      }
    }
    params.toList sortBy (_.key)
  }

  private[signedrequest4scala]
  def getAuthorizationHeader(signature: String, oAuthNonce: String, oAuthTimestamp: Long): String = {
    val buf = new StringBuilder
    buf.append("OAuth ")
    realm match {
      case null =>
      case _ => {
        buf.append("realm=\"")
        buf.append(OAuthEncoding.encode(realm))
        buf.append("\",")
      }
    }
    token match {
      case null =>
      case _ => {
        buf.append("oauth_token=\"")
        buf.append(OAuthEncoding.encode(token.getToken))
        buf.append("\",")
      }
    }
    buf.append("oauth_consumer_key=\"")
    buf.append(OAuthEncoding.encode(consumer.getConsumerKey))
    buf.append("\",")
    buf.append("oauth_signature_method=\"")
    buf.append(OAuthEncoding.encode(signatureMethod))
    buf.append("\",")
    buf.append("oauth_signature=\"")
    buf.append(OAuthEncoding.encode(signature))
    buf.append("\",")
    buf.append("oauth_timestamp=\"")
    buf.append(OAuthEncoding.encode(oAuthTimestamp))
    buf.append("\",")
    buf.append("oauth_nonce=\"")
    buf.append(OAuthEncoding.encode(oAuthNonce))
    buf.append("\",")
    buf.append("oauth_version=\"")
    buf.append(OAuthEncoding.encode(oAuthVersion))
    buf.append("\"")
    additionalParameters match {
      case null =>
      case _ => additionalParameters foreach {
        case (key, value) => {
          buf.append(",")
          buf.append(OAuthEncoding.encode(key))
          buf.append("=\"")
          buf.append(OAuthEncoding.encode(value))
          buf.append("\"")
        }
      }
    }
    buf.toString
  }

  private[signedrequest4scala]
  def getResponseCotent(conn: HttpURLConnection, charset: String): String = {
    var is: InputStream = null
    var br: BufferedReader = null
    try {
      is = conn.getInputStream
      val isr = charset match {
        case null => new InputStreamReader(is)
        case _ => new InputStreamReader(is, charset)
      }
      br = new BufferedReader(isr)
      val buf = new StringBuilder
      var line: String = null
      while ( {
        line = br.readLine;
        line
      } != null) {
        buf.append(line)
        buf.append("\n")
      }
      buf.toString
    }
    finally {
      is match {
        case null =>
        case _ => try is.close catch {
          case _ =>
        }
      }
      br match {
        case null =>
        case _ => try br.close catch {
          case _ =>
        }
      }
    }
  }

}
