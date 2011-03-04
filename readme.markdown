# SignedRequest4J

## What's this?

SignedRequest4J is a simple Java library supporting OAuth 1.0 signing. 
With SignedRequest4J, you can easily make 2-legged/3-legged OAuth signed HTTP requests.

### 2-legged OAuth 
* Service Provider, Consumer
* a.k.a Signed Fetch, Phone HOme in the OpenSocial community
* OAuth Consumer Request 1.0 Draft 1
    http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html

### 3-legged OAuth
* Service Provider, Consumer, End User
* "OAuth" term mostly means 3-legged OAuth protocol
* OAuth Core 1.0tw
    http://oauth.net/core/1.0/#signing_process
* RFC 5849: The OAuth 1.0 Protocol
    http://tools.ietf.org/html/rfc5849

## How to install

### Download

No additional jars required.

    ./download/signed-request-for-java-1.0-SNAPSHOT.jar

### via Maven

    <repositories>
      ...
      <repository>
        <id>signed-request-for-java-releases</id>
        <url>https://github.com/seratch/signed-request-for-java/raw/master/mvn-repo/releases</url>
      </repository>
      <repository>
        <id>signed-request-for-java-snapshots</id>
        <url>https://github.com/seratch/signed-request-for-java/raw/master/mvn-repo/snapshots</url>
      </repository>
      ...
    </repositories>

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>signed-request-for-java</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

## How to use

### Snippet: 2 Legged OAuth Request

    import com.github.seratch.signedrequest.HttpResponse;
    import com.github.seratch.signedrequest.OAuthConsumer;
    import com.github.seratch.signedrequest.SignedRequest;
    import com.github.seratch.signedrequest.SignedRequestFactory;

    SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
            "http://sp.example.com/",
            new OAuthConsumer("consumer_key", "consumer_secret"));

### Snippet: 3 Legged OAuth Request

    import com.github.seratch.signedrequest.OAuthToken;
    
    SignedRequest signedRequest = SignedRequestFactory.get3LeggedOAuthRequest(
            "http://sp.example.com/",
            new OAuthConsumer("consumer_key", "consumer_secret"),
            new OAuthToken("access_token"));

### Snippet: Signature with Additional Parameters

    Map<String, Object> additionalParams = new HashMap<String, Object>();
    additionalParams.put("xoauth_requestor_id", "user@example.com");
    
    // 2 Legged OAuth
    SignedRequest signedRequest2 = SignedRequestFactory.get2LeggedOAuthRequest(
            "http://sp.example.com/", 
            new OAuthConsumer("consumer_key", "consumer_secret"), 
            additionalParams);
    
    // 3 Legged OAuth
    SignedRequest signedRequest3 = SignedRequestFactory.get2LeggedOAuthRequest(
            "http://sp.example.com/", 
            new OAuthConsumer("consumer_key", "consumer_secret"), 
            new OAuthToken("access_token"),
            additionalParams);

### Snippet: Simple GET Request

    HttpResponse response = signedRequest.doGet(
            "https://github.com/seratch/signed-request-for-java", 
            "UTF-8");

    System.out.println(response.getStatusCode());
    System.out.println(response.getHeaders());
    System.out.println(response.getContent());

### Snippet: Simple POST Request

    import java.util.HashMap;
    import java.util.Map;

    Map<String, Object> requestParameters = new HashMap<String, Object>();
    requestParameters.put("something", "updated");
    HttpResponse response = signedRequest.doPost(
            "https://github.com/seratch/signed-request-for-java", 
            requestParameters,
            "UTF-8");

### Snippet: Simple PUT Request
    HttpResponse response = signedRequest.doPut(
            "https://github.com/seratch/signed-request-for-java");

### Snippet: Simple DELETE Request
    HttpResponse response = signedRequest.doDelete(
            "https://github.com/seratch/signed-request-for-java");

### Snippet: Simple HEAD Request
    HttpResponse response = signedRequest.doHead(
            "https://github.com/seratch/signed-request-for-java");

### Snippet: Simple OPTIONS Request
    HttpResponse response = signedRequest.doOptions(
            "https://github.com/seratch/signed-request-for-java");

### Snippet: Simple TRACE Request
    HttpResponse response = signedRequest.doTrace(
            "https://github.com/seratch/signed-request-for-java");

### Snippet: Taking HttpURLConnection instance(not connected yet)
    HttpURLConnection conn = signedRequest.getHttpURLConnection(
            "https://github.com/seratch/signed-request-for-java", 
            HttpMethod.GET);

## Contributors

* Kazuhiro Sera &lt;seratch at gmail.com&gt;
* Kenichi Dewa

