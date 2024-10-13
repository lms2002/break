package com.project.break_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BreakApplication
// 주석
fun main(args: Array<String>) {
	runApplication<BreakApplication>(*args)
}
