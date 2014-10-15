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

public class Bob(x: Float, y: Float) : DynamicGameObject(x, y, Bob.BOB_WIDTH, Bob.BOB_HEIGHT) {

    var state: Int = BOB_STATE_FALL
    var stateTime: Float = 0f

    public fun update(deltaTime: Float) {
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime)
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        bounds.x = position.x - bounds.width / 2
        bounds.y = position.y - bounds.height / 2

        if (velocity.y > 0 && state != BOB_STATE_HIT) {
            if (state != BOB_STATE_JUMP) {
                state = BOB_STATE_JUMP
                stateTime = 0f
            }
        }

        if (velocity.y < 0 && state != BOB_STATE_HIT) {
            if (state != BOB_STATE_FALL) {
                state = BOB_STATE_FALL
                stateTime = 0f
            }
        }

        if (position.x < 0) position.x = World.WORLD_WIDTH
        if (position.x > World.WORLD_WIDTH) position.x = 0f

        stateTime += deltaTime
    }

    public fun hitSquirrel() {
        velocity.set(0f, 0f)
        state = BOB_STATE_HIT
        stateTime = 0f
    }

    public fun hitPlatform() {
        velocity.y = BOB_JUMP_VELOCITY
        state = BOB_STATE_JUMP
        stateTime = 0f
    }

    public fun hitSpring() {
        velocity.y = BOB_JUMP_VELOCITY * 1.5.toFloat()
        state = BOB_STATE_JUMP
        stateTime = 0f
    }

    class object {
        public val BOB_STATE_JUMP: Int = 0
        public val BOB_STATE_FALL: Int = 1
        public val BOB_STATE_HIT: Int = 2
        public val BOB_JUMP_VELOCITY: Float = 11f
        public val BOB_MOVE_VELOCITY: Float = 20f
        public val BOB_WIDTH: Float = 0.8.toFloat()
        public val BOB_HEIGHT: Float = 0.8.toFloat()
    }
}
