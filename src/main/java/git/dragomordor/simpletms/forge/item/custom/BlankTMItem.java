package git.dragomordor.simpletms.forge.item.custom;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import git.dragomordor.simpletms.forge.config.SimpleTMsConfig;
import git.dragomordor.simpletms.forge.item.SimpleTMsItems;
import git.dragomordor.simpletms.forge.util.OverlayMessage;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class BlankTMItem extends PokemonUseItem {
    SimpleTMsConfig config = SimpleTMsConfig.Builder.load();


    private final boolean IsTM;
    String prefix;
    String blankTitle;


    public BlankTMItem(boolean isTM) {
        super(new Properties());
        this.IsTM = isTM;
    }

    @Override
    public InteractionResult processInteraction(ItemStack itemStack, Player player, PokemonEntity target, Pokemon pokemon) {

        if (!config.imprintableBlankTMs) {
            OverlayMessage.displayOverlayMessage(player,"Blank TM and TR imprinting disabled!");
            return InteractionResult.FAIL;
        }

        Move FirstMoveInMovesetMove = pokemon.getMoveSet().get(0);
        String FirstMoveInMoveset = Objects.requireNonNull(FirstMoveInMovesetMove).getName();
        // add custom mod item with name "FirstMoveInMoveset"
        if (IsTM) {
            prefix = "tm_";
            blankTitle = "TM";
        } else {
            prefix = "tr_";
            blankTitle = "TR";
        }

        ItemStack newTMItem = SimpleTMsItems.getItemStackByName(prefix+FirstMoveInMoveset);
        Inventory inventory = player.getInventory(); // get player inventory
        // get current item inventory slot

        // TODO add item to main hand if possible?
        if (!inventory.add(newTMItem)) { // add item to inventory
            player.drop(newTMItem,false); // drop item on ground if inventory full
        }
        itemStack.shrink(1); // delete blank tm

        OverlayMessage.displayOverlayMessage(player,"Imprinted "+FirstMoveInMovesetMove.getDisplayName().getString()+blankTitle+"!");

        player.level().playSound(null,player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,1.0f,2.0f);
        return InteractionResult.SUCCESS;
    }
}

