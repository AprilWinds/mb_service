package wings.app.microblog.restful.filter;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.WebAdmin;
import wings.app.microblog.service.AccountService;
import wings.app.microblog.service.WAService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            Member m= verifyUserAccessToken(accessToken);
            WebAdmin wa= simpleVerifyWAToken(accessToken);
            if (m!=null&&m.getIsActive()==0){
                String s="{\"status\":0,\"data\":null,\"msg\":\"The account was banned\"}";
                response.setHeader("content-type", "application/json;charset=UTF-8");
                response.getWriter().write(s);
                return;
            }else {
                member = m;
            }
            request.setAttribute("wa",wa);
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

        return accountService.verifyToken(token);


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