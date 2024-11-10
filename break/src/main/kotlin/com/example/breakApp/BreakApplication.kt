package com.example.breakApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BreakApplication

fun main(args: Array<String>) {
	runApplication<BreakApplication>(*args)
}