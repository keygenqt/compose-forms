## Validation TextField

You can add create custom states or use default states

#### Validations

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