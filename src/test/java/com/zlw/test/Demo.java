package com.zlw.test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Demo extends JFrame implements ActionListener {
    JFrame frame;
    JPanel panel;
    JPanel containe;
    JPanel panel2;
    JButton button1 = null;
    JButton button2 = null;
//    javax.swing.Timer time = new Timer(80, this);

    Demo() {
//        time.start();
        frame = new JFrame();
        containe = (JPanel) frame.getContentPane();
        panel = new JPanel();
        panel2 = new JPanel();
        button1 = new JButton("按钮1");
        button2 = new JButton("按钮2");

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel2.setBackground(Color.RED);
                panel2.add(button2);
                containe.removeAll();
                containe.add(panel2);
                containe.updateUI();
                System.out.println("red");
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(Color.BLACK);
                panel.add(button1);
                containe.removeAll();
                containe.add(panel);
                containe.updateUI();//更改组件UI外观即可
                System.out.println("balck");
            }
        });
        panel.setBackground(Color.BLACK);
        panel2.setBackground(Color.RED);
        panel.add(button1);
        panel2.add(button2);
        containe.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Demo();
    }

    public void actionPerformed(ActionEvent e) {

    }
}