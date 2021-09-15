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

package com.keygenqt.forms.fields

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.keygenqt.forms.R
import com.keygenqt.forms.base.FormFieldState
import com.keygenqt.forms.base.TextFieldError
import com.keygenqt.forms.base.onValueChangeMask
import com.vdurmont.emoji.EmojiParser
import kotlinx.coroutines.launch

/**
 * Default form field
 *
 * @param modifier modifier to apply to this layout node.
 * @param enabled controls the enabled state of the TextField.
 * @param label the optional label to be displayed.
 * @param textStyle Styling configuration for a Text.
 * @param imeAction Signals the keyboard what type of action should be displayed. It is not guaranteed if the keyboard will show the requested action.
 * @param keyboardActions The KeyboardActions class allows developers to specify actions that will be triggered in response to users triggering IME action on the software keyboard.
 * @param colors TextFieldColors for settings colors
 * @param state remember with FormFieldState for management TextField.
 * @param onValueChange the callback that is triggered when the input service updates values in [TextFieldValue].
 * @param filter allows you to filter out all characters except those specified in the string.
 * @param filterEmoji Prevent or Allow emoji input for KeyboardType.Text
 * @param maxLines the maximum height in terms of maximum number of visible lines.
 * @param singleLine field becomes a single horizontally scrolling text field instead of wrapping onto multiple lines.
 * @param maxLength Maximum allowed field length.
 * @param mask +380 (###) ###-##-##, +7 (###) ###-##-##, +# (###) ###-##-##, ####-####-####-#### etc
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and the input text is empty
 * @param keyboardType keyboard type used to request an IME.
 * @param contentError the optional error to be displayed inside the text field container.
 *
 * @since 0.0.7
 * @author Vitaliy Zarubin
 */
@Composable
fun FormField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = stringResource(id = R.string.form_field),
    textStyle: TextStyle = LocalTextStyle.current,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    state: FormFieldState = remember { FormFieldState() },
    onValueChange: ((TextFieldValue) -> TextFieldValue)? = null,
    filter: String? = null,
    filterEmoji: Boolean = false,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    maxLength: Int? = null,
    mask: String? = null,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    contentError: @Composable (() -> Unit)? = null,
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    TextField(
        maxLines = maxLines,
        singleLine = singleLine,
        enabled = enabled,
        value = state.text,
        textStyle = textStyle,
        onValueChange = { textFieldValue ->
            // filter
            var value = filter?.let {
                val filterWithMask = mask?.let { mask + filter } ?: filter
                textFieldValue.copy(text = textFieldValue.text.filter { c -> filterWithMask.contains(c) })
            } ?: textFieldValue

            // filter Emoji
            if (filterEmoji) {
                EmojiParser.removeAllEmojis(value.text)?.let {
                    if (it.length != value.text.length) {
                        scope.launch {
                            Toast.makeText(context, R.string.form_error_emoji, Toast.LENGTH_SHORT).show()
                        }
                        value = value.copy(text = it)
                    }
                }
            }

            // maxLength
            if (value.text.length > maxLength ?: Int.MAX_VALUE) {
                return@TextField
            }

            mask?.let {
                // mask
                state.text = onValueChangeMask.invoke(mask, state, value)
            } ?: run {
                // custom or default
                state.text = onValueChange?.invoke(value) ?: value
            }
        },
        label = {
            Text(label)
        },
        placeholder = placeholder?.let { { Text(placeholder) } },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    state.positionToEnd()
                }
            },
        isError = state.hasErrors,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = keyboardActions,
        colors = colors
    )

    state.getError(LocalContext.current)?.let { error ->
        contentError?.invoke() ?: TextFieldError(text = error)
    }
}