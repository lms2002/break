package com.example.breakApp.common.status

enum class Gender(val desc:String) {
    MALE("남"),
    FEMALE("여")
}

enum class ResultCode(val msg:String){
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다."),
    INBODY_CREATED("인바디 데이터가 성공적으로 생성되었습니다."),
    INBODY_RETRIEVED("인바디 데이터를 성공적으로 조회했습니다."),
    INBODY_UPDATED("인바디 데이터가 성공적으로 수정되었습니다."),
    INBODY_DELETED("인바디 데이터가 성공적으로 삭제되었습니다.")
}

enum class ROLE {
    MEMBER
}