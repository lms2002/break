package com.example.breakApp.exercise.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExerciseDto(
    @JsonProperty("name")
    val name: String,

    // ExerciseDto는 운동 데이터를 전달하기 위한 데이터 전송 객체(DTO)로서, REST API를 통해 전달되는 JSON 데이터를 매핑합니다.

    @JsonProperty("instructions")
    val instructions: List<String>? = null,

    // @JsonProperty("instructions"): API에서 오는 JSON 데이터의 "instructions" 필드를 instructions 변수에 매핑합니다.
    // 이 필드는 List<String> 타입의 운동 설명을 담을 수 있으며, 값이 없으면 null로 설정됩니다.

    @JsonProperty("bodyPart")  // 실제 API의 필드 이름이 맞는지 확인
    val category: String = "General",

    // @JsonProperty("bodyPart"): API에서 오는 JSON 데이터의 "bodyPart" 필드를 category 변수에 매핑합니다.
    // 기본값으로 "General"을 설정하며, 실제 API의 필드 이름이 "bodyPart"가 맞는지 확인이 필요합니다.

    @JsonProperty("target")  // 실제 API의 필드 이름이 맞는지 확인
    val targetArea: String = "Full Body"

    // @JsonProperty("target"): API에서 오는 JSON 데이터의 "target" 필드를 targetArea 변수에 매핑합니다.
    // 기본값으로 "Full Body"를 설정하며, 실제 API의 필드 이름이 "target"이 맞는지 확인이 필요합니다.
)
