package org.kenta.pongkt.source.scene

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import ktx.app.KtxScreen
import org.kenta.pongkt.Game
import org.kenta.pongkt.source.utils.BitmapFontAsset
import org.kenta.pongkt.source.utils.SoundAsset

class LoadingScreen(private val game: Game, private val assets: AssetManager = game.assets) : KtxScreen {
    private var assetsFinished = false

    override fun show() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        val old = System.currentTimeMillis()
        SoundAsset.values().forEach { assets.load(it.descriptor) }
        BitmapFontAsset.values().forEach { assets.load(it.descriptor) }
        Gdx.app.debug("Loading Screen", "It took ${(System.currentTimeMillis() - old) * 0.001f} seconds to load assets and initialise.");
    }

    private fun assetsLoaded() {
        game.addScreen(GameScene(game))
    }

    override fun render(delta: Float) {
        if (assetsFinished && game.containsScreen<GameScene>()) {
            game.removeScreen(LoadingScreen::class.java)
            dispose()
            game.setScreen<GameScene>()
        }

        if (assets.update() && !assetsFinished) {
            assetsFinished = true
            assetsLoaded()
        }
    }

    override fun dispose() {
        Gdx.app.logLevel = Application.LOG_NONE
    }
}
