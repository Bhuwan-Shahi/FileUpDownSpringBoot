package com.example.FileManagemnt.controller;

import com.example.FileManagemnt.service.FileStrorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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


@Controller
public class GuiContoller {
    @Autowired
    FileStrorageService fileStrorageService;
    @GetMapping("/uploader")
    public  String uploader(){
        return  "uploader";
    }

    @GetMapping("/list-files")
    public String listFiles(Model model) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(new
                File(fileStrorageService.STORAGE_DIRECTORY).toPath())) {
            model.addAttribute("files",
                    StreamSupport.stream(stream.spliterator(), false)
                            .map(Path::getFileName)
                            .map(Path::toString)
                            .collect(Collectors.toList()));
        }
        return "list";
    }


}

