package ir.syrent.mctools.DataManager;

import ir.syrent.mctools.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigurationFile {

    private final ConfigurationProvider configurationProvider;
    private Configuration config;

    public ConfigurationFile() {
        configurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);

        createDirectory();
        loadConfigFile();
        saveConfigFile();
    }

    private void loadConfigFile() {
        try {
            config = configurationProvider.load(new File(Main.getInstance().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfigFile() {
        try {
            configurationProvider.save(config, new File(Main.getInstance().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirectory() {
        if (!Main.getInstance().getDataFolder().exists())
            Main.getInstance().getDataFolder().mkdir();

        File file = new File(Main.getInstance().getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = Main.getInstance().getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration getConfig() {
        return config;
    }
}
