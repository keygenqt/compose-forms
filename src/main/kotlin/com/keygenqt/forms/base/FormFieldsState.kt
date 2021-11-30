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

import android.os.Handler
import android.os.Looper
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * State management TextField multiple
 *
 * @since 0.0.2
 * @author Vitaliy Zarubin
 */
class FormFieldsState {

    private val _isValidate: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val isValidate: StateFlow<Boolean> get() = _isValidate.asStateFlow()

    private val states = mutableMapOf<FormStates, FormFieldState>()

    fun add(key: FormStates, state: FormFieldState, value: String? = null) {
        states[key] = state
        value?.let { state.text = TextFieldValue(text = it) }
    }

    fun remove(key: FormStates) {
        states.remove(key)
    }

    fun get(key: FormStates): FormFieldState {
        return states[key]!!
    }

    fun validate() {
        var isFocus = false
        states.forEach {
            it.value.validate()
            if (!isFocus && it.value.hasErrors) {
                it.value.requestFocus()
                isFocus = true
            }
        }
        _isValidate.value = true
        // disable validate
        Handler(Looper.getMainLooper()).postDelayed({
            _isValidate.value = false
        }, 100)
    }

    fun clearError() {
        _isValidate.value = false
        states.forEach { it.value.clearError() }
    }

    fun hasErrors(): Boolean {
        return states.count { it.value.hasErrors } > 0
    }

    fun clear() {
        return states.forEach { it.value.clear() }
    }
}