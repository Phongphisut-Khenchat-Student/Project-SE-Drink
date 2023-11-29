import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LinkedList<Order> OrderRecords = Order.readOrderRecordsFromFile("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Order.txt");
        LinkedList<Menu> MenuRecords = Menu.readMenuRecordsFromFile("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Menu.txt");
        LinkedList<LoginRecord> loginRecords = LoginRecord.readLoginRecordsFromFile("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Login.txt");
        LinkedList<DrinkRecord> drinkRecords = DrinkRecord.readDrinkRecordsFromFile("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Machine.txt");
        Scanner inputScanner = new Scanner(System.in);

        int selectMenu222 = 0;

        boolean isLoggedIn = false;

        while ( true ) {

                System.out.println("-------------------");
                System.out.println("   SE BUU Drink");
                System.out.println("-------------------");
                System.out.println("1. Ordering your drink");
                System.out.println("2. PIN Check");
                System.out.println("3. Most popular drink");
                System.out.println("4. Virtual machine");
                System.out.println("5. Login");
                System.out.println("6. Exit");
                System.out.println("-------------------");
                System.out.print("Enter Number: ");
                int EnterNumber = inputScanner.nextInt();
                System.out.println("-------------------");

                switch (EnterNumber) {
                    case 1: // Ordering your drink

                        OrderRecords.add(Menu.selectOrder(MenuRecords, OrderRecords));
                        Order.saveOrderToFile("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Order.txt", OrderRecords);
                        break;


                    case 2: // PIN Check
                        Order.CheckPIN(OrderRecords, MenuRecords);
                        break;

                    case 3: // Most popular drink
                        System.out.println("--------------------");
                        System.out.println(" Most popular drink ");
                        System.out.println("--------------------");
                        System.out.println("1. For-Men");
                        System.out.println("2. For-Women");
                        System.out.println("3. For-All");
                        System.out.println("4. Exit");
                        System.out.println("--------------------");
                        System.out.print("Enter Number : ");
                        int selectMenu3 = inputScanner.nextInt();
                        System.out.println("--------------------");

                        // For-Men
                        if (selectMenu3 == 1) {
                            Menu.findTop("M", OrderRecords, MenuRecords);
                            break;
                        }
                        // For-Women
                        else if (selectMenu3 == 2) {
                            Menu.findTop("F", OrderRecords, MenuRecords);
                            break;
                        }
                        // For-All
                        else if (selectMenu3 == 3) {
                            Menu.findTop("A", OrderRecords, MenuRecords);
                            break;
                        }
                        // Exit
                        else if (selectMenu3 == 4) {
                            break;
                        }
                    case 4: // Virtual machine
                        DrinkRecord.Virtual(drinkRecords, OrderRecords, MenuRecords);
                        break;


                    case 5: // Login
                        int incorrectAttempts = 0;
                        final int MAX_INCORRECT_ATTEMPTS = 3;

                        while (incorrectAttempts < MAX_INCORRECT_ATTEMPTS ) {
                            System.out.println("--------------------");
                            System.out.println("        Login        ");
                            System.out.println("--------------------");
                            System.out.print("Username : ");
                            String usernameInput = inputScanner.next();

                            System.out.print("Password : ");
                            String passwordInput = inputScanner.next();
                            System.out.println("--------------------");



                            for (LoginRecord client : loginRecords) {
                                if (client.validate(usernameInput, passwordInput)) {
                                    System.out.println("Welcome : " + client.getFormattedName());
                                    System.out.println("Email : " + usernameInput);
                                    System.out.println("Tel. : " + client.maskTel());


                                    while (selectMenu222 != 4) {
                                        System.out.println("--------------------");
                                        System.out.println("        Menu        ");
                                        System.out.println("--------------------");
                                        System.out.println("1. Machine details");
                                        System.out.println("2. Add User");
                                        System.out.println("3. Edit user");
                                        System.out.println("4. Exit");
                                        System.out.println("--------------------");
                                        System.out.print("Enter Number (1-4) : ");
                                        selectMenu222 = inputScanner.nextInt();
                                        System.out.println("--------------------");

                                        // Machine details
                                        if (selectMenu222 == 1) {
                                            // ส่วนเมนู Machine details
                                            int sortMenuOption = 0;
                                            // เพิ่มเมนู Sorting
                                            while (sortMenuOption != 3) {
                                                System.out.println("--------------------");
                                                System.out.println("        Menu        ");
                                                System.out.println("--------------------");
                                                System.out.println("1. Sorting by balance (DESC)");
                                                System.out.println("2. Sorting by city (ASC)");
                                                System.out.println("3. Return to main menu");
                                                System.out.println("--------------------");
                                                System.out.print("Enter Number (1-3) : ");
                                                sortMenuOption = inputScanner.nextInt();
                                                System.out.println("--------------------");

                                                // เรียกเมธอดที่เกี่ยวข้องกับการเรียงลำดับตามเมนูที่เลือก
                                                switch (sortMenuOption) {
                                                    case 1:
                                                        // เรียงลำดับโดยดุลย์ (DESC)
                                                        DrinkRecord.displayMachineDetails(drinkRecords, true);
                                                        break;
                                                    case 2:
                                                        // เรียงลำดับตามเมือง (ASC)
                                                        DrinkRecord.displayMachineDetails(drinkRecords, false);
                                                        break;
                                                    case 3:
                                                        // กลับไปยังเมนูหลัก
                                                        break;
                                                    default:
                                                        System.out.println("Invalid option. Please enter a valid option (1-3).");
                                                }
                                            }
                                        } else if (selectMenu222 == 2) {
                                            boolean addUserResult = LoginRecord.addUser(loginRecords);

                                            if (addUserResult) {
                                                System.out.println("The user added successfully!");
                                                // บันทึกข้อมูลผู้ใช้งานลงในไฟล์
                                                LoginRecord.saveLoginRecordsToFile("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Login.txt", loginRecords);

                                            } else {
                                                System.out.println("The user addition failed.");
                                            }

                                            // Edit user
                                        } else if (selectMenu222 == 3) {
                                            boolean editUserResult = LoginRecord.editUser(loginRecords);

                                            if (editUserResult) {
                                                System.out.println("The user edited successfully!");
                                                // บันทึกข้อมูลผู้ใช้งานลงในไฟล์
                                                LoginRecord.saveLoginRecordsToFile("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Login.txt", loginRecords);
                                            } else {
                                                System.out.println("The user editing failed.");
                                            }

                                            // Exit
                                        } else if (selectMenu222 == 4) {
                                            System.out.println("Thank you for using the service.");
                                            continue;
                                        }
                                    }
                                    isLoggedIn = true;
                                    break;
                                }
                            }
                            if (!isLoggedIn) {
                                incorrectAttempts++;
                                System.out.println("The username or password is incorrect. (" + incorrectAttempts + ")");
                                if (incorrectAttempts == MAX_INCORRECT_ATTEMPTS) {
                                    System.out.println("Thank you for using the service.");
                                }
                            }
                            break;
                    }
                    break;
                    case 6: // Exit
                        System.exit(0);
                        break;
            }
        }
    }
}