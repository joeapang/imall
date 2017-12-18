package com.imall.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtils {
    private static final Logger logger = LoggerFactory.getLogger(FTPUtils.class);


    private static String ftpIp = PropertiesUtils.getProperties("ftp.server.ip");
    private static String ftpUser = PropertiesUtils.getProperties("ftp.user");
    private static String ftpPassword = PropertiesUtils.getProperties("ftp.password");


    private String ip;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public static boolean uploadFile(List<File> files) {
        FTPUtils ftpUtils = new FTPUtils(ftpIp, 21, ftpUser, ftpPassword);

        logger.info("开始连接ftp");
        boolean result=ftpUtils.uploadFile("img",files);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> files) {
        boolean upload = true;
        FileInputStream fis = null;
        if (connectionFtpServer(this.ip, this.port, this.user, this.password)) {
            try {
                //切换工作目录到目标目录
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setBufferSize(1024);
                //设置被上传的文件类型为二进制类型
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();

                for (File item : files) {
                    fis = new FileInputStream(item);
                    ftpClient.storeFile(remotePath, fis);
                }
            } catch (IOException e) {
                upload = false;
                logger.error("上传到Ftp失败" + e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                        disconnectionFtpServer();
                    } catch (IOException e) {
                        logger.error("IO异常,请检查网络或者服务器："+e);
                    }
                }
                ;

            }

        }
        return upload;
    }


    private boolean connectionFtpServer(String ip, int port, String user, String password) {
        boolean isSuccess = false;
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, password);
        } catch (IOException e) {
            logger.error("连接ftp异常:" + e);
        }
        return isSuccess;
    }

    private void disconnectionFtpServer() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error("ftp断开异常，请检查网络：" + e);
            }
        }
    }

    public FTPUtils(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
