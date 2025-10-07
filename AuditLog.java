//Importing local time format
import java.time.LocalDateTime;

//Creating AuditLog class
public class AuditLog {
    private final Integer employId; //Identify Terralina Employee or customer
    private final String role; //Identify employee role
    private final String actionType; //Example for point host, floor manager, etc.
    private String employName = ""; //Merch ID.
    private final LocalDateTime timestamp; //Usage of timestamp to keep track of records
    private final String records; //Employee ratings and recorded comments.

    public AuditLog(Integer employId, String role, String actionType, LocalDateTime timestamp, String employName, String records) {
        //Declaring parameterized constructors
        this.employId = employId;
        this.role = role;
        this.actionType = actionType;
        this.employName = employName;
        this.timestamp = timestamp;
        this.records = records;
    }

    //Getter methods
    public Integer getEmployId() { return employId; } //Employee ID number
    public String getRole() { return role; } //Employee position
    public String getActionType() { return actionType; } //Action that was taken
    public LocalDateTime getTimestamp() { return timestamp; } //Time of action
    public String getEmployName() { return employName; } //Employee name
    public String getNotes() { return records; } //Employee records and ratings


    // toString() for displaying employee actions
    @Override
    public String toString() {
        return employId + "-" + role + "-" + actionType + "-" +
                timestamp + "-" + employName + "-" + records;
    }
}
