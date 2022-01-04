You can make validate state both custom and use ready-made

### Ready-made states

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

#### Ready-made validations

Default methods for validation

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