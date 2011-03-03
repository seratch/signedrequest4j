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
import com.github.seratch.signedrequest.HttpMethod;
import com.github.seratch.signedrequest.OAuthConsumer;
import com.github.seratch.signedrequest.SignedRequest;
import com.github.seratch.signedrequest.SignedRequestFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Snippet {

	public static void main(String[] args) throws Exception {
		SignedRequest request = SignedRequestFactory.getInstance(null,
				new OAuthConsumer("consumer_key", "consumer_secret"));
		HttpURLConnection conn = request.getHttpURLConnection(
				"http://example.com/", HttpMethod.GET);
		InputStream is = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
		is.close();
	}

}

* Contributors
------------
Kazuhiro Sera <seratch at gmail.com>
Kenichi Dewa

