package com.app.e_library.util;

import org.springframework.web.multipart.MultipartFile;

public interface FileReader<T> {

    T readFile(MultipartFile file);
}
