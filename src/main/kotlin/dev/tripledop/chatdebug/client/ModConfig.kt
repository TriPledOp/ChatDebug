@file:Suppress("unused")

package dev.tripledop.chatdebug.client

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "chatdebug")
class ModConfig : ConfigData {
    @ConfigEntry.Gui.PrefixText
    var isEnabled = true
}