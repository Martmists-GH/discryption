package com.martmists.discryption.renderer

data class RenderInfo(
    val name: String,
    val rare: Boolean,
    val terrain: Boolean,
    val conduit: Boolean,
    val conduitState: Int,
    val temple: String,
    val opponent: Boolean,
    val health: Int,
    val attack: Int,
    val maxHealth: Int,
    val maxAttack: Int,
    val cost: Int,
    val costType: Int,
    val sigils: List<String>,
)
