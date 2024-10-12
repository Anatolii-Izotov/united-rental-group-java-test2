package com.app.test2springboot;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.CommandLineRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

@SpringBootApplication
public class Test2SpringBootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Test2SpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            Path filePath = Paths.get("products.json");

            String jsonInput = Files.readString(filePath);

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            JsonNode root = mapper.readTree(jsonInput);


            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            sc.close();

            switch (str) {
                case "print":
                    print(root);
                    System.exit(0);
                case "printRecursive":
                    printRecursive(root, 0);
                    System.exit(0);
                case "findMax":
                    System.out.println(findPath(root, findMax(root, 0), ""));
                    System.exit(0);
                default:
                    System.out.println("Method not found!");
                    System.exit(1);
            }
        } catch (IOException exception) {
            System.err.println("File error: " + exception.getMessage());
            System.exit(11);
        }
    }

    public void print(JsonNode root) {

        Iterator<String> firstLevelKeys = root.fieldNames();

        while (firstLevelKeys.hasNext()) {
            String firstLevelKey = firstLevelKeys.next();
            System.out.println(firstLevelKey);

            JsonNode secondLevelNode = root.get(firstLevelKey);
            Iterator<String> secondLevelKeys = secondLevelNode.fieldNames();

            while (secondLevelKeys.hasNext()) {
                String secondLevelKey = secondLevelKeys.next();
                System.out.print(".. ");
                System.out.println(secondLevelKey);

                JsonNode thirdLevelNode = secondLevelNode.get(secondLevelKey);
                Iterator<String> thirdLevelKeys = thirdLevelNode.fieldNames();
                while (thirdLevelKeys.hasNext()) {
                    String thirdLevelKey = thirdLevelKeys.next();
                    System.out.print(".... ");
                    System.out.println(thirdLevelKey);
                }
            }
        }
    }

    public void printRecursive(JsonNode root, int n) {
        if (root.isObject()) {
            Iterator<String> fieldNames = root.fieldNames();

            while (fieldNames.hasNext()) {
                if (root.isValueNode()) {
                    indent(n + 1);
                    System.out.println(root.asText());
                    break;
                }
                String fieldName = fieldNames.next();
                indent(n);
                System.out.println(fieldName);

                JsonNode childNode = root.get(fieldName);
                printRecursive(childNode, n + 1);
            }

        }
    }


    public int findMax(JsonNode root, int max) {
        if (root.isObject()) {
            Iterator<String> fieldNames = root.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = root.get(fieldName);
                max = findMax(childNode, max);
            }
        } else if (root.isValueNode()) {
            max = Math.max(max, root.intValue());
        }
        return max;
    }

    public String findPath(JsonNode root, int value, String path) {
        if (root.isObject()) {
            Iterator<String> fieldNames = root.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = root.get(fieldName);
                String newPath;
                if (path.isEmpty()) {
                    newPath = fieldName;
                } else {
                    newPath = path + " -> " + fieldName;
                }
                String foundPath = findPath(childNode, value, newPath);

                if (foundPath != null) {
                    return foundPath;
                }
            }
        } else if (root.isValueNode()) {
            if (root.intValue() == value) {
                return path + ": " + value;
            }
        }
        return null;
    }


    private void indent(int times) {
        for (int i = 0; i < times; i++) {
            System.out.print("..");
        }
    }
}