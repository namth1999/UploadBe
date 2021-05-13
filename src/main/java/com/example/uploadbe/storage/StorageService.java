package com.example.uploadbe.storage;

import com.example.uploadbe.commons.FileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    boolean delete(String filename) throws IOException;

    void deleteAll();

    FileResponse storev2(MultipartFile file);

}