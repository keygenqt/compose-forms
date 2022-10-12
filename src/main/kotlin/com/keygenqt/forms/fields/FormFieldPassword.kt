/*
 * Copyright 2022 Vitaliy Zarubin
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

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.keygenqt.forms.R
import com.keygenqt.forms.base.FormFieldState

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
 * @param maxLength Maximum allowed field length.
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and the input text is empty
 * @param contentError the optional error to be displayed inside the text field container.
 *
 * @since 0.0.7
 * @author Vitaliy Zarubin
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    tintIcon: Color = MaterialTheme.colorScheme.primary,
    icVisibilityOff: Int = R.drawable.ic_visibility_off,
    icVisibilityOn: Int = R.drawable.ic_visibility,
    maxLength: Int? = null,
    placeholder: String? = null,
    contentError: @Composable (() -> Unit)? = null,
) {
    var visibility: Boolean by remember { mutableStateOf(false) }
    FormField(
        modifier = modifier,
        enabled = enabled,
        label = label,
        textStyle = textStyle,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        colors = colors,
        state = state,
        maxLength = maxLength,
        placeholder = placeholder,
        contentError = contentError,
        keyboardType = KeyboardType.Password,
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
    )
}