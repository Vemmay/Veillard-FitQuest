package com.example.fitquest.component.compose_icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Directions_run: ImageVector
    get() {
        if (_Directions_run != null) {
            return _Directions_run!!
        }
        _Directions_run = ImageVector.Builder(
            name = "Directions_run",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(520f, 920f)
                verticalLineToRelative(-240f)
                lineToRelative(-84f, -80f)
                lineToRelative(-40f, 176f)
                lineToRelative(-276f, -56f)
                lineToRelative(16f, -80f)
                lineToRelative(192f, 40f)
                lineToRelative(64f, -324f)
                lineToRelative(-72f, 28f)
                verticalLineToRelative(136f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-188f)
                lineToRelative(158f, -68f)
                quadToRelative(35f, -15f, 51.5f, -19.5f)
                reflectiveQuadTo(480f, 240f)
                quadToRelative(21f, 0f, 39f, 11f)
                reflectiveQuadToRelative(29f, 29f)
                lineToRelative(40f, 64f)
                quadToRelative(26f, 42f, 70.5f, 69f)
                reflectiveQuadTo(760f, 440f)
                verticalLineToRelative(80f)
                quadToRelative(-66f, 0f, -123.5f, -27.5f)
                reflectiveQuadTo(540f, 420f)
                lineToRelative(-24f, 120f)
                lineToRelative(84f, 80f)
                verticalLineToRelative(300f)
                close()
                moveToRelative(20f, -700f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(460f, 140f)
                reflectiveQuadToRelative(23.5f, -56.5f)
                reflectiveQuadTo(540f, 60f)
                reflectiveQuadToRelative(56.5f, 23.5f)
                reflectiveQuadTo(620f, 140f)
                reflectiveQuadToRelative(-23.5f, 56.5f)
                reflectiveQuadTo(540f, 220f)
            }
        }.build()
        return _Directions_run!!
    }

private var _Directions_run: ImageVector? = null
