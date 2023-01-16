package ru.avalon.javapp.devj110.notepad;

import javax.swing.*;
import java.awt.*;

public class NotePad extends JFrame {
    JTextArea text;
    JMenuBar mainMenu;
    public NotePad() {
        super("NotePad");
        setBounds(800, 400, 600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        text = new JTextArea();
        text.setEditable(true);

        mainMenu = new JMenuBar("https://javaswing.wordpress.com/2010/02/20/jmenubar/");


        add(text, BorderLayout.SOUTH);
        add(mainMenu, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        new NotePad().setVisible(true);
    }
}
