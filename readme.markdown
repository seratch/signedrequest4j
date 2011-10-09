# SignedRequest4J : A Java library supporting OAuth 1.0 signing

## What's this?

SignedRequest4J is a Java library supporting OAuth 1.0 signing.

This library supports sending OAuth 1.0 signed HTTP requests and verifying the signature of requests.

With SignedRequest4J, it's so simple to execute 2-legged or 3-legged OAuth 1.0 signed HTTP requests.

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

```xml
<repositories>
  <repository>
    <id>seratch.github.com releases</id>
    <name>seratch.github.com releases</name>
    <url>http://seratch.github.com/mvn-repo/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.seratch</groupId>
    <artifactId>signedrequest4j</artifactId>
    <version>[1,)</version>
  </dependency>
</dependencies>
```

## Snippets

### 2-legged OAuth instance

```java
import com.github.seratch.signedrequest4j.*;

OAuthConsumer consumer = new OAuthConsumer("consumer_key", "consumer_secret");
SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(consumer);
```

### 3-legged OAuth instance

```java
OAuthConsumer consumer = new OAuthConsumer("consumer_key", "consumer_secret");
OAuthAccessToken accessToken = new OAuthAccessToken("token", "token_secret");
SignedRequest signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(consumer, accessToken);
```

### Signature with additional parameters

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Object> additionalParams = new HashMap<String, Object>();
additionalParams.put("xoauth_requestor_id", "user@example.com");

SignedRequest signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(consumer, additionalParams);
SignedRequest signedRequest3 = SignedRequestFactory.get3LeggedOAuthRequest(consumer, accessToken, additionalParams);
```

### Signature method HMAC-SHA1 (default)

```java
SignedRequest signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(consumer, SignatureMethod.HMAC_SHA1);
```

### Signature method RSA-SHA1

```java
SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(consumer, SignatureMethod.RSA_SHA1);
signedRequest.setRsaPrivateKeyValue("-----BEGIN RSA PRIVATE KEY-----\n...");
```

### Signature method PLAINTEXT

```java
SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(consumer, SignatureMethod.PLAINTEXT);
```

### Verifying signature

```java
String signature = signedRequest.getSignature(
  "http://example.com/", // URL
  HttpMethod.GET,        // HTTP method
  "nonce_value",         // oauth_nonce value
  1272026745L            // oauth_timestamp value
); // "K7OrQ7UU+k94LnaezxFs4jBBekc="
```

### GET

```java
HttpResponse response = signedRequest.doGet("http://example.com/", "UTF-8");

// response.getStatusCode();
// response.getHeaders();
// response.getContent();
```

### POST

```java
Map<String, Object> requestParameters = new HashMap<String, Object>();
requestParameters.put("something", "updated");
HttpResponse response = signedRequest.doPost("http://example.com/", requestParameters, "UTF-8");
```

or

```java
RequestBody body = new RequestBody("abc".getBytes(), "text/plain");
HttpResponse response = signedRequest.doPost("http://example.com/", body, "UTF-8");
```

### PUT

```java
Map<String, Object> requestParameters = new HashMap<String, Object>();
requestParameters.put("something", "updated");
HttpResponse response = signedRequest.doPut("http://example.com/", requestParameters, "UTF-8");
```

or

```java
RequestBody body = new RequestBody("abc".getBytes(), "text/plain");
HttpResponse response = signedRequest.doPost("http://example.com/", body, "UTF-8");
```

### DELETE

```java
Map<String, Object> requestParameters = new HashMap<String, Object>();
requestParameters.put("something", "updated");
HttpResponse response = signedRequest.doDelete("http://example.com/", requestParameters, "UTF-8");
```

or

```java
RequestBody body = new RequestBody("abc".getBytes(), "text/plain");
HttpResponse response = signedRequest.doDelete("http://example.com/", body, "UTF-8");
```

### HEAD

```java
HttpResponse response = signedRequest.doHead("http://example.com/");
```

### OPTIONS

```java
HttpResponse response = signedRequest.doOptions("http://example.com/");
```

### TRACE

```java
signedRequest.setHeader("Max-Forwards", "5");
HttpResponse response = signedRequest.doTrace("http://example.com/");
```

### Verifying signed requests

```java
String authorizationHeader = request.getHeader("Authorization");
OAuthConsumer consumer = new OAuthConsumer("key","secret");

boolean verified = SignedRequestVerifier.verifyHMacGetRequest(
"http://localhost/test/", authorizationHeader, consumer);

boolean verified = SignedRequestVerifier.verify("http://localhost/test/",
	authorizationHeader, consumer, HttpMethod.GET, SignatureMethod.HMAC_SHA1);
```

## Contributors

* <a href="https://github.com/seratch">Kazuhiro Sera</a> &lt;seratch at gmail.com&gt;
* <a href="https://github.com/dewaken">Kenichi Dewa</a>

