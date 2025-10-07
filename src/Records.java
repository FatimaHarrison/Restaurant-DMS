//Importing local time format, date format, and exceptions.
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class Record {
    private Integer id;// 7-digit ID
    private String name;            // Max 25 characters
    private Integer partySize;    //Party size less than or equal ro 13
    private String email;           // Must contain '@'
    private String phoneNumber;   // Exactly 10 digits
    private String date;            // Format: YYYY/MM/DD
    private String time;// Between 11:00–23:00 ET
    private String notes;           // Safe characters only
 //Setter methods
    public Record(Integer id, String name, String email, String phoneNumber, Integer partySize, String date, String time, String notes) {
        setId(id);
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPartySize(partySize);
        setDate(date);
        setTime(time);
        setNotes(notes);
    }

    // Getters methods
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Integer getPartySize() { return partySize; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getNotes() { return notes; }

    // Setters with validation
    public void setId(Integer id) {
        if (id == null || id < 1000000 || id > 9999999) { //Used for 7 digit only
            throw new IllegalArgumentException("ID must be a 7-digit number."); //Error notification
        }
        this.id = id; //Parametrized constructor
    }
    //Setter with validation in 25 char letters to name.
    public void setName(String name) {
        if (name == null || name.length() > 25) {
            throw new IllegalArgumentException("Name must be 25 characters or fewer.");
        }
        this.name = name; //Parametrized constructor
    }
    //Setter with validation using a specialized symbol @ to email.
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format."); //Error message
        }
        this.email = email; //Parametrized constructor
    }
    //Setter with validation phone number using 10 digit.
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) { //Method to verify
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;  //Parametrized constructor
    }
    public void setPartySize(Integer partySize) {
        if (partySize == null || partySize > 13) {
            throw new IllegalArgumentException("Party size must be between 1 and 13.");
        }
        this.partySize = partySize;
    }
    //Setter with validation date formatting in YYYY/MM/DD.
    public void setDate(String date) {
        if (date == null || !date.matches("\\d{4}/\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Date must be in YYYY/MM/DD format.");
        }
        this.date = date; //Parametrized constructor
    }
    //Setter with validation to set the time between 11:00am - 11:59pm.
    public void setTime(String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty.");
        }
        //Setting the time format into AM/PM format.
        try {
            // Normalize input: insert space before AM/PM if missing
            time = time.trim().toUpperCase().replaceAll("(?<=\\d)(AM|PM)", " $1");
          //Setting 12-hour clock range
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime parsedTime = LocalTime.parse(time, formatter);

            this.time = parsedTime.format(formatter); // Store as "hh:mm AM/PM"
        } catch (DateTimeParseException e) { // IllegalArgumentException("Time must be between 11:00 AM and 11:59 PM ET.");
            throw new IllegalArgumentException("Time must be in hh:mm AM/PM format (e.g., 02:30 PM).");
        }
    }

//Setter method to receive notes for recreation.
    public void setNotes(String notes) {
        if (notes == null || notes.matches(".*[<>\"].*")) { //Restriction on unsafe characters.
            throw new IllegalArgumentException("Notes contain unsafe characters."); //Notify user of error.
        }
        this.notes = notes;
    }
//Override method to display data from text file.
    @Override
    public String toString() { //Text file format with class attributes.
        return String.format("ID: %d - Name: %s - Email: %s - Phone: %s - Date: %s - Time: %s - Notes: %s",
                id, name, email, phoneNumber, date, time, notes);
    }
}
