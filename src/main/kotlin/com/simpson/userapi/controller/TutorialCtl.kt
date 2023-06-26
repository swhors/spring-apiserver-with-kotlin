package com.simpson.userapi.controller

import com.simpson.userapi.model.Tutorial
import com.simpson.userapi.repository.mongodb.TutorialRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Optional
import kotlin.collections.ArrayList
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@CrossOrigin(origins = ["http://localhost:80811"])
@RequestMapping("/api/mongodb")
@RestController
class TutorialCtl {
	@Autowired
	lateinit var tutorialRepository: TutorialRepository
	
	private final val log: Logger = LoggerFactory.getLogger(javaClass)
	
	@GetMapping("/tutorials")
	fun getAllTutorials(@RequestParam(required=false) title: String): ResponseEntity<List<Tutorial>> {
		try {
			
			val tutorials: ArrayList<Tutorial> = ArrayList()
			
			if (title.length == 0) {
				tutorialRepository.findAll().forEach(tutorials::add)
			} else {
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add)
			}
			
			if (tutorials.isEmpty()) {
				return ResponseEntity(HttpStatus.NO_CONTENT)
			}
			return ResponseEntity<List<Tutorial>>(tutorials, HttpStatus.OK)
		} catch (e: Exception) {
			log.info("Exception : %s".format(e.message))
			return ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}
	
	@GetMapping("/tutorials/{id}")
	fun getTutorialById(@PathVariable("id") id: String): ResponseEntity<Tutorial> {
		val tutorialData: Optional<Tutorial> = tutorialRepository.findById(id)
		return if (tutorialData.isPresent) {
			ResponseEntity<Tutorial>(tutorialData.get(), HttpStatus.OK)
		} else {
			ResponseEntity(HttpStatus.NOT_FOUND)
		}
	}
	
	@PostMapping("/tutorials")
	fun createTutorial(@RequestBody tutorial: Tutorial) : ResponseEntity<Tutorial>{
		return try {
			val retTutorial = tutorialRepository.save<Tutorial>(tutorial)
			ResponseEntity<Tutorial>(retTutorial, HttpStatus.CREATED)
		} catch (e: Exception) {
			ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}
	
	@PostMapping("/tutorials/{id}/{title}/{description}")
	fun createTutorialWithPath(@PathVariable id: String, @PathVariable title: String, @PathVariable description: String) : ResponseEntity<Tutorial>{
		return try {
			val newTutorial = Tutorial(id = id, title=title, description = description, published = false)
			val retTutorial = tutorialRepository.save<Tutorial>(newTutorial)
			ResponseEntity<Tutorial>(retTutorial, HttpStatus.CREATED)
		} catch (e: Exception) {
			ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}
	
	@PutMapping("/tutorials/{id}")
	fun updateTutorial(@PathVariable("id") id: String, @RequestBody tutorial: Tutorial): ResponseEntity<Tutorial> {
		val tutorialData: Optional<Tutorial> = tutorialRepository.findById(id)
		
		return if (tutorialData.isPresent) {
			val retTutorial: Tutorial = tutorialData.get()
			retTutorial.title = tutorial.title
			retTutorial.description = tutorial.description
			retTutorial.published = tutorial.published
			val ret = tutorialRepository.save(retTutorial)
			ResponseEntity<Tutorial>(ret, HttpStatus.OK)
		} else {
			ResponseEntity(HttpStatus.NOT_FOUND)
		}
	}
	
	@DeleteMapping("/tutorials/{id}")
	fun deleteTutorial(@PathVariable("id") id: String) : ResponseEntity<HttpStatus> {
		this.log.info("id = {%s}".format(id))
		val tutorialData: Optional<Tutorial> = tutorialRepository.findById(id)
		return if (tutorialData.isPresent) {
			tutorialRepository.deleteAllById(id)
			ResponseEntity(HttpStatus.OK)
		} else {
			ResponseEntity(HttpStatus.NOT_FOUND)
		}
	}
	
	@DeleteMapping("/tutorials")
	fun deleteAllTutorials(): ResponseEntity<HttpStatus> {
		return try {
			tutorialRepository.deleteAll()
			ResponseEntity(HttpStatus.OK)
		} catch (e: Exception) {
			ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}
	
	@GetMapping("/tutorials/published")
	fun findByPublished(): ResponseEntity<List<Tutorial>> {
		return try {
			log.info("0001")
			val tutorials: List<Tutorial> = tutorialRepository.findByPublished(true)
			log.info("0002")
			if (tutorials.isEmpty()) {
				log.info("0003")
				ResponseEntity(HttpStatus.NO_CONTENT)
			} else {
				log.info("0004")
				ResponseEntity<List<Tutorial>>(tutorials, HttpStatus.OK)
			}
		} catch (e: java.lang.Exception) {
			log.info("0006")
			ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}
}