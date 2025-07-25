package com.abdownloadmanager.desktop.pages.settings.configurable

import com.abdownloadmanager.shared.utils.ui.myColors
import com.abdownloadmanager.shared.utils.ui.theme.myTextSizes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import com.abdownloadmanager.shared.ui.widget.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.abdownloadmanager.desktop.pages.settings.ThemeInfo
import com.abdownloadmanager.desktop.utils.configurable.BaseEnumConfigurable
import ir.amirab.util.compose.StringSource
import ir.amirab.util.ifThen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeConfigurable(
    title: StringSource,
    description: StringSource,
    backedBy: MutableStateFlow<ThemeInfo>,
    describe: (ThemeInfo) -> StringSource,
    possibleValues: List<ThemeInfo>,
    valueToString: (ThemeInfo) -> List<String> = {
        listOf(it.name.getString())
    },
    enabled: StateFlow<Boolean> = DefaultEnabledValue,
    visible: StateFlow<Boolean> = DefaultVisibleValue,
) : BaseEnumConfigurable<ThemeInfo>(
    title = title,
    description = description,
    backedBy = backedBy,
    describe = describe,
    possibleValues = possibleValues,
    valueToString = valueToString,
    enabled = enabled,
    visible = visible,
) {
    @Composable
    override fun render(modifier: Modifier) {
        RenderThemeConfig(this, modifier)
    }
}


@Composable
private fun RenderThemeConfig(cfg: ThemeConfigurable, modifier: Modifier) {
    val value by cfg.stateFlow.collectAsState()
    val setValue = cfg::set
    val enabled = isConfigEnabled()
    ConfigTemplate(
        modifier = modifier,
        title = {
            TitleAndDescription(cfg, true)
        },
        value = {
            RenderSpinner(
                possibleValues = cfg.possibleValues, value = value, onSelect = {
                    setValue(it)
                },
                modifier = Modifier.widthIn(min = 160.dp),
                enabled = enabled,
                valueToString = cfg.valueToString,
                render = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.ifThen(!enabled) {
                            alpha(0.5f)
                        }
                    ) {
                        Spacer(
                            Modifier
                                .clip(CircleShape)
                                .border(
                                    1.dp,
                                    Brush.verticalGradient(myColors.primaryGradientColors),
                                    CircleShape
                                )
                                .padding(1.dp)
                                .background(
                                    it.color,
                                )
                                .size(16.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(cfg.describe(it).rememberString(), fontSize = myTextSizes.lg)
                    }
                })
        }
    )
}
