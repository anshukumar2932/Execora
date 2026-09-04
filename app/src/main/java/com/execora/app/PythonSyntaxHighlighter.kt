package com.execora.app

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import com.execora.app.ui.theme.ExecoraExtraColors

class PythonSyntaxHighlighter(private val colors: ExecoraExtraColors) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            highlightPythonCode(text.text, colors),
            OffsetMapping.Identity
        )
    }
}

fun highlightPythonCode(code: String, colors: ExecoraExtraColors): AnnotatedString {
    val keywords = setOf(
        "def", "class", "if", "else", "elif", "for", "while", "return",
        "import", "from", "as", "try", "except", "finally", "with",
        "lambda", "yield", "pass", "break", "continue", "in", "is",
        "not", "and", "or", "True", "False", "None"
    )

    return buildAnnotatedString {
        var i = 0
        while (i < code.length) {
            when {
                // Comments
                code[i] == '#' -> {
                    val end = code.indexOf('\n', i).let { if (it == -1) code.length else it }
                    withStyle(SpanStyle(color = colors.comment)) {
                        append(code.substring(i, end))
                    }
                    i = end
                }
                // Strings
                code[i] == '"' || code[i] == '\'' -> {
                    val quote = code[i]
                    var end = i + 1
                    while (end < code.length && code[end] != quote) {
                        if (code[end] == '\\' && end + 1 < code.length) end += 2
                        else end++
                    }
                    if (end < code.length) end++
                    withStyle(SpanStyle(color = colors.string)) {
                        append(code.substring(i, end))
                    }
                    i = end
                }
                // Identifiers / Keywords
                code[i].isLetter() || code[i] == '_' -> {
                    var end = i + 1
                    while (end < code.length && (code[end].isLetterOrDigit() || code[end] == '_')) {
                        end++
                    }
                    val word = code.substring(i, end)
                    val color = when {
                        keywords.contains(word) -> colors.keyword
                        end < code.length && code[end] == '(' -> colors.function
                        else -> Color.Unspecified
                    }
                    
                    if (color != Color.Unspecified) {
                        withStyle(SpanStyle(color = color)) {
                            append(word)
                        }
                    } else {
                        append(word)
                    }
                    i = end
                }
                else -> {
                    append(code[i])
                    i++
                }
            }
        }
    }
}
