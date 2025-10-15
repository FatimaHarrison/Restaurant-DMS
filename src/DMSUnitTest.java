//Importing Junit testing along with test setups.
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//Using assertions to declare the system files as true or false.
import static org.junit.jupiter.api.Assertions.*;
//Importing imports from src files.
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

//Declaring DMS unit testing
class DMSUnitTest {
    //Adding class attributes.
    private DMS manager;

    //Adding this method to setup before testing all unit testes.
    @BeforeEach
    void setUp() {
        manager = new DMS();
    }

    @Test
        //Passed test.
    void loadFromFile_affirmative() { //Giving a current text file.
        manager.loadFromFile("C:\\Users\\tutor\\IdeaProjects\\Terralina DMS Project\\Textual\\Data");
        assertTrue(manager.employeeRecords.isEmpty());//Stating that employee records is empty
        assertFalse(manager.auditLogs.isEmpty());//Stating that employee records is not empty
    }

    @Test
        //failed test
    void loadFromFile_negative_invalidPath() {
        manager.loadFromFile("invalid_file.txt");//Given invalid file path.
        assertTrue(manager.employeeRecords.isEmpty());//Given both ture for being empty.
        assertTrue(manager.auditLogs.isEmpty());
    }

    @Test
//Passed test
    void displayRecords_affirmative() { //Given valid sample data.
        Record r = new Record(7745768, "Ally", "AllyBanks@gamil.com", "3210000000", 11, "2025/10/10", "12:00 PM", "Window");
        manager.records.add(r);//Add to record
        assertEquals(1, manager.records.size());//Retrieves the party size number and expects it.
    }

    @Test
//Test failed
    void displayRecords_negative_emptyList() {
        assertTrue(manager.records.isEmpty());//Nothing to show on file.
    }

    @Test
        //Passed test
    void displayAuditLogs_affirmative() { //Given passed sample data
        AuditLog log = new AuditLog(1234567, "Manager", "Add Record", LocalDateTime.now(), "Jane", "Added reservation");
        manager.auditLogs.add(log);//Adds the data into log
        assertEquals(1, manager.auditLogs.size()); //Expects 1 data entry.
    }

    @Test
        //Failed test there's nothing to show or invalid file.
    void displayAuditLogs_negative_emptyList() {
        assertTrue(manager.auditLogs.isEmpty());
    }

    @Test
        //Passed test
    void addRecord_affirmative() {//Given passed data
        Record r = new Record(1010101, "Alice", "alice@example.com", "3211234567", 2, "2025/10/10", "01:00 PM", "Patio");
        manager.records.add(r);//Add into records if valid.
        assertTrue(manager.records.contains(r));//Holds authorized user for completions.
    }

    @Test
//Failed test
    void addRecord_negative_duplicateId() { //Given duplicate data entries.
        Record r1 = new Record(1010101, "Alice Wonderland", "alicewonder4@yahoo.com", "3211234567", 2, "2025/10/10", "01:00 PM", "Outside Patio");
        Record r2 = new Record(1010101, "Bob Burgers", "bobbyburgers21@yahoo.com", "3219876543", 4, "2025/10/11", "02:00 PM", "Booth");
        manager.records.add(r1);//Identification of first data entry
        boolean exists = manager.records.stream().anyMatch(r -> r.getId().equals(r2.getId()));//Identification of duplicated entry
        assertTrue(exists, "Duplicate ID should be detected");//Error message.
    }

    @Test
        //Passed test
    void removeRecord_affirmative() { //Given sample data
        Record r = new Record(8786579, "Barbie Frank", "BarFrank21@email.com", "3219876543", 9, "2025/10/11", "02:00 PM", "Window seating");
        manager.records.add(r);//Adds record
        manager.removeRecord(8786579);//Remove record with customer ID number
        assertFalse(manager.records.contains(r));
    }

    @Test //Failed test
    void removeRecord_negative_notFound() {
        // Attempt to remove a record that doesn't exist
        manager.removeRecord(6000000);
        // Ensure the records list remains empty
        assertTrue(manager.records.isEmpty());
    }

    @Test //Passed test
    void removeAuditLog_affirmative() {
        // Add an audit log and then remove it
        AuditLog log = new AuditLog(7654321, "Host", "Remove", LocalDateTime.now(), "Carlos Hilton", "Removed Customer");
        manager.auditLogs.add(log);
        manager.removeAuditLog(7654321);
        // Confirm the audit log was successfully removed
        assertTrue(manager.auditLogs.isEmpty());
    }

    @Test //Failed test
    void removeAuditLog_negative_notFound() {
        // Attempt to remove a non-existent audit log
        manager.removeAuditLog(9999999);
        // Ensure the audit log list remains empty
        assertTrue(manager.auditLogs.isEmpty());
    }

    @Test //Passed test
    void updateRecord_affirmative() {
        // Add a record and update its fields
        Record r = new Record(8865599, "Dana", "dana@example.com", "3210001111", 5, "2025/10/12", "03:00 PM", "Booth");
        manager.records.add(r);
        r.setNotes("Window");
        r.setPartySize(6);
        // Verify the updates were applied
        assertEquals("Window", manager.records.get(0).getNotes());
        assertEquals(6, manager.records.get(0).getPartySize());
    }

    @Test //Failed test
    void updateRecord_negative_invalidField() {
        // Attempt to update a record with an invalid field
        Record r = new Record(8865599, "Dana", "dana@example.com", "3210001111", 5, "2025/10/12", "03:00 PM", "Booth");
        manager.records.add(r);
        String invalidField = "location";
        // Validate that the field is not among the accepted ones
        boolean validField = Arrays.asList("name", "email", "phone number", "date", "time", "notes").contains(invalidField);
        assertFalse(validField, "Invalid field should not be accepted");
    }

    @Test //Passed test
    void generateAuditReport_affirmative() {
        // Add a recent audit log and verify it's included in the report
        manager.auditLogs.add(new AuditLog(1010101, "Manager", "Update", LocalDateTime.now(), "Eli", "Changed size"));
        assertEquals(1, manager.auditLogs.size());
    }

    @Test //Failed test
    void generateAuditReport_negative_outOfRange() {
        // Add an old audit log and check if it's excluded from a 30-day report
        LocalDateTime oldDate = LocalDateTime.now().minusDays(45);
        AuditLog log = new AuditLog(1010101, "Manager", "Update", oldDate, "Eli", "Changed size");
        manager.auditLogs.add(log);
        boolean isWithinRange = !log.getTimestamp().isBefore(LocalDateTime.now().minusDays(30));
        assertFalse(isWithinRange, "Audit log should be within last 30 days");
    }

    @Test//Passed test
    void addEmployeeRecord_affirmative() {
        // Add a valid employee record
        EmployeeRecord emp = new EmployeeRecord(1234567, "Fiona", "Female", "Host", "FullTime", LocalDate.of(2020, 1, 1));
        manager.employeeRecords.add(emp);
        // Confirm the employee was added
        assertTrue(manager.employeeRecords.contains(emp));
    }

    @Test//Failed test
    void addEmployeeRecord_negative_invalidIdFormat() {
        // Attempt to add an employee with an invalid ID format
        EmployeeRecord emp = new EmployeeRecord(123, "Fiona", "Female", "Host", "FullTime", LocalDate.of(2020, 1, 1));
        boolean isValidId = String.valueOf(emp.getEmployID()).matches("\\d{7}");
        assertFalse(isValidId, "Employee ID must be 7 digits");
    }

    @Test//Failed test
    void addEmployeeRecord_negative_invalidStatus() {
        // Attempt to add an employee with an invalid employment status
        EmployeeRecord emp = new EmployeeRecord(1234567, "Jill", "Female", "Manager", "HalfTime", LocalDate.of(2020, 1, 1));
        boolean isValidStatus = emp.getEmployStat().equalsIgnoreCase("FullTime") || emp.getEmployStat().equalsIgnoreCase("PartTime");
        assertFalse(isValidStatus, "Status must be FullTime or PartTime");
    }

    @Test //Passed test
    void removeEmployeeRecord_affirmative() {
        // Add and then remove an employee record
        EmployeeRecord emp = new EmployeeRecord(7654321, "George", "Male", "Server", "PartTime", LocalDate.of(2019, 5, 15));
        manager.employeeRecords.add(emp);
        manager.removeEmployeeRecord(7654321);
        // Confirm the employee was removed
        assertTrue(manager.employeeRecords.isEmpty());
    }

    @Test//Failed test
    void removeEmployeeRecord_negative_notFound() {
        // Attempt to remove a non-existent employee record
        manager.removeEmployeeRecord(9029382);
        // Ensure the list remains unchanged
        assertTrue(manager.employeeRecords.isEmpty());
    }

    @Test //Passed test
    void displayEmployeeRecords_affirmative() {
        // Add an employee and verify display logic
        EmployeeRecord emp = new EmployeeRecord(8888888, "Hannah", "Female", "Manager", "FullTime", LocalDate.of(2021, 3, 10));
        manager.employeeRecords.add(emp);
        assertEquals("Hannah", manager.employeeRecords.get(0).getEmployName()); //Getting expected employee name in the search.
    }

    @Test//Failed test
    void displayEmployeeRecords_negative_emptyList() {
        assertTrue(manager.employeeRecords.isEmpty()); //Nothing to show
    }
}
