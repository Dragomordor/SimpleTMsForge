package git.dragomordor.simpletms.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SimpleTMsClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Configs for Simple TMs");

        // Define Configs

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
