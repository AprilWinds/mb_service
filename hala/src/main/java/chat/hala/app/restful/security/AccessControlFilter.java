package chat.hala.app.restful.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;


@Component
@Order(10)
public class AccessControlFilter implements Filter{
    private final List<WhiteRecord> WHITE_LIST = new ArrayList<>(
            Arrays.asList(new WhiteRecord("post", "/account/*"),
                          new WhiteRecord("get", "/account/*"),
                          new WhiteRecord("get", "/util/*"),
                          new WhiteRecord("post", "/was/login"),
                          new WhiteRecord("post","/online")
            ));


    private static final String STATIC_ESCAPE = "(/wa/.*)|(/wa/assets/*/.*)|(/index.html)";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        boolean shouldBypass = !(httpServletRequest.getAttribute("member") == null && httpServletRequest.getAttribute("wa") == null) || isStaticResource(getRequestedURI(httpServletRequest)) || isInWhiteList(httpServletRequest.getMethod(), getRequestedURI(httpServletRequest));
        if (shouldBypass) {
            chain.doFilter(request, response);
        } else {
            String s="{\"status\":0,\"data\":null,\"msg\":\"The token is not set\"}";
            httpServletResponse.setHeader("content-type", "application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(s);
            //setUnauthorizedResponse(httpServletResponse);
        }


    }

    private boolean isStaticResource(String uri){
        return uri.matches(STATIC_ESCAPE);
    }

    private boolean isInWhiteList(String method, String uri) {

        for (WhiteRecord record : WHITE_LIST) {
            boolean result = record.match(method, uri);
            if (result) {
                return true;
            }
        }

        return false;
    }

    private void setUnauthorizedResponse(HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.addHeader("WWW-Authenticate",
                String.format("OAuth realm=\"jsbodi\", oauth_error=\"unauthorized\""));
    }

    static String getRequestedURI(HttpServletRequest httpServletRequest) {
        StringBuilder buf = new StringBuilder();
        if (httpServletRequest.getServletPath() != null) {
            buf.append(httpServletRequest.getServletPath());
            if (httpServletRequest.getPathInfo() != null) {
                buf.append(httpServletRequest.getPathInfo());
            }
        } else {
            buf.append(httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length()));
        }
        String uri = buf.toString();
        return uri;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    private static class WhiteRecord {
        final String method;
        final String uri;

        private static final AntPathMatcher pathMatcher = new AntPathMatcher();
        private static final AntPathMatcher methodMatcher = new AntPathMatcher();

        WhiteRecord(String method, String uri) {
            super();
            this.method = method;
            this.uri = uri;
            methodMatcher.setCaseSensitive(false);
        }

        boolean match(String method, String uri) {
            return methodMatcher.match(this.method.toLowerCase(), method) && pathMatcher.match(this.uri, uri);
        }
    }

}