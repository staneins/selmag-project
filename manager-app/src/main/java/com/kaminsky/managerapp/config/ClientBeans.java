package com.kaminsky.managerapp.config;

import com.kaminsky.managerapp.client.RestClientProductsRestClient;
import com.kaminsky.managerapp.security.OAuthClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClient restClient(@Value("${selmag.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
                                                   InMemoryClientRegistrationRepository clientRegistrationRepository,
                                                   OAuth2AuthorizedClientRepository authorizedClientRepository,
                                                   @Value("${selmag.services.catalogue.registration-id:keycloak}") String registrationId) {
        return new RestClientProductsRestClient(RestClient.builder().baseUrl(catalogueBaseUri)
                .requestInterceptor(new OAuthClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository) , registrationId))
                .build());
    }


}
