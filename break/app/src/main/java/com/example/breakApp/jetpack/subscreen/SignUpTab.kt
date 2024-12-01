package com.example.breakApp.jetpack.subscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.MemberDtoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpTab(navController: NavController) {
    // 입력 필드 상태 변수
    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }

    //에러 변수
    var idError by remember { mutableStateOf("") }
    var pwError by remember { mutableStateOf("") }
    var confirmPwError by remember { mutableStateOf("") }

    // UI 상태 변수
    var statusMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val inputFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .height(36.dp)
        .background(Color.Gray, shape = MaterialTheme.shapes.small)
        .padding(horizontal = 16.dp, vertical = 8.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // 컬럼 내부 중앙 정렬
            verticalArrangement = Arrangement.Top
        ) {
            // 회원가입 타이틀
            Text(
                text = "회원가입", // 상단 중앙 타이틀
                color = Color.White,
                fontSize = 28.sp, // 강조를 위해 글자 크기 조정
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp) // 상단 여백 추가
            )
            }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 16.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // 상태 메시지 표시


            Text(text = "아이디", color = Color.White)
            BasicTextField(
                value = loginId,
                onValueChange = {
                    loginId = it
                    idError = if (!it.matches(Regex("^[a-zA-Z0-9]{5,20}$"))) {
                        "영문자와 숫자를 포함한 5~20자를 입력해주세요"
                    } else {
                        ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(36.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (loginId.isEmpty()) {
                            Text(
                                text = "아이디는 영문자와 숫자로 5~20자", // 플레이스홀더 텍스트
                                color = Color.LightGray, // 플레이스홀더 색상
                                fontSize = 12.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
            // 아이디 에러 메시지
            if (idError.isNotEmpty()) {
                Text(
                    text = idError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(text = "비밀번호", color = Color.White)
            BasicTextField(
                value = password,
                onValueChange = {
                    password = it
                    pwError = if (!it.matches(Regex("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}$"))) {
                        "영문, 숫자, 특수문자를 포함한 8~20자를 입력해주세요"
                    } else {
                        ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp) // 동일한 패딩
                    .height(36.dp) // 동일한 높이
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 6.dp), // 동일한 내부 패딩
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp), // 동일한 텍스트 크기
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (password.isEmpty()) {
                            Text(
                                text = "영문, 숫자, 특수문자 포함 8~20자", // 플레이스홀더 텍스트
                                color = Color.LightGray,
                                fontSize = 12.sp // 동일한 플레이스홀더 크기
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (pwError.isNotEmpty()) {
                Text(
                    text = pwError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(text = "비밀번호 확인", color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it
                    confirmPwError = if (password != it) {
                        "비밀번호가 일치하지 않습니다"
                    } else {
                        ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp) // 필드 높이 설정
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (loginId.isEmpty()) {
                            Text(
                                text = "동일한 비밀번호를 입력해주세요.", // 플레이스홀더 텍스트
                                color = Color.LightGray, // 플레이스홀더 색상
                                fontSize = 12.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (confirmPwError.isNotEmpty()) {
                Text(
                    text = confirmPwError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Text(text = "이메일", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp) // 충분한 높이를 설정
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 6.dp), // 내부 패딩 조정
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp), // 입력 텍스트 스타일
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart // 수직 중앙 정렬
                    ) {
                        if (email.isEmpty()) { // 이메일 값이 비어 있을 경우
                            Text(
                                text = "이메일은 필수로 입력해주세요", // 플레이스홀더 텍스트
                                color = Color.LightGray, // 플레이스홀더 색상
                                fontSize = 12.sp, // 플레이스홀더 크기
                                modifier = Modifier.padding(start = 0.dp) // 시작 위치 조정
                            )
                        }
                        innerTextField() // 실제 입력 필드
                    }
                }
            )
            if (statusMessage.isNotBlank()) {
                Text(
                    text = statusMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Text(text = "이메일 인증", color = Color.White, modifier = Modifier.padding(top = 8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                BasicTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    modifier = Modifier
                        .weight(6.5f)
                        .height(36.dp) // 충분한 높이 설정
                        .background(Color.Gray, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 10.dp, vertical = 6.dp), // 내부 패딩 설정
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp), // 입력 텍스트 스타일
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterStart // 플레이스홀더와 텍스트 수직 중앙 정렬
                        ) {
                            if (verificationCode.isEmpty()) { // 입력 값이 비어 있을 경우
                                Text(
                                    text = "인증 코드를 입력하세요", // 플레이스홀더 텍스트
                                    color = Color.LightGray, // 플레이스홀더 색상
                                    fontSize = 12.sp, // 플레이스홀더 크기
                                    modifier = Modifier.padding(start = 0.dp) // 시작 위치 조정
                                )
                            }
                            innerTextField() // 실제 입력 필드
                        }
                    }
                )


                Box(
                    modifier = Modifier
                        .weight(2.5f)
                        .padding(start = 8.dp)
                        .height(36.dp)
                        .background(Color(0xFFBA0000), shape = MaterialTheme.shapes.small)
                        .clickable {
                            scope.launch {
                                try {
                                    val response = RetrofitInstance.api.requestEmailVerification(email)
                                    withContext(Dispatchers.Main) {
                                        if (response.isSuccessful) {
                                            val message = response.body()
                                            statusMessage = "$message"
                                            Log.d("SignUpTab", statusMessage)
                                        } else {
                                            statusMessage = "이메일 전송 실패: ${response.errorBody()?.string()}"
                                            Log.d("SignUpTab", statusMessage)
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        statusMessage = "오류 발생: ${e.message}"
                                        Log.e("SignUpTab", statusMessage)
                                    }
                                }
                            }
                        }
                ) {
                    Text(
                        text = "전송",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Text(text = "이름", color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
            BasicTextField(
                value = userName,
                onValueChange = { userName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp) // 충분한 높이 설정
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 6.dp), // 내부 패딩 설정
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp), // 입력 텍스트 스타일
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart // 플레이스홀더와 텍스트를 수직 중앙 정렬
                    ) {
                        if (userName.isEmpty()) { // 이름 값이 비어 있을 경우
                            Text(
                                text = "이름을 입력해주세요", // 플레이스홀더 텍스트
                                color = Color.LightGray, // 플레이스홀더 색상
                                fontSize = 12.sp, // 플레이스홀더 크기
                                modifier = Modifier.padding(start = 0.dp) // 시작 위치 조정
                            )
                        }
                        innerTextField() // 실제 입력 필드
                    }
                }
            )


            Text(
                text = "성별 선택", // 텍스트 변경
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically // 수직 중앙 정렬
            ) {
                // MALE 옵션

                // MALE 옵션
                RadioButton(
                    selected = selectedGender == "MALE",
                    onClick = { selectedGender = "MALE" },
                    modifier = Modifier.wrapContentWidth(Alignment.Start) // 너비 조정
                )
                Text(
                    text = "남성",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 16.dp) // 체크박스 오른쪽 여백
                )

                // FEMALE 옵션
                RadioButton(
                    selected = selectedGender == "FEMALE",
                    onClick = { selectedGender = "FEMALE" },
                    modifier = Modifier.wrapContentWidth(Alignment.Start)
                )
                Text(
                    text = "여성",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = {
                    when {
                        email.isBlank() || verificationCode.isBlank() -> {
                            statusMessage = "이메일과 인증 코드를 입력해주세요."
                        }
                        password != confirmPassword -> {
                            statusMessage = "비밀번호가 일치하지 않습니다."
                        }
                        else -> {
                            scope.launch {
                                try {
                                    // Step 1: 이메일 인증번호 확인
                                    val verifyResponse = RetrofitInstance.api.verifyEmail(
                                        email = email,
                                        token = verificationCode
                                    )

                                    withContext(Dispatchers.Main) {
                                        if (verifyResponse.isSuccessful) {
                                            Log.d("SignUpTab", "이메일 인증 성공")

                                            // Step 2: 이메일 인증 성공 후 회원가입 진행
                                            val memberDtoRequest = MemberDtoRequest(
                                                loginId = loginId,
                                                password = password,
                                                userName = userName,
                                                email = email,
                                                gender = selectedGender
                                            )
                                            val signUpResponse = RetrofitInstance.api.signUp(memberDtoRequest)

                                            if (signUpResponse.isSuccessful) {
                                                statusMessage = "회원가입 성공"
                                                Log.d("SignUpTab", statusMessage)
                                                navController.navigate("loginTab")
                                            } else {
                                                statusMessage = "회원가입 실패: ${signUpResponse.errorBody()?.string()}"
                                                Log.d("SignUpTab", statusMessage)
                                            }
                                        } else {
                                            statusMessage = "인증번호가 올바르지 않습니다."
                                            Log.d("SignUpTab", "이메일 인증 실패: ${verifyResponse.errorBody()?.string()}")
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        statusMessage = "오류 발생: ${e.message}"
                                        Log.e("SignUpTab", statusMessage)
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "가입하기", color = Color.Black)
            }
        }
    }
}
