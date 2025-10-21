import java.time.LocalDate;
public class EmployeeRecord {
    private final int employID;
    private final String employName;
    private final String employGen;
    private final String employRole;
    private final String employStat;
    private final LocalDate hireDate; // If you want to use LocalDate instead, change this to LocalDate

    // Constructor
    public EmployeeRecord(int employID, String employName, String employGen,
                          String employRole, String employStat, LocalDate hireDate) {
        this.employID = employID;
        this.employName = employName;
        this.employGen = employGen;
        this.employRole = employRole;
        this.employStat = employStat;
        this.hireDate = hireDate;
    }

    // Getters
    public int getEmployID() { return employID; }
    public String getEmployName() { return employName; }
    public String getEmployGen() { return employGen; }
    public String getEmployRole() { return employRole; }
    public String getEmployStat() { return employStat; }
    public LocalDate getHireDate() { return hireDate; }

    // toString for display
    @Override
    public String toString() {
        return employID + "-" + employName + "-" + employGen + "-" +
                employRole + "-" + employStat + "-" + hireDate;
    }
}
