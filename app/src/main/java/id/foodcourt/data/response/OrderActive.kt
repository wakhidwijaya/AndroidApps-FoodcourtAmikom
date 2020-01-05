package id.foodcourt.data.response

data class OrderActive (
    val name : String ="",
    val total_price : Int = 0,
    val status : Boolean = false,
    val time : String = ""
)