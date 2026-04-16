package com.aml.trustwatch.loader;

import com.aml.trustwatch.exception.DataLoadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class JsonDataLoader<T> {
    
    protected final ObjectMapper objectMapper;
    
    protected JsonDataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    protected List<T> loadFromFile(String filePath, TypeReference<List<T>> typeRef) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                return objectMapper.readValue(inputStream, typeRef);
            }
        } catch (IOException e) {
            throw new DataLoadException("Failed to load data from: " + filePath, e);
        }
    }
}

