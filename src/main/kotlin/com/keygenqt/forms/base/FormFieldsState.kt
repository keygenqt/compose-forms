package com.keygenqt.forms.base

import androidx.compose.ui.text.input.TextFieldValue

/**
 * @todo descriptions
 *
 * @since 0.0.1
 * @author Vitaliy Zarubin
 */
class FormFieldsState {

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
        states.forEach { it.value.validate() }
    }

    fun clearError() {
        states.forEach { it.value.clearError() }
    }

    fun hasErrors(): Boolean {
        return states.count { it.value.hasErrors } > 0
    }

    fun clear() {
        return states.forEach { it.value.clear() }
    }
}