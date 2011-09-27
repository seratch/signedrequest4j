package server;

import com.github.seratch.signedrequest4j.HttpMethod;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetHandler extends BasicHandler {

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        try {
            _handle(request.getMethod() == HttpMethod.GET.name(), HttpMethod.GET, baseRequest, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
