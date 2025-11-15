import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.util.LinkedList;
import java.util.List;

public class bill {
    public static void main(String[] args) {
        Product p1 = new Product();
        p1.name = "ABCDEFGHIJKLMNOPQ  RSTUV   wxyz";
        p1.qauntity = 8;
        p1.pricePP = 2599;
        p1.tprice = p1.qauntity * p1.pricePP;

        Product p2 = new Product();
        p2.name = "S24 Ultra";
        p2.qauntity = 1;
        p2.pricePP = 121599;
        p2.tprice = p2.qauntity * p2.pricePP;

        Product p3 = new Product();
        p3.name = "Titan Watch";
        p3.qauntity = 1;
        p3.pricePP = 21599;
        p3.tprice = p3.qauntity * p3.pricePP;

        List<Product> pl = new LinkedList<Product>();
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);

        // System.out.format(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------");
        // System.out.println("\nSrNo \t\tName\t\t\tQauntity\tRate \t\tTotalPrice\n");
        // //
        // System.out.format("---------------------------------------------------------------------------------");
        // System.out.format(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        // System.out.format(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        // System.out.println("SrNo \t\tName\t\t\tQuantity\t Rate \t\t TotalPrice\n");
        // System.out.format(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        // int count = 1;
        // double totalAmount = 0;
        // for (Product p : pl) {
        // // System.out.format(" %-9s");
        // System.out.format("%-9d %-20s %10d %20.2f %25.2f\n",
        // count++, p.name, p.qauntity, p.pricePP, p.tprice);
        // totalAmount += p.tprice;
        // }
        // System.out.format(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        // System.out.println("Total Amount to Pay: " + totalAmount);

        // WORKING #01
        // StringBuilder sb = new StringBuilder();
        // sb.append(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        // sb.append("SrNo\t Name\t\t Quantity\t\t Rate \t\t TotalPrice\n");
        // sb.append(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        // int count = 1;
        // double totalAmount = 0;
        // for (Product p : pl) {
        // sb.append(String.format("%-9d %-20s %10d %20.2f %25.2f\n",
        // count++, p.name, p.qauntity, p.pricePP, p.tprice));
        // totalAmount += p.tprice;
        // }

        // sb.append(
        // "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        // // sb.append(String.format("Total Amount to Pay: %70.2f\n", totalAmount));
        // sb.append("Total Amount to Pay: " + totalAmount);

        // // Print to console
        // System.out.print(sb.toString());

        // // Write to file
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter("bill.txt")))
        // {
        // writer.write(sb.toString());
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        StringBuilder sb = new StringBuilder();
        sb.append(
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("SrNo            Name                    Quantity            Rate            TotalPrice\n");
        sb.append(
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        int count = 1;
        double totalAmount = 0;
        for (Product p : pl) {
            String Name = p.name.length() > 20 ? p.name.substring(0, 17) + "..." : p.name;
            sb.append(String.format("   %-9d %-15s %15d %20.2f %20.2f\n",
                    count++, Name, p.qauntity, p.pricePP, p.tprice));
            totalAmount += p.tprice;
        }

        sb.append(
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("TOTAL AMOUNT TO PAY: " + totalAmount);

        // Print to console
        System.out.print(sb.toString());

        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bill1.txt"))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // %-9d: Serial number (left-aligned within 9 characters)
    // %-20s: Product name (left-aligned within 20 characters)
    // %15d: Quantity (right-aligned within 15 characters)
    // %20.2f: Rate (right-aligned within 20 characters, 2 decimal places)
    // %20.2f: Total price (right-aligned within 20 characters, 2 decimal places)
}

class Product {
    String name;
    int qauntity;
    double pricePP;
    double tprice;
}