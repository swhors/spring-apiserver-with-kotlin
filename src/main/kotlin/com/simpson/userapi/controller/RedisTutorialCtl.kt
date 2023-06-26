package com.simpson.userapi.controller

import com.simpson.userapi.model.UserToken
import com.simpson.userapi.repository.redis.RedisAccountRepositoryImpl
import com.simpson.userapi.repository.redis.RedisMsgRepositoryImpl
import com.simpson.userapi.repository.redis.RedisSessionTokenRepositoryImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api/redis")
@RestController
class RedisTutorialCtl(
	redisSessionTokenRepositoryImpl: RedisSessionTokenRepositoryImpl,
	redisMsgRepositoryImpl: RedisMsgRepositoryImpl,
	redisAccountRepositoryImpl: RedisAccountRepositoryImpl
) {
	private final val log: Logger = LoggerFactory.getLogger(javaClass)
	
	private lateinit var tokenRepository: RedisSessionTokenRepositoryImpl
	private lateinit var msgRepository: RedisMsgRepositoryImpl
	private lateinit var accountRepository: RedisAccountRepositoryImpl
	
	init {
		tokenRepository = redisSessionTokenRepositoryImpl
		msgRepository = redisMsgRepositoryImpl
		accountRepository = redisAccountRepositoryImpl
	}
	
	@PostMapping("/token/put/{userId}/{token}")
	fun putToken(@PathVariable("userId") userId: Long, @PathVariable("token") token: String): ResponseEntity<HttpStatus> {
		tokenRepository.setSessionTokenByAccountId(userId, token)
		return ResponseEntity(HttpStatus.OK)
	}
	
	@GetMapping("/token/get/{userId}")
	fun getToken(@PathVariable("userId") userId: Long): ResponseEntity<UserToken> {
		return when(val token = tokenRepository.getSessionTokenByAccountId(userId)) {
			"" -> ResponseEntity(HttpStatus.NOT_FOUND)
			else -> ResponseEntity<UserToken>(UserToken(userId = userId, token = token), HttpStatus.OK)
		}
	}
	
	data class MsgID (
		val targetId: String,
		val targetType: String,
		val senderId: String,
		val msgId: Long
	)
	
	@GetMapping("/msg/inc/{targetId}/{targetType}/{senderId}")
	fun incMsg(@PathVariable("targetId") targetId: String,
	           @PathVariable("targetType") targetType: String,
	           @PathVariable("senderId") senderId: String
	): ResponseEntity<MsgID> {
		log.info("incMsg = $targetId, $targetType, $senderId")
		val msgId = msgRepository.getMsgId(targetId, targetType, senderId, 0L)
		return if (msgId.isPresent) {
			ResponseEntity<MsgID>(MsgID(targetId = targetId, targetType = targetType, senderId = senderId, msgId = msgId.asLong), HttpStatus.OK)
		} else {
			ResponseEntity(HttpStatus.NOT_FOUND)
		}
	}
	
	@GetMapping("/msg/dec/{targetId}/{targetType}/{senderId}")
	fun decMsg(@PathVariable("targetId") targetId: String,
	           @PathVariable("targetType") targetType: String,
	           @PathVariable("senderId") senderId: String
	): ResponseEntity<MsgID> {
		log.info("decMsg 0 $targetId, $targetType, $senderId")
		val msgId = msgRepository.decMsgId(targetId, targetType, senderId)
		log.info("decMsg 1 $targetId, $targetType, $senderId")
		return if (msgId.isPresent) {
			ResponseEntity<MsgID>(MsgID(targetId = targetId, targetType = targetType, senderId = senderId, msgId = msgId.asLong), HttpStatus.OK)
		} else {
			ResponseEntity(HttpStatus.NOT_FOUND)
		}
	}
}