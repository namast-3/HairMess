package ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose_example.composeapp.generated.resources.Res
import compose_example.composeapp.generated.resources.board_background
import compose_example.composeapp.generated.resources.customer_ready_3
import compose_example.composeapp.generated.resources.customer_walking
import compose_example.composeapp.generated.resources.icon_cross
import compose_example.composeapp.generated.resources.scissor_l
import compose_example.composeapp.generated.resources.scissor_r
import helper.ResHelper
import kotlinx.coroutines.delay
import model.Board
import model.Customer
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import percentToDp
import ui.GameScreenViewModel.Companion.CUSTOMER_NB

const val customerWidth = 0.18
const val customerMiddleXPosition = 0.5 - customerWidth / 2
const val customerMiddleYPosition = 0.45
const val headWidth = 0.8 * customerWidth

@Composable
fun GameScreen(
    viewModel: GameScreenViewModel,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        val state = viewModel.getState().collectAsState()
        when (state.value.state) {
            Board.State.IDLE -> IdleScreen { viewModel.onStartGame() }
            Board.State.PLAYING -> PlayingScreen(viewModel, state.value)
            Board.State.GAME_OVER -> GameOverScreen(state.value){
                viewModel.onStartGame()
            }
        }
    }
}

@Composable
fun GameOverScreen(board: Board, onGameOverClick: () -> Unit) {
    BoardBackground()
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color(0xCC000000))
            .clickable {
                onGameOverClick()
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val gameOverText =  if(board.level == CUSTOMER_NB) "YOU WON !!" else "GAME OVER"
            Text(
                text = gameOverText,
                color = Color.White,
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .padding(0.dp, 0.24.percentToDp(), 0.dp, 0.dp)
            )
            val scoreText = if(board.level == CUSTOMER_NB){
                "Congratulation you cut the whole city. Thanks for playing"
            } else {
                "Score : ${board.level - 1}"
            }
            Text(
                text = scoreText,
                color = Color.White,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
            )
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(0.dp, 10.dp, 0.dp, 0.dp),
                columns = GridCells.Fixed(6)
            ) {
                items(board.doneCustomers.size) { index ->
                    val customer = board.doneCustomers[index]
                    CustomerHead(
                        customer,
                        0.dp,
                        0.dp,
                        ResHelper.getFinishedHair(customer.finishedHair)
                    )
                }
            }
        }
    }
    Lives(0)
}

@Composable
fun PlayingScreen(viewModel: GameScreenViewModel, value: Board) {
    BoardBackground()

    when (value.currentCustomer?.state) {
        Customer.State.WalkingIn -> WalkingInCustomer(viewModel)
        Customer.State.Sitting -> SittingCustomer(viewModel, value.currentCustomer)
        is Customer.State.Ready -> ReadyCustomer(
            viewModel,
            value.currentCustomer.state as Customer.State.Ready
        )

        Customer.State.Leaving -> LeavingCustomer(viewModel, value.currentCustomer)
        else -> {}
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Lives(value.lives)
        if (value.level > 1){
            Text(
                modifier = Modifier.fillMaxWidth().padding(0.dp,2.dp, 8.dp, 0.dp),
                text = "Score : ${value.level - 1}",
                color = Color.White,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.End,
            )
        }

    }
}

@Composable
fun Lives(lives: Int) {
    Row {
        repeat(3 - lives) {
            RedCross()
        }
    }
}

@Composable
fun RedCross() {
    Image(
        modifier = Modifier.width(0.05.percentToDp()),
        painter = painterResource(Res.drawable.icon_cross),
        contentDescription = null
    )
}

@Composable
fun LeavingCustomer(viewModel: GameScreenViewModel, customer: Customer) {
    val leavingDuration = 1800
    LaunchedEffect(key1 = true) {
        delay(leavingDuration.toLong())
        viewModel.onLeavingFinished()
    }

    val xPosition = remember {
        Animatable(0.7.percentToDp().value)
    }
    LaunchedEffect(key1 = true) {
        xPosition.animateTo(
            targetValue = 0.1.percentToDp().value,
            animationSpec = tween(
                durationMillis = leavingDuration,
                easing = LinearEasing
            )
        )
        viewModel.onWalkingInFinished()
    }


    val hairDrawable = remember { ResHelper.getFinishedHair(customer.finishedHair) }
    Customer(customer, xPosition.value.dp, hairDrawable)

    Scissor(viewModel)
}

@Composable
fun ReadyCustomer(viewModel: GameScreenViewModel, readyCustomer: Customer.State.Ready) {
    val xPosition = readyCustomer.positionList[readyCustomer.currentPositionIndex].percentToDp()
    val currentCustomer = viewModel.getState().value.currentCustomer ?: return
    val hairDrawable = remember { ResHelper.getInitialHair(currentCustomer.initialHair) }
    Customer(currentCustomer, xPosition, hairDrawable)

    Scissor(viewModel)
}

@Composable
fun WalkingInCustomer(viewModel: GameScreenViewModel) {
    var mirrorRotation by remember { mutableStateOf(false) }
    val xPosition = remember {
        Animatable((0.7 + customerWidth).percentToDp().value)
    }
    val yOffset = remember {
        Animatable(0.005f)
    }
    LaunchedEffect(key1 = true) {
        xPosition.animateTo(
            targetValue = customerMiddleXPosition.percentToDp().value,
            animationSpec = tween(
                durationMillis = 3500,
                easing = LinearEasing
            )
        )
        mirrorRotation = true
        viewModel.onWalkingInFinished()
    }
    LaunchedEffect(key1 = true) {
        yOffset.animateTo(
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(300, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val rotation = animateFloatAsState(
        targetValue = if (mirrorRotation) 180f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = LinearEasing,
        )
    )

    Image(
        modifier = Modifier.width(customerWidth.percentToDp())
            .offset(
                x = xPosition.value.dp,
                y = (customerMiddleYPosition + yOffset.value).percentToDp()
            )
            .graphicsLayer {
                transformOrigin = TransformOrigin(0.5f, 0.5f)
                this.rotationY = rotation.value
            },
        painter = painterResource(Res.drawable.customer_walking),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun SittingCustomer(viewModel: GameScreenViewModel, customer: Customer) {
    LaunchedEffect(key1 = true) {
        delay(1000)
        viewModel.onSittingFinished()
    }
    val xPosition = customerMiddleXPosition.percentToDp()
    val currentCustomer = viewModel.getState().value.currentCustomer ?: return
    val hairDrawable = remember { ResHelper.getInitialHair(currentCustomer.initialHair) }
    Customer(customer, xPosition, hairDrawable)
}

@Composable
private fun Customer(customer: Customer, xPosition: Dp, hairDrawable: DrawableResource) {
    Image(
        modifier = Modifier.width(customerWidth.percentToDp())
            .offset(
                x = xPosition,
                y = customerMiddleYPosition.percentToDp()
            ),
        painter = painterResource(Res.drawable.customer_ready_3),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )

    CustomerHead(
        customer,
        xPosition,
        (customerMiddleYPosition + headWidth / 8).percentToDp(),
        hairDrawable
    )
}

@Composable
fun CustomerHead(customer: Customer, xPosition: Dp, yPosition: Dp, hairDrawable: DrawableResource) {
    Image(
        modifier = Modifier.width(headWidth.percentToDp())
            .offset(
                x = (headWidth / 4).percentToDp() + xPosition,
                y = yPosition
            ),
        painter = painterResource(ResHelper.getPlayerHead(customer.id)),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
    Image(
        modifier = Modifier.width(customerWidth.percentToDp())
            .offset(
                x = xPosition + 0.02.percentToDp(),
                y = yPosition - (0.25 * headWidth).percentToDp()
            ),
        painter = painterResource(hairDrawable),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}


@Composable
fun Scissor(viewModel: GameScreenViewModel) {
    var rotated by remember { mutableStateOf(false) }
    val rotationOffset = animateFloatAsState(
        targetValue = if (rotated) -45f else 45f,
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearEasing,
        )
    ) {
        rotated = false
    }

    Image(
        modifier = Modifier.height(customerWidth.percentToDp())
            .offset(
                x = 0.75.percentToDp(),
                y = 0.3.percentToDp()
            )
            .graphicsLayer {
                transformOrigin = TransformOrigin(0.5f, 0.3f)
                this.rotationZ = rotationOffset.value
            },
        painter = painterResource(Res.drawable.scissor_r),
        contentDescription = null,
        contentScale = ContentScale.FillHeight
    )
    Image(
        modifier = Modifier.height(customerWidth.percentToDp())
            .offset(
                x = 0.75.percentToDp(),
                y = 0.3.percentToDp()
            )
            .graphicsLayer {
                transformOrigin = TransformOrigin(0.5f, 0.3f)
                this.rotationZ = -rotationOffset.value
            },
        painter = painterResource(Res.drawable.scissor_l),
        contentDescription = null,
        contentScale = ContentScale.FillHeight
    )
    Spacer(modifier = Modifier.fillMaxWidth().fillMaxHeight().clickable {
        viewModel.onScissorClick()
        if (!rotated) {
            rotated = true
        }
    })
}

@Composable
fun IdleScreen(
    onStartGame: () -> Unit
) {
    BoardBackground()

    val alpha = remember {
        Animatable(0.2f)
    }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Column(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .background(Color(0xBB000000))
        .clickable {
            onStartGame.invoke()
        }) {
        Text(
            "Press to play",
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(0.dp, 0.24.percentToDp(), 0.dp, 0.dp)
                .alpha(alpha.value)
        )

        Text(
            "Scouts are shy without their hoods. \nClick at the right time to cut their hair.",
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.fillMaxWidth()
                .padding(0.dp, 0.20.percentToDp(), 0.dp, 0.dp)
                .alpha(0.8f)
        )
    }
}

@Composable
fun BoardBackground() {
    Image(
        modifier = Modifier.fillMaxWidth(),
        painter = painterResource(Res.drawable.board_background),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}
