package com.sms;

import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SMSUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getValidatedInput(String prompt, Predicate<String> validator, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (validator.test(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    public static int getValidatedInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    public static void displayMenu() {
        System.out.println("\nStudent Management System");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. View Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Export Students to CSV");
        System.out.println("7. Exit");
    }
}