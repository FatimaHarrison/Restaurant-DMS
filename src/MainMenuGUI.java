import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

//Declaring main file class
public class MainMenuGUI extends JFrame {
    //Declaring class attributes
    private JPanel HostMenu;
    private JTextField optionField;
    private JButton btnEnter;
    private JLabel optionLabel;
    private JList list1;
    private File selectedFile;
    private List<String> reservationLines;

    //Setters for the main menu
    public MainMenuGUI() {
        setTitle("Terralina Host Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Stops the run when exiting.
        setSize(500, 600); //Setting the default size.
        setLocationRelativeTo(null); //Pop-ups on middle of screen.
        setContentPane(HostMenu); //Gets the content of the GUI menu.
        setVisible(true);//Is able to see.

        //Action button once entry.
        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //Action button method to submit.
                String input = optionField.getText().trim(); //Text user input on GUI
                try { //Switch cases for user options on list1.
                    int selection = Integer.parseInt(input); //Input must be a number
                    switch (selection) {
                        case 1: //The host can load a file.
                            JOptionPane.showMessageDialog(null, "Load the file");
                            JFileChooser fileChooser = new JFileChooser(); //Opens cpu file dialog.
                            int result = fileChooser.showOpenDialog(null); //Able tp se;ect a file.
                            if (result == JFileChooser.APPROVE_OPTION) { //Verifies for accepted files.
                                selectedFile = fileChooser.getSelectedFile(); //Get the selected file.
                                JOptionPane.showMessageDialog(null, "File loaded: " + selectedFile.getAbsolutePath());
                                //Gives the user the method file has successfully loaded.
                                break;
                            }
                            case 2: //Allows the user to display the file.
                            JOptionPane.showMessageDialog(null, "Displaying File...");
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
                                JOptionPane.showMessageDialog(null, "No file loaded. Please select option 1.");
                            }
                            break;
                        case 3: //Allows the host to delete reservation
                            JOptionPane.showMessageDialog(null, "Cancel a Reservation"); //Notification
                            String idToDelete = JOptionPane.showInputDialog("Enter Reservation ID to delete:"); //Prompting user to input ID number
                            if (idToDelete == null || idToDelete.trim().isEmpty()) { //Message if input was left empty.
                                JOptionPane.showMessageDialog(null, "No ID entered.");
                            if (selectedFile == null || reservationLines.isEmpty()) {//Message if a file has not been successfully loaded.
                                JOptionPane.showMessageDialog(null, "No file loaded. Please select option 1.");
                            }
                            break;
                            }
                            //If the ID was not found.
                            boolean found = false; //
                            java.util.List<String> updatedLines = new java.util.ArrayList<>();
                            for (String line : reservationLines) {
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
                                    reservationLines = updatedLines; //Successfully updates the line that was deleted.
                                    //Gives the user a message.
                                    JOptionPane.showMessageDialog(null, "Reservation with ID " + idToDelete + " deleted.");
                                } catch (Exception ex) { //Notify the user if file can not be updated.
                                    JOptionPane.showMessageDialog(null, "Error updating file: " + ex.getMessage());
                                }
                            } else {
                                //Notify if reservation ID is not found.
                                JOptionPane.showMessageDialog(null, "Reservation ID not found.");
                            }
                            break;
                        case 4:
                            if (selectedFile == null || reservationLines.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "No file loaded. Please select option 1 first.");
                                break;
                            }

                            String idToUpdate = JOptionPane.showInputDialog("Enter Reservation ID to update:");
                            if (idToUpdate == null || idToUpdate.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "No ID entered.");
                                break;
                            }

                            found = false;
                            for (int i = 0; i < reservationLines.size(); i++) {
                                String[] parts = reservationLines.get(i).split("\\|");
                                if (parts.length >= 8 && parts[0].equals(idToUpdate)) {
                                    found = true;

                                    String[] fields = {"Name", "Email", "Phone", "PartySize", "Date", "Time", "Notes"};
                                    for (int j = 1; j < fields.length + 1; j++) {
                                        String newValue = JOptionPane.showInputDialog("Update " + fields[j - 1] + " (current: " + parts[j] + "):");
                                        if (newValue != null && !newValue.trim().isEmpty()) {
                                            parts[j] = newValue.trim();
                                        }
                                    }

                                    reservationLines.set(i, String.join("|", parts));
                                    break;
                                }
                            }

                            if (found) {
                                try (PrintWriter writer = new PrintWriter(selectedFile)) {
                                    for (String line : reservationLines) {
                                        writer.println(line);
                                    }
                                    JOptionPane.showMessageDialog(null, "Reservation updated successfully.");
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage());
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Reservation ID not found.");
                            }
                            break;

                        case 5:
                            if (selectedFile == null) {
                                JOptionPane.showMessageDialog(null, "No file loaded. Please select option 1 first.");
                                break;
                            }

                            String[] fields = {"ID", "Name", "Email", "Phone", "PartySize", "Date", "Time", "Notes"};
                            String[] values = new String[fields.length];

                            for (int i = 0; i < fields.length; i++) {
                                input = JOptionPane.showInputDialog("Enter " + fields[i] + ":");
                                if (input == null || input.trim().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, fields[i] + " is required.");
                                    break;
                                }
                                values[i] = input.trim();
                            }

                            String newReservation = String.join("-", values);
                            reservationLines.add(newReservation);

                            try (PrintWriter writer = new PrintWriter(selectedFile)) {
                                for (String line : reservationLines) {
                                    writer.println(line);
                                }
                                JOptionPane.showMessageDialog(null, "Reservation created successfully.");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage());
                            }
                            break;

                        case 6:
                            JOptionPane.showMessageDialog(null, "Exiting Host Menu.");
                            dispose();
                            break;
                            case 7:
                            JOptionPane.showMessageDialog(null, "Help: Enter a number between 1 and 6 to select an option.");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid selection.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
            }
        });

    }
    public static void main(String[] args) {
        new MainMenuGUI();
    }
}
