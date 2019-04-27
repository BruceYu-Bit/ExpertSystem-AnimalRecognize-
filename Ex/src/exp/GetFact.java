package exp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
public class GetFact {
//    从数据库里获取数据
    public String[] facts = new String[100];//事实的值
//    public static int[] rule_con_rindex = new int[100];//rcondition页面的rindex值;
//    public static int[] rule_con_rcon = new int[100];//rcondition页面的rcondition值
//    public static int[] rule_conclu = new int[100];//rconclusion页面的conclusion值
//    public static int[] isfinal=new int[100];//记录按钮是否能一键得到结论
//    public static int[] fact_isfinal=new int[100];
//    private static JButton choices[];         //按钮数组，存放按钮事实
    public int count = 0;//保存事实的数目
//    public static int flag=0;//判断是否达到最后的结果
//    public static int click=0;//保存点击的下标
//    public static int length=0;//保存rule_con_rindex数组的长度
    public void getfact() throws SQLException {
        Connection con;
        Statement sql;
        ResultSet rs;
        String uri = "jdbc:mysql://140.143.38.227/caugreenhouse_c?user=yx&password=cau2018&characterEncoding=gb2312";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e);
        }
        con = DriverManager.getConnection(uri);
        sql = con.createStatement();
        rs = sql.executeQuery("SELECT * FROM factd");
        while (rs.next()) {
            facts[count++] = rs.getString(1);
//            fact_isfinal[count++]=rs.getShort(3);
        }
//        int k=0;
//        rs=sql.executeQuery("SELECT * FROM rule_conclusion");
//        while(rs.next()){
//            rule_conclu[k++]=Integer.valueOf(rs.getString(2));
//        }
//        k=0;
//        rs=sql.executeQuery("SELECT * FROM rule_condition");
//        while(rs.next()){
//            rule_con_rindex[k]=Integer.valueOf(rs.getString(1));
//            rule_con_rcon[k]=Integer.valueOf(rs.getString(2));
//            isfinal[k++]=Integer.valueOf(rs.getString(3));
//            length++;
//        }
    }
//    public static void main(String[]args) throws SQLException{
//        GetFact a=new GetFact();
//        a.getfact();
//    }
}
