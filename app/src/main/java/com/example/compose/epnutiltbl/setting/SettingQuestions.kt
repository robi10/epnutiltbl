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


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.compose.epnutiltbl.R
import com.example.compose.epnutiltbl.data.Answer
import com.example.compose.epnutiltbl.data.PossibleAnswer
import com.example.compose.epnutiltbl.data.Question
import com.example.compose.epnutiltbl.data.withAnswerSelected


@Composable
fun Question(
    question: Question,
    answer: Answer<*>?,
    onAnswer: (Answer<*>) -> Unit,
    onAllChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(44.dp))
            val backgroundColor = if (MaterialTheme.colors.isLight) {
                MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = backgroundColor,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Text(
                    text = stringResource(id = question.questionText),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (question.description != null) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(id = question.description),
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }
            MultipleChoiceQuestion(
                possibleAnswer = question.answer as PossibleAnswer.MultipleChoice,
                answer = answer as Answer.MultipleChoice?,
                onAnswerSelected = { newAnswer, selected ->
                    // create the answer if it doesn't exist or
                    // update it based on the user's selection
                    if (answer == null) {
                        onAnswer(Answer.MultipleChoice(setOf(newAnswer)))
                    } else {
                        onAnswer(answer.withAnswerSelected(newAnswer, selected))
                    }
                },
                onAllChecked = { selected -> onAllChecked(selected) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
private fun MultipleChoiceQuestion(
    possibleAnswer: PossibleAnswer.MultipleChoice,
    answer: Answer.MultipleChoice?,
    onAnswerSelected: (Int, Boolean) -> Unit,
    onAllChecked:(Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = possibleAnswer.optionsStringRes.associateBy { stringResource(id = it) }
    Column(modifier = modifier) {
        var checkedStateAll by remember(answer)  {
            val status = answer?.allChecked
            mutableStateOf(status)
        }

        Surface(
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            answer?.allChecked = !(answer?.allChecked)!!
                            onAllChecked(checkedStateAll!!)
                        }
                    )
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.allchecked))

                Checkbox(
                    checked = checkedStateAll!!,
                    onCheckedChange = { selected ->
                        answer?.allChecked = selected
                        onAllChecked(selected)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.primary
                    ),
                )
            }
        }
        for (option in options) {
            var checkedState by remember(answer) {
                val selectedOption = answer?.answersStringRes?.contains(option.value)
                mutableStateOf(selectedOption ?: false)
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                checkedState = !checkedState
                                onAnswerSelected(option.value, checkedState)
                            }
                        )
                        .padding(vertical = 16.dp, horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = option.key)

                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { selected ->
                            checkedState = selected
                            onAnswerSelected(option.value, selected)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary
                        ),
                    )
                }
            }
        }
    }
}

