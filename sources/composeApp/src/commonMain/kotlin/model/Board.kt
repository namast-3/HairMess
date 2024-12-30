package model

data class Board(
    val state : State,
    val level:Int,
    val lives : Int,
    val currentCustomer: Customer?,
    val doneCustomers: List<Customer>,
) {
    enum class State {
        IDLE, PLAYING, GAME_OVER
    }

}