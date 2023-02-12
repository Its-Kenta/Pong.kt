@file:JvmName("Lwjgl3Launcher")

package org.kenta.pongkt.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import org.kenta.pongkt.Game
import org.kenta.pongkt.source.utils.WINDOW_HEIGHT
import org.kenta.pongkt.source.utils.WINDOW_TITLE
import org.kenta.pongkt.source.utils.WINDOW_WIDTH

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(Game(), Lwjgl3ApplicationConfiguration().apply {
        setTitle(WINDOW_TITLE)
        useVsync(true)
        setResizable(false)
        setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
