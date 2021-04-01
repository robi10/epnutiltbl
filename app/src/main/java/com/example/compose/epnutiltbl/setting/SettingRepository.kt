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

import android.os.Build
import com.example.compose.epnutiltbl.R
import com.example.compose.epnutiltbl.setting.PossibleAnswer.MultipleChoice

// Static data of questions
private val jetpackQuestions = mutableListOf(
    Question(
        id = 1,
        questionText = R.string.in_my_free_time,
        answer = MultipleChoice(
            optionsStringRes = listOf(
                R.string.read,
                R.string.work_out,
                R.string.draw,
                R.string.play_games,
                R.string.dance,
                R.string.watch_movies
            )
        ),
        description = R.string.select_all
    ),
    Question(
        id = 2,
        questionText = R.string.in_my_free_time,
        answer = MultipleChoice(
            optionsStringRes = listOf(
                R.string.read,
                R.string.work_out,
                R.string.draw,
                R.string.play_games,
                R.string.dance,
                R.string.watch_movies
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
    fun getSettingResult(options:List<PossibleAnswer>,answers: List<Answer<*>>): List<ResultState> {
        val list = ArrayList<ResultState>()
        for ((count, answer) in answers.withIndex()) {
            val optionsRes = options[count] as MultipleChoice?
            val optionsResStr = optionsRes?.optionsStringRes?.toMutableList()
            val choiceStrs = answer as Answer.MultipleChoice?
            val strs = choiceStrs?.answersStringRes?.toMutableList()

            var res = ResultState()
            if (strs != null) {
                for (str in strs) {
                    var selectId = -1
                    if (optionsResStr != null) {
                        selectId = optionsResStr.indexOf(str)
                    }
                    res.add(selectId, str)
                }
            }
            list.add(res)
        }
        return list
    }
}
