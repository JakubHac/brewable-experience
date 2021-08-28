package com.hacah.brewable_experience.log;

import com.hacah.brewable_experience.BrewableExperience;

public class Logger {

    public static void DebugLog(String message){
        if (BrewableExperience.CONFIG.ALLOWED_LOG.get(LogType.Debug)){
            System.out.println("[BrewExpDeb] " + message);
        }
    }

    public static void WarningLog(String message){
        if (BrewableExperience.CONFIG.ALLOWED_LOG.get(LogType.Warning)){
            System.out.println("[BrewExpWarn] " + message);
        }
    }

    public static void ErrorLog(String message){
        if (BrewableExperience.CONFIG.ALLOWED_LOG.get(LogType.Error)){
            System.out.println("[BrewExpErr] " + message);
        }
    }

}
