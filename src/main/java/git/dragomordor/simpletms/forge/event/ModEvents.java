package git.dragomordor.simpletms.forge.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent;
import com.cobblemon.mod.common.api.reactive.EventObservable;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import git.dragomordor.simpletms.forge.config.SimpleTMsConfig;
import git.dragomordor.simpletms.forge.item.SimpleTMsItems;
import git.dragomordor.simpletms.forge.util.OverlayMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ModEvents {
    SimpleTMsConfig config = SimpleTMsConfig.Builder.load();

    // Create an instance of the EventObservable for the BATTLE_FAINTED event
    private final EventObservable<BattleFaintedEvent> cobblemonBattleFaintedEventObservable = CobblemonEvents.BATTLE_FAINTED;

    // Your handler method to handle the Cobblemon BattleFaintedEvent
    private void handleCobblemonBattleFaintedEvent(BattleFaintedEvent event) {
        // Get all players involved in the battle
        List<ServerPlayer> players = event.getBattle().getPlayers();
        // If only one player is involved, proceed
        if (players.size() == 1) {
            BattlePokemon killed = event.getKilled();
            Pokemon pokemon = killed.getEffectedPokemon();

            if (!pokemon.isWild()) return; // only wild pokemon drop TMs
            
            // Get the world and position where the Pokémon fainted
            Level world = pokemon.getEntity().getCommandSenderWorld();
            BlockPos pos = pokemon.getEntity().blockPosition();

            ItemStack droppedTMitem = SimpleTMsItems.getRandomTMItemStack(pokemon);
            ItemStack droppedTRitem = SimpleTMsItems.getRandomTRItemStack(pokemon);

            // Spawn the chosen TM item
            float randomTMChance = world.getRandom().nextFloat() * 100;
            float dropChanceTMPercentage = config.tmDropPercentChance;

            if (randomTMChance <= dropChanceTMPercentage && !droppedTMitem.isEmpty()) {
                spawnTMItem(world, pos, droppedTMitem, event);
            } else {
                float randomTRChance = world.getRandom().nextFloat() * 100;
                float dropChanceTRPercentage = config.trDropPercentChance;
                if (randomTRChance <= dropChanceTRPercentage && !droppedTRitem.isEmpty()) {
                    spawnTMItem(world, pos, droppedTRitem, event);
                }
            }
        }
    }

    private static void spawnTMItem(Level world, BlockPos pos, ItemStack tmItemStack, BattleFaintedEvent event) {
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tmItemStack);

        String tmName = tmItemStack.getDisplayName().getString();
        // Get all players involved in the battle
        List<ServerPlayer> players = event.getBattle().getPlayers();

        if (players.size() == 1) {
            ServerPlayer playerEntity = players.get(0);

            // Add the TM item to the player's inventory
            Inventory playerInventory = playerEntity.getInventory();
            if (!playerInventory.add(tmItemStack)) {
                // If the inventory is full, drop the item in the world
                world.addFreshEntity(itemEntity);
            }
            OverlayMessage.displayOverlayMessage(playerEntity,"Received "+tmName+" from "+event.getKilled().getEntity().getPokemon().getDisplayName().getString());
        } else {
            // If there is no player or more than one player, simply spawn the item in the world
            world.addFreshEntity(itemEntity);
        }
    }


    // Subscribe to the event on the Forge event bus
    public void subscribeToEvents() {
        cobblemonBattleFaintedEventObservable.subscribe(Priority.NORMAL, event -> {
            // Inside this block, 'event' is the instance of BattleFaintedEvent
            handleCobblemonBattleFaintedEvent(event);
            return null; // return Unit.INSTANCE in Kotlin
        });
    }
}
