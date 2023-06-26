package com.simpson.userapi.repository.redis

import java.util.Optional
import java.util.OptionalLong

interface RedisMsgRepository {
    fun getMsgId(targetId: String,targetType: String,senderId: String,defValue: Long): OptionalLong
    fun decMsgId(targetId: String,targetType: String,senderId: String): OptionalLong
    fun getLastMsgId(targetId: String,targetType: String,senderId: String): OptionalLong
    fun updateLastMsgId(targetId: String,targetType: String,senderId: String, msgId: Long): Boolean
    fun setTimeMsg(targetId: String,targetType: String,senderId: String,duration: String,timeStamp: Long): Boolean
    fun getTimeMsg(targetId: String,targetType: String,senderId: String): Optional<String>
    fun delTimeMsg(targetId: String,targetType: String,senderId: String): Boolean
}
