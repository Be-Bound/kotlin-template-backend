package com.bebound.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.logging.Logger

@SpringBootApplication
open class TemplateApplication

fun main(args: Array<String>) {
    runApplication<TemplateApplication>(*args)
    Logger.getAnonymousLogger().info("SERVER LAUNCHED")
}