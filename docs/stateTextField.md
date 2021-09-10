## State management TextField

The library calls for encapsulating state management

#### FormFieldState

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