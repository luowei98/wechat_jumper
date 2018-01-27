package com.luowei.wechat;

import java.awt.image.BufferedImage;

/**
 * Created by luo.wei on 2018/1/22.
 * <p>
 * 查找棋子位置
 */
class PieceFinder {
    private static final int PIECE_BASE_HEIGHT_1_2 = Config.getInt("piece_base_height_1_2", 20);

    private static final int R_TARGET = 40;
    private static final int G_TARGET = 43;
    private static final int B_TARGET = 86;

    static int[] find(BufferedImage image) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pos = {0, 0};
//        int startY = ImageHelper.getStartY(image);

        int pieceXSum = 0;
        int pieceXCount = 0;
        int pieceYMax = 0;

        // 从 3/4 开始往上扫描到棋子的底部
        for (int i = height * 3 / 4; i > 0; i--) {
            // 横向 1/8 处开始，7/8 处结束
            for (int j = width / 8; j < width * 7 / 8; j++) {
                if (ImageHelper.isPiece(image.getRGB(j, i))) {
                    pieceXSum += j;
                    pieceXCount++;
                }
            }
            if (pieceXCount > 0) {
                pieceYMax = i;
                break;
            }
        }

        if (pieceXSum == 0 || pieceXCount == 0) {
            return null;
        }
        pos[0] = pieceXSum / pieceXCount;
        // 上移棋子底盘高度的一半
        pos[1] = pieceYMax - PIECE_BASE_HEIGHT_1_2;

        return pos;
    }
}
