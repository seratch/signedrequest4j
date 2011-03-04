# Signed Request for Java

## What's this?

Signed Request for Java is an implementation of OAuth Consumer Request 1.0 Draft 1.

* OAuth Consumer Request 1.0 Draft 1

    http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html

## How to install

### Download

    ./download/signed-request-1.0-SNAPSHOT.jar

### via Maven

    <repositories>
      ...
      <repository>
        <id>signed-request-releases</id>
        <url>https://github.com/seratch/signed-request/raw/master/mvn-repo/releases</url>
      </repository>
      <repository>
        <id>signed-request-snapshots</id>
        <url>https://github.com/seratch/signed-request/raw/master/mvn-repo/snapshots</url>
      </repository>
      ...
    </repositories>

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>signed-request</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

## How to use

### Snippet: Simple GET Request

    import com.github.seratch.signedrequest.HttpResponse;
    import com.github.seratch.signedrequest.OAuthConsumer;
    import com.github.seratch.signedrequest.SignedRequest;
    import com.github.seratch.signedrequest.SignedRequestFactory;
    
    public class SimpleGETRequestSnippet {
        public static void main(String[] args) throws Exception {
            SignedRequest signedRequest = SignedRequestFactory.getInstance(
                    "http://sp.example.com/",
                    new OAuthConsumer("consumer_key", "consumer_secret"));
            HttpResponse response = signedRequest.doGetRequest(
                    "https://github.com/seratch/signed-request", 
                    "UTF-8");
            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders());
            System.out.println(response.getContent());
        }
    }

### Snippet: Simple POST Request

    import com.github.seratch.signedrequest.HttpResponse;
    import com.github.seratch.signedrequest.OAuthConsumer;
    import com.github.seratch.signedrequest.SignedRequest;
    import com.github.seratch.signedrequest.SignedRequestFactory;
    
    import java.util.HashMap;
    import java.util.Map;
    
    public class SimplePOSTRequestSnippet {
        public static void main(String[] args) throws Exception {
            SignedRequest signedRequest = SignedRequestFactory.getInstance(
                    "http://sp.example.com/", 
                    new OAuthConsumer("consumer_key", "consumer_secret"));
            Map<String, Object> requestParameters = new HashMap<String, Object>();
            requestParameters.put("something", "updated");
            HttpResponse response = signedRequest.doPostRequest(
                    "https://github.com/seratch/signed-request", 
                    requestParameters,
                    "UTF-8");
            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders());
            System.out.println(response.getContent());
        }
    }

### Snippet: Signature with Additional Parameters

    import com.github.seratch.signedrequest.HttpResponse;
    import com.github.seratch.signedrequest.OAuthConsumer;
    import com.github.seratch.signedrequest.SignedRequest;
    import com.github.seratch.signedrequest.SignedRequestFactory;
    
    import java.util.HashMap;
    import java.util.Map;
    
    public class SignatureWithAdditionalParamsSnippet {
        public static void main(String[] args) throws Exception {
            Map<String, Object> additionalParams = new HashMap<String, Object>();
            additionalParams.put("xoauth_requestor_id", "user@example.com");
            SignedRequest signedRequest = SignedRequestFactory.getInstance(
                    "http://sp.example.com/", 
                    new OAuthConsumer("consumer_key", "consumer_secret"), 
                    additionalParams);
            HttpResponse response = signedRequest.doGetRequest(
                    "http://sp.example.com/api/?aaa=bbb", 
                    "UTF-8");
            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders());
            System.out.println(response.getContent());
        }
    }

## Contributors

* Kazuhiro Sera &lt;seratch at gmail.com&gt;
* Kenichi Dewa

