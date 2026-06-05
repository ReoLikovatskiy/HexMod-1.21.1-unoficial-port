# NeoForge Hex Casting - Implementation Notes

## Project Structure

```
NeoForge/
├── src/main/java/at/petrak/hexcasting/neoforge/
│   ├── NeoForgeHexInitializer.kt           # Main mod entry point
│   ├── NeoForgeHexClientInitializer.kt     # Client initialization
│   ├── NeoForgeHexConfig.java              # Configuration
│   ├── NeoForgeHexClientConfig.java        # Client configuration
│   ├── network/                             # Network/packet handling
│   ├── recipe/                              # Recipe related
│   ├── event/                               # Event handlers
│   └── xplat/                               # Cross-platform implementations
└── src/main/resources/
    ├── META-INF/
    │   ├── neoforge.mods.toml              # Mod metadata
    │   └── accesswidener.txt               # Access widener
    └── *.mixins.json                       # Mixin configurations
```

## Key Files

### Initializers
- **NeoForgeHexInitializer.kt** - Main entry point for the mod, handles registration and setup
- **NeoForgeHexClientInitializer.kt** - Client-side initialization and event handling

### Configuration
- **NeoForgeHexConfig.java** - Common and Server configuration using NeoForge's ModConfigSpec
- **NeoForgeHexClientConfig.java** - Client-side configuration

### Network
- **NeoForgePacketHandler.kt** - Handles packet registration and sending

### Recipes
- **NeoForgeModConditionalIngredient.kt** - Conditional ingredients based on config

### Events
- **NeoForgeEventHandler.kt** - Game event handlers

### Cross-Platform
- **NeoForgeXplatImpl.kt** - NeoForge-specific implementations

## Building

```bash
# Build the mod
./gradlew build

# Run dev environment
./gradlew runServer
./gradlew runClient

# Run datagen
./gradlew runAllDatagen
```

## Dependencies

Main dependencies:
- NeoForge 21.1.68
- Paucal (Kotlin utilities)
- Patchouli (guidebook)
- Kotlin for Forge

## Configuration Files

### neoforge.mods.toml
Metadata and dependencies for the mod. Located in `NeoForge/src/main/resources/META-INF/`

### Mixin Configuration
- `hexplat.mixins.json` - Common mixins (shared code)
- `neoforgeasting.mixins.json` - NeoForge-specific mixins

### Access Widener
`accesswidener.txt` - Exposes private/protected methods and fields needed by the mod

## Common Issues & Solutions

### Import errors
Ensure all NeoForge imports are from `net.neoforged.*` packages, not Forge

### Mixin issues
Make sure mixin classes are in the correct package: `at.petrak.hexcasting.neoforge.mixin`

### Config loading
Configs are registered in the mod constructor via `FMLJavaModLoadingContext`
