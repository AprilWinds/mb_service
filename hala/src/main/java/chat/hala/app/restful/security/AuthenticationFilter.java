package chat.hala.app.restful.security;

import chat.hala.app.entity.Member;
import chat.hala.app.entity.WebAdmin;
import chat.hala.app.service.AccountService;
import chat.hala.app.service.WAService;
import chat.hala.app.service.exception.InvalidCredentialException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.common.base.Strings;

@Component
@Order(0)
public class AuthenticationFilter implements Filter {

    @Autowired
    private AccountService accountService;

    @Autowired
    private WAService was;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String accessToken = getAccessToken(request);
        Member member = null;


        if (!Strings.isNullOrEmpty(accessToken)) {

            member = verifyUserAccessToken(accessToken);
            if (member == null && !((HttpServletRequest) req).getRequestURI().contains("was")) {
                sendInvalidAccessTokenResponse(request, response);
                return;
            }
            if (member == null && ((HttpServletRequest) req).getRequestURI().contains("was")) {
                WebAdmin wa = simpleVerifyWAToken(accessToken);
                if(wa == null){
                    sendInvalidAccessTokenResponse(request, response);
                    return;
                }
                request.setAttribute("wa", wa);
            }
        }
        request.setAttribute("member", member);
        filterChain.doFilter(request, response);
    }


    private void sendInvalidAccessTokenResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s="{\"status\":0,\"data\":null,\"msg\":\"token invalid\"}";
        response.setHeader("content-type", "application/json;charset=UTF-8");
        response.getWriter().write(s);
    }

    //edit try to simplify duplicate code
    private Member verifyUserAccessToken(String accessToken) {
        int firstSpaceIndex = accessToken.indexOf(" ");

        if (firstSpaceIndex == -1) {
            return null;
        }

        String authMethod = accessToken.substring(0, firstSpaceIndex).trim();
        String token = accessToken.substring(firstSpaceIndex).trim();

        if(!authMethod.equalsIgnoreCase("Bearer") || Strings.isNullOrEmpty(token)) {
            return null;
        }

        try{
            return accountService.verifyToken(token);
        }
        catch(InvalidCredentialException e) {
            return null;
        }
    }

    private WebAdmin simpleVerifyWAToken(String accessToken){
        int firstSpaceIndex = accessToken.indexOf(" ");
        String token = accessToken.substring(firstSpaceIndex).trim();
        if(Strings.isNullOrEmpty(token) || token.equals("undefined")) return null;
        return was.validateToken(token);
    }

    private String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(headerValue)) {
            return headerValue;
        }
        else {
            return null;
        }
    }
}