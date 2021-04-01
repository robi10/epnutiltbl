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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.compose.epnutiltbl.Screen
import com.example.compose.epnutiltbl.navigate
import com.example.compose.epnutiltbl.theme.EpnUtilTheme

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels {
        SettingViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.Setting)
            }
        }

        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                EpnUtilTheme {
                    viewModel.uiState.observeAsState().value?.let { settingState ->
                        SettingQuestionsScreen(
                            questions = settingState as SettingState.Questions,
                            onDonePressed = {
                                viewModel.computeResult(settingState)
                                viewModel.qaGo()
                            },
                            onBackPressed = {
                                activity?.onBackPressedDispatcher?.onBackPressed()
                            }
                        )
                    }
                }
            }
        }
    }
}
