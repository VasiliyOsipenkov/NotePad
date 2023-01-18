package ru.avalon.javapp.devj110.notepad;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class NotePad extends JFrame {
    JTextArea text;
    JMenuBar menuBar;
    JFileChooser chooser;
    JScrollPane scrollPane;//https://spec-zone.ru/RU/Java/Tutorials/uiswing/components/scrollpane.html
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

        openFile.addActionListener(e -> openFile());

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

    }

    private void openFile() {
        int val = chooser.showOpenDialog(this);
        try {
            FileReader fileReader = new FileReader(chooser.getSelectedFile());
            BufferedReader reader = new BufferedReader(fileReader);
            while (reader.ready()) {
                text.append(reader.readLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new NotePad().setVisible(true);
    }
}
/*Реализуйте приложение, выполняющее функции простого текстового редактора, при помощи
которого можно редактировать текст, сохранять его в файл и загружать из файла.
Редактор должен иметь меню для доступа к его функциям.
В заголовке окна редактора должно отображаться полное имя редактируемого файла. Если редактируется новый файл,
то заголовок должен содержать соответствующий текст.
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
При создании нового файла, загрузке файла и при выходе, если редактор содержит текст с несохранёнными изменениями, то
операция должна выполняться после дополнительного подтверждения.*/