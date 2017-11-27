package com.imall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class TokenCatch {
    private static Logger logger = Logger.getLogger(TokenCatch.class);
    private static LoadingCache<String, String> loadingCache
            = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000)
            .expireAfterAccess(15, TimeUnit.MINUTES).
                    build(new CacheLoader<String, String>() {
                        //默认的数据加载实现，当调用get取值时，没有对应的key值，就用这个方法加载
                        @Override
                        public String load(String s) throws Exception {

                            return null;
                        }
                    });
    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }

    public static  String getKey(String key){
        String value=null;
        try{
            value=loadingCache.get(key);
            if (StringUtils.isEmpty(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("========LocalCatch get an Error" +e);
        }
        return null;
    }

    public static void removeKey(String key){
        try {
            if(getKey(key)!=null) {
                loadingCache.invalidate(key);
            }
        }catch (Exception e){
            logger.error("==========removeCatch get an Error"+e);
        }
    }
}
