
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Menu {
    private String ID;
    private String Menu;
    private String Price;
    private String Age;
    private String Gender;
    private String Type;
    private String Keywords;

    public Menu(String ID, String Menu, String Price, String Age, String Gender, String Type, String Keywords) {
        this.ID = ID;
        this.Menu = Menu;
        this.Price = Price;
        this.Age = Age;
        this.Gender = Gender;
        this.Type = Type;
        this.Keywords = Keywords;
    }

    public static LinkedList<Menu> readMenuRecordsFromFile(String filePath) {
        LinkedList<Menu> menuRecords = new LinkedList<>();
        try {
            BufferedReader menuReader = new BufferedReader(new FileReader(filePath));
            String menuLine;
            while ((menuLine = menuReader.readLine()) != null) {
                String[] parts = menuLine.split("\t");
                String ID = parts[0];
                String Menu = parts[1];
                String Price = parts[2];
                String Age = parts[3];
                String Gender = parts[4];
                String Type = parts[5];
                String Keywords = parts[6];

                menuRecords.add(new Menu(ID, Menu, Price, Age, Gender, Type, Keywords));
            }
            menuReader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return menuRecords;
    }

    public String getID() {
        return ID;
    }

    public String getMenu() {
        return Menu;
    }

    public String getPrice() {
        return Price;
    }

    public String getAge() {
        return Age;
    }

    public String getGender() {
        return Gender;
    }

    public String getType() {
        return Type;
    }

    public String getKeywords() {
        return Keywords;
    }

    public static void displayMenu(List<Menu> MenuRecords) {
        System.out.println("----------------------------------");
        System.out.printf("%-5s %-20s %-10s %n", "ID", "Menu", "Price");
        System.out.println("----------------------------------");

        Collections.sort(MenuRecords, Comparator.comparingDouble(menu -> Double.parseDouble(menu.getPrice())));

        for (Menu menu : MenuRecords) {
            if (menu != null) {
                System.out.printf("%-5s %-20s %-10s %n", menu.getID(), menu.getMenu(), menu.getPrice());
            }
        }
    }

    public static Order selectOrder(LinkedList<Menu> menuRecords, LinkedList<Order> orderRecords) {
        Scanner inputScanner = new Scanner(System.in);

        // Print the menu
        displayMenu(menuRecords);

        // Take input for Menu ID
        System.out.println("--------------------------------------");
        System.out.print("Enter Menu ID : ");
        String selectedMenuID = inputScanner.nextLine();
        // Check if the entered Menu ID exists
        boolean menuIDExists = menuRecords.stream().anyMatch(menu -> menu.getID().equals(selectedMenuID));

        if (!menuIDExists) {
            return new Order("-1", "-", "-", "-", "-", "-", "-");
        }

        // Take input for Tel
        System.out.println("--------------------------------------");
        System.out.print("Enter Tel. : ");
        String tel = inputScanner.nextLine();
        if (tel.length() != 10) {
            return new Order("-1", "-", "-", "-", "-", "-", "-");
        }

        // Generate PIN
        String pin = generatePIN();

        // Display PIN
        System.out.println("--------------------------------------");
        System.out.println("Your PIN is : " + pin);
        System.out.println("--------------------------------------");

        // Display order confirmation
        System.out.println("Your order has been successfully ordered.");

        int maxUserID = orderRecords.stream()
                .map(order -> Integer.parseInt(order.getOrderID()))
                .max(Comparator.naturalOrder())
                .orElse(0);

        // Start from the maximum user ID + 1
        int userCount = maxUserID + 1;

        return new Order(String.valueOf(userCount), selectedMenuID, "-", formatTel(tel), pin, "0", "-");
    }

    private static String generatePIN() {
        int length = 6;
        Random random = new Random();
        StringBuilder pinBuilder = new StringBuilder();

        for (int i = 1; i <= length; i++) {
            if (i > 4) {
                char randomChar = (char) (random.nextInt(26) + 65);
                pinBuilder.append(randomChar);
            } else {
                // Generate a random digit (0-9) and append to the PIN
                int randomDigit = random.nextInt(10);
                pinBuilder.append(randomDigit);
            }
        }
        return pinBuilder.toString();
    }

    public static String formatTel(String tel) {
        // Check if the phone number is in the correct format (10 digits) or not
        if (tel.length() == 10) {
            return tel.substring(0, 3) + "-" + tel.substring(3, 6) + "-" + tel.substring(6);
        } else {
            // If the phone number is not in the correct format, return the original value
            return tel;
        }
    }

    /**
     * ค้นหาเมนู 3 รายการยอดนิยมที่สูงสุดตามจำนวนการสั่งซื้อสำหรับเพศที่ระบุ
     * และพิมพ์ผลลัพธ์ลงคอนโซล
     *
     * @param gender        เพศที่ใช้กรองการสั่งซื้อ
     * @param orderRecords  รายการของออร์เดอร์ที่มีข้อมูลการสั่งซื้อ
     * @param menuRecords   รายการของเมนูที่มีข้อมูลเมนู
     */
    public static void findTop(String gender, List<Order> orderRecords, List<Menu> menuRecords) {
        // Map เพื่อเก็บจำนวนของการสั่งซื้อสำหรับแต่ละเมนู
        Map<String, Integer> menuItemOrderCounts = new HashMap<>();

        // วนลูปผ่านทุกรายการออร์เดอร์
        for (Order order : orderRecords) {
            // ตรวจสอบว่าออร์เดอร์นี้เป็นของเพศที่ระบุหรือไม่
            String checkGender = getMenuGender(order.getMainID(), menuRecords);
            if (checkGender != null && checkGender.equalsIgnoreCase(gender)) {
                // ดึงเมนูที่เกี่ยวข้องกับออร์เดอร์นี้
                String menu = getMenu(order.getMainID(), menuRecords);
                // เพิ่มจำนวนสำหรับเมนูนี้
                menuItemOrderCounts.putIfAbsent(menu, 0);
                menuItemOrderCounts.put(menu, menuItemOrderCounts.get(menu) + 1);
            }
        }

        // ดึงเมนู 3 รายการที่มีจำนวนสั่งซื้อสูงสุด
        List<String> topMenuItems = menuItemOrderCounts.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // พิมพ์ผลลัพธ์เมนู 3 อันดับแรก
        System.out.println("-------------------");
        System.out.println("Top 3 " + (gender.equalsIgnoreCase("M") ? "For-Men" : (gender.equalsIgnoreCase("F") ? "For-Women" : "For-All")));

        System.out.println("-------------------");
        int count = 1;
        for (String menuItem : topMenuItems) {
            if (count > 3) {
                break;
            }
            // พิมพ์เมนูและรหัสเมนูที่เกี่ยวข้อง
            System.out.printf("# %d : %s %s%n", count, getMenuID(menuItem, menuRecords), menuItem);
            count++;
        }
        System.out.println("-------------------");
    }

    /**
     * ดึงชื่อเมนูที่เกี่ยวข้องกับ ID เมนูที่กำหนด
     *
     * @param menuID      ID ของเมนู
     * @param menuRecords รายการของเมนูที่มีข้อมูลเมนู
     * @return ชื่อเมนูหรือสตริงว่างหากไม่พบ
     */
    private static String getMenu(String menuID, List<Menu> menuRecords) {
        for (Menu menu : menuRecords) {
            if (menu.getID().equals(menuID)) {
                return menu.getMenu();
            }
        }
        return "";
    }

    /**
     * ดึงรหัสเมนูที่เกี่ยวข้องกับชื่อเมนูที่กำหนด
     *
     * @param menuName    ชื่อเมนู
     * @param menuRecords รายการของเมนูที่มีข้อมูลเมนู
     * @return รหัสเมนูหรือสตริงว่างหากไม่พบ
     */
    private static String getMenuID(String menuName, List<Menu> menuRecords) {
        for (Menu menu : menuRecords) {
            if (menu.getMenu().equals(menuName)) {
                return menu.getID();
            }
        }
        return "";
    }

    /**
     * ดึงเพศที่เกี่ยวข้องกับ ID เมนูที่กำหนด
     *
     * @param menuID      ID ของเมนู
     * @param menuRecords รายการของเมนูที่มีข้อมูลเมนู
     * @return เพศหรือสตริงว่างหากไม่พบ
     */
    private static String getMenuGender(String menuID, List<Menu> menuRecords) {
        for (Menu menu : menuRecords) {
            if (menu.getID().equals(menuID)) {
                return menu.getGender();
            }
        }
        return "";
    }
}
