package com.simpson.userapi.config.redis

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@ComponentScan("com.simpson.userapi")
@EnableRedisRepositories
class RedisAccountConfig : RedisConfig(){
    protected val dbNum = 3
    
    @Primary
    @Qualifier("accountRedisConnectionFactory")
    @Bean
    fun accountRedisConnectionFactory() : RedisConnectionFactory {
        return LettuceConnectionFactory(getRedisClusterConf(dbNum))
    }
    
    @Qualifier("accountRedisTemplate")
    @Bean
    fun accountRedisTemplate(
        @Qualifier("accountRedisConnectionFactory") accountRedisConnectionFactory: RedisConnectionFactory
    ) : RedisTemplate<String, Any> {
        val template = createRedisTemplate(accountRedisConnectionFactory)
        template.valueSerializer = StringRedisSerializer()
        return template
    }
}
