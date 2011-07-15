package com.github.seratch.signedrequest4scala

import org.specs.Specification
import java.io._

object SignedRequestFactorySpec extends Specification {

  "#get2LeggedOAuthRequest" should {
    "be available" in {
      val signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"))
      signedRequest mustNotBe null
    }
  }

  "#get2LeggedOAuthRequest SignatureMethod" should {
    "be available" in {
      val signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"),
        SignatureMethod.HMAC_SHA1)
      signedRequest mustNotBe null
    }
  }

  "#get2LeggedOAuthRequest SignatureMethod RSA_SHA1" should {
    "be available" in {
      val signedRequest: SignedRequestImpl = SignedRequestFactory.get2LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"),
        SignatureMethod.RSA_SHA1).asInstanceOf[SignedRequestImpl]
      val rsaPrivateKey: StringBuilder = new StringBuilder
      try {
        val br: BufferedReader = new BufferedReader(
          new InputStreamReader(
            this.getClass.getClassLoader.getResourceAsStream("RSA_PrivateKey.txt")))
        var line: String = null
        while ((({
          line = br.readLine;
          line
        })) != null) {
          rsaPrivateKey.append(line)
          rsaPrivateKey.append("\n")
        }
        signedRequest.setRsaPrivateKeyValue(rsaPrivateKey.toString)
        val actual: String = signedRequest.getSignature("http://seratch.net/", HttpMethod.GET, "oAuthNonce", 12345L)
        actual.length mustEqual 172
        System.out.println(signedRequest.getAuthorizationHeader(actual, "hogehoge", 12345L))
        val response: HttpResponse = signedRequest.doGet("http://seratch.net/", "UTF-8")
        System.out.println(response.headers)
        System.out.println(response.content)
      }
      catch {
        case e: NullPointerException => {
          System.out.println("RSA_PrivateKey.txt not found, test skipped.")
        }
      }
      signedRequest mustNotBe null
    }
  }

  "#get2LeggedOAuthRequest SignatureMethod PLAINTEXT" should {
    "be available" in {
      val signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"),
        SignatureMethod.PLAINTEXT)
      signedRequest mustNotBe null
    }
  }

  "#get2LeggedOAuthRequest addtional parameters" should {
    "be available" in {
      val additionalParams = Map("xoauth_requestor_id" -> "user@example.com")
      val signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"),
        additionalParams)
      signedRequest mustNotBe null
    }
  }

  "#get3LeggedOAuthRequest" should {
    "be available" in {
      val signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"),
        OAuthAccessToken("access_token", "token_secret"))
      signedRequest mustNotBe null
    }
  }

  "#get3LeggedOAuthRequest addtional parameters" should {
    "be available" in {
      val additionalParams = Map("xoauth_requestor_id" -> "user@example.com")
      val signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"),
        OAuthAccessToken("access_token", "token_secret"),
        additionalParams)
      signedRequest mustNotBe null
    }
  }

}

