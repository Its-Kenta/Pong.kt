package org.kenta.pongkt.source.entities

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

// Defining components.
// Internal components -- START

interface Shape {
    var rectangle: Rectangle
}

interface Position {
    var position: Vector2
}

interface Velocity {
    var velocity: Vector2
    var speed: Vector2
}

interface EntityType {
    enum class Type {
        PLAYER,
        ENEMY,
        OBJECT
    }
}

interface Drawable : Shape, Position

interface Updatable {
    fun update(delta: Float)
}
// Internal components -- END

interface Dynamics : Shape, Position, Velocity, EntityType {
    override var velocity: Vector2
    override var speed: Vector2
    override var rectangle: Rectangle
    var type: EntityType.Type

    fun keepInBoundary()
}

interface Collidable {
    fun checkCollision(collider: Rectangle, collidesWith: Rectangle) : Boolean
}
