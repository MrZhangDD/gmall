package com.atguigu.gmall.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description
 * @ClassName FileUploadController
 * @Author
 */
@RestController
public class FileUploadController {
    //@value注解表示从application.properties文件中读取数据
    //该注解使用前提是该类必须被spring容器扫描，
    // fileServer.url=http://172.18.1.100
    @Value("${fileServer.url}")
    private String fileUrl;

    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException, MyException {
        //return "http://img14.360buyimg.com/n0/jfs/t1/40983/17/10549/262503/5d41672dEde2486a9/85e00374085c741e.jpg";
        // 图片的路径 imgUrl = http://192.168.67.209
        String imgUrl=fileUrl;

        if (file!=null){
            String configFile  = this.getClass().getResource("/tracker.conf").getFile();
            ClientGlobal.init(configFile);
            TrackerClient trackerClient=new TrackerClient();
            TrackerServer trackerServer=trackerClient.getConnection();
            StorageClient storageClient=new StorageClient(trackerServer,null);
            // 将该文件变成上传的文件
            String filename = file.getOriginalFilename();
            // 还需要得到上传文件的后缀名
            String extName = StringUtils.substringAfterLast(filename,".");

            //   String orginalFilename="e://img//zly.jpg";
            String[] upload_file = storageClient.upload_file(file.getBytes(), extName, null);

            for (int i = 0; i < upload_file.length; i++) {
                // s = group1
                // s = M00/00/00/wKhD0VvdynmAeKTIAACGx2c4tJ4972.jpg
                String path  = upload_file[i];
                // 第一次的path group1 第二次的path M00/00/00/wKhD0VvdynmAeKTIAACGx2c4tJ4972.jpg
                //  imgUrl = http://192.168.67.209/group1/M00/00/00/wKhD0VvdynmAeKTIAACGx2c4tJ4972.jpg
                imgUrl+="/"+path;
            }
        }
//        return "http://192.168.67.209/group1/M00/00/00/wKhD0VvdvJaAFvr0AAAl_GXv6Z4772_big.jpg";
        return imgUrl;
    }

}
