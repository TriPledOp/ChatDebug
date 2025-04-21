package dev.tripledop.chatdebug.client

import net.minecraft.util.Formatting
import org.entur.jackson.jsh.SyntaxHighlighter
import java.math.BigDecimal
import java.math.BigInteger

class MCJsonSyntaxHighlight : SyntaxHighlighter {
    override fun forCurlyBrackets() = "§" + Formatting.WHITE.code
    override fun forSquareBrackets() = "§" + Formatting.WHITE.code
    override fun forColon() = "§" + Formatting.WHITE.code
    override fun forComma() = "§" + Formatting.WHITE.code
    override fun forWhitespace() = ""
    override fun forFieldName(value: String?) = "§" + Formatting.LIGHT_PURPLE.code
    override fun forNumber(value: Int) = "§" + Formatting.AQUA.code
    override fun forNumber(value: Double) = "§" + Formatting.AQUA.code
    override fun forNumber(value: Long) = "§" + Formatting.AQUA.code
    override fun forNumber(v: BigInteger?) = "§" + Formatting.AQUA.code
    override fun forNumber(v: BigDecimal?) = "§" + Formatting.AQUA.code
    override fun forNumber(encodedValue: String?) = "§" + Formatting.AQUA.code
    override fun forString(string: String?) = "§" + Formatting.GREEN.code
    override fun forBinary() = "§" + Formatting.AQUA.code
    override fun forBoolean(value: Boolean) = "§" + Formatting.GOLD.code
    override fun forNull() = "§" + Formatting.GRAY.code
}