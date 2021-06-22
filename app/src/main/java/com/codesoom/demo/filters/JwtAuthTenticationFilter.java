package com.codesoom.demo.filters;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.domain.Role;
import com.codesoom.demo.security.UserAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthTenticationFilter extends BasicAuthenticationFilter {
    private final AuthenticationService authenticationService;

    public JwtAuthTenticationFilter(AuthenticationManager authenticationManager, AuthenticationService authenticationService) {
        super(authenticationManager);
        this.authenticationService = authenticationService;
    }

    //prehandle이랑 비슷
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);
        // TODO : 이부분 삭제
//        if (filterWithPathAndMethod(request)) {
//            chain.doFilter(request, response);
//            return;
//        }

        //TODO : authentication
        String authorization = request.getHeader("Authorization");

        if (authorization != null) {
            String accessToken = authorization.substring("Bearer ".length());
            Long userId = authenticationService.parseToken(accessToken);
            List<Role> roles = authenticationService.roles(userId);
            //TODO : USER ID를 넘겨주자 -> authetication
            Authentication authentication = new UserAuthentication(userId, roles);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }


        //다음 필터로 넘겨줌
        chain.doFilter(request, response);



    }


    private boolean filterWithPathAndMethod(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (!path.equals("/products")) {
            return true;
        }
        String method = request.getMethod();

        if (method.equals("GET")) {
            return true;
        }
        return false;
    }

}
