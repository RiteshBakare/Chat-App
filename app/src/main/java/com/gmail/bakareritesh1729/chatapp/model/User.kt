package com.gmail.bakareritesh1729.chatapp.model

data class User(val userId: String = "", val userName: String = "", val profileImage: String = "") {
    constructor(map: Map<String, String>) : this(
        userId = map["userId"] ?: "",
        userName = map["userName"] ?: "",
        profileImage = map["userImage"] ?: ""
    )
}

