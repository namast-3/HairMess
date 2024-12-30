package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Board
import model.Customer
import model.Customer.State
import kotlin.random.Random

class GameScreenViewModel : ViewModel() {
    private var loopFlag = false

    private val uiState = MutableStateFlow(getInitialBoard())
    fun getState(): StateFlow<Board> = uiState.asStateFlow()

    private fun getInitialBoard(): Board {
        return Board(
            Board.State.IDLE,
            1,
            3,
            null,
            emptyList()
        )
    }

    fun onStartGame() {
        loopFlag = false
        uiState.value = getInitialBoard().copy(
            state = Board.State.PLAYING,
            currentCustomer = buildRandomCustomer()
        )
    }

    private fun buildRandomCustomer(): Customer {
        val id = buildRandomCustomerId()
        val initialHair = (1..INITIAL_HAIR_NB).random()
        val finishedHair = (1..FINISHED_HAIR_NB).random()

        return Customer(State.WalkingIn, id, initialHair, finishedHair)
    }

    private fun buildRandomCustomerId(): Int {
       val result = (1..CUSTOMER_NB).random()
        return if(uiState.value.doneCustomers.any { it.id == result }){
             buildRandomCustomerId()
        }else result
    }

    fun onWalkingInFinished() {
        viewModelScope.launch {
            delay(350)
            uiState.value = uiState.value.copy(
                state = Board.State.PLAYING,
                currentCustomer = uiState.value.currentCustomer?.copy(
                    state = State.Sitting
                )
            )
        }
    }

    fun onSittingFinished() {
        val positions = buildPositionNumber(uiState.value.level)
        uiState.value = uiState.value.copy(
            currentCustomer = uiState.value.currentCustomer?.copy(
                state = State.Ready(
                    speed = buildSpeed(uiState.value.level),
                    positionList = positions,
                    currentPositionIndex = 0
                )
            )
        )
        viewModelScope.launch {
            loopFlag = true
            startRefresh(uiState.value.currentCustomer?.state as? State.Ready ?: return@launch)
        }
    }

    private suspend fun startRefresh(customerState: State.Ready) {
        delay(customerState.speed.toLong())
        if (!loopFlag) return
        val newIndex =
            if (customerState.currentPositionIndex >= customerState.positionList.size - 1) {
                0
            } else {
                customerState.currentPositionIndex + 1
            }
        val newCustomerState = customerState.copy(
            currentPositionIndex = newIndex
        )
        withContext(Dispatchers.Main) {
            uiState.value = uiState.value.copy(
                currentCustomer = uiState.value.currentCustomer?.copy(
                    state = newCustomerState
                )
            )
        }
        withContext(Dispatchers.Default) {
            startRefresh(newCustomerState)
        }
    }

    private fun buildSpeed(level: Int): Int {
        return when (level) {
            1 -> 1000
            2 -> 800
            3 -> 600
            4 -> 500
            5 -> 400
            6 -> 300
            else -> 200
        }
    }

    private fun buildPositionNumber(level: Int): List<Double> {
        return when (level) {
            1 -> listOf(getRandomX(), getRandomX(), 0.68)
            2 -> listOf(getRandomX(), getRandomX(), 0.68)
            3 -> listOf(getRandomX(), getRandomX(), getRandomX(), 0.68)
            4 -> listOf(getRandomX(), getRandomX(), getRandomX(), 0.68)
            5 -> listOf(getRandomX(), getRandomX(), getRandomX(), getRandomX(), 0.68)
            6 -> listOf(getRandomX(), getRandomX(), getRandomX(), getRandomX(), getRandomX(), 0.68)
            7 -> listOf(getRandomX(), getRandomX(), getRandomX(), getRandomX(), getRandomX(),getRandomX(), 0.68)
            else -> listOf(getRandomX(), getRandomX(), getRandomX(), getRandomX(), getRandomX(), getRandomX(),getRandomX(),0.68)
        }
    }

    private fun getRandomX() = Random.nextDouble(0.2, 0.48)

    fun onScissorClick() {
        (uiState.value.currentCustomer?.state as? State.Ready)?.let { state ->

            if (state.positionList.last() == state.positionList[state.currentPositionIndex]) { // win
                loopFlag = false
                viewModelScope.launch {
                    delay(500)
                    uiState.value = uiState.value.copy(
                        currentCustomer = uiState.value.currentCustomer?.copy(
                            state = State.Leaving
                        ),
                    )
                }
            } else { // fail
                if(uiState.value.lives>1){
                    uiState.value = uiState.value.copy(
                        lives = uiState.value.lives-1
                    )
                }else{
                    gameOver()
                }
            }
        }
    }

    private fun gameOver() {
        uiState.value = uiState.value.copy(
            state = Board.State.GAME_OVER
        )
    }

    fun onLeavingFinished() {
        if(uiState.value.level == CUSTOMER_NB -1){
            gameOver()
        }
        uiState.value = uiState.value.copy(
            doneCustomers = uiState.value.doneCustomers + uiState.value.currentCustomer!!,
            level = uiState.value.level + 1,
            currentCustomer = buildRandomCustomer()
        )
    }

    companion object {
        const val CUSTOMER_NB = 39
        const val INITIAL_HAIR_NB = 4
        const val FINISHED_HAIR_NB = 6
    }
}