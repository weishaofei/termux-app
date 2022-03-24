package com.termux.app;

import android.content.Context;
import android.widget.Toast;

import com.termux.shared.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;

public class KRHInitConfig {
    private static final String LOG_TAG = "TermuxActivity";

    public static void initConfig(Context context) {
        Logger.logDebug(LOG_TAG, "init bash");
        try {
            // dpkg --configure -a
            String copyContent =
                "sed -i 's@^\\(deb.*stable main\\)$@#\1\\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/termux-packages-24 stable main@' $PREFIX/etc/apt/sources.list\n" +
                    "sed -i 's@^\\(deb.*games stable\\)$@#\1\\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/game-packages-24 games stable@' $PREFIX/etc/apt/sources.list.d/game.list\n" +
                    "sed -i 's@^\\(deb.*science stable\\)$@#\1\\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/science-packages-24 science stable@' $PREFIX/etc/apt/sources.list.d/science.list\n" +
                    "yes | apt upgrade\n" + // TODO: 2022/3/24 偶发脚本执行失败 
                    "pkg install -y openssh\n" +
                    "pkg install termux-tools\n" +
                    "termux-setup-storage";// TODO: 2022/3/24 判断是否已经安装
            File fs = new File("/data/data/com.termux/files/home/.bashrc");
            FileOutputStream outputStream = new FileOutputStream(fs);
            outputStream.write(copyContent.getBytes());
            outputStream.flush();
            outputStream.close();
            Logger.logDebug(LOG_TAG, "success bash");
            Toast.makeText(context, "File created successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            System.out.println("fail: " + e.getMessage());
        }
    }
}
