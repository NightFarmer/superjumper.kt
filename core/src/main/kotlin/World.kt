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

import java.util.ArrayList
import java.util.Random

import com.badlogic.gdx.math.Vector2

public class World(public val listener: World.WorldListener) {
    public trait WorldListener {
        public fun jump()

        public fun highJump()

        public fun hit()

        public fun coin()
    }

    public val bob: Bob = Bob(5f, 1f)
    public val platforms: ArrayList<Platform> = ArrayList<Platform>()
    public val springs: ArrayList<Spring> = ArrayList<Spring>()
    public val squirrels: ArrayList<Squirrel> = ArrayList<Squirrel>()
    public val coins: ArrayList<Coin> = ArrayList<Coin>()
    public val castle: Castle
    public val rand: Random = Random()

    public var heightSoFar: Float = 0.toFloat()
    public var score: Int = 0
    public var state: Int = WORLD_STATE_RUNNING

    {
        var y = Platform.PLATFORM_HEIGHT / 2
        val maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY / (2 * -gravity.y)
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            val type = if (rand.nextFloat() > 0.8.toFloat()) Platform.PLATFORM_TYPE_MOVING else Platform.PLATFORM_TYPE_STATIC
            val x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH / 2

            val platform = Platform(type, x, y)
            platforms.add(platform)

            if (rand.nextFloat() > 0.9.toFloat() && type != Platform.PLATFORM_TYPE_MOVING) {
                val spring = Spring(platform.position.x, platform.position.y + Platform.PLATFORM_HEIGHT / 2 + Spring.SPRING_HEIGHT / 2)
                springs.add(spring)
            }

            if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8.toFloat()) {
                val squirrel = Squirrel(platform.position.x + rand.nextFloat(), platform.position.y + Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * 2)
                squirrels.add(squirrel)
            }

            if (rand.nextFloat() > 0.6.toFloat()) {
                val coin = Coin(platform.position.x + rand.nextFloat(), platform.position.y + Coin.COIN_HEIGHT + rand.nextFloat() * 3)
                coins.add(coin)
            }

            y += (maxJumpHeight - 0.5.toFloat())
            y -= rand.nextFloat() * (maxJumpHeight / 3)
        }

        castle = Castle(WORLD_WIDTH / 2, y)
    }

    public fun update(deltaTime: Float, accelX: Float) {
        updateBob(deltaTime, accelX)
        updatePlatforms(deltaTime)
        updateSquirrels(deltaTime)
        updateCoins(deltaTime)
        if (bob.state != Bob.BOB_STATE_HIT) checkCollisions()
        checkGameOver()
    }

    private fun updateBob(deltaTime: Float, accelX: Float) {
        if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5.toFloat()) bob.hitPlatform()
        if (bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY
        bob.update(deltaTime)
        heightSoFar = Math.max(bob.position.y, heightSoFar)
    }

    private fun updatePlatforms(deltaTime: Float) {
        for (platform in platforms.toList()) {
            platform.update(deltaTime)
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
                platforms.remove(platform)
            }
        }
    }

    private fun updateSquirrels(deltaTime: Float) {
        for (squirrel in squirrels.toList())
            squirrel.update(deltaTime)
    }

    private fun updateCoins(deltaTime: Float) {
        for (coin in coins.toList())
            coin.update(deltaTime)
    }

    private fun checkCollisions() {
        checkPlatformCollisions()
        checkSquirrelCollisions()
        checkItemCollisions()
        checkCastleCollisions()
    }

    private fun checkPlatformCollisions() {
        if (bob.velocity.y > 0)
            return

        for (platform in platforms.toList()) {
            if (bob.position.y > platform.position.y) {
                if (bob.bounds.overlaps(platform.bounds)) {
                    bob.hitPlatform()
                    listener.jump()
                    if (rand.nextFloat() > 0.5f) {
                        platform.pulverize()
                    }
                    break
                }
            }
        }
    }

    private fun checkSquirrelCollisions() {
        for (squirrel in squirrels.toList()) {
            if (squirrel.bounds.overlaps(bob.bounds)) {
                bob.hitSquirrel()
                listener.hit()
            }
        }
    }

    private fun checkItemCollisions() {
        for (coin in coins.toList()) {
            if (bob.bounds.overlaps(coin.bounds)) {
                coins.remove(coin)
                listener.coin()
                score += Coin.COIN_SCORE
            }
        }

        if (bob.velocity.y > 0) return

        for (spring in springs.toList()) {
            if (bob.position.y > spring.position.y) {
                if (bob.bounds.overlaps(spring.bounds)) {
                    bob.hitSpring()
                    listener.highJump()
                }
            }
        }
    }

    private fun checkCastleCollisions() {
        if (castle.bounds.overlaps(bob.bounds)) {
            state = WORLD_STATE_NEXT_LEVEL
        }
    }

    private fun checkGameOver() {
        if (heightSoFar - 7.5f > bob.position.y) {
            state = WORLD_STATE_GAME_OVER
        }
    }

    class object {
        public val WORLD_WIDTH: Float = 10f
        public val WORLD_HEIGHT: Float = 15f * 20
        public val WORLD_STATE_RUNNING: Int = 0
        public val WORLD_STATE_NEXT_LEVEL: Int = 1
        public val WORLD_STATE_GAME_OVER: Int = 2
        public val gravity: Vector2 = Vector2(0f, -12f)
    }
}
