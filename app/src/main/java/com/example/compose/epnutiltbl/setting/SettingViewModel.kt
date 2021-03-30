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

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.compose.epnutiltbl.Screen
import com.example.compose.epnutiltbl.util.Event
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableLiveData<SettingState>()
    val uiState: LiveData<SettingState>
        get() = _uiState

    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>> = _navigateTo

    private lateinit var settingInitialState: SettingState

    init {
        viewModelScope.launch {
            val setting = settingRepository.getSetting()

            // Create the default questions state based on the survey questions
            val questions: List<QuestionState> = setting.questions.mapIndexed { index, question ->
                val showPrevious = index > 0
                val showDone = index == setting.questions.size - 1
                QuestionState(
                    question = question,
                    questionIndex = index,
                    totalQuestionsCount = setting.questions.size,
                    showPrevious = showPrevious,
                    showDone = showDone
                )
            }
            settingInitialState = SettingState.Questions(setting.title, questions)
            _uiState.value = settingInitialState
        }
    }

    fun computeResult(settingQuestions: SettingState.Questions) {
        val answers = settingQuestions.questionsState.mapNotNull { it.answer }
        val result = settingRepository.getSettingResult(answers)
        _uiState.value = SettingState.Result(settingQuestions.settingTitle, result)
    }
    
    fun qaGo() {
        _navigateTo.value = Event(Screen.QA)
    }
}

class SettingViewModelFactory(
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(SettingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}