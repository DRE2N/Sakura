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

import java.util.Random;
import net.sothatsit.blockstore.BlockStoreApi;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
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
        if (sapling.getType() == LegacyUtil.OAK_SAPLING && BlockStoreApi.containsBlockMeta(sapling, plugin, "sapling")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (BlockState block : event.getBlocks()) {
                        if (block.getType() == LegacyUtil.OAK_LOG) {
                            setLogData(block.getBlock());
                        } else if (block.getType() == LegacyUtil.OAK_LEAVES) {
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
                setLogData(event.getBlock());
            } else if (getIdentifier(item).equals(ChatColor.GRAY + plugin.sapling)) {
                setSaplingData(event.getBlock());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        Block block = event.getBlock();
        World world = event.getBlock().getWorld();
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        if (BlockStoreApi.containsBlockMeta(block, plugin, "leaves")) {
            event.setDropItems(false);
            if (tool != null && tool.getType() == Material.SHEARS || tool.hasItemMeta() && tool.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                world.dropItemNaturally(block.getLocation(), SakuraItem.LEAVES);
            }
            if (new Random().nextInt(100) < plugin.dropChanceCherry) {
                world.dropItemNaturally(block.getLocation(), SakuraItem.CHERRY);
            }
            if (new Random().nextInt(100) < plugin.dropChanceSapling) {
                world.dropItemNaturally(block.getLocation(), SakuraItem.SAPLING);
            }
        } else if (BlockStoreApi.containsBlockMeta(block, plugin, "log")) {
            event.setDropItems(false);
            world.dropItemNaturally(block.getLocation(), SakuraItem.LOG);
        } else if (BlockStoreApi.containsBlockMeta(block, plugin, "sapling")) {
            event.setDropItems(false);
            world.dropItemNaturally(block.getLocation(), SakuraItem.SAPLING);
        }
    }

    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent event) {
        Boolean sakura = null;
        for (ItemStack item : event.getInventory().getContents()) {
            if (sakura == null && item.hasItemMeta() && item.getItemMeta().hasLore() && (ChatColor.GRAY + plugin.leaves).equals(getIdentifier(item))) {
                sakura = true;
            } else if (item.getType() != Material.AIR) {
                sakura = false;
            } else if (item.hasItemMeta() && item.getItemMeta().hasLore() && (ChatColor.GRAY + plugin.cherry).equals(getIdentifier(item))) {
                event.getInventory().setResult(null);
                break;
            }
        }
        if (sakura != null && sakura) {
            ItemStack result = SakuraItem.SAKURA.clone();
            result.setAmount(3);
            event.getInventory().setResult(result);
        }
    }

    // Craftig Fix
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (event.getSlotType() == SlotType.RESULT && event.getCurrentItem().isSimilar(SakuraItem.SAKURA) && event.getCurrentItem().getAmount() == 3) {
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                event.setCancelled(true);
            } else if (inv.containsAtLeast(SakuraItem.LEAVES, 2)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ItemStack remove = SakuraItem.LEAVES.clone();
                        remove.setAmount(inv.getItem(inv.first(LegacyUtil.PINK_WOOL)).getAmount() / 2);
                        inv.removeItem(remove);
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }

    private String getIdentifier(ItemStack item) {
        return item.getItemMeta().getLore().get(0);
    }

    private void setLogData(Block block) {
        block.setType(LegacyUtil.ACACIA_LOG);
        BlockStoreApi.setBlockMeta(block, plugin, "log", (byte) 1);
    }

    private void setLeavesData(Block block) {
        block.setType(LegacyUtil.PINK_WOOL);
        if (block.getType().name().equals("WOOL")) {
            BlockState state = block.getState();
            Wool wool = ((Wool) state.getData());
            wool.setColor(DyeColor.PINK);
            state.setData(wool);
            state.update(true);
        }
        BlockStoreApi.setBlockMeta(block, plugin, "leaves", (byte) 1);
    }

    private void setSaplingData(Block block) {
        BlockStoreApi.setBlockMeta(block, plugin, "sapling", (byte) 1);
    }

}
