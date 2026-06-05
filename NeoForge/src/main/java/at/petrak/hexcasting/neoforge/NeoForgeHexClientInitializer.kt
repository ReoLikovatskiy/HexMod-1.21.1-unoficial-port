package at.petrak.hexcasting.neoforge

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.client.ClientTickCounter
import at.petrak.hexcasting.client.Keybinds
import at.petrak.hexcasting.client.RegisterClientStuff
import at.petrak.hexcasting.client.ShiftScrollListener
import at.petrak.hexcasting.client.gui.PatternTooltipComponent
import at.petrak.hexcasting.client.model.HexModelLayers
import at.petrak.hexcasting.client.render.HexAdditionalRenderers
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.common.lib.HexParticles
import at.petrak.hexcasting.interop.HexInterop
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.common.NeoForge
import java.util.function.Function

@Mod.EventBusSubscriber(modid = HexAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object NeoForgeHexClientInitializer {
    fun setupClient(modEventBus: IEventBus) {
        modEventBus.addListener(::setupClientStuff)

        // Client events
        NeoForge.EVENT_BUS.register(ClientTickHandler)
    }

    private fun setupClientStuff(event: FMLClientSetupEvent) {
        Keybinds.ALL_BINDS.forEach { KeyBindingHelper.register(it) }

        RegisterClientStuff.init()
        HexModelLayers.init { loc, defn -> EntityModelLayerRegistry.registerModelLayer(loc, defn::get) }

        HexParticles.FactoryHandler.registerFactories(object : HexParticles.FactoryHandler.Consumer {
            override fun <T : ParticleOptions?> register(
                type: ParticleType<T>,
                constructor: Function<SpriteSet, ParticleProvider<T>>
            ) {
                ParticleFactoryRegistry.registerParticleFactory(type, constructor::apply)
            }
        })

        RegisterClientStuff.registerBlockEntityRenderers(object :
            RegisterClientStuff.BlockEntityRendererRegisterererer {
            override fun <T : BlockEntity> registerBlockEntityRenderer(
                type: BlockEntityType<T>,
                berp: BlockEntityRendererProvider<in T>
            ) {
                BlockEntityRenderers.register(type, berp)
            }
        })

        HexInterop.clientInit()
        RegisterClientStuff.registerColorProviders(
            { colorizer, item -> ItemBlockRenderTypes.register(item, colorizer) },
            { colorizer, block -> ItemBlockRenderTypes.register(block, colorizer) }
        )
        ModelEvent.RegisterAdditional.register(RegisterClientStuff::onModelRegister)
    }

    object ClientTickHandler {
        @SubscribeEvent
        fun clientTick(event: TickEvent.ClientTickEvent) {
            if (event.phase == TickEvent.Phase.END) {
                ClientTickCounter.clientTickEnd()
                Keybinds.clientTickEnd()
                ShiftScrollListener.clientTickEnd()
            }
        }

        @SubscribeEvent
        fun renderLevelStage(event: RenderLevelStageEvent) {
            if (event.stage == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT) {
                HexAdditionalRenderers.overlayLevel(event.poseStack, event.partialTick)
            }
        }

        @SubscribeEvent
        fun renderGuiOverlay(event: RenderGuiOverlayEvent.Post) {
            if (event.overlay == VanillaGuiOverlay.HELMET.id()) {
                HexAdditionalRenderers.overlayGui(event.guiGraphics, event.partialTick)
            }
        }
    }
}
