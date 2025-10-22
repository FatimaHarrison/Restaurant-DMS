import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class EmployeeGUI extends JFrame {
    private JList list1;
    private JTextField optionField2;
    private JButton button1;
    private JPanel EmployeeMenu;
    private File selectedFile;
    private List<String> employeeLines;

    public EmployeeGUI() {
        setTitle("Employee Records"); //Setting title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Stop code when exited.
        setContentPane(EmployeeMenu); //Jpanel name
        setSize(500, 600); //Setting the default size.
        setVisible(true);//Is able to see.
        setLocationRelativeTo(null); //Pop-ups on middle of screen.

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = optionField2.getText().trim();
                try { //Switch cases for user options on list1.
                    int selection = Integer.parseInt(input); //Input must be a number
                    switch (selection) {
                        case 1: //Displaying the file
                            JOptionPane.showMessageDialog(null, "Display Employee Records");
                            if (selectedFile != null) { //If the selected file is true show the content.
                                try (java.util.Scanner scanner = new java.util.Scanner(selectedFile)) {
                                    StringBuilder content = new StringBuilder(); //Gets the file display.
                                    while (scanner.hasNextLine()) { //File format.
                                        content.append(scanner.nextLine()).append("\n"); //Add on new line.
                                    } //Gives the user the message the file is being loaded.
                                    JOptionPane.showMessageDialog(null, "File Content:\n" + content.toString());
                                } catch (Exception ex) { //Gives the error message if it cant be read.
                                    JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
                                }
                            } else { //Prompts message if the file has not been successfully loaded.
                                JOptionPane.showMessageDialog(null, "No file loaded.");
                            }
                            break;
                        case 2: // Add new record
                            JOptionPane.showMessageDialog(null, "Add New Employee Record");

                            if (selectedFile == null) {
                                JOptionPane.showMessageDialog(null, "No file loaded. Please select option 1 first.");
                                break;
                            }
                            try {
                                String idInput = JOptionPane.showInputDialog("Enter New Employee ID (7 digits):");
                                if (idInput == null || !idInput.matches("\\d{7}")) {
                                    JOptionPane.showMessageDialog(null, "Error: Employee ID must be exactly 7 digits.");
                                    break;
                                }
                                Integer employId = Integer.parseInt(idInput);
                                boolean exists = employeeRecords.stream().anyMatch(e -> e.getEmployID() == employId);
                                if (exists) {
                                    JOptionPane.showMessageDialog(null, "Error: Employee record already exists.");
                                    break;
                                }

                                String employName = JOptionPane.showInputDialog("Enter Employee Full Name:");
                                if (employName == null || employName.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Full Name is required.");
                                    break;
                                }

                                String employGen = JOptionPane.showInputDialog("Enter Gender:");
                                if (employGen == null || employGen.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Gender is required.");
                                    break;
                                }

                                String employRole = JOptionPane.showInputDialog("Enter Employee Role:");
                                if (employRole == null || employRole.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Role is required.");
                                    break;
                                }

                                String employStat = JOptionPane.showInputDialog("Enter Availability (FullTime or PartTime):");
                                if (employStat == null || employStat.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Availability is required.");
                                    break;
                                }
                                employStat = employStat.trim().toUpperCase();
                                if (!employStat.equals("FULLTIME") && !employStat.equals("PARTTIME")) {
                                    JOptionPane.showMessageDialog(null, "Error: Availability must be 'FullTime' or 'PartTime'.");
                                    break;
                                }

                                String dateInput = JOptionPane.showInputDialog("Enter Hire Date (yyyy/MM/dd):");
                                LocalDate hireDate;
                                try {
                                    DateTimeFormatter hireFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                    hireDate = LocalDate.parse(dateInput.trim(), hireFormatter);
                                } catch (DateTimeParseException e) {
                                    JOptionPane.showMessageDialog(null, "Error: Invalid date format. Please use yyyy/MM/dd.");
                                    break;
                                }
                                // Add to employeeRecords list
                                employeeRecords.add(new EmployeeRecord(employId, employName.trim(), employGen.trim(), employRole.trim(), employStat, hireDate));
                                validUsers.put(employId, employRole.trim().toUpperCase());

                                // Write to file
                                try (PrintWriter writer = new PrintWriter(selectedFile)) {
                                    for (EmployeeRecord record : employeeRecords) {
                                        writer.println(record.toFileString()); // Ensure EmployeeRecord has a toFileString() method
                                    }
                                    JOptionPane.showMessageDialog(null, "Employee record added successfully.");
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage());
                                }

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Unexpected error: " + ex.getMessage());
                            }
                            break;

                        case 3:
                            if (selectedFile == null || employeeLines.isEmpty()) { //Message if a file has not been successfully loaded.
                                JOptionPane.showMessageDialog(null, "No file loaded.");
                                break;
                            }

                            //Prompting user to input Employee ID number
                            String idToDelete = JOptionPane.showInputDialog("Enter Employee ID to delete:");
                            if (idToDelete == null || idToDelete.trim().isEmpty()) { //Message if input was left empty.
                                JOptionPane.showMessageDialog(null, "No ID entered.");
                                break;
                            }

                            //If the ID was not found.
                            boolean found = false;
                            java.util.List<String> updatedLines = new java.util.ArrayList<>();
                            for (String line : employeeLines) {
                                if (line.contains(idToDelete)) { //If the ID is found.
                                    found = true;
                                    continue; // skip this line to delete it
                                }
                                updatedLines.add(line); //Updates the line
                            }

                            if (found) { //Verifies the ID is found.
                                try (java.io.PrintWriter writer = new java.io.PrintWriter(selectedFile)) { //The selected file.
                                    for (String line : updatedLines) { //Display the lines
                                        writer.println(line);
                                    }
                                    employeeLines = updatedLines; //Successfully updates the line that was deleted.
                                    //Gives the user a message.
                                    JOptionPane.showMessageDialog(null, "Employee with ID " + idToDelete + " deleted.");
                                } catch (Exception ex) { //Notify the user if file can not be updated.
                                    JOptionPane.showMessageDialog(null, "Error updating file: " + ex.getMessage());
                                }
                            } else {
                                //Notify if employee ID is not found.
                                JOptionPane.showMessageDialog(null, "Employee ID not found.");
                            }
                            break;

                        case 4:
                            JOptionPane.showMessageDialog(null, "***Main Menu***");
                            break;
                            default:
                            JOptionPane.showMessageDialog(null, "Invalid selection. Please enter between 1 and 4.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
            }
        });
    }

    public static void main(String[] args) {
        new EmployeeGUI(); // Correct class name
    }
}
