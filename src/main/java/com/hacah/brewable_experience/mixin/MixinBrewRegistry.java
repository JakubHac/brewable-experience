package com.hacah.brewable_experience.mixin;

import com.google.common.collect.Lists;
import com.hacah.brewable_experience.recipes.CustomRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BrewingRecipeRegistry.class)
public class MixinBrewRegistry {

    private static final List<CustomRecipe> CUSTOM_RECIPES = Lists.newArrayList();

    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private static void afterRegistration(CallbackInfo cb) {
        CUSTOM_RECIPES.add(new CustomRecipe(Potions.MUNDANE, Items.ENDER_EYE, Items.EXPERIENCE_BOTTLE));
    }

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private static void CustomCraft(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> cir) {
        if (!ingredient.isEmpty()) {
            Potion fromPotion = PotionUtil.getPotion(ingredient);
            Item inputItem = input.getItem();

            CustomRecipe tempRecipe;
            for(int i =0; i < CUSTOM_RECIPES.size(); i++) {
                tempRecipe = CUSTOM_RECIPES.get(i);
                boolean test = tempRecipe.potion == fromPotion && tempRecipe.input == inputItem;
                System.out.println("item: " + inputItem.getName() + " potion: " + fromPotion.toString() + "is craftable: " + test);
                if (test)
                {
                    cir.setReturnValue(new ItemStack((ItemConvertible)tempRecipe.output));
                    return;
                }
            }
        }
    }

    @Inject(method = "isValidIngredient", at= @At("HEAD"), cancellable = true)
    private static void isValidCustomIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("Checking ingredient: " + stack.getItem().getName());
        Item potion = stack.getItem();
        for (int i= 0; i<CUSTOM_RECIPES.size(); i++){
            System.out.println();
            if (CUSTOM_RECIPES.get(i).input == potion){
                System.out.println("Ingredient: " + stack.getItem().getName() + " is valid");
                cir.setReturnValue(true);
                return;
            }
        }
    }

    @Inject(method = "hasRecipe", at = @At("HEAD"), cancellable = true)
    private static void hasCustomRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("Checking recipe, input: " + input.getItem().getName() + " ingredient: " + ingredient.getItem().getName());
        Item InputItem = ingredient.getItem();
        Potion potion = PotionUtil.getPotion(input);

        CustomRecipe tempRecipe;
        for (int i = 0; i < CUSTOM_RECIPES.size(); i++){
            tempRecipe = CUSTOM_RECIPES.get(i);
            if (tempRecipe.potion == potion && tempRecipe.input == InputItem){
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
