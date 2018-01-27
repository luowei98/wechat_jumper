package com.luowei.wechat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by luo.wei on 2018/1/24.
 *
 * 瓶子的下一步位置计算
 */
class CupFinder {

    private static final int TARGET = 72;

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

    static boolean isCupColor(int color) {
        return ImageHelper.isSameColor(color, TARGET, TARGET, TARGET);
    }

}
