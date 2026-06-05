# Hex Casting

[Curseforge](https://www.curseforge.com/minecraft/mc-mods/hexcasting) | [Modrinth](https://modrinth.com/mod/hex-casting)
| [Source](https://github.com/gamma-delta/HexMod)

A minecraft mod about casting Hexes, powerful and programmable magical effects, inspired by PSI.

## Versions

This is an **unofficial NeoForge 1.21.1 port** of Hex Casting.

**Original mod support:**

On Forge (1.20.1), this mod requires:

- PAUCAL
- Patchouli
- Kotlin for Forge
- Caelus elytra api

On Fabric (1.20.1), it requires:

- PAUCAL
- Patchouli
- Fabric Language Kotlin
- Cardinal Components
- ClothConfig and ModMenu

**On NeoForge (1.21.1), this mod requires:**

- PAUCAL (NeoForge 1.21.1)
- Patchouli (NeoForge 1.21.1)
- Kotlin for Forge (NeoForge compatible)

## Documentation

[Read the documentation online here!](https://fallingcolors.github.io/HexMod/)

[Discord link](https://discord.gg/4xxHGYteWk)

## The Branches

Original repository has branches for different Minecraft versions:

- `main` - Latest development for 1.20.1
- `1.19` - Long-term support for 1.19
- `1.18` - Long-term support for 1.18.2
- `gh-pages` - Online Hex book documentation

**This fork:**
- `neoforge-1.21.1` - NeoForge 1.21.1 port (this branch)

## For Developers

We publish artifacts on Maven at [https://maven.blamejared.com/at/petra-k/hexcasting/]. The modern coordinates are at:

> `hexcasting-[PLATFORM]-[MC VERSION]/[MOD VERSION]`

There are some other folders in the `hexcasting` folder from old CI configurations; ignore those, they're stale.

Please only use things in the `at.petrak.hexcasting.api` package. (We do try to keep the API fairly stable, but we don't
do a very good job.) If you find you need something not in there yell at me on Discord.

## Contributing

Contributions are welcome via pull requests on GitHub. Please [link your PR](https://docs.github.com/en/issues/tracking-your-work-with-issues/using-issues/linking-a-pull-request-to-an-issue) to an
issue if one exists, and provide a detailed description of the change if one doesn't.

## Building

This NeoForge 1.21.1 port uses Gradle for building:

```bash
# Build the mod
./gradlew build

# Run the dev environment
./gradlew runServer  # Server only
./gradlew runClient  # Client only

# Run all datagen
./gradlew runAllDatagen
```

Jars will be output to `NeoForge/build/libs/`

## License

This mod is licensed under the MIT License. See LICENSE.txt for details.

## Credits

Original mod created by petrak@ (aka gamma-delta)

- Falkory for textures
- Wiresegal for lots of polish
- Alwinfy for visual effects, proofreading, and code help
- Kra3tor for sound effects
- naj77 for the nice logo
- And all of the wonderful patrons and testers!

NeoForge 1.21.1 port by ReoLikovatskiy
