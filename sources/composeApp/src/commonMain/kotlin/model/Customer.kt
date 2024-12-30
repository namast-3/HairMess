package model

data class Customer(
    val state: State,
    val id : Int,
    val initialHair : Int,
    val finishedHair :Int
) {
    sealed class State{
        data object WalkingIn : State()
        data object Sitting : State()
        data class Ready(
            val speed : Int,// in ms
            val positionList : List<Double>,
            val currentPositionIndex:Int
        ): State()
        data object Leaving : State()
    }
}

