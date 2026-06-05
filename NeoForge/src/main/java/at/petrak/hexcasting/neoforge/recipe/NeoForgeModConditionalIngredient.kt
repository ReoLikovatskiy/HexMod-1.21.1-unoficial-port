package at.petrak.hexcasting.neoforge.recipe

import at.petrak.hexcasting.api.mod.HexConfig
import io.github.tropheusj.serialization_hooks.ingredient.Ingredient
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.IShapedRecipe

/**
 * Conditional ingredient based on mod configuration for NeoForge.
 * This allows recipes to be disabled based on server config.
 */
class NeoForgeModConditionalIngredient(val id: ResourceLocation) : Ingredient() {
    override fun test(itemStack: ItemStack?): Boolean {
        // Check if the action/item is allowed by config
        return HexConfig.getServer()?.isActionAllowed(id) ?: true
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return Deserializer.INSTANCE
    }

    object Deserializer : RecipeSerializer<NeoForgeModConditionalIngredient> {
        val INSTANCE = this

        override fun fromJson(json: com.google.gson.JsonObject): NeoForgeModConditionalIngredient {
            val id = ResourceLocation(json.get("id").asString)
            return NeoForgeModConditionalIngredient(id)
        }

        override fun fromNetwork(buffer: FriendlyByteBuf): NeoForgeModConditionalIngredient {
            val id = buffer.readResourceLocation()
            return NeoForgeModConditionalIngredient(id)
        }

        override fun toNetwork(buffer: FriendlyByteBuf, ingredient: NeoForgeModConditionalIngredient) {
            buffer.writeResourceLocation(ingredient.id)
        }
    }
}
