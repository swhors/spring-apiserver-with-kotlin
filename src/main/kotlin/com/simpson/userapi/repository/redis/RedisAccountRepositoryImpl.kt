package com.simpson.userapi.repository.redis

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.stereotype.Repository

@Repository
@Slf4j
class RedisAccountRepositoryImpl(
	@Qualifier("accountRedisTemplate") accountRedisTemplate: RedisTemplate<String, Any>
) : RedisAccountRepository {
	private final val log: Logger = LoggerFactory.getLogger(javaClass)
	
	private val formatKeyRoom = "room:%d:join"
	private val formatKeyGroup = "group:%d:%d:join"
	
	private lateinit var redisTemplate: RedisTemplate<String, Any>
	private lateinit var setOperations: SetOperations<String, Any>

	init {
		redisTemplate = accountRedisTemplate
		setOperations = redisTemplate.opsForSet()
	}
	
	private fun appendValueByKey(key: String, value: String, createNew: Boolean): Boolean {
		val idxs = getListValueByKey(key)
		if (idxs.isEmpty()) {
			return if (createNew) {
				try {
					setValueByKey(key, value)
					true
				} catch (e: Exception) {
					log.error(e.message)
					false
				}
			} else true
		} else {
			if (!idxs.contains(value)) {
				return try {
					idxs.add(value)
					return setListValueByKey(key, idxs)
				} catch (e: Exception) {
					log.error(e.message)
					false
				}
			}
			return true
		}
	}
	
	private fun deleteValueByKey(key: String): Boolean {
		return try {
			redisTemplate.delete(key)
			true
		} catch (e: Exception) {
			log.error(e.message)
			false
		}
	}
	
	private fun getListValueByKey(key: String): ArrayList<String> {
		val values = ArrayList<String>()
		return try {
			setOperations.members(key)?.forEach{
				values.add(it.toString())
			}
			values
		} catch (e: Exception) {
			log.error(e.message)
			values
		}
	}
	
	private fun setValueByKey(key: String, value: String): Boolean {
		return try {
			setOperations.add(key, value)
			true
		} catch (e: Exception) {
			log.error(e.message)
			false
		}
	}
	
	private fun setListValueByKey(key: String, values: List<String>): Boolean {
		return try {
			for (value in values) setOperations.add(key, value)
			 true
		} catch (e: Exception) {
			log.error(e.message)
			return false
		}
	}
	
	
	override fun appendUserToRoomIdx(roomIdx: Long, userIdx: String, createNew: Boolean): Boolean {
		val key = String.format(formatKeyRoom, roomIdx)
		return appendValueByKey(key, userIdx, createNew)
	}
	
	override fun appendUserToGroupIdx(groupIdx: Long, userIdx: String, createNew: Boolean): Boolean {
		val key = String.format(formatKeyGroup, userIdx.toLong(), groupIdx)
		return appendValueByKey(key, userIdx, createNew)	}
	
	override fun deleteFriendsInRoom(roomIdx: Long): Boolean {
		return deleteValueByKey(String.format(formatKeyRoom, roomIdx))
	}
	
	override fun deleteFriendsInGroup(userIdx: Long, groupIdx: Long): Boolean {
		return deleteValueByKey(String.format(formatKeyGroup, userIdx, groupIdx))
	}
	
	override fun getFriendsByRoomIdx(roomIdx: Long): List<String> {
		return getListValueByKey(String.format(formatKeyRoom, roomIdx))
	}
	
	override fun getFriendsByGroupIdx(userIdx: Long, groupIdx: Long): List<String> {
		return getListValueByKey(String.format(formatKeyGroup, userIdx, groupIdx))
	}
	
	override fun setFriendsByRoomIdx(roomIdx: Long, friendIdxs: List<String>): Boolean {
		return setListValueByKey(String.format(formatKeyRoom, roomIdx), friendIdxs)
	}
	
	override fun setFriendsByGroupIdx(userIdx: Long, groupIdx: Long, friendIdxs: List<String>): Boolean {
		return setListValueByKey(String.format(formatKeyGroup, userIdx, groupIdx), friendIdxs)
	}
}