package com.simpson.userapi.controller

import com.simpson.userapi.model.Tutorial
import com.simpson.userapi.repository.mongodb.TutorialRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.bind.annotation.*
import java.util.*


@WebMvcTest(TutorialCtl::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("local")
class TutorialCtlTest {
	
	@MockBean
	lateinit var tutorialRepository: TutorialRepository
	
	@Autowired
	lateinit var mvc: MockMvc
	
	@Autowired
	lateinit var objectMapper: ObjectMapper
	
	private val findData: MutableList<Tutorial> = ArrayList<Tutorial>()
	
	@BeforeEach
	fun setupAllTutorials() {
		findData.clear()
		findData.add(Tutorial("G001", "Simpson", "Test Book 01", true))
		findData.add(Tutorial("G002", "SpiderMan", "Test Book 02", false))
		Mockito.`when`(tutorialRepository.findAll()).thenReturn(findData)
	}
	
	@Test
	@Order(1)
	fun testGetAllTutorials() {
		// given
		val expectByTitle = "$.[?(@.title == '%s')]"
		val title = "Simpson"
		
		mvc.perform(
			MockMvcRequestBuilders.get("/api/mongodb/tutorials").param("title", "").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.jsonPath(expectByTitle, title).exists())
	}
	
	@BeforeEach
	fun setupAllTutorialsWithParam() {
		findData.clear()
		findData.add(Tutorial("G001", "Simpson", "Test Book 01", true))
		Mockito.`when`(tutorialRepository.findByTitleContaining(anyString())).thenReturn(findData)
	}
	
	@Test
	@Order(2)
	fun testGetAllTutorialsWithParam() {
		// given
		val expectByTitle = "$.[?(@.title == '%s')]"
		val title = "Simpson"
		
		// Test
		mvc.perform(
			MockMvcRequestBuilders.get("/api/mongodb/tutorials").param("title", title).accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.jsonPath(expectByTitle, title).exists())
	}
	
	@BeforeEach
	fun setupGetTutorialById() {
		val tutorial = Tutorial("G001", "Simpson", "Test Book 01", true)
		Mockito.`when`(tutorialRepository.findById(anyString())).thenReturn(Optional.of(tutorial))
	}

	@Test
	@Order(3)
	fun testGetTutorialById() {
		// Given
		val expectById = "$.[?(@.id == '%s')]"
		val id = "G001"
		
		// Test
		mvc.perform(
			MockMvcRequestBuilders.get("/api/mongodb/tutorials/$id").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.jsonPath(expectById, id).exists())
	}
	
	@BeforeEach
	fun setupCreateTutorial() {
		val tutorial = Tutorial("G001", "Simpson", "Test Book 01", true)
		Mockito.`when`(tutorialRepository.save(any())).thenReturn(tutorial)
	}

	@Test
	@Order(4)
	fun testCreateTutorial() {
		// Given
		val expectById = "$.[?(@.id == '%s')]"
		val id = "G001"
		val title = "simpson"
		val description = "Test Book 01"
		
		// Test
		mvc.perform(
			MockMvcRequestBuilders.post("/api/mongodb/tutorials/$id/$title/$description").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.jsonPath(expectById, id).exists())
	}
	
	@BeforeEach
	fun setuptUpdateTutorial() {
		val tutorial = Tutorial("G001", "Simpson", "Test Book 01", true)
		Mockito.`when`(tutorialRepository.findById(anyString())).thenReturn(Optional.of(tutorial))
		Mockito.`when`(tutorialRepository.save(any())).thenReturn(tutorial)
	}

	@Test
	@Order(5)
	fun testUpdateTutorial() {
		// given
		val newTitle = "The Simpson"
		val id = "G001"
		val published = false
		val description = "Not published"
		
		val content: String = objectMapper.writeValueAsString(Tutorial(id, newTitle, description, published))
		
		// Test
		mvc.perform(
			MockMvcRequestBuilders.put("/api/mongodb/tutorials/$id").
					content(content).
					contentType(MediaType.APPLICATION_JSON).
					accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
		
	}
	
	@BeforeEach
	fun setuptDeleteTutorial() {
		val tutorial = Tutorial("G001", "Simpson", "Test Book 01", true)
		Mockito.`when`(tutorialRepository.findById(anyString())).thenReturn(Optional.of(tutorial))
	}

	@Test
	@Order(6)
	fun testDeleteTutorial() {
		// given
		val id = "G001"

		// test
		mvc.perform(
			MockMvcRequestBuilders.delete("/api/mongodb/tutorials/$id"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk)
	}

	@BeforeEach
	fun setuptDeleteAllTutorial() {
	
	}

	@Test
	@Order(7)
	fun testDeleteAllTutorials() {
		mvc.perform(
			MockMvcRequestBuilders.delete("/api/mongodb/tutorials"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk)
		
	}
	
	@BeforeEach
	fun setuptFindByPublished() {
		findData.clear()
		findData.add(Tutorial("G001", "Simpson", "Test Book 01", true))
		findData.add(Tutorial("G002", "SpiderMan", "Test Book 02", true))
		Mockito.`when`(tutorialRepository.findByPublished(anyBoolean())).thenReturn(findData)
	}
	
	@Test
	@Order(8)
	fun testFindByPublished() {
		// test
		mvc.perform(
			MockMvcRequestBuilders.get("/api/mongodb/tutorials/published"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("$.*").isArray())
	}
}