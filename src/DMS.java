import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//DMS functional class
public class DMS {
    //Declaring my class attributes
    private final List<Record> records = new ArrayList<>();
    private final List<AuditLog> auditLogs = new ArrayList<>();

    // Load both reservation records and audit logs from a file
    public void loadFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) { //If the file does not exist, the user will be notified.
            System.out.println("File is not found.");
            return; //Return home
        }
        //Method for the bufferreader to scanned through the files.
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            records.clear();
            auditLogs.clear();
            //Method for the user to input the date and time in a specific format.
            String line; //The date and time are considered as strings.
            DateTimeFormatter auditFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");
            //A while loop user as a seperator.
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                //Each file has their differences in length
                //Also restricts a user based of their role to open the specific file.
                if (parts.length == 8) {
                    try {
                        Integer id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        Integer partySize = Integer.parseInt(parts[2].trim());
                        String email = parts[3].trim();
                        String phone = parts[4].trim();
                        String date = parts[5].trim();
                        String time = parts[6].trim();
                        String notes = parts[7].trim();

                        records.add(new Record(id, name, email, phone, partySize, date, time, notes));
                    } catch (Exception e) {
                        System.out.println("Invalid reservation record: " + line);
                    }

                    //The length of the data file.
                } else if (parts.length == 6) {
                    // Audit log entry
                    try { //Number of arrays
                        Integer employId = Integer.parseInt(parts[0].trim());
                        String role = parts[1].trim();
                        String action = parts[2].trim();
                        LocalDateTime timestamp = LocalDateTime.parse(parts[3].trim(), auditFormatter);
                        String name = parts[4].trim();
                        String notes = parts[5].trim();

                        auditLogs.add(new AuditLog(employId, role, action, timestamp, name, notes));
                    } catch (Exception e) { //Catches if there's an error.
                        System.out.println("Invalid audit log entry: " + line);
                    }

                } else { //Other catches if it's been inputted incorrectly.
                    System.out.println("Unrecognized line format: " + line);
                }
            }

            System.out.println("Data loaded successfully."); //System to log out once selected on menu option.

        } catch (IOException e) { //Error message if the system can;t read the file.
            System.out.println("Error reading file.");
        }
    }

    public void displayRecords() {
        if (records.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }
        records.forEach(System.out::println);
    }

    public void displayAuditLogs() {
        if (auditLogs.isEmpty()) {
            System.out.println("No audit logs to display.");
            return;
        }

        for (AuditLog log : auditLogs) {
            System.out.printf("EmployeeID: %d | Role: %s | Action: %s | Time: %s | Name: %s | Notes: %s\n",
                    log.getEmployId(), log.getRole(), log.getActionType(),
                    log.getTimestamp(), log.getEmployName(), log.getNotes());
        }
    }
//Method to add a new reservation
    public void addRecord(Scanner scanner) {
        try { //Prompting the user to insert information
            System.out.print("Enter ID (7-digit number): ");
            Integer id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Name (max 25 chars): ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter partySize: ");
            Integer partysize = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter Phone Number (10 digits): ");
            String phone = scanner.nextLine().trim();

            System.out.print("Enter Date: ");
            String date = scanner.nextLine().trim();

            System.out.print("Enter Time: ");
            String time = scanner.nextLine().trim();

            System.out.print("Enter Notes: ");
            String notes = scanner.nextLine().trim();

            records.add(new Record(id, name, email, phone, partysize, date, time, notes));
            System.out.println("Record added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void removeRecord(Integer id) {
        boolean removed = records.removeIf(r -> r.getId().equals(id));
        System.out.println(removed ? "Record removed." : "Record not found.");
    }

    public void removeAuditLog(Integer employId) {
        boolean removed = auditLogs.removeIf(log -> log.getEmployId().equals(employId));
        System.out.println(removed ? "Audit log entry removed." : "Audit log entry not found.");
    }

    public void updateRecord(Scanner scanner, Integer id) {
        for (Record r : records) {
            if (r.getId().equals(id)) {
                System.out.print("Field to update: ");
                String field = scanner.nextLine().trim().toLowerCase();

                System.out.print("New value: ");
                String value = scanner.nextLine().trim();

                try {
                    switch (field) {
                        case "name": r.setName(value); break;
                        case "email": r.setEmail(value); break;
                        case "phone number": r.setPhoneNumber(value); break;
                        case "date": r.setDate(value); break;
                        case "time": r.setTime(value); break;
                        case "notes": r.setNotes(value); break;
                        default:
                            System.out.println("Invalid field.");
                            return;
                    }
                    System.out.println("Record updated.");
                } catch (Exception e) {
                    System.out.println("Update failed: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Record not found.");
    }

    public void generateAuditReport(Scanner scanner, String currentUserRole) {
        if (!currentUserRole.equalsIgnoreCase("manager") && !currentUserRole.equalsIgnoreCase("audit")) {
            System.out.println("Access denied authorized usage only");
            return;
        }

        System.out.print("Enter start date (YYYY/MM/DD): ");
        String startInput = scanner.nextLine().trim();

        System.out.print("Enter end date (YYYY/MM/DD): ");
        String endInput = scanner.nextLine().trim();

        LocalDateTime startDate, endDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate startLocal = LocalDate.parse(startInput, formatter);
            LocalDate endLocal = LocalDate.parse(endInput, formatter);
            startDate = startLocal.atTime(11, 0);
            endDate = endLocal.atTime(11, 59);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid entry. Try again");
            return;
        }
        // 30-day restriction check
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        //The before and after date must range in the 30day range.
        if (startDate.isBefore(thirtyDaysAgo) || endDate.isAfter(now)) {
            System.out.println("Error: Date range must be within the last 30 days.");
            return;
        } //Continues prompting the user to insert information.
        System.out.print("Enter employee role: ");
        String roleFilter = scanner.nextLine().trim().toLowerCase();

        System.out.print("Enter employee ID number: ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        //List of filtering
        List<AuditLog> filtered = auditLogs.stream()
                .filter(log -> !log.getTimestamp().isBefore(startDate) && !log.getTimestamp().isAfter(endDate))
                .filter(log -> roleFilter.equals("all") || log.getRole().equalsIgnoreCase(roleFilter)) //Insert user role
                .filter(log -> keyword.isEmpty() || //Keyword
                        log.getEmployId().toString().toLowerCase().contains(keyword) ||
                        log.getEmployName().toLowerCase().contains(keyword) ||
                        log.getNotes().toLowerCase().contains(keyword))
                .sorted(Comparator.comparing(AuditLog::getTimestamp))
                .limit(10000)
                .toList();
        System.out.println("\n--- Audit Report ---");
        for (AuditLog log : filtered) {
            String maskedNotes = currentUserRole.equalsIgnoreCase("audit") ? log.getNotes() : "***";
            System.out.printf("EmployeeID: %d | Role: %s | Action: %s | Time: %s | Employee Name: %s | Reports: %s\n",
                    log.getEmployId(), log.getRole(), log.getActionType(),
                    log.getTimestamp(), log.getEmployName(), maskedNotes);
        }

        System.out.println("Report generated. Total entries: " + filtered.size());
    }
}
