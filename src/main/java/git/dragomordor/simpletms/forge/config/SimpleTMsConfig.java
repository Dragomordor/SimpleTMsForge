package git.dragomordor.simpletms.forge.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import git.dragomordor.simpletms.forge.SimpleTMsMod;

import java.io.*;
import java.lang.reflect.Type;

public class SimpleTMsConfig {
    public boolean eggMovesLearnable;
    public boolean tutorMovesLearnable;
    public boolean anyMoveAnyPokemon;
    public boolean imprintableBlankTMs;
    public int tmCooldownTicks;
    public float trDropPercentChance;
    public float tmDropPercentChance;

    public static class Builder {
        public static SimpleTMsConfig load() {
            Gson gson = new GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create();

            SimpleTMsConfig config = new SimpleTMsConfig();
            File configFile = new File("config/" + SimpleTMsMod.MODID + "/" + SimpleTMsMod.MODID + ".json");
            configFile.getParentFile().mkdirs();

            if (configFile.exists()) {
                try {
                    FileReader fileReader = new FileReader(configFile);
                    Type listType = new TypeToken<SimpleTMsConfig>(){}.getType();
                    config = gson.fromJson(fileReader, SimpleTMsConfig.class);
                    fileReader.close();

                } catch (Exception e) {
                    System.out.println("Error reading config file");
                }
            } else {
                // If the file does not exist, create it with the default values
                try {
                    // Defaults of all tiers
                    // Set default values
                    config.eggMovesLearnable = false;
                    config.tutorMovesLearnable = false;
                    config.anyMoveAnyPokemon = false;
                    config.imprintableBlankTMs = true;
                    config.tmCooldownTicks = 100;
                    config.trDropPercentChance = 5.0f;
                    config.tmDropPercentChance = 0.01f;
                    // Write the entire config to the file
                    FileWriter fileWriter = new FileWriter(configFile);
                    gson.toJson(config, fileWriter);
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return config;
        }
    }
    public void saveConfig() {
        File configFile = new File("config/" + SimpleTMsMod.MODID + "/" + SimpleTMsMod.MODID + ".json");

        try (FileWriter writer = new FileWriter(configFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

