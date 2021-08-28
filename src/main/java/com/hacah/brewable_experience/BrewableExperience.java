package com.hacah.brewable_experience;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.hacah.brewable_experience.config.Config;
import com.hacah.brewable_experience.log.Logger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class BrewableExperience implements ModInitializer {

    public static final String MODID = "brewable_experience";
    private static final JsonParser parser = new JsonParser();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Config CONFIG;

    @Override
    public void onInitialize() {
        InitConfig();
    }

    private void InitConfig() {
        Path configPath = FabricLoader.getInstance().getConfigDir();
        File configFile = new File(configPath + File.separator + "brewable_experience.json");

        CONFIG = new Config();

        Logger.DebugLog("Trying to read config file...");
        try {
            if (configFile.createNewFile()) {
                Logger.WarningLog("No config file found, creating a new one...");
                String json = ConfigToJson(new Config());
                try (PrintWriter out = new PrintWriter(configFile)) {
                    out.println(json);
                }
                CONFIG = new Config();
                Logger.DebugLog("Successfully created default config file.");
            } else {
                Logger.DebugLog("A config file was found, loading it..");
                CONFIG = ConfigFromJson(new String(Files.readAllBytes(configFile.toPath())));
                if(CONFIG == null) {
                    throw new NullPointerException("The config file was empty.");
                }else{
                    Logger.DebugLog("Successfully loaded config file.");
                }
            }
        }catch (Exception exception) {
            Logger.ErrorLog("There was an error creating/loading the config file!" + exception);
            CONFIG = new Config();
            Logger.WarningLog("Defaulting to original config.");
        }
    }

    public static String ConfigToJson(Config config){
        return gson.toJson(parser.parse(gson.toJson(config)));
    }

    public static Config ConfigFromJson(String json){
        return gson.fromJson(json, Config.class);
    }
}
