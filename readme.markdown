# SignedRequest4J

## What's this?

SignedRequest4J is a simple Java library supporting OAuth 1.0 signing. 

With SignedRequest4J, you can easily make 2-legged/3-legged OAuth signed HTTP requests.

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

### Download

No additional jars required.

    ./download/signedrequest4j-1.0.jar

### via Maven

    <repositories>
      ...
      <repository>
        <id>signedrequest4j-releases</id>
        <url>https://github.com/seratch/signedrequest4j/raw/master/mvn-repo/releases</url>
      </repository>
      <repository>
        <id>signedrequest4j-snapshots</id>
        <url>https://github.com/seratch/signedrequest4j/raw/master/mvn-repo/snapshots</url>
      </repository>
      ...
    </repositories>

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>signedrequest4j</artifactId>
      <version>1.0</version>
    </dependency>

## Snippets

### 2-legged OAuth instance

    import com.github.seratch.signedrequest4j.HttpResponse;
    import com.github.seratch.signedrequest4j.OAuthConsumer;
    import com.github.seratch.signedrequest4j.SignedRequest;
    import com.github.seratch.signedrequest4j.SignedRequestFactory;

    SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"));

### 3-legged OAuth instance

    import com.github.seratch.signedrequest4j.OAuthToken;
    
    SignedRequest signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            new OAuthToken("access_token", "token_secret"));

### Signature with additional parameters

    import java.util.HashMap;
    import java.util.Map;

    Map<String, Object> additionalParams = new HashMap<String, Object>();
    additionalParams.put("xoauth_requestor_id", "user@example.com");
    
    SignedRequest signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            additionalParams);
    SignedRequest signedRequest3 = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            new OAuthToken("access_token", "token_secret"),
            additionalParams);

### Signature method : HMAC-SHA1 (default)

    SignedRequest signedRequest1 = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"));

    import com.github.seratch.signedrequest4j.SignatureMethod;
    SignedRequest signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.HMAC_SHA1);

### Signature method : RSA-SHA1

    SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.RSA_SHA1)
    signedRequest.setRsaPrivateKeyValue("-----BEGIN RSA PRIVATE KEY-----\n...");

### Signature method : PLAINTEXT

    SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            SignatureMethod.PLAINTEXT)

### Verifying signature

    Map<String, Object> additionalParams = new HashMap<String, Object>();
    additionalParams.put("xoauth_requestor_id", "user@example.com");
    SignedRequest signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
            new OAuthConsumer("consumer_key", "consumer_secret"),
            new OAuthToken("access_token", "token_secret"),
            additionalParams);
    String signature = signedRequest.getSignature(
            "http://sp.example.com/",   // URL
            HttpMethod.GET,			    // HTTP method
            "nonce_value",			    // oauth_nonce value
            1272026745L				    // oauth_timestamp value
    );
    if ("K7OrQ7UU+k94LnaezxFs4jBBekc=".equals(signature)) {
        System.out.println("Signature is valid.");
    }

### HTTP/1.1 GET request

    HttpResponse response = signedRequest.doGet(
            "https://github.com/seratch/signedrequest4j", 
            "UTF-8");

    System.out.println(response.getStatusCode());
    System.out.println(response.getHeaders());
    System.out.println(response.getContent());

### HTTP/1.1 POST request

    Map<String, Object> requestParameters = new HashMap<String, Object>();
    requestParameters.put("something", "updated");
    HttpResponse response = signedRequest.doPost(
            "https://github.com/seratch/signedrequest4j", 
            requestParameters,
            "UTF-8");

### HTTP/1.1 PUT request
    HttpResponse response = signedRequest.doPut(
            "https://github.com/seratch/signedrequest4j");

### HTTP/1.1 DELETE request
    HttpResponse response = signedRequest.doDelete(
            "https://github.com/seratch/signedrequest4j");

### HTTP/1.1 HEAD request
    HttpResponse response = signedRequest.doHead(
            "https://github.com/seratch/signedrequest4j");

### HTTP/1.1 OPTIONS request
    HttpResponse response = signedRequest.doOptions(
            "https://github.com/seratch/signedrequest4j");

### HTTP/1.1 TRACE request
    HttpResponse response = signedRequest.doTrace(
            "https://github.com/seratch/signedrequest4j");

### Using HttpURLConnection(not connected yet)

    import com.github.seratch.signedrequest4j.HttpMethod;
    
    HttpURLConnection conn = signedRequest.getHttpURLConnection(
            "https://github.com/seratch/signedrequest4j", 
            HttpMethod.GET);

## Contributors

* <a href="https://github.com/seratch">Kazuhiro Sera</a> &lt;seratch at gmail.com&gt;
* <a href="https://github.com/dewaken">Kenichi Dewa</a>

