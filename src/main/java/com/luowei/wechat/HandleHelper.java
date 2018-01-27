package com.luowei.wechat;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by luo.wei on 2018/1/21.
 *
 * 屏幕截图
 */
class HandleHelper {
    private static final Logger logger = LogManager.getLogger(HandleHelper.class);
    private static final String adbPath = Config.getString("adb_path");

    static String screenshotPath = "screenshot.png";

    static void pull(String saveDir) {

        if (!Files.exists(Paths.get(saveDir))) {
            try {
                Files.createDirectories(Paths.get(saveDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        screenshotPath = saveDir + "screenshot.png";

        String cmdCapture = adbPath + " shell /system/bin/screencap -p /sdcard/screenshot.png";
        String cmdPull = adbPath + " pull /sdcard/screenshot.png " + screenshotPath;

        try {
            Process process = Runtime.getRuntime().exec(cmdCapture);
            process.waitFor();
            process = Runtime.getRuntime().exec(cmdPull);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void jump(BufferedImage image, int[] piecePos, int[] nextPos) throws IOException {
        // 计算距离
        List<Float> coefficient = Config.getList(Float.class, "press_coefficient");

        int side1 = nextPos[0] - piecePos[0];
        int side2 = nextPos[1] - piecePos[1];
        double d = Math.sqrt(side1 * side1 + side2 * side2);
        int n = (int) (d / 450);
        int distance = (int) (d * coefficient.get(n));

        // 计算偏移量
        double eff = Math.atan((double) side2 / (double) side1);
        eff = Math.abs(180 * eff / Math.PI) - 30;
        if (eff > 3) {
            distance += eff * distance / 40;
        }
        distance = Integer.max(distance, 200);

        // 设置点击位置
        int width = image.getWidth();
        int height = image.getHeight();
        int pressX = width / 2;
        int pressY = 1584 * (height / 1920);
        pressX = ThreadLocalRandom.current().nextInt(pressX - 50, pressX + 50);
        pressY = ThreadLocalRandom.current().nextInt(pressY - 10, pressY + 10);

        logger.debug(String.format(" shell input swipe %d %d %d %d %d", pressX, pressY, pressX, pressY, distance));
        String cmd = adbPath + String.format(" shell input swipe %d %d %d %d %d", pressX, pressY, pressX, pressY, distance);
        Runtime.getRuntime().exec(cmd);
    }

}
