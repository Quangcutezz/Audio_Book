package com.example.audiobook

import java.io.Serializable

class profile(
    var name: String="",
    var age: String="",
    var phone: String=""
): Serializable{
    override fun toString(): String {
        return "profile(name='$name', age='$age', phone='$phone')"
    }
}
