package org.cubeville.cvportal.warps;

import java.util.HashMap;
import java.util.Map;

public class Warp
{
    private String server;
    private String world;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;
    private String command;
    
    public Warp(String server, String world, double x, double y, double z, double yaw, double pitch, String command) {
        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.command = command;
    }

    public String getServer() {
        return server;
    }

    public String getLocationString() {
        return world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
    }

    public String getCommand() {
        return command;
    }

    public String getWorld() {
        return world;
    }
    
    public Map<String, Object> getConfiguration()
    {
        Map<String, Object> config = new HashMap<>();
        config.put("server", server);
        config.put("world", world);
        config.put("x", x);
        config.put("y", y);
        config.put("z", z);
        config.put("yaw", yaw);
        config.put("pitch", pitch);
        if(command != null) config.put("command", command);
        return config;
    }
    
}
