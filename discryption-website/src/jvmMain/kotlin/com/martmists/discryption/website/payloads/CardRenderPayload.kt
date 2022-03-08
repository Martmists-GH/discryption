package com.martmists.discryption.website.payloads

import kotlinx.serialization.Serializable

@Serializable
data class CardRenderPayload(
    val inConduit: Boolean,
    val opponent: Boolean,
    val health: Int,
    val attack: Int,
)
