package com.example.audiobook

class audiobook {
    var id: String = ""
    var name: String = ""
    var image: String = ""
    var type: String = ""
    var author: String = ""

    constructor(name: String, image: String, type: String, author: String) {
        this.name = name
        this.image = image
        this.type = type
        this.author = author
    }
    constructor(){

    }



    fun printInfo() {
        println("ID: $id")
        println("Name: $name")
        println("Image: $image")
        println("Type: $type")
        println("Author: $author")
    }
}
