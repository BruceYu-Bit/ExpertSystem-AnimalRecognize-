package exp;
import java.awt.BorderLayout;
import java.sql.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
public class index extends JFrame {     //需要继承JFrame
    public String[] facts = new String[100];//事实的值
    public int[] rule_con_rindex = new int[100];//rcondition页面的rindex值;
    public int[] rule_con_rcon = new int[100];//rcondition页面的rcondition值
    public int[] rule_conclu = new int[100];//rconclusion页面的conclusion值
    public int[] isfinal=new int[100];//记录按钮是否能一键得到结论
    public int[] fact_isfinal=new int[100];
    private JButton choices[];         //按钮数组，存放按钮事实
    public int fil=0;//final=1的个数
    public int count = 0;//保存事实的数目
    public int flag=0;//判断是否达到最后的结果
    public int click=0;//保存点击的下标
    public int length=0;//保存rule_con_rindex数组的长度
    public int[]reason=new int[100];//存放点击按钮
    public JButton btn1 = new JButton("增加规则");
    public JButton btn2 = new JButton("删除规则");
    public JButton btn3 = new JButton("所有规则");
    public JButton btn4 = new JButton("重置");
    JLabel L1 = new JLabel("推理事实");
    JLabel L4=new JLabel("中间推理过程");    
    JLabel L3 = new JLabel("显示推理结果");
    JButton L2 = new JButton("进行最终推理");
    JTextArea txt1 = new JTextArea(5, 8);
    JTextArea txt2 = new JTextArea(5, 8);
    JTextArea txt3 = new JTextArea(5, 8);
    public index(String title) throws SQLException {
        JFrame jf = new JFrame(title);
        jf.setLayout(null);
        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(5,5,10,10));
        pan.setBounds(20, 20, 580, 200);
        JPanel pan1=new JPanel();
        pan1.setLayout(new GridLayout(1,3,10,10));  
        pan1.setBounds(20, 220, 500, 50);
        JPanel pan2=new JPanel();
        pan2.setLayout(new GridLayout(1,3,10,10));
        pan2.setBounds(20, 270, 500, 200);
        JPanel pan3=new JPanel();
        pan3.setLayout(new GridLayout(5,1,10,10));
        pan3.setBounds(530, 270, 100, 200);
        jf.add(pan);
        jf.add(pan1);
        jf.add(pan2);
        jf.add(pan3);
        btn1.setToolTipText("增加规则");btn2.setToolTipText("删除规则");btn3.setToolTipText("所有规则");
        btn4.setToolTipText("重置");L2.setToolTipText("进行最终推理");
        Dimension preferredSize = new Dimension(100,40);//设置尺寸
        //在窗体里加入事实按钮
        getfact();
        choices = new JButton[count];
        for (int i = 0; i < count; i++) {
            choices[i] = new JButton(facts[i]);
            choices[i].setToolTipText(facts[i]);
            choices[i].setPreferredSize(preferredSize);
            String s=String.valueOf(i+1);
            choices[i].setName(s);
            if(fact_isfinal[i]==0){                
                pan.add(choices[i]);
            }            
        }
        pan1.add(L1);
        pan1.add(L4);
        pan1.add(L3);
        pan2.add(txt1);
        pan2.add(txt2);
        pan2.add(txt3);
        pan3.add(btn1);
        pan3.add(btn2);
        pan3.add(btn3);
        pan3.add(btn4);
        pan3.add(L2);

        jf.setBounds(200, 200, 700, 600); //设置窗口的属性 窗口位置以及窗口的大小
        jf.setVisible(true);//设置窗口可见
        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //设置关闭方式 如果不设置的话 似乎关闭窗口之后不会退出程序

        for (int i = 0; i < count; i++) {
            choices[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton temp = (JButton) e.getSource();
                    String str = temp.getLabel() + "\n";
                    txt1.append(str);
                    //将字符串转化为数字                    
                    int j=Integer.valueOf(temp.getName());    
                    int tru=0;
                    for(int k=0;k<length;k++){                        
                        if(rule_con_rcon[k]==j&&isfinal[k]==1){
                            str=str+"-->"+facts[rule_conclu[rule_con_rindex[k]-1]-1]+"\n";                           
                            txt2.append(str);
                            reason[click++]=rule_conclu[rule_con_rindex[k]-1];
                            tru=1;
                            break;
                        }
                    }
                    if(tru==0){
//                        System.out.println("又被点击了");
                        reason[click++]=j;
                    }
                }
            });
        }
        ShowMes();
        Reset();
        OnClick();
        btn1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    new AddRule();
                } catch (SQLException ex) {
                    Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btn2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    new DeleteRule();
                } catch (SQLException ex) {
                    Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }

    public static void main(String[] args) throws SQLException {
        new index("动物识别专家系统");        //创建窗口
    }

    //从数据库里获取数据
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
        rs = sql.executeQuery("SELECT * FROM fact");
        while (rs.next()) {
            facts[count] = rs.getString(2);
            fact_isfinal[count++]=Integer.valueOf(rs.getString(3));
//            if(Integer.valueOf(rs.getString(3))==1){
//                fil++;
//            }
        }
        int k=0;
        rs=sql.executeQuery("SELECT * FROM rule_conclusion");
        while(rs.next()){
            rule_conclu[k++]=Integer.valueOf(rs.getString(2));
        }
        k=0;
        rs=sql.executeQuery("SELECT * FROM rule_condition");
        while(rs.next()){
            rule_con_rindex[k]=Integer.valueOf(rs.getString(1));
            rule_con_rcon[k]=Integer.valueOf(rs.getString(2));
            isfinal[k++]=Integer.valueOf(rs.getString(3));
            length++;
        }
    }

    //显示信息
    public void ShowMes() {
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "IF 该动物有毛发 THEN 该动物是哺乳动物\n"
                        + "IF 该动物有奶   THEN 该动物是哺乳动物\n"
                        + "IF 该动物有羽毛 THEN 该动物是鸟\n"
                        + "IF 该动物会飞   AND   会下蛋 THEN 该动物是鸟\n"
                        + "IF 该动物吃肉   THEN 该动物是食肉动物\n"
                        + "IF 该动物有犬齿  AND有爪  AND 眼盯前方 THEN 该动物是食肉动物\n"
                        + "IF 该动物是哺乳动动 AND 有蹄 THEN 该动物是有蹄类动物\n"
                        + "IF 该动物是哺乳动物 AND 是嚼反刍动物 THEN 该动物是有蹄类动物\n"
                        + "IF 该动物是哺乳动物 AND 是食肉动物 AND 是黄褐色 AND 身上有暗斑点 THEN 该动物是金钱豹\n"
                        + "IF 该动物是哺乳动物 AND 是食肉动物 AND 是黄褐色 AND 身上有黑色条纹 THEN 该动物是老虎\n"
                        + "IF 该动物是有蹄类动物 AND 有长脖子 AND 有长腿 AND 身上有暗斑点 THEN 该动物是长颈鹿\n"
                        + "IF 该动物是有蹄类动物 AND 身上有黑色条纹 THEN 该动物是斑马\n"
                        + "IF 该动物是鸟 AND 有长脖子 AND 有长腿 AND 不会飞 AND 有黑白二色 THEN 该动物是鸵鸟\n"
                        + "IF 该动物是鸟 AND 会游泳 AND 不会飞 AND 有黑白二色 THEN 该动物是企鹅\n"
                        + "IF 该动物是鸟 AND 善飞 THEN 该动物是信天翁\n",
                        "所有规则",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    //重置按钮的操作
    public void Reset() {
        btn4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt1.setText("");
                txt2.setText("");
                txt3.setText("");
                click=0;
            }
        });
    }
    //进行推理的操作
    public void OnClick(){
        L2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(click==1){
                    txt3.append("无匹配动物");
                }else{
                    
                    int t = click;
                    for (t = click; t < 100; t++) {
                        reason[t] = 1000;
                    }//会飞会下蛋
                    int dif = 0;//不同条件个数
                    int fla = 0;//为1时表示找到符合条件的生物
                    for (t = 0; t < click; t++) {
                        if (reason[t] != reason[t + 1]) {
                            dif++;
                        }
                    }
                    for(int p=0;p<click;p++){
                        System.out.println(reason[p]);
                    }
                    Arrays.sort(reason);
                    int s = 0;
                    for (s = 0; s < length; s++) {
//                        System.out.println(s+1);
                        if (isfinal[s] != dif) {
                            s = s + isfinal[s] - 1;
                        } else {
                            for (t = 0; t < click; t++) {
//                                System.out.println("进行比对");
                                if (reason[t] != rule_con_rcon[s]&&t==0) {
                                    s=s+dif-1;
                                    break;
                                } else {
                                    if (reason[t] == reason[t + 1]) {
                                        t += 1;
                                    }
                                    s++;
                                }
                            }
                            if (t == click) {
                                fla = 1;
                                break;//没有考虑两种结果
                            }
                        }
                    }
                    if (fla == 1) {
                        String temp = facts[rule_conclu[rule_con_rindex[s - 1] - 1] - 1] + "\n";
                        if(fact_isfinal[rule_conclu[rule_con_rindex[s - 1] - 1] - 1]==-1){
                            txt3.setText("");
                            txt3.append(temp);
                        }else{
                            txt2.append(temp);
                            txt3.append("无匹配动物");
                        }                       
                        
                    } else {
                        txt3.append("所给条件无法推理出最终的结论，请查看所有规则或重置重新尝试");
                    }
                }
            }
        });
    }
}
