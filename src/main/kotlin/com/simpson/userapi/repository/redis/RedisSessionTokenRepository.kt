package com.simpson.userapi.repository.redis


interface RedisSessionTokenRepository {
    fun getSessionTokenByAccountId(accountIdx: Long): String
    fun setSessionTokenByAccountId(accountIdx: Long,token: String): Boolean
}
