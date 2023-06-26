package com.simpson.userapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class AboutCtl () {

	@GetMapping("/about")
	fun getAbout() : String {
		return "Hello"
	}
}