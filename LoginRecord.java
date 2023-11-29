import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class LoginRecord {
    private String ID;
    private String name;
    private String Username;
    private String Password;
    private String Tel;

    public LoginRecord(String ID, String name, String Username, String Password, String Tel) {
        this.ID = ID;
        this.name = name;
        this.Username = Username;
        this.Password = Password;
        this.Tel = Tel;
    }

    public static LinkedList<LoginRecord> readLoginRecordsFromFile(String filePath) {
        LinkedList<LoginRecord> loginRecords = new LinkedList<>();
        try {
            BufferedReader loginReader = new BufferedReader(new FileReader(filePath));
            String loginLine;
            while ((loginLine = loginReader.readLine()) != null) {
                String[] parts = loginLine.split("\t");
                if (parts.length >= 5) {
                    String ID = parts[0];
                    String name = parts[1];
                    String username = parts[2];
                    String password = parts[3];
                    String tel = parts[4];
                    loginRecords.add(new LoginRecord(ID, name, username, password, tel));
                } else {
                    System.out.println(loginLine);
                }
            }
            loginReader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return loginRecords;
    }
    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getTel() {
        return Tel;
    }

    public String maskTel() {
        String[] telParts = this.Tel.split("-");
        if (telParts.length == 3) {
            String maskedPart = telParts[2].substring(0, 0) + "xxxx";
            return telParts[0] + "-" + telParts[1] + "-" + maskedPart;
        } else {
            return this.Tel;
        }
    }

    public String getFormattedName() {
        String[] nameParts = getName().split(" ");
        if (nameParts.length == 2) {
            String firstNameInitial = nameParts[0].substring(0, 1).toUpperCase();
            String lastName = nameParts[1];
            return firstNameInitial + ". " + lastName;
        } else {
            return getName();
        }
    }

    public boolean validate(String username, String passwordInput) {
        String passValidate = Password.substring(3, 5) + Password.substring(11, 15);
        return Username.equals(username) && passwordInput.equals(passValidate) && Password.substring(8, 9).equals("1");
    }
    public static boolean editUser(LinkedList<LoginRecord> loginRecords) {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("--------------------");
        System.out.println("     Edit User     ");
        System.out.println("--------------------");
        System.out.print("Enter member ID: ");
        String memberID = inputScanner.next();

        boolean isFound = false;

        for (LoginRecord record : loginRecords) {
            if (record.getID().equals(memberID)) {
                isFound = true;
                // ทำการแก้ไขข้อมูลผู้ใช้ที่นี่
                boolean editUserMenu = true;

                while (editUserMenu) {
                    System.out.println("--------------------");
                    System.out.println("1. Edit user status");
                    System.out.println("2. Reset password");
                    System.out.println("3. Return to main menu");
                    System.out.println("--------------------");
                    System.out.print("Enter Number (1-3): ");
                    int editOption = inputScanner.nextInt();

                    switch (editOption) {
                        case 1:
                            // Edit user status
                            System.out.println("3.1 Edit user status");
                            System.out.println("--------------------");
                            System.out.println("1. Active");
                            System.out.println("2. Non-active");
                            System.out.println("--------------------");
                            System.out.print("Enter Number (1-2): ");
                            int statusOption = inputScanner.nextInt();

                            switch (statusOption) {
                                case 1:
                                    // Active
                                    record.setActive(true);
                                    System.out.println("This user status has been successfully edit.");
                                    return isFound;
                                case 2:
                                    // Non-active
                                    record.setActive(false);
                                    System.out.println("This user status has been successfully edit.");
                                    return isFound;
                                default:
                                    System.out.println("Invalid option. Please enter a valid option (1-2).");
                            }
                            break;
                        case 2:
                            // Reset password
                            System.out.println("3.2 Reset password");
                            System.out.println("--------------------");

                            while (true) {
                                System.out.println("Are you sure about resetting this password?");
                                System.out.println("--------------------");
                                   System.out.print("Enter Number (Y/N): ");
                                String resetOption = inputScanner.next();

                                if (resetOption.equalsIgnoreCase("Y")) {
                                    // รีเซ็ตรหัสผ่าน
                                    record.resetPassword();
                                    String newPass = record.getPassword().substring(3,5)+record.getPassword().substring(11,15);
                                    System.out.println("This password has been successfully reset. => New password is "+newPass);
                                    return isFound; // หลุดจากลูปเมื่อผู้ใช้เลือก "Y"
                                } else if (resetOption.equalsIgnoreCase("N")) {
                                    System.out.println("Password reset canceled.");
                                    break; // หลุดจากลูปเมื่อผู้ใช้เลือก "N"
                                } else {
                                    System.out.println("Invalid option. Please enter 'Y' or 'N'.");
                                }
                            }
                            break;
                        case 3:
                            // Return to main menu
                            return isFound;

                        default:
                            System.out.println("Invalid option. Please enter a valid option (1-3).");
                    }
                }
                break;
            }
        }

        if (!isFound) {
            System.out.println("Invalid Member ID.");
        }
        return isFound;
    }
    public void resetPassword() {
        // สร้างตัวเลขสุ่ม 6 หลัก
        Random random = new Random();
        int newPasswordInt = random.nextInt(900000) + 100000;

        // แปลงตัวเลขสุ่มเป็นสตริงและเติมตัวอักษรตามรูปแบบเดิม
        String newPasswordStr = String.valueOf(newPasswordInt);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder newPassword = new StringBuilder();

        for (int i = 1; i <= 3; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }

        newPassword.append(newPasswordStr.charAt(0));
        newPassword.append(newPasswordStr.charAt(1));

        for (int i = 1; i <= 3; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }

        newPassword.append('1');

        for (int i = 1; i <= 2; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }

        newPassword.append(newPasswordStr.substring(2, 6));

        for (int i = 1; i <= 3; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }

        // กำหนดรหัสผ่านใหม่
        Password = newPassword.toString();
    }


    // เพิ่มฟังก์ชันแก้ไขสถานะผู้ใช้งาน
    public void setActive(boolean isActive) {
        // ตรวจสอบว่าสถานะคือ Active (true) หรือ Non-active (false) แล้วกำหนดค่าให้กับ record
        if (isActive) {
            Password = Password.substring(0, 8) + "1" + Password.substring(9);
        } else {
            Password = Password.substring(0, 8) + "0" + Password.substring(9);
        }
    }
    public static String maskedTel(String tel) {
        String[] telParts = tel.split("-");
        if (telParts.length == 3) {
            String maskedPart = telParts[2].substring(0, 0) + "xxxx";
            return telParts[0] + "-" + telParts[1] + "-" + maskedPart;
        } else {
            return tel;
        }
    }


    public static boolean addUser(LinkedList<LoginRecord> loginRecords) {
        Scanner inputScanner = new Scanner(System.in);

        // รับข้อมูลผู้ใช้จากผู้ใช้
        System.out.println("--------------------");
        System.out.println("     Add User     ");
        System.out.println("--------------------");
        System.out.print("Enter First name : ");
        String firstName = inputScanner.next();
        System.out.print("Enter Last name : ");
        String lastName = inputScanner.next();
        System.out.print("Enter Email : ");
        String email = inputScanner.next();
        System.out.print("Enter Password : ");
        String password = inputScanner.next();
        System.out.print("Confirm Password : ");
        String confirmPassword = inputScanner.next();
        System.out.print("Enter Telephone number : ");
        String tel = inputScanner.next();
        String maskedTel ;

        // ตรวจสอบข้อมูลผู้ใช้งาน
        if (firstName.length() >= 2 && lastName.length() >= 2 && email.contains("@") && password.length() == 6 && confirmPassword.equals(password) && tel.length() == 10) {

            // สร้าง LoginRecord ใหม่และเพิ่มลงในลิงค์ลิสต์ของผู้ใช้งาน
            String newUserID = generateUserID(loginRecords);
            String maskedPassword = generateRandomPassword(password);
            String formattedTel = formatPhoneNumber(tel); // จัดรูปแบบหมายเลขโทรศัพท์


            LoginRecord newUser = new LoginRecord(newUserID, firstName + " " + lastName, email, maskedPassword, formattedTel);
            loginRecords.add(newUser);



            return true; // การเพิ่มผู้ใช้สำเร็จ
        } else {
            System.out.println("Invalid user data. Please make sure all fields are filled correctly.");
            return false; // การเพิ่มผู้ใช้ล้มเหลว
        }
    }

    public static String formatPhoneNumber(String tel) {
        // ตรวจสอบว่าหมายเลขโทรศัพท์มีรูปแบบที่ถูกต้อง (10 หลัก) หรือไม่
        if (tel.length() == 10) {
            String formattedTel = tel.substring(0, 3) + "-" + tel.substring(3, 6) + "-" + tel.substring(6);
            return formattedTel;
        } else {
            // ถ้าหมายเลขโทรศัพท์ไม่ถูกต้อง ให้คืนค่าเดิม
            return tel;
        }
    }
    private static String generateRandomPassword(String originalPassword) {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder newPassword = new StringBuilder();

        for (int i = 1; i <= 3; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }

        newPassword.append(originalPassword.charAt(0));
        newPassword.append(originalPassword.charAt(1));

        for (int i = 1; i <= 3; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }
        newPassword.append('1');


        for (int i = 1; i <=2; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }
        newPassword.append(originalPassword.substring(2, 6));

        for (int i = 1; i <= 3; i++) {
            int randomIndex = random.nextInt(characters.length());
            newPassword.append(characters.charAt(randomIndex));
        }

        return newPassword.toString();
    }


    public static String generateUserID(LinkedList<LoginRecord> loginRecords) {
        // หาค่ารหัสผู้ใช้งานที่มากที่สุดในปัจจุบัน
        int maxUserID = 0;
        for (LoginRecord record : loginRecords) {
            int userID = Integer.parseInt(record.getID());
            if (userID > maxUserID) {
                maxUserID = userID;
            }
        }

        // เริ่มต้นที่ค่ารหัสผู้ใช้งานที่มากที่สุด + 1
        int userCount = maxUserID + 1;

        // สร้างรหัสผู้ใช้งานใหม่
        return String.format("%03d", userCount);
    }

    // ฟังก์ชั่นบันทึกข้อมูลผู้ใช้ลงในไฟล์
    public static void saveLoginRecordsToFile(String filePath, LinkedList<LoginRecord> loginRecords) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (LoginRecord record : loginRecords) {
                writer.println(record.getID() + "\t" + record.getName() + "\t" + record.getUsername() + "\t" + record.getPassword() + "\t" + record.getTel());
            }
            System.out.println("Login records saved successfully.");
        } catch (IOException ex) {
            System.out.println("An error occurred while saving login records to the file: " + ex.getMessage());
        }
    }

}