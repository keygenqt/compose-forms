Compose Forms
===================

![picture](../data/just-image.png)

#### Connection:

```gradle
repositories {
    maven("https://artifactory.keygenqt.com/artifactory/open-source")
}
dependencies {
    implementation("com.keygenqt.forms:compose-forms:0.0.2")
}
```

### Idea

Field state manager and basic set of validation, fields

### FormFieldState
* Default text
* Savable text
* Check has errors
* Validate
* Clear errors
* Position to end

#### Usage
```kotlin
val fieldState: FormFieldState = remember { FormFieldState() }

TextField(
    value = fieldState.text,
    onValueChange = { fieldState.text = it },
    label = { Text("Label") },
    modifier = modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
            if (focusState.isFocused) {
                fieldState.positionToEnd()
            }
        },
    isError = fieldState.hasErrors,
)
```

### Default states validation (You can add custom states)
* DomainStateValidate
* DomainStateValidateRequired
* EmailStateValidate
* EmailStateValidateRequired
* PhoneStateValidate
* PhoneStateValidateRequired
* UrlStateValidate
* UrlStateValidateRequired

#### Usage
```kotlin
val fieldState: FormFieldState = remember { DomainStateValidate() }

TextField(
    value = fieldState.text,
    onValueChange = { fieldState.text = it },
    label = { Text("Label") },
    modifier = modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
            if (focusState.isFocused) {
                fieldState.positionToEnd()
            }
        },
    isError = fieldState.hasErrors,
)

fieldState.getError(LocalContext.current)?.let { error ->
    TextFieldError(text = error)
}
```

### Validations
* getErrorIsBlank
* getErrorIsNotDomain
* getErrorIsNotEmail
* getErrorIsNotPhone
* getErrorIsNotUrl

#### Usage
```kotlin
class CustomStateValidate : FormFieldState(checkValid = { target: String ->
    listOfNotNull(
        getErrorIsBlank(target),
        getErrorIsNotDomain(target),
        getErrorIsNotEmail(target),
        getCustomErrorSize(target),
    )
})

private fun getCustomErrorSize(target: String) =
    when {
        target.length != 5 -> { it: Context ->
            it.getString(R.string.contact_change_common_code_error_validate, "5")
        }
        else -> null
    }
```

### Fields form
* FormField
* FormFieldEmail
* FormFieldPassword
* FormFieldPhone

#### Params
```kotlin
/**
 * @param modifier modifier to apply to this layout node.
 * @param enabled controls the enabled state of the TextField.
 * @param label the optional label to be displayed.
 * @param textStyle Styling configuration for a Text.
 * @param imeAction Signals the keyboard what type of action should be displayed. It is not guaranteed if the keyboard will show the requested action.
 * @param keyboardActions The KeyboardActions class allows developers to specify actions that will be triggered in
 * response to users triggering IME action on the software keyboard.
 * @param colors TextFieldColors for settings colors
 * @param state remember with FormFieldState for management TextField.
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and the input text is empty
 * @param keyboardType keyboard type used to request an IME.
 * @param contentError the optional error to be displayed inside the text field container.
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
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    contentError: @Composable (() -> Unit)? = null,
)
```

#### Usage with form -> [@see](https://github.com/keygenqt/android-DemoCompose/blob/master/app/src/main/kotlin/com/keygenqt/demo_contacts/modules/other/ui/form/SignInFieldsForm.kt#L24) <-
```kotlin
// Create form
enum class SignInFieldsForm(val state: FormFieldState) : FormStates {
    SignInEmail(EmailStateValidateRequired()),
    SignInPassword(PasswordStateValidateRequired()),
}
```

#### Example composable form -> [@see](https://github.com/keygenqt/android-DemoCompose/blob/master/app/src/main/kotlin/com/keygenqt/demo_contacts/modules/other/ui/screens/signIn/SignInForm.kt#L52) <-
```kotlin
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInForm(
    loading: Boolean = false,
    onNavigationEvent: (SignInEvents) -> Unit = {},
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val padding = 16.dp

    // create from state
    val formFields = FormFieldsState().apply {
        add(SignInEmail, remember { SignInEmail.state.default(ConstantsApp.DEBUG_CREDENTIAL_LOGIN) })
        add(SignInPassword, remember { SignInPassword.state.default(ConstantsApp.DEBUG_CREDENTIAL_PASSW) })
    }

    // for focus to field
    val requesterFieldEmail = remember { FocusRequester() }
    val requesterFieldPassword = remember { FocusRequester() }

    // click submit
    val submitClick = {
        // validate before send
        formFields.validate()
        // check has errors
        if (!formFields.hasErrors()) {
            // submit query
            onNavigationEvent(
                SignInEvents.SignIn(
                    login = formFields.get(SignInEmail).getValue(),
                    passw = formFields.get(SignInPassword).getValue(),
                )
            )
            // hide keyboard
            localFocusManager.clearFocus()
            softwareKeyboardController?.hide()
        }
    }

    // create field email
    FormFieldEmail(
        modifier = Modifier.focusRequester(requesterFieldEmail),
        label = stringResource(id = R.string.form_email),
        enabled = !loading,
        state = formFields.get(SignInEmail),
        imeAction = ImeAction.Next,
        colors = customTextFieldColors(),
        keyboardActions = KeyboardActions(onNext = { requesterFieldPassword.requestFocus() })
    )

    Spacer(modifier = Modifier.size(padding))

    // create field password
    FormFieldPassword(
        modifier = Modifier.focusRequester(requesterFieldPassword),
        enabled = !loading,
        state = formFields.get(SignInPassword),
        imeAction = ImeAction.Done,
        colors = customTextFieldColors(),
        tintIcon = MaterialTheme.colors.onPrimary,
        keyboardActions = KeyboardActions(onDone = { submitClick.invoke() })
    )

    Spacer(modifier = Modifier.size(padding))

    // submit button
    Button(
        enabled = !loading,
        onClick = { submitClick.invoke() },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.textButtonColors(backgroundColor = MaterialTheme.colors.secondary),
    ) {
        Text(
            text = stringResource(id = R.string.form_button_submit).uppercase(),
        )
    }

    // clear focus after end
    LaunchedEffect(Unit) {
        scope.launch {
            delay(10)
            requesterFieldEmail.requestFocus()
        }
    }
}
```

# License

```
Copyright 2021 Vitaliy Zarubin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```