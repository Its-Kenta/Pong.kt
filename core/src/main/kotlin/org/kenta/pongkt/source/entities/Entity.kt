package org.kenta.pongkt.source.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import org.kenta.pongkt.source.scene.serveNumber
import org.kenta.pongkt.source.utils.WINDOW_WIDTH
import kotlin.math.pow

// Player Entity creation with allocated delegates.
class Player(dynamicsImpl: Dynamics) : Drawable, Updatable, Dynamics by dynamicsImpl {

    override fun update(delta: Float) {
        velocity.setZero()

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y += 1
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y -= 1
        }

        velocity.nor()
        rectangle.y += velocity.y * speed.y * delta

        this.keepInBoundary()
    }


    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): Player {
            return Player(DynamicsImpl(initialPosition, dimensions, speed, EntityType.Type.PLAYER))
        }
    }
}

// AI Entity creation with allocated delegates.
class AI(private val ball: Ball, dynamicsImpl: Dynamics) : Drawable, Updatable, Dynamics by dynamicsImpl {

    override fun update(delta: Float) {
        if ((ball.rectangle.x - rectangle.x).pow(2).pow(0.5F) < WINDOW_WIDTH /2) {
            if (rectangle.y > (ball.rectangle.y + ball.rectangle.height/2)) {
                rectangle.y -= velocity.y * speed.y * delta
            } else if (rectangle.y + rectangle.height < (ball.rectangle.y + ball.rectangle.height/2)) {
                rectangle.y += velocity.y * speed.y * delta
            }
        }
    }

    companion object {
        fun new(ball: Ball, initialPosition: Vector2, dimensions: Vector2, speed: Vector2): AI {
            return AI(ball, DynamicsImpl(initialPosition, dimensions, speed, EntityType.Type.ENEMY))
        }
    }
}

// Ball Entity creation with allocated delegates.
class Ball(dynamicsImpl: Dynamics, collidableImpl: Collidable) : Drawable, Updatable, Dynamics by dynamicsImpl, Collidable by collidableImpl {

    override fun update(delta: Float) {
        velocity.setZero()

        when (serveNumber) {
            0 -> {
                velocity.x += 1
                velocity.y += 1
            }

            1 -> {
                velocity.x -= 1
                velocity.y += 1
            }
        }

        velocity.nor()

        rectangle.y += velocity.y * speed.y * delta
        rectangle.x += velocity.x * speed.x * delta
    }

    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): Ball {
            return Ball(DynamicsImpl(initialPosition, dimensions, speed, EntityType.Type.OBJECT), CollidableImpl())
        }
    }
}
