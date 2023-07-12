package com.marcofaccani.awssqs;

import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static String readFileAsString(String pathToFile) {
    try {
      return Files.readString(new ClassPathResource(pathToFile).getFile().toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readFileAsObject(String pathToFile, Class<T> targetType) {
    final var objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(readFileAsString(pathToFile), targetType);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
