package com.acmeair.morphia.repositories;

import org.springframework.context.annotation.Profile;

@Profile("jpa")
interface CustomerSessionJpaRepository extends CustomerSessionRepository {
}
