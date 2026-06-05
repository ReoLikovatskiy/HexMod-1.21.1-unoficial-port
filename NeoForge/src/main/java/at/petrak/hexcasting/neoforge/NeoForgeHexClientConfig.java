package at.petrak.hexcasting.neoforge;

import at.petrak.hexcasting.api.mod.HexConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgeHexClientConfig implements HexConfig.ClientConfigAccess {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

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
        ctrlTogglesOffStrokeOrder = BUILDER.comment(
                "Whether the ctrl key will instead turn *off* the color gradient on patterns")
            .define("ctrlTogglesOffStrokeOrder", DEFAULT_CTRL_TOGGLES_OFF_STROKE_ORDER);
        disableInworldScrolling = BUILDER.comment(
                "Disable scrolling input for spellbooks and abaci in the normal world")
            .define("disableInworldScrolling", DEFAULT_DISABLE_INWORLD_SCROLLING);
        invertSpellbookScrollDirection = BUILDER.comment(
                "Whether scrolling up will increase the page index of the spellbook")
            .define("invertSpellbookScrollDirection", DEFAULT_INVERT_SPELLBOOK_SCROLL);
        invertAbacusScrollDirection = BUILDER.comment(
                "Whether scrolling up will increase the value of the abacus")
            .define("invertAbacusScrollDirection", DEFAULT_INVERT_ABACUS_SCROLL);
        gridSnapThreshold = BUILDER.comment(
                "When using a staff, the distance from one dot you have to go to snap to the next dot")
            .defineInRange("gridSnapThreshold", DEFAULT_GRID_SNAP_THRESHOLD, 0.5, 1.0);
        clickingTogglesDrawing = BUILDER.comment(
                "Whether you click to start and stop drawing instead of clicking and dragging")
            .define("clickingTogglesDrawing", DEFAULT_CLICKING_TOGGLES_DRAWING);
        alwaysShowListCommas = BUILDER.comment(
                "Whether all iota types should be comma-separated in lists")
            .define("alwaysShowListCommas", DEFAULT_ALWAYS_SHOW_LIST_COMMAS);
        advancedTooltipsShowsIotaNBT = BUILDER.comment(
                "Whether enabling advanced tooltips (F3+H) should display the full NBT of iotas")
            .define("advancedTooltipsShowsIotaNBT", DEFAULT_ADVANCED_TOOLTIPS_SHOWS_IOTA_NBT);
        staticActiveSlates = BUILDER.comment(
                "Whether patterns on active slates should be rendered without wobble")
            .define("staticActiveSlates", DEFAULT_STATIC_ACTIVE_SLATES);

        SPEC = BUILDER.build();
        HexConfig.setClient(new NeoForgeHexClientConfig());
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
