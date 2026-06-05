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
import net.neoforged.fml.javafxmod.FMLJavaModLoadingContext
import net.neoforged.neoforge.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.common.NeoForge
import java.util.function.Function

@Mod.EventBusSubscriber(modid = "hexcasting", bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object NeoForgeHexClientInitializer {
    fun setupClient(event: net.neoforged.fml.event.lifecycle.FMLClientSetupEvent) {
        Keybinds.ALL_BINDS.forEach { net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.register(it) }

        RegisterClientStuff.init()
        HexModelLayers.init { loc, defn -> net.minecraft.client.renderer.entity.EntityModelLayerRegistry.registerModelLayer(loc, defn::get) }

        HexParticles.FactoryHandler.registerFactories(object : HexParticles.FactoryHandler.Consumer {
            override fun <T : ParticleOptions?> register(
                type: ParticleType<T>,
                constructor: Function<SpriteSet, ParticleProvider<T>>
            ) {
                net.minecraft.client.particle.ParticleFactoryRegistry.getInstance().register(type, constructor::apply)
            }
        })

        RegisterClientStuff.registerBlockEntityRenderers(object :
            RegisterClientStuff.BlockEntityRendererRegisterererer {
            override fun <T : BlockEntity> registerBlockEntityRenderer(
                type: BlockEntityType<T>,
                berp: BlockEntityRendererProvider<in T>
            ) {
                net.minecraft.client.renderer.blockentity.BlockEntityRenderers.register(type, berp)
            }
        })

        HexInterop.clientInit()
        RegisterClientStuff.registerColorProviders(
            { colorizer, item -> net.minecraft.client.color.item.ItemColors.register(colorizer, item) },
            { colorizer, block -> net.minecraft.client.color.block.BlockColors.getInstance().register(colorizer, block) }
        )
    }

    object ClientTickHandler {
        @net.neoforged.neoforge.api.distmarker.OnlyIn(Dist.CLIENT)
        fun clientTick(event: net.neoforged.neoforge.client.event.TickEvent.ClientTickEvent) {
            if (event.phase == net.neoforged.neoforge.client.event.TickEvent.Phase.END) {
                ClientTickCounter.clientTickEnd()
                Keybinds.clientTickEnd()
                ShiftScrollListener.clientTickEnd()
            }
        }

        @net.neoforged.neoforge.api.distmarker.OnlyIn(Dist.CLIENT)
        fun renderLevelStage(event: RenderLevelStageEvent) {
            if (event.stage == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT) {
                HexAdditionalRenderers.overlayLevel(event.poseStack, event.partialTick)
            }
        }

        @net.neoforged.neoforge.api.distmarker.OnlyIn(Dist.CLIENT)
        fun renderGuiOverlay(event: RenderGuiOverlayEvent.Post) {
            HexAdditionalRenderers.overlayGui(event.guiGraphics, event.partialTick)
        }
    }
}
