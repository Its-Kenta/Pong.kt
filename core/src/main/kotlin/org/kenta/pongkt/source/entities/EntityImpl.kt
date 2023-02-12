package org.kenta.pongkt.source.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class DynamicsImpl(initialPosition: Vector2, dimensions: Vector2, override var speed: Vector2, override var type: EntityType.Type) : Dynamics {
    override var position: Vector2 = initialPosition
    override var velocity: Vector2 = Vector2.Zero
    override var rectangle: Rectangle = Rectangle(this.position.x, this.position.y, dimensions.x, dimensions.y)

    override fun keepInBoundary() {
        if (type == EntityType.Type.ENEMY || type == EntityType.Type.PLAYER) {
            if (this.rectangle.y < 0F) {
                this.rectangle.y = 0F
            }

            if (this.rectangle.y >= Gdx.graphics.height - this.rectangle.height) {
                this.rectangle.y = Gdx.graphics.height - this.rectangle.height
            }
        }
    }
}

class CollidableImpl() : Collidable {
    override fun checkCollision(collider: Rectangle, collidesWith: Rectangle): Boolean {
        return Intersector.overlaps(collider, collidesWith)
    }

}
