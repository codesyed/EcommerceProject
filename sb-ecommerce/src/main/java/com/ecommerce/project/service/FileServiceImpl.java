package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements  FileService{
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        /*
            -> get the full fileName
            -> Generate a unique file name (uniqueName+FileExtension)
            -> upload this file(coming from API-Postman) to server.
               But before uploading Check whether path exists or need to create?
            -> return unique generate fileName
         */

        //fileName.extension
        String originalImageName = image.getOriginalFilename();
        String fileExtension = originalImageName.substring(originalImageName.lastIndexOf('.'));

        String randomId = UUID.randomUUID().toString();
        String uniqueFileName = randomId.concat(fileExtension);

        //Creating path for uploading file
        //Use File.seperator to run safe on all OS
        String filePath = path + File.separator + uniqueFileName;

        //Creating or Checking whether folder exists or not
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        Files.copy(image.getInputStream(), Paths.get(filePath));
        return uniqueFileName;
    }
}
