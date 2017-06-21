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

import java.util.Random;
import net.sothatsit.blockstore.BlockStoreApi;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Daniel Saukel
 */
public class SakuraListener implements Listener {

    Sakura plugin;

    public SakuraListener(Sakura plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        Block sapling = event.getLocation().getBlock();
        if (sapling.getType() == Material.SAPLING && BlockStoreApi.containsBlockMeta(sapling, plugin, "sapling")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (BlockState block : event.getBlocks()) {
                        if (block.getType() == Material.LOG) {
                            setLogData(block.getBlock());
                        } else if (block.getType() == Material.LEAVES) {
                            setLeavesData(block.getBlock());
                        }
                    }
                }
            }.runTaskLater(plugin, 1);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getItemMeta().hasLore()) {
            if (getIdentifier(item).equals(ChatColor.GRAY + plugin.leaves)) {
                setLeavesData(event.getBlock());
            } else if (getIdentifier(item).equals(ChatColor.GRAY + plugin.log)) {
                setLeavesData(event.getBlock());
            } else if (getIdentifier(item).equals(ChatColor.GRAY + plugin.sapling)) {
                setSaplingData(event.getBlock());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        World world = event.getBlock().getWorld();
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        if (BlockStoreApi.containsBlockMeta(block, plugin, "leaves")) {
            event.setDropItems(false);
            if (tool != null && tool.getType() == Material.SHEARS) {
                world.dropItem(block.getLocation(), SakuraItem.LEAVES);
            }
            if (new Random().nextInt(100) > 50) {
                world.dropItem(block.getLocation(), SakuraItem.CHERRY);
            }
            if (new Random().nextInt(100) > 15) {
                world.dropItem(block.getLocation(), SakuraItem.SAPLING);
            }
        } else if (BlockStoreApi.containsBlockMeta(block, plugin, "log")) {
            event.setDropItems(false);
            world.dropItem(block.getLocation(), SakuraItem.LOG);
        } else if (BlockStoreApi.containsBlockMeta(block, plugin, "sapling")) {
            event.setDropItems(false);
            world.dropItem(block.getLocation(), SakuraItem.SAPLING);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        for (ItemStack item : event.getInventory().getContents()) {
            if (item.getItemMeta().hasLore() && (ChatColor.GRAY + plugin.cherry).equals(getIdentifier(item))) {
                event.setCancelled(true);
            }
        }
    }

    private String getIdentifier(ItemStack item) {
        return item.getItemMeta().getLore().get(0);
    }

    private void setLogData(Block block) {
        block.setType(Material.LOG_2);
        BlockStoreApi.setBlockMeta(block, plugin, "log", (byte) 1);
    }

    private void setLeavesData(Block block) {
        block.setTypeIdAndData(Material.WOOL.getId(), (byte) 6, false);
        BlockStoreApi.setBlockMeta(block, plugin, "leaves", (byte) 1);
    }

    private void setSaplingData(Block block) {
        BlockStoreApi.setBlockMeta(block, plugin, "sapling", (byte) 1);
    }

}
