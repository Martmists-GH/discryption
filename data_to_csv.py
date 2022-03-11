import re

card_regex = re.compile(r"=+\n(?P<properties>(?:.+\n)+)=+", re.M)
attr_regex = re.compile(r"\s*(?P<prop>.+?) \[(?P<value>.*?)\]\s*", re.M)


def main():
    with open("card_data.txt", "r") as f:
        content = f.read()

    cards = []

    for card in card_regex.finditer(content):
        all_props = card.group("properties")
        card_props = {}
        for prop in attr_regex.finditer(all_props):
            name, value = prop.group("prop"), prop.group("value")
            if name in card_props:
                card_props[name].append(value)
            else:
                card_props[name] = [value]

        if "GBCPack" in (card_props.get("Meta category", [])):
            cards.append(card_props)

    all_sigils = list(sorted(set(x for card in cards for x in card.get("Ability", []))))

    with open("card_data.csv", "w") as f:
        for card in cards:
            name = card["Displayed Name"][0]
            internal_name = card["Name"][0]
            description = card["Description"][0]
            attack = card["Attack"][0]
            health = card["Health"][0]
            gem_cost = sum(((g == "Green") + 2*(g == "Orange") + 4*(g == "Blue")) for g in card.get("Gem", []))
            cost = max([int(card[c][0]) for c in ("Cost", "Bones Cost", "EnergyCost")] + [gem_cost])
            cost_type = 2 if (cost == gem_cost) else (0 if (cost == int(card["Bones Cost"][0])) else (3 if (cost == int(card["EnergyCost"][0])) else 1))
            sigils = [all_sigils.index(x) for x in card.get("Ability", [])]
            temple = ["Nature", "Tech", "Undead", "Wizard"].index(card["Temple"][0])
            is_terrain = ("TerrainBackground" in card.get("Appearance Behaviour", []))
            is_rare = ("Rare" in card.get("Meta category", []))
            sigil1 = sigils[0] if len(sigils) > 0 else ""
            sigil2 = sigils[1] if len(sigils) > 1 else ""

            f.write(f"{name},{internal_name.lower()},{description},{int(is_terrain)},{temple},{int(is_rare)},{attack},{health},{cost},{cost_type},{sigil1},{sigil2},0\n")


if __name__ == "__main__":
    main()
