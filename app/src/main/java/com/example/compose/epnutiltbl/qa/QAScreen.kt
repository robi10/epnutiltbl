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

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.epnutiltbl.R
import com.example.compose.epnutiltbl.data.SettingState

@Composable
fun QAScreen(
    data: SettingState.Result,
    onAnswer: () -> Unit,
    onSetting: () -> Unit
) {
    var brandingBottom by remember { mutableStateOf(0f) }
    val showBranding by remember { mutableStateOf(true) }
    val currentOffsetHolder = remember { mutableStateOf(0f) }
    var heightWithBranding by remember { mutableStateOf(0) }
    currentOffsetHolder.value = if (showBranding) 0f else -brandingBottom
    val currentOffsetHolderDp =
        with(LocalDensity.current) { currentOffsetHolder.value.toDp() }
    val heightDp = with(LocalDensity.current) { heightWithBranding.toDp() }

    var selectStringA by remember { mutableStateOf(-1)}
    var selectStringB by remember { mutableStateOf(-1)}
    //val selectStringC by remember { mutableStateOf(-1)}

    selectStringA = data.results[0].selectStringIds[0]
    selectStringB = data.results[1].selectStringIds[0]
    selectStringA.toString()
    selectStringB.toString()

    Surface(modifier = Modifier.fillMaxSize()) {
        val offset by animateDpAsState(targetValue = currentOffsetHolderDp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .brandingPreferredHeight(showBranding, heightDp)
                .offset(y = offset)
                .onSizeChanged {
                    if (showBranding) {
                        heightWithBranding = it.height
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id =selectStringA),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (brandingBottom == 0f) {
                                brandingBottom = it.boundsInParent().bottom
                            }
                        }
                )
                Text(
                    text = stringResource(id = selectStringB),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (brandingBottom == 0f) {
                                brandingBottom = it.boundsInParent().bottom
                            }
                        }
                )
                Text(
                    text = stringResource(id = R.string.app_tagline),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (brandingBottom == 0f) {
                                brandingBottom = it.boundsInParent().bottom
                            }
                        }
                )
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(align = Alignment.CenterVertically)) {
                Button(
                    onClick = onAnswer,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.to_answer),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSetting,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.to_setting),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        }
    }
}


@Composable
fun QAResultScreen(
    data: SettingState.Result,
    onPrevious:()->Unit,
    onSetting:()->Unit,
    onNext: ()-> Unit
) {
    var brandingBottom by remember { mutableStateOf(0f) }
    val showBranding by remember { mutableStateOf(true) }
    var heightWithBranding by remember { mutableStateOf(0) }

    val currentOffsetHolder = remember { mutableStateOf(0f) }
    currentOffsetHolder.value = if (showBranding) 0f else -brandingBottom
    val currentOffsetHolderDp =
        with(LocalDensity.current) { currentOffsetHolder.value.toDp() }
    val heightDp = with(LocalDensity.current) { heightWithBranding.toDp() }

    var selectStringA by remember { mutableStateOf(-1)}
    var selectStringB by remember { mutableStateOf(-1)}
    //val selectStringC by remember { mutableStateOf(-1)}

    selectStringA = data.results[0].selectStringIds[0]
    selectStringB = data.results[1].selectStringIds[0]
    selectStringA.toString()
    selectStringB.toString()

    Surface(modifier = Modifier.fillMaxSize()) {
        val offset by animateDpAsState(targetValue = currentOffsetHolderDp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .brandingPreferredHeight(showBranding, heightDp)
                .offset(y = offset)
                .onSizeChanged {
                    if (showBranding) {
                        heightWithBranding = it.height
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = selectStringA),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (brandingBottom == 0f) {
                                brandingBottom = it.boundsInParent().bottom
                            }
                        }
                )
                Text(
                    text = stringResource(id = selectStringB),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (brandingBottom == 0f) {
                                brandingBottom = it.boundsInParent().bottom
                            }
                        }
                )
                Text(
                    text = stringResource(id = R.string.app_tagline),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (brandingBottom == 0f) {
                                brandingBottom = it.boundsInParent().bottom
                            }
                        }
                )
                Text(
                    text = stringResource(id = R.string.app_tagline),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (brandingBottom == 0f) {
                                brandingBottom = it.boundsInParent().bottom
                            }
                        }
                )
            }
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)) {
                    Button(
                        onClick = onPrevious,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_back),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onSetting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_setting),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onNext,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_next),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }
        }
    }
}

private fun Modifier.brandingPreferredHeight(
    showBranding: Boolean,
    heightDp: Dp
): Modifier {
    return if (!showBranding) {
        this
            .wrapContentHeight(unbounded = true)
            .height(heightDp)
    } else {
        this
    }
}