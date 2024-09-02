package com.example.scramblegame.ui.theme

import android.app.Activity
import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scramblegame.R


@Preview(showBackground = true)
@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()){

    val gameUiState by gameViewModel.uiState.collectAsState()

    val meduimpadding = dimensionResource(R.dimen.padding_medium)
    Column(modifier = Modifier
        .padding(meduimpadding)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.app_name),
            style = typography.titleLarge)

        GameLayout({gameViewModel.updateUserGuess(it)},
            gameViewModel.userGuess,
            onKeyBoardDone = {},
            gameUiState.currentScrambledWord,
            gameUiState.isGuessedWordWrong,
            gameUiState.currentWordCount,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(meduimpadding))

        Column(modifier = Modifier.padding(meduimpadding),
            verticalArrangement = Arrangement.spacedBy(meduimpadding),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { gameViewModel.checkUserGuess() }) {
                Text(text = stringResource(id = R.string.submit),
                    fontSize = 16.sp)
            }
            OutlinedButton(onClick = { gameViewModel.skipWord() }) {
                Text(text = stringResource(id = R.string.skip),
                    fontSize = 16.sp)
            }
        }
        GameStatus(score = gameUiState.score, modifier =Modifier.padding(20.dp) )
    }
    if(gameUiState.isGameOver){
        FinalScoreDialogbox(score = gameUiState.score, onPlayAgain ={ gameViewModel.resetGame()}, modifier = Modifier.padding(10.dp))
    }
}
@Composable
fun GameStatus(score : Int,modifier: Modifier){
    Card(modifier){
        Text(text = stringResource(id = R.string.score,score),
            modifier=Modifier.padding(8.dp))
    }
}


@Composable
fun GameLayout(onUserGuessChanged: (String) -> Unit,
               userGuess : String,
               onKeyBoardDone : ()->Unit,
               currentScrambledword: String,
               isGuessWrong : Boolean,
               wordCount : Int,
               modifier: Modifier){
    Card(modifier=modifier) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(Alignment.End),
                text = stringResource(id = R.string.word_count,wordCount),
                color = colorScheme.onPrimary
            )
            Text(text = currentScrambledword,
                style = typography.displayMedium)
            Text(
                text = stringResource(id = R.string.instructions),
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )

            OutlinedTextField(
                value = userGuess,
                isError = isGuessWrong,
                onValueChange = onUserGuessChanged,
                shape = shapes.large,
                label = {
                    if(isGuessWrong){
                        Text(text = stringResource(id = R.string.wrong_guess))
                    }else{
                        Text(stringResource(id = R.string.enter_your_word))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp, vertical = 20.dp),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyBoardDone()}
                )
            )

        }

    }
}

@Composable
private fun FinalScoreDialogbox(
    score: Int, modifier: Modifier,
    onPlayAgain: () -> Unit){
    val activity = (LocalContext.current as Activity)
    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = stringResource(id = R.string.congratulations))},
        text = { Text(text = stringResource(id = R.string.you_scored,score))},
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = {
                activity.finish()
            }) {
                Text(text = stringResource(id = R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(id = R.string.play_again))
            }
        })
}


