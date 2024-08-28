package com.example.FileManagemnt.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Component
public class FileStrorageService {

    //rEMEMBER TO CHANGE THE FOLDER PERMISSION FOR OTHERS USER ON LINUX
    public   static  final  String STORAGE_DIRECTORY ="/home/bhuwan/Desktop/college";

    public  void saveFile(MultipartFile filetoSave) throws IOException {
        if (filetoSave == null){
            throw  new NullPointerException("File is null");
        }
        var targetfile = new File(STORAGE_DIRECTORY + File.separator + filetoSave.getOriginalFilename());

        if (!Objects.equals(targetfile.getParent(),STORAGE_DIRECTORY)){
            throw  new SecurityException("Unsupported file name!!");
        }

        Files.copy(filetoSave.getInputStream(),targetfile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }
    public  File getDownloadFile(String filename) throws  Exception{
        if (filename == null){
            throw  new NullPointerException("filename is null!!");
        }
        var filetoDownload = new File(STORAGE_DIRECTORY + File.separator + filename);
        if (!Objects.equals(filetoDownload.getParent(),STORAGE_DIRECTORY)){
            throw  new SecurityException("Unsupported file name!!");
        }
        if (!filetoDownload.exists()){
            throw  new FileNotFoundException("No file "+filename);
        }
        return  filetoDownload;
    }
}
