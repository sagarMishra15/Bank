package com.app.Bank.jwt_config;

import com.app.Bank.dao.user.UserService;
import com.app.Bank.exception.CustomException;
import com.app.Bank.model.MyResponse;
import com.app.Bank.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@EnableWebSecurity
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException, ExpiredJwtException {

        String tokenHeader = request.getHeader("Authorization");
        String errorMsg = "";
        HttpStatus errorStatus = HttpStatus.UNAUTHORIZED;
        int statusCode = HttpServletResponse.SC_NOT_ACCEPTABLE;
        String reqUrl = MySecurityConfig.handleProUat(request.getRequestURI());

        if ("/auth/register".equals(reqUrl) || "/auth/authentication".equals(reqUrl)) {
            logger.info("Bypassing authentication for /register or /authentication");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (MySecurityConfig.isNoAuthAllowed(reqUrl)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (tokenHeader != null) {
                if (tokenHeader.startsWith("Bearer")) {
                    checkBearerAuth(tokenHeader, request);
                } else {
                    throw new CustomException("Invalid Authentication/Authorization");
                }
            } else {
                throw new CustomException("Authentication/Authorization not found");
            }
            filterChain.doFilter(request, response);
            return;
        } catch (ExpiredJwtException e) {
            errorMsg = "Token Expired";
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        } catch (CustomException e) {
            logger.info(e.getMessage(), e);
            errorStatus = e.getErrorCode();
            errorMsg = e.getMessage();
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            errorStatus = HttpStatus.BAD_REQUEST;
            errorMsg = e.getMessage();
        }
        Object res = MyResponse.newInstance(errorStatus, errorMsg);
        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(res);
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(msg);
    }


//    private void getUserAndSetContext(int id, HttpServletRequest request) throws CustomException {
//        User currentUser = null;
//        if (id > 0) {
//            currentUser = userService.findById(id);
//        }
//        if (currentUser == null) {
//            throw new CustomException("User Not Found", HttpStatus.BAD_REQUEST);
//        }
////        if (!currentUser.getStatus()) {
////            throw new CustomException("User Inactive, Please contact Admin.", HttpStatus.BAD_REQUEST);
////        }
//        if (SecurityContextHolder.getContext().getAuthentication() == null) {
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                    currentUser, null, null);
//            usernamePasswordAuthenticationToken
//                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        }
//    }


    private void checkBearerAuth(String tokenHeader, HttpServletRequest request) throws CustomException {
        logger.info("checkBearerAuth invoked, token => "+tokenHeader);
        String jwtToken = tokenHeader.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);

        User currentUser = userService.findByUsername(username);
        if (currentUser == null) {
            throw new CustomException("User Not Found", HttpStatus.BAD_REQUEST);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    currentUser, null, null);

            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {
            throw new CustomException("Invalid Token", HttpStatus.UNAUTHORIZED);
        }
    }
}
