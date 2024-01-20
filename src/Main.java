import expressions.Expression;
import ordinals.OrdinalNormaliseExpression;
import parser.ExpressionParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String INPUT_FILE = "src/input.txt";
    private static final String OUTPUT_FILE = "src/output.txt";

    private static List<Boolean> getAnswers(List<String> inp) {
        List<Boolean> result = new ArrayList<>();
        ExpressionParser parser = new ExpressionParser();
        for (String s : inp) {
            String first = s.split("=")[0];
            String second = s.split("=")[1];

            Expression firstExpression = parser.parse(first);
            Expression secondExpression = parser.parse(second);

            OrdinalNormaliseExpression firstNormalise = firstExpression.normalise();
            OrdinalNormaliseExpression secondNormalise = secondExpression.normalise();

            result.add(firstNormalise.equals(secondNormalise));
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(INPUT_FILE))) {
            while (scanner.hasNextLine()) {
                input.add(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (input.isEmpty()) {
            return;
        }

        List<Boolean> answer = getAnswers(input);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, StandardCharsets.UTF_8))) {
            for (Boolean res : answer) {
                if (res) {
                    writer.write("Равны\n");
                } else {
                    writer.write("Не равны\n");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
