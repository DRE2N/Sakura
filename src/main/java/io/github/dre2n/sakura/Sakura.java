/*
 * Copyright (C) 2017 Daniel Saukel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.dre2n.sakura;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel Saukel
 */
public class Sakura extends JavaPlugin {

    private static Sakura instance;

    public String error = ChatColor.RED + "[Sakura] Error: Command usage is /sakura ([player]) [sapling|cherry]";
    public String give = ChatColor.GRAY + "[Sakura] Giving %amount% of %type% to %player%.";
    public String cherry = "Cherry";
    public String leaves = "Cherry Blossoms";
    public String log = "Cherry Wood";
    public String sapling = "Cherry Sapling";

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new SakuraListener(this), this);
        getCommand("sakura").setExecutor(new SakuraCommand());
        cherry = getConfig().getString("cherry");
        leaves = getConfig().getString("leaves");
        log = getConfig().getString("log");
        sapling = getConfig().getString("sapling");
    }

    public static Sakura getInstance() {
        return instance;
    }

}
