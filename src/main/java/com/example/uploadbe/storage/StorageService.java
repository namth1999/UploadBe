package com.example.uploadbe.storage;

import com.example.uploadbe.commons.FileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file, String filePath);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename, String filePath);

    boolean delete(String filename, String filePath) throws IOException;

    void deleteAll();

    FileResponse storev2(MultipartFile file, String filePath) throws IOException;

}