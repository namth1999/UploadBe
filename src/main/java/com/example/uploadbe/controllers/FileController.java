package com.example.uploadbe.controllers;

import com.example.uploadbe.commons.FileResponse;
import com.example.uploadbe.storage.FileSystemStorageService;
import com.example.uploadbe.storage.StorageException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class FileController {

    private final FileSystemStorageService storageService;

    @GetMapping("/")
    public String listAllFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(path.getFileName().toString())
                        .toUriString())
                .collect(Collectors.toList()));

        return "listFiles";
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename, @RequestParam String filePath) {

        Resource resource = storageService.loadAsResource(filename, filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@RequestParam String filename, @RequestParam String filePath) {
        try {
            boolean deletable = storageService.delete(filename, filePath);
            return ResponseEntity.status(HttpStatus.OK).body("" + deletable);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String filePath) {
        try {
            FileResponse fileResponse = storageService.storev2(file, filePath);
            return ResponseEntity.status(HttpStatus.OK).body(fileResponse);
        } catch (StorageException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("preview")
    public ResponseEntity<byte[]> getImage(@RequestParam String path) throws IOException {
        File img = new File("uploads/" + path);
        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
    }

}
