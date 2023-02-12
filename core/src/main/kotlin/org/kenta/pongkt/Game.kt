package org.kenta.pongkt

import com.badlogic.gdx.assets.AssetManager
import ktx.app.KtxGame
import ktx.app.KtxScreen
import org.kenta.pongkt.source.scene.LoadingScreen
import org.kenta.pongkt.source.utils.BitmapFontAsset
import org.kenta.pongkt.source.utils.SoundAsset

class Game : KtxGame<KtxScreen>() {
    val assets: AssetManager by lazy { AssetManager() }
    override fun create() {
        SoundAsset.values().forEach { assets.load(it.descriptor) }
        BitmapFontAsset.values().forEach { assets.load(it.descriptor) }
        assets.finishLoading()
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        assets.dispose()
    }
}
