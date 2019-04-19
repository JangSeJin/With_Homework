package com.hour24.with.model


data class MarvelModel constructor(var rank: Int, // 순위
                                   var title: String, // 제목
                                   var description: String, // 내용
                                   var imageUrl: String // 이미지 주소
)