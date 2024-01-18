package git.dragomordor.simpletms.forge.item.custom;

import com.cobblemon.mod.common.api.moves.*;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import git.dragomordor.simpletms.forge.config.SimpleTMsConfig;
import git.dragomordor.simpletms.forge.network.ModNetwork;
import git.dragomordor.simpletms.forge.network.ServerCooldownTicksPacket;
import git.dragomordor.simpletms.forge.util.OverlayMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import org.jetbrains.annotations.Nullable;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveTutorItem extends PokemonUseItem {
    SimpleTMsConfig config = SimpleTMsConfig.Builder.load();
    private final String moveName;
    private final String moveType;
    private final boolean SingleUse;

    public MoveTutorItem(String moveName, String moveType, boolean singleUse) {
        super(new Properties().stacksTo(1));
        this.moveType = moveType;
        this.moveName = moveName;
        SingleUse = singleUse;
    }



    @Override
    public InteractionResult processInteraction(ItemStack itemStack, Player player, PokemonEntity target, Pokemon pokemon) {

        MoveSet currentmoves = pokemon.getMoveSet(); // moves Pokémon currently has equipped
        BenchedMoves benchedMoves = pokemon.getBenchedMoves(); // moves Pokémon currently has benched
        MoveTemplate taughtMove = Moves.INSTANCE.getByName(moveName);

        final int cooldownTicks = config.tmCooldownTicks; // Define the cooldown in ticks

        if (player.getCooldowns().isOnCooldown(this)) {
            OverlayMessage.displayOverlayMessage(player,"TM is on cooldown.");

            return InteractionResult.FAIL;
        }
        if (taughtMove == null) {
            OverlayMessage.displayOverlayMessage(player,"Invalid move!");

            return InteractionResult.FAIL;
        }
        if (currentmoves.getMoves().stream().anyMatch(move -> move.getTemplate().equals(taughtMove))
                || benchedMovesContainsMove(benchedMoves, taughtMove)) {
            OverlayMessage.displayOverlayMessage(player,pokemon.getSpecies().getName() + " already knows " + taughtMove.getDisplayName().getString() + "!");
            return InteractionResult.FAIL;
        }
        // can Pokémon learn move?
        boolean canLearnMove = canLearnMove(itemStack, player, target, pokemon, taughtMove);

        // if Pokémon can't learn move, return fail
        if (!canLearnMove) {
            OverlayMessage.displayOverlayMessage(player,pokemon.getSpecies().getName() + " cannot be taught " + taughtMove.getDisplayName().getString());
            return InteractionResult.FAIL;
        }
        // if Pokémon can learn move, teach move
        if (currentmoves.hasSpace()) {
            currentmoves.add(taughtMove.create());
        } else {
            benchedMoves.add(new BenchedMove(taughtMove, 0));
        }
        OverlayMessage.displayOverlayMessage(player,"Taught " + pokemon.getSpecies().getName() + " " + taughtMove.getDisplayName().getString() + "!");
        // removes item if it is single use
        if (SingleUse) {
            itemStack.shrink(1); // remove item after use
        }
        // Puts item on cooldown if not singleUse
        if (!SingleUse && (cooldownTicks > 0)) {
            player.getCooldowns().addCooldown(this, cooldownTicks);
            // Send the server-configured cooldown ticks to the client
            ModNetwork.CHANNEL.sendTo(new ServerCooldownTicksPacket(cooldownTicks), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);

        }
        // play level up sound if Pokémon is taught move
        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);
        return InteractionResult.SUCCESS;
    }

    private boolean benchedMovesContainsMove(BenchedMoves benchedMoves, MoveTemplate taughtMove) {
        for (BenchedMove benchedMove : benchedMoves) {
            if (benchedMove.getMoveTemplate().equals(taughtMove)) {
                return true;
            }
        }
        return false;
    }

    private boolean canLearnMove(ItemStack itemStack, Player player, PokemonEntity target, Pokemon pokemon, MoveTemplate taughtMove) {
        boolean canLearnMove = config.anyMoveAnyPokemon; // Default value for canLearnMove
        if (canLearnMove) {
            return true;
        }
        if (pokemon.getForm().getMoves().getTmMoves().contains(taughtMove)) {
            return true;
        }
        if (pokemon.getForm().getMoves().getTutorMoves().contains(taughtMove) && config.tutorMovesLearnable) {
            return true;
        }
        if (pokemon.getForm().getMoves().getEggMoves().contains(taughtMove) && config.eggMovesLearnable) {
            return true;
        }
        return false;
    }

    // Tooltip
    private static final Map<String, Integer> moveTypeColors = new HashMap<>();
    static {  // Initialize moveTypeColors map
        moveTypeColors.put("Bug", 0xB6E881);
        moveTypeColors.put("Dark", 0x68A0A9);
        moveTypeColors.put("Dragon", 0xA9B0D1);
        moveTypeColors.put("Electric", 0xF3BF00);
        moveTypeColors.put("Fairy", 0xEE9F96);
        moveTypeColors.put("Fighting", 0xD87000);
        moveTypeColors.put("Fire", 0xD72200);
        moveTypeColors.put("Flying", 0xAED8E9);
        moveTypeColors.put("Ghost", 0xAB62DC);
        moveTypeColors.put("Grass", 0x3EB556);
        moveTypeColors.put("Ground", 0xEFE843);
        moveTypeColors.put("Ice", 0x94E0F2);
        moveTypeColors.put("Normal", 0xFFFFFF);
        moveTypeColors.put("Poison", 0xC379BA);
        moveTypeColors.put("Psychic", 0xF3B8AF);
        moveTypeColors.put("Rock", 0xCDB02E);
        moveTypeColors.put("Steel", 0xD0D0D0);
        moveTypeColors.put("Water", 0x63A8EB);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);

        Player player = Minecraft.getInstance().player;
        // Check if the item has a cooldown and add the cooldown information to the tooltip
        if (player != null) {
            if (player.getCooldowns().isOnCooldown(this)) {
                // get remaining cooldown
                int maxCooldownTicks = config.tmCooldownTicks;
                int ticksLeft = Math.round(player.getCooldowns().getCooldownPercent(this, 0.0F) * maxCooldownTicks);
                //convert cooldown to seconds, minutes, hours
                int hoursLeft = ticksLeft / 72000; // 72000 ticks = 1 hour
                int minutesLeft = (ticksLeft % 72000) / 1200; // 1200 ticks = 1 minute
                int secondsLeft = (ticksLeft % 1200) / 20; // 20 ticks = 1 second
                // display cooldown
                StringBuilder cooldownText = new StringBuilder("Cooldown: ");
                boolean hasContent = false;
                if (hoursLeft > 0) {
                    cooldownText.append(hoursLeft).append(" hour").append(hoursLeft > 1 ? "s" : "");
                    hasContent = true;
                }
                if (minutesLeft > 0) {
                    if (hasContent) {
                        cooldownText.append(", ");
                    }
                    cooldownText.append(minutesLeft).append(" minute").append(minutesLeft > 1 ? "s" : "");
                    hasContent = true;
                }
                if (secondsLeft > 0 || (!hasContent && ticksLeft == 0)) {
                    if (hasContent) {
                        cooldownText.append(", ");
                    }
                    cooldownText.append(secondsLeft).append(" second").append(secondsLeft > 1 ? "s" : "");
                }

                tooltip.add(Component.literal(cooldownText.toString()).withStyle(ChatFormatting.RED));

            }
        }

        // Adds warning that item is single use on tooltip
        if (SingleUse) {
            String singleUseString = ("Consumed after use");
            tooltip.add(Component.literal(singleUseString).withStyle(ChatFormatting.DARK_RED));
        }

        // Adds move type to tooltip
        String moveType = this.moveType;
        // Check if the move type exists in the map
        if (moveTypeColors.containsKey(moveType)) {
            int color = moveTypeColors.get(moveType);

            // Create the text with the move type and apply the specified color
            tooltip.add(Component.literal(moveType).withStyle(Style.EMPTY.withColor(color)));
        }
    }


    public String getMoveType() {
        return moveType;
    }

    public String getMoveName() {
        return moveName;
    }


}



