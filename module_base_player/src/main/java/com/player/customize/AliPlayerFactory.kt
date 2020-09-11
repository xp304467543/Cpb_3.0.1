package com.player.customize

import android.content.Context
import com.player.customize.player.PlayerFactory

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/10
 * @ Describe
 *
 */

class AliPlayerFactory : PlayerFactory<AliYunPlayer?>() {

    fun create(): AliPlayerFactory? {
        return AliPlayerFactory()
    }


    override fun createPlayer(context: Context?): AliYunPlayer {
        return AliYunPlayer(context!!)
    }
    companion object {
        fun create(): AliPlayerFactory {
            return AliPlayerFactory()
        }
    }
}
