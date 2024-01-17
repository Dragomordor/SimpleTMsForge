package git.dragomordor.simpletms.forge.config;


import git.dragomordor.simpletms.forge.SimpleTMsMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

public class SimpleTMsCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> EGG_MOVES_LEARABLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TUTOR_MOVES_LEARABLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALL_MOVES_LEARABLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IMPRINTABLE_BLANK_TMS;
    public static final ForgeConfigSpec.ConfigValue<Integer> TM_COOLDOWN_TICKS;

    static {
        BUILDER.push("Configs for Simple TMs");
        // Define Configs

        EGG_MOVES_LEARABLE = BUILDER.comment("If moves in a Pokémon's egg moves list can be taught via TM or TR.")
                        .define("EggMovesLearnable",false);
        TUTOR_MOVES_LEARABLE = BUILDER.comment("If moves in a Pokémon's tutor moves list can be taught via TM or TR.")
                                .define("TutorMovesLearnable",false);
        ALL_MOVES_LEARABLE = BUILDER.comment("If any move can be taught to any Pokémon by a TM or TR.")
                                .define("AnyMoveAnyPokemon",false);
        IMPRINTABLE_BLANK_TMS = BUILDER.comment("If Blank TMs and TRs can be used on a Pokémon, and their first equipped move imprinted onto it")
                                .define("ImprintableBlankTMs",true);
        TM_COOLDOWN_TICKS = BUILDER.comment("Cooldown duration (in ticks) for TMs after use")
                                .define("TMCooldownTicks",100);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
