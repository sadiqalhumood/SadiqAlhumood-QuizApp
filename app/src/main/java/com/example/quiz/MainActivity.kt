package com.example.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiz.ui.theme.QuizTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ){
                    Quiz()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Quiz() {
    data class Question(val q: String, val a: String)

    var qIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var quizComplete by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val questions: List<Question> = listOf(
        Question("Which animal is known as the 'Ship of the Desert'?", "Camel"),
        Question("How many letters are there in the English alphabet?", "26"),
        Question("How many days are there in a week?", "7"),
        Question("How many hours are there in a day?", "24"),
        Question("How many minutes are there in an hour?", "60")
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {


        if (!quizComplete) {
            val currentQuestion = questions[qIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Add padding here
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = currentQuestion.q,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                TextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Answer") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Button(
                    onClick = {
                        val currentQuestion = questions[qIndex]
                        val isCorrect = userAnswer.trim().equals(currentQuestion.a, ignoreCase = true)

                        coroutineScope.launch {
                            val message = if (isCorrect){
                                "Correct Answer"
                            }
                            else
                            {
                                "Wrong Answer"
                            }
                            snackbarHostState.showSnackbar(message)

                            if (isCorrect) {
                                if (qIndex < questions.size - 1) {
                                    qIndex++
                                    userAnswer = ""
                                } else {
                                    quizComplete = true
                                }
                            } else {
                                userAnswer = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Submit")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Quiz Complete!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    qIndex = 0
                    userAnswer = ""
                    quizComplete = false
                }) {
                    Text("Restart")
                }
            }
        }
    }
}