package com.simpson.userapi.config.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding


@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisCacheClusterProperties @ConstructorBinding constructor(
	val cluster: RedisCluster,
	val password: String
)