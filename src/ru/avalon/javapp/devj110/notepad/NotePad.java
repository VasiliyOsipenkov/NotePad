package ru.avalon.javapp.devj110.notepad;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;

public class NotePad extends JFrame {
    JTextArea text;
    JMenuBar menuBar;
    JFileChooser chooser;
    JScrollPane scrollPane;
    public NotePad() {
        super("NotePad");
        setBounds(800, 400, 600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        text = new JTextArea();
        text.setEditable(true);

        chooser = new JFileChooser();
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem saveAsFile = new JMenuItem("Save as");
        JMenuItem closeFile = new JMenuItem("Close");

        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK | Event.SHIFT_MASK));
        closeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

        openFile.addActionListener(e -> openFile());
        saveFile.addActionListener(e -> saveFile());
        saveAsFile.addActionListener(e -> saveAsFile());
        closeFile.addActionListener(e -> closeFile());

        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.add(closeFile);
        menuBar.add(fileMenu);
        scrollPane = new JScrollPane(text);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(menuBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        textChangeListener();

    }

    private void openFile() {
        chooser.setDialogTitle("Open file");
        int val = chooser.showOpenDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            setTitle(chooser.getSelectedFile().toString());
            try (FileInputStream inputStream = new FileInputStream(chooser.getSelectedFile())){
                StringBuilder sb = new StringBuilder();
                int i;
                while ((i = inputStream.read()) != -1) {
                    sb.append((char) i);
                }
                text.setText(sb.toString());
                text.setCaretPosition(0);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void saveFile() {
        if (chooser.getSelectedFile() != null) {
            save();
        } else {
            chooser.setDialogTitle("Save file");
            int val = chooser.showSaveDialog(this);
            if (val == JFileChooser.APPROVE_OPTION) {
                setTitle(chooser.getSelectedFile().toString());
                save();
            }
        }
    }

    private void save() {
        try (FileOutputStream outputStream = new FileOutputStream(chooser.getSelectedFile())){
            outputStream.write(text.getText().getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAsFile() {
        File file = chooser.getSelectedFile();
        chooser.setDialogTitle("Save file as");
        int val = chooser.showSaveDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            if (!file.equals(chooser.getSelectedFile()) && chooser.getSelectedFile().exists()) {
                int result = JOptionPane.showConfirmDialog(this,"Overwrite file?",
                        "Overwrite",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    setTitle(chooser.getSelectedFile().toString());
                    save();
            } else {
                setTitle(chooser.getSelectedFile().toString());
                save();
            }
        }
    }

    private void closeFile() {
        chooser.setSelectedFile(new File(""));
        setTitle("NotePad");
        text.setText(null);
        /*
        проверочка
        */
    }
    private void textChangeListener() {
        text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (chooser.getSelectedFile() == null)
                    setTitle("New text*");
                else {
                    setTitle(chooser.getSelectedFile().toString() + "*");//Заголовок должен отображать признак того, что редактор содержит несохранённые изменения.
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    public static void main(String[] args) {
        new NotePad().setVisible(true);
    }
}
/*

*Заголовок должен отображать признак того, что редактор содержит несохранённые изменения.
Редактор должен:

При возникновении ошибок чтения/сохранения файла пользователю должно быть показано соответствующее сообщение.
При создании нового файла, загрузке файла и при выходе, если редактор содержит текст с несохранёнными изменениями, то
операция должна выполняться после дополнительного подтверждения.*/