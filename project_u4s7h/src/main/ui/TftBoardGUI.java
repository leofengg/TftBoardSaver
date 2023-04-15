package ui;

import model.BoardCollection;
import model.Event;
import model.EventLog;
import model.TftBoard;
import model.Units;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TftBoardGUI extends JPanel {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 1200;

    private JsonWriter writer;
    private JsonReader reader;
    private static final String JSON_STORE = "./data/boardCollection.json";

    private JFrame frame;


    private JLabel imageLabel;
    BufferedImage image;

    private JButton addUnitButton;
    private JButton saveCollectionButton;
    private JButton addBoardToCollection;
    private JButton createBoardButton;
    private JButton loadCollectionButton;

    private JButton removeBoardFromCollection;
    private JButton viewCollection;
    private JButton viewBoardButton;
    private JButton removeUnitButton;
    private JButton exitBoardOptions;


    private BoardCollection collection;

    private JPanel board;
    private TftBoard tftBoard;

    private JPanel collectionOptions;
    private JPanel boardOptions;

    public TftBoardGUI() throws IOException {

        writer = new JsonWriter(JSON_STORE);
        reader = new JsonReader(JSON_STORE);
        collection = new BoardCollection();

        frame = new JFrame();
        collectionOptions = new JPanel();
        boardOptions = new JPanel();

        frame.setTitle("TFT BOARD BUILDER");
        frame.setLayout(new BorderLayout());

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        image = ImageIO.read(new File("./data/tftlabel.png"));
        imageLabel = new JLabel(new ImageIcon(image));

        frame.add(collectionOptions, BorderLayout.NORTH);
        frame.add(imageLabel, BorderLayout.CENTER);
        initializeCollectionOptions();
        initializeBoardOptions();

        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                printLog(EventLog.getInstance());
            }
        });
    }

    //MODIFIES: this
    //EFFECTS: initializes collection options and adds buttons with actions
    public void initializeCollectionOptions() {

        createBoardButton = new JButton("Create Board");
        collectionOptions.add(createBoardButton);
        createBoardButton.addActionListener(new CreateBoard());

        viewBoardButton = new JButton("View Board");
        collectionOptions.add(viewBoardButton);
        viewBoardButton.addActionListener(new ViewBoard());

        viewCollection = new JButton("View Collection");
        collectionOptions.add(viewCollection);
        viewCollection.addActionListener(new ViewCollection());

        removeBoardFromCollection = new JButton("Remove from Collection");
        collectionOptions.add(removeBoardFromCollection);
        removeBoardFromCollection.addActionListener(new RemoveBoard());

        saveCollectionButton = new JButton("Save Collection");
        collectionOptions.add(saveCollectionButton);
        saveCollectionButton.addActionListener(new SaveCollection());

        loadCollectionButton = new JButton("Load Collection");
        collectionOptions.add(loadCollectionButton);
        loadCollectionButton.addActionListener(new LoadCollection());

    }

    //MODIFIES: this
    //EFFECTS: initializes board option buttons with action
    public void initializeBoardOptions() {

        addUnitButton = new JButton("Add Unit");
        boardOptions.add(addUnitButton);
        addUnitButton.addActionListener(new AddUnit());

        removeUnitButton = new JButton("Remove Unit");
        boardOptions.add(removeUnitButton);
        removeUnitButton.addActionListener(new RemoveUnit());

        addBoardToCollection = new JButton("Save Board");
        boardOptions.add(addBoardToCollection);
        addBoardToCollection.addActionListener(new SaveBoard());

        exitBoardOptions = new JButton("Exit");
        boardOptions.add(exitBoardOptions);
        exitBoardOptions.addActionListener(new ExitBoard());

    }

    //MODIFIES: this
    //EFFECTS: displays and updates board state after player adds/removes/creates/loads/views a board
    public void displayBoard() {
        board = new JPanel(new GridLayout(3, 7, 2, 2));

        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 8; j++) {
                if (tftBoard.getUnit(i, j) == null) {
                    JButton button = new JButton("EMPTY");
                    board.add(button);
                } else {
                    JButton button = new JButton(tftBoard.getUnit(i,j).getName());
                    board.add(button);
                }
            }
        }
        frame.add(board, BorderLayout.CENTER);
    }

    //EFFECTS: prints log of events occurred
    public void printLog(EventLog e) {
        for (Event next : e) {
            System.out.println(next.toString() + "\n");
        }
    }

    //class for create board button
    private class CreateBoard implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for create board button
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("Enter a board name");
            if (name != null) {
                tftBoard = new TftBoard(name);
                frame.remove(collectionOptions);
                frame.remove(imageLabel);
                frame.add(boardOptions, BorderLayout.NORTH);
                updateBoard();
            }
        }
    }

    //class for view board button
    private class ViewBoard implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for view board button
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!collection.isEmpty()) {
                String name = JOptionPane.showInputDialog("Enter a board name");
                if (name != null) {
                    tftBoard = collection.getBoard(name);

                    frame.remove(collectionOptions);
                    frame.remove(imageLabel);
                    frame.add(boardOptions, BorderLayout.NORTH);
                    displayBoard();

                    frame.revalidate();
                    frame.repaint();
                }

            } else {
                JOptionPane.showMessageDialog(frame,"There are no boards to view");
            }
        }
    }

    //class for add unit button
    private class AddUnit implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for add unit button
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField name = new JTextField();
            JTextField trait = new JTextField();
            JTextField row = new JTextField();
            JTextField col = new JTextField();

            Object[] fields = {
                    "name", name, "trait", trait, "Row", row, "Col", col
            };

            frame.remove(board);

            int option = JOptionPane.showConfirmDialog(null, fields, "Add Unit", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    String unitName = name.getText();
                    String unitTrait = trait.getText();
                    int unitRow = Integer.parseInt(row.getText());
                    int unitCol = Integer.parseInt(col.getText());

                    tftBoard.addUnit(new Units(unitName, unitTrait), unitRow, unitCol);

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(frame, "Invalid row or col. Please enter an integer.");
                } catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(frame, "Row or Col out of bounds.");
                }
            }
            updateBoard();
        }
    }

    //MODIFIES: this
    //EFFECTS: removes board and repaints to update board state
    private void updateBoard() {
        displayBoard();
        frame.revalidate();
        frame.repaint();
    }

    //class for remove unit button
    private class RemoveUnit implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for remove unit button
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField row = new JTextField();
            JTextField col = new JTextField();

            Object[] fields = {
                    "Row", row,
                    "Col", col
            };
            frame.remove(board);

            int option = JOptionPane.showConfirmDialog(null,
                    fields, "Remove Unit", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int unitRow = Integer.parseInt(row.getText());
                    int unitCol = Integer.parseInt(col.getText());

                    tftBoard.removeUnit(unitRow, unitCol);
                    // DISPLAY BOARD
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(frame, "Invalid row or col. Please enter an integer.");
                } catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(frame, "Row or Col out of bounds.");
                }
            }
            updateBoard();
        }
    }

    //class for exit board menu button
    private class ExitBoard implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for exit board button
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(boardOptions);
            frame.remove(board);
            frame.add(imageLabel);
            frame.add(collectionOptions, BorderLayout.NORTH);

            frame.revalidate();
            frame.repaint();
        }
    }

    //class for save board button
    private class SaveBoard implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for save board button
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tftBoard == null) {
                JOptionPane.showMessageDialog(frame, "No board to save");
            } else {
                collection.addToCollection(tftBoard);
                JOptionPane.showMessageDialog(frame,"Board successfully saved!");
            }
        }
    }

    //class for save collection button
    private class SaveCollection implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for save collection button
        @Override
        public void actionPerformed(ActionEvent e) {

            if (!collection.isEmpty()) {
                try {
                    writer.open();
                    writer.write(collection);
                    writer.close();
                    JOptionPane.showMessageDialog(frame, "Collection saved");
                } catch (FileNotFoundException f) {
                    JOptionPane.showMessageDialog(frame, "Unable to save collection");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Nothing to save");
            }
        }
    }

    //class for load collection button
    private class LoadCollection implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for load collection button
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                collection = reader.read();
                JOptionPane.showMessageDialog(frame, "Your collection has been loaded");
            } catch (IOException f) {
                JOptionPane.showMessageDialog(frame, "Unable to read file" + JSON_STORE);
            }
        }
    }

    //class for remove board button
    private class RemoveBoard implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for remove board button
        @Override
        public void actionPerformed(ActionEvent e) {

            if (!collection.isEmpty()) {
                String name = JOptionPane.showInputDialog(frame, "Please enter then name of "
                        + "the board you would like to remove");
                if (name != null) {
                    if (collection.isInCollection(name)) {
                        collection.removeFromCollection(name);
                        JOptionPane.showMessageDialog(frame, "Board " + name + " has been removed");
                    } else {
                        JOptionPane.showMessageDialog(frame, name + " not in collection");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame,"Collection is Empty");
            }

        }
    }

    //a class for view collection button
    private class ViewCollection implements ActionListener {

        //MODIFIES: this
        //EFFECTS: gives action for view collection button
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!collection.isEmpty()) {
                JOptionPane.showMessageDialog(frame, collection.toString());
            } else {
                JOptionPane.showMessageDialog(frame, "Collection is Empty");
            }
        }
    }
}