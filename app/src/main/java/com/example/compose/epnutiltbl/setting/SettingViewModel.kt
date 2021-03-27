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

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository,
    private val photoUriManager: PhotoUriManager
) : ViewModel() {

    private val _uiState = MutableLiveData<SettingState>()
    val uiState: LiveData<SettingState>
        get() = _uiState

    private lateinit var settingInitialState: SettingState

    // Uri used to save photos taken with the camera
    private var uri: Uri? = null

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

    fun onDatePicked(questionId: Int, date: String) {
        updateStateWithActionResult(questionId, SettingActionResult.Date(date))
    }

    fun getUriToSaveImage(): Uri? {
        uri = photoUriManager.buildNewUri()
        return uri
    }

    fun onImageSaved() {
        uri?.let { uri ->
            getLatestQuestionId()?.let { questionId ->
                updateStateWithActionResult(questionId, SettingActionResult.Photo(uri))
            }
        }
    }

    private fun updateStateWithActionResult(questionId: Int, result: SettingActionResult) {
        val latestState = _uiState.value
        if (latestState != null && latestState is SettingState.Questions) {
            val question =
                latestState.questionsState.first { questionState ->
                    questionState.question.id == questionId
                }
            question.answer = Answer.Action(result)
            question.enableNext = true
        }
    }

    private fun getLatestQuestionId(): Int? {
        val latestState = _uiState.value
        if (latestState != null && latestState is SettingState.Questions) {
            return latestState.questionsState[latestState.currentQuestionIndex].question.id
        }
        return null
    }
}

class SettingViewModelFactory(
    private val photoUriManager: PhotoUriManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(SettingRepository, photoUriManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
