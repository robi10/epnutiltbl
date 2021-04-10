package com.example.compose.epnutiltbl.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class DataViewModel(
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableLiveData<SettingState>()
    val uiState: LiveData<SettingState>
        get() = _uiState

    fun setInit() {
        _uiState.value = settingInitialState
    }

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

    fun computeResult(
        settingQuestions: SettingState.Questions,
    ){
        val options = settingQuestions.questionsState.mapNotNull { it.question.answer }
        val answers = settingQuestions.questionsState.mapNotNull { it.answer }
        _uiState.value = settingRepository.getSettingResult(options, answers)
    }
}


class DataViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(SettingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}