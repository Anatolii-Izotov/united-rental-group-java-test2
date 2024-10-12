package com.app.test2springboot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Main class tests")
class Test2SpringBootApplicationTest {

    Test2SpringBootApplication test2SpringBootApplication;

    private final String jsonInput = """
            {
                "fruits": {
                    "apple": {
                        "green": 5,
                        "red": 4
                    },
                    "pear": {
                        "green": 2,
                        "light green": 4,
                        "dark green": 8
                    },
                    "lemon": {
                        "yellow": 3,
                        "green": 12
                    }
                },
                "vegetables": {
                    "carrot": {
                        "orange": 12,
                        "red": 21
                    },
                    "cabbage": {
                        "green": 8,
                        "dark green": 0
                    },
                    "leek": {
                        "green": 9,
                        "light green": 20
                    }
                }
            }
            """;

    @Test
    @DisplayName("print method test")
    void testPrint() throws Exception {
        ObjectMapper mapper = new ObjectMapper();


        JsonNode jsonNode = mapper.readTree(jsonInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        test2SpringBootApplication = new Test2SpringBootApplication();

        test2SpringBootApplication.print(jsonNode);

        String expectedOutput = """
                fruits
                .. apple
                .... green
                .... red
                .. pear
                .... green
                .... light green
                .... dark green
                .. lemon
                .... yellow
                .... green
                vegetables
                .. carrot
                .... orange
                .... red
                .. cabbage
                .... green
                .... dark green
                .. leek
                .... green
                .... light green
                """;

        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    @DisplayName("findMax method test")
    void findMaxTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = mapper.readTree(jsonInput);
        test2SpringBootApplication = new Test2SpringBootApplication();
        int result = test2SpringBootApplication.findMax(jsonNode, 0);
        int expectedResult = 21;
        assertEquals(result, expectedResult);

    }

    @Test
    @DisplayName("findPath method test")
    void findPathTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = mapper.readTree(jsonInput);
        test2SpringBootApplication = new Test2SpringBootApplication();

        String result = test2SpringBootApplication.findPath(jsonNode, 21, "");
        String expectedResult = "vegetables -> carrot -> red: 21";

        assertEquals(result, expectedResult);

    }
}