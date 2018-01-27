package com.luowei.wechat;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;
import java.util.List;

/**
 * Created by luo.wei on 2018/1/20.
 *
 * 读取配置文件类
 */
class Config {

    private final static String configFileName = "config.properties";
    private final static InputStreamReader stream = new InputStreamReader(
            Config.class.getClassLoader().getResourceAsStream(configFileName)
    );
    private final static PropertiesConfiguration properties = new PropertiesConfiguration();

    static {
        try {
            properties.setListDelimiterHandler(new DefaultListDelimiterHandler(','));
            properties.read(new BufferedReader(stream));
        } catch (ConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }


    static String getString(String key) {
        return properties.getString(key);
    }

    static int getInt(String key, int defaultValue) {
        return properties.getInt(key, defaultValue);
    }

    static <T> List<T> getList(Class<T> cls, String key) {
        return properties.getList(cls, key);
    }

}
