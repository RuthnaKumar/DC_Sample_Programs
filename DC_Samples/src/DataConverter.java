import java.util.Scanner;

public class DataConverter {

    // Function to convert KB to MB
    public static double kbToMb(double kilobytes) {
        return kilobytes / 1024;
    }

    // Function to convert B to MB
    public static double bytesToMb(double bytes) {
        return bytes / (1024 * 1024);
    }

    // Function to process input and convert to MB
    public static double convertToMb(String valueWithUnit) {
        double value;
        String unit = valueWithUnit.replaceAll("[0-9.]", "").toLowerCase();
        valueWithUnit = valueWithUnit.replaceAll("[^0-9.]", "");

        switch (unit) {
            case "kb":
                value = Double.parseDouble(valueWithUnit);
                return kbToMb(value);
            case "b":
                value = Double.parseDouble(valueWithUnit);
                return bytesToMb(value);
            case "mb":
                value = Double.parseDouble(valueWithUnit);
                return value;
            default:
                throw new IllegalArgumentException("Invalid unit. Please provide value with 'b', 'kb', or 'mb'.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input values
        System.out.print("Enter the first value (with unit): ");
        String firstValue = scanner.next();
        System.out.print("Enter the second value (with unit): ");
        String secondValue = scanner.next();

        try {
            // Convert values to MB
            double firstValueInMb = convertToMb(firstValue);
            double secondValueInMb = convertToMb(secondValue);

            // Print results
            System.out.printf("The first value in MB is: %.8f MB%n", firstValueInMb);
            System.out.printf("The second value in MB is: %.8f MB%n", secondValueInMb);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}
