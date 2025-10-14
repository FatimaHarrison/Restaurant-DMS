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
    private final List<EmployeeRecord> employeeRecords = new ArrayList<>();
    public final Map<Integer, String> validUsers = new HashMap<>();

    // Load both reservation records and audit logs from a file
    public void loadFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File is not found.");
            return;
        }
        //Buffer reader to read each file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            records.clear();
            auditLogs.clear();
            employeeRecords.clear();
            //Inserting a string line to ensure files have the correct formatting.
            String line; //The one below is for records
            DateTimeFormatter auditFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");
            DateTimeFormatter hireFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");//Employee records file
            //Ensuring the separator is in each line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                //Given the list of array to determine user access.
                if (parts.length == 8) { // Reservation Record
                    try {
                        Integer id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        Integer partySize = Integer.parseInt(parts[2].trim());
                        String email = parts[3].trim();
                        String phone = parts[4].trim();
                        String date = parts[5].trim();
                        String time = parts[6].trim();
                        String notes = parts[7].trim();
                        //Setting claas attributes into array list
                        records.add(new Record(id, name, email, phone, partySize, date, time, notes));
                    } catch (Exception e) { //Error message
                        System.out.println("Invalid reservation record: " + line);
                    }
                    //Given an additional file with identifiers.
                } else if (parts.length == 6 && parts[2].equalsIgnoreCase("Server") || parts[2].equalsIgnoreCase("Supervisor") || parts[2].equalsIgnoreCase("Busser") || parts[2].equalsIgnoreCase("Host")) {
                    // Employee Record
                    try {
                        Integer employId = Integer.parseInt(parts[0].trim());
                        String employName = parts[1].trim();
                        String employGen = parts[2].trim();
                        String employRole = parts[3].trim();
                        String employStat = parts[4].trim();
                        LocalDate hireDate = LocalDate.parse(parts[5].trim(), hireFormatter);
                        //Given class attributes to array list
                        employeeRecords.add(new EmployeeRecord(employId, employName, employGen, employRole, employStat, hireDate));
                        validUsers.put(employId, employRole.toUpperCase());
                    } catch (Exception e) { //Eroor message
                        System.out.println("Invalid employee record: " + line);
                    }
                    //Given Additional file
                } else if (parts.length == 6) {
                    // Audit Log
                    try {
                        Integer employId = Integer.parseInt(parts[0].trim());
                        String role = parts[1].trim();
                        String action = parts[2].trim();
                        LocalDateTime timestamp = LocalDateTime.parse(parts[3].trim(), auditFormatter);
                        String name = parts[4].trim();
                        String notes = parts[5].trim();
                        //Assigned class attributes to array list
                        auditLogs.add(new AuditLog(employId, role, action, timestamp, name, notes));
                        validUsers.put(employId, role.toUpperCase());
                    } catch (Exception e) { //Error message
                        System.out.println("Invalid audit log entry: " + line);
                    }

                } else { //Given message is line format is not matched.
                    System.out.println("Unrecognized line format: " + line);
                }
            }
            //Message once data is loaded successfully.
            System.out.println("Data loaded successfully.");

        } catch (IOException e) { //Message is a file is not able to read.
            System.out.println("Error reading file.");
        }
    }

    //To display the records file.
    public void displayRecords() {
        if (records.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }
        records.forEach(System.out::println);
    }

    //To display the audit log file
    public void displayAuditLogs() {
        if (auditLogs.isEmpty()) {
            System.out.println("No audit logs to display.");
            return;
        }
//CLI display setup.
        for (AuditLog log : auditLogs) {
            System.out.printf("EmployeeID: %d | Role: %s | Action: %s | Time: %s | Name: %s | Notes: %s\n",
                    log.getEmployId(), log.getRole(), log.getActionType(),
                    log.getTimestamp(), log.getEmployName(), log.getNotes());
        }
    }

    //Method to add a new record.
    public void addRecord(Scanner scanner) {
        try { //Prompting the user to insert information
            System.out.print("Enter Reservation ID: ");
            Integer id = Integer.parseInt(scanner.nextLine().trim());
            //Restricts the host to not duplicate a reserved ID number.
            boolean exists = records.stream().anyMatch(r -> r.getId().equals(id));
            if (exists) {
                System.out.println("Error: A reservation with this ID already exists.");
                return;
            }
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
            //Method to store in each input, otherwise receive a error message.
            records.add(new Record(id, name, email, phone, partysize, date, time, notes));
            System.out.println("Record added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Remove method by using the reservation and employee ID
    public void removeRecord(Integer id) {
        boolean removed = records.removeIf(r -> r.getId().equals(id));
        System.out.println(removed ? "Record removed." : "Record not found.");
    }

    public void removeAuditLog(Integer employId) {
        boolean removed = auditLogs.removeIf(log -> log.getEmployId().equals(employId));
        System.out.println(removed ? "Audit log entry removed." : "Audit log entry not found.");
    }

    //Update methods on reservation
    public void updateRecord(Scanner scanner, Integer id) {
        for (Record r : records) {
            if (r.getId().equals(id)) {
                System.out.print("Field to update: ");
                String field = scanner.nextLine().trim().toLowerCase();

                System.out.print("New value: ");
                String value = scanner.nextLine().trim();

                try { //Switch cases to update once user selected it on menu option.
                    switch (field) {
                        case "name":
                            r.setName(value);
                            break;
                        case "email":
                            r.setEmail(value);
                            break;
                        case "phone number":
                            r.setPhoneNumber(value);
                            break;
                        case "date":
                            r.setDate(value);
                            break;
                        case "time":
                            r.setTime(value);
                            break;
                        case "notes":
                            r.setNotes(value);
                            break;
                        default:
                            System.out.println("Invalid field.");
                            return;
                    } //Notify the user.
                    System.out.println("Record updated.");
                } catch (Exception e) {
                    System.out.println("Update failed: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Record not found.");
    }

    //Method to get the reports of restaurant employees.
    public void generateAuditReport(Scanner scanner, String currentUserRole) {
        if (!currentUserRole.equalsIgnoreCase("manager") && !currentUserRole.equalsIgnoreCase("audit")) {
            System.out.println("Access denied authorized usage only");
            return;
        }
//Prompting the manager to insert the date.
        System.out.print("Enter start date (YYYY/MM/DD): ");
        String startInput = scanner.nextLine().trim();

        System.out.print("Enter end date (YYYY/MM/DD): ");
        String endInput = scanner.nextLine().trim();
//12-hour date time formatting.
        LocalDateTime startDate, endDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate startLocal = LocalDate.parse(startInput, formatter);
            LocalDate endLocal = LocalDate.parse(endInput, formatter);
            startDate = startLocal.atTime(11, 0); //Between 11:00am - 11:59pm
            endDate = endLocal.atTime(11, 59);
        } catch (DateTimeParseException e) { //Error message
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
                        log.getEmployId().toString().toLowerCase().contains(keyword) ||// Keyword for employee id
                        log.getEmployName().toLowerCase().contains(keyword) || //keyword EMPLOYEE NAME
                        log.getNotes().toLowerCase().contains(keyword))//Keyword for employee notes.
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
        //Prompts out how many entries the employee has.
        System.out.println("Report generated. Total entries: " + filtered.size());
    }

    //Method to add in a new employee record.
    public void addEmployeeRecord(Scanner scanner) {
        try { //Prompting the user to insert the data along with exceptions for errors.
            System.out.print("Enter New Employee ID: "); //Prompting for new employee ID
            Integer employId = Integer.parseInt(scanner.nextLine().trim());

            boolean exists = employeeRecords.stream().anyMatch(e -> e.getEmployID() == employId);
            if (exists) { //Gives an error message in case it's a duplicate.
                System.out.println("Error: Employee record already exists.");
                return;
            }
            //Other prompts for the user to input.
            System.out.print("Enter Employee Full Name: ");
            String employName = scanner.nextLine().trim();

            System.out.print("Enter Gender: ");
            String employGen = scanner.nextLine().trim();

            System.out.print("Enter Employee Role: ");
            String employRole = scanner.nextLine().trim();

            System.out.print("Enter Status (FullTime/PartTime): ");
            String employStat = scanner.nextLine().trim();

            System.out.print("Enter Hire Date (YYYY/MM/DD): ");
            LocalDate hireDate = LocalDate.parse(scanner.nextLine().trim());

            // Add to employeeRecords list using the clas attributes.
            //using the users validation for authorized managers usage.
            employeeRecords.add(new EmployeeRecord(employId, employName, employGen, employRole, employStat, hireDate));
            validUsers.put(employId, employRole.toUpperCase()); // Optional: add to login map
            System.out.println("Employee record added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Method to remove employee record.
    public void removeEmployeeRecord(Integer employId) {
        boolean removed = employeeRecords.removeIf(e -> e.getEmployID() == employId);
        System.out.println(removed ? "Employee record removed." : "Employee record not found.");
    }
    //To keep track og =f the mangers removing or adding a employee record.
    public void logAuditAction(int actorId, String role, String actionType, LocalDateTime timestamp, String targetId, String notes) {
            auditLogs.add(new AuditLog(actorId, role, actionType, timestamp, targetId, notes));
        }

    }



