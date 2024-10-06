package com.example.break_app

import kotlinx.coroutines.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseManager {
    private val jdbcUrl = "jdbc:mysql://127.0.0.1:3306/your_database_name"
    private val username = "root"
    private val password = "your_password"

    // MySQL 연결 메서드
    fun connectToDatabase(): Connection? {
        return try {
            DriverManager.getConnection(jdbcUrl, username, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    // 데이터베이스에서 사용자 정보 가져오기
    fun fetchUsers() {
        GlobalScope.launch(Dispatchers.IO) {
            val connection = connectToDatabase()
            connection?.let {
                val statement = it.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM users")

                // 데이터를 가져옴 (여기서는 콘솔에 출력)
                while (resultSet.next()) {
                    val userId = resultSet.getInt("id")
                    val userName = resultSet.getString("name")
                    println("User ID: $userId, Name: $userName")
                }

                resultSet.close()
                statement.close()
                connection.close()
            }
        }
    }
    // 활동 데이터를 데이터베이스에 저장하는 메서드
}
