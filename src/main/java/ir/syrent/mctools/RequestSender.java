package ir.syrent.mctools;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RequestSender {

    private boolean isConnected = true;
    private HashMap<String, String> serverList;
    private List<String> players;

    public RequestSender() {
        if (!Main.getInstance().getConfigurationFile().getConfig().getBoolean("gamemode")) {
            Main.getInstance().getLogger().info("gamemode option disabled in config.yml file. Continue register task...");
            return;
        }

        Main.getInstance().getLogger().info("Connecting to database...");

        Main.getInstance().getProxy().getScheduler().schedule(Main.getInstance(), this::sendRequest, 0, 5, TimeUnit.SECONDS);

        if (!isConnected) {
            Main.getInstance().getLogger().warning("The plugin can't connect to the database at this time!");
            return;
        }

        Main.getInstance().getLogger().info("Successfully connected to the database!");
    }

    private void sendRequest() {
        try {
            URL url = new URL(Constants.URL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setConnectTimeout(5000);

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);

            serverList = new HashMap<>();
            players = new ArrayList<>();

            Main.getInstance().serverInfoList.forEach(serverInfo -> {
                    serverInfo.getPlayers().forEach(proxiedPlayer -> players.add(proxiedPlayer.getName()));
                    serverList.put(serverInfo.getName(), players.toString());
                }
            );

            String mapToJson = new Gson().toJson(serverList);
            Main.getInstance().getLogger().warning(mapToJson);

            byte[] out = mapToJson.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            httpConnection.setFixedLengthStreamingMode(length);
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.connect();

            OutputStream os = httpConnection.getOutputStream();
            os.write(out);

            if (httpConnection.getResponseCode() != 200) {
                isConnected = false;
            }

            httpConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
