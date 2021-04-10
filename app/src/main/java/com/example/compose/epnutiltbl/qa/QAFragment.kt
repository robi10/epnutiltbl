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

package com.example.compose.epnutiltbl.qa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.compose.epnutiltbl.Screen
import com.example.compose.epnutiltbl.data.DataViewModel
import com.example.compose.epnutiltbl.data.SettingState
import com.example.compose.epnutiltbl.navigate
import com.example.compose.epnutiltbl.theme.EpnUtilTheme

/**
 * Fragment containing the welcome UI.
 */
class QAFragment : Fragment() {
    private val viewModel: QAViewModel by viewModels { QAViewModelFactory() }

    private val dataModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.QA)
            }
        }

        val data = dataModel.uiState.value as SettingState.Result

        return ComposeView(requireContext()).apply {
            setContent {
                EpnUtilTheme {
                    viewModel.uiState.observeAsState().value?.let { qaState ->
                        when (qaState) {
                            QAStatus.QAQuestion -> QAScreen(
                                data = data,
                                onAnswer = {
                                    viewModel.setUiState(QAStatus.QAAnswer) },
                                onSetting = {
                                    dataModel.setInit()
                                    viewModel.settingGo() }
                            )
                            QAStatus.QAAnswer -> QAResultScreen(
                                data = data,
                                onPrevious = { },
                                onSetting = {
                                    dataModel.setInit()
                                    viewModel.settingGo() },
                                onNext = {
                                    viewModel.setUiState(QAStatus.QAQuestion) }
                            )
                        }
                    }
                }
            }
        }
    }
}
