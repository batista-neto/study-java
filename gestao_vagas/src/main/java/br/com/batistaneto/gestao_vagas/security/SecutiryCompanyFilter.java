package br.com.batistaneto.gestao_vagas.security;

import br.com.batistaneto.gestao_vagas.providers.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecutiryCompanyFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if(request.getRequestURI().startsWith("/company")) {
            SecurityContextHolder.getContext().setAuthentication(null);
            String header = request.getHeader("Authorization");
            if(header != null) {
                var token = jwtProvider.validateToken(header);
                if (token == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                request.setAttribute("company_id", token.getSubject());
                var roles = token.getClaim("roles").asList(Object.class);
                var grants = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.toString().toUpperCase()))
                        .toList();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        token.getSubject(), null, grants);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
