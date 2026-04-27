package com.lumi.reimagined.core

import net.neoforged.neoforge.common.ModConfigSpec

object Config {
    private val BUILDER = ModConfigSpec.Builder()
    val SPEC: ModConfigSpec = BUILDER.build()
}
