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

import androidx.annotation.StringRes

data class SettingResult(
    val library: String,
    @StringRes val result: Int,
    @StringRes val description: Int
)

data class Setting(
    @StringRes val title: Int,
    val questions: List<Question>
)

data class Question(
    val id: Int,
    @StringRes val questionText: Int,
    val answer: PossibleAnswer,
    @StringRes val description: Int? = null
)

/**
 * Type of supported actions for a survey
 */

sealed class PossibleAnswer {
    data class MultipleChoice(val optionsStringRes: List<Int>) : PossibleAnswer()
}

sealed class Answer<T : PossibleAnswer> {
    data class MultipleChoice(val answersStringRes: Set<Int>) :
        Answer<PossibleAnswer.MultipleChoice>() {
    }
}

/**
 * Add or remove an answer from the list of selected answers depending on whether the answer was
 * selected or deselected.
 */
fun Answer.MultipleChoice.withAnswerSelected(
    @StringRes answer: Int,
    selected: Boolean
): Answer.MultipleChoice {
    val newStringRes = answersStringRes.toMutableSet()
    if (!selected) {
        newStringRes.remove(answer)
    } else {
        newStringRes.add(answer)
    }
    return Answer.MultipleChoice(newStringRes)
}