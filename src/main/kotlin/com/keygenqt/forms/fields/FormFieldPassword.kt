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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.keygenqt.forms.R
import com.keygenqt.forms.base.FormFieldState
import com.keygenqt.forms.base.TextFieldError

/**
 * Password form field with trailingIcon
 *
 * @param modifier modifier to apply to this layout node.
 * @param enabled controls the enabled state of the TextField.
 * @param label the optional label to be displayed.
 * @param textStyle Styling configuration for a Text.
 * @param imeAction Signals the keyboard what type of action should be displayed. It is not guaranteed if the keyboard will show the requested action.
 * @param keyboardActions The KeyboardActions class allows developers to specify actions that will be triggered in response to users triggering IME action on the software keyboard.
 * @param colors TextFieldColors for settings colors
 * @param state remember with FormFieldState for management TextField.
 * @param icVisibilityOff Resources object to query the image file from.
 * @param icVisibilityOn Resources object to query the image file from.
 * @param filter allows you to filter out all characters except those specified in the string
 * @param maxLength Maximum allowed field length.
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and the input text is empty
 * @param contentError the optional error to be displayed inside the text field container.
 *
 * @since 0.0.7
 * @author Vitaliy Zarubin
 */
@Composable
fun FormFieldPassword(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = stringResource(id = R.string.form_fields_password),
    textStyle: TextStyle = LocalTextStyle.current,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    state: FormFieldState = remember { FormFieldState() },
    tintIcon: Color = MaterialTheme.colors.primary,
    icVisibilityOff: Int = R.drawable.ic_visibility_off,
    icVisibilityOn: Int = R.drawable.ic_visibility,
    filter: String? = null,
    maxLength: Int? = null,
    placeholder: String? = null,
    contentError: @Composable (() -> Unit)? = null,
) {
    var visibility: Boolean by remember { mutableStateOf(false) }
    TextField(
        enabled = enabled,
        value = state.text,
        onValueChange = { textFieldValue ->

            // filter
            val value = filter?.let {
                textFieldValue.copy(text = textFieldValue.text.filter { c -> filter.contains(c) })
            } ?: textFieldValue

            // maxLength
            if (value.text.length > maxLength ?: Int.MAX_VALUE) {
                return@TextField
            }

            state.text = value
        },
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(placeholder) } },
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visibility = !visibility }) {
                Icon(
                    painter = painterResource(id = if (visibility) icVisibilityOff else icVisibilityOn),
                    contentDescription = label,
                    tint = tintIcon
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    state.positionToEnd()
                }
            },
        textStyle = textStyle,
        isError = state.hasErrors,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction, keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
        colors = colors
    )

    state.getError(LocalContext.current)?.let { error ->
        contentError?.invoke() ?: TextFieldError(text = error)
    }
}