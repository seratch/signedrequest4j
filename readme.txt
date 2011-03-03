signed-request is an implementation of "OAuth Consumer Request 1.0 Draft 1".

"OAuth Consumer Request 1.0 Draft 1"
http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html

* How to install
------------

- download jar file

 ./download/*.jar

- pom.xml
 
<repositories>
  <repository>
    <id>signed-request-releases</id>
    <url>https://github.com/seratch/signed-request/raw/master/mvn-repo/releases</url>
  </repository>
  <repository>
    <id>signed-request-snapshots</id>
    <url>https://github.com/seratch/signed-request/raw/master/mvn-repo/snapshots</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.seratch</groupId>
  <artifactId>signed-request</artifactId>
  <version>1.0</version>
</dependency>

* Snippet
------------
import com.github.seratch.signedrequest.HttpResponse;
import com.github.seratch.signedrequest.OAuthConsumer;
import com.github.seratch.signedrequest.SignedRequest;
import com.github.seratch.signedrequest.SignedRequestFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Snippet {

	public static void main(String[] args) throws Exception {
		SignedRequest signedRequest = SignedRequestFactory.getInstance(
				"http://sp.example.com/", new OAuthConsumer("consumer_key",
						"consumer_secret"));
		HttpResponse response = signedRequest.doGetRequest(
				"https://github.com/seratch/signed-request", "UTF-8");
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getContent());
	}

}

// GET / HTTP/1.1
// User-Agent: Signed Request Client
// (+https://github.com/seratch/signed-request)
// Authorization: OAuth realm="http://sp.example.com/",
// oauth_consumer_key="consumer_key",oauth_signature_method="HMAC-SHA1",oauth_signature="q9eXspUbaYB6NEmAXxxXXuBrk10=",oauth_timestamp="1299157615628",oauth_nonce="-4568253211378628139",oauth_version="1.0",

* Contributors
------------
Kazuhiro Sera <seratch at gmail.com>
Kenichi Dewa

