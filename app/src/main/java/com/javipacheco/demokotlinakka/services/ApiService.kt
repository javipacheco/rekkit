package com.javipacheco.demokotlinakka.services

import khttp.get
import katkka.*
import kategory.*

class ApiService {

    fun getDogs(): Service<ListKW<String>> = ServiceRight {
        val json = get("https://dog.ceo/api/breeds/list").jsonObject
        json.getJSONArray("message").toListKW<String>()
    }

    fun getRandomImageDog(name: String): Service<String> = ServiceRight {
        val json = get("""https://dog.ceo/api/breed/$name/images/random""").jsonObject
        json.getJSONArray("message").toString()
    }

}