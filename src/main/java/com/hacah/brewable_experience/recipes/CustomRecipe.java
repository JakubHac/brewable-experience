package com.hacah.brewable_experience.recipes;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;

public class CustomRecipe<T> {

        public final Potion potion;
        public final Item input;
        public final T output;

        public CustomRecipe(Potion From, Item Input, T Output) {
            this.potion = From;
            this.input = Input;
            this.output = Output;
        }

}
