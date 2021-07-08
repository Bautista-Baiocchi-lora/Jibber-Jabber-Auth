package edu.austral.ingsis.jibberjabberauth.security;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtTokenUtil.validateToken(jwt)) {
                String username = jwtTokenUtil.getUsernameFromToken(jwt);

                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e){
            logger.error("Cannot set user authentication: {}");
        }
        chain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt")).findFirst();
            if (jwtCookie.isPresent()) {
                return jwtCookie.get().getValue();
            }
        }
        return null;
    }
}
