/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicgames.superjumper

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

public open class GameObject(x: Float, y: Float, width: Float, height: Float) {
    public val position: Vector2 = Vector2(x, y)
    public val bounds: Rectangle = Rectangle(x - width / 2, y - height / 2, width, height)
}

public open class DynamicGameObject(x: Float, y: Float, width: Float, height: Float) : GameObject(x, y, width, height) {
    public val velocity: Vector2 = Vector2()
    public val accel: Vector2 = Vector2()
}

public class Castle(x: Float, y: Float) : GameObject(x, y, 1.7f, 1.7f)

public class Spring(x: Float, y: Float) : GameObject(x, y, Spring.SPRING_WIDTH, Spring.SPRING_HEIGHT) {
    class object {
        public var SPRING_WIDTH: Float = 0.3f
        public var SPRING_HEIGHT: Float = 0.3f
    }
}

public class Coin(x: Float, y: Float) : GameObject(x, y, Coin.COIN_WIDTH, Coin.COIN_HEIGHT) {
    var stateTime: Float = 0f

    public fun update(deltaTime: Float) {
        stateTime += deltaTime
    }

    class object {
        public val COIN_WIDTH: Float = 0.5f
        public val COIN_HEIGHT: Float = 0.8f
        public val COIN_SCORE: Int = 10
    }
}

public class Squirrel(x: Float, y: Float) : DynamicGameObject(x, y, Squirrel.SQUIRREL_WIDTH, Squirrel.SQUIRREL_HEIGHT) {

    var stateTime: Float = 0f

    {
        velocity.set(SQUIRREL_VELOCITY, 0f)
    }

    public fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        bounds.x = position.x - SQUIRREL_WIDTH / 2
        bounds.y = position.y - SQUIRREL_HEIGHT / 2

        if (position.x < SQUIRREL_WIDTH / 2) {
            position.x = SQUIRREL_WIDTH / 2
            velocity.x = SQUIRREL_VELOCITY
        }
        if (position.x > World.WORLD_WIDTH - SQUIRREL_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - SQUIRREL_WIDTH / 2
            velocity.x = -SQUIRREL_VELOCITY
        }
        stateTime += deltaTime
    }

    class object {
        public val SQUIRREL_WIDTH: Float = 1f
        public val SQUIRREL_HEIGHT: Float = 0.6.toFloat()
        public val SQUIRREL_VELOCITY: Float = 3.toFloat()
    }
}
