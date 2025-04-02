package com.rysanek.common_ui.composables

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = true,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
){
    val textDecorations = if (underlined || strikethrough) {
        val decorations = mutableListOf<TextDecoration>()
        if (underlined) decorations.add(TextDecoration.Underline)
        if (strikethrough) decorations.add(TextDecoration.Underline)
        TextDecoration.combine(decorations)
    } else null

    Text(
        modifier = modifier,
        text = text,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = bold.takeIf { it }?.let { FontWeight.Bold },
        textDecoration = textDecorations,
        color = color,
        overflow = overflow,
        lineHeight = lineHeight,
        maxLines = maxLines,
        minLines = minLines,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
        style = style,
        textAlign = textAlign
    )
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
){
    val textDecorations = if (underlined || strikethrough) {
        val decorations = mutableListOf<TextDecoration>()
        if (underlined) decorations.add(TextDecoration.Underline)
        if (strikethrough) decorations.add(TextDecoration.Underline)
        TextDecoration.combine(decorations)
    } else null

    Text(
        modifier = modifier,
        text = text,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = bold.takeIf { it }?.let { FontWeight.Bold },
        textDecoration = textDecorations,
        color = color,
        overflow = overflow,
        lineHeight = lineHeight,
        maxLines = maxLines,
        minLines = minLines,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
        style = style,
        textAlign = textAlign
    )
}

@Composable
fun SmallBodyText(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
){
    val textDecorations = if (underlined || strikethrough) {
        val decorations = mutableListOf<TextDecoration>()
        if (underlined) decorations.add(TextDecoration.Underline)
        if (strikethrough) decorations.add(TextDecoration.Underline)
        TextDecoration.combine(decorations)
    } else null

    Text(
        modifier = modifier,
        text = text,
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        fontWeight = bold.takeIf { it }?.let { FontWeight.Bold },
        textDecoration = textDecorations,
        color = color,
        overflow = overflow,
        lineHeight = lineHeight,
        maxLines = maxLines,
        minLines = minLines,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
        style = style,
        textAlign = textAlign
    )
}

@Composable
fun TitleText(
    stringResource: Int,
    modifier: Modifier = Modifier,
    bold: Boolean = true,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
){
    val textDecorations = if (underlined || strikethrough) {
        val decorations = mutableListOf<TextDecoration>()
        if (underlined) decorations.add(TextDecoration.Underline)
        if (strikethrough) decorations.add(TextDecoration.Underline)
        TextDecoration.combine(decorations)
    } else null

    Text(
        modifier = modifier,
        text = stringResource(stringResource),
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = bold.takeIf { it }?.let { FontWeight.Bold },
        textDecoration = textDecorations,
        color = color,
        overflow = overflow,
        lineHeight = lineHeight,
        maxLines = maxLines,
        minLines = minLines,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
        style = style,
        textAlign = textAlign
    )
}

@Composable
fun BodyText(
    stringResource: Int,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
){
    val textDecorations = if (underlined || strikethrough) {
        val decorations = mutableListOf<TextDecoration>()
        if (underlined) decorations.add(TextDecoration.Underline)
        if (strikethrough) decorations.add(TextDecoration.Underline)
        TextDecoration.combine(decorations)
    } else null

    Text(
        modifier = modifier,
        text = stringResource(stringResource),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = bold.takeIf { it }?.let { FontWeight.Bold },
        textDecoration = textDecorations,
        color = color,
        overflow = overflow,
        lineHeight = lineHeight,
        maxLines = maxLines,
        minLines = minLines,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
        style = style,
        textAlign = textAlign
    )
}

@Composable
fun SmallBodyText(
    stringResource: Int,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    softWrap: Boolean = true,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
){
    val textDecorations = if (underlined || strikethrough) {
        val decorations = mutableListOf<TextDecoration>()
        if (underlined) decorations.add(TextDecoration.Underline)
        if (strikethrough) decorations.add(TextDecoration.Underline)
        TextDecoration.combine(decorations)
    } else null

    Text(
        modifier = modifier,
        text = stringResource(stringResource),
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        fontWeight = bold.takeIf { it }?.let { FontWeight.Bold },
        textDecoration = textDecorations,
        color = color,
        overflow = overflow,
        lineHeight = lineHeight,
        maxLines = maxLines,
        minLines = minLines,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
        style = style,
        textAlign = textAlign
    )
}
