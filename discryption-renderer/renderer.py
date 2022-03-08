from io import BytesIO
from typing import List

from PIL import Image, ImageFont, ImageDraw

color_black = (0,
               0,
               0,
               255)
color_blue = (0,
              int(0.75502014 * 255),
              255,
              255)
color_dark_blue = (0,
                   int(0.48435253 * 255),
                   int(0.6415094 * 255),
                   255)
color_dark_fuchsia = (int(0.3137255 * 255),
                      int(0.1254902 * 255),
                      int(0.24313726 * 255),
                      255)

root = "../assets/"
font = ImageFont.truetype(f"{root}Marksman.otf", 16)


def get_card_back(submerged: bool) -> bytes:
    image = Image.new("RGBA", (44, 58))
    backdrop_path = f"{root}pixel_card{'_submerged' if submerged else 'back'}.png"
    backdrop = Image.open(backdrop_path)
    image.paste(backdrop, (1, 1))

    b = BytesIO()
    image.save(b)
    b.seek(0)
    return b.read()


def get_card_image(card_name: str, rare: bool, terrain: bool, conduit: bool, temple: str, opponent: bool,
                   health: int, health_max: int, attack: int, attack_max: int, cost: int, cost_type: int,
                   sigils: List[str]) -> bytes:
    backdrop_path = f"{root}pixel_card_empty{'_rare' if rare else ''}{'_terrain' if terrain else ''}.png"
    portrait_path = f"{root}pixelportrait_{card_name}.png"
    rare_frame_path = f"{root}pixel_rare_frame_{temple}.png"

    image = Image.new("RGBA", (44, 58), (0, 0, 0, 0))

    backdrop = Image.open(backdrop_path).convert("RGBA")
    image.paste(backdrop, (0, 0) if rare else (1, 1))
    backdrop.close()

    portrait = Image.open(portrait_path).convert("RGBA")
    image.alpha_composite(portrait, (2, 2))
    portrait.close()

    if rare:
        rare_frame = Image.open(rare_frame_path).convert("RGBA")
        image.alpha_composite(rare_frame, (0, 0))
        rare_frame.close()

    if cost and not opponent:
        cost_icons = Image.open(f"{root}pixel_card_costs.png").convert("RGBA")
        cost_select_pos = (2 + cost_type * 27, 2 + 16 * (cost - 1))
        if cost_type == 0 and cost > 10:
            cost_select_pos = (cost_select_pos[0] + 27, cost_select_pos[1] - 48)
        cost_crop = cost_icons.crop((cost_select_pos[0], cost_select_pos[1],
                                     cost_select_pos[0] + 24, cost_select_pos[1] + 13))
        image.alpha_composite(cost_crop, (18, 2))
        cost_icons.close()

    draw = ImageDraw.Draw(image)
    health_color = color_black if health == health_max else (color_dark_fuchsia if health < health_max else color_blue)
    attack_color = color_black if attack == attack_max else color_dark_blue

    draw.text((42 - 6 * len(str(health)), 44), str(health), font=font, fill=health_color)
    draw.text((3, 44), f"{attack}", font=font, fill=attack_color)

    def draw_sigil(name: str, x: int, y: int):
        img = Image.open(f"{root}pixelability_{name}.png").convert("RGBA")
        if name.startswith("activated_"):
            x -= 3
            y += 2
            button = Image.open(f"{root}pixel_activated_ability_button.png").convert("RGBA")
            button_crop = button.crop((0, 0, 26, 17))
            image.alpha_composite(button_crop, (x-2, y-2))
        elif name == "gaingem_all":
            x -= 6
        image.alpha_composite(img, (x, y))
        img.close()

    if len(sigils) == 1:
        draw_sigil(sigils[0], 14, 32)
    elif len(sigils) == 2:
        draw_sigil(sigils[0], 5, 32)
        draw_sigil(sigils[1], 23, 32)

    if conduit:
        conduit_img = Image.open(f"{root}pixel_card_conduitborder.png")
        image.alpha_composite(conduit_img, (0, 0))
        conduit_img.close()

    image = image.resize((44*16, 58*16), Image.BOX)

    b = BytesIO()
    image.save(b, format="PNG")
    b.seek(0)
    return b.read()
