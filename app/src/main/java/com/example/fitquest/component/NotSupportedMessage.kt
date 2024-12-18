package com.example.fitquest.component



import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.fitquest.data.MIN_SUPPORTED_SDK
import com.example.fitquest.R

/**
 * Welcome text shown when the app first starts, where the device is not running a sufficient
 * Android version for Health Connect to be used.
 */
@Composable
fun NotSupportedMessage() {
    val tag = stringResource(R.string.not_supported_tag)
    val url = stringResource(R.string.not_supported_url)
    val handler = LocalUriHandler.current

    val notSupportedText = stringResource(
        id = R.string.not_supported_description,
        MIN_SUPPORTED_SDK
    )
    val notSupportedLinkText = stringResource(R.string.not_supported_link_text)

    val unavailableText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append(notSupportedText)
            append("\n\n")
        }
        pushStringAnnotation(tag = tag, annotation = url)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(notSupportedLinkText)
        }
    }

    var textLayoutResult: TextLayoutResult? = null
    //     var textLayoutResult: androidx.compose.ui.text.TextLayoutResult? = null

    BasicText(
        text = unavailableText,
        style = TextStyle(textAlign = TextAlign.Justify),
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                textLayoutResult?.let { layoutResult ->
                    val characterOffset = layoutResult.getOffsetForPosition(tapOffset)
                    unavailableText.getStringAnnotations(
                        tag = tag,
                        start = characterOffset,
                        end = characterOffset
                    ).firstOrNull()?.let {
                        handler.openUri(it.item)
                    }
                }
            }
        },
        onTextLayout = { result ->
            textLayoutResult = result // Save layout result
        }
    )
}