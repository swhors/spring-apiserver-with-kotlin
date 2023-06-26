package com.simpson.userapi.repository.redis

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository

import java.util.concurrent.TimeUnit


@Repository
@Slf4j
class RedisSessionTokenRepositoryImpl(
    @Qualifier("sessionTokenRedisTemplate") sessionTokenRedisTemplate: RedisTemplate<String, String>
): RedisSessionTokenRepository {
    final val log: Logger = LoggerFactory.getLogger(javaClass)
    
    private lateinit var valueOperations: ValueOperations<String, String>
    private lateinit var redisTemplate: RedisTemplate<String, String>
    
    init {
        redisTemplate = sessionTokenRedisTemplate
        valueOperations = sessionTokenRedisTemplate.opsForValue()
    }

    override fun getSessionTokenByAccountId(accountIdx: Long): String {
        return try {
            valueOperations.get("DeviceSessionToken.$accountIdx")!!
        } catch (e: Exception) {
            log.error(e.message)
            return ""
        }
    }
    
    override fun setSessionTokenByAccountId(accountIdx: Long, token: String): Boolean {
        return try {
            valueOperations.set("DeviceSessionToken.$accountIdx", token)
            redisTemplate.expire("DeviceSessionToken.$accountIdx", 10, TimeUnit.HOURS)
            true
        } catch (e: Exception) {
            log.warn(e.message)
            false
        }
    }
}
