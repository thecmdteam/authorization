/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cmd.authorization.config;

import com.cmd.authorization.jose.Jwks;
import com.cmd.authorization.model.CmdClient;
import com.cmd.authorization.repositories.CmdClientRepository;
import com.cmd.authorization.security.CmdRegisteredClientRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

	@Value("${baseUrl}")
	private String baseUrl;

	@Autowired
	private CmdClientRepository clientRepository;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.formLogin(Customizer.withDefaults())
				.build();
	}

	// @formatter:off
	@Bean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
		var webClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("web-client")
				.clientSecret(passwordEncoder.encode("secret"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/messaging-client-oidc")
				.redirectUri("http://127.0.0.1:8000/authorized")
				.scope(OidcScopes.OPENID)
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				.build();

		var androidClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("android-client")
				.clientSecret(passwordEncoder.encode("secret"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope(OidcScopes.OPENID)
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				.tokenSettings(TokenSettings.builder()
						.reuseRefreshTokens(true)
						.build())
				.build();

		var clientRepo = new CmdRegisteredClientRepository();
		clientRepo.save(webClient);
		clientRepo.save(androidClient);

		return clientRepo;
	}
	// @formatter:on



	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder().issuer(baseUrl).build();
	}


	@Autowired
	public void setMapKeyDotReplacement(MappingMongoConverter mongoConverter) {
		mongoConverter.setMapKeyDotReplacement("#");
	}

}
