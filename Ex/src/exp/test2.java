/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author 为你而战
 */
public class test2 {
    public static void main(String[] args) {
  JFrame frame = new JFrame();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setLayout(new FlowLayout());
  JButton button = new JButton("测试按钮");
  Dimension preferredSize = new Dimension(100,50);//设置尺寸
  button.setPreferredSize(preferredSize );
  frame.add(button);
  frame.setBounds(0,0, 400, 300);
  frame.setVisible(true); 
 }
    
}
