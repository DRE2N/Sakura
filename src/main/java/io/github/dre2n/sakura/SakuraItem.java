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
package io.github.dre2n.sakura;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Daniel Saukel
 */
public class SakuraItem {

    public static final ItemStack CHERRY = setup(Material.BEETROOT, Sakura.getInstance().cherry);
    public static final ItemStack SAPLING = setup(Material.SAPLING, Sakura.getInstance().sapling);
    public static final ItemStack LEAVES = setup(Material.WOOL, (short) 6, Sakura.getInstance().leaves);
    public static final ItemStack LOG = setup(Material.LOG_2, Sakura.getInstance().log);
    public static final ItemStack SAKURA = setup(Material.RED_ROSE, (short) 7, Sakura.getInstance().sakura);

    public static ItemStack setup(Material type, short dv, String name) {
        ItemStack itemStack = setup(type, name);
        itemStack.setDurability(dv);
        return itemStack;
    }

    public static ItemStack setup(Material type, String name) {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        meta.setLore(Arrays.asList(ChatColor.GRAY + name));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
