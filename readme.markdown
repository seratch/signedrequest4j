# SignedRequest4J/Scala

## What's this?

SignedRequest4J/Scala is a simple Java/Scala library supporting OAuth 1.0 signing.

With SignedRequest4J/Scala, you can easily execute 2-legged or 3-legged OAuth signed HTTP requests.

### 2-legged OAuth

* Service Provider, Consumer

* a.k.a Signed Fetch, Phone Home in the OpenSocial community

* OAuth Consumer Request 1.0 Draft 1

    <a href="http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html">http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html</a>

### 3-legged OAuth

* Service Provider, Consumer, User

* The term "OAuth" mostly means 3-legged OAuth

* OAuth Core 1.0

    <a href="http://oauth.net/core/1.0/#signing_process">http://oauth.net/core/1.0/#signing_process</a>

* RFC 5849: The OAuth 1.0 Protocol

    <a href="http://tools.ietf.org/html/rfc5849">http://tools.ietf.org/html/rfc5849</a>

## How to install

### via Maven

    <repositories>
      <repository>
        <id>seratch.github.com releases</id>
        <name>seratch.github.com releases</name>
	<url>http://seratch.github.com/mvn-repo/releases</url>
      </repository>
    </repositories>

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>signedrequest4j</artifactId>
      <version>[1,)</version>
    </dependency>

## Snippets (Java)

### 2-legged OAuth instance
    import com.github.seratch.signedrequest4j.HttpResponse;
    import com.github.seratch.signedrequest4j.OAuthConsumer;
    import com.github.seratch.signedrequest4j.SignedRequest;
    import com.github.seratch.signedrequest4j.SignedRequestFactory;

    SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"));

### 3-legged OAuth instance
    import com.github.seratch.signedrequest4j.OAuthAccessToken;

    SignedRequest signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            new OAuthAccessToken("token", "token_secret"));

### Signature with additional parameters
    import java.util.HashMap;
    import java.util.Map;

    Map<String, Object> additionalParams = new HashMap<String, Object>();
    additionalParams.put("xoauth_requestor_id", "user@example.com");
    
    SignedRequest signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            additionalParams);
    SignedRequest signedRequest3 = SignedRequestFactory.get3LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            new OAuthAccessToken("token", "token_secret"),
            additionalParams);

### Signature method HMAC-SHA1 (default)
    SignedRequest signedRequest1 = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"));

    import com.github.seratch.signedrequest4j.SignatureMethod;
    
    SignedRequest signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.HMAC_SHA1);

### Signature method RSA-SHA1
    SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.RSA_SHA1)
    signedRequest.setRsaPrivateKeyValue("-----BEGIN RSA PRIVATE KEY-----\n...");

### Signature method PLAINTEXT
    SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.PLAINTEXT)

### Verifying signature
    String signature = signedRequest.getSignature(
            "http://sp.example.com/",   // URL
            HttpMethod.GET,             // HTTP method
            "nonce_value",              // oauth_nonce value
            1272026745L                 // oauth_timestamp value
    );

    if ("K7OrQ7UU+k94LnaezxFs4jBBekc=".equals(signature)) {
        System.out.println("Signature is valid.");
    }

### HTTP GET request
    HttpResponse response = signedRequest.doGet(
            "https://github.com/seratch/signedrequest4j",
            "UTF-8");

    System.out.println(response.getStatusCode());
    System.out.println(response.getHeaders());
    System.out.println(response.getContent());

### HTTP POST request
    Map<String, Object> requestParameters = new HashMap<String, Object>();
    requestParameters.put("something", "updated");
    HttpResponse response = signedRequest.doPost(
            "https://github.com/seratch/signedrequest4j",
            requestParameters,
            "UTF-8");

### HTTP PUT request
    HttpResponse response = signedRequest.doPut(
            "https://github.com/seratch/signedrequest4j");

### HTTP DELETE request
    HttpResponse response = signedRequest.doDelete(
            "https://github.com/seratch/signedrequest4j");

### HTTP HEAD request
    HttpResponse response = signedRequest.doHead(
            "https://github.com/seratch/signedrequest4j");

### HTTP OPTIONS request
    HttpResponse response = signedRequest.doOptions(
            "https://github.com/seratch/signedrequest4j");

### HTTP TRACE request
    HttpResponse response = signedRequest.doTrace(
            "https://github.com/seratch/signedrequest4j");

### Using HttpURLConnection(not connected yet)
    import com.github.seratch.signedrequest4j.HttpMethod;

    HttpURLConnection conn = signedRequest.getHttpURLConnection(
            "https://github.com/seratch/signedrequest4j",
            HttpMethod.GET);

### Verifying signed requests

    String authorizationHeader = request.getHeader("Authorization");
    OAuthConsumer consumer = new OAuthConsumer("key","secret");

    boolean verified = SignedRequestVerifier.verifyHMacGetRequest(
        "http://localhost/test/", authorizationHeader, consumer);

    boolean verified = SignedRequestVerifier.verify("http://localhost/test/",
		authorizationHeader, consumer, HttpMethod.GET, SignatureMethod.HMAC_SHA1);

## Snippets (Scala)

### 2-legged OAuth instance
    import com.github.seratch.signedrequest4scala._

    val signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"))

### 3-legged OAuth instance
    val signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"),
            OAuthAccessToken("token", "token_secret"))

### Signature with additional parameters
    val additionalParams = Map("xoauth_requestor_id" -> "user@example.com")

    val signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"),
            additionalParams)
    val signedRequest3 = SignedRequestFactory.get3LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"),
            OAuthAccessToken("token", "token_secret"),
            additionalParams)

### Signature method HMAC-SHA1 (default)
    val signedRequest1 = SignedRequestFactory.get2LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"))
    val signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.HMAC_SHA1)
### Signature method RSA-SHA1
    val signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.RSA_SHA1)
    signedRequest.setRsaPrivateKeyValue("-----BEGIN RSA PRIVATE KEY-----\n...");

### Signature method PLAINTEXT
    val signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.PLAINTEXT)

### Verifying signature
    val signature = signedRequest.getSignature(
            "http://sp.example.com/",   // URL
            HttpMethod.GET,             // HTTP method
            "nonce_value",              // oauth_nonce value
            1272026745L                 // oauth_timestamp value
    )
    signature match {
        case "K7OrQ7UU+k94LnaezxFs4jBBekc=" => println("Signature is valid.")
    }

### HTTP GET request
    val response = signedRequest.doGet(
             "https://github.com/seratch/signedrequest4j",
             "UTF-8")
    println(response.statusCode)
    println(response.headers)
    println(response.content)

### HTTP POST request
    val requestParameters = Map("something" -> "updated")
    val response = signedRequest.doPost(
            "https://github.com/seratch/signedrequest4j",
            requestParameters,
            "UTF-8")

### HTTP PUT request
    val response = signedRequest.doPut("https://github.com/seratch/signedrequest4j")

### HTTP DELETE request
    val response = signedRequest.doDelete("https://github.com/seratch/signedrequest4j")

### HTTP HEAD request
    val response = signedRequest.doHead("https://github.com/seratch/signedrequest4j")

### HTTP OPTIONS request
    val response = signedRequest.doOptions("https://github.com/seratch/signedrequest4j")

### HTTP TRACE request
    val response = signedRequest.doTrace("https://github.com/seratch/signedrequest4j")

### Using HttpURLConnection(not connected yet)
    val conn = req.getHttpURLConnection(
            "https://github.com/seratch/signedrequest4j",
            HttpMethod.GET)

## Contributors

* <a href="https://github.com/seratch">Kazuhiro Sera</a> &lt;seratch at gmail.com&gt;
* <a href="https://github.com/dewaken">Kenichi Dewa</a>

