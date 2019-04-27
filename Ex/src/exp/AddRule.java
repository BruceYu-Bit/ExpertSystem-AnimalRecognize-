/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exp;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class AddRule extends JFrame {

    public String[] facts = new String[100];//事实的值
    public int[] rule_con_rindex = new int[100];//rcondition页面的rindex值;
    public int[] rule_con_rcon = new int[100];//rcondition页面的rcondition值
    public int[] rule_conclu = new int[100];//rconclusion页面的conclusion值
    public int[] isfinal=new int[100];//记录按钮是否能一键得到结论
    public int[] fact_isfinal = new int[100];
    private JButton choices[];         //按钮数组，存放按钮事实
    public int count = 0;//保存事实的数目
    public int flag=0;//判断是否达到最后的结果
    public int click=0;//保存点击的下标
    public int length=0;//保存rule_con_rindex数组的长度
    public int length2 = 0;//淇濆瓨conclusion涓暟
    public int[]reason=new int[100];//存放点击按钮   
    public int pre = 0;//琛ㄧず杈撳叆鍓嶆彁鐨勪釜鏁�
    public String precon = "鍓嶆彁";
    JLabel L1 = new JLabel("原有条件");
    JLabel L2 = new JLabel("新增条件");
    JLabel L3 = new JLabel("结论");
    public JButton btn1 = new JButton("增加新前提");
    public JRadioButton bb = new JRadioButton("是否是动物");
    public JButton btn2 = new JButton("加入规则");
    public JButton btn4 = new JButton("重置");
    public JButton btn5 = new JButton("操作说明");
    JTextField[] tx = new JTextField[10];
    JTextArea txt1 = new JTextArea(5, 8);
    JTextArea txt2 = new JTextArea(5, 8);
    JPanel pan = new JPanel();
    JPanel pan1 = new JPanel();
    int width = 200;
    int height = 50;

    public AddRule() throws SQLException {
        JFrame jf = new JFrame();
        jf.setLayout(null);
        jf.setTitle("增加规则");

//        pan1.setLayout(new GridLayout(1,1));
        pan1.setLayout(null);
        pan.setLayout(new GridLayout(5, 5, 10, 10));
        pan.setBounds(20, 20, 580, 200);
        pan1.setBounds(20, 220, 580, 400);
        jf.add(pan);
        jf.add(pan1);
        getfact();
        choices = new JButton[count];
        for (int i = 0; i < count; i++) {
            choices[i] = new JButton(facts[i]);
            choices[i].setToolTipText(facts[i]);
            String s = String.valueOf(i + 1);
            choices[i].setName(s);
            if (fact_isfinal[i] == 0) {
                pan.add(choices[i]);
            }
        }
        //澧炲姞甯冨眬
        L1.setBounds(80, 20, 100, 30);
        txt1.setBounds(20, 50, 150, 250);
        L2.setBounds(255, 20, 100, 20);

        L3.setBounds(450, 20, 100, 20);
        txt2.setBounds(380, 50, 150, 250);
        btn1.setBounds(40, 350, 90, 40);
        btn1.setToolTipText("增加新前提");
        btn2.setBounds(140, 350, 90, 40);
        btn2.setToolTipText("加入规则");
        bb.setBounds(240, 350, 100, 40);
        btn4.setBounds(340, 350, 90, 40);
        btn4.setToolTipText("重置");
        btn5.setBounds(440, 350, 90, 40);
        btn5.setToolTipText("操作说明");
        pan1.add(L1);
        pan1.add(L2);
        pan1.add(L3);
        pan1.add(txt1);
        pan1.add(txt2);
        pan1.add(btn1);
        pan1.add(btn2);
        pan1.add(bb);
        pan1.add(btn4);
        pan1.add(btn5);

        jf.setBounds(200, 200, 650, 650); //璁剧疆绐楀彛鐨勫睘鎬� 绐楀彛浣嶇疆浠ュ強绐楀彛鐨勫ぇ灏�
        jf.setVisible(true);//璁剧疆绐楀彛鍙
        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //璁剧疆鍏抽棴鏂瑰紡 濡傛灉涓嶈缃殑璇� 浼间箮鍏抽棴绐楀彛涔嬪悗涓嶄細閫�鍑虹▼搴�
        for (int i = 0; i < count; i++) {
            choices[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton temp = (JButton) e.getSource();
                    String str = temp.getLabel() + "\n";
                    txt1.append(str);
                    //灏嗗瓧绗︿覆杞寲涓烘暟瀛�                    
                    int j = Integer.valueOf(temp.getName());
                    reason[click++] = j;
                }
            });
        }
        Reset();
        Des();
        AddPre();
        IsFinalSub();
    }

    public void Des() {
        btn5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "首先点击上方的事实规则按钮添加您需添加的事实按钮\n"
                        + "之后如果有新前提，则点击增加新前提的按钮增加前提，就加入规则\n"
                        + "增加完规则之后添加结论\n"
                        + "在此过程中如果有错误添加，则添加重置按钮即可\n"
                        + "添加完之后，点击是否为动物的单选按钮\n"
                                + "最后点击加入规则，进行最后的提交",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    //获取事实
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
            fact_isfinal[count++] = rs.getShort(3);
        }
        int k = 0;
        rs = sql.executeQuery("SELECT * FROM rule_conclusion");
        while (rs.next()) {
            rule_conclu[k++] = Integer.valueOf(rs.getString(2));
        }
        length2 = k;
        k = 0;
        rs = sql.executeQuery("SELECT * FROM rule_condition");
        while (rs.next()) {
            rule_con_rindex[k] = Integer.valueOf(rs.getString(1));
            rule_con_rcon[k] = Integer.valueOf(rs.getString(2));
            isfinal[k++] = Integer.valueOf(rs.getString(3));
            length++;
        }
    }

    public void Reset() {
        btn4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt1.setText("");
                txt2.setText("");
                click = 0;
            }
        });

    }

    public void AddPre() {
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (pre < 10) {
                    JLabel L4 = new JLabel(precon + Integer.valueOf((pre + 1)));
                    tx[pre] = new JTextField();
                    L4.setBounds(width, height, 100, 25);
                    tx[pre].setBounds(width + 40, height, 100, 25);
                    height += 30;
                    pan1.add(L4);
                    pan1.add(tx[pre]);
                    pre++;
                    pan1.validate();
                    pan1.repaint();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "超过最大添加的前提数目，请重置，或者分两次添加",
                            "警告",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    //鏈�缁堢粨璁烘槸鍔ㄧ墿
    public void IsFinalSub() {
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String conclusion = txt2.getText();
                int flag = 0;
                if (bb.isSelected()) {
                    flag = -1;
                }
                try {
                    Connection con = null;
                    Statement sql = null;
                    ResultSet rs;
                    String uri = "jdbc:mysql://140.143.38.227/caugreenhouse_c?user=yx&password=cau2018&characterEncoding=gb2312";
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (Exception ee) {
                        System.out.println(ee);
                    }
                    con = DriverManager.getConnection(uri);
                    sql = con.createStatement();
                    if (conclusion != null) {
                        if (pre == 0) {//琛ㄧず鏃犳柊鍓嶆彁
                            count++;
                            length2++;
                            String fa = "INSERT INTO fact VALUES('" + count + "','" + conclusion + "','" + flag + "','" + length2 + "')";
                            sql.executeUpdate(fa);
                            String des = "IF 该动物�";
                            for (int i = 0; i < click; i++) {
                                if (i != click - 1) {
                                    des = des + facts[reason[i] - 1] + " AND ";
                                } else {
                                    des = des + facts[reason[i] - 1] + " THEN ";
                                }
                            }
                            des = des + "该动物是" + conclusion;
                            String fad = "INSERT INTO factd VALUES('" + des + "','" + length2 + "')";
                            sql.executeUpdate(fad);                            
                            String rcon = "INSERT INTO rule_conclusion VALUES('" + length2 + "','" + count + "')";
                            sql.executeUpdate(rcon);
                            for (int i = 0; i < click; i++) {
                                String rule_cond = "INSERT INTO rule_condition VALUES('" + length2 + "','" + reason[i] + "','" + (click) + "')";
                                sql.executeUpdate(rule_cond);
                            }
                        } else {
//                            System.out.println(pre);
//                            for(int k=0;k<pre;k++){
//                                System.out.println(tx[k].getText());
//                            }
                            //鎻掑叆fact琛�
                            length2++;
                            int fla=0;
                            for (int k = 0; k < pre; k++) {
                                if(tx[k].getText()!=null){
                                    String fa = "INSERT INTO fact VALUES('" + ++count + "','" + tx[k].getText() + "','" + fla + "','" + length2 + "')";
                                    sql.executeUpdate(fa);
                                }                                
                            }
                            String fa = "INSERT INTO fact VALUES('" + ++count + "','" + conclusion + "','" + flag + "','" + length2 + "')";
                            sql.executeUpdate(fa);

                            //factd琛�
                            String des = "IF 该动物";
                            for (int i = 0; i < click; i++) {
                                des = des + facts[reason[i] - 1] + " AND ";
                            }
                            for (int j = 0; j < pre; j++) {
                                if (j != pre - 1 && tx[j].getText()!=null) {
                                    des = des + tx[j].getText() + " AND ";
                                } else {
                                    des = des + tx[j].getText() + " THEN ";
                                }
                            }
                            des = des + "该动物是" + conclusion;
                            String fad = "INSERT INTO factd VALUES('" + des + "','" + length2+ "')";
                            sql.executeUpdate(fad);
                            //rule_conclusion琛�
                            
                            String rcon = "INSERT INTO rule_conclusion VALUES('" + length2 + "','" + count + "')";
                            sql.executeUpdate(rcon);
                            //rule_condition琛�
                            for (int i = 0; i < click; i++) {
                                String rule_cond = "INSERT INTO rule_condition VALUES('" + length2 + "','" + reason[i] + "','" + (click + pre) + "')";
                                sql.executeUpdate(rule_cond);
                            }
                            for (int s = 0; s < pre; s++) {
                                String rule_cond = "INSERT INTO rule_condition VALUES('" + length2 + "','" + (count - pre + s) + "','" + (click + pre) + "')";
                                sql.executeUpdate(rule_cond);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AddRule.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null,
                            "添加成功",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        new AddRule();        //鍒涘缓绐楀彛
    }
}
