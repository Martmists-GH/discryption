package com.martmists.discryption.renderer

import org.jetbrains.skija.*

object Renderer {
    // Upscale factor
    private const val renderScale = 16

    // Colors
    private val black = Paint().setColor(0xFF000000.toInt())
    private val blue = Paint().setColor(0xFF00C0FF.toInt())
    private val dark_blue = Paint().setColor(0xFF007BA3.toInt())
    private val dark_fuchsia = Paint().setColor(0xFF50203E.toInt())

    // Font
    private val typeface = Typeface.makeFromData(Data.makeFromBytes(this::class.java.classLoader.getResource("assets/Marksman.otf")!!.readBytes()))
    private val font = Font(typeface, 16f)

    // Images
    private val temples = listOf(
        "nature",
        "tech",
        "undead",
        "wizard",
    ).associateWith {
        getImage("pixel_rare_frame_$it.png")
    }
    private val empty = mapOf(
        true to mapOf(
            true to getImage("pixel_card_empty_rare_terrain.png"),
            false to getImage("pixel_card_empty_rare.png")
        ),
        false to mapOf(
            true to getImage("pixel_card_empty_terrain.png"),
            false to getImage("pixel_card_empty.png")
        )
    )
    private val costs = getImage("pixel_card_costs.png")
    private val conduit = getImage("pixel_card_conduitborder.png")

    // Rendering
    private val surface = Surface.makeRasterN32Premul(44 * renderScale, 58 * renderScale)
    private val canvas = surface.canvas.scale(renderScale.toFloat(), renderScale.toFloat())

    private fun getImage(path: String): Image {
        return Image.makeFromEncoded(
            this::class.java.classLoader.getResource("assets/$path")!!.readBytes()
        )
    }

    fun render(info: RenderInfo) : ByteArray {
        val borderOffset = if (info.rare) 0f else 1f
        canvas.drawImage(empty[info.rare]!![info.terrain]!!,  borderOffset, borderOffset)
        canvas.drawImage(getImage("pixelportrait_${info.name}.png"), 2f, 2f)

        if (info.rare) {
            canvas.drawImage(temples[info.temple]!!, 0f, 0f)
        }

        if (info.cost > 0 && !info.opponent){
            var sx = 2f + info.costType * 27
            var sy = 2f + (info.cost - 1) * 16
            if (info.costType == 0 && info.cost > 10) {
                sx += 27
                sy -= 48
            }
            canvas.drawImageRect(costs, Rect(sx, sy, sx+24, sy+13), Rect(18f, 2f, 42f, 15f))
        }

        val attackPaint = when {
            info.attack == info.maxAttack -> black
            else -> dark_blue
        }
        val healthPaint = when {
            info.health == info.maxHealth -> black
            info.health > info.maxHealth -> blue
            else -> dark_fuchsia
        }

        canvas.drawString(info.attack.toString(), 3f, 55f, font, attackPaint)
        val health = info.health.toString()
        canvas.drawString(health, 42f - 6 * health.length, 55f, font, healthPaint)

        fun drawSigil(name: String, x: Float, y: Float) {
            var sx = x
            var sy = y
            if (name.startsWith("activated_")) {
                sx -= 3
                sy += 2
            } else if (name == "gaingem_all") {
                sx -= 6
            }
            canvas.drawImage(getImage("pixelability_$name.png"), sx, sy)
        }

        when (info.sigils.size) {
            1 -> drawSigil(info.sigils[0], 14f, 32f)
            2 -> {
                drawSigil(info.sigils[0], 5f, 32f)
                drawSigil(info.sigils[1], 23f, 32f)
            }
        }

        if (info.conduit) {
            canvas.drawImage(conduit, 0f, 0f)
        }

        val image = surface.makeImageSnapshot()
        val pngData = image.encodeToData(EncodedImageFormat.PNG)!!
        canvas.clear(0)

        return pngData.bytes
    }
}
