
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.util.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DrinkRecord {
    private String ID;
    private String City;
    private String Position;
    private String Account;
    private String Balance;

    public DrinkRecord(String ID, String City, String Position, String Account, String Balance) {
        this.ID = ID;
        this.City = City;
        this.Position = Position;
        this.Account = Account;
        this.Balance = Balance;
    }

    public static LinkedList<DrinkRecord> readDrinkRecordsFromFile(String filePath) {
        LinkedList<DrinkRecord> drinkRecords = new LinkedList<>();
        try {
            BufferedReader drinkReader = new BufferedReader(new FileReader(filePath));
            String drinkLine;
            while ((drinkLine = drinkReader.readLine()) != null) {
                String[] parts = drinkLine.split("\t");
                String ID = parts[0];
                String City = parts[1];
                String Position = parts[2];
                String[] positionParts = Position.split(", ");
                String formattedPosition = formatPosition(positionParts);
                String Account = parts[3];
                String Balance = parts[4];
                drinkRecords.add(new DrinkRecord(ID, City, formattedPosition,Account, Balance));
            }
            drinkReader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return drinkRecords;
    }

    public String getID() {
        return ID;
    }

    public String getCity() {
        return City;
    }

    public String getPosition() {
        return Position;
    }

    public String getAccount() {
        return Account;
    }

    public String getBalance() {
        return Balance;
    }
    public static String maskAccount(String account) {
        String lastFourDigits = account.substring(0, Math.min(account.length(), 12));
        return lastFourDigits + "xxxx";
    }


    public static  String formatPosition(String[] positionParts) {
        double latitude = Double.parseDouble(positionParts[0]);
        double longitude = Double.parseDouble(positionParts[1]);
        return String.format("%.2f, %.2f", latitude, longitude);
    }

    public String getShippingType() {
        return checkShippingType(Position);
    }

    public static String checkShippingType(String position) {
        String[] positionParts = position.split(", ");
        double latitude = Double.parseDouble(positionParts[0]);
        double longitude = Double.parseDouble(positionParts[1]);

        if (latitude < 0 && longitude < 0) {
            return "Ships";
        } else if (latitude > 0 && longitude > 0) {
            return "Planes";
        } else {
            return "Trucks";
        }
    }
    public static void Virtual(List<DrinkRecord> drinkRecords,LinkedList<Order> OrderRecords,LinkedList<Menu> menuRecords) {
        Scanner Input = new Scanner(System.in);

        System.out.println("-------------------");
        System.out.println("ID     City");
        System.out.println("-------------------");
        for (DrinkRecord record : drinkRecords) {
            if (record != null) {
                String ID = record.getID().substring(3, 5) + record.getID().substring(8, 9);
                System.out.printf("%-6s %-10s %n",ID, record.getCity());
            }

        }
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.print("Enter Machine ID : ");
        String checkData = Input.next();
        System.out.println("--------------------");
        System.out.println();
        System.out.println("--------------------");
        // สร้างตัวแปรเพื่อเก็บข้อมูลเมืองที่เกี่ยวข้อง
        String cityAssociated = null;

        for (DrinkRecord record : drinkRecords) {
            if (record != null) {
                String ID = record.getID().substring(3, 5) + record.getID().substring(8, 9);
                if (ID.equals(checkData)) {
                    // เก็บข้อมูลเมืองที่เกี่ยวข้องไว้
                    cityAssociated = record.getCity();
                    System.out.println("Machine ID : " + ID + "  (" + cityAssociated + ")");
                    // หยุดการวน loop หลังจากพบ Machine ID ที่ตรง
                    break;
                }
            }
        }

        // Check if Machine ID is found
        if (cityAssociated == null) {
            System.out.println("Machine ID not found.");
        } else {
            System.out.println("--------------------");
            System.out.println("1. Use your PIN to get a drink.");
            System.out.println("2. Recommend the drink menu.");
            System.out.println("3. Exit");
            System.out.println("--------------------");
            System.out.print("Enter Number : ");
            int EnterNumber = Input.nextInt();
            System.out.println("--------------------");

            if (EnterNumber == 1) {
                System.out.print("Enter your PIN : ");
                String usePIN = Input.next();  // Changed variable name to use camelCase convention
                System.out.println("--------------------");

                boolean PINFound = false;
                Map<String , String  > IDMenu = new HashMap<>();
                for (Menu menu:menuRecords) {
                    IDMenu.put(menu.getID(),menu.getMenu());
                }

                Map<String , String  > MenuPrice = new HashMap<>();
                for (Menu menu:menuRecords) {
                    MenuPrice.put(menu.getID(),menu.getPrice());
                }

                Map<String , String  > Balance = new HashMap<>();
                for (DrinkRecord Drink:drinkRecords) {
                    String ID =Drink.getID().substring(3, 5) + Drink.getID().substring(8, 9);
                    Balance.put(ID,Drink.getBalance());
                }

                for (Order order : OrderRecords) {

                    if (usePIN.equals(order.getPIN())) {
                        PINFound = true;

                        if (order.getStatus().equals("0")||order.getStatus().equals("-")) {

                            System.out.println("Your " + IDMenu.get(order.getMainID()) + " is currently in the process of being prepared. Please wait a moment.");
                            order.ChangeToStatus();
                            Order.SaveToFile(OrderRecords,order.getOrderID(),findMachine(checkData, drinkRecords));
                            UpdateMachineBalance(checkData,MenuPrice.get(order.getMainID()),Balance.get(checkData), drinkRecords);


                            
                        } else if (order.getStatus().equals("1")){
                            System.out.println("Your PIN has already been used.");
                        }
                    }
                }

                if (!PINFound) {
                    System.out.println("Invalid PIN or machine ID combination.");
                }
            } else if(EnterNumber == 2) {

                while (true) {
                    System.out.println("-------------------------");
                    System.out.println("Recommend the drink menu");
                    System.out.println("-------------------------");
                    System.out.println("1. By Age");
                    System.out.println("2. By Gender");
                    System.out.println("3. By Type");
                    System.out.println("4. By Keywords");
                    System.out.println("5. Exit");
                    System.out.println("-------------------------");
                    System.out.print("Enter Number : ");
                    int EnterMenu = Input.nextInt();
                    System.out.println("-------------------------");
                    if (EnterMenu == 1) {
                        System.out.print("Enter your Age : ");
                        int EnterAge = Input.nextInt();
                        System.out.println("-------------------------");
                        System.out.println("ID \t\t Menu");
                        System.out.println("-------------------------");

                        for (Menu m : menuRecords){
                            int age = Integer.parseInt(m.getAge());
                            if (EnterAge >= age){
                                System.out.println(m.getID()+" "+ m.getMenu());
                            }

                        }

                    } else if (EnterMenu == 2) {
                        System.out.print("Enter your gender (F/M) : ");
                        String EnterGender = Input.next();
                        System.out.println("-------------------------");
                        System.out.println("ID \t\t Menu");
                        System.out.println("-------------------------");
                        if (EnterGender.equalsIgnoreCase("F")) {
                            for (Menu G : menuRecords) {
                                if (G.getGender().equalsIgnoreCase("F")) {
                                    System.out.println(G.getID() + "\t\t" + G.getMenu());
                                }

                            }
                            System.out.println("-------------------------");
                        }
                        if (EnterGender.equalsIgnoreCase("M")) {
                            for (Menu G : menuRecords) {
                                if (G.getType().equalsIgnoreCase("M")) {
                                    System.out.println(G.getID() + "\t\t" + G.getMenu());
                                }
                            }
                            System.out.println("-------------------------");
                            for (Menu G : menuRecords) {
                                if (G.getType().equals("A")) {
                                    System.out.println(G.getID() + "\t\t" + G.getMenu());
                                }
                            }
                            System.out.println("-------------------------");
                        }

                    } else if (EnterMenu == 3) {
                        System.out.print("Enter type (SD/HD) :  ");
                        String EnterType = Input.next();
                        System.out.println("-------------------------");
                        System.out.println("ID \t\t Menu");
                        System.out.println("-------------------------");

                        if (EnterType.equalsIgnoreCase("SD")) {
                            for (Menu T : menuRecords) {

                                if (T.getType().equalsIgnoreCase("SD")) {
                                    System.out.println(T.getID() + "\t\t" + T.getMenu());
                                }

                            }
                            System.out.println("-------------------------");
                        }
                        if (EnterType.equalsIgnoreCase("HD")) {
                            for (Menu T : menuRecords) {


                                if (T.getType().equalsIgnoreCase("HD")) {
                                    System.out.println(T.getID() + "\t\t" + T.getMenu());
                                }

                            }
                            System.out.println("-------------------------");
                        }


                    } else if (EnterMenu == 4) {
                        System.out.print("Enter Keywords : ");
                        String EnterKeyword = Input.next();
                        System.out.println("-------------------------");
                        System.out.println("ID \t\t Menu");
                        System.out.println("-------------------------");

                        for (Menu K : menuRecords) {
                            if (K.getKeywords().toLowerCase().contains(EnterKeyword.toLowerCase())){
                                System.out.println(K.getID() + "\t\t" + K.getMenu());
                            }
                        }
                        System.out.println("-------------------------");



                    } else if (EnterMenu == 5) {
                        break;
                    }
                }


            }else {

            }
        }
    }

    public static String findMachine(String IDMachine,List<DrinkRecord> drinkRecords ){
        String idWrite = "";
        for ( DrinkRecord Dr :drinkRecords ) {
            String idM = Dr.ID.substring(3,5)+Dr.ID.substring(8,9);
            if (IDMachine.equals(idM)){
                idWrite = Dr.ID;
            }
        }
        return idWrite;
    }
    public  static  void  UpdateMachineBalance(String IDMachine,String PriceMenu,String Balance,List<DrinkRecord> drinkRecords ){
        String balanceString = Balance.replace("$", "").replace(",", "");

        double menu = Double.parseDouble(PriceMenu);
               menu/= 36.96;
        double balance = Double.parseDouble(balanceString);

        double Total = menu+balance;
        NumberFormat numberFormat  = NumberFormat.getCurrencyInstance(Locale.US);
        String TotalFormat = numberFormat.format(Total);
        try (PrintWriter writer = new PrintWriter(new FileWriter("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Machine.txt"))) {
            for (DrinkRecord Drink :drinkRecords) {
                if(Drink != null){
                    String GG = Drink.getID().substring(3,5)+Drink.getID().substring(8,9);
                    if (GG.equals(IDMachine)) {

                        writer.println(Drink.getID() + "\t" + Drink.getCity() + "\t" + Drink.getPosition() + "\t" + Drink.getAccount() + "\t" + TotalFormat);
                    }else {
                        writer.println(Drink.getID() + "\t" + Drink.getCity() + "\t" + Drink.getPosition() + "\t" + Drink.getAccount()+ "\t" + Drink.getBalance());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void displayMachineDetails(List<DrinkRecord> drinkRecords, boolean sortByBalance) {
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("ID           City           Position               Shipping Type      Account Number       Balance      ");
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (sortByBalance) {
            // เรียงลำดับโดย balance (DESC)
            Collections.sort(drinkRecords, (record1, record2) -> {
                double balance1 = Double.parseDouble(record1.getBalance().replace(",", "").substring(1)); // ลบ "," และ "$" และแปลงเป็น double
                double balance2 = Double.parseDouble(record2.getBalance().replace(",", "").substring(1));
                return Double.compare(balance2, balance1);
            });

        } else {
            // เรียงลำดับตาม city (ASC)
            Collections.sort(drinkRecords, Comparator.comparing(DrinkRecord::getCity));
        }

        for (DrinkRecord machine : drinkRecords) {
            if (machine != null) {
                String shippingType = machine.getShippingType();
                // ลบ "$" และ ",", และแทนที่บาลานซ์ด้วย "X,XXX"
                String maskedBalance = "";
                    String balance = machine.getBalance().replace("$", "").replace(",", "");
                    if(balance.length() == 4){
                        maskedBalance = balance.substring(0, balance.length() - 3) + ",XXX";
                    }else {
                        maskedBalance = balance.substring(0, balance.length() - 6) + ",XXX.XX";
                    }

                System.out.printf("%-12s %-14s %-23s %-16s %-20s %-10s %n", machine.getID(), machine.getCity(), machine.getPosition(), shippingType, maskAccount(machine.Account), maskedBalance);
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

}
