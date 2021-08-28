package com.hacah.brewable_experience.config;

import com.google.common.collect.Lists;
import com.hacah.brewable_experience.log.LogType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {

    public List<CustomItemRecipe> CUSTOM_ITEM_RECIPES = new ArrayList<>();

    public HashMap<LogType, Boolean> ALLOWED_LOG = new HashMap<LogType, Boolean>();

    public Config() {
        ALLOWED_LOG = new HashMap<LogType, Boolean>();
        ALLOWED_LOG.put(LogType.Debug, false);
        ALLOWED_LOG.put(LogType.Error, true);
        ALLOWED_LOG.put(LogType.Warning,true);

        this.CUSTOM_ITEM_RECIPES = Lists.newArrayList();
        CUSTOM_ITEM_RECIPES.add(new CustomItemRecipe(Potions.MUNDANE, Items.ENDER_EYE, Items.EXPERIENCE_BOTTLE));
    }

    public static class CustomItemRecipe {

        private String PotionIdentifierString;
        private String InputIdentifierString;
        private String OutputIdentifierString;

        public Potion GetPotion(){
            return Registry.POTION.get(new Identifier(PotionIdentifierString));
        };
        public Item GetInput(){
            return Registry.ITEM.get(new Identifier(InputIdentifierString));
        };
        public Item GetOutput(){
            return Registry.ITEM.get(new Identifier(OutputIdentifierString));
        };

        public CustomItemRecipe(String From, String Input, String Output) {
            PotionIdentifierString = From;
            InputIdentifierString = Input;
            OutputIdentifierString = Output;
        }

        public CustomItemRecipe(Potion From, Item Input, Item Output) {
            PotionIdentifierString = Registry.POTION.getId(From).toString();
            InputIdentifierString = Registry.ITEM.getId(Input).toString();
            OutputIdentifierString = Registry.ITEM.getId(Output).toString();
        }
    }
}
