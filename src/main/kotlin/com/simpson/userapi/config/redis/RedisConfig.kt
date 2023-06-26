package com.simpson.userapi.config.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisNode
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@ComponentScan("com.simpson.userapi")
@EnableRedisRepositories
class RedisConfig {
    @Autowired
    lateinit var redisCaheClusterProperties: RedisCacheClusterProperties

    fun getRedisClusterConf(dbNum : Int) : RedisConfiguration {
        return if (redisCaheClusterProperties.cluster.nodes.size > 1) {
            val redisClusterConfiguration = RedisClusterConfiguration()
            redisCaheClusterProperties.cluster.toHostInfo().forEach {
                redisClusterConfiguration.addClusterNode(RedisNode(it.host, it.port))
            }
            redisClusterConfiguration.setPassword(redisCaheClusterProperties.password)
            redisClusterConfiguration
        } else {
            val redisConfiguration = RedisStandaloneConfiguration()
            redisConfiguration.setPassword(redisCaheClusterProperties.password)
            val hostInfo = redisCaheClusterProperties.cluster.toHostInfo()
            if (hostInfo.isEmpty()) {
                redisConfiguration.hostName = "127.0.0.1"
                redisConfiguration.port = 6379
            } else {
                redisConfiguration.hostName = hostInfo[0].host
                redisConfiguration.port = hostInfo[0].port
            }
            redisConfiguration
        }
    }
    
    fun createRedisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any>{
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(connectionFactory)
        redisTemplate.keySerializer = StringRedisSerializer()
        return redisTemplate
    }
    
    fun createStringRedisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.setConnectionFactory(connectionFactory)
        redisTemplate.keySerializer = StringRedisSerializer()
        return redisTemplate
        
    }
}
