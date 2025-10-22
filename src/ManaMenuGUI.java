import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ManaMenuGUI extends JFrame {
    private JList<String> list2;
    private JTextField optionField2;
    private JButton btnEnter;
    private JPanel ManaMenu;
    private File selectedFile;

    public ManaMenuGUI() {
        setTitle("Terralina Management Menu");
       setContentPane(ManaMenu);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Stops the run when exiting.
        setSize(500, 600); //Setting the default size.
        setLocationRelativeTo(null); //Pop-ups on middle of screen.
        setVisible(true); //Is able to see.

        // Button logic
        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = optionField2.getText().trim(); //Text user input on GUI
                try { //Switch cases for user options on list2.
                    int selection = Integer.parseInt(input); //Input must be a number

                    switch (selection) {
                        case 1: // Load Logs from file
                            JOptionPane.showMessageDialog(null, "**** Load Logs from file...");
                            JFileChooser fileChooser = new JFileChooser();
                            int result = fileChooser.showOpenDialog(null);
                            if (result == JFileChooser.APPROVE_OPTION) {
                                selectedFile = fileChooser.getSelectedFile();
                                JOptionPane.showMessageDialog(null, "File loaded: " + selectedFile.getAbsolutePath());
                            } else {
                                JOptionPane.showMessageDialog(null, "No file selected.");
                            }
                            break;

                        case 2: // Display Log
                            JOptionPane.showMessageDialog(null, "**** Displaying Log...");
                            if (selectedFile != null) {
                                try (java.util.Scanner scanner = new java.util.Scanner(selectedFile)) {
                                    StringBuilder content = new StringBuilder();
                                    while (scanner.hasNextLine()) {
                                        content.append(scanner.nextLine()).append("\n");
                                    }
                                    JOptionPane.showMessageDialog(null, "Log Content:\n" + content.toString());
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No file loaded. Please select option 1.");
                            }
                            break;

                        case 3: // Remove Data
                            JOptionPane.showMessageDialog(null, "**** Removing Data...");
                            if (selectedFile != null && selectedFile.exists()) {
                                boolean deleted = selectedFile.delete();
                                if (deleted) {
                                    JOptionPane.showMessageDialog(null, "File deleted successfully.");
                                    selectedFile = null;
                                } else {
                                    JOptionPane.showMessageDialog(null, "Failed to delete the file.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No file loaded or file does not exist.");
                            }
                            break;

                        case 4: // Audit Log
                            JOptionPane.showMessageDialog(null, "**** Auditing Log...");
                            if (selectedFile != null) {
                                try (java.util.Scanner scanner = new java.util.Scanner(selectedFile)) {
                                    int lineCount = 0;
                                    while (scanner.hasNextLine()) {
                                        scanner.nextLine();
                                        lineCount++;
                                    }
                                    JOptionPane.showMessageDialog(null, "Audit Complete: " + lineCount + " entries found.");
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "Error auditing file: " + ex.getMessage());
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No file loaded. Please select option 1.");
                            }
                            break;

                        case 5: // Employee Records
                            JOptionPane.showMessageDialog(null, "**** Viewing Employee Records...");
                            // Placeholder for actual logic — you can connect this to your Terralina employee module
                            JOptionPane.showMessageDialog(null, "Feature under development.");
                            break;

                        case 6: // Exit
                            JOptionPane.showMessageDialog(null, "Exiting Manager Menu.");
                            System.exit(0);
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Invalid selection. Please enter a number between 1 and 6.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
            }
        });
    }

    public static void main(String[] args) {
        new ManaMenuGUI(); // Launches the correct GUI
    }
}
