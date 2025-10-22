import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoginGUI extends JFrame {
    private JPanel UserLogin; //Panel name
    private JButton btnSubmit; //Submit action button
    private JTextField employRole; //Prompt Username
    private JPasswordField employID; //Prompt Password
    private File selectedFile; //File reference for login validation

    public LoginGUI() {
        setTitle("Terralina Employee Login"); //Setting a direct title.
        setContentPane(UserLogin); //Setting frame content
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Stop run on close.
        setSize(350, 300); //Setting size of form.
        setLocationRelativeTo(null); //Center window
        setVisible(true); //Setting visibility.

        // ✅ Load the employee data file directly from project directory
        selectedFile = new File("Textual/Data"); //Get the selected file

        if (!selectedFile.exists()) { //Prompts message if the file has not been successfully loaded.
            JOptionPane.showMessageDialog(null, "Data file not found. Please check the path.");
            return;
        }

        // Preview file content
        try (java.util.Scanner scanner = new java.util.Scanner(selectedFile)) {
            StringBuilder content = new StringBuilder(); //Gets the file display.
            while (scanner.hasNextLine()) { //File format.
                content.append(scanner.nextLine()).append("\n"); //Add on new line.
            }
            //Gives the user the message the file is being loaded.
            JOptionPane.showMessageDialog(null, "File Content:\n" + content.toString());
        } catch (Exception ex) { //Gives the error message if it can't be read.
            JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
        }

        // Submit button logic
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String role = employRole.getText().trim(); //Getting user input username
                String idInput = new String(employID.getPassword()).trim(); //Getting user input password

                //Role must be Host or Manager
                if (!role.equalsIgnoreCase("Host") && !role.equalsIgnoreCase("Manager")) {
                    JOptionPane.showMessageDialog(null, "Access Denied");
                    return;
                }

                //ID must be a 7-digit number
                if (!idInput.matches("\\d{7}")) {
                    JOptionPane.showMessageDialog(null, "Invalid ID format.");
                    return;
                }

                int employeeId = Integer.parseInt(idInput);
                boolean validLogin = false;

                // Check file for matching ID and role
                try (java.util.Scanner scanner = new java.util.Scanner(selectedFile)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        // Example format: 1234567-Host
                        if (line.matches("\\d{7}-\\w+")) {
                            String[] parts = line.split("-");
                            int fileId = Integer.parseInt(parts[0]);
                            String fileRole = parts[1];

                            if (fileId == employeeId && fileRole.equalsIgnoreCase(role)) {
                                validLogin = true;
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error validating login: " + ex.getMessage());
                    return;
                }

                // Declare at class level
                private Map<Integer, String> validUsers = new HashMap<>();

// ✅ Load users into the map (inside constructor, after file check)
                try (java.util.Scanner scanner = new java.util.Scanner(selectedFile)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        // Expected format: 1234567 Host
                        if (line.matches("\\d{7}\\s+\\w+")) {
                            String[] parts = line.split("\\s+");
                            int id = Integer.parseInt(parts[0]);
                            String userRole = parts[1];
                            validUsers.put(id, userRole);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error loading user data: " + ex.getMessage());
                    dispose(); // Close login window
                    return;
                }

// ✅ Inside btnSubmit.addActionListener
                btnSubmit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String role = employRole.getText().trim(); // Getting user input username
                        String idInput = new String(employID.getPassword()).trim(); // Getting user input password

                        // Role must be Host or Manager
                        if (!role.equalsIgnoreCase("Host") && !role.equalsIgnoreCase("Manager")) {
                            JOptionPane.showMessageDialog(null, "Access Denied: Role must be Host or Manager.");
                            return;
                        }

                        // ID must be a 7-digit number
                        if (!idInput.matches("\\d{7}")) {
                            JOptionPane.showMessageDialog(null, "Invalid ID format. Must be a 7-digit number.");
                            return;
                        }

                        int employeeId = Integer.parseInt(idInput);

                        // ✅ Validate login using map
                        if (!validUsers.containsKey(employeeId) ||
                                !validUsers.get(employeeId).equalsIgnoreCase(role)) {
                            JOptionPane.showMessageDialog(null, "Access Denied: ID and Role do not match system records.");
                            return;
                        }

                        // ✅ Login successful
                        JOptionPane.showMessageDialog(null, "Logged in as: " + role);
                        if (role.equalsIgnoreCase("Host")) {
                            new MainMenuGUI();
                        } else if (role.equalsIgnoreCase("Manager")) {
                            new ManaMenuGUI();
                        }
                        dispose(); // Close login window
                    }
                });
            }

            public static void main(String[] args) {
                new LoginGUI();
            }
        }
    }
}








