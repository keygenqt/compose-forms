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

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

/**
 * Convert value to mask
 *
 * @since 0.0.5
 * @author Vitaliy Zarubin
 */
tailrec
fun mock(value: String, mask1: String): String {
    return if (value.isEmpty()) mask1 else mock(
        value.drop(1),
        mask1.replaceFirst("#", value.first().toString())
    )
}

/**
 * Clearing a value from mask variables
 *
 * @since 0.0.5
 * @author Vitaliy Zarubin
 */
fun String.clearMask(mask: String): String {
    val value = mask.replace("""[^\d]+""".toRegex(), "")
    return if (this.length <= value.length || !this.contains(value)) {
        this
    } else {
        this.drop(value.length)
    }
}

/**
 * State events TextFieldValue
 *
 * @since 0.0.5
 * @author Vitaliy Zarubin
 */
enum class TextFieldState {
    REMOVE,
    ADDED,
    END,
    MOVE
}

/**
 * Get state events from TextFieldValue
 *
 * @since 0.0.5
 * @author Vitaliy Zarubin
 */
val onValueChangeMaskState: (String, FormFieldState, TextFieldValue) -> TextFieldState = { mask, formState, textFieldValue ->
    val value = textFieldValue.text.take(mask.length)
    when {
        formState.getValue().length > value.length -> TextFieldState.REMOVE
        formState.getValue().length < value.length -> TextFieldState.ADDED
        value.length == mask.length && textFieldValue.selection.start == textFieldValue.text.length -> TextFieldState.END
        else -> TextFieldState.MOVE
    }
}

/**
 * Main job of providing field masking
 *
 * @since 0.0.5
 * @author Vitaliy Zarubin
 */
val onValueChangeMask: (String, FormFieldState, TextFieldValue) -> TextFieldValue = { mask, formState, textFieldValue ->
    val value = textFieldValue.text.take(mask.length)

    val state = onValueChangeMaskState.invoke(mask, formState, textFieldValue)

    val clearValue = value.replace("""[^\d]+""".toRegex(), "")
    val clearMask = mask.replace("""[^\d]+""".toRegex(), "")

    if (state == TextFieldState.MOVE && textFieldValue.selection.start < mask.substringBefore("#").length) {
        TextFieldValue(
            text = value,
            selection = TextRange(mask.substringBefore("#").length + 1, mask.substringBefore("#").length + 1)
        )
    } else if (state == TextFieldState.REMOVE && (clearValue == clearMask || clearValue == "")) {
        TextFieldValue(
            text = "",
            selection = TextRange(0, 0)
        )
    } else {
        when (state) {
            TextFieldState.REMOVE, TextFieldState.ADDED, TextFieldState.MOVE -> clearValue.let { text ->
                mock(text.clearMask(mask), mask)
                    .substringBefore("#")
                    .dropLastWhile { it !in '0'..'9' && it != '(' }
                    .take(mask.length)
                    .let { mockText ->
                        TextFieldValue(
                            text = mockText,
                            selection = when (state) {
                                TextFieldState.ADDED, TextFieldState.REMOVE -> if (textFieldValue.selection.start < value.length) {
                                    val plus =
                                        if (textFieldValue.selection.start < mask.substringBefore("#").length + 1) 1 else 0
                                    TextRange(
                                        textFieldValue.selection.start.plus(plus),
                                        textFieldValue.selection.start.plus(plus)
                                    )
                                } else {
                                    TextRange(mockText.length, mockText.length)
                                }
                                else -> textFieldValue.selection
                            }
                        )
                    }
            }
            TextFieldState.END -> TextFieldValue(
                text = value.take(mask.length),
                selection = textFieldValue.selection
            )
        }
    }
}