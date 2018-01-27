package com.luowei.wechat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by luo.wei on 2018/1/25.
 *
 * 凳子的下一步位置计算
 */
public class DiscFinder {

    private static final int R_TARGET = 167;
    private static final int G_TARGET = 161;
    private static final int B_TARGET = 153;

    static int[] find(BufferedImage image, int[] pos) {
        if (image == null) {
            return null;
        }

        int[] topPos = ImageHelper.findRange(
                image,
                new Color(R_TARGET, G_TARGET, B_TARGET).getRGB(),
                pos,
                16,
                ImageHelper::fuzzyMatch);
        int[] leftPos = ImageHelper.findRange(
                image,
                new Color(R_TARGET, G_TARGET, B_TARGET).getRGB(),
                new int[] {pos[0] - 150, pos[1] + 100},
                16,
                ImageHelper::fuzzyMatch);
        int[] rightPos = ImageHelper.findRange(
                image,
                new Color(R_TARGET, G_TARGET, B_TARGET).getRGB(),
                new int[] {pos[0] + 150, pos[1] + 100},
                16,
                ImageHelper::fuzzyMatch);
        // 上，左，右，-30 抬高至唱片透明面
        return new int[] {topPos[0], topPos[1], leftPos[2], leftPos[3] - 30, rightPos[4], rightPos[5] - 30};
    }

    static boolean isDiscColor(int color) {
        return ImageHelper.fuzzyMatch(color, R_TARGET, G_TARGET, B_TARGET, 16);
    }
}