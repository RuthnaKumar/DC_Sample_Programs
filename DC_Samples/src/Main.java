// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        String installStatus = "Installation completed successfully.";
        if (installStatus.contains(installStatus) || (installStatus.contains("Installation") && installStatus.contains("successfully"))) {
            System.out.println("successfully");
        } else {
            System.out.println("un successfully");
        }
    }
}