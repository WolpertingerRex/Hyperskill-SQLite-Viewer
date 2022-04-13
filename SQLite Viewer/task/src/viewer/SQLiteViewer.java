package viewer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class SQLiteViewer extends JFrame {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private Database database;
    private List<String> tables;
    private final JTextField filenameField = new JTextField();
    private final JButton openFileButton = new JButton("Open");
    private final JComboBox<String> tablesBox = new JComboBox<>();
    private final JTextArea queryText = new JTextArea();
    private final JButton executeButton = new JButton("Execute");
    private final JTable table = new JTable(new DefaultTableModel());


    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        createGUI();
        setVisible(true);
    }

    private void createGUI(){
        this.setTitle("SQLite Viewer");
        filenameField.setName("FileNameTextField");
        filenameField.setBounds(10,10,WIDTH*3/4, HEIGHT/10);
        filenameField.setBorder(new LineBorder(Color.BLUE, 3));

        openFileButton.setName("OpenFileButton");
        openFileButton.setBounds(640, 10, 120, HEIGHT/10);


        tablesBox.setName("TablesComboBox");
        tablesBox.setBounds(10, 80, WIDTH-40, HEIGHT/10);

        queryText.setName("QueryTextArea");
        queryText.setBounds(10, 150, WIDTH*3/4, HEIGHT/8);
        queryText.setBorder(new LineBorder(Color.BLUE, 3));
        queryText.setEnabled(false);

        executeButton.setName("ExecuteQueryButton");
        executeButton.setBounds(640, 150, 120, HEIGHT/10);
        executeButton.setEnabled(false);

        table.setName("Table");
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 250, WIDTH - 40, (int) (HEIGHT*0.45));
        scrollPane.setBorder(new LineBorder(Color.BLUE, 3));

        this.getContentPane().add(filenameField);
        this.getContentPane().add(openFileButton);
        this.getContentPane().add(tablesBox);
        this.getContentPane().add(queryText);
        this.getContentPane().add(executeButton);
        this.add(scrollPane);
        init();

    }

    private void init(){

        openFileButton.addActionListener(actionEvent -> {
            queryText.setText("");
            tablesBox.removeAllItems();

            String filename = filenameField.getText();

            if(Files.exists(Paths.get(filename))) {
                database = new Database(filename);

                tables = database.getTables(database.connect());
                tables.forEach(tablesBox::addItem);

                queryText.setEnabled(true);
                executeButton.setEnabled(true);

                String tableName = (String) tablesBox.getSelectedItem();
                String query = String.format("SELECT * FROM %s;",tableName);

                queryText.setText(query);

            } else {
                JOptionPane.showMessageDialog(new Frame(), "File doesn't exist!");
                queryText.setEnabled(false);
                executeButton.setEnabled(false);
            }


        });

        tablesBox.addItemListener(e->queryText.setText(String.format("SELECT * FROM %s;", e.getItem().toString())));

        executeButton.addActionListener(actionEvent -> {
            String query = queryText.getText();
            MyTableModel model = null;
            try {
                model = database.execute(query);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(new Frame(), "Wrong SQL query!");

            }
            table.setModel(model);
        });

    }
}
