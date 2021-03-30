/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.epnutiltbl.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.compose.epnutiltbl.R
import com.example.compose.epnutiltbl.theme.progressIndicatorBackground

@Composable
fun SettingQuestionsScreen(
    questions: SettingState.Questions,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit
) {
    val questionState = remember(questions.currentQuestionIndex) {
        questions.questionsState[questions.currentQuestionIndex]
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                SettingTopAppBar(
                    questionIndex = questionState.questionIndex,
                    totalQuestionsCount = questionState.totalQuestionsCount,
                    onBackPressed = onBackPressed
                )
            },
            content = { innerPadding ->
                Question(
                    question = questionState.question,
                    answer = questionState.answer,
                    onAnswer = {
                        questionState.answer = it
                        questionState.enableNext = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            },
            bottomBar = {
                SettingBottomBar(
                    questionState = questionState,
                    onPreviousPressed = { questions.currentQuestionIndex-- },
                    onNextPressed = { questions.currentQuestionIndex++ },
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}

@Composable
fun SettingResultScreen(
    result: SettingState.Result,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            content = { innerPadding ->
                val modifier = Modifier.padding(innerPadding)
                SettingResult(result = result, modifier = modifier)
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onBackPressed
                    ) {
                        Text(text = stringResource(id = R.string.previous))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDonePressed,
                    ) {
                        Text(text = stringResource(id = R.string.done))
                    }
                }
            }
        )
    }
}

@Composable
private fun SettingResult(result: SettingState.Result, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.height(44.dp))
            Text(
                text = result.settingResult.library,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Text(
                text = stringResource(
                    result.settingResult.result,
                    result.settingResult.library
                ),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(20.dp)
            )
            Text(
                text = stringResource(result.settingResult.description),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

@Composable
private fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier
) {
    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val totalStyle = MaterialTheme.typography.caption.toSpanStyle()
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${questionIndex + 1}")
        }
        withStyle(style = totalStyle) {
            append(stringResource(R.string.question_count, totalQuestionsCount))
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
}

@Composable
private fun SettingTopAppBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onBackPressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TopAppBarTitle(
                questionIndex = questionIndex,
                totalQuestionsCount = totalQuestionsCount,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .align(Alignment.Center)
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
            }
        }
        LinearProgressIndicator(
            progress = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            backgroundColor = MaterialTheme.colors.progressIndicatorBackground
        )
    }
}

@Composable
private fun SettingBottomBar(
    questionState: QuestionState,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Surface(
        elevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (questionState.showPrevious) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onPreviousPressed
                ) {
                    Text(text = stringResource(id = R.string.previous))
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (questionState.showDone) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onDonePressed,
                    enabled = questionState.enableNext
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            } else {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onNextPressed,
                    enabled = questionState.enableNext
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
}
