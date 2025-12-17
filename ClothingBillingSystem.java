import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class ClothingBillingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<ClothingItem> cart = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("--THE FASHION PARADISE POS SYSTEM --");
        System.out.println("(Type 'print' as Item Name to finish and generate bill)");
        while (true) {
            System.out.println("\n--- New Item ---");
            System.out.print("Item Name: ");
            String name = sc.nextLine().trim();
            if (name.equalsIgnoreCase("print")) {
                break;
            }
            if (name.isEmpty()) {
                continue;
            }
            double price = 0;
            while (true) {
                System.out.print("Unit Price: ");
                try {
                    String input = sc.nextLine();
                    price = Double.parseDouble(input);
                    if (price < 0) {
                        System.out.println("Price cannot be negative.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price! Please enter a number.");
                }
            }
            int qty = 0;
            while (true) {
                System.out.print("Quantity: ");
                try {
                    String input = sc.nextLine();
                    qty = Integer.parseInt(input);
                    if (qty <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity! Please enter a whole number.");
                }
            }
            ClothingItem item = new ClothingItem(name, price, qty);
            cart.add(item);
            System.out.println("-> Added: " + name + " | GST Applied: " + (int)item.gstPercent + "%");
        }
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Exiting...");
            sc.close();
            return;
        }
        System.out.println("\n\n");
        System.out.println("*******");
        System.out.println(" THE FASHION PARADISE ");
        System.out.println(" Retail Invoice (GST) ");
        System.out.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")));
        System.out.println("*******");
        System.out.printf("%-20s %-8s %-5s %-5s %-8s %-9s\n", "Item", "Price", "Qty", "GST%", "Tax", "Total");
        System.out.println("-------------------------------------------------------------");
        double grandTotal = 0;
        double totalTax = 0;
        for (ClothingItem item : cart) {
            System.out.printf("%-20s %-8.2f %-5d %-5.0f %-8.2f %-9.2f\n", 
                truncate(item.name, 18), 
                item.price, 
                item.qty, 
                item.gstPercent, 
                item.gstAmount, 
                item.totalAmount
            );
            grandTotal += item.totalAmount;
            totalTax += item.gstAmount; }
        System.out.println("-------------------------------------------------------------");
        System.out.printf("TOTAL TAX (CGST+SGST): Rs. %10s\n", df.format(totalTax));
        System.out.printf("NET PAYABLE AMOUNT: Rs. %10s\n", df.format(grandTotal));
        System.out.println("*******");
        System.out.println(" Thank you! No Return / No Exchange ");
        sc.close();
    }
    private static String truncate(String str, int width) {
        if (str.length() > width) {
            return str.substring(0, width - 3) + "...";
        }
        return str;
    }
}
class ClothingItem {
    String name;
    double price;
    int qty;
    double gstPercent;
    double gstAmount;
    double totalAmount;
    public ClothingItem(String name, double price, int qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
        calculateTax();
    }
    private void calculateTax() {
        if (this.price > 2500) {
            this.gstPercent = 18.0; 
        } else {
            this.gstPercent = 5.0; 
        }
        double baseTotal = this.price * this.qty;
        this.gstAmount = baseTotal * (this.gstPercent / 100.0);
        this.totalAmount = baseTotal + this.gstAmount;
    }
}
            
