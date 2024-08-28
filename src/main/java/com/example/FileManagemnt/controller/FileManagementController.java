package com.example.FileManagemnt.controller;

import com.example.FileManagemnt.service.FileStrorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
public class FileManagementController {
    @Autowired
    FileStrorageService fileStrorageService;
    private static final Logger logger = Logger.getLogger(FileManagementController.class.getName());


    @PostMapping("/Upload")
    public ResponseEntity<Boolean> uplodFile(@RequestParam("file") MultipartFile file) {

        try {
            fileStrorageService.saveFile(file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception during upload" + e, e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/Download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileName") String filename) {
        logger.log(Level.INFO, "[Faster] Download with /Download endpoint");


        try {
            var filetoDownload = fileStrorageService.getDownloadFile(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + filetoDownload.getName());

            return ResponseEntity.ok()

                    .headers(headers)
                    .contentLength(filetoDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(Files.newInputStream(filetoDownload.toPath())));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/fastDownload")
    public ResponseEntity<FileSystemResource> downloadFilefast(@RequestParam("fileName") String filename) {
        logger.log(Level.INFO, "[Faster] Download with /fastDownload endpoint");
        try {
            var filetoDownload = fileStrorageService.getDownloadFile(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + filetoDownload.getName());

            return ResponseEntity.ok()

                    .headers(headers)
                    .contentLength(filetoDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(filetoDownload));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
