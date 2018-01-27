package com.luowei.wechat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by luo.wei on 2018/1/22.
 * <p>
 * 查找下一步落点
 */
class NextFinder {
    private static final int WHITE_POINT_TARGET = 245;

    static int[] find(BufferedImage image, int[] piecePos) {

        int[] vertex = findVertex(image, piecePos);
        int[] nextPos;

        nextPos = findWhitePoint(image, vertex);
        if (nextPos == null) {
            nextPos = new int[]{vertex[0], vertex[3]};
        }
        // 中心 x,y，上 x,y，左 x,y，右 x,y
        nextPos = new int[]{nextPos[0], nextPos[1], vertex[0], vertex[1], vertex[2], vertex[3], vertex[4], vertex[5]};

        return nextPos;
    }

    private static int[] findVertex(BufferedImage image, int[] piecePos) {
        int width = image.getWidth();
        int height = image.getHeight();

        // 确定扫描范围
        int startX;
        int endX;
        if (piecePos[0] < width / 2) {
            startX = piecePos[0];
            endX = width;
        } else {
            startX = 0;
            endX = piecePos[0];
        }
        int startY = height / 3;
        int endY = piecePos[1];

        // 开始扫描顶点
        int pieceBodyWidth = Config.getInt("piece_body_width", 70);
        int posXSum;
        int posXCount;
        int[] pos = new int[6];
        int lastPixel, pixel;
        for (int i = startY; i < endY; i++) {
            lastPixel = image.getRGB(0, i);
            if (pos[0] > 0) {
                break;
            }
            posXSum = 0;
            posXCount = 0;

            for (int j = startX; j < endX; j++) {
                // 去除同棋子的纵向位置，修掉脑袋比下一个小格子还高的情况
                if (Math.abs(j - piecePos[0]) < pieceBodyWidth) {
                    continue;
                }
                pixel = image.getRGB(j, i);

                if (!ImageHelper.fuzzyMatch(pixel, lastPixel, 16)) {
                    posXSum += j;
                    posXCount++;
                }
            }

            if (posXSum > 0 && posXCount > 0) {
                pos[0] = posXSum / posXCount;
                pos[1] = i;
            }
        }

        // 判断是否是瓶子
        if (BottleFinder.isBottleColor(image.getRGB(pos[0], pos[1]))) {
            int[] result = BottleFinder.find(image, pos);
            if (result != null) {
                return result;
            }
        }

        // 判断是否是凳子
        if (WoodenFinder.isWoodenColor(image.getRGB(pos[0], pos[1]))) {
            int[] result = WoodenFinder.find(image, pos);
            if (result != null) {
                return result;
            }
        }

        // 判断是否是大理石
        if (MarbleFinder.isMarbleColor(image.getRGB(pos[0], pos[1]))) {
            int[] result = MarbleFinder.find(image, pos);
            if (result != null) {
                return result;
            }
        }

        // 判断是否是唱片
        if (DiscFinder.isDiscColor(image.getRGB(pos[0], pos[1]))) {
            int[] result = DiscFinder.find(image, pos);
            if (result != null) {
                return result;
            }
        }

        // 判断是否是咖啡杯
        if (CupFinder.isCupColor(image.getRGB(pos[0], pos[1]))) {
            int[] result = CupFinder.find(image, pos);
            if (result != null) {
                return result;
            }
        }

        // 清除小超市顶点颜色不同的问题
        if (ImageHelper.isMarketTopVertex(image.getRGB(pos[0], pos[1]))) {
            pos[1] += 1;
        }

        // 清除设计书顶点颜色不同的问题
        if (ImageHelper.isDesignTopVertex(image.getRGB(pos[0], pos[1]))) {
            pos[1] += 1;
        }

        // 扫描左右顶点
        pos = ImageHelper.findRange(
                image,  image.getRGB(pos[0], pos[1]), pos, 16, ImageHelper::fuzzyMatch);

        return pos;
    }

    private static int[] findWhitePoint(BufferedImage image, int[] vertex) {

        // top[0] - 120, top[1], top[0] + 120, top[1] + 180
        for (int i = vertex[0] - 80; i < vertex[0] + 80; i++) {
            for (int j = vertex[1] + 20; j < vertex[1] + 180; j++) {
                if (ImageHelper.isSameColor(
                        image.getRGB(i, j),
                        WHITE_POINT_TARGET, WHITE_POINT_TARGET, WHITE_POINT_TARGET)) {
                    int[] pos = ImageHelper.findRange(
                            image,
                            new Color(WHITE_POINT_TARGET, WHITE_POINT_TARGET, WHITE_POINT_TARGET).getRGB(),
                            new int[] {i, j},
                            0,
                            ImageHelper::fuzzyMatch);

                    // 35 <= maxX - minX <= 45 && 20 <= maxY - minY <= 30
                    if (pos[4] - pos[2] <= 45 && pos[4] - pos[2] >= 35 &&
                            pos[7] - pos[1] <= 30 && pos[7] - pos[1] >= 20) {
                        return new int[] {(pos[2] + pos[4]) / 2, (pos[1] + pos[7]) / 2};
                    }
                }
            }
        }

        return null;
    }
}
