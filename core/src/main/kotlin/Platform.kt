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

public class Platform(var type: Int, x: Float, y: Float) : DynamicGameObject(x, y, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT) {
    var state: Int = PLATFORM_STATE_NORMAL
    var stateTime: Float = 0f;

    {
        if (type == PLATFORM_TYPE_MOVING) {
            velocity.x = PLATFORM_VELOCITY
        }
    }

    public fun update(deltaTime: Float) {
        if (type == PLATFORM_TYPE_MOVING) {
            position.add(velocity.x * deltaTime, 0f)
            bounds.x = position.x - PLATFORM_WIDTH / 2
            bounds.y = position.y - PLATFORM_HEIGHT / 2

            if (position.x < PLATFORM_WIDTH / 2) {
                velocity.x = -velocity.x
                position.x = PLATFORM_WIDTH / 2
            }
            if (position.x > World.WORLD_WIDTH - PLATFORM_WIDTH / 2) {
                velocity.x = -velocity.x
                position.x = World.WORLD_WIDTH - PLATFORM_WIDTH / 2
            }
        }

        stateTime += deltaTime
    }

    public fun pulverize() {
        state = PLATFORM_STATE_PULVERIZING
        stateTime = 0f
        velocity.x = 0f
    }

    class object {
        public val PLATFORM_WIDTH: Float = 2f
        public val PLATFORM_HEIGHT: Float = 0.5.toFloat()
        public val PLATFORM_TYPE_STATIC: Int = 0
        public val PLATFORM_TYPE_MOVING: Int = 1
        public val PLATFORM_STATE_NORMAL: Int = 0
        public val PLATFORM_STATE_PULVERIZING: Int = 1
        public val PLATFORM_PULVERIZE_TIME: Float = 0.2.toFloat() * 4
        public val PLATFORM_VELOCITY: Float = 2f
    }
}
