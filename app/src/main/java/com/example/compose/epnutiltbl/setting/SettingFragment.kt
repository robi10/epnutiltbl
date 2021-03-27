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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.compose.epnutiltbl.R
import com.example.compose.epnutiltbl.theme.EpnUtilTheme
import com.google.android.material.datepicker.MaterialDatePicker

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(PhotoUriManager(requireContext().applicationContext))
    }

    private val takePicture = registerForActivityResult(TakePicture()) { photoSaved ->
        if (photoSaved) {
            viewModel.onImageSaved()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            // In order for savedState to work, the same ID needs to be used for all instances.
            id = R.id.sign_in_fragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                EpnUtilTheme {
                    viewModel.uiState.observeAsState().value?.let { settingState ->
                        when (settingState) {
                            is SettingState.Questions -> SettingQuestionsScreen(
                                questions = settingState,
                                onAction = { id, action -> handleSurveyAction(id, action) },
                                onDonePressed = { viewModel.computeResult(settingState) },
                                onBackPressed = {
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                }
                            )
                            is SettingState.Result -> SettingResultScreen(
                                result = settingState,
                                onDonePressed = {
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleSurveyAction(questionId: Int, actionType: SettingActionType) {
        when (actionType) {
            SettingActionType.PICK_DATE -> showDatePicker(questionId)
            SettingActionType.TAKE_PHOTO -> takeAPhoto()
            SettingActionType.SELECT_CONTACT -> selectContact(questionId)
        }
    }

    private fun showDatePicker(questionId: Int) {
        val picker = MaterialDatePicker.Builder.datePicker().build()
        activity?.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.onDatePicked(questionId, picker.headerText)
            }
        }
    }

    private fun takeAPhoto() {
        takePicture.launch(viewModel.getUriToSaveImage())
    }

    @Suppress("UNUSED_PARAMETER")
    private fun selectContact(questionId: Int) {
        // TODO: unsupported for now
    }
}