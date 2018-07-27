/*
 * Copyright (C) 2017-2018 Daniel Saukel
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
package de.erethon.sakura;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class SakuraCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length == 1 & !(sender instanceof Player)) {
            sender.sendMessage(Sakura.getInstance().errorSyntax);
            return true;
        }
        if (!sender.hasPermission("sakura.cmd")) {
            sender.sendMessage(Sakura.getInstance().errorPermission);
            return true;
        }
        Player player = args.length < 2 ? (Player) sender : Bukkit.getPlayer(args[0]);
        String item = args.length < 2 ? args[0] : args[1];
        int arg = item.equals(args[0]) ? 1 : 2;
        int amount = args.length < arg + 1 ? 1 : Integer.parseInt(args[arg]);
        ItemStack stack = null;
        if (item.equalsIgnoreCase("sapling")) {
            stack = SakuraItem.SAPLING.clone();
        } else if (item.equalsIgnoreCase("cherry")) {
            stack = SakuraItem.CHERRY.clone();
        } else if (item.equalsIgnoreCase("log")) {
            stack = SakuraItem.LOG.clone();
        } else if (item.equalsIgnoreCase("leaves")) {
            stack = SakuraItem.LEAVES.clone();
        } else if (item.equalsIgnoreCase("flower") || item.equalsIgnoreCase("sakura")) {
            stack = SakuraItem.SAKURA.clone();
        } else {
            sender.sendMessage(Sakura.getInstance().errorSyntax);
            return true;
        }
        stack.setAmount(amount);
        player.getInventory().addItem(stack);
        sender.sendMessage(Sakura.getInstance().give.replace("%amount%", String.valueOf(amount))
                .replace("%type%", item.toLowerCase()).replace("%player%", player.getName()));
        return true;
    }

}
