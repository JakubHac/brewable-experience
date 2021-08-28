package com.hacah.brewable_experience.config;

import com.hacah.brewable_experience.BrewableExperience;
import com.hacah.brewable_experience.event.PlayerJoinCallback;
import com.hacah.brewable_experience.log.Logger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class ConfigSync implements ClientModInitializer, DedicatedServerModInitializer{

    private static boolean DebugClient = true;
    private static boolean InitClientOnConnected = false;

    private static final Identifier ConfigSyncResponsePacketID = new Identifier(BrewableExperience.MODID,"config_sync_response");

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncResponsePacketID, (client, handler, buf, responseSender) -> {
            String json = buf.readString();

            client.execute(() -> {
                var serverConfig = BrewableExperience.ConfigFromJson(json);
                BrewableExperience.CONFIG.CUSTOM_ITEM_RECIPES = serverConfig.CUSTOM_ITEM_RECIPES;
                Logger.DebugLog("Received Server Config");
            });
        });
    }

    @Override
    public void onInitializeServer() {
        PlayerJoinCallback.EVENT.register((player, server) -> {
            String json = BrewableExperience.ConfigToJson(BrewableExperience.CONFIG);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(json);
            ServerPlayNetworking.send(player, ConfigSyncResponsePacketID, buf);
            Logger.DebugLog("Sent Config To " + player.getEntityName());
            return ActionResult.PASS;
        });
    }

}
