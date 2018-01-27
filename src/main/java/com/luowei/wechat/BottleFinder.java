package com.luowei.wechat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by luo.wei on 2018/1/24.
 *
 * 瓶子的下一步位置计算
 */
class BottleFinder {

    private static final int TARGET = 255;

    static int[] find(BufferedImage image, int[] pos) {
        if (image == null) {
            return null;
        }

        return ImageHelper.findRange(
                image,
                new Color(TARGET, TARGET, TARGET).getRGB(),
                pos,
                0,
                ImageHelper::fuzzyMatch);
    }

    static boolean isBottleColor(int color) {
        return ImageHelper.isSameColor(color, TARGET, TARGET, TARGET);
    }
}
