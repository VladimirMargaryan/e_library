package com.app.e_library.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileReaderBeanConfig {

    @Bean
    public CsvFileReader fileReader(){
        return new CsvFileReader();
    }
}
