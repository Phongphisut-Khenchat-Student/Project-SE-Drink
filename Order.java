
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.time.LocalDate;

public class Order {
    private String OrderID;
    private String MainID;
    private String MachineID;
    private String CusTel;
    private String PIN;
    private String Status;
    private String UseDate;

    public Order(String orderID, String mainID, String machineID, String cusTel, String PIN, String status, String useDate) {
        this.OrderID = orderID;
        this.MainID = mainID;
        this.MachineID = machineID;
        this.CusTel = cusTel;
        this.PIN = PIN;
        this.Status = status;
        this.UseDate = useDate;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getMainID() {
        return MainID;
    }

    public String getMachineID() {
        return MachineID;
    }

    public String getCusTel() {
        return CusTel;
    }

    public String getPIN() {
        return PIN;
    }

    public String getStatus() {
        return Status;
    }

    public String getUseDate() {
        return UseDate;
    }


    public static LinkedList<Order> readOrderRecordsFromFile(String filePath) {
        LinkedList<Order> orderRecords = new LinkedList<>();
        try {
            BufferedReader orderReader = new BufferedReader(new FileReader(filePath));
            String orderLine;
            while ((orderLine = orderReader.readLine()) != null) {
                String[] parts = orderLine.split("\t");
                String OrderID = parts[0];
                String MainID = parts[1];
                String MachineID = parts[2];
                String CusTel = parts[3];
                String PIN = parts[4];
                String Status = parts[5];
                String UseDate = parts[6];

                orderRecords.add(new Order(OrderID, MainID, MachineID, CusTel, PIN, Status, UseDate));
            }
            orderReader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return orderRecords;
    }

    public static void saveOrderToFile(String filePath, LinkedList<Order> orderRecords) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Order Or : orderRecords) {
                if (!Or.OrderID.equals("-1")) {
                    writer.println(Or.getOrderID() + "\t" + Or.getMainID() + "\t" + Or.getMachineID() + "\t" + Or.getCusTel() + "\t" + Or.getPIN() + "\t" + Or.getStatus() + "\t" + Or.getUseDate());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public  void ChangeToStatus(){
        this.Status = "1";

    }
    public static void CheckPIN(LinkedList<Order> OrderRecords,LinkedList<Menu> menuRecords) {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("-------------------");
        System.out.println("\t" + "PIN Check");
        System.out.println("-------------------");
        System.out.print("Enter PIN : ");
        String PinInput = inputScanner.next();

        boolean check = false ;
        for (int i = 0; i < OrderRecords.size(); i++) {
            if (PinInput.equals(OrderRecords.get(i).PIN)) {
                String NameMenu = "";
                for (int j = 0; j < menuRecords.size(); j++) {
                    if (OrderRecords.get(i).getMainID().equals(menuRecords.get(j).getID())) {
                        NameMenu = menuRecords.get(j).getMenu();
                    }
                }
                System.out.println("-------------------");
                System.out.println("Menu : " + NameMenu);
                if(OrderRecords.get(i).getStatus().equals("0")){
                    System.out.println("Status :  Not yet used " );

                }else {

                    System.out.println("Status : Used");
                }

                System.out.println("-------------------");
                check = true;
            }
        }
        if(check == false){
            System.out.println("Invalid PIN.");
            System.out.println("-------------------");
        }

    }
    public static  String getDate (){
        LocalDate Date = LocalDate.now();
        String DatetoString = Date.toString();
        DatetoString = DatetoString.substring(0,4)+"."+DatetoString.substring(5,7)+"."+DatetoString.substring(8,10);
        return DatetoString ;

    }
    public static  void SaveToFile(LinkedList<Order> OrderRecords,String SaveOrderID , String mdId){

        try (PrintWriter writer = new PrintWriter(new FileWriter("D:\\Education\\2-1\\Individual Software Development Process Laboratory\\Work\\SE Drink #9\\src\\Order.txt"))) {
            for (Order order :OrderRecords) {
                if(order != null){
                    if (order.OrderID.equals(SaveOrderID)) {
                        writer.println(order.OrderID + "\t" + order.MainID + "\t" + mdId + "\t" + order.CusTel + "\t" + order.PIN + "\t" + order.Status + "\t" + getDate());
                    }else {
                        writer.println(order.OrderID + "\t" + order.MainID + "\t" + order.MachineID + "\t" + order.CusTel + "\t" + order.PIN + "\t" + order.Status + "\t" + order.UseDate);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
