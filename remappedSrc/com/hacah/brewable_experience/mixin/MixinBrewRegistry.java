package com.hacah.brewable_experience.mixin;

import com.hacah.brewable_experience.BrewableExperience;
import com.hacah.brewable_experience.config.Config;
import com.hacah.brewable_experience.log.Logger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class MixinBrewRegistry {

    @Inject(method = "isValidIngredient", at= @At("HEAD"), cancellable = true)
    private static void isValidCustomIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Logger.DebugLog("Checking ingredient: " + stack.getItem().getName());
        Item potion = stack.getItem();
        for (int i = 0; i<BrewableExperience.CONFIG.CUSTOM_ITEM_RECIPES.size(); i++){
            if (BrewableExperience.CONFIG.CUSTOM_ITEM_RECIPES.get(i).GetInput() == potion){
                Logger.DebugLog("Ingredient: " + stack.getItem().getName() + " is valid");
                cir.setReturnValue(true);
                return;
            }
        }
    }

    @Inject(method = "hasRecipe", at = @At("HEAD"), cancellable = true)
    private static void hasCustomRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        Logger.DebugLog("Checking recipe, input: " + input.getItem().getName() + " ingredient: " + ingredient.getItem().getName());
        Item InputItem = ingredient.getItem();
        Potion potion = PotionUtil.getPotion(input);

        Config.CustomItemRecipe tempRecipe;
        for (int i = 0; i < BrewableExperience.CONFIG.CUSTOM_ITEM_RECIPES.size(); i++){
            tempRecipe = BrewableExperience.CONFIG.CUSTOM_ITEM_RECIPES.get(i);
            if (tempRecipe.GetPotion() == potion && tempRecipe.GetInput() == InputItem){
                cir.setReturnValue(true);
                return;
            }
        }
    }

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private static void CustomCraft(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> cir) {
        if (!ingredient.isEmpty()) {
            Potion fromPotion = PotionUtil.getPotion(ingredient);
            Item inputItem = input.getItem();

            Config.CustomItemRecipe tempRecipe;
            for(int i = 0; i < BrewableExperience.CONFIG.CUSTOM_ITEM_RECIPES.size(); i++) {
                tempRecipe = BrewableExperience.CONFIG.CUSTOM_ITEM_RECIPES.get(i);
                boolean test = tempRecipe.GetPotion() == fromPotion && tempRecipe.GetInput() == inputItem;
                Logger.DebugLog("item: " + inputItem.getName() + " potion: " + fromPotion.toString() + "is craftable: " + test);
                if (test)
                {
                    cir.setReturnValue(new ItemStack((ItemConvertible)tempRecipe.GetOutput()));
                    return;
                }
            }
        }
    }
}
