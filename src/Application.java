//Fatima Harrison, CEN 3024C, 10/2/2025
//This is where Terralina comes alive with the basics of staring with the development of programming the Database Management System.
//The program offers a comprehensive approach to the functionality of a Database Management System,
//delivering a fully operational experience.
// Users, particularly those in host and customer role, have the ability to create, update, and delete records seamlessly within the system.
// A noteworthy feature included is the custom access to the audit log, allowing for detailed oversight of user activity in management.
// To access these functions, users will need to enter a seven-digit employee or customer ID when updating or removing a record.
// For record creation, users must provide a reservation description, guided by the prompts presented.
// Additionally, the program will showcase a complete listing of all customer reservations when using the display option.
import java.util.Scanner;
//Importing the main class util scanner.
//Declaring Application main class
public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DMS manager = new DMS(); //Importing DMS scanner.
     //Load file before login for user ID number
        //It's also used as a validation
        System.out.print("Enter login file: ");
        String loginPath = scanner.nextLine().trim();
        manager.loadFromFile(loginPath); // Populates validUsers
        System.out.print("Enter your role (Host/Manager): ");
        String role = scanner.nextLine().trim();

        if (!role.equalsIgnoreCase("Host") && !role.equalsIgnoreCase("Manager")) {
            System.out.println("Access Denied");
            role = "Guest";
            return;
        }
       //Prompts the user to enter their 7 digit ID number. 
        System.out.print("Enter your 7-digit Employee ID: ");
        String idInput = scanner.nextLine().trim();
       //Notify them if it's invalid
        if (!idInput.matches("\\d{7}")) {
            System.out.println("Invalid ID format. Must be a 7-digit number.");
            return;
        }

        int employeeId = Integer.parseInt(idInput);

// Validate login
        if (!manager.validUsers.containsKey(employeeId) ||
                !manager.validUsers.get(employeeId).equalsIgnoreCase(role)) {
            System.out.println("Access Denied: ID and Role do not match system records.");
            return;
        }

        System.out.println("Logged in as: " + role);

        //A while loop to print out each Terralina DMS user menu
        while (true) {
            System.out.println("\n--- Terralina DMS Menu ---");
            if (role.equalsIgnoreCase("Host")) {
                System.out.println("1. Load Records from File");//Insert file
                System.out.println("2. Display Reservations");//Show reservation
                System.out.println("3. Create New Reservation");//Set new reservation
                System.out.println("4. Cancel Reservation");//Remove Reservation
                System.out.println("5. Update Reservation"); //Modify Reservation
                System.out.println("6. Exit"); //Exit
            } else if (role.equalsIgnoreCase("Manager")) {
                System.out.println("1. Load Audit Logs from File");//Insert file
                System.out.println("2. Display Log");//Display the audit log listing
                System.out.println("3. Remove Data");//Remove data from log audit log
                System.out.println("4. Audit log");//Access the audit log
                System.out.println("6. Exit");//Exit menu
            } else {
                System.out.println("6. Exit");//Exit menu
            }
            //Custom end line
            System.out.print("---------------------:");
            String input = scanner.nextLine().trim();
            //Switch functions for each menu options
            //Switch cases for menu options
            switch (input) {
                case "1": //User based file paths
                    System.out.print("Enter file path: ");
                    String path = scanner.nextLine().trim();
                    if (role.equalsIgnoreCase("Host")) { //Host only can read the records file
                        manager.loadFromFile(path); // Load reservation records
                    } else if (role.equalsIgnoreCase("Manager")) { //Manager can only read the data file.
                        manager.loadFromFile(path); // Load audit logs
                    } else {
                        System.out.println("Access Denied"); //Otherwise it's denied.
                    }
                    break;

                case "2": //Case for the user can display their file.
                    if (role.equalsIgnoreCase("Host")) {
                        manager.displayRecords(); // Show reservations
                    } else if (role.equalsIgnoreCase("Manager")) {
                        manager.displayAuditLogs();
                    } else {
                        System.out.println("Access Denied"); //Otherwise denied.
                    }
                    break;
                case "3": //Cases for the host to add a reservation and the manger to remove n employee.
                    if (role.equalsIgnoreCase("Host")) {
                        manager.addRecord(scanner);
                    } else if (role.equalsIgnoreCase("Manager")) {
                        System.out.print("Enter Employee ID: ");
                        try { //Allowing the system to retrieve the ID number.
                            Integer employId = Integer.parseInt(scanner.nextLine().trim());
                            manager.removeAuditLog(employId);
                        } catch (NumberFormatException e) { //Notify the user if an error has occurred.
                            System.out.println("Invalid ID format.");
                        }
                    } else {
                        System.out.println("Access Denied"); //Otherwise denied.
                    }
                    break;

                case "4": //Cases for the host to remove a reservation.
                    if (role.equalsIgnoreCase("Host")) {
                        System.out.print("Enter ID to remove: ");
                        manager.removeRecord(Integer.valueOf(scanner.nextLine().trim()));
                    } else if (role.equalsIgnoreCase("Manager")) { //Case for the manager to generate the audit report.
                        manager.generateAuditReport(scanner, role);
                    } else {
                        System.out.println("Access Denied"); //Otherwise denied.
                    }
                    break;
                case "5": //Option for host to update a reservation
                    if (role.equalsIgnoreCase("Host")) {
                        System.out.print("Enter Reserved ID to update: ");
                        manager.updateRecord(scanner, Integer.valueOf(scanner.nextLine().trim()));
                    } else {
                        System.out.println("Access Denied"); //Otherwise denied.
                    }
                    break;
                case "6": //Case allowing the users to log out successfully.
                    System.out.println("Successfully logged out");
                    return;
                default: //Handling invalid entries.
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
