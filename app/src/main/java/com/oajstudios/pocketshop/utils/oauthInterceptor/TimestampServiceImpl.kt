package com.oajstudios.pocketshop.utils.oauthInterceptor

import java.util.*

class TimestampServiceImpl : TimestampService {
    private var timer: Timer

    override val nonce: String
        get() {
            val ts = ts
            return (ts + timer.randomInteger).toString()
        }

    override val timestampInSeconds: String
        get() {
            return (ts).toString()
        }
    private val ts: Long
        get() {
            return timer.milis / 1000
        }

    init {
        timer = Timer()
    }

    internal fun setTimer(timer: Timer) {
        this.timer = timer
    }

    internal class Timer {
        private val rand = Random()
        val milis: Long
            get() {
                return System.currentTimeMillis()
            }
        val randomInteger: Int
            get() {
                return rand.nextInt()
            }
    }
}