package com.topka.enchants.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public final class TreeUtil {
    private static final int MAX_BLOCKS = 1024;
    private static final BlockFace[] FACES = {
            BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST
    };

    private TreeUtil() {}

    private static boolean isLog(Block block) {
        return block.getType().name().endsWith("_LOG");
    }

    /** Итеративный обход без рекурсии (нет StackOverflow на больших деревьях). */
    public static List<Block> getTreeBlocks(Block start) {
        List<Block> result = new ArrayList<>();
        if (!isLog(start)) {
            return result;
        }
        Set<Block> visited = new HashSet<>();
        Deque<Block> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty() && result.size() < MAX_BLOCKS) {
            Block current = queue.poll();
            result.add(current);
            for (BlockFace face : FACES) {
                Block next = current.getRelative(face);
                if (isLog(next) && visited.add(next)) {
                    queue.add(next);
                }
            }
        }
        return result;
    }
}
