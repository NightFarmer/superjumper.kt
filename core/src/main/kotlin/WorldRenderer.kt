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

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

public class WorldRenderer(var batch: SpriteBatch, var world: World) {
    var cam: OrthographicCamera

    {
        this.cam = OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT)
        this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0f)
    }

    public fun render() {
        if (world.bob.position.y > cam.position.y) cam.position.y = world.bob.position.y
        cam.update()
        batch.setProjectionMatrix(cam.combined)
        renderBackground()
        renderObjects()
    }

    public fun renderBackground() {
        batch.disableBlending()
        batch.begin()
        batch.draw(Assets.backgroundRegion, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH, FRUSTUM_HEIGHT)
        batch.end()
    }

    public fun renderObjects() {
        batch.enableBlending()
        batch.begin()
        renderBob()
        renderPlatforms()
        renderItems()
        renderSquirrels()
        renderCastle()
        batch.end()
    }

    private fun renderBob() {
        val keyFrame: TextureRegion
        when (world.bob.state) {
            Bob.BOB_STATE_FALL -> keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.Type.Looping)
            Bob.BOB_STATE_JUMP -> keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.Type.Looping)
            else -> keyFrame = Assets.bobHit
        }

        val side = (if (world.bob.velocity.x < 0) -1 else 1).toFloat()
        if (side < 0)
            batch.draw(keyFrame, world.bob.position.x + 0.5.toFloat(), world.bob.position.y - 0.5.toFloat(), side * 1, 1f)
        else
            batch.draw(keyFrame, world.bob.position.x - 0.5.toFloat(), world.bob.position.y - 0.5.toFloat(), side * 1, 1f)
    }

    private fun renderPlatforms() {
        val len = world.platforms.size()
        for (i in 0..len - 1) {
            val platform = world.platforms.get(i)
            var keyFrame = Assets.platform
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
                keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.Type.Once)
            }

            batch.draw(keyFrame, platform.position.x - 1, platform.position.y - 0.25.toFloat(), 2f, 0.5.toFloat())
        }
    }

    private fun renderItems() {
        var len = world.springs.size()
        for (i in 0..len - 1) {
            val spring = world.springs.get(i)
            batch.draw(Assets.spring, spring.position.x - 0.5.toFloat(), spring.position.y - 0.5.toFloat(), 1f, 1f)
        }

        len = world.coins.size()
        for (i in 0..len - 1) {
            val coin = world.coins.get(i)
            val keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.Type.Looping)
            batch.draw(keyFrame, coin.position.x - 0.5.toFloat(), coin.position.y - 0.5.toFloat(), 1f, 1f)
        }
    }

    private fun renderSquirrels() {
        val len = world.squirrels.size()
        for (i in 0..len - 1) {
            val squirrel = world.squirrels.get(i)
            val keyFrame = Assets.squirrelFly.getKeyFrame(squirrel.stateTime, Animation.Type.Looping)
            val side = (if (squirrel.velocity.x < 0) -1 else 1).toFloat()
            if (side < 0)
                batch.draw(keyFrame, squirrel.position.x + 0.5.toFloat(), squirrel.position.y - 0.5.toFloat(), side * 1, 1f)
            else
                batch.draw(keyFrame, squirrel.position.x - 0.5.toFloat(), squirrel.position.y - 0.5.toFloat(), side * 1, 1f)
        }
    }

    private fun renderCastle() {
        val castle = world.castle
        batch.draw(Assets.castle, castle.position.x - 1, castle.position.y - 1, 2f, 2f)
    }

    class object {
        val FRUSTUM_WIDTH: Float = 10f
        val FRUSTUM_HEIGHT: Float = 15f
    }
}
