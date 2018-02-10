package app.core.security;

import org.springframework.session.Session;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestOrHeaderHttpSessionStrategyAdapter implements HttpSessionStrategy {

    private static final String X_AUTH_TOKEN = "x-auth-token";
    private HeaderHttpSessionStrategy headerSessionStrategy = new HeaderHttpSessionStrategy();

    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        final String sessionId = headerSessionStrategy.getRequestedSessionId(request);

        return sessionId != null ? sessionId : sessionTokenFromRequest(request);
    }

    private String sessionTokenFromRequest(HttpServletRequest request) {
        return request.getParameter(X_AUTH_TOKEN);
    }

    @Override
    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        headerSessionStrategy.onNewSession(session, request, response);
    }

    @Override
    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        headerSessionStrategy.onInvalidateSession(request, response);
    }
}
