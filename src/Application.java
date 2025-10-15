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
import java.time.LocalDateTime;

//Importing the main class util scanner.
//Using the file scanner
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
        //Prompting the user to input the data.
        System.out.print("Enter your 7-digit Employee ID: ");
        String idInput = scanner.nextLine().trim();
        //Erro message if it's invalid
        if (!idInput.matches("\\d{7}")) {
            System.out.println("Invalid ID format. Must be a 7-digit number.");
            return;
        }
         //Parse employee Id for an integer.
        int employeeId = Integer.parseInt(idInput);

// ✅ Validate login
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
                System.out.println("1. Load Logs from File");//Insert file
                System.out.println("2. Display Log");//Display the audit log listing
                System.out.println("3. Remove Data");//Remove data from log audit log
                System.out.println("4. Audit log");//Access the audit log
                System.out.println("5. Employee records");
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
                case "3": // Host adds reservation; Manager removes employee
                    //With the addition method of keeping track of the removing and adding employee records.
                    if (role.equalsIgnoreCase("Host")) {
                        manager.addRecord(scanner);
                    } else if (role.equalsIgnoreCase("Manager")) {
                        System.out.print("Enter your Authorized ID: ");
                        String authIdInput = scanner.nextLine().trim();
                        try { //Adds on.
                            int authId = Integer.parseInt(authIdInput);
                            if (manager.validUsers.containsKey(authId) &&
                                    manager.validUsers.get(authId).equalsIgnoreCase("Manager")) {
                                //Method to remove the record.
                                System.out.print("Enter Employee ID to remove: ");
                                int employeeToRemove = Integer.parseInt(scanner.nextLine().trim());
                                manager.removeEmployeeRecord(employeeToRemove);
                                manager.logAuditAction(authId, "Manager", "REMOVE_EMPLOYEE", LocalDateTime.now(),
                                        String.valueOf(employeeToRemove), "Removed employee record");

                              //Gives the error message if the user input the ID number incorrectly.
                            } else {
                                System.out.println("Authorization Failed");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format. Must be a 7-digit number.");
                        }
                    } else {
                        System.out.println("Access Denied");
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
                case "5": // Host updates reservation; Manager manages employee records
                    if (role.equalsIgnoreCase("Host")) {
                        System.out.print("Enter Reserved ID to update: ");
                        try {
                            int reservationId = Integer.parseInt(scanner.nextLine().trim());
                            manager.updateRecord(scanner, reservationId);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid reservation ID format.");
                        } //Prompting the manager to input their ID number to access the portal.
                    } else if (role.equalsIgnoreCase("Manager")) {
                        System.out.print("Enter your Authorized ID: ");
                        String authIdInput = scanner.nextLine().trim();
                        try {
                            int authId = Integer.parseInt(authIdInput);
                            if (manager.validUsers.containsKey(authId) &&
                                    manager.validUsers.get(authId).equalsIgnoreCase("Manager")) {
                                //Manager option 5 menu.
                                System.out.println("******Records Menu******");
                                System.out.println("1. Display Employee Records");
                                System.out.println("2. Add Employee");
                                System.out.println("3. Remove Employee");
                                System.out.println("4. Main Menu");

                                String empAction = scanner.nextLine().trim();

                                switch (empAction) { //Case to display employee records.
                                    case "1" -> manager.displayEmployeeRecords();

                                    case "2" -> { //Case to add employee record.
                                        manager.addEmployeeRecord(scanner);
                                    }
                                    case "3" -> { //Case to remove employee record.
                                        System.out.print("Enter Employee ID to remove: ");
                                        int empId = Integer.parseInt(scanner.nextLine().trim());
                                        manager.removeAuditLog(empId);
                                    }
                                    //Case to return back to the main menu.
                                    case "4" -> {
                                        System.out.println("Loading...");
                                        // Add logic here if needed
                                    } //Notify user in invalid entry has been entered.
                                    default -> System.out.println("Invalid option.");
                                }


                        } else { //Additional error message is code has failed.
                                System.out.println("Authorization Failed");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format..");
                        }
                    } else {
                        System.out.println("Access Denied");
                    }
                    break; //Positive message.
                case "6": //Case allowing the users to log out successfully.
                    System.out.println("Successfully logged out");
                    return;
                default: //Handling invalid entries.
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
