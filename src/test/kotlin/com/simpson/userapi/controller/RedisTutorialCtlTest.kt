package com.simpson.userapi.controller

import com.simpson.userapi.repository.redis.RedisAccountRepositoryImpl
import com.simpson.userapi.repository.redis.RedisMsgRepositoryImpl
import com.simpson.userapi.repository.redis.RedisSessionTokenRepositoryImpl
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.mockito.Mockito
import org.mockito.ArgumentMatchers.*
import org.springframework.test.context.ActiveProfiles
import java.util.*


@WebMvcTest(RedisTutorialCtl::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("local")
class RedisTutorialCtlTest {

	@MockBean
	lateinit var tokenRepository: RedisSessionTokenRepositoryImpl
	
	@MockBean
	lateinit var msgRepository: RedisMsgRepositoryImpl
	
	@MockBean
	lateinit var accountRepository: RedisAccountRepositoryImpl
	
	@Autowired
	lateinit var mvc: MockMvc

	@Test
	@Order(1)
	fun testIncMsg(){
		
		// given
		val targetId = "1111"
		val targetType = 'G'
		val senderId = "1112"
		Mockito.`when`(msgRepository.getMsgId(anyString(), anyString(), anyString(), anyLong())).thenReturn(OptionalLong.of(1L))
		
		// result
		mvc.perform(
			MockMvcRequestBuilders.get("/api/redis/msg/inc/$targetId/$targetType/$senderId").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("$.msgId").exists())
	}
	
	@Test
	@Order(2)
	fun testDecMsg() {
		// given
		val targetId = "1111"
		val targetType = 'G'
		val senderId = "1112"
		Mockito.`when`(msgRepository.decMsgId(anyString(), anyString(), anyString())).thenReturn(OptionalLong.of(0L))
		
		// result
		mvc.perform(
			MockMvcRequestBuilders.get("/api/redis/msg/dec/$targetId/$targetType/$senderId").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("$.msgId").exists())
		
	}
}