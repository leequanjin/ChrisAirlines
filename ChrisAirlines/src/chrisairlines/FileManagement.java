package chrisairlines;
import chrisairlines.Membership.MembershipLevel;
import chrisairlines.Membership.MembershipStatus;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList; // Import ArrayList for creating a list of memberships
import java.util.List; // Import List interface for generic list handling


public class FileManagement {
    public static void createFile() {
        try {
          File myObj = new File("MemberInfo.txt");
          if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("An error occurred.");
        }
    }
    
    public static void writeToFile(Membership member) {
       try (FileWriter myWriter = new FileWriter("MemberInfo.txt", true)) {
        // Format the data for writing to the file
        String memberData = String.format("%d,%s,%s,%s,%d,%s,%s\n",
                member.getMembership_id(),
                member.getMember_name(),
                member.getContact_info(),
                member.getMembership_level(), // Enum toString() method
                member.getPoints_accumulated(),
                member.getMembership_status(),
                member.getPassword());    

        myWriter.write(memberData);
        System.out.println("Successfully wrote member data to the file.");
        } catch (IOException e) {
        System.out.println("An error occurred.");
      }
    }
    
    public static List<Membership> readMembersFromFile() {
        List<Membership> memberships = new ArrayList<>();
        int latestId = Membership.getMembershipIdCount();
        
        try {
            File myObj = new File("MemberInfo.txt");
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    String[] memberData = line.split(","); // Split by comma
                    
                    // Extract and parse data 
                    int membershipId = Integer.parseInt(memberData[0]);
                    latestId = Math.max(latestId, membershipId); // Update latestId if encountered
                        
                    String memberName = memberData[1];
                    String contactInfo = memberData[2];
                    MembershipLevel level = MembershipLevel.valueOf(memberData[3]); // Assuming enum valueOf()
                    int points = Integer.parseInt(memberData[4]);
                    MembershipStatus status = MembershipStatus.valueOf(memberData[5]); // Assuming enum valueOf()
                    String password = memberData[6];
                    
                    Membership member = new Membership(membershipId, memberName, contactInfo, level, points, status ,password);
                    memberships.add(member);
                }
                myReader.close();
                Membership.setMembership_id_count(latestId + 1); // Update static count based on latest ID
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return memberships; // Explicitly return the list
    }
}
