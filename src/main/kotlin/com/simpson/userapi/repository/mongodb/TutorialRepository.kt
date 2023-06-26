package com.simpson.userapi.repository.mongodb

import com.simpson.userapi.model.Tutorial
import org.springframework.data.mongodb.repository.MongoRepository

interface TutorialRepository : MongoRepository<Tutorial, String > {
	fun findByTitleContains(title: String): List<Tutorial>
	fun findByPublished(published: Boolean): List<Tutorial>
	fun findByTitle(title: String): List<Tutorial>
	fun findByTitleContaining(title: String): List<Tutorial>
	fun deleteAllById(id : String)
}