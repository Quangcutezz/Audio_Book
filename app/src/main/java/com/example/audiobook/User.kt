package com.example.audiobook

class User(
    val userId: String,
    var username: String,
    var email: String,
    var Tuoi: String,
    var soDT:String,
    var gioiTinh:String,
    var TheLoaiYeuThich:String,

) {
    // Các phương thức khác có thể được thêm vào đây nếu cần
    override fun toString(): String {
        return "User(userId='$userId', username='$username', email='$email')"
    }
}
