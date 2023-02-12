@file:JvmName("TeaVMLauncher")

package org.kenta.pongkt.teavm

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration
import com.github.xpenatan.gdx.backends.web.WebApplication
import org.kenta.pongkt.Game
import org.kenta.pongkt.source.utils.WINDOW_HEIGHT
import org.kenta.pongkt.source.utils.WINDOW_WIDTH

/** Launches the TeaVM/HTML application. */
fun main() {
    val config = TeaApplicationConfiguration("canvas").apply {
        width = WINDOW_WIDTH
        height = WINDOW_HEIGHT
    }
    WebApplication(Game(), config)
}
