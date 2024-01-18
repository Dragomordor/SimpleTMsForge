package git.dragomordor.simpletms.forge;

import git.dragomordor.simpletms.forge.config.SimpleTMsConfig;
import git.dragomordor.simpletms.forge.event.ModEvents;
import git.dragomordor.simpletms.forge.item.SimpleTMsItemGroups;
import git.dragomordor.simpletms.forge.item.SimpleTMsItems;
import git.dragomordor.simpletms.forge.network.ModNetwork;
import git.dragomordor.simpletms.forge.network.ModPacketHandler;
import git.dragomordor.simpletms.forge.util.TMsTRsList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SimpleTMsMod.MODID)
public class SimpleTMsMod {
    public static final Logger LOGGER = LogManager.getLogger(SimpleTMsMod.class); // create logger
    public static final String MODID = "simpletms"; // mod ID
    public static final String MOD_CHANNEL = MODID + ":network";
    public static SimpleTMsConfig config;


    public SimpleTMsMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Load config
        config = SimpleTMsConfig.Builder.load();

        // Register all items
        SimpleTMsItems.register(modEventBus);

        // Register creative tabs
        SimpleTMsItemGroups.register(modEventBus);

        // listeners
        modEventBus.addListener(this::commonSetup); //common setup event bus listener
        MinecraftForge.EVENT_BUS.register(this);

        // Register TM list
        TMsTRsList.registerTMList();

        // Load network
        ModPacketHandler.initializeConfig();
        ModNetwork.initialize();

        // Register events
        ModEvents modEvents = new ModEvents();
        modEvents.subscribeToEvents();

        // Register your mod's event handler with the Forge event bus
        MinecraftForge.EVENT_BUS.register(modEvents);

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }


    // new functions
    private void commonSetup(final FMLCommonSetupEvent event){
    }

}
