package org.kenta.pongkt.source.utils

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion


private val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).also { it.setColor(Color.WHITE); it.drawPixel(0, 0) }
private val texture = Texture(pixmap)
val region = TextureRegion(texture, 0, 0, 1,1)

enum class SoundAsset(fileName: String, directory: String = "sound", val descriptor: AssetDescriptor<Sound> = AssetDescriptor("$directory/$fileName", Sound::class.java)) {
    PAD_HIT("padHit.wav"),
    WALL_HIT("wallHit.wav"),
    SCORED("scored.wav"),
}

enum class BitmapFontAsset(fileName: String, directory: String = "fonts", val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor("$directory/$fileName", BitmapFont::class.java)) {
    STANDARD_FONT("m5x7.fnt"),
    STANDARD_SCORE_FONT("m5x7s.fnt"),
}
