import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.GameScreen
import ui.GameScreenViewModel

@Composable
fun App(viewModel: GameScreenViewModel = viewModel { GameScreenViewModel() }) {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            GameScreen(
                viewModel,
                Modifier
                    .width(screenSize.dp)
                    .height(screenSize.dp)
            )
        }
    }
}

const val screenSize = 700
fun Double.percentToDp() = (screenSize * this).dp