You can group fields into a form. You can make fields both custom and use ready-made

#### Fields form

Default fields form

* FormField
* FormFieldEmail
* FormFieldPassword
* FormFieldPhone
* FormFieldNumber

#### FormField numbers

Numeric fields (FormFieldPhone, FormFieldNumber) have the parameter mask. Examples of mask:

```
+380 (###) ###-##-##
+7 (###) ###-##-##
+# (###) ###-##-##
####-####-####-####
```

#### Params
```kotlin
/**
 * Default form field
 *
 * @param modifier modifier to apply to this layout node.
 * @param enabled controls the enabled state of the [TextField].
 * @param readOnly controls the editable state of the [TextField].
 * @param label the optional label to be displayed.
 * @param textStyle Styling configuration for a Text.
 * @param imeAction Signals the keyboard what type of action should be displayed. It is not guaranteed if the keyboard will show the requested action.
 * @param visualTransformation
 * @param keyboardActions The KeyboardActions class allows developers to specify actions that will be triggered in response to users triggering IME action on the software keyboard.
 * @param leadingIcon the optional leading icon to be displayed at the beginning of the text field container
 * @param trailingIcon the optional trailing icon to be displayed at the end of the text field container
 * @param colors TextFieldColors for settings colors
 * @param state remember with FormFieldState for management TextField.
 * @param onValueChange the callback that is triggered when the input service updates values in [TextFieldValue].
 * @param lines height in lines.
 * @param maxLines the maximum height in terms of maximum number of visible lines.
 * @param singleLine field becomes a single horizontally scrolling text field instead of wrapping onto multiple lines.
 * @param maxLength Maximum allowed field length.
 * @param mask +380 (###) ###-##-##, +7 (###) ###-##-##, +# (###) ###-##-##, ####-####-####-#### etc
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and the input text is empty
 * @param keyboardType keyboard type used to request an IME.
 * @param contentError the optional error to be displayed inside the text field container.
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun FormField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    state: FormFieldState = remember { FormFieldState() },
    onValueChange: ((TextFieldValue) -> TextFieldValue)? = null,
    lines: Int? = null,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    maxLength: Int? = null,
    mask: String? = null,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    contentError: @Composable (() -> Unit)? = null,
)
```

#### Usage with form

-> [@see](https://github.com/keygenqt/android-DemoCompose/blob/master/app/src/main/kotlin/com/keygenqt/demo_contacts/modules/other/ui/form/SignInFieldsForm.kt#L24) <-

```kotlin
// Create form
enum class SignInFieldsForm(val state: FormFieldState) : FormStates {
    SignInEmail(EmailStateValidateRequired()),
    SignInPassword(PasswordStateValidateRequired()),
}
```

#### Example composable form

-> [@see](https://github.com/keygenqt/android-DemoCompose/blob/master/app/src/main/kotlin/com/keygenqt/demo_contacts/modules/other/ui/screens/signIn/SignInForm.kt#L52) <-

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

### Preview

<p>
<img src="https://github.com/keygenqt/compose-forms/blob/gh-pages/data/vokoscreen-2021-09-14_20-10-40.gif?raw=true" width="33%"/>
</p>