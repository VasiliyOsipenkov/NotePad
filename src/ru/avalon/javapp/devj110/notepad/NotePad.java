package ru.avalon.javapp.devj110.notepad;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotePad extends JFrame {
    private final JTextArea text;
    private final JMenuBar menuBar;
    private final JFileChooser chooser;
    private final JScrollPane scrollPane;
    private boolean editTextArea = false;
    public NotePad() {
        super("NotePad");
        setBounds(800, 400, 600, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        text = new JTextArea();
        text.setEditable(true);

        chooser = new JFileChooser();
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem clearText = new JMenuItem("Clear");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem saveAsFile = new JMenuItem("Save as");
        JMenuItem exit = new JMenuItem("Exit");

        clearText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK | Event.SHIFT_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

        clearText.addActionListener(e -> clearText());
        openFile.addActionListener(e -> openFile());
        saveFile.addActionListener(e -> saveFile());
        saveAsFile.addActionListener(e -> saveAsFile());
        exit.addActionListener(e -> exit());

        fileMenu.add(clearText);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.add(exit);

        menuBar.add(fileMenu);
        scrollPane = new JScrollPane(text);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(menuBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        textChangeListener();
        windowListener();

    }

    private void openFile() {
        if (editTextArea)
            if (!exitDialogPane("Delete unsaved text?"))
                return;

        chooser.setDialogTitle("Open file");
        int val = chooser.showOpenDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            try (FileInputStream inputStream = new FileInputStream(chooser.getSelectedFile())){
                StringBuilder sb = new StringBuilder();
                int i;
                while ((i = inputStream.read()) != -1) {
                    sb.append((char) i);
                }
                text.setText(sb.toString());
                text.setCaretPosition(0);
                setTitle(chooser.getSelectedFile().toString());
                editTextArea = false;
            } catch (FileNotFoundException ex) {
                errorPane("File read error:\n" + ex);
            } catch (IOException ex) {
                errorPane("File read data error: \n" + ex);
            }
        }

    }

    private void saveFile() {
        if (chooser.getSelectedFile() != null) {
            save();
        } else {
            if (editTextArea)
                if (!exitDialogPane("Create a new file?"))
                return;

            chooser.setDialogTitle("Save file");
            int val = chooser.showSaveDialog(this);
            if (val == JFileChooser.APPROVE_OPTION) {
                save();
            }
        }
    }

    private void save() {
        try (FileOutputStream outputStream = new FileOutputStream(chooser.getSelectedFile())){
            outputStream.write(text.getText().getBytes());
            editTextArea = false;
            setTitle(chooser.getSelectedFile().toString());
        } catch (FileNotFoundException e) {
            errorPane("File write error\n" + e);
        } catch (IOException e) {
            errorPane("file write data error\n" + e);
        }
    }

    private void saveAsFile() {
        chooser.setDialogTitle("Save file as");
        int val = chooser.showSaveDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists()) {
                int result = JOptionPane.showConfirmDialog(this,"Overwrite file?",
                        "Overwrite",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    save();
            } else {
                save();
            }
        }
    }

    private void clearText() {
        if (editTextArea)
            if (!exitDialogPane("Delete unsaved text?"))
                return;

        chooser.setSelectedFile(null);
        text.setText(null);
        setTitle("NotePad");
        editTextArea = false;
    }

    private void exit() {
        if (editTextArea) {
            if (exitDialogPane("Changes not saved, close editor?"))
                System.exit(1);
        } else
            System.exit(1);
    }
    private void changeTitle() {
        if (chooser.getSelectedFile() == null) {
            setTitle("New text*");
            editTextArea = true;
        } else {
            setTitle(chooser.getSelectedFile().toString() + "*");
            editTextArea = true;
        }
    }
    private void textChangeListener() {
        text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeTitle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeTitle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    private void errorPane(String s) {
        int result = JOptionPane.showConfirmDialog(this,s,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean exitDialogPane(String s) {
        int result = JOptionPane.showConfirmDialog(this,s,
                "Exit confirmation",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION && editTextArea) {
            return true;
        } else
            return false;

    }

    private void windowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    public static void main(String[] args) {
        new NotePad().setVisible(true);
    }
}
