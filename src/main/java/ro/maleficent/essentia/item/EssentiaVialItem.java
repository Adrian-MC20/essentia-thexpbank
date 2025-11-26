package ro.maleficent.essentia.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemUseAnimation;
import org.jetbrains.annotations.NotNull;
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
        // Clamp XP to valid bounds
        int storedXp = Math.max(0, Math.min(CAPACITY, amount));

        // Store XP into component
        stack.set(ModDataComponents.STORED_XP, storedXp);

        // Use stored XP as model selector via item damage
        stack.setDamageValue(storedXp);
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

    // Drink animation like a potion
    @Override
    public @NotNull ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    // Duration of the drink animation (32 ticks = vanilla potion)
    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 32;
    }

    // Right-click behaviour
    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        int stored = getStoredXp(stack);

        // 1. SNEAKING = DEPOSIT (instant)
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                int remaining = getRemainingCapacity(stack);
                if (remaining > 0) {
                    int playerXp = getPlayerTotalXp(player);
                    int toDeposit = Math.min(remaining, playerXp);

                    if (toDeposit > 0) {
                        removeXpFromPlayer(player, toDeposit);
                        setStoredXp(stack, stored + toDeposit);

                        // Optional: bottle fill sound
                        level.playSound(
                                null,
                                player.getX(), player.getY(), player.getZ(),
                                SoundEvents.BOTTLE_FILL,
                                SoundSource.PLAYERS,
                                1.0f,
                                1.0f
                        );
                    }
                }
            }
            // sidedSuccess is the usual pattern for use()
            return InteractionResult.SUCCESS;
        }

        // 2. Normal right-click with stored XP = start drink animation
        if (stored > 0) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }

        // 3. Normal right-click with empty vial = nothing special
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (!level.isClientSide()) {
            return;
        }

        // Every 4 ticks, same as vanilla potion
        if (remainingUseTicks % 4 == 0) {
            entity.playSound(
                    SoundEvents.GENERIC_DRINK.value(),
                    0.5F,
                    1.0F
            );
        }
    }

    // Called when drink animation finishes
    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(!level.isClientSide() && entity instanceof  Player player) {
            int stored = getStoredXp(stack);

            if (stored > 0) {
                addXpToPlayer(player, stored);
                setStoredXp(stack, 0);

                level.playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GENERIC_DRINK,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
        }

        return stack;
    }

    @Override
    public boolean isBarVisible(ItemStack stack){
        // Hide vanilla durability bar; we use damage only for model selection
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        int stored = getStoredXp(stack);
        textConsumer.accept(Component.literal("Stored: " + stored + " / " + CAPACITY + " XP"));
    }
}
