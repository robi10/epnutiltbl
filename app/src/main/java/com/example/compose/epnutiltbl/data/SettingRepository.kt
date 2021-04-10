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

package com.example.compose.epnutiltbl.data

import com.example.compose.epnutiltbl.R
import com.example.compose.epnutiltbl.data.PossibleAnswer.MultipleChoice

// Static data of questions
private val jetpackQuestions = mutableListOf(
    Question(
        id = 1,
        questionText = R.string.person_selection,
        answer = MultipleChoice(
            optionsStringRes = listOf(
                R.string.first_person_singular,
                R.string.second_person_singular,
                R.string.third_person_singular,
                R.string.first_person_plural,
                R.string.second_person_plural,
                R.string.third_person_singular
            )
        ),
        description = R.string.select_all
    ),
    Question(
        id = 2,
        questionText = R.string.tense_selection,
        answer = MultipleChoice(
            optionsStringRes = listOf(
                R.string.direct_present_tense,
                R.string.direct_point_past_form,
                R.string.direct_line_past_form,
                R.string.direct_future_tense,
                R.string.direct_past_future_form,
                R.string.subjunctive_present_tense,
                R.string.subjunctive_past_form
            )
        ),
        description = R.string.select_all
    )

).apply {

}.toList()

private val jetpackSetting = Setting(
    title = R.string.which_jetpack_library,
    questions = jetpackQuestions
)

object SettingRepository {

    suspend fun getSetting() = jetpackSetting

    @Suppress("UNUSED_PARAMETER")
    fun getSettingResult(options:List<PossibleAnswer>,answers: List<Answer<*>>):
            SettingState.Result {

        val list = ArrayList<ResultState>()
        for ((count, answer) in answers.withIndex()) {
            val optionsRes = options[count] as MultipleChoice?
            val optionsResStr = optionsRes?.optionsStringRes?.toMutableList()
            val choiceStrs = answer as Answer.MultipleChoice?
            val strs = choiceStrs?.answersStringRes?.toMutableList()

            var selectIds = ArrayList<Int>()
            var selectStringIds = ArrayList<Int>()
            if (strs != null) {
                for (str in strs) {
                    var selectId = -1
                    if (optionsResStr != null) {
                        selectId = optionsResStr.indexOf(str)
                    }
                    selectIds.add(selectId)
                    selectStringIds.add(str)
                }

            }
            list.add(ResultState(selectIds, selectStringIds))
        }

        return SettingState.Result(results = list)
    }
}
