package no.fintlabs;

import no.vigoiks.resourceserver.security.FintJwtUserConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Value("${fint.integration.service.authorized-org-id:vigo.no}")
    private String authorizedOrgId;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/**")
                        .hasAnyAuthority("ORGID_" + authorizedOrgId)
                        .anyExchange()
                        .authenticated())
                .addFilterBefore(new AuthorizationLogFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt()
                        .jwtAuthenticationConverter(new FintJwtUserConverter()));
        return http.build();
    }
}