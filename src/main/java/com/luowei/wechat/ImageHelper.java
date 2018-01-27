package com.luowei.wechat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by luo.wei on 2018/1/22.
 *
 * 图片工具类
 */
class ImageHelper {
    // 小超市顶点颜色
    private static final int R_MARKET_TARGET = 250;
    private static final int G_MARKET_TARGET = 199;
    private static final int B_MARKET_TARGET = 152;
    // 设计书顶点颜色
    private static final int DESIGN_TARGET = 223;

    static BufferedImage load(String path) throws IOException {
        BufferedImage image;
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(path));
            image = ImageIO.read(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return image;
    }

    // 颜色模糊匹配
    static boolean fuzzyMatch(int r, int g, int b, int rt, int gt, int bt, int t) {
        return r >= rt - t &&
                r <= rt + t &&
                g >= gt - t &&
                g <= gt + t &&
                b >= bt - t &&
                b <= bt + t;
    }

    static boolean fuzzyMatch(int rgb, int rt, int gt, int bt, int t) {
        int[] oldRGB = getRGB(rgb);
        return fuzzyMatch(oldRGB[0], oldRGB[1], oldRGB[2], rt, gt, bt, t);
    }

    static boolean fuzzyMatch(int rgb1, int rgb2, int t) {
        int[] oldRGB = getRGB(rgb1);
        int[] newRGB = getRGB(rgb2);
        return fuzzyMatch(oldRGB[0], oldRGB[1], oldRGB[2], newRGB[0], newRGB[1], newRGB[2], t);
    }

    static boolean isPiece(int rgb) {
        int[] oldRGB = getRGB(rgb);
        return (50 < oldRGB[0]) && (oldRGB[0] < 60) &&
                (53 < oldRGB[1]) && (oldRGB[1] < 63) &&
                (95 < oldRGB[2]) && (oldRGB[2] < 110);
    }

    static int[] getRGB(int rgb) {
        int r = (rgb & 0xff0000) >> 16;
        int g = (rgb & 0xff00) >> 8;
        int b = (rgb & 0xff);
        return new int[] {r, g, b};
    }

    static boolean isSameColor(int rgb, int r, int g, int b) {
        int[] oldRGB = getRGB(rgb);
        return fuzzyMatch(oldRGB[0], oldRGB[1], oldRGB[2], r, g, b, 0);
    }

    static int[] findRange(BufferedImage image, int color, int[] topVertex, int difference, Comparator comparator) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] pos = new int[] {topVertex[0], topVertex[1], 0, 0, 0, 0, 0, 0};
        // 左，右
        pos[2] = Integer.MAX_VALUE;
        pos[3] = Integer.MAX_VALUE;
        pos[4] = Integer.MIN_VALUE;
        pos[5] = Integer.MAX_VALUE;
        pos[6] = Integer.MAX_VALUE;
        pos[7] = Integer.MIN_VALUE;

        // 扫描左右顶点
        boolean[][] vMap = new boolean[width][height];
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            int[] item = queue.poll();
            int i = item[0];
            int j = item[1];

            if (i < Integer.max(pos[0] - 300, 0) ||
                    i >= Integer.min(pos[0] + 300, width) ||
                    j < Integer.max(0, pos[1] - 400) ||
                    j >= Integer.max(height, pos[1] + 400) ||
                    vMap[i][j]) {
                continue;
            }
            vMap[i][j] = true;

            try {
                if (comparator.operation(image.getRGB(i, j), color, difference)) {
                    if (i < pos[2]) {
                        pos[2] = i;
                        pos[3] = j;
                    }
                    if (i > pos[4]) {
                        pos[4] = i;
                        pos[5] = j;
                    }
                    if (j < pos[1]) {
                        pos[0] = i;
                        pos[1] = j;
                    }
                    if (j > pos[7]) {
                        pos[6] = i;
                        pos[7] = j;
                    }

                    queue.add(new int[]{i - 1, j});
                    queue.add(new int[]{i + 1, j});
                    queue.add(new int[]{i, j + 1});
                    queue.add(new int[]{i, j - 1});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pos;
    }

    static boolean isMarketTopVertex(int rgb) {
        return ImageHelper.isSameColor(
                rgb,
                R_MARKET_TARGET, G_MARKET_TARGET, B_MARKET_TARGET);
    }

    static boolean isDesignTopVertex(int rgb) {
        return ImageHelper.fuzzyMatch(
                rgb,
                DESIGN_TARGET, DESIGN_TARGET, DESIGN_TARGET,
                8);
    }

//    // 获取开始扫描的 y 值
//    static int getStartY(BufferedImage image) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        int pixel, lastPixel;
//        int startY = -1;
//
//        // 以 50px 步长，尝试探测 startY
//        for (int i = height / 3; i < height * 2 / 3; i += 50) {
//            lastPixel = image.getRGB(0, i);
//            for (int j = 1; j < width; j++) {
//                pixel = image.getRGB(j, i);
//                // 不是纯色，则记录并跳出循环
//                if (pixel != lastPixel) {
//                    startY = i - 50;
//                    break;
//                }
//            }
//            if (startY != -1) {
//                break;
//            }
//        }
//
//        return startY;
//    }

}

interface Comparator {
    boolean operation(int oldRgb, int newRgb, int t);
}

