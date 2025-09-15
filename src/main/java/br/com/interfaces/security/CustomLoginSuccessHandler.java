package br.com.interfaces.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_CLIENT = "ROLE_CLIENT";

    private static final Map<String, String> ROLE_REDIRECTS = Map.of(
            ROLE_ADMIN, "/admin/accounts",
            ROLE_CLIENT, "/client/operations"
    );

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String expectedRole = request.getParameter("role");
        if (expectedRole != null && !userRoles.contains("ROLE_" + expectedRole.toUpperCase())) {
            response.sendRedirect("/error?message=acesso-negado");
            return;
        }

        String redirectUrl = userRoles.stream()
                .filter(ROLE_REDIRECTS::containsKey)
                .map(ROLE_REDIRECTS::get)
                .findFirst()
                .orElse("/error?message=acesso-negado");

        response.sendRedirect(redirectUrl);
    }
}
