/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.button

enum class ComponentType {
    Primary,
    Secondary,
    Tertiary,
    Error,
    Inverse,
    Outlined,
    Neutral,
    Warning,
    Info,
    Success
}

fun randomButtonType(): ComponentType {
    return (ComponentType.entries - ComponentType.Error - ComponentType.Inverse - ComponentType.Neutral - ComponentType.Outlined).random()
}
