package com.imall.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtils {
    private static Logger logger=Logger.getLogger(PropertiesUtils.class);

    private static Properties properties;

    static {
        String fileName="config.properties";
        properties=new Properties();
        try {
            properties.load(new InputStreamReader(PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName),"utf-8"));

        } catch (IOException e) {
            logger.error("配置文件读取异常！"+e);
        }
    }

    public static String getProperties(String key){
        String value=properties.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperties(String key,String defaultValue){
        String value=properties.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return defaultValue.trim();
        }
        return value.trim();
    }
}
