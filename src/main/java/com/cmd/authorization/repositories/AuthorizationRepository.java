package com.cmd.authorization.repositories;/*
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

import java.util.Optional;

import com.cmd.authorization.model.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Steve Riesenberg
 */
@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, String> {
	Optional<Authorization> findByState(String state);
	Optional<Authorization> findByAuthorizationCode(String authorizationCode);
	Optional<Authorization> findByAccessToken(String accessToken);
	Optional<Authorization> findByRefreshToken(String refreshToken);
	@Query("select a from Authorization a where a.state = :token" +
			" or a.authorizationCode = :token" +
			" or a.accessToken = :token" +
			" or a.refreshToken = :token"
	)
	Optional<Authorization> findByStateOrAuthorizationCodeOrAccessTokenOrRefreshToken(@Param("token") String token);
}
