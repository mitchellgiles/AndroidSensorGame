package com.example.hardwaresensors

import kotlin.random.Random

class TargetUtils {

    fun generateTargets(): List<Float> {
        val firstFloat = Random.nextFloat() * 10000
        val secondFloat = Random.nextFloat() * 10000
        return arrayListOf(firstFloat, secondFloat)
    }

    fun isInTarget(firstTarget: Float, secondTarget: Float, f: Float): Boolean {
        return (firstTarget < f && f < secondTarget)
    }
}