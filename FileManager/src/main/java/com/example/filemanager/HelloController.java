package com.example.filemanager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick(){

        JButton myButton = new JButton("Open new window");
        JFrame newFrame = new JFrame("New Window");
//add this line of code
        myButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
// open a new frame i.e window
                newFrame.pack();
                newFrame.setVisible(true);
            }
        });
        }
}