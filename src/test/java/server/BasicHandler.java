package server;

import com.github.seratch.signedrequest4j.HttpMethod;
import com.github.seratch.signedrequest4j.SignatureMethod;
import com.github.seratch.signedrequest4j.SignedRequestVerifier;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BasicHandler extends AbstractHandler {

	public void _handle(Boolean isAllowed,
	                    HttpMethod method,
	                    Request baseRequest,
	                    HttpServletRequest request,
	                    HttpServletResponse response) throws Exception {

		boolean isValid = false;
		if (method.equals(HttpMethod.POST)) {
			isValid = SignedRequestVerifier.verifyPOST(
					"http://localhost:8888/",
					request.getHeader("Authorization"),
					HttpServerSpec.SINGLETON_CONSUMER,
					SignatureMethod.HMAC_SHA1,
					request.getParameterMap()
			);
		} else {
			isValid = SignedRequestVerifier.verify(
					"http://localhost:8888/",
					request.getHeader("Authorization"),
					HttpServerSpec.SINGLETON_CONSUMER,
					method,
					SignatureMethod.HMAC_SHA1
			);
		}
		if (isAllowed && isValid) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		response.getWriter().print("");
		baseRequest.setHandled(true);
	}

}
