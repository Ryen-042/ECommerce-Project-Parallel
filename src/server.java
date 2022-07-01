import Connectivity.ConnectionClass;
import Connectivity.Session;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.*;  
import java.net.*;

import javax.print.DocFlavor.STRING;  
public class server {  
    
    static final int PORT = 1978;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new EchoThread(socket).start();
        }
    }
} 
class EchoThread extends Thread {
    protected Socket socket;
    Session session;
    
    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
        session = new Session(0);
    }

    public void run() {
        BufferedReader brinp = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            //brinp = new BufferedReader(new InputStreamReader(in));
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();
                Statement statement=connection.createStatement();
                statement=connection.createStatement();
                ResultSet resultSet;
                // Variables to be used
                String sql, username, password;
                int num, qnt, prodqnt, id, price;

                line = (String)in.readUTF();
                switch(line){
                    case "QUIT" :
                        socket.close();
                        return;
                    case "check log in" :
                        username = (String)in.readUTF();
                        password = (String)in.readUTF();
                        if (username.equals("admin") && password.equals("admin"))
                        {
                            out.writeUTF("admin");
                            out.flush();                            
                            break;
                        }
                        sql="SELECT * FROM customer WHERE username = '"+username+"' AND password = '"+password+"';";
                        resultSet=statement.executeQuery(sql);

                        if (resultSet.next())
                        {
//                            session.getSession() = resultSet.getInt("custId");
                            session.setSession(resultSet.getInt("custId"));
                            out.writeUTF("okc");
                            out.flush();
                        }
                        else{
                            out.writeUTF("noc");
                            out.flush();
                        }
                        break;
                    case "register":
                        String name = (String)in.readUTF();
                        username = (String)in.readUTF();
                        password = (String)in.readUTF();                        
                        String email = (String)in.readUTF();              

                        // Check dublicate username
                        sql = "SELECT * FROM customer WHERE username = '"+username+"';";
                        resultSet = statement.executeQuery(sql);

                        if(resultSet.next()){
                            out.writeUTF("dublicate username");
                            out.flush();
                        }
                        else{
                            sql="INSERT INTO customer(name,username,password,email) VALUES('"+name+"','"+username+"','"+password+"','"+email+"');";
                            statement.executeUpdate(sql);

                            // Save the session
                            sql="SELECT * FROM customer WHERE username = '"+username+"' AND password = '"+password+"';";                
                            resultSet=statement.executeQuery(sql);
                            resultSet.next();
                            session.setSession(resultSet.getInt("custId"));
                            //session.setSession(resultSet.getInt("custId"));
                            out.writeUTF("ok");
                            out.flush();                            
                        }
                        break;
                    case "deposite":
                        int amount = Integer.parseInt((String)in.readUTF());
//                        System.out.println(session.getSession());
                        System.out.println(session.getSession());
                        sql="UPDATE customer SET balance = balance+'"+amount+"' WHERE custId ='"+session.getSession()+"';";
                        statement.executeUpdate(sql);
                        out.writeUTF("ok");
                        out.flush();
                        break;
                    case "Get account info":
                        System.out.println(session.getSession());
                        sql="SELECT * FROM customer WHERE custid = '"+session.getSession()+"';";
                        resultSet=statement.executeQuery(sql);
                        if (resultSet.next()){
                            out.writeUTF(resultSet.getString("username"));
                            out.writeUTF(resultSet.getString("name"));
                            out.writeUTF(resultSet.getString("email"));
                            out.writeUTF(resultSet.getString("balance"));
                            out.flush();
                        }                            
                        break;
                    case "Get purchase info":                        
                        sql="SELECT count(*) AS num FROM purchase pu, customer c, product p WHERE c.custid = '"+session.getSession()+"' AND c.custid = pu.custid AND pu.prodid = p.prodid;";
                        resultSet=statement.executeQuery(sql);
                        resultSet.next();
                        num = resultSet.getInt("num");
                        if (num > 0){
                            out.writeUTF(String.valueOf(num));                        
                            sql="SELECT p.prodname, pu.purqnt, pu.purDate FROM purchase pu, customer c, product p WHERE c.custid = '"+session.getSession()+"' AND c.custid = pu.custid AND pu.prodid = p.prodid ORDER BY pu.purdate desc;";
                            resultSet=statement.executeQuery(sql);
                            while (resultSet.next())
                            {
                                out.writeUTF(resultSet.getString("prodname"));
                                out.writeUTF(resultSet.getString("purqnt"));
                                out.writeUTF(resultSet.getString("purDate"));
                            }
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();
                        }
                        break;
                    case "Get shop data":
                        sql="SELECT count(*) AS num FROM product;";
                        resultSet=statement.executeQuery(sql);
                        resultSet.next();
                        num = resultSet.getInt("num");
                        if (num > 0){
                            out.writeUTF(String.valueOf(num));                        
                            sql="SELECT * FROM product WHERE prodStockQnt > 0;";
                            resultSet=statement.executeQuery(sql);
                            while (resultSet.next())
                            {
                                out.writeUTF(resultSet.getString("prodid"));
                                out.writeUTF(resultSet.getString("prodname"));
                                out.writeUTF(resultSet.getString("prodprice"));
                                out.writeUTF(resultSet.getString("category"));
                            }
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();
                        }
                        break;
                    case "search":
                        String word = (String)in.readUTF();
                        String category = (String)in.readUTF();
                        word = "%"+word +"%";
                        sql="SELECT count(*) AS num FROM product WHERE prodStockQnt > 0 AND prodname LIKE '"+word+"' AND category = '"+category+"';";
                        resultSet=statement.executeQuery(sql);
                        resultSet.next();
                        num = resultSet.getInt("num");
                        if (num > 0){
                            out.writeUTF(String.valueOf(num));                        
                            sql="SELECT * FROM product WHERE prodStockQnt > 0 AND prodname LIKE '"+word+"' AND category = '"+category+"';";
                            resultSet=statement.executeQuery(sql);
                            while (resultSet.next())
                            {
                                out.writeUTF(resultSet.getString("prodid"));
                                out.writeUTF(resultSet.getString("prodname"));
                                out.writeUTF(resultSet.getString("prodprice"));
                                out.writeUTF(resultSet.getString("category"));
                            }
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();
                        }
                        break;
                    case "word search":
                        word = (String)in.readUTF();
                        word = "%"+word +"%";
                        sql="SELECT count(*) AS num FROM product WHERE prodStockQnt > 0 AND prodname LIKE '"+word+"';";
                        resultSet=statement.executeQuery(sql);
                        resultSet.next();
                        num = resultSet.getInt("num");
                        if (num > 0){
                            out.writeUTF(String.valueOf(num));                        
                            sql="SELECT * FROM product WHERE prodStockQnt > 0 AND prodname LIKE '"+word+"';";
                            resultSet=statement.executeQuery(sql);
                            while (resultSet.next())
                            {
                                out.writeUTF(resultSet.getString("prodid"));
                                out.writeUTF(resultSet.getString("prodname"));
                                out.writeUTF(resultSet.getString("prodprice"));
                                out.writeUTF(resultSet.getString("category"));
                            }
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();
                        }
                        break;
                    case "Get cart data":
                        sql = "UPDATE cart c, product p SET c.orderQnt = p.prodStockQnt WHERE p.prodid = c.prodid AND custid = '"+session.getSession()+"' AND c.orderqnt > p.prodStockQnt;";
                        statement.executeUpdate(sql);
                        sql="SELECT count(*) AS num FROM cart WHERE custid = '"+session.getSession()+"' AND orderqnt > 0;";
                        resultSet=statement.executeQuery(sql);
                        resultSet.next();
                        num = resultSet.getInt("num");
                        if (num > 0){
                            out.writeUTF(String.valueOf(num));                        
                            sql="SELECT * FROM cart c, product p WHERE p.prodid = c.prodid AND c.custid = '"+session.getSession()+"' AND orderqnt > 0;";
                            resultSet=statement.executeQuery(sql);
                            while (resultSet.next())
                            {
                                out.writeUTF(resultSet.getString("prodid"));
                                out.writeUTF(resultSet.getString("prodname"));
                                out.writeUTF(resultSet.getString("prodprice"));
                                out.writeUTF(resultSet.getString("orderqnt"));
                            }
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();
                        }
                        break;
                    case "buy shop":
                        qnt = Integer.parseInt((String)in.readUTF());
                        id = Integer.parseInt((String)in.readUTF());
                        sql="SELECT * FROM product WHERE prodid = '"+id+"';";
                        resultSet=statement.executeQuery(sql);
                        if (resultSet.next())
                        {
                            prodqnt = resultSet.getInt("prodStockQnt");
                            price = resultSet.getInt("prodprice");// float
                            if (prodqnt < qnt)
                            {
                                out.writeUTF("qnt not valid");
                                out.flush();
                                break;
                            }
                            sql = "SELECT * FROM customer WHERE custid = '"+session.getSession()+"';";
                            resultSet=statement.executeQuery(sql);
                            resultSet.next();
                            if (resultSet.getInt("balance") < qnt * price)
                            {
                                out.writeUTF("You have no enough balance");
                                out.flush();
                                break;                                
                            }
                            sql = "INSERT INTO purchase(purqnt,prodid,custid) VALUES('"+qnt+"','"+id+"','"+session.getSession()+"');";
                            statement.executeUpdate(sql);
                            sql = "UPDATE product SET prodStockQnt = prodStockQnt - '"+qnt+"' WHERE prodid = '"+id+"';";
                            statement.executeUpdate(sql);
                            sql = "UPDATE customer SET balance = balance - '"+qnt*price+"' WHERE custid = '"+session.getSession()+"';";
                            statement.executeUpdate(sql);
                            out.writeUTF("ok");
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();                            
                        }                        
                        break;
                    case "put in cart":
                        qnt = Integer.parseInt((String)in.readUTF());
                        id = Integer.parseInt((String)in.readUTF());
                        sql="SELECT * FROM product WHERE prodid = '"+id+"';";
                        resultSet=statement.executeQuery(sql);
                        if (resultSet.next())
                        {
                            prodqnt = resultSet.getInt("prodStockQnt");
                            if (prodqnt < qnt)
                            {
                                out.writeUTF("qnt not valid");
                                out.flush();
                                break;
                            }
                            sql = "SELECT * FROM cart WHERE custid = '"+session.getSession()+"' AND prodid = '"+id+"';";
                            resultSet=statement.executeQuery(sql);
                            if (resultSet.next()){
                                if (resultSet.getInt("orderQnt") + qnt > prodqnt){
                                    out.writeUTF("qnt not valid2");
                                    out.writeUTF(String.valueOf(qnt));
                                    out.writeUTF(resultSet.getString("orderqnt"));                                    
                                    out.flush();
                                }
                                else{
                                    sql = "UPDATE cart SET orderQnt = orderQnt + '"+qnt+"' WHERE custid = '"+session.getSession()+"' AND prodid = '"+id+"';";
                                    statement.executeUpdate(sql);
                                    out.writeUTF("ok");                                    
                                }
                            }
                            else{
                                sql = "INSERT INTO cart VALUES('"+session.getSession()+"','"+id+"', '"+qnt+"');";
                                statement.executeUpdate(sql);
                                out.writeUTF("ok");
                                out.flush();
                            }
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();                            
                        }                        
                        break;
                    case "edit cart":
                        qnt = Integer.parseInt((String)in.readUTF());
                        id = Integer.parseInt((String)in.readUTF());
                        sql="SELECT * FROM product WHERE prodid = '"+id+"';";
                        resultSet=statement.executeQuery(sql);
                        if (resultSet.next())
                        {
                            prodqnt = resultSet.getInt("prodStockQnt");
                            if (prodqnt < qnt)
                            {
                                out.writeUTF("qnt not valid");
                                out.flush();
                                break;
                            }                            
                            sql = "UPDATE cart SET orderQnt = '"+qnt+"' WHERE custid = '"+session.getSession()+"' AND prodid = '"+id+"';";
                            statement.executeUpdate(sql);
                            out.writeUTF("cart ok");
                        }
                        break;
                    case "buy cart":
                        qnt = Integer.parseInt((String)in.readUTF());
                        id = Integer.parseInt((String)in.readUTF());
                        sql="SELECT * FROM product WHERE prodid = '"+id+"';";
                        resultSet=statement.executeQuery(sql);
                        if (resultSet.next())
                        {
                            prodqnt = resultSet.getInt("prodStockQnt");
                            price = resultSet.getInt("prodprice");
                            if (prodqnt < qnt)
                            {
                                out.writeUTF("qnt not valid");
                                out.flush();
                                break;
                            }
                            sql = "SELECT * FROM customer WHERE custid = '"+session.getSession()+"';";
                            resultSet=statement.executeQuery(sql);
                            resultSet.next();
                            if (resultSet.getInt("balance") < qnt * price)
                            {
                                out.writeUTF("You have no enough balance");
                                out.flush();
                                break;                                
                            }
                            sql = "INSERT INTO purchase(purqnt,prodid,custid) VALUES('"+qnt+"','"+id+"','"+session.getSession()+"');";
                            statement.executeUpdate(sql);
                            sql = "UPDATE product SET prodStockQnt = prodStockQnt - '"+qnt+"' WHERE prodid = '"+id+"';";
                            statement.executeUpdate(sql);
                            sql = "UPDATE customer SET balance = balance - '"+qnt*price+"' WHERE custid = '"+session.getSession()+"';";
                            statement.executeUpdate(sql);
                            sql = "DELETE FROM cart WHERE custid = '"+session.getSession()+"' AND prodid = '"+id+"';";
                            statement.executeUpdate(sql);
                            out.writeUTF("ok");
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();                            
                        }                        
                        break;
                    case "delete cart":
                        id = Integer.parseInt((String)in.readUTF());
                        sql="SELECT * FROM product WHERE prodid = '"+id+"';";
                        resultSet=statement.executeQuery(sql);
                        if (resultSet.next())
                        {
                            sql = "DELETE FROM cart WHERE custid = '"+session.getSession()+"' AND prodid = '"+id+"';";
                            statement.executeUpdate(sql);
                            out.writeUTF("ok");
                            out.flush();                            
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();                            
                        }                        
                        break;
                    case "admin":
                        sql="SELECT SUM(pu.purqnt * p.prodprice) AS sum,count(*) AS num FROM purchase pu, product p WHERE p.prodid = pu.prodid;";
                        resultSet=statement.executeQuery(sql);
                        resultSet.next();
                        num = resultSet.getInt("num");
                        int sum = resultSet.getInt("sum");
                        if (num > 0){
                            out.writeUTF(String.valueOf(num));
                            out.writeUTF(String.valueOf(sum));

                            sql="SELECT * FROM purchase pu, product p, customer c WHERE p.prodid = pu.prodid AND c.custid = pu.custid ORDER BY pu.purdate desc;";
                            resultSet=statement.executeQuery(sql);
                            while (resultSet.next())
                            {
                                out.writeUTF(resultSet.getString("username"));
                                out.writeUTF(resultSet.getString("prodname"));
                                out.writeUTF(resultSet.getString("purqnt"));
                                out.writeUTF(resultSet.getString("prodprice"));
                                out.writeUTF(resultSet.getString("purdate"));
                            }
                            out.flush();
                        }
                        else{
                            out.writeUTF("no");
                            out.flush();
                        }                        
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /*public static void check_log_in(ServerSocket ss ,Socket s , DataInputStream din , DataOutputStream dout) throws IOException {

        if ((String)din.readUTF()=="admin"){
            dout.writeUTF("ok");
            dout.flush();
            if ((String)din.readUTF()=="admin"){
                dout.writeUTF("ok");
                dout.flush();
    
            }else{
                dout.writeUTF("no");
                dout.flush();
            }
        }else{
            dout.writeUTF("no");
            dout.flush();
        }

    }*/
}
