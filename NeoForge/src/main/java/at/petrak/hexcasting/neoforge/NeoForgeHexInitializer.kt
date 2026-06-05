package at.petrak.hexcasting.neoforge

import at.petrak.hexcasting.api.HexAPI.modLoc
import at.petrak.hexcasting.api.advancements.HexAdvancementTriggers
import at.petrak.hexcasting.api.mod.HexStatistics
import at.petrak.hexcasting.common.blocks.behavior.HexComposting
import at.petrak.hexcasting.common.blocks.behavior.HexStrippables
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.common.casting.actions.spells.OpFlight
import at.petrak.hexcasting.common.casting.actions.spells.great.OpAltiora
import at.petrak.hexcasting.common.command.PatternResLocArgument
import at.petrak.hexcasting.common.entities.HexEntities
import at.petrak.hexcasting.common.items.ItemJewelerHammer
import at.petrak.hexcasting.common.lib.*
import at.petrak.hexcasting.common.lib.hex.*
import at.petrak.hexcasting.common.misc.AkashicTreeGrower
import at.petrak.hexcasting.common.misc.BrainsweepingEvents
import at.petrak.hexcasting.common.misc.PlayerPositionRecorder
import at.petrak.hexcasting.common.misc.RegisterMisc
import at.petrak.hexcasting.common.recipe.HexRecipeStuffRegistry
import at.petrak.hexcasting.interop.HexInterop
import at.petrak.hexcasting.xplat.IXplatAbstractions
import io.github.tropheusj.serialization_hooks.ingredient.IngredientDeserializer
import net.minecraft.commands.synchronization.SingletonArgumentInfo
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.javafxmod.FMLJavaModLoadingContext
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.neoforged.neoforge.network.PacketDistributor
import java.util.function.BiConsumer

@Mod("hexcasting")
object NeoForgeHexInitializer {
    var patternRegistryIsProcessed: Boolean = false

    init {
        val modEventBus = FMLJavaModLoadingContext.getInstance().modEventBus
        modEventBus.addListener(::commonSetup)
        modEventBus.addListener(::attributeSetup)

        // Register config
        val container = FMLJavaModLoadingContext.getInstance().container
        container.registerConfig(ModConfig.Type.COMMON, NeoForgeHexConfig.COMMON_SPEC)
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeHexClientConfig.SPEC)

        // Server events
        NeoForge.EVENT_BUS.addListener(::serverTick)
        NeoForge.EVENT_BUS.addListener(::levelLoad)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            initRegistries()
            initListeners()
            HexInterop.init()
            RegisterMisc.register()
        }
    }

    private fun attributeSetup(event: EntityAttributeCreationEvent) {
        for ((entityType, attributeMap) in HexEntities.ATTRIBUTE_MAP) {
            event.put(entityType, attributeMap.build())
        }
    }

    private fun initListeners() {
        NeoForge.EVENT_BUS.addListener { event: net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract ->
            if (BrainsweepingEvents.interactWithBrainswept(
                    event.entity,
                    event.level,
                    event.hand,
                    event.target,
                ) == InteractionResult.SUCCESS
            ) {
                event.cancellationResult = InteractionResult.SUCCESS
            }
        }

        NeoForge.EVENT_BUS.addListener { event: net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickBlock ->
            if (ItemJewelerHammer.shouldFailToBreak(
                    event.entity,
                    event.level.getBlockState(event.pos),
                    event.pos
                )
            ) {
                event.cancellationResult = InteractionResult.SUCCESS
            }
        }
    }

    private fun initRegistries() {
        HexBlockSetTypes.registerBlocks(BlockSetType::register)
        HexCreativeTabs.registerCreativeTabs(bind(BuiltInRegistries.CREATIVE_MODE_TAB))
        HexSounds.registerSounds(bind(BuiltInRegistries.SOUND_EVENT))
        HexBlocks.registerBlocks(bind(BuiltInRegistries.BLOCK))
        HexBlocks.registerBlockItems(bind(BuiltInRegistries.ITEM))
        HexBlockEntities.registerTiles(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE))
        HexItems.registerItems(bind(BuiltInRegistries.ITEM))
        Registry.register(
            IngredientDeserializer.REGISTRY,
            modLoc("mod_conditional"),
            NeoForgeModConditionalIngredient.Deserializer.INSTANCE
        )
        HexEntities.registerEntities(bind(BuiltInRegistries.ENTITY_TYPE))
        HexAttributes.register(bind(BuiltInRegistries.ATTRIBUTE))
        HexMobEffects.register(bind(BuiltInRegistries.MOB_EFFECT))
        HexPotions.register(bind(BuiltInRegistries.POTION))
        HexRecipeStuffRegistry.registerSerializers(bind(BuiltInRegistries.RECIPE_SERIALIZER))
        HexRecipeStuffRegistry.registerTypes(bind(BuiltInRegistries.RECIPE_TYPE))
        HexParticles.registerParticles(bind(BuiltInRegistries.PARTICLE_TYPE))
        HexLootFunctions.registerSerializers(bind(BuiltInRegistries.LOOT_FUNCTION_TYPE))
        HexIotaTypes.registerTypes(bind(IXplatAbstractions.INSTANCE.iotaTypeRegistry))
        HexActions.register(bind(IXplatAbstractions.INSTANCE.actionRegistry))
        HexSpecialHandlers.register(bind(IXplatAbstractions.INSTANCE.specialHandlerRegistry))
        HexArithmetics.register(bind(IXplatAbstractions.INSTANCE.arithmeticRegistry))
        HexContinuationTypes.registerContinuations(bind(IXplatAbstractions.INSTANCE.continuationTypeRegistry))
        HexEvalSounds.register(bind(IXplatAbstractions.INSTANCE.evalSoundRegistry))

        AkashicTreeGrower.init()
        HexComposting.setup()
        HexStrippables.init()
        HexAdvancementTriggers.registerTriggers()
        HexStatistics.register()
    }

    private fun serverTick(event: ServerTickEvent.Post) {
        PlayerPositionRecorder.updateAllPlayers(event.server.overworld())
        OpFlight.tickAllPlayers(event.server.overworld())
        OpAltiora.checkAllPlayers(event.server.overworld())
    }

    private fun levelLoad(event: LevelEvent.Load) {
        if (!patternRegistryIsProcessed && event.level !is net.minecraft.client.multiplayer.ClientLevel) {
            PatternRegistryManifest.processRegistry(event.level)
            patternRegistryIsProcessed = true
        }
    }

    private fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
        BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
}
