package org.kenta.pongkt.source.scene

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import org.kenta.pongkt.Game
import org.kenta.pongkt.source.entities.*
import org.kenta.pongkt.source.utils.*
import org.kenta.pongkt.source.utils.Utils.changeBackgroundColor
import org.kenta.pongkt.source.utils.Utils.createFlashingText
import space.earlygrey.shapedrawer.ShapeDrawer
import kotlin.random.Random

var serveNumber: Int = Random.nextInt(2)

class GameScene(private val game: Game, private val assets: AssetManager = game.assets) : KtxScreen {

    enum class GameState {
        IDLE,
        SERVE,
        START,
        SCORED,
        OUTCOME,
    }

    // GameScene Variables
    // ==================================================== //
    private var batch = PolygonSpriteBatch()
    private val camera = OrthographicCamera().also { it.setToOrtho(false, WINDOW_WIDTH.toFloat(), WINDOW_HEIGHT.toFloat() ) }
    private val shapeDrawer = ShapeDrawer(batch, region)
    private var currentState = GameState.IDLE
    private var pScored: Boolean = false
    private var aScored: Boolean = false
    private var playerScore: Int = 0
    private var aiScore: Int = 0

    // Entities
    // ==================================================== //
    private val player = Player.new(Vector2(20F, 300F), Vector2(10F, 120F), Vector2(0F, 490F))
    private val ball = Ball.new(Vector2(WINDOW_WIDTH.toFloat()/2 - 8, 370F), Vector2(15F, 15F), Vector2(700F, 700F))
    private val ai = AI.new(ball, Vector2(WINDOW_WIDTH.toFloat() - 30, 300F), Vector2(10F, 120F), Vector2(0F, 780F))

    // Sequence of Entities
    private val entities: Sequence<Any> = sequenceOf(player, ball, ai)

    override fun show() {
        assets[BitmapFontAsset.STANDARD_SCORE_FONT.descriptor].data.scale(1.3F)
    }

    // Game Functions
    // ==================================================== //

    // Updating game
    private fun update(delta: Float) {
        // Update entities.
        entities.filterIsInstance<Updatable>().forEach { updatable -> updatable.update(delta) }

        // Check for collisions
        if (ball.checkCollision(ball.rectangle, player.rectangle) || ball.checkCollision(ball.rectangle, ai.rectangle)) {
            assets[SoundAsset.PAD_HIT.descriptor].play()
            ball.speed.x *= -1.05F
        }

        if (ball.rectangle.y < 5 || ball.rectangle.y >= Gdx.graphics.height - 15) {
            assets[SoundAsset.WALL_HIT.descriptor].play()
            ball.speed.y *= -1
        }

        if (ball.speed.x.coerceAtMost(1600F) == 1600F) ball.speed.x = 1600F
    }

    // Render and call our update function.
    override fun render(delta: Float) {
        changeBackgroundColor()
        camera.update()

        batch.use {
            it.projectionMatrix = camera.combined

            // Draw entities.
            entities.filterIsInstance<Drawable>().forEach { drawable -> shapeDrawer.filledRectangle(drawable.rectangle) }
            // Draw AI & Player score
            assets[BitmapFontAsset.STANDARD_SCORE_FONT.descriptor].draw(it, "$playerScore", WINDOW_WIDTH.toFloat()/2 - 120, WINDOW_HEIGHT.toFloat()/2 + 270)
            assets[BitmapFontAsset.STANDARD_SCORE_FONT.descriptor].draw(it, "$aiScore", WINDOW_WIDTH.toFloat()/2 + 90, WINDOW_HEIGHT.toFloat()/2 + 270)

            when (currentState) {
                GameState.IDLE -> {
                    createFlashingText(it, assets[BitmapFontAsset.STANDARD_FONT.descriptor], "PRESS SPACE TO START!", Vector2(WINDOW_WIDTH.toFloat()/2 - 120, WINDOW_HEIGHT.toFloat()/2 + 150))

                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        currentState = GameState.SERVE
                    }
                }

                GameState.SERVE -> {
                    createFlashingText(it, assets[BitmapFontAsset.STANDARD_FONT.descriptor], "PRESS SPACE TO SERVE!", Vector2(WINDOW_WIDTH.toFloat()/2 - 120, WINDOW_HEIGHT.toFloat()/2 + 150))

                    when (serveNumber) {
                        0 -> assets[BitmapFontAsset.STANDARD_FONT.descriptor].draw(it, "AI SERVES!", (WINDOW_WIDTH.toFloat()/2) - 60, (WINDOW_HEIGHT.toFloat()/2) + 100)
                        1 -> assets[BitmapFontAsset.STANDARD_FONT.descriptor].draw(it, "PLAYER SERVES!", (WINDOW_WIDTH.toFloat()/2) - 80, (WINDOW_HEIGHT.toFloat()/2) + 100)
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        currentState = GameState.START
                    }
                }

                GameState.START -> {
                    update(delta)
                    if (ball.rectangle.x > WINDOW_WIDTH - 4) {
                        assets[SoundAsset.SCORED.descriptor].play()
                        playerScore++
                        pScored = true
                        currentState = GameState.SCORED
                    }

                    if (ball.rectangle.x <= -4) {
                        assets[SoundAsset.SCORED.descriptor].play()
                        aiScore++
                        aScored = true
                        currentState = GameState.SCORED
                    }
                }

                GameState.SCORED -> {
                    if (pScored && playerScore != 4) {
                        createFlashingText(it, assets[BitmapFontAsset.STANDARD_FONT.descriptor], "YOU SCORED! PRESS SPACE TO SERVE!", Vector2(
                            WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))

                        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                            reset(playerScored = true, aiScored = false, state = GameState.SERVE)
                        }
                    } else {
                        createFlashingText(it, assets[BitmapFontAsset.STANDARD_FONT.descriptor], "AI SCORED! PRESS SPACE TO CONTINUE!", Vector2(
                            WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))

                        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                            reset(playerScored = false, aiScored = true, state = GameState.SERVE)
                        }
                    }

                    if (aiScore == 4 || playerScore == 4) {
                        currentState = GameState.OUTCOME
                    }
                }

                GameState.OUTCOME -> {

                    if (aiScore == 4) {
                        createFlashingText(it, assets[BitmapFontAsset.STANDARD_FONT.descriptor], "YOU LOST! PRESS SPACE TO RESTART!", Vector2(
                            WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))
                    } else {
                        createFlashingText(it, assets[BitmapFontAsset.STANDARD_FONT.descriptor], "YOU WON! PRESS SPACE TO RESTART!", Vector2(
                            WINDOW_WIDTH.toFloat()/2 - 197, WINDOW_HEIGHT.toFloat()/2 + 150))
                    }

                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        reset(playerScored = false, aiScored = false, state = GameState.IDLE)
                    }
                }
            }
        }
        exit()
    }

    private fun reset(playerScored: Boolean, aiScored: Boolean, state: GameState) {
        ball.also {
            it.rectangle.x = WINDOW_WIDTH.toFloat()/2 - 8
            it.rectangle.y = 370F
            it.speed.x = 700F
            it.speed.y = 700F
        }

        player.also {
            it.rectangle.y = 300F
        }

        ai.also {
            it.rectangle.y = 300F
        }

        currentState = state

        serveNumber = if (playerScored) {
            1
        } else if (aiScored) {
            0
        } else {
            Random.nextInt(0, 2)
        }

        if (playerScored) {
            pScored = false
        } else {
            aScored = false
        }

        if (!playerScored && !aiScored) {
            playerScore = 0
            aiScore = 0
        }
    }

    // If ESC key is pressed, quit game.
    private fun exit() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    // Free memory on exit.
    // ==================================================== //
    override fun dispose() {
        batch.dispose()
        this.disposeSafely()
    }
}
