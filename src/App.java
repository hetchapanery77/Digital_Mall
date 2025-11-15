
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

class category {
    int c_id;
    String c_name;

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

}

class orders {
    int o_id;
    int u_id;
    int s_id;
    String order_date;
    double total_cost;
    String order_status;

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public void cancelOrder(String email, Connection con) throws SQLException {
        String sql = "{call getUserIdbyEmail(?,?)}";
        CallableStatement cst = con.prepareCall(sql);
        cst.setString(1, email);
        cst.executeQuery();
        int userID = cst.getInt(2);

        String sql1 = "{call displayOrders(?)}";
        CallableStatement cst1 = con.prepareCall(sql1);
        cst1.setInt(1, userID);
        ResultSet rs = cst1.executeQuery();
        boolean b = false;
        while (rs.next()) {
            b = true;
            System.out.println("ID: " + rs.getInt(1));
            System.out.println("Product Name: " + rs.getString(2));
            System.out.println("Qauntity: " + rs.getInt(3));
            System.out.println("Date: " + rs.getString(4));
            System.out.println("Status: " + rs.getString(5));
            System.out.println();
            System.out.println();
        }
        if (!b) {
            System.out.println("NO ORDERS FOUND THAT ARE CURRENTLY ENROUTE");
            return;
        } else {
            System.out.print("Enter ID of Order to Cancel The Order: ");
            @SuppressWarnings("resource")
            Scanner sc = new Scanner(System.in);
            int order_id = sc.nextInt();

            String sql2 = "UPDATE orders set order_status = ? WHERE order_id = ?";
            PreparedStatement pst = con.prepareStatement(sql2);
            pst.setString(1, "CANCELLED");
            pst.setInt(2, order_id);
            int r = pst.executeUpdate();
            // System.out.println((r > 0) ? "" : "ORDER CANCELLATION FAILED");
            if (r > 0) {
                JFrame fr = new JFrame("ORDER CANCELLATION");
                fr.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(fr, "YOUR ORDER HAS BEEN SUCCESSFULLY CANCELLED");
                fr.setVisible(true); // makes it so that the frame is visible
                // fr.toFront(); // top/front upar frame ne laine aave
                fr.requestFocus();// focused to input
                fr.dispose(); // deletes the frame
            } else {
                System.out.println("NO SUCH ORDER FOUND\nPlease Try Again");
            }
        }
    }
}

class order_items {
    int o_item_id;
    int o_id;
    int p_id;
    int qauntity;
    double price;

    public int getO_item_id() {
        return o_item_id;
    }

    public void setO_item_id(int o_item_id) {
        this.o_item_id = o_item_id;
    }

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getQauntity() {
        return qauntity;
    }

    public void setQauntity(int qauntity) {
        this.qauntity = qauntity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class products {
    static Scanner sc = new Scanner(System.in);
    int p_id;
    int s_id;
    String p_name;
    String p_brand_name;
    double price;
    int stock;
    String url;

    int count = 1001;
    LinkedList<products> Products = new LinkedList<products>();

    public void addProducts(Connection con) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/digital_mall", "root", "");
        System.out.print("Enter Product ID: ");
        this.p_id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Store_name: ");
        String ps_name = sc.nextLine().trim();
        String query7 = "{call getStoreId(?,?)}";
        CallableStatement cst7 = con.prepareCall(query7);
        cst7.setString(1, ps_name);
        cst7.executeQuery();
        this.s_id = cst7.getInt(2);
        System.out.println("Enter product_name: ");
        p_name = sc.nextLine();
        System.out.print("ENter brand_name: ");
        p_brand_name = sc.nextLine();
        try {
            // sc.nextLine();
            System.out.print("ENter price: ");
            price = sc.nextDouble();
            System.out.print("Enter stock: ");
            stock = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("SOMETHING IS INCORRECT TRY AGAIN!!");
            addProducts(con);
        }
        String query9 = "INSERT INTO products (product_id,store_id,product_name,brand_name,price,stock) VALUES(?,?,?,?,?,?)";
        PreparedStatement pst9 = con.prepareStatement(query9);
        pst9.setInt(1, p_id);
        pst9.setInt(2, s_id);
        pst9.setString(3, p_name);
        pst9.setString(4, p_brand_name);
        pst9.setDouble(5, price);
        pst9.setInt(6, stock);
        try {
            int r9 = pst9.executeUpdate();
            // System.out.println((r9 > 0) ? "s" : "f");
        } catch (Exception e) {
            // System.out.println("query9 not executed");
            // System.out.println(e);
            System.out.println("Something Went Wrong\nPlease Try Again");
        }
        setUrl(this.p_id, con);

    }

    public void updateProduct(Connection con) throws SQLException {
        try {
            System.out.println("ENter product ID:");
            int upid = sc.nextInt();
            System.out.println("PRESS 1 FOR UPDATE PRODUCTNAME ");
            System.out.println("PRESS 2 FOR UPDATE BRANDNAME ");
            System.out.println("PRESS 3 FOR UPDATE PRICE ");
            System.out.println("PRESS 4 FOR STOCK ");
            System.out.println("Enter a choice for update :");
            int updatechoice = sc.nextInt();
            switch (updatechoice) {
                case 1:
                    String namequery = "UPDATE products SET product_name = ? WHERE product_id = ?";
                    PreparedStatement pstn = con.prepareStatement(namequery);
                    System.out.println("ENTER UPDATED NAME ");
                    sc.nextLine();
                    pstn.setString(1, sc.nextLine());
                    pstn.setInt(2, upid);
                    pstn.executeUpdate();
                    pstn.close();
                    break;

                case 2:
                    String brandquery = "UPDATE products SET brand_name = ? WHERE product_id = ?";
                    PreparedStatement pstb = con.prepareStatement(brandquery);
                    System.out.println("ENter your updated brand name ");
                    sc.nextLine();
                    pstb.setString(1, sc.nextLine());
                    pstb.setInt(2, upid);
                    pstb.executeUpdate();
                    // System.out.println(((pstb.executeUpdate()) > 0) ? "updated " : "failed");
                    pstb.close();
                    break;

                case 3:
                    String pricequery = "UPDATE products SET price = ? WHERE product_id = ?";
                    PreparedStatement pstp = con.prepareStatement(pricequery);
                    System.out.println("ENter updated price ");
                    sc.nextLine();
                    pstp.setString(1, sc.nextLine());
                    pstp.setInt(2, upid);
                    pstp.executeUpdate();
                    // System.out.println(((pstp.executeUpdate()) > 0) ? "updated " : "failed");
                    pstp.close();
                    break;

                case 4:
                    String stockquery = "UPDATE products SET stock = ? WHERE product_id = ?";
                    PreparedStatement psts = con.prepareStatement(stockquery);
                    System.out.println("ENter updated stock count ");
                    int stock4 = sc.nextInt();
                    psts.setInt(1, stock4);
                    psts.setInt(2, upid);
                    psts.executeUpdate();
                    psts.close();
                    break;

                default:
                    System.out.println("PLEASE ENTER VALID CHOICE");
                    App.promptToContinue();
                    break;
            }

        } catch (Exception e) {
            System.out.println("PLEASE TRY AGAIN");
            App.promptToContinue();
            return;
        }
    }

    public void deleteProduct(Connection con) throws SQLException {
        try {

            System.out.print("Enter product id :");
            int ap_id = sc.nextInt();
            String deletequery = "DELETE FROM products WHERE product_id = ?";
            PreparedStatement pstd = con.prepareStatement(deletequery);
            pstd.setInt(1, ap_id);
            int r = pstd.executeUpdate();
            // System.out.println((r > 0) ? "deleted successfully" : "deletion failed ");
        } catch (Exception e) {
            System.out.println("PLEASE DO AGAIN>>");
            deleteProduct(con);
        }
    }

    public void displayProduct(Connection con) throws SQLException {
        try {
            String selectQuery = "SELECT * FROM products";
            PreparedStatement psts = con.prepareStatement(selectQuery);
            ResultSet rss = psts.executeQuery();
            while (rss.next()) {
                System.out.println("------------------------------");
                System.out.println("Product id : " + rss.getInt(1));
                System.out.println("Store id: " + rss.getInt(2));
                System.out.println("Product name : " + rss.getString(3));
                System.out.println("Brand name : " + rss.getString(4));
                System.out.println("price : " + rss.getDouble(5));
                System.out.println("Stock: " + rss.getInt(6));
                System.out.println("------------------------------");

            }

        } catch (Exception e) {
            System.out.println("DO AGAIN >>>...");
            displayProduct(con);
        }
    }

    public void deductStock(int productID, int nbrOfProducts, Connection con) throws SQLException {
        // System.out.println(productID);
        int currentStock = getCurrentStock(productID, con);
        int updatedStock = currentStock - nbrOfProducts;
        String q = "UPDATE products SET stock = ? WHERE product_id = ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setInt(1, updatedStock);
        pst.setInt(2, productID);
        int r = pst.executeUpdate();
        // System.out.println((r > 0) ? "Stock minus minus" : "Stock minus minus
        // failed");
    }

    public int getCurrentStock(int productID, Connection con) throws SQLException {
        String q = "SELECT stock FROM products WHERE product_id = ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setInt(1, productID);
        ResultSet rs = pst.executeQuery();
        int currentStock = 0;
        while (rs.next()) {
            currentStock = rs.getInt(1);
        }
        return currentStock;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setUrl(int id_url, Connection con) throws SQLException {
        try {
            String path = "C://SEM-II_Digital_Mall//src//";
            System.out.println("Enter file name with .jpg/.png : ");
            String fullpathname = sc.nextLine().trim();
            fullpathname = path.concat(fullpathname);
            File f = new File(fullpathname);
            FileInputStream fis1 = new FileInputStream(f);
            String query8 = "UPDATE products SET image_url = ? WHERE product_id = ?";
            PreparedStatement pst8 = con.prepareStatement(query8);
            pst8.setBinaryStream(1, fis1);
            pst8.setInt(2, id_url);
            int r8 = pst8.executeUpdate();
            // System.out.println((r8 > 0) ? "successfully picture entered !!" : "failed to
            // store!!");
        } catch (Exception e) {
            System.out.println("PLease try again!!.");
            System.out.println(e);
            setUrl(id_url, con);
        }
    }

    public boolean getRemainingStock(int pid, int quantity, Connection con) throws SQLException {

        String query = "{call getStockAmount(?,?)}";
        CallableStatement cst = con.prepareCall(query);
        cst.setInt(1, pid);
        cst.executeQuery();
        int remainingStock = cst.getInt(2);
        if (remainingStock >= quantity) {
            return true;
        } else {
            return false;
        }
    }

    public void displayProductImage(int productId, Connection con) throws SQLException, IOException {
        String query = "SELECT image_url from products where product_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, productId);
        ResultSet rs = pst.executeQuery();
        File f = new File("C:\\SEM-II_Digital_Mall\\PICS\\pic.png");
        f.createNewFile();
        while (rs.next()) {
            // System.out.println("INSIDE RS");
            Blob b = rs.getBlob("image_url");
            byte[] arr = b.getBytes(1, (int) b.length());
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(arr);
            fos.close();
        }
        String[] cmd = { "cmd.exe", "/c", "start", f.getAbsolutePath() };
        Runtime r = Runtime.getRuntime();
        r.exec(cmd);
    }
}

class stores {
    int s_id;
    String s_name;
    String category;
    int count = 1;
    LinkedList<stores> stores = new LinkedList<stores>();
    Scanner sc = new Scanner(System.in);

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void addStore(Connection con) throws SQLException {

        System.out.print("Enter Store ID: ");
        this.s_id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Store name :");
        this.s_name = sc.nextLine();
        System.out.print("Enter Category: ");
        this.category = sc.nextLine();
        this.stores.add(this); // adds store object to linkedList////

        String query3 = "INSERT INTO stores(store_id,store_name,category) VALUES (?,?,?)";
        PreparedStatement pst1 = con.prepareStatement(query3);
        try {
            pst1.setInt(1, this.s_id);
            pst1.setString(2, this.s_name);
            pst1.setString(3, this.category);
            int r = pst1.executeUpdate();
            // System.out.println((r > 0) ? "Insertion Success" : "Insertion Failed");
        } catch (Exception e) {
            System.out.println("SOMETHING went wrong!..");
            System.out.println(e);
            addStore(con);
        }
    }

    public void displayStore(Connection con) throws SQLException {
        String query6 = "{call displayStores()}";
        Statement st6 = con.createStatement();
        try {
            ResultSet rs6 = st6.executeQuery(query6);
            while (rs6.next()) {
                System.out.print("Store_id: " + rs6.getInt(1) + " --> ");
                System.out.print("Store_name: " + rs6.getString(2) + " --> ");
                System.out.println("Store_category: " + rs6.getString(3) + " --> ");
                System.out.println();

            }
        } catch (Exception e) {
            System.out.println("Something went wrong !..");
        }
    }

    public void updateStore(Connection con) throws SQLException {
        System.out.print("Enter name of store you want to update: ");
        String store_name = sc.nextLine();
        System.out.print("Enter updated category: ");
        String new_category = sc.nextLine();
        String query4 = "UPDATE stores SET category = ? WHERE store_name = ?";
        PreparedStatement pst2 = con.prepareStatement(query4);
        pst2.setString(2, store_name);
        pst2.setString(1, new_category);
        try {
            int r = pst2.executeUpdate();
            // System.out.println((r > 0) ? "sucessfully updated!!" : "failed to update!!");
        } catch (Exception e) {
            System.out.println("PLEASE try again!!");
            updateStore(con);
        }
    }

    public void deleteStore(Connection con) throws SQLException {
        System.out.print("Enter name of store you want to delete : ");
        String store_name = sc.nextLine();
        String query5 = "{call deleteStore(?)}";
        CallableStatement cst2 = con.prepareCall(query5);
        cst2.setString(1, store_name);
        try {
            int r2 = cst2.executeUpdate();
            // System.out.println((r2 > 0) ? "sucessfully deleted!!" : "failed to
            // delete!!");
        } catch (Exception e) {
            System.out.println("PLEASE try again!!..");
            deleteStore(con);
        }
    }
}

class users {
    int u_id;
    String u_name;
    String email;
    String password;
    String phone_nbr;
    String address;
    Connection con;

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_nbr() {
        return phone_nbr;
    }

    public void setPhone_nbr(String phone_nbr) {
        this.phone_nbr = phone_nbr;
    }

    void register(Connection con) throws SQLException {

        System.out.println("PLEASE REGISTER YOUR DETAILS FOR PROCESSING...");
        try {
            @SuppressWarnings({ "resource" })
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter name :");
            // sc.nextLine();
            u_name = sc.nextLine().trim();
            while (true) {
                System.out.print("Enter your Mail ID: ");
                email = sc.nextLine().trim();
                if (email.endsWith("@gmail.com")) {
                    break;
                } else {
                    System.out.println("Invalid email. Please enter an email that ends with '@gmail.com'.");
                }
            }

            while (true) {
                System.out.print("Enter password: ");
                password = sc.nextLine().trim();

                boolean hasNumber = false;
                boolean hasSpecialChar = false;

                for (char ch : password.toCharArray()) {
                    if (Character.isDigit(ch)) {
                        hasNumber = true;
                    } else if (!Character.isLetterOrDigit(ch)) {
                        hasSpecialChar = true;
                    }

                    // If both conditions are met, we can break early
                    if (hasNumber && hasSpecialChar) {
                        break;
                    }
                }

                if (hasNumber && hasSpecialChar) {
                    System.out.println("Password is valid!");
                    break;
                } else {
                    System.out.println(
                            "Password must contain at least one number and one special character. Please try again.");
                }
            }

            String q = "{call checkPresenceForRegister(?,?)}";
            CallableStatement cst = con.prepareCall(q);
            cst.setString(1, email);
            cst.executeQuery();
            int r = cst.getInt(2);
            // System.out.println(r);
            if (r > 0) {
                System.out.println("---User Already Exists with that email address----");
                register(con);
            }
            boolean b = false;
            while (b == false) {
                System.out.print("Enter phone number {10digits : xxxxxxxxxx}: ");
                try {
                    phone_nbr = sc.next().trim();
                    if (phone_nbr.matches("[0-9]+") && phone_nbr.length() == 10) {
                        b = true;
                    } else {
                        b = false;
                        System.out.println("phoone_number must contain  10 digits ONLY!..");
                    }
                } catch (Exception e) {
                    System.out.println("PLEASE ENTER your PHONE_number AGAIN!..");
                    // System.out.println(e);
                    b = false;
                }
            }
            System.out.print("Enter address: ");
            sc.nextLine();
            address = sc.nextLine().trim();
            String query1 = "INSERT INTO Users(user_name,email,password1,phone_number,address) VALUES(?,?,?,?,?)";
            PreparedStatement prt = con.prepareStatement(query1);
            // prt.setInt(1, u_id);
            prt.setString(1, u_name);
            prt.setString(2, email);
            prt.setString(3, password);
            prt.setString(4, phone_nbr);
            prt.setString(5, address);
            int r2 = prt.executeUpdate();
            // System.out.println((r2 > 0) ? "insertion success" : "insertion failed");

        } catch (Exception e) {
            System.out.println("Something Went Wrong..");
            // System.out.println(e);
            // e.printStackTrace();
            App.promptToContinue();
        }
    }

    public boolean login(String email, linkedlist ll, Connection con) throws Exception {
        try {
            @SuppressWarnings("resource")
            Scanner sc = new Scanner(System.in);
            // sc.nextLine();
            // System.out.println(email);

            System.out.print("Enter your Password: ");
            String password = sc.nextLine().trim();
            // System.out.println(password);
            String q7 = "{call verifyPassword(?,?)}";
            CallableStatement cst9 = con.prepareCall(q7);
            cst9.setString(1, email);
            cst9.executeQuery();
            String s = cst9.getString(2);
            // System.out.println(s);
            if (s != null) {
                if (s.equals(password)) {
                    System.out.println("LOGIN SUCCESSFULLY ");
                    System.out.println("PROCEED TO SEE WHAT you want to order FROM the MENUITEMS AND RATINGS :)");
                    System.out.println("Here are some best results you may like...>>");
                    ll.addLog(email, 0, 0, "LOGIN");
                    return true;
                } else {
                    System.out.println("LOGIN FAILED");
                    System.out.println("If you want to login do this process agin in a correct way");
                    return false;
                }
            } else {
                System.out.println("Please Enter Valid Credentials...");
                App.promptToContinue();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}

public class App {
    static users u = new users();
    static products p = new products();
    static stores st = new stores();
    static orders o = new orders();
    static order_items oit = new order_items();
    static Scanner sc = new Scanner(System.in);
    static Connection con;
    static linkedlist ll = new linkedlist();
    private static boolean running = true;

    public static void main(String[] args) throws SQLException {

        Thread statusChecking = new Thread(() -> updateOrderStatus());

        try {

            statusChecking.start();
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/digital_mall", "root", "");

            System.out.println("------------------------");
            System.out.println("------------------------");
            System.out.println("------------------------");
            System.out.println("WELCOME   TO  DIGITAL MALL");
            System.out.println("------------------------");
            System.out.println("------------------------");
            System.out.println();
            while (true) {
                System.out.println("PRESS 1 TO REGISTER");
                System.out.println("PRESS 2 FOR LOGIN--->");
                System.out.println("PRESS 3 FOR ADMINLOGIN--->");
                System.out.println("PRESS 0 FOR EXIT");
                System.out.println();
                // System.out.print("Please Enter Your Choice: ");
                String email = "";
                // int choice = sc.nextInt();
                // sc.nextLine();
                int choice = 0;
                boolean check1 = false;
                while (!check1) {
                    System.out.print("PLEASE ENTER YOUR CHOICE: ");
                    if (sc.hasNextInt()) {
                        choice = sc.nextInt();
                        sc.nextLine();
                        check1 = true;
                    } else {
                        System.out.println("Invalid Input. Please Enter a Number.");
                        sc.next();
                    }
                }
                switch (choice) {
                    case 1:
                        System.out.println("REGISTER LOADING>>>...");
                        u.register(con);
                        System.out.println("REGISTERED SUCCESFULLY>>..");
                        System.out.println("DO THE LOGIN PROCESS TO SHOP/VIEWMORE>>");
                        break;
                    case 2:
                        System.out.println("LOGIN LOADING>>>...");
                        // System.out.print("Enter your Mail to Login: ");

                        boolean b2 = false;
                        while (b2 == false) {
                            System.out.print("Enter your Mail to Login: ");
                            email = sc.nextLine().trim();
                            b2 = u.login(email, ll, con);
                        }
                        System.out.println("YOU LOGGED IN>>>>...");

                        boolean b = true;
                        while (b == true) {
                            System.out.println();
                            System.out.println("PRESS 1 FOR STORE DETAILS LIST");
                            System.out.println("PRESS 2 FOR SHOP BY STORE");
                            System.out.println("PRESS 3 FOR SHOP BY PRODUCT NAME");
                            System.out.println("PRESS 4 FOR TO TRACK ORDERS");
                            System.out.println("PRESS 5 FOR DISPLAY HISTORY");
                            System.out.println("PRESS 6 TO CANCEL ORDER");
                            System.out.println("PRESS 7 FOR GO BACK TO LOGIN PAGE");
                            System.out.println("PRESS 0 FOR EXITING MALL");
                            // System.out.print("ENTER YOUR CHOICE: ");

                            // int choice1 = sc.nextInt();
                            int choice1 = 0;
                            boolean check22 = false;
                            while (!check22) {
                                System.out.print("PLEASE ENTER YOUR CHOICE: ");
                                if (sc.hasNextInt()) {
                                    choice1 = sc.nextInt();
                                    check22 = true;
                                } else {
                                    System.out.println("Invalid Input. Please Enter a Number.");
                                    sc.next();
                                }
                            }
                            switch (choice1) {
                                case 1:
                                    // select * from store
                                    System.out.println("");
                                    Statement st26 = con.createStatement();
                                    String q26 = "SELECT * FROM stores";
                                    ResultSet rs26 = st26.executeQuery(q26);
                                    System.out.println("STOREID ||   STORE_NAME ||   STORE+CATEGORY ||");
                                    System.out.println("-----------------------------------------------");
                                    while (rs26.next()) {
                                        System.out.print(" " + rs26.getInt(1) + " ---->>");
                                        System.out.print(" " + rs26.getString(2) + " ---->>");
                                        System.out.println(" " + rs26.getString(3) + " ");
                                    }
                                    System.out.println("-----------------------------------------------");
                                    break;

                                case 2:
                                    selectProductBYstorename(email, con);
                                    break;
                                case 3:
                                    selectProduct(email, con);
                                    break;

                                case 4:
                                    String q23 = "{call getUserIdbyEmail(?,?)}";
                                    CallableStatement cstt = con.prepareCall(q23);
                                    cstt.setString(1, email);
                                    cstt.executeQuery();
                                    trackOrder(cstt.getInt(2), con);

                                    break;

                                case 5:
                                    String q1 = "{call getUserIdbyEmail(?,?)}";
                                    CallableStatement cst1 = con.prepareCall(q1);
                                    cst1.setString(1, email);
                                    cst1.executeQuery();
                                    displayHistory(cst1.getInt(2), con);

                                    break;

                                case 6:
                                    o.cancelOrder(email, con);
                                    break;

                                case 7:
                                    b = false;
                                    ll.addLog(email, 0, 0, "LOGOUT");
                                    break;

                                case 0:
                                    System.out.println("THANK YOU FOR SHOPPING AND VISITING !!..");
                                    ll.deleteFront();
                                    System.exit(0);
                                    break;

                                default:
                                    System.out.println("PLEASE ENTER CORRECT CHOICE");
                                    break;
                            }
                        }
                        break;

                    case 3:
                        System.out.println("ADMIN LOGIN>>..");
                        String emaill = "";
                        boolean b3 = false;
                        while (b3 == false) {
                            System.out.print("Enter your Mail to Login: ");
                            emaill = sc.nextLine().trim();
                            b3 = u.login(emaill, ll, con);
                        }
                        boolean adminkey = true;
                        while (adminkey) {

                            System.out.println("PRESS 1 FOR ADDSTORE");
                            System.out.println("PRESS 2 FOR UPDATESTORE");
                            System.out.println("PRESS 3 FOR REMOVESTORE");
                            System.out.println("PRESS 4 FOR VIEWSTOREs");
                            System.out.println("PRESS 5 FOR ADDPRODUCT");
                            System.out.println("PRESS 6 FOR UPDATEPRODUCT");
                            System.out.println("PRESS 7 FOR DELETEPRODUCT");
                            System.out.println("PRESS 8 FOR VIEWPRODUCTS ");
                            System.out.println("PRESS 9 FOR VIEWLOGS ");
                            System.out.println("PRESS 0 FOR EXIT ADMIN PANEL");
                            System.out.println("ENter your choice : ");
                            int admissionchoice = 0;
                            boolean check23 = false;
                            while (!check23) {
                                System.out.print("PLEASE ENTER YOUR CHOICE: ");
                                if (sc.hasNextInt()) {
                                    admissionchoice = sc.nextInt();
                                    check23 = true;
                                } else {
                                    System.out.println("Invalid Input. Please Enter a Number.");
                                    sc.next();
                                }
                            }
                            // sc.nextLine();
                            switch (admissionchoice) {
                                case 1:
                                    st.addStore(con);
                                    break;
                                case 2:
                                    st.updateStore(con);
                                    break;
                                case 3:
                                    st.deleteStore(con);
                                    break;
                                case 4:
                                    st.displayStore(con);
                                    break;
                                case 5:
                                    p.addProducts(con);
                                    break;
                                case 6:
                                    p.updateProduct(con);
                                    break;
                                case 7:
                                    p.deleteProduct(con);
                                    break;
                                case 8:
                                    p.displayProduct(con);
                                    break;
                                case 9:
                                    File f = new File("C:\\SEM-II_Digital_Mall");
                                    System.out.println("LOGS DOWNLOADED \nPLEASE VIEW AT " + f.getPath());
                                    promptToContinue();
                                    break;
                                case 0:
                                    System.out.println("Exiting admin panel>>...");
                                    adminkey = false;
                                    break;

                                default:
                                    System.out.println("Enter valid choice :");
                                    break;
                            }

                        }
                        break;
                    case 0:
                        System.out.println("---------------------------------");
                        System.out.println("THANK YOU FOR VISITING>>....");
                        System.out.println("---------------------------------");
                        ll.addLog(email, 0, 0, "LOGOUT");
                        ll.deleteFront();
                        System.exit(0);
                        break;
                    default:
                        System.out.println(" OOPS !!..ENTER A VALID NUMBER CHOICE!! :)");
                        break;
                }
            }
        } catch (Exception e) {
            // System.out.println(e);
            e.printStackTrace();
            System.out.println("DANGER!.....");
            System.out.println("Please Try Again");
            promptToContinue();
            sc.nextLine();
            main(args);

        }

        running = false;
        try {
            statusChecking.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void updateOrderStatus() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Digital_Mall", "root", "");
            // System.out.println("INSIDE MEOW");
            while (running) {
                String q = "SELECT order_date FROM orders WHERE order_status <> 'DELIVERED' AND order_status <> 'CANCELLED' ";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(q);
                while (rs.next()) {
                    String originalDate = rs.getString(1);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate oDate = LocalDate.parse(originalDate, dtf);
                    LocalDate cDate = LocalDate.now();
                    long daysDiff = ChronoUnit.DAYS.between(oDate, cDate);
                    // System.out.println("days diff: " + daysDiff);
                    if (daysDiff >= 2) {
                        // System.out.println("INSIDE IF");
                        String q1 = "UPDATE orders SET order_status = ? WHERE order_date = ? ";
                        PreparedStatement pst1 = con.prepareStatement(q1);
                        pst1.setString(1, "DELIVERED");
                        pst1.setString(2, originalDate);
                        pst1.executeUpdate();
                    }
                    Thread.sleep(10000);
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public static void displayHistory(int user_id, Connection con) throws SQLException {
        String historyquery = "{call displayHistory(?)}";

        CallableStatement cst = con.prepareCall(historyquery);
        cst.setInt(1, user_id);
        ResultSet rsh = cst.executeQuery();
        while (rsh.next()) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("Product Name: " + rsh.getString(1));
            System.out.println("Order Status: " + rsh.getString(2));
            System.out.println("Order Date: " + rsh.getString(3));
            System.out.println("Brand Name: " + rsh.getString(4));
            System.out.println("----------------------------------------------------------------");
            System.out.println();
            System.out.println();
        }

    }

    public static void trackOrder(int user_id, Connection con) throws SQLException {
        String trackquery = "{call trackOrder(?)}";
        CallableStatement cstt = con.prepareCall(trackquery);
        cstt.setInt(1, user_id);
        ResultSet rst = cstt.executeQuery();
        while (rst.next()) {

            System.out.println("---------------------------------------------");
            System.out.println("ProductName: " + rst.getString(1));
            System.out.println("OrderStatus: " + rst.getString(2));
            System.out.println("OrderDate: " + rst.getString(3));
            System.out.println("---------------------------------------------");
        }

    }

    public static void selectProduct(String mail, Connection con) {
        try {
            orders order = new orders();
            linkedList2 order_items = new linkedList2();

            System.out.println("HERE ARE SOME RESULTS YOU MAY LIKE!!..");
            Statement st10 = con.createStatement();
            String query10 = "SELECT * FROM products";
            ResultSet rs10 = st10.executeQuery(query10);
            System.out.printf("+-----------+----------------------+----------------------+-----------+\n");
            System.out.printf("| %-9s | %-20s | %-20s | %-9s |\n", "PRODUCTID", "PRODUCT_NAME", "BRAND_NAME",
                    "PRICE");
            System.out.printf("+-----------+----------------------+----------------------+-----------+\n");

            // Table Rows
            while (rs10.next()) {
                String bName = rs10.getString(4);
                double price = rs10.getDouble(5);
                System.out.printf("| %-9d | %-20s | %-20s | %-9.2f |\n",
                        rs10.getInt(1),
                        rs10.getString(3),
                        // rs10.getString(3),
                        bName,
                        price);
            }

            // Table Footer
            System.out.printf("+-----------+----------------------+----------------------+-----------+\n");

            // System.out.print("Enter Product ID to View Photo: ");
            // int pid = sc.nextInt();
            // p.displayProductImage(pid, con);
            // promptToContinue();
            boolean loop = true;
            while (loop) {
                System.out.println();
                System.out.println("PRESS 1 TO VIEW PRODUCT IMAGE");
                System.out.println("PRESS 2 TO CONTINUE FURTHER");
                System.out.println("PRESS 0 TO GO BACK");
                // System.out.print("Enter Your Choice: ");
                int c = 0;
                boolean check23 = false;
                while (!check23) {
                    System.out.print("PLEASE ENTER YOUR CHOICE: ");
                    if (sc.hasNextInt()) {
                        c = sc.nextInt();
                        sc.nextLine();
                        check23 = true;
                    } else {
                        System.out.println("Invalid Input. Please Enter a Number.");
                        sc.next();
                    }
                }
                switch (c) {

                    case 1:
                        System.out.print("Enter Product ID to View Photo: ");
                        int pid = sc.nextInt();
                        p.displayProductImage(pid, con);
                        promptToContinue();
                        break;

                    case 2:
                        loop = false;
                        promptToContinue();
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println("Please Enter Correct Choice");
                        promptToContinue();
                        break;
                }
            }
            System.out.println();
            System.out.println();

            System.out.println("Enter Product id to addtocart!!");
            int selection = 0;
            boolean check2345 = false;
            while (!check2345) {
                System.out.print("Enter Product ID: ");
                // System.out.print("PLEASE ENTER YOUR CHOICE: ");
                if (sc.hasNextInt()) {
                    selection = sc.nextInt();
                    sc.nextLine();
                    check2345 = true;
                } else {
                    System.out.println("Invalid Input. Please Enter a Number.");
                    sc.next();
                }
            }
            int qauntity = 0;
            boolean check2673 = false;
            while (!check2673) {
                // System.out.print("PLEASE ENTER YOUR CHOICE: ");
                System.out.println("Enter how many you want to buy: ");
                if (sc.hasNextInt()) {
                    qauntity = sc.nextInt();
                    sc.nextLine();
                    check2673 = true;
                } else {
                    System.out.println("Invalid Input. Please Enter a Number.");
                    sc.next();
                }
            }
            boolean available2 = p.getRemainingStock(selection, qauntity, con);
            if (available2) {
                addOrder(mail, selection, qauntity, con, order);
                addOrderItem(selection, qauntity, mail, con, order_items);
                sc.nextLine();
                System.out.println("DO you Want to add more item!!...");
                String userchoice = sc.nextLine();
                while (userchoice.equalsIgnoreCase("yes") || userchoice.equalsIgnoreCase("true")) {

                    System.out.println("HERE ARE SOME RESULTS YOU MAY LIKE!!..");
                    Statement st11 = con.createStatement();
                    String query11 = "SELECT * FROM products";
                    ResultSet rs11 = st11.executeQuery(query11);
                    System.out.printf("+-----------+----------------------+----------------------+-----------+\n");
                    System.out.printf("| %-9s | %-20s | %-20s | %-9s |\n", "PRODUCTID", "PRODUCT_NAME", "BRAND_NAME",
                            "PRICE");
                    System.out.printf("+-----------+----------------------+----------------------+-----------+\n");

                    // Table Rows
                    while (rs11.next()) {
                        String bName = rs11.getString(4);
                        System.out.printf("| %-9d | %-20s | %-20s | %-9.2f |\n",
                                rs11.getInt(1),
                                rs11.getString(3),
                                bName,
                                rs11.getDouble(5));
                    }

                    // Table Footer
                    System.out.printf("+-----------+----------------------+----------------------+-----------+\n");
                    System.out.println("Enter Product id to addtocart!!");
                    selection = 0;
                    boolean check23 = false;
                    while (!check23) {
                        System.out.print("Enter Product ID: ");
                        // System.out.print("PLEASE ENTER YOUR CHOICE: ");
                        if (sc.hasNextInt()) {
                            selection = sc.nextInt();
                            sc.nextLine();
                            check23 = true;
                        } else {
                            System.out.println("Invalid Input. Please Enter a Number.");
                            sc.next();
                        }
                    }
                    qauntity = 0;

                    boolean check233 = false;
                    while (!check233) {
                        // System.out.print("PLEASE ENTER YOUR CHOICE: ");
                        System.out.println("Enter how many you want to buy: ");
                        if (sc.hasNextInt()) {
                            qauntity = sc.nextInt();
                            sc.nextLine();
                            check233 = true;
                        } else {
                            System.out.println("Invalid Input. Please Enter a Number.");
                            sc.next();
                        }
                    }
                    sc.nextLine();
                    boolean available3 = p.getRemainingStock(selection, qauntity, con);
                    if (available3) {
                        addOrderItem(selection, qauntity, mail, con, order_items);
                        System.out.print("DO you Want to add more item!!...");
                        userchoice = sc.nextLine();
                        System.out.println();
                    } else {
                        System.out
                                .println("Not Enough Stock Available Please choose another product or update qauntity");
                        promptToContinue();
                        System.out.print("DO you Want to add more item!!...");
                        userchoice = sc.nextLine();
                        System.out.println();
                        System.out.println();

                    }

                }

                generatebills(mail, con, order_items, order);
            } else {
                System.out.println("Not Enough Stock Available Please choose another product or update qauntity");
                promptToContinue();
                System.out.println();
                System.out.println();
                selectProduct(mail, con);
            }

        } catch (Exception e) {
            System.out.println("SOMETHING WENT WRONG !!......>>>>");
            // System.out.println(e);
            e.printStackTrace();
            selectProduct(mail, con);
        }
    }

    public static void selectProductBYstorename(String mail, Connection con) throws SQLException, IOException {

        linkedList2 order_items = new linkedList2();
        orders order = new orders();

        Statement st27 = con.createStatement();
        String q27 = "SELECT * FROM stores";
        ResultSet rs27 = st27.executeQuery(q27);
        System.out.printf("+----------+----------------------+----------------------+\n");
        System.out.printf("| %-8s | %-20s | %-20s |\n", "STOREID", "STORE_NAME", "STORE_CATEGORY");
        System.out.printf("+----------+----------------------+----------------------+\n");

        // Table Rows
        while (rs27.next()) {
            System.out.printf("| %-8d | %-20s | %-20s |\n",
                    rs27.getInt(1),
                    rs27.getString(2),
                    rs27.getString(3));
        }

        // Table Footer
        System.out.printf("+----------+----------------------+----------------------+\n");
        System.out.println();
        sc.nextLine();
        System.out.println("Enter store name to view its products ");
        String sname = sc.nextLine().trim();
        String query7 = "{call getStoreId(?,?)}";
        CallableStatement cst7 = con.prepareCall(query7);
        cst7.setString(1, sname);
        cst7.executeQuery();
        int sid = cst7.getInt(2);
        while (sid == 0) {
            System.out.println("INVALID STORE NAME\nPLS TRY AGAIN");
            System.out.println("Enter store name to view its products ");
            sname = sc.nextLine().trim();
            String query8 = "{call getStoreId(?,?)}";
            CallableStatement cst8 = con.prepareCall(query8);
            cst8.setString(1, sname);
            cst8.executeQuery();
            sid = cst8.getInt(2);
        }
        // System.out.println(sid);

        String query9 = "SELECT product_id , product_name , brand_name , price FROM products WHERE store_id = ? ";
        PreparedStatement pst9 = con.prepareStatement(query9);
        pst9.setInt(1, sid);
        ResultSet rs9 = pst9.executeQuery();

        // Table Header
        System.out.printf("+-----------+----------------------+----------------------+-----------+\n");
        System.out.printf("| %-9s | %-20s | %-20s | %-9s |\n", "PRODUCTID", "PRODUCT_NAME", "BRAND_NAME", "PRICE");
        System.out.printf("+-----------+----------------------+----------------------+-----------+\n");

        // Table Rows
        while (rs9.next()) {
            String bName = rs9.getString(3);
            System.out.printf("| %-9d | %-20s | %-20s | %-9.2f |\n",
                    rs9.getInt(1),
                    rs9.getString(2),
                    // rs9.getString(3),
                    bName,
                    rs9.getDouble(4));
        }

        // Table Footer
        System.out.printf("+-----------+----------------------+----------------------+-----------+\n");

        // System.out.print("Enter Product ID to View Photo: ");
        // int pid = sc.nextInt();
        // p.displayProductImage(pid, con);
        // promptToContinue();
        boolean loop = true;
        while (loop) {
            System.out.println("PRESS 1 TO VIEW IMAGE");
            System.out.println("PRESS 2 TO CONTINUE FURTHER");
            System.out.println("PRESS 0 TO GO BACK");
            // System.out.print("Enter Your Choice: ");
            int c = 0;
            boolean check23 = false;
            while (!check23) {
                System.out.print("PLEASE ENTER YOUR CHOICE: ");
                if (sc.hasNextInt()) {
                    c = sc.nextInt();
                    sc.nextLine();
                    check23 = true;
                } else {
                    System.out.println("Invalid Input. Please Enter a Number.");
                    sc.next();
                }
            }
            switch (c) {

                case 1:
                    System.out.print("Enter Product ID to View Photo: ");
                    int pid = sc.nextInt();
                    p.displayProductImage(pid, con);
                    promptToContinue();
                    break;

                case 2:
                    loop = false;
                    promptToContinue();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Please Enter Correct Choice");
                    promptToContinue();
                    break;
            }
        }

        System.out.println("Enter Product id to addtocart!!");
        int selection = 0;
        boolean check234 = false;
        while (!check234) {
            System.out.print("Enter Product ID: ");
            // System.out.print("PLEASE ENTER YOUR CHOICE: ");
            if (sc.hasNextInt()) {
                selection = sc.nextInt();
                sc.nextLine();
                check234 = true;
            } else {
                System.out.println("Invalid Input. Please Enter a Number.");
                sc.next();
            }
        }
        int qauntity = 0;
        boolean check2345 = false;
        while (!check2345) {
            System.out.println("Enter how many you want to buy: ");
            // System.out.print("PLEASE ENTER YOUR CHOICE: ");
            if (sc.hasNextInt()) {
                qauntity = sc.nextInt();
                sc.nextLine();
                check2345 = true;
            } else {
                System.out.println("Invalid Input. Please Enter a Number.");
                sc.next();
            }
        }
        boolean available = p.getRemainingStock(selection, qauntity, con);
        if (available) {
            addOrder(mail, selection, qauntity, con, order);
            addOrderItem(selection, qauntity, mail, con, order_items);

            sc.nextLine();
            System.out.println("DO you Want to add more item!!...");

            String userchoice = sc.nextLine();
            while (userchoice.equalsIgnoreCase("yes") || userchoice.equalsIgnoreCase("true")) {
                String query10 = "SELECT product_id , product_name , brand_name , price FROM products WHERE store_id = ? ";
                PreparedStatement pst10 = con.prepareStatement(query10);
                pst10.setInt(1, sid);
                ResultSet rs10 = pst9.executeQuery();
                System.out.printf("+-----------+----------------------+----------------------+-----------+\n");
                System.out.printf("| %-9s | %-20s | %-20s | %-9s |\n", "PRODUCTID", "PRODUCT_NAME", "BRAND_NAME",
                        "PRICE");
                System.out.printf("+-----------+----------------------+----------------------+-----------+\n");

                // Table Rows
                while (rs10.next()) {
                    String bName = rs10.getString(3);
                    System.out.printf("| %-9d | %-20s | %-20s | %-9.2f |\n",
                            rs10.getInt(1),
                            rs10.getString(2),
                            // rs10.getString(3),
                            bName,
                            rs10.getDouble(4));
                }

                // Table Footer
                System.out.printf("+-----------+----------------------+----------------------+-----------+\n");

                System.out.println("Enter Product id to addtocart!!");
                selection = 0;
                boolean check23 = false;
                while (!check23) {
                    System.out.print("Enter Product ID: ");
                    // System.out.print("PLEASE ENTER YOUR CHOICE: ");
                    if (sc.hasNextInt()) {
                        selection = sc.nextInt();
                        sc.nextLine();
                        check23 = true;
                    } else {
                        System.out.println("Invalid Input. Please Enter a Number.");
                        sc.next();
                    }
                }
                qauntity = 0;
                boolean check24 = false;
                while (!check24) {
                    // System.out.print("PLEASE ENTER YOUR CHOICE: ");
                    System.out.println("Enter how many you want to buy: ");
                    if (sc.hasNextInt()) {
                        qauntity = sc.nextInt();
                        sc.nextLine();
                        check24 = true;
                    } else {
                        System.out.println("Invalid Input. Please Enter a Number.");
                        sc.next();
                    }
                }
                boolean available1 = p.getRemainingStock(selection, qauntity, con);
                if (available1) {
                    addOrderItem(selection, qauntity, mail, con, order_items);

                    sc.nextLine();
                    System.out.println("DO you Want to add more item!!...");
                    userchoice = sc.nextLine().trim();
                    System.out.println();
                    System.out.println();
                } else {
                    System.out.println("Not Enough Stock Available Please choose another product or update qauntity");
                    promptToContinue();
                    System.out.println();
                    System.out.println();
                    System.out.println("DO you Want to add more item!!...");
                    userchoice = sc.nextLine().trim();
                }
            }

            generatebills(mail, con, order_items, order);
        } else {
            System.out.println("Not Enough Stock Available Please choose another product or update qauntity");
            promptToContinue();
            selectProductBYstorename(mail, con);
        }

    }

    public static void generatebills(String mail, Connection con, linkedList2 order_items,
            orders order1)
            throws SQLException, IOException {

        String q1 = "{call getUserIdbyEmail(?,?)}";
        CallableStatement cst1 = con.prepareCall(q1);
        cst1.setString(1, mail);
        cst1.executeQuery();
        int u_id1 = cst1.getInt(2);
        // System.out.println("user_id: " + u_id1);

        StringBuilder sb = new StringBuilder();
        sb.append(
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("SrNo            Name                    Quantity            Rate            TotalPrice\n");
        sb.append(
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        int count = 1;
        double totalAmount = 0;

        linkedList2.Node temp = order_items.Head;
        while (temp != null) {
            order_items oit = temp.o;
            int productID = oit.p_id;
            double totalPrice = oit.price;
            int quantity1 = oit.qauntity;
            double rate = 0.0;
            String pName = "";
            try {
                String query = "SELECT product_name , price FROM products WHERE product_id = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, productID);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    pName = rs.getString(1);
                    rate = rs.getDouble(2);
                }
            } catch (SQLException e) {
                // System.out.println();
                e.printStackTrace();
            }

            temp = temp.Next;

            sb.append(String.format(" %-9d %-15s %15d %20.2f %20.2f \n", count++, pName, quantity1, rate,
                    totalPrice));
            totalAmount += totalPrice;
        }

        sb.append(
                "-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        // sb.append("TOTAL AMOUNT TO PAY: " + totalAmount);
        System.out.println();
        System.out.println();
        System.out.print(sb.toString());
        // App.promptToContinue();
        // System.exit(0);
        boolean paid = payment(totalAmount, mail, con);
        if (paid) {

            String q22 = "INSERT INTO orders (user_id , store_id , order_date,total_cost , order_status) VALUES(?,?,?,?,?)";
            PreparedStatement pst22 = con.prepareStatement(q22, Statement.RETURN_GENERATED_KEYS);
            // pst22.setInt(1, o.o_id);
            pst22.setInt(1, order1.u_id);
            pst22.setInt(2, order1.s_id);
            pst22.setString(3, order1.order_date);
            pst22.setDouble(4, totalAmount);
            pst22.setString(5, "SHIPPED");
            int oID = 0;
            int r22 = pst22.executeUpdate();
            System.out.println((r22 > 0) ? "INSERTION SUCCESSFULLY " : "INSERTION FAILED");

            ResultSet rs = pst22.getGeneratedKeys();
            if (rs.next()) {
                oID = rs.getInt(1);

                linkedList2.Node temp1 = order_items.Head;
                while (temp1 != null) {
                    order_items oit1 = temp1.o;

                    String q25 = "INSERT INTO order_item (order_id , product_id , quantity ,price) VALUES (?,?,?,?)";
                    PreparedStatement pst25 = con.prepareStatement(q25);
                    // pst25.setInt(1, oit.o_item_id);
                    pst25.setInt(1, oID);
                    pst25.setInt(2, oit1.p_id);
                    pst25.setInt(3, oit1.qauntity);
                    pst25.setDouble(4, oit1.price);

                    pst25.executeUpdate();

                    p.deductStock(oit1.p_id, oit1.qauntity, con);
                    temp1 = temp1.Next;
                    // System.out.println((r25 > 0) ? "INSERTION SUCCESS" : "INSERTION FAILED");
                }

            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("billof" +
                    mail + ".txt"))) {
                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            // p.rollbackStock(productid, qauntity, con);
            // String query = "{call deleteOrder(?)}";
            // CallableStatement cst = con.prepareCall(query);
            // cst.setInt(1, orderid);
            // cst.executeQuery();
            System.out.println("ORDER CANCELLED");
        }
    }

    public static boolean payment(double amountToPay, String email, Connection con) throws IOException {
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        int ch22;
        do {
            System.out.println("Total Amount to Pay: " + amountToPay);
            System.out.println("Press 1 to Make Payment");
            System.out.println("Press 0 to Cancel");
            System.out.print("Enter Your Choice: ");
            ch22 = sc.nextInt();
            switch (ch22) {
                case 1:
                    System.out.println("Payment SuccessFull");
                    JFrame fr = new JFrame("ORDER SUCCESS");
                    fr.setAlwaysOnTop(true);
                    JOptionPane.showMessageDialog(fr, "ORDER PLACED SUCCESSFULLY");
                    fr.setVisible(true); // makes it so that the frame is visible
                    fr.toFront(); // top/front upar frame ne laine aave
                    fr.requestFocus();// focused to input
                    fr.dispose(); // deletes the frame
                    ll.addLog(email, 0, amountToPay, "PAID");
                    return true;
                // break;

                case 0:
                    System.out.println("CANCELLING...");
                    return false;
                // break;

                default:
                    System.out.println("Please Enter Valid Choice");
                    break;
            }
        } while (ch22 != 1 || ch22 != 2);
        return false;

    }

    public static void addOrderItem(int product_id, int q, String email, Connection con, linkedList2 order_items_list)
            throws SQLException {

        order_items oit1 = new order_items();

        String q1 = "{call getUserIdbyEmail(?,?)}";
        CallableStatement cst1 = con.prepareCall(q1);
        cst1.setString(1, email);
        cst1.executeQuery();
        int u_id1 = cst1.getInt(2);
        // System.out.println("user_id: " + u_id1);

        // String q23 = "{call getOrderId9090(?,?)}";
        // CallableStatement cst23 = con.prepareCall(q23);
        // cst23.setInt(1, u_id1);
        // cst23.executeQuery();
        // int orderid = cst23.getInt(2);
        // oit1.o_id = orderid;

        oit1.p_id = product_id;
        oit1.qauntity = q;

        String q24 = "{call getPriceByProductId(?,?)}";
        CallableStatement cst24 = con.prepareCall(q24);
        cst24.setInt(1, product_id);
        cst24.executeQuery();
        double p = cst24.getDouble(2);
        oit1.price = p * q;

        order_items_list.addToCart(oit1);

    }

    public static void addOrder(String email, int productid, int qauntity, Connection con, orders o1)
            throws SQLException {
        try {

            String q1 = "{call getUserIdbyEmail(?,?)}";
            CallableStatement cst1 = con.prepareCall(q1);
            cst1.setString(1, email);
            cst1.executeQuery();
            int u_id1 = cst1.getInt(2);
            // System.out.println("user_id: " + u_id1);

            // o.o_id = 1;
            o1.u_id = u_id1;

            String q2 = "{call getStoreIdByProductId(?,?)}";
            CallableStatement cst2 = con.prepareCall(q2);
            cst2.setInt(1, productid);
            cst2.executeQuery();
            int s_id1 = cst2.getInt(2);
            // System.out.println("store_id: " + s_id1);
            // linked list for adding ,maintaing the list
            o1.s_id = s_id1;
            LocalDate d = LocalDate.now();
            String date = d.toString();
            // System.out.println(date);
            o1.order_date = date;

        } catch (Exception e) {
            System.out.println("PLS TRY AGAIN...");
            e.printStackTrace();
            addOrder(email, productid, qauntity, con, o1);
        }
    }

    public static void promptToContinue() {
        @SuppressWarnings("resource")
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Press Enter to continue...");
        sc1.nextLine();
        System.out.println("Continuing...");
    }
}

class linkedlist {
    class Node {
        String log;
        Node Prev;
        Node Next;

        Node(String log) {
            this.log = log;
        }
    }

    Node Head;
    Node Tail;

    public void addLog(String email, int userid, double amount, String status) throws IOException {
        StringBuilder sb = new StringBuilder();
        LocalDate ld = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String cDate = ld.format(df);
        LocalTime lt = LocalTime.now();
        String cTime = lt.toString();
        String log = "";
        if (status.equals("LOGIN")) {
            // do design as per u
            sb.append("<" + cDate + " " + cTime + "> " + email + " LOGGED IN");
            log = sb.toString();
        } else if (status.equals("LOGOUT")) {
            sb.append("<" + cDate + " " + cTime + "> " + email + " LOGGED OUT");
            log = sb.toString();
        } else if (status.equals("PAID")) {
            sb.append("<" + cDate + " " + cTime + "> " + email + " PAID " + amount);
            log = sb.toString();
        }
        // System.out.println(log);
        insertLast(log);
    }

    public void insertLast(String log) {
        Node n = new Node(log);
        if (Head == null) {
            Head = n;
            Tail = n;
        } else {
            n.Prev = Tail;
            Tail.Next = n;
            Tail = n;
        }
    }

    public void deleteFront() throws IOException {
        if (Head == null) {
            // System.out.println("FAILED TO GENERATE LOGS");
        } else {
            LocalDate ld = LocalDate.now();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String cDate = ld.format(df);
            File f = new File("C:\\SEM-II_Digital_Mall\\LOGS -- " + cDate);
            f.createNewFile();
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            while (Head != Tail) {
                raf.seek(raf.length());
                raf.writeChars(Head.log + "\n");
                Head = Head.Next;
            }
            raf.writeChars("\n");
            raf.seek(raf.length());
            raf.writeChars(Head.log + "\n");
            raf.close();
        }
    }
}

class linkedList2 {
    class Node {
        order_items o;
        Node Next;

        public Node(order_items o) {
            this.o = o;
        }
    }

    Node Head;
    Node Tail;

    public void addToCart(order_items o) {
        Node n = new Node(o);
        if (Head == null) {
            Head = n;
            Tail = n;
        } else {
            Tail.Next = n;
            Tail = n;
        }

    }

}
