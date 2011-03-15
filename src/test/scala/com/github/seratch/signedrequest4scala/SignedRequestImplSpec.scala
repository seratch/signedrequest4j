package com.github.seratch.signedrequest4scala

import java.util.Properties
import org.specs.Specification

object SignedRequestImplSpec extends Specification {

  "3-legged OAuth" should {
    "be able to retrieve home timeline from Twitter" in {
      val props = new Properties
      props.load(this.getClass().getClassLoader().getResourceAsStream("TwitterOAuth.properties"))

      val signedReq = SignedRequestFactory.get3LeggedOAuthRequest(
        OAuthConsumer(props.get("consumer_key").toString, props.get("consumer_secret").toString),
        OAuthToken(props.get("access_token").toString, props.get("token_secret").toString)
      )
      val response = signedReq.doGet("http://api.twitter.com/1/statuses/home_timeline.xml", "UTF-8")
      println(response.statusCode)
      println(response.headers)
      println(response.content)
      response.statusCode mustEqual 200
    }
  }

  "Instantiation" should {
    "be available" in {
      val req = new SignedRequestImpl(null, OAuthConsumer("sdfsa", "sdfafa33333"), SignatureMethod.HMAC_SHA1)
      req mustNotBe null
    }
  }

  "#getHttpURLConnection" should {
    "be available" in {
      val req = new SignedRequestImpl(null, OAuthConsumer("sdfsa", "sdfafa33333"), SignatureMethod.HMAC_SHA1)
      req mustNotBe null
      val conn = req.getHttpURLConnection("http://localhost:8080/", HttpMethod.GET)
      conn mustNotBe null
    }
  }

  "#getSignature" should {
    "be available" in {
      val req = new SignedRequestImpl(null, OAuthConsumer("sdfsa", "sdfafa33333"), SignatureMethod.HMAC_SHA1)
      req mustNotBe null
      val url: String = "http://localhost:8080/"
      val method = HttpMethod.GET
      val oAuthNonce = "sdfaaaa"
      val oAuthTimestamp = 12345L
      val actual = req.getSignature(url, method, oAuthNonce, oAuthTimestamp)
      actual mustEqual "I/ci60MwaIR2x2mWvCZPFaWjaEI="
    }
  }

  "#getSignature" should {
    "be available" in {
      val props = new Properties
      props.load(this.getClass().getClassLoader().getResourceAsStream("TwitterOAuth.properties"))
      val req = SignedRequestFactory.get2LeggedOAuthRequest(OAuthConsumer("sdfsa", "sdfafa33333"))
      val url = "http://localhost:8080/"
      val method = HttpMethod.GET
      val oAuthNonce = "sdfaaaa"
      val oAuthTimestamp = 12345L
      val actual = req.getSignature(url, method, oAuthNonce, oAuthTimestamp)
      actual mustEqual "I/ci60MwaIR2x2mWvCZPFaWjaEI="
    }
  }
  "#getSignature" should {
    "be available" in {
      val additionalparams = Map[String, Any]("xoauth_requestor_id" -> "user@example.com")
      val signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
        OAuthConsumer("consumer_key", "consumer_secret"),
        OAuthToken("access_token", "token_secret"),
        additionalparams);
      val signature = signedRequest.getSignature(
        "http://sp.example.com/", // URL
        HttpMethod.GET, // HTTP method
        "nonce_value", // oauth_nonce value
        1272026745L // oauth_timestamp value
      );
      signature mustEqual "K7OrQ7UU+k94LnaezxFs4jBBekc="
      signature match {
        case "K7OrQ7UU+k94LnaezxFs4jBBekc=" => println("Signature is valid.")
      }
    }
  }

  "#getSignature" should {
    "work as OAuth 1.0 Core Appendix A.1.  Documentation and Registration" in {
      val additionalparams = Map[String, Any](
        "file" -> "vacation.jpg",
        "size" -> "original"
      )
      val req = new SignedRequestImpl(
        null,
        OAuthConsumer("dpf43f3p2l4k3l03", "kd94hf93k423kf44"),
        OAuthToken("nnch734d00sl2jdk", "pfkkdhi9sl3r4s00"),
        SignatureMethod.HMAC_SHA1,
        additionalparams
      )
      val url = "http://photos.example.net/photos"
      val method = HttpMethod.GET
      val oAuthNonce = "kllo9940pd9333jh"
      val oAuthTimestamp = 1191242096L
      val baseString = req.getSignatureBaseString(url, method, oAuthNonce, oAuthTimestamp)
      baseString mustEqual "GET&http%3A%2F%2Fphotos.example.net%2Fphotos&file%3Dvacation.jpg%26oauth_consumer_key%3Ddpf43f3p2l4k3l03%26oauth_nonce%3Dkllo9940pd9333jh%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1191242096%26oauth_token%3Dnnch734d00sl2jdk%26oauth_version%3D1.0%26size%3Doriginal"
      val signature = req.getSignature(url, method, oAuthNonce, oAuthTimestamp)
      signature mustEqual "tR3+Ty81lMeYAr/Fid0kMTYa/WM="
    }
  }

  "#getAuthorizationHeader" should {
    "be available" in {
      val req = new SignedRequestImpl(
        null,
        OAuthConsumer("sdfsa", "sdfafa33333"),
        null,
        SignatureMethod.HMAC_SHA1
      )
      val signature = "signatureXXX"
      val oAuthNonce = "noncenonce"
      val oAuthTimestamp = 12345L
      val actual = req.getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp)
      actual mustEqual "OAuth oauth_consumer_key=\"sdfsa\",oauth_signature_method=\"HMAC-SHA1\",oauth_signature=\"signatureXXX\",oauth_timestamp=\"12345\",oauth_nonce=\"noncenonce\",oauth_version=\"1.0\""
    }
  }

  "#doRequest" should {
    "be available" in {
      val req = new SignedRequestImpl(null, OAuthConsumer("sdfsa", "sdfafa33333"), SignatureMethod.HMAC_SHA1)
      req mustNotBe null
      val params = Map[String, Any](
        "hoge" -> "foo"
      )
      val response = req.doRequest("http://seratch.net/", HttpMethod.GET, params, "UTF-8")
      println(response.headers)
      println(response.content)
      response.statusCode mustEqual 200
    }
  }

  "#doGet" should {
    "be available" in {
      val req = new SignedRequestImpl(null, OAuthConsumer("sdfsa", "sdfafa33333"), SignatureMethod.HMAC_SHA1)
      req mustNotBe null
      val params = Map[String, Any](
        "hoge" -> "foo"
      )
      val response = req.doGet("http://seratch.net/", "UTF-8")
      println(response.headers)
      println(response.content)
      response.statusCode mustEqual 200
    }
  }

  "#doPost" should {
    "be available" in {
      val req = new SignedRequestImpl(null, OAuthConsumer("sdfsa", "sdfafa33333"), SignatureMethod.HMAC_SHA1)
      req mustNotBe null
      val params = Map("foo" -> "var")
      val response = req.doPost("http://seratch.net/", params, "UTF-8")
      println(response.headers)
      println(response.content)
      response.statusCode mustEqual 200
    }
  }

  "#getHttpURLConnection" should {
    "be available" in {
      val req = new SignedRequestImpl(null, OAuthConsumer("sdfsa", "sdfafa33333"), SignatureMethod.HMAC_SHA1)
      req mustNotBe null
      val conn = req.getHttpURLConnection("http://seratch.net/", HttpMethod.GET)
      conn mustNotBe null
    }
  }

}