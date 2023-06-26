package com.simpson.userapi.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Unwrapped.Empty

@Document(collection = "tutorials")
data class Tutorial(
	@field:Empty var id : String,
	@field:Empty var title: String,
	@field:Empty var description: String,
	@field:Empty var published: Boolean
)
