package com.luowei.wechat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by luo.wei on 2018/1/25.
 *
 * 凳子的下一步位置计算
 */
public class MarbleFinder {

    private static final int EXCLUDE_TARGET = 113;
    private static final int R_TARGET = 118;
    private static final int G_TARGET = 95;
    private static final int B_TARGET = 92;

    static int[] find(BufferedImage image, int[] pos) {
        if (image == null) {
            return null;
        }

        return ImageHelper.findRange(
                image,
                new Color(R_TARGET, G_TARGET, B_TARGET).getRGB(),
                pos,
                64,
                ImageHelper::fuzzyMatch);
    }

    static boolean isMarbleColor(int color) {
        // 排除 rgb=113,113,113 的颜色，有可能会碰到这种颜色的方块
        return !ImageHelper.isSameColor(color, EXCLUDE_TARGET, EXCLUDE_TARGET, EXCLUDE_TARGET) &&
                ImageHelper.fuzzyMatch(color, R_TARGET, G_TARGET, B_TARGET, 16);
    }
}