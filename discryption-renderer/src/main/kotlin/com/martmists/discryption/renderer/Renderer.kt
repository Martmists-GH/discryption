package com.martmists.discryption.renderer

import org.jetbrains.skija.*

object Renderer {
    private val black = Paint().setColor(0xFF000000.toInt())
    private val blue = Paint().setColor(0xFF00C0FF.toInt())
    private val dark_blue = Paint().setColor(0xFF007BA3.toInt())
    private val dark_fuchsia = Paint().setColor(0xFF50203E.toInt())

    private val typeface = Typeface.makeFromData(Data.makeFromBytes(this::class.java.classLoader.getResource("assets/Marksman.otf")!!.readBytes()))
    private val font = Font(typeface, 16f)
    private val renderScale = 16

    private fun getImage(path: String): Image {
        return Image.makeFromEncoded(
            this::class.java.classLoader.getResource("assets/$path")!!.readBytes()
        )
    }

    fun render(info: RenderInfo) : ByteArray {
        val surface = Surface.makeRasterN32Premul(44 * renderScale, 58 * renderScale)
        var canvas = surface.canvas.scale(renderScale.toFloat(), renderScale.toFloat())

        val borderOffset = if (info.rare) 0f else 1f
        canvas.drawImage(getImage("pixel_card_empty${if (info.rare) "_rare" else ""}${if (info.terrain) "_terrain" else ""}.png"),  borderOffset, borderOffset)
        canvas.drawImage(getImage("pixelportrait_${info.name}.png"), 2f, 2f)

        if (info.rare) {
            canvas.drawImage(getImage("pixel_rare_frame_${info.temple}.png"), 0f, 0f)
        }

        if (info.cost > 0 && !info.opponent){
            var sx = 2f + info.costType * 27
            var sy = 2f + (info.cost - 1) * 16
            if (info.costType == 0 && info.cost > 10) {
                sx += 27
                sy -= 48
            }
            canvas.drawImageRect(getImage("pixel_card_costs.png"), Rect(sx, sy, sx+24, sy+13), Rect(18f, 2f, 42f, 15f))
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
            canvas.drawImage(getImage("pixel_card_conduitborder.png"), 0f, 0f)
        }

        canvas.scale(1 / renderScale.toFloat(), 1 / renderScale.toFloat())
        val image = surface.makeImageSnapshot()
        val pngData = image.encodeToData(EncodedImageFormat.PNG)!!
        return pngData.bytes
    }
}
