package ro.maleficent.essentia.item;

import ro.maleficent.essentia.registry.ModDataComponents;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class EssentiaVialItem extends Item {

    public static final int CAPACITY = 350;

    public EssentiaVialItem(Properties settings){
        super(settings);
    }

    // Helper: get stored XP from the stack
    public static int getStoredXp(ItemStack stack){
        Integer value = stack.getOrDefault(ModDataComponents.STORED_XP, 0);
        return Math.max(0, Math.min(CAPACITY, value));
    }

    // Helper: set stored XP and update visual damage
    public static void setStoredXp(ItemStack stack, int amount){
        int clamped = Math.max(0, Math.min(CAPACITY, amount));
        stack.set(ModDataComponents.STORED_XP, clamped);

        // Damage grows with fill level
        int damage = clamped;
        stack.setDamageValue(damage);
    }

    // Helper: compute how much space is left
    public static int getRemainingCapacity(ItemStack stack){
        return CAPACITY - getStoredXp(stack);
    }

    // Helper: rough total XP of player (simplified)
    public static int getPlayerTotalXp(Player player){
        return player.totalExperience;
    }

    private static void addXpToPlayer(Player player, int amount){
        if (amount <= 0) return;
        player.giveExperiencePoints(amount);
    }

    public static void removeXpFromPlayer(Player player, int amount){
        if (amount <= 0) return;
        player.giveExperiencePoints(-amount);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);

        // Don't do logic twice on the client
        if (world.isClientSide()){
            return InteractionResult.SUCCESS;
        }

        int stored = getStoredXp(stack);

        if (player.isShiftKeyDown()){
            // Deposit: player -> vial
            int remaining = getRemainingCapacity(stack);
            if (remaining > 0){
                int playerXp = getPlayerTotalXp(player);
                int toDeposit = Math.min(remaining, playerXp);

                if (toDeposit > 0) {
                    removeXpFromPlayer(player, toDeposit);
                    setStoredXp(stack, stored + toDeposit);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            // Withdraw: vial -> player
            if (stored > 0){
                addXpToPlayer(player, stored);
                setStoredXp(stack, 0);
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack){
        // Hide vanilla durability bar; we use damage only for model selection
        return false;
    }

    @SuppressWarnings("deprecated")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        int stored = getStoredXp(stack);
        textConsumer.accept(Component.literal("Stored: " + stored + " / " + CAPACITY + " XP"));
    }
}
