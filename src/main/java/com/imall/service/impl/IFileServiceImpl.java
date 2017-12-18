package com.imall.service.impl;

import com.google.common.collect.Lists;
import com.imall.service.IFileService;

import com.imall.utils.FTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileServiceImpl")
public class IFileServiceImpl implements IFileService {

    private static Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName=file.getOriginalFilename();

        //获取扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1 );
        //上传后的文件名
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件名：{},上传路径：{},新文件名：{}",fileName,path,uploadFileName);

        File fileDir=new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);

        try {
            //做文件上传
            file.transferTo(targetFile);
            logger.info("文件上传成功！");
            //将targetFile上传到ftp中
            FTPUtils.uploadFile(Lists.newArrayList(targetFile));
            //删除本地文件
            targetFile.delete();


        } catch (IOException e) {
            logger.error("文件上传异常!"+e);
            return null;
        }
        return targetFile.getName();
    }
}
