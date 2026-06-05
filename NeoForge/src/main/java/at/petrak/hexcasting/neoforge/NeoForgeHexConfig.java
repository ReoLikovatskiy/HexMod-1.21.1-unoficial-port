package at.petrak.hexcasting.neoforge;

import at.petrak.hexcasting.api.mod.HexConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

import static at.petrak.hexcasting.api.mod.HexConfig.noneMatch;

public class NeoForgeHexConfig implements HexConfig.CommonConfigAccess {
    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec SERVER_SPEC;

    private static ModConfigSpec.LongValue dustMediaAmount;
    private static ModConfigSpec.LongValue shardMediaAmount;
    private static ModConfigSpec.LongValue chargedCrystalMediaAmount;
    private static ModConfigSpec.DoubleValue mediaToHealthRate;

    private static ModConfigSpec.IntValue cypherCooldown;
    private static ModConfigSpec.IntValue trinketCooldown;
    private static ModConfigSpec.IntValue artifactCooldown;

    static {
        COMMON_BUILDER.push("Media Amounts");
        dustMediaAmount = COMMON_BUILDER.comment("How much media a single Amethyst Dust item is worth")
            .defineInRange("dustMediaAmount", DEFAULT_DUST_MEDIA_AMOUNT, 0, Integer.MAX_VALUE);
        shardMediaAmount = COMMON_BUILDER.comment("How much media a single Amethyst Shard item is worth")
            .defineInRange("shardMediaAmount", DEFAULT_SHARD_MEDIA_AMOUNT, 0, Integer.MAX_VALUE);
        chargedCrystalMediaAmount = COMMON_BUILDER.comment("How much media a single Charged Amethyst Crystal item is worth")
            .defineInRange("chargedCrystalMediaAmount", DEFAULT_CHARGED_MEDIA_AMOUNT, 0, Integer.MAX_VALUE);
        mediaToHealthRate = COMMON_BUILDER.comment("How many points of media a half-heart is worth when casting from HP")
            .defineInRange("mediaToHealthRate", DEFAULT_MEDIA_TO_HEALTH_RATE, 0.0, Double.POSITIVE_INFINITY);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("Cooldowns");
        cypherCooldown = COMMON_BUILDER.comment("Cooldown in ticks of a cypher")
            .defineInRange("cypherCooldown", DEFAULT_CYPHER_COOLDOWN, 0, Integer.MAX_VALUE);
        trinketCooldown = COMMON_BUILDER.comment("Cooldown in ticks of a trinket")
            .defineInRange("trinketCooldown", DEFAULT_TRINKET_COOLDOWN, 0, Integer.MAX_VALUE);
        artifactCooldown = COMMON_BUILDER.comment("Cooldown in ticks of a artifact")
            .defineInRange("artifactCooldown", DEFAULT_ARTIFACT_COOLDOWN, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_SPEC = COMMON_BUILDER.build();
        CLIENT_SPEC = CLIENT_BUILDER.build();
        SERVER_SPEC = SERVER_BUILDER.build();

        HexConfig.setCommon(new NeoForgeHexConfig());
    }

    @Override
    public long dustMediaAmount() {
        return dustMediaAmount.get();
    }

    @Override
    public long shardMediaAmount() {
        return shardMediaAmount.get();
    }

    @Override
    public long chargedCrystalMediaAmount() {
        return chargedCrystalMediaAmount.get();
    }

    @Override
    public double mediaToHealthRate() {
        return mediaToHealthRate.get();
    }

    @Override
    public int cypherCooldown() {
        return cypherCooldown.get();
    }

    @Override
    public int trinketCooldown() {
        return trinketCooldown.get();
    }

    @Override
    public int artifactCooldown() {
        return artifactCooldown.get();
    }

    public static class Client implements HexConfig.ClientConfigAccess {
        private static ModConfigSpec.BooleanValue ctrlTogglesOffStrokeOrder;
        private static ModConfigSpec.BooleanValue disableInworldScrolling;
        private static ModConfigSpec.BooleanValue invertSpellbookScrollDirection;
        private static ModConfigSpec.BooleanValue invertAbacusScrollDirection;
        private static ModConfigSpec.DoubleValue gridSnapThreshold;
        private static ModConfigSpec.BooleanValue clickingTogglesDrawing;
        private static ModConfigSpec.BooleanValue alwaysShowListCommas;
        private static ModConfigSpec.BooleanValue advancedTooltipsShowsIotaNBT;
        private static ModConfigSpec.BooleanValue staticActiveSlates;

        static {
            ctrlTogglesOffStrokeOrder = CLIENT_BUILDER.comment(
                    "Whether the ctrl key will instead turn *off* the color gradient on patterns")
                .define("ctrlTogglesOffStrokeOrder", DEFAULT_CTRL_TOGGLES_OFF_STROKE_ORDER);
            disableInworldScrolling = CLIENT_BUILDER.comment(
                    "Disable scrolling input for spellbooks and abaci in the normal world, keeping keybinds and staff screen scrolling normal")
                .define("disableInworldScrolling", DEFAULT_DISABLE_INWORLD_SCROLLING);
            invertSpellbookScrollDirection = CLIENT_BUILDER.comment(
                    "Whether scrolling up (as opposed to down) will increase the page index of the spellbook, and vice versa")
                .define("invertSpellbookScrollDirection", DEFAULT_INVERT_SPELLBOOK_SCROLL);
            invertAbacusScrollDirection = CLIENT_BUILDER.comment(
                    "Whether scrolling up (as opposed to down) will increase the value of the abacus, and vice versa")
                .define("invertAbacusScrollDirection", DEFAULT_INVERT_ABACUS_SCROLL);
            gridSnapThreshold = CLIENT_BUILDER.comment(
                    "When using a staff, the distance from one dot you have to go to snap to the next dot, where 0.5 means 50% of the way.")
                .defineInRange("gridSnapThreshold", DEFAULT_GRID_SNAP_THRESHOLD, 0.5, 1.0);
            clickingTogglesDrawing = CLIENT_BUILDER.comment(
                    "Whether you click to start and stop drawing instead of clicking and dragging")
                .define("clickingTogglesDrawing", DEFAULT_CLICKING_TOGGLES_DRAWING);
            alwaysShowListCommas = CLIENT_BUILDER.comment(
                    "Whether all iota types should be comma-separated in lists")
                .define("alwaysShowListCommas", DEFAULT_ALWAYS_SHOW_LIST_COMMAS);
            advancedTooltipsShowsIotaNBT = CLIENT_BUILDER.comment(
                    "Whether enabling advanced tooltips (F3+H) should display the full NBT of iotas stored in items")
                .define("advancedTooltipsShowsIotaNBT", DEFAULT_ADVANCED_TOOLTIPS_SHOWS_IOTA_NBT);
            staticActiveSlates = CLIENT_BUILDER.comment(
                    "Whether patterns on active slates should be rendered without wobble")
                .define("staticActiveSlates", DEFAULT_STATIC_ACTIVE_SLATES);
        }

        @Override
        public boolean ctrlTogglesOffStrokeOrder() {
            return ctrlTogglesOffStrokeOrder.get();
        }

        @Override
        public boolean disableInworldScrolling() {
            return disableInworldScrolling.get();
        }

        @Override
        public boolean invertSpellbookScrollDirection() {
            return invertSpellbookScrollDirection.get();
        }

        @Override
        public boolean invertAbacusScrollDirection() {
            return invertAbacusScrollDirection.get();
        }

        @Override
        public double gridSnapThreshold() {
            return gridSnapThreshold.get();
        }

        @Override
        public boolean clickingTogglesDrawing() {
            return clickingTogglesDrawing.get();
        }

        @Override
        public boolean alwaysShowListCommas() {
            return alwaysShowListCommas.get();
        }

        @Override
        public boolean advancedTooltipsShowsIotaNBT() {
            return advancedTooltipsShowsIotaNBT.get();
        }

        @Override
        public boolean staticActiveSlates() {
            return staticActiveSlates.get();
        }
    }

    public static class Server implements HexConfig.ServerConfigAccess {
        private static ModConfigSpec.IntValue opBreakHarvestLevel;
        private static ModConfigSpec.IntValue maxOpCount;
        private static ModConfigSpec.IntValue maxSpellCircleLength;
        private static ModConfigSpec.ConfigValue<List<? extends String>> actionDenyList;
        private static ModConfigSpec.ConfigValue<List<? extends String>> circleActionDenyList;
        private static ModConfigSpec.BooleanValue greaterTeleportSplatsItems;
        private static ModConfigSpec.BooleanValue villagersOffendedByMindMurder;
        private static ModConfigSpec.ConfigValue<List<? extends String>> tpDimDenyList;
        private static ModConfigSpec.BooleanValue doesTrueNameHaveAmbit;
        private static ModConfigSpec.DoubleValue traderScrollChance;

        static {
            SERVER_BUILDER.push("Spells");
            maxOpCount = SERVER_BUILDER.comment("The maximum number of actions that can be executed in one tick")
                .defineInRange("maxOpCount", DEFAULT_MAX_OP_COUNT, 0, Integer.MAX_VALUE);
            opBreakHarvestLevel = SERVER_BUILDER.comment(
                "The harvest level of the Break Block spell.",
                "0 = wood, 1 = stone, 2 = iron, 3 = diamond, 4 = netherite.")
                .defineInRange("opBreakHarvestLevel", DEFAULT_OP_BREAK_HARVEST_LEVEL, 0, 4);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.push("Spell Circles");
            maxSpellCircleLength = SERVER_BUILDER.comment("The maximum number of slates in a spell circle")
                .defineInRange("maxSpellCircleLength", DEFAULT_MAX_SPELL_CIRCLE_LENGTH, 4, Integer.MAX_VALUE);
            circleActionDenyList = SERVER_BUILDER.comment("Resource locations of disallowed actions within circles")
                .defineList("circleActionDenyList", List.of(), Server::isValidReslocArg);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.push("Loot");
            traderScrollChance = SERVER_BUILDER.comment("The chance for wandering traders to sell an Ancient Scroll")
                .defineInRange("traderScrollChance", DEFAULT_TRADER_SCROLL_CHANCE, 0.0, 1.0);
            SERVER_BUILDER.pop();

            actionDenyList = SERVER_BUILDER.comment("Resource locations of disallowed actions")
                .defineList("actionDenyList", List.of(), Server::isValidReslocArg);
            greaterTeleportSplatsItems = SERVER_BUILDER.comment(
                "Should items fly out of the player's inventory when using Greater Teleport?")
                .define("greaterTeleportSplatsItems", DEFAULT_GREATER_TELEPORT_SPLATS_ITEMS);
            villagersOffendedByMindMurder = SERVER_BUILDER.comment(
                "Should villagers take offense when you flay the mind of their fellow villagers?")
                .define("villagersOffendedByMindMurder", DEFAULT_VILLAGERS_DISLIKE_MIND_MURDER);
            tpDimDenyList = SERVER_BUILDER.comment("Resource locations of dimensions you can't Blink or Greater Teleport in")
                .defineList("tpDimDenyList", DEFAULT_DIM_TP_DENYLIST, Server::isValidReslocArg);
            doesTrueNameHaveAmbit = SERVER_BUILDER.comment(
                "When false, makes player reference iotas behave as normal entity reference iotas")
                .define("doesTrueNameHaveAmbit", DEFAULT_TRUE_NAME_HAS_AMBIT);
        }

        @Override
        public int opBreakHarvestLevelBecauseForgeThoughtItWasAGoodIdeaToImplementHarvestTiersUsingAnHonestToGodTopoSort() {
            return opBreakHarvestLevel.get();
        }

        @Override
        public int maxOpCount() {
            return maxOpCount.get();
        }

        @Override
        public int maxSpellCircleLength() {
            return maxSpellCircleLength.get();
        }

        @Override
        public boolean isActionAllowed(ResourceLocation actionID) {
            return noneMatch(actionDenyList.get(), actionID);
        }

        @Override
        public boolean isActionAllowedInCircles(ResourceLocation actionID) {
            return noneMatch(circleActionDenyList.get(), actionID);
        }

        @Override
        public boolean doesGreaterTeleportSplatItems() {
            return greaterTeleportSplatsItems.get();
        }

        @Override
        public boolean doVillagersTakeOffenseAtMindMurder() {
            return villagersOffendedByMindMurder.get();
        }

        @Override
        public boolean canTeleportInThisDimension(ResourceKey<Level> dimension) {
            return noneMatch(tpDimDenyList.get(), dimension.location());
        }

        @Override
        public boolean trueNameHasAmbit() {
            return doesTrueNameHaveAmbit.get();
        }

        @Override
        public double traderScrollChance() {
            return traderScrollChance.get();
        }

        private static boolean isValidReslocArg(Object o) {
            return o instanceof String s && ResourceLocation.isValidResourceLocation(s);
        }
    }
}
