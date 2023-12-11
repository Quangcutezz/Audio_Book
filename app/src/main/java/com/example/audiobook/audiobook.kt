import java.io.Serializable

class audiobook(
    var id: String,
    var name: String,
    var image: String,
    var type: String,
    var author: String,
    var file:String,
    var detailPageType:String
) : Serializable {

    constructor(name: String, image: String, type: String, author: String,file: String,detailPageType: String) : this("", name, image, type, author,file,detailPageType)

    constructor() : this("", "", "", "", "","")

    fun printInfo() {
        println("ID: $id")
        println("Name: $name")
        println("Image: $image")
        println("Type: $type")
        println("Author: $author")
    }
}
