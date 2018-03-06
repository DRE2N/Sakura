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

import org.bukkit.Material;

/**
 * @author Daniel Saukel
 */
public class LegacyUtil {

    public static final Material ACACIA_LOG = get("ACACIA_LOG", "LOG_2");
    public static final Material OAK_LEAVES = get("OAK_LEAVES", "LEAVES");
    public static final Material OAK_LOG = get("OAK_LOG", "LOG");
    public static final Material OAK_SAPLING = get("OAK_SAPLING", "SAPLING");
    public static final Material PINK_TULIP = get("PINK_TULIP", "RED_ROSE");
    public static final Material PINK_WOOL = get("PINK_WOOL", "WOOL");

    private static Material get(String newName, String oldName) {
        try {
            return Material.valueOf(newName);
        } catch (IllegalArgumentException exception) {
            return Material.valueOf(oldName);
        }
    }

}
