package com.luowei.wechat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by luo.wei on 2018/1/20.
 *
 * 微信挑一挑外挂主入口
 */
public class Application {
    private static Logger logger = LogManager.getLogger(Application.class);

    // DEBUG 开关，需要调试的时候请改为 true，不需要调试的时候为 false
    private static final boolean DEBUG_SWITCH = true;

    public static void main(String [] args) throws InterruptedException, IOException {
//        System.setOut(new PrintStream(System.out, true, "GBK"));
        // 确认准备就绪
        System.out.println("请确保手机打开了 ADB 并连接了电脑，");
        System.out.print("然后打开跳一跳并【开始游戏】后再用本程序，确定开始？[Y/n]:");
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()) {
            String answer = sc.nextLine();
            if (answer.isEmpty() || answer.toLowerCase().equals("y")) {
                break;
            } else if (answer.toLowerCase().equals("n")) {
                System.exit(0);
            }
            System.out.print("确定开始？[Y/n]:");
        }

//        if (DEBUG_SWITCH) {
//            DebugHelper.dumpDeviceInfo();
//        }

        int i = 0;
        int nextRest = ThreadLocalRandom.current().nextInt(3, 10 + 1);
        int nextRestTime = ThreadLocalRandom.current().nextInt(5, 10 + 1);

        while (true) {

            HandleHelper.pull(Config.getString("screenshot_dir"));
            BufferedImage image = ImageHelper.load(HandleHelper.screenshotPath);

            int[] piecePos = PieceFinder.find(image);
            if (piecePos == null) {
                break;
            }

            int[] nextPos = NextFinder.find(image, piecePos);

            HandleHelper.jump(image, piecePos, nextPos);

            if (DEBUG_SWITCH) {
                 DebugHelper.saveDebugScreenshot(image, piecePos, nextPos);
            }

            // 防 ban，随机暂停若干秒
            i++;
            if (i == nextRest) {
                logger.info(String.format("已经连续打了 %d 下，休息 %ds", i, nextRestTime));
                for (int j = 0; j < nextRestTime; j++) {
                    logger.info(String.format("程序将在 %ds 后继续", nextRestTime - j));
                    Thread.sleep(1000);
                }
                logger.info("继续");

                i = 0;
                nextRest = ThreadLocalRandom.current().nextInt(30, 100);
                nextRestTime = ThreadLocalRandom.current().nextInt(5, 30);
            }

            Thread.sleep(ThreadLocalRandom.current().nextInt(2200, 3000));
        }
    }

}
