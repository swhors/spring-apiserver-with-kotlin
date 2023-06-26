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
class RedisSessionTokenConfig : RedisConfig(){
    
    protected val dbNum = 2

    @Qualifier("sessionTokenRedisConnectionFactory")
    @Bean
    fun sessionTokenRedisConnectionFactory() : RedisConnectionFactory {
        return LettuceConnectionFactory(getRedisClusterConf(dbNum))
    }
    
    @Qualifier("sessionTokenRedisTemplate")
    @Bean
    fun sessionTokenRedisTemplate(sessionTokenRedisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = createStringRedisTemplate(sessionTokenRedisConnectionFactory)
        template.valueSerializer = StringRedisSerializer()
        return template
    }
}
