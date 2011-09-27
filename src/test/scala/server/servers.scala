package server

import org.eclipse.jetty.server.{Request, Handler, Server}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.server.handler.AbstractHandler
import com.github.seratch.signedrequest4j.HttpMethod
import com.github.seratch.signedrequest4j.{SignatureMethod, OAuthConsumer, SignedRequestVerifier}

abstract class BasicHandler extends AbstractHandler {

  def _handle(isAllowed: Boolean,
              method: HttpMethod,
              baseRequest: Request,
              request: HttpServletRequest,
              response: HttpServletResponse): Unit = {
    if (isAllowed) {
      val isValid = SignedRequestVerifier.verify(
        "http://localhost:8888/",
        request.getHeader("Authorization"),
        HttpServerSpec.SINGLETON_CONSUMER,
        method,
        SignatureMethod.HMAC_SHA1
      )
      response.setStatus(HttpServletResponse.SC_OK)
    } else {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN)
    }
    response.getWriter.print("")
    baseRequest.setHandled(true)
  }
}

class GetHandler extends BasicHandler {
  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    _handle(request.getMethod == HttpMethod.GET.name(), HttpMethod.GET, baseRequest, request, response)
  }
}

class PostHandler extends BasicHandler {
  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    _handle(request.getMethod == HttpMethod.POST.name(), HttpMethod.POST, baseRequest, request, response)
  }
}

class PutHandler extends BasicHandler {
  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    _handle(request.getMethod == HttpMethod.PUT.name(), HttpMethod.PUT, baseRequest, request, response)
  }
}

class DeleteHandler extends BasicHandler {
  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    _handle(request.getMethod == HttpMethod.DELETE.name(), HttpMethod.DELETE, baseRequest, request, response)
  }
}

class OptionsHandler extends BasicHandler {
  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    _handle(request.getMethod == HttpMethod.OPTIONS.name(), HttpMethod.OPTIONS, baseRequest, request, response)
  }
}

class TraceHandler extends BasicHandler {
  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    _handle(request.getMethod == HttpMethod.TRACE.name(), HttpMethod.TRACE, baseRequest, request, response)
  }
}

object HttpServerSpec {
  val SINGLETON_CONSUMER = new OAuthConsumer("sdfsa", "sdfafa33333")
}

case class HttpServer(handler: Handler) {

  val server = new Server(8888)
  server.setHandler(handler)

  def start() {
    server.start()
    server.join()
  }

  def stop() {
    server.stop()
  }

}