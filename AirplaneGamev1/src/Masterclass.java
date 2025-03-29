
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.*;
public class Masterclass {
    public boolean checkEmail(String email)
    {
        try{
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        // Compile the regex
        Pattern pattern = Pattern.compile(emailRegex);
        // Match the input email against the regex
        Matcher matcher = pattern.matcher(email);
        // Return true if the email matches the regex, otherwise false
        return matcher.matches();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public int getStringLength(String txt)
    {
        return txt.length();
    }
    public String changeToUpper(String txt)
    {
        return txt.toUpperCase();
    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
    public boolean feedbackExists(String email) {
        String query = "SELECT * FROM feedback WHERE email = ?";
        DBConnection obj=new DBConnection();
        try {
            Connection conn = obj.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if feedback exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
