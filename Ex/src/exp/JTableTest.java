package exp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.AbstractTableModel;

public class JTableTest extends JFrame {

    public JButton btn = new JButton("确认删除");
    String[] columnNames = {"事实", "操作"};
    JPanel pan = new JPanel();
    
    Object[][] data;
    GetFact fa = new GetFact();

    public JTableTest() throws SQLException {
        fa.getfact();
        intiComponent(fa);
        Del();
    }

    public void Del() {
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                    for (int i = 0; i < fa.count; i++) {
                        if (data[i][1].toString().equals("true")) {
                            //删除fad的信息                            
                            String fad = "delete from factd where id='"+(i+1)+"'";
                            sql.executeUpdate(fad);

                            String fa =  "delete from fact where del='"+(i+1)+"'";
                            sql.executeUpdate(fa);

//                            //rule_conclusion表
                            String rcon = "delete from rule_conclusion where rindex='"+(i+1)+"'";
                            sql.executeUpdate(rcon);
                            
//                            //rule_condition表//
                            String rule_cond =  "delete from rule_condition where rindex='"+(i+1)+"'";
                            sql.executeUpdate(rule_cond);
                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(AddRule.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null,
                            "删除成功！",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                try {
                    fa.getfact();
                } catch (SQLException ex) {
                    Logger.getLogger(JTableTest.class.getName()).log(Level.SEVERE, null, ex);
                }                
                pan.repaint();
            }
        }
        );
    }

    private void intiComponent(GetFact fa) {
        JTable table = new JTable(new MyTableModel(fa));
        
        /* 用JScrollPane装载JTable，这样超出范围的列就可以通过滚动条来查看 */
        JScrollPane scroll = new JScrollPane(table);
//        add(btn);
//        add(scroll);
        pan.add(scroll);
        pan.add(btn);
        add(pan);

        this.setTitle("删除规则");
        this.setSize(700, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //设置关闭方式 如果不设置的话 似乎关闭窗口之后不会退出程序
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

    }

    public class MyTableModel extends AbstractTableModel {

        /*
	 * 这里和刚才一样，定义列名和每个数据的值
         */
        /**
         *
         * 构造方法，初始化二维数组data对应的数据
         *
         */
        public MyTableModel(GetFact fa) {
            data = new Object[fa.count][2];
            for (int i = 0; i < fa.count; i++) {
                for (int j = 0; j < 2; j++) {
                    if (0 == j) {
                        data[i][j] = fa.facts[i];
                    } else {
                        data[i][j] = new Boolean(false);
                    }
                }
            }
        }

        // 以下为继承自AbstractTableModle的方法，可以自定义
        /**
         *
         * 得到列名
         *
         */
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        /**
         *
         * 重写方法，得到表格列数
         *
         */
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        /**
         *
         * 得到表格行数
         *
         */
        @Override
        public int getRowCount() {
            return data.length;
        }

        /**
         *
         * 得到数据所对应对象
         *
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        /**
         *
         * 得到指定列的数据类型
         *
         */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return data[0][columnIndex].getClass();
        }

        /**
         *
         * 指定设置数据单元是否可编辑.这里设置"姓名","学号"不可编辑
         *
         */
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex < 1) {
                return false;
            } else {
                return true;
            }
        }

        /**
         *
         * 如果数据单元为可编辑，则将编辑后的值替换原来的值
         *
         */
        @Override

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
            /*通知监听器数据单元数据已经改变*/
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public static void main(String[] args) throws SQLException {
        new JTableTest();
    }

}
