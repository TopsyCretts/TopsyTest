package org.top.test

internal fun String.snakeCase(): String =
    buildString {
        for (c in this@snakeCase) {
            if (c.isUpperCase() && isNotEmpty()) {
                append('_')
            }

            append(c.lowercaseChar())
        }
    }
