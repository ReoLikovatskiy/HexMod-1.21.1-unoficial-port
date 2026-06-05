package at.petrak.hexcasting.neoforge.event

import at.petrak.hexcasting.common.misc.BrainsweepingEvents
import net.minecraft.world.InteractionResult
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

/**
 * Event handlers for NeoForge specific game events.
 */
object NeoForgeEventHandler {
    fun onPlayerInteractEntity(event: PlayerInteractEvent.EntityInteract) {
        val result = BrainsweepingEvents.interactWithBrainswept(
            event.entity,
            event.level,
            event.hand,
            event.target
        )
        if (result == InteractionResult.SUCCESS) {
            event.cancellationResult = InteractionResult.SUCCESS
        }
    }

    fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
        val shouldCancel = at.petrak.hexcasting.common.items.ItemJewelerHammer.shouldFailToBreak(
            event.entity,
            event.level.getBlockState(event.pos),
            event.pos
        )
        if (shouldCancel) {
            event.cancellationResult = InteractionResult.SUCCESS
        }
    }
}
