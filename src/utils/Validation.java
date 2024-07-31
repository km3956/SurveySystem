package utils;

import java.util.ArrayList;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Validate that values are what they're supposed to be. Very helpful when getting user
 * input.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Validation {
    
    /**
     * Validate that a string is a number
     * @param num The string to be validated
     * @return True if the String can be parsed to an int, else false
     */
    public static boolean isInt(String num){
        try{
            Integer.parseInt(num);
            return true;
        }
        // Not an int
        catch(Exception e){
            return false;
        }
    }

    /**
     * Validate that a string is a double
     * @param num The string to be validated
     * @return True if it's a double, else false
     */
    public static boolean isDouble(String num){
        try{
            Double.parseDouble(num);
            return true;
        }
        // Not a double
        catch(Exception e){
            return false;
        }
    }

    /**
     * Validate that a String is a valid integer between an inclusive range
     * @param start The start of the inclusive range
     * @param end The end of the inclusive range
     * @param num The String to be validated
     * @return True if it's a valid int between the inclusive range, else false
     */
    public static boolean isIntBetweenInclusive(int start, int end, String num){
        if(!isInt(num)) return false;
        int val = Integer.parseInt(num);
        return val >= start && val <= end;
    }

    public static int validIntegerInput(){
        boolean flag = true;
        int option = 0;
        while (flag){
            try {
                option = Integer.parseInt(Input.readInput());
                flag = false;
            } catch (NumberFormatException e) {
                Output.output("Please enter a valid number.");
            }
        }
        return option;
    }

    public static String validStringInput() {
        String input = Input.readInput();

        while (input.isEmpty()) {
            Output.output("Please enter something.");
            input = Input.readInput();
        }

        return input;
    }

    public static String validSingleCharInput(){
        String input = Input.readInput();

        while (input.length() != 1) {
            Output.output("Please enter 1 character.");
            input = Input.readInput();
        }

        return input;
    }

    public static String validSingleCharInputMCQ(ArrayList<String> options) {
        String input = Input.readInput().toUpperCase();

        while (input.length() != 1 || !Character.isLetter(input.charAt(0)) || input.charAt(0) >= 'A' + options.size()) {
            Output.output("Please enter from the available options.");
            input = Input.readInput().toUpperCase();
        }

        return input;
    }

    public static String validSingleCharInputTrueOrFalse(){
        String input = Input.readInput();

        while (!(input.equalsIgnoreCase("T") || input.equalsIgnoreCase("F"))) {
            Output.output("Please enter only T/F.");
            input = Input.readInput();
        }

        return input.toUpperCase();
    }

    public static String validDateInput() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        String date = "";

        while (true) {
            date = Input.readInput();
            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    dateFormat.parse(date);
                    break;
                } catch (ParseException e) {
                    Output.output("Invalid date. Please enter a valid date in YYYY-MM-DD format.");
                }
            } else {
                Output.output("Invalid format. Please enter the date in YYYY-MM-DD format.");
            }
        }
        return date;
    }

    public static String validFormattedInput(ArrayList<String> leftColumn, ArrayList<String> rightColumn) {
        String pattern = "^[A-Za-z] \\d+$";
        String input = "";

        int maxLeftIndex = leftColumn.size();
        int maxRightIndex = rightColumn.size();

        while (true) {
            input = Input.readInput();
            if (input.matches(pattern)) {
                char inputChar = Character.toUpperCase(input.charAt(0));
                int inputNumber = Integer.parseInt(input.substring(2));
                int leftIndex = inputChar - 'A';
                int rightIndex = inputNumber - 1; // Assuming inputNumber is 1-based index

                if (leftIndex < maxLeftIndex && rightIndex < maxRightIndex) {
                    break;
                } else {
                    Output.output("Out of bounds values");
                }
            } else {
                Output.output("Invalid format. Please enter a single uppercase letter, a space, and a number (e.g., A 1).");
            }
        }
        return input;
    }

}
