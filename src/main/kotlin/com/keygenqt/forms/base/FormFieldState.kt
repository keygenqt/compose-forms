/*
 * Copyright 2021 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.forms.base

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.runBlocking

/**
 * State management TextField
 *
 * @param text Default value field
 * @param checkValid Validate functions array
 *
 * @since 0.0.2
 * @author Vitaliy Zarubin
 */
@OptIn(ExperimentalComposeUiApi::class)
open class FormFieldState(
    text: String = "",
    private val checkValid: (String) -> List<(Context) -> String> = { emptyList() },
) {

    private var _default = TextFieldValue(text = text)
    private var _text: TextFieldValue by mutableStateOf(_default)
    private var _errors: List<(Context) -> String> by mutableStateOf(listOf())
    private var _maxValue: Int by mutableStateOf(Int.MAX_VALUE)

    val focus: FocusRequester by mutableStateOf(FocusRequester())

    @ExperimentalFoundationApi
    val relocation: BringIntoViewRequester by mutableStateOf(BringIntoViewRequester())

    var text: TextFieldValue
        get() = _text
        set(value) {
            _text = value
            _errors = checkValid(value.text)
        }

    val hasErrors: Boolean
        get() = _errors.isNotEmpty()

    fun validate() {
        _errors = checkValid(_text.text)
    }

    fun clearError() {
        _errors = emptyList()
    }

    fun getError(context: Context): String? {
        return _errors.firstOrNull()?.invoke(context)
    }

    fun setMaxValue(max: Int) {
        _maxValue = max
    }

    fun getValue(): String {
        return _text.text
    }

    fun setValue(text: String) {
        if (text.length <= _maxValue) {
            this.text = TextFieldValue(text)
            positionToEnd()
        }
    }

    fun addValue(text: String) {
        if ((getValue() + text).length <= _maxValue) {
            this.text = TextFieldValue(getValue() + text)
            positionToEnd()
        }
    }

    fun removeLast() {
        this.text = TextFieldValue(getValue().dropLast(1))
        positionToEnd()
    }

    fun setError(error: (Context) -> String) {
        _errors = listOf(error)
    }

    fun positionToEnd() {
        _text = _text.copy(
            selection = TextRange(_text.text.length, _text.text.length)
        )
    }

    fun default(value: String): FormFieldState {
        _default = TextFieldValue(text = value)
        _text = _default
        positionToEnd()
        return this
    }

    fun clear(): FormFieldState {
        _text = _default
        return this
    }

    fun requestFocus() {
        focus.requestFocus()
    }

    @OptIn(ExperimentalFoundationApi::class)
    suspend fun bringIntoView() {
        relocation.bringIntoView()
    }
}
