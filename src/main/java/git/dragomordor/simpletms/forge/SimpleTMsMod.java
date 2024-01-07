package git.dragomordor.simpletms.forge;

import git.dragomordor.simpletms.forge.config.SimpleTMsClientConfigs;
import git.dragomordor.simpletms.forge.config.SimpleTMsCommonConfig;
import git.dragomordor.simpletms.forge.item.SimpleTMsItemGroups;
import git.dragomordor.simpletms.forge.item.SimpleTMsItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SimpleTMsMod.MODID)
public class SimpleTMsMod {
    public static final Logger LOGGER = LogManager.getLogger(SimpleTMsMod.class); // create logger
    public static final String MODID = "simpletms"; // mod ID

    public SimpleTMsMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register all items
        SimpleTMsItems.register(modEventBus);
        // Register creative tabs
        SimpleTMsItemGroups.register(modEventBus);

        // listeners
        modEventBus.addListener(this::commonSetup); //common setup event bus listener
        MinecraftForge.EVENT_BUS.register(this);

        // Load config file
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SimpleTMsClientConfigs.SPEC, "simpletms-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SimpleTMsCommonConfig.SPEC, "simpletms-common.toml");

    }

    // new functions
    private void commonSetup(final FMLCommonSetupEvent event){
    }

}
