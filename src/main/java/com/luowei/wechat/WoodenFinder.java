package com.luowei.wechat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by luo.wei on 2018/1/25.
 *
 * 凳子的下一步位置计算
 */
class WoodenFinder {

    private static final int R_TARGET = 222;
    private static final int G_TARGET = 188;
    private static final int B_TARGET = 153;

    static int[] find(BufferedImage image, int[] pos) {
        if (image == null) {
            return null;
        }

        return ImageHelper.findRange(
                image,
                new Color(R_TARGET, G_TARGET, B_TARGET).getRGB(),
                pos,
                32,
                ImageHelper::fuzzyMatch);
    }

    static boolean isWoodenColor(int color) {
        // 排除小超市顶点色
        return !ImageHelper.isMarketTopVertex(color) &&
                ImageHelper.fuzzyMatch(color, R_TARGET, G_TARGET, B_TARGET, 32);
    }
}