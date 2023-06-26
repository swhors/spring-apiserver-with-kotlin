package com.simpson.userapi.config.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding


@ConfigurationProperties(prefix = "spring.data.redis.cluster")
data class RedisCluster @ConstructorBinding constructor (
	val nodes: List<String>
) {
	data class HostInfo (
		val host: String,
		val port: Int
	) {
		override fun toString(): String {
			return "node %s:%s".format(host, port)
		}
	}
	fun toHostInfo() : List<HostInfo> {
		val hostInfos = ArrayList<HostInfo>()
		if(nodes.isNotEmpty()) {
			nodes.forEach {
				val infos: List<String> = it.split(":")
				if (infos.size == 2) {
					hostInfos.add(HostInfo(infos[0], infos[1].toInt()))
				}
			}
		}
		return hostInfos
	}
}
