## Compose Forms

![picture](https://github.com/keygenqt/surf-accompanist/blob/master/data/just-image.png?raw=true)

Field state manager and basic set of validation, fields

## Connection

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifactory.keygenqt.com%2Fartifactory%2Fopen-source%2Fcom%2Fkeygenqt%2Fsurf_accompanist%2Fsurf-accompanist%2Fmaven-metadata.xml)

```gradle
repositories {
    maven("https://artifactory.keygenqt.com/artifactory/open-source")
}
dependencies {
    implementation("com.keygenqt.forms:compose-forms:{version}")
}
```

## Features:

### ![picture](https://github.com/google/material-design-icons/blob/master/png/action/build_circle/materialicons/18dp/1x/baseline_build_circle_black_18dp.png?raw=true) State management TextField
* Default text
* Savable text
* Check validates
* Check has errors
* Clear errors
* Position to end

### ![picture](https://github.com/google/material-design-icons/blob/master/png/action/check_circle/materialicons/18dp/1x/baseline_check_circle_black_18dp.png?raw=true) Validation TextField
You can add create custom states or use default states
* DomainStateValidate
* DomainStateValidateRequired
* EmailStateValidate
* EmailStateValidateRequired
* PhoneStateValidate
* PhoneStateValidateRequired
* UrlStateValidate
* UrlStateValidateRequired

Default methods for validation

* getErrorIsBlank
* getErrorIsNotDomain
* getErrorIsNotEmail
* getErrorIsNotPhone
* getErrorIsNotUrl

### ![picture](https://github.com/google/material-design-icons/blob/master/png/action/grading/materialicons/18dp/1x/baseline_grading_black_18dp.png?raw=true) Systematization of work with forms
You can group fields into a form. You can make fields both custom and use ready-made:
* FormField
* FormFieldEmail
* FormFieldPassword
* FormFieldPhone

## License

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