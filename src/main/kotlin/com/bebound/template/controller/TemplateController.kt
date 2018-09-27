package com.bebound.template.controller

import com.bebound.template.listeners.Failure
import com.bebound.template.listeners.Response
import com.bebound.template.listeners.Success
import com.bebound.template.model.Request
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

import java.util.logging.Logger

@RestController
class TemplateController {

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun check(@RequestHeader headers: Map<String, String>, @RequestBody body: String): ResponseEntity<String> {
        // You may want to check authorization here, request should have Basic auth header
        val jsonRequestObject = JsonParser().parse(body).asJsonObject

        if (!jsonRequestObject.has("moduleName"))
            return ResponseEntity("moduleName missing", HttpStatus.BAD_REQUEST)

        if (!jsonRequestObject.has("moduleId"))
            return ResponseEntity("moduleId missing", HttpStatus.BAD_REQUEST)

        if (!jsonRequestObject.has("moduleVersion"))
            return ResponseEntity("moduleVersion missing", HttpStatus.BAD_REQUEST)

        if (!jsonRequestObject.has("operation"))
            return ResponseEntity("operation missing", HttpStatus.BAD_REQUEST)

        if (!jsonRequestObject.has("params"))
            return ResponseEntity("params missing", HttpStatus.BAD_REQUEST)

        if (!jsonRequestObject.has("transport"))
            return ResponseEntity("transport missing", HttpStatus.BAD_REQUEST)

        val request = gson.fromJson(body, Request.MapRequest::class.java)
        request.jsonParams = gson.toJson(request.parameters)

        try {
            return ResponseEntity(gson.toJson(performRequest(request)), HttpStatus.OK)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun performRequest(request: Request): Response {
        Logger.getAnonymousLogger().info("TEST: " + request.operationName)

        return when ("send_text") {
            request.operationName -> {
                Logger.getAnonymousLogger().info("PERFORM REQUEST")
                Success.Builder().withParameter(
                        "length",
                        request.parameters["content"].toString().length
                ).build()
            }
            else -> {
                Logger.getAnonymousLogger().info("ERROR REQUEST")
                Failure.Builder().withErrorStatus("Can't perform request").build()
            }
        }
    }

    companion object {
        private val gson = GsonBuilder().create()
    }
}
