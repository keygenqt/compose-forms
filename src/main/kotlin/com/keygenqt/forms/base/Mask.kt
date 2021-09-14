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

tailrec
fun mock(value: String, mask1: String): String {
    return if (value.isEmpty()) mask1 else mock(
        value.drop(1),
        mask1.replaceFirst("#", value.first().toString())
    )
}

fun String.clearMask(mask: String): String {
    val value = mask.replace("""[^\d]+""".toRegex(), "")
    return if (this != value && this.length == 1) this else this.drop(value.length)
}

val onValueChangeMask: (String, FormFieldState, TextFieldValue) -> TextFieldValue = { mask, formState, textFieldValue ->
    val value = textFieldValue.text.take(mask.length)

    val state = when {
        formState.getValue().length > value.length -> "remove"
        formState.getValue().length < value.length -> "added"
        value.length == mask.length && textFieldValue.selection.start == textFieldValue.text.length -> "end"
        else -> "move"
    }

    val clearValue = value.replace("""[^\d]+""".toRegex(), "")
    val clearMask = mask.replace("""[^\d]+""".toRegex(), "")

    if (state == "move" && textFieldValue.selection.start < mask.substringBefore("#").length) {
        TextFieldValue(
            text = value,
            selection = TextRange(mask.substringBefore("#").length + 1, mask.substringBefore("#").length + 1)
        )
    } else if (clearValue == clearMask) {
        TextFieldValue(
            text = "",
            selection = TextRange(0, 0)
        )
    } else {
        when (state) {
            "remove", "added", "move" -> clearValue.let { text ->
                mock(text.clearMask(mask), mask)
                    .substringBefore("#")
                    .dropLastWhile { it !in '0'..'9' }
                    .take(mask.length)
                    .let { mockText ->
                        TextFieldValue(
                            text = mockText,
                            selection = when (state) {
                                "added", "remove" -> if (textFieldValue.selection.start < value.length) {
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
            "end" -> TextFieldValue(
                text = value.take(mask.length),
                selection = textFieldValue.selection
            )
            else -> textFieldValue
        }
    }
}