package ru.avalon.javapp.devj110.notepad;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Main extends JFrame {
    private final JList list;
    private final JTextArea content;
    private final JMenuBar menuBar;

    private File[] children;

    public Main() {
        list = new JList();
        list.addListSelectionListener(e -> listSelectionChanged());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    int ndx = list.getSelectedIndex();
                    if(ndx >= 0 && children[ndx].isDirectory())
                        goToDir(children[ndx].getAbsolutePath());
                }
            }
        });
        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int ndx = list.getSelectedIndex();
                    if(ndx >= 0 && children[ndx].isDirectory())
                        goToDir(children[ndx].getAbsolutePath());
                }
            }
        });



        content = new JTextArea();
        content.setEditable(false);

        JScrollPane sp = new JScrollPane(content);
        add(sp, BorderLayout.CENTER);

        setBounds(800, 400, 600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        goToDir(System.getProperty("user.dir"));

        JMenu menu = new JMenu("Файл");
        JFileChooser chooser = new JFileChooser(File );
        JMenuItem openFile = new JMenuItem("Открыть");
        openFile.add(chooser);
        menu.add(openFile);
        JMenuItem newFile = new JMenuItem("Создать");
        newFile.setAccelerator(KeyStroke.getKeyStroke('x'));
        menu.add(newFile);

        menuBar = new JMenuBar();
        menuBar.add(menu);
        add(menuBar, BorderLayout.NORTH);
    }

    private void goToDir(String path) {
        File dir = new File(path);
        File[] a = dir.listFiles();
        if(a == null) {
            content.setText("Error reading directory.");
            return;
        }

        setTitle(dir.toString());
        children = a;
        Arrays.sort(children, Main::compareFiles);

        File parent = dir.getParentFile();
        if(parent != null) {
            File[] ch = new File[children.length + 1];
            ch[0] = parent;
            System.arraycopy(children, 0, ch, 1, children.length);
            children = ch;
        }

        String[] names = new String[children.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = children[i].getName();
        }
        if(parent != null) {
            names[0] = "..";
        }
        list.setListData(names);
    }

    private void listSelectionChanged() {
        int ndx = list.getSelectedIndex();
        if(ndx == -1)
            return;
        if(children[ndx].isDirectory()) {
            content.setText("");
            return;
        }
        try(FileReader r = new FileReader(children[ndx])) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4_096];
            int n;
            while( ( n = r.read(buf) ) >= 0) {
                sb.append(buf, 0, n);
            }
            content.setText(sb.toString());
            content.setCaretPosition(0);
        } catch (IOException ex) {
            content.setText("Error reading file: " + ex.getMessage() + ".");
        }
    }

    private static int compareFiles(File f1, File f2) {
        if(f1.isDirectory() && f2.isFile())
            return -1;
        if(f1.isFile() && f2.isDirectory())
            return 1;
        return f1.getName().compareTo(f2.getName());
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
/*
Реализуйте приложение, выполняющее функции простого текстового редактора, при помощи
которого можно редактировать текст, сохранять его в файл и загружать из файла.
Редактор должен иметь меню для доступа к его функциям.
В заголовке окна редактора должно отображаться полное имя редактируемого файла. Если редактируется новый файл, то заголовок должен содержать соответствующий текст.
*Заголовок должен отображать признак того, что редактор содержит несохранённые изменения.
Редактор должен:
• позволять пользователю загружать текст из файла;
• сохранять текст в файл; если файл ранее был загружен, или сохранялся, то текст должен
быть сохранён в файл с тем же именем; если текст не был загружен и ещё не сохранялся,
то у пользователя должно быть запрошено расположение и имя файла, в который нужно
сохранить текст;
• сохранять текст в файл с именем, отличным от того, из которого файл был загружен, или
в который он был сохранён ранее; если пользователь сохраняет файл с другим именем, и
такой файл уже существует, то должно запрашиваться дополнительное подтверждение на
перезапись файла;
• очищать поле редактирования текста и сброс имени файла (закрытие и создание нового
файла).
Функции работы с файлами должны быть доступны из меню, а также по обычному сочетанию
клавиш:
• очистка редактора (текста) — Ctrl+N;
• загрузка файла — Ctrl+O;
• сохранение файла — Ctrl+S;
• сохранение файла под новым именем — Ctrl+Shift+S;
• завершение работы с редактором (выход) — Ctrl+W.
При возникновении ошибок чтения/сохранения файла пользователю должно быть показано соответствующее сообщение.
При создании нового файла, загрузке файла и при выходе, если редактор содержит текст с несохранёнными изменениями, то операция должна выполняться после дополнительного подтверждения.
*Если выход из редактора совершается системными средствами (системное меню окна, или
нажатие на соответствующую кнопку в правом верхнем углу), то при наличии несохранённых
изменений закрытие должно выполняться после дополнительного подтверждения.*/