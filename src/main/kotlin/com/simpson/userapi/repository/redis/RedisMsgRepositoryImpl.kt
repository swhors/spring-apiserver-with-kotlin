package com.simpson.userapi.repository.redis

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.*


@Repository
@Slf4j
class RedisMsgRepositoryImpl(
    @Qualifier("msgRedisStringTemplate") msgRedisStringTemplate: RedisTemplate<String, String>,
    @Qualifier("msgRedisObjectTemplate") msgRedisObjectTemplate: RedisTemplate<String, Any>
) : RedisMsgRepository {
    final val log: Logger = LoggerFactory.getLogger(javaClass)
    
    protected val prefixKeyMsgIdxP: String = "P.%s.%s"
    protected val prefixKeyMsgIdxR: String = "R.%s"
    protected val prefixKeyMsgIdxG: String = "G.%s"

    protected val prefixKeyLastMsgIdxP: String = "A.P.%s.%s"
    protected val prefixKeyLastMsgIdxR: String = "A.R.%s.%s"
    protected val prefixKeyLastMsgIdxG: String = "A.G.%s.%s"

    protected val prefixKeyTimeMsgIdxP: String = "P.%s.%s.timeMsg"
    protected val prefixKeyTimeMsgIdxR: String = "R.%s.timeMsg"
    protected val prefixKeyTimeMsgIdxG: String = "G.%s.timeMsg"
    
    private lateinit var valueOperations: ValueOperations<String, Any>
    private lateinit var strValueOperations: ValueOperations<String, String>
    
    init {
        valueOperations = msgRedisObjectTemplate.opsForValue()
        strValueOperations = msgRedisStringTemplate.opsForValue()
    }

    private fun getMsgKey(targetIdx: String,targetType: String,senderIdx: String): String = when (targetType) {
        "1", "P" -> {
            if (targetIdx.toLong() < senderIdx.toLong())
                String.format(prefixKeyMsgIdxP, targetIdx, senderIdx)
            else
                String.format(prefixKeyMsgIdxP, senderIdx, targetIdx)
        }
        "2", "R" -> String.format(prefixKeyMsgIdxR, targetIdx)
        "3", "G" -> String.format(prefixKeyMsgIdxG, targetIdx)
        else -> ""
    }

    private fun getLastMsgKey(targetIdx: String,targetType: String,senderIdx: String): String {
        var key = ""
        when(targetType) {
            "1", "P" ->
                key = String.format(prefixKeyLastMsgIdxP, targetIdx, senderIdx)
            "2", "R" ->
                key = String.format(prefixKeyLastMsgIdxR, targetIdx, senderIdx)
            "3", "G" ->
                key = String.format(prefixKeyLastMsgIdxG, targetIdx, senderIdx)
        }
        return key
    }

    private fun getTimeMsgKey(targetIdx: String,targetType: String,senderIdx: String): String {
        var key = ""
        when(targetType) {
            "1", "P" ->
                key = if (Integer.parseInt(targetIdx) < Integer.parseInt(senderIdx))
                    String.format(prefixKeyTimeMsgIdxP, targetIdx, senderIdx)
                else
                    String.format(prefixKeyTimeMsgIdxP, senderIdx, targetIdx)
            "2", "R" ->
                key = String.format(prefixKeyTimeMsgIdxR, targetIdx)
            "3", "G" ->
                key = String.format(prefixKeyTimeMsgIdxG, targetIdx)
        }
        return key
    }
    
    override fun getMsgId(targetId: String, targetType: String, senderId: String, defValue: Long): OptionalLong {
        log.info("getMsgId : $targetId, $targetType, $senderId")
        val key = getMsgKey(targetId, targetType, senderId)
        return try {
            log.info("getMsgId : key = $key")
            OptionalLong.of(valueOperations.increment(key, 1)!!)
        } catch (e: Exception) {
            log.info("Exception:\n\tgetMsgId\ne.message")
            OptionalLong.empty()
        }
    }
    
    override fun decMsgId(targetId: String,targetType: String,senderId: String): OptionalLong {
        val key = getMsgKey(targetId, targetType, senderId)
        return try {
            log.info("decMsgId 0")
            val result = valueOperations.get(key)
            log.info("decMsgId 1 result = $result")
            if (valueOperations.get(key).toString() == "0")
            {
                OptionalLong.of(0L)
            } else {
                OptionalLong.of(valueOperations.decrement(key, 1)!!)
            }
        } catch (e: Exception) {
            log.error("Exception : ${e.message}")
            OptionalLong.empty()
        }
    }

    override fun getLastMsgId(targetId: String, targetType: String, senderId: String): OptionalLong {
        val key = getLastMsgKey(targetId, targetType, senderId)
        try {
            val result: Long = valueOperations.get(key) as Long
            return OptionalLong.of(result)
        } catch (e: Exception) {
            log.info(e.message)
        }
        return OptionalLong.empty()
    }
    
    override fun updateLastMsgId(targetId: String, targetType: String, senderId: String, msgId: Long): Boolean {
        val key = getLastMsgKey(targetId, targetType, senderId)
        try {
            valueOperations.set(key, msgId)
            return true
        } catch (e: Exception) {
            log.info(e.message)
        }
        return false
    }
    
    override fun setTimeMsg(
        targetId: String,
        targetType: String,
        senderId: String,
        duration: String,
        timeStamp: Long
    ): Boolean {
        val key = getTimeMsgKey(targetId, targetType, senderId)
        try {
            this.strValueOperations.set(key, duration)
            return true
        } catch (e: Exception) {
            log.info(e.message)
        }
        return false
    }
    
    override fun getTimeMsg(targetId: String, targetType: String, senderId: String): Optional<String> {
        val key = getTimeMsgKey(targetId, targetType, senderId)
        try {
            return Optional.of(strValueOperations.get(key).toString())
        } catch (e: Exception) {
            log.info(e.message)
        }
        return Optional.empty()
    }
    
    override fun delTimeMsg(targetId: String, targetType: String, senderId: String): Boolean {
        return false
    }
}
