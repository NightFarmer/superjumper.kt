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

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*

public object Assets {
    public val background: Texture = loadTexture("data/background.png")
    public val backgroundRegion: TextureRegion = TextureRegion(background, 0, 0, 320, 480)

    public val items: Texture = loadTexture("data/items.png")
    public val mainMenu: TextureRegion = TextureRegion(items, 0, 224, 300, 110)
    public val pauseMenu: TextureRegion = TextureRegion(items, 224, 128, 192, 96)
    public val ready: TextureRegion = TextureRegion(items, 320, 224, 192, 32)
    public val gameOver: TextureRegion = TextureRegion(items, 352, 256, 160, 96)
    public val highScoresRegion: TextureRegion = TextureRegion(items, 0, 257, 300, 110 / 3)
    public val logo: TextureRegion = TextureRegion(items, 0, 352, 274, 142)
    public val soundOff: TextureRegion = TextureRegion(items, 0, 0, 64, 64)
    public val soundOn: TextureRegion = TextureRegion(items, 64, 0, 64, 64)
    public val arrow: TextureRegion = TextureRegion(items, 0, 64, 64, 64)
    public val pause: TextureRegion = TextureRegion(items, 64, 64, 64, 64)
    public val spring: TextureRegion = TextureRegion(items, 128, 0, 32, 32)
    public val castle: TextureRegion = TextureRegion(items, 128, 64, 64, 64)
    public val coinAnim: Animation = Animation(0.2.toFloat(), TextureRegion(items, 128, 32, 32, 32), TextureRegion(items, 160, 32, 32, 32), TextureRegion(items, 192, 32, 32, 32), TextureRegion(items, 160, 32, 32, 32))
    public val bobJump: Animation = Animation(0.2.toFloat(), TextureRegion(items, 0, 128, 32, 32), TextureRegion(items, 32, 128, 32, 32))
    public val bobFall: Animation = Animation(0.2.toFloat(), TextureRegion(items, 64, 128, 32, 32), TextureRegion(items, 96, 128, 32, 32))
    public val bobHit: TextureRegion = TextureRegion(items, 128, 128, 32, 32)
    public val squirrelFly: Animation = Animation(0.2.toFloat(), TextureRegion(items, 0, 160, 32, 32), TextureRegion(items, 32, 160, 32, 32))
    public val platform: TextureRegion = TextureRegion(items, 64, 160, 64, 16)
    public val brakingPlatform: Animation = Animation(0.2.toFloat(), TextureRegion(items, 64, 160, 64, 16), TextureRegion(items, 64, 176, 64, 16), TextureRegion(items, 64, 192, 64, 16), TextureRegion(items, 64, 208, 64, 16))
    public val font: BitmapFont = BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false)

    public val music: Music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"))
    public val jumpSound: Sound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"))
    public val highJumpSound: Sound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"))
    public val hitSound: Sound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"))
    public val coinSound: Sound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"))
    public val clickSound: Sound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"))

            ;{
        music.setLooping(true)
        music.setVolume(0.5.toFloat())
        if (Settings.soundEnabled)
            music.play()

    }

    public fun loadTexture(file: String): Texture {
        return Texture(Gdx.files.internal(file))
    }

    public fun playSound(sound: Sound) {
        if (Settings.soundEnabled) sound.play(1f)
    }
}
