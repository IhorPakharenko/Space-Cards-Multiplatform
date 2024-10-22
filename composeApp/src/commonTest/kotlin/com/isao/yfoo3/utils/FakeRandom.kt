package com.isao.yfoo3.utils

import kotlin.random.Random

class FakeRandom(
    val expectedIntFrom: Int? = null,
    val expectedIntUntil: Int? = null,
    val fakeInt: Int = Int.MIN_VALUE,
    val expectedLongFrom: Long? = null,
    val expectedLongUntil: Long? = null,
    val fakeLong: Long = Long.MIN_VALUE,
    val expectedDoubleFrom: Double? = null,
    val expectedDoubleUntil: Double? = null,
    val fakeDouble: Double = Double.MIN_VALUE,
    val expectedBoolean: Boolean? = null
) : Random() {

    override fun nextBits(bitCount: Int): Int {
        throw Exception("Not implemented")
    }

    override fun nextInt(): Int {
        return fakeInt
    }

    override fun nextInt(until: Int): Int {
        return if (until == expectedIntUntil) fakeInt else super.nextInt(until)
    }

    override fun nextInt(from: Int, until: Int): Int {
        return if (from == expectedIntFrom && until == expectedIntUntil) {
            fakeInt
        } else {
            super.nextInt(from, until)
        }
    }

    override fun nextLong(): Long {
        return fakeLong
    }

    override fun nextLong(until: Long): Long {
        return if (until == expectedLongUntil) fakeLong else super.nextLong(until)
    }

    override fun nextLong(from: Long, until: Long): Long {
        return if (from == expectedLongFrom && until == expectedLongUntil) {
            fakeLong
        } else {
            super.nextLong(from, until)
        }
    }

    override fun nextBoolean(): Boolean {
        return expectedBoolean ?: super.nextBoolean()
    }

    override fun nextDouble(): Double {
        return fakeDouble
    }

    override fun nextDouble(until: Double): Double {
        return if (until == expectedDoubleUntil) fakeDouble else super.nextDouble(until)
    }

    override fun nextDouble(from: Double, until: Double): Double {
        return if (from == expectedDoubleFrom && until == expectedDoubleUntil) {
            fakeDouble
        } else {
            super.nextDouble(from, until)
        }
    }
}
