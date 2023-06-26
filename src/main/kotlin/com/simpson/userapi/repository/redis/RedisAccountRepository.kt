package com.simpson.userapi.repository.redis


interface RedisAccountRepository {
    fun appendUserToRoomIdx(roomIdx: Long,userIdx: String,createNew: Boolean): Boolean
    fun appendUserToGroupIdx(groupIdx: Long,userIdx: String,createNew: Boolean): Boolean
    fun deleteFriendsInRoom(roomIdx: Long): Boolean
    fun deleteFriendsInGroup(userIdx: Long,groupIdx: Long): Boolean
    fun getFriendsByRoomIdx(roomIdx: Long): List<String>
    fun getFriendsByGroupIdx(userIdx: Long,groupIdx: Long): List<String>
    fun setFriendsByRoomIdx(roomIdx: Long,friendIdxs: List<String>): Boolean
    fun setFriendsByGroupIdx(userIdx: Long,groupIdx: Long,friendIdxs: List<String>): Boolean
}
