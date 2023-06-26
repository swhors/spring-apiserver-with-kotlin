package com.simpson.userapi.config.redis

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@ComponentScan("com.simpson.userapi")
@EnableRedisRepositories
class RedisMsgConfig : RedisConfig(){
    
    protected val dbNum = 1

    @Qualifier("msgRedisConnectionFactory")
    @Bean
    fun msgRedisConnectionFactory() : RedisConnectionFactory {
        return LettuceConnectionFactory(getRedisClusterConf(dbNum))
    }

    @Qualifier("msgRedisStringTemplate")
    @Bean
    fun msgRedisStringTemplate(
        @Qualifier("msgRedisConnectionFactory") msgRedisConnectionFactory: RedisConnectionFactory
    ) : RedisTemplate<String, String> {
        val redisTemplate = createStringRedisTemplate(msgRedisConnectionFactory)
        redisTemplate.valueSerializer = StringRedisSerializer()
        return redisTemplate
    }
    
    @Qualifier("msgRedisObjectTemplate")
    @Bean
    fun msgRedisObjectTemplate(
        @Qualifier("msgRedisConnectionFactory") msgRedisConnectionFactory: RedisConnectionFactory
    ) : RedisTemplate<String, Any> {
        val redisTemplate = createRedisTemplate(msgRedisConnectionFactory)
        redisTemplate.valueSerializer = StringRedisSerializer()
        return redisTemplate
    }
}
