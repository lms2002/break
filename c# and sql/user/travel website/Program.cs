using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient; // MySQL 참조

namespace travel_website
{
    class Program
    {
            static void Main(string[] args)
            {
                try
                {
                    // MySQL 연결 명령어
                    MySqlConnection connection = new MySqlConnection("Server=localhost;Database=user_information;Uid=lms2002;Pwd=56492662002lms!");
                // SQL 서버와 연결.
                // Server = localhost : 로컬 호스트 (내 컴퓨터) 서버와 연결
                // Database = 스키마 이름
                // Uid = DB 로그인 아이디
                // Pwd = DB 로그인 비밀번호

                // MySQL 서버 연결 유지
                connection.Open();

                    // MySQL로 보낼 문자열 Query 변수 선언
                    string Query = "SELECT * FROM user_information.user WHERE id = 'lm2002';";
                    // MySqlCommand 클래스를 사용해 쿼리문을 MySQL로 전송
                    MySqlCommand command = new MySqlCommand(Query, connection);

                    // MySqlDataReader 클래스와 ExecuteReader() 함수를 이용해,
                    // 받아온 정보를 reader에 저장
                    MySqlDataReader reader = command.ExecuteReader();

                    while (reader.Read())
                    {
                    // 받아온 reader의 정보 중, email 열만 출력
                    Console.WriteLine((string)reader["email"]);
                    }
                    // MySQL 서버 연결 종료
                    connection.Close();
                }
                catch (Exception ex)
                { Console.WriteLine(ex.Message.ToString()); }
            }
        }
    }
