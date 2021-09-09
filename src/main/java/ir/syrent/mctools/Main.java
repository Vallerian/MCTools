package ir.syrent.mctools;

import ir.syrent.mctools.DataManager.ConfigurationFile;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends Plugin {

    public List<ServerInfo> serverInfoList;
    public ConfigurationFile configurationFile;

    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        initializeVariables();
        prepareRequest();
    }

    private void initializeVariables() {
        serverInfoList = new ArrayList<>();
        configurationFile = new ConfigurationFile();
    }

    private void prepareRequest() {
        serverInfoList.addAll(getProxy().getServers().values());
        new RequestSender();
    }

    public ConfigurationFile getConfigurationFile() {
        return configurationFile;
    }
}
