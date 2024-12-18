package com.example.Task_Manager.config;

import com.example.Task_Manager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            System.out.println("Authorization header is missing or does not start with 'Bearer'");
            filterChain.doFilter(request, response);
            return;
        }

        try{
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            System.out.println("Extracted email: " + userEmail);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(userEmail != null && authentication == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                System.out.println("UserDetails loaded: " + userDetails.getUsername());

                if(jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set for user: " + userDetails.getUsername());
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception exception){
            System.out.println("Exception during JWT processing: " + exception.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
