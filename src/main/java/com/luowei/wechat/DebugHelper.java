package com.luowei.wechat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by luo.wei on 2018/1/23.
 *
 * 调试分析类
 */
public class DebugHelper {
    static void saveDebugScreenshot(BufferedImage image, int[] piecePos, int[] vertex) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage desc = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = desc.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

        // 保存原文件
        File orgFile = new File(
                Config.getString("screenshot_dir"),
                Long.toString(System.currentTimeMillis()) + ".png");
        if (!orgFile.exists() && orgFile.createNewFile()) {
            ImageIO.write(desc, "png", orgFile);
        }

        // 棋子底面中心点
        g.setColor(Color.RED);
        g.fillOval(piecePos[0] - 10, piecePos[1] - 10, 20, 20);
        g.drawLine(piecePos[0], 0, piecePos[0], height);
        g.drawLine(0, piecePos[1], width, piecePos[1]);

        // 下一步中心点
        g.setColor(Color.BLUE);
        g.fillOval(vertex[0] - 10, vertex[1] - 10, 20, 20);
        g.drawLine(vertex[0], 0, vertex[0], height);
        g.drawLine(0, vertex[1], width, vertex[1]);

        // 下一步上、左、右点
        g.setColor(Color.GREEN);
        g.fillOval(vertex[2] - 10, vertex[3] - 10, 20, 20);
        g.fillOval(vertex[4] - 10, vertex[5] - 10, 20, 20);
        g.fillOval(vertex[6] - 10, vertex[7] - 10, 20, 20);

        // 保存调试文件
        orgFile = new File(
                Config.getString("screenshot_dir"),
                Long.toString(System.currentTimeMillis()) + ".png");
        if (!orgFile.exists() && orgFile.createNewFile()) {
            ImageIO.write(desc, "png", orgFile);
        }
    }

    public static void main(String [] args) throws IOException {
        Logger logger = LogManager.getLogger(DebugHelper.class);

        logger.debug("start...");
        BufferedImage image = ImageHelper.load(Config.getString("screenshot_dir") + HandleHelper.screenshotPath);

        int[] piecePos = PieceFinder.find(image);
        if (piecePos == null) {
            return;
        }
        int[] nextPos = NextFinder.find(image, piecePos);
        logger.debug("end!");

        DebugHelper.saveDebugScreenshot(image, piecePos, nextPos);
    }
}
