package ui;

import model.BoardCollection;
import model.TftBoard;
import model.Units;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//TFT Board Application
public class TftBoardApp {

    private static final String JSON_STORE = "./data/boardCollection.json";
    private BoardCollection boardCollection;        //name of collection of tft boards
    private TftBoard board;                         //name of tft board
    private Scanner input;                           //create scanner
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;



    /*
     * EFFECTS: Runs TFT Board Application
     */
    public TftBoardApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        boardCollection = new BoardCollection();
        runBoardMaker();
    }

    /*
     * MODIFIES: this
     * EFFECTS: processes user input
     */
    private void runBoardMaker() {
        boolean exitCollection = false;

        while (!exitCollection) {
            collectionOptions();
            String response = input.next();
            response = response.toLowerCase();

            if (response.equals("e")) {
                exitCollection = true;
            } else {
                collectionOptionsHandler(response);
            }
        }
    }

    /*
     * EFFECTS: displays a menu for board options to the user
     */
    private void boardOptions() {
        System.out.print("Would you like to add/remove a unit, view/save board or exit?");
        System.out.print("\na -> add a unit");
        System.out.print("\nr -> remove a unit");
        System.out.print("\nv -> view board");
        System.out.print("\ns -> save board");
        System.out.print("\ne -> exit\n");
    }

    /*
     * EFFECTS: displays a menu for board collection to the user
     */
    private void collectionOptions() {

        input = new Scanner(System.in);

        System.out.print("Would you like to create, remove a board from your collection or "
                + "would you like to view your collection?");
        System.out.print("\nc -> create a new board");
        System.out.print("\nr -> remove a board from collection");
        System.out.print("\nv -> view collection of boards");
        System.out.print("\ns -> save collection");
        System.out.print("\nl -> load collection");
        System.out.print("\ne -> exit\n");
    }


    /*
     * MODIFIES: this
     * EFFECTS: initializes a tft board
     */
    private void createBoard() {

        input = new Scanner(System.in);
        boolean exit = false;
        System.out.print("Enter a board name: ");

        String boardName = input.nextLine();
        board = new TftBoard(boardName);

        while (!exit) {
            boardOptions();
            String command = input.next();

            if (command.equals("e")) {
                exit = true;
            } else if (command.equals("s")) {
                saveBoard(board);
                board = null;
                exit = true;
            } else {
                boardOptionsHandler(command);
            }
        }
    }

    /*
     * REQUIRES: a command
     * MODIFIES: this
     * EFFECTS: processes given commands for board collection
     */
    private void collectionOptionsHandler(String command) {
        if (command.equals("c")) {
            createBoard();
        } else if (command.equals("r")) {
            removeBoard();
        } else if (command.equals("v")) {
            viewCollection();
        } else if (command.equals("s")) {
            saveCollection();
        } else if (command.equals("l")) {
            loadCollection();
        } else {
            System.out.print("Selection is not valid.\n");
        }
    }

    private void loadCollection() {
        try {
            boardCollection = jsonReader.read();
            System.out.println("Loaded MY COLLECTION from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read file: " + JSON_STORE);
        }
    }

    /*
     * REQUIRES: tft board object
     * EFFECTS: adds board if there is board object to add
     */
    private void saveBoard(TftBoard board) {
        if (board == null) {
            System.out.print("There is no board to save.\n");
        } else {
            System.out.print("Your " + board.getName() + " board has been saved\n");
            boardCollection.addToCollection(board);
        }
    }

    /*
     * EFFECTS: saves collection to file
     */
    private void saveCollection() {
        try {
            jsonWriter.open();
            jsonWriter.write(boardCollection);
            jsonWriter.close();
            System.out.println("Your collection of board has been saved to" + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write file" + JSON_STORE);
        }
    }

    /*
     * EFFECTS: if  collection of boards is not empty, it shows the collection
     */
    private void viewCollection() {
        if (boardCollection.isEmpty()) {
            System.out.print("Your collection is empty. There is nothing to view.\n");
        } else {
            System.out.print(boardCollection.toString());
            System.out.println();
            System.out.print("Is there a specific board you would like to view? Y/N ");
            String response = input.next();
            response = response.toLowerCase();

            if (response.equals("y")) {
                viewBoard();
            }
        }
    }

    /*
     * EFFECTS: displays specified board
     */
    private void viewBoard() {
        System.out.print("Which board would you like to view? ");
        input.nextLine();       //consume \n
        String board = input.nextLine();

        System.out.print(boardCollection.getBoardString(board));
    }

    /*
     * REQUIRES: a command
     * MODIFIES: this
     * EFFECTS: processes given commands for tft boards
     */
    private void boardOptionsHandler(String command) {
        if (command.equals("a")) {
            Units unit = createUnit();
            placeUnit(unit);
        } else if (command.equals("r")) {
            removeUnit();
        } else if (command.equals("v")) {
            System.out.print(board.toString());
        } else {
            System.out.print("Selection is not valid.\n");
        }
    }

    /*
     * REQUIRES: unit object
     * MODIFIES: tftboard
     * EFFEECTS: adds a unit onto tft board
     */
    private void placeUnit(Units unit) {
        //get where to place unit on board
        System.out.print("Please enter the row (between 1 and 3): \n");
        while (!input.hasNextInt()) {
            System.out.println("Input an integer. ");
            input.next();
        }
        int row = input.nextInt();

        //check if row is within board size
        if (row > 3 || row < 1) {
            System.out.print("Row must be between 1 and 3\n");
        } else {
            System.out.print("Please enter the column (between 1 and 7): \n");
            while (!input.hasNextInt()) {
                System.out.println("Input an integer. ");
                input.next();
            }
            int column = input.nextInt();

            //check if column is within board size
            if (column > 7 || column < 1) {
                System.out.print("Column must be between 1 and 7\n");
            } else {
                board.addUnit(unit, row, column);
                System.out.print(board.toString());
            }
        }
    }

    /*
     * EFFECTS: creates a new instance of Units class and adds it to the TftBoard
     */
    private Units createUnit() {
        //get user input on unit
        System.out.print("Please enter the Name of the Unit you would like to add:\n");
        String name = input.next();
        System.out.print("Please enter the Trait of the Unit you would like to add:\n");
        String trait = input.next();

        //create instance of unit class
        Units unit = new Units(name, trait);

        return unit;
    }

    /*
     * MODIFIES: this
     * EFFECTS: removes desired unit from the tft board
     */
    private void removeUnit() {
        //check if board is empty
        if (board.isEmpty()) {
            System.out.print("Sorry the board is empty, there is nothing to remove\n");
        } else {
            //get location of unit
            System.out.print("Please enter the row the unit you want to remove is in ");
            while (!input.hasNextInt()) {
                System.out.println("Input an integer. ");
                input.next();
            }
            int row = input.nextInt();
            System.out.print("Please enter the column of the unit you want to remove ");
            while (!input.hasNextInt()) {
                System.out.println("Input an integer. ");
                input.next();
            }
            int column = input.nextInt();

            //check if a unit is there
            if (board.cellIsEmpty(row, column)) {
                System.out.print("There is nothing here to remove\n");
                System.out.print(board.toString());
            } else {
                //remove unit
                board.removeUnit(row, column);
                System.out.print(board.toString());
            }
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: removes desired board from collection of boards
     */
    private void removeBoard() {

        input = new Scanner(System.in);

        //there is nothing to remove
        if (boardCollection.isEmpty()) {
            System.out.print("Sorry your collection is empty, there is nothing to remove.\n");
        } else {
            System.out.print(boardCollection.toString());
            System.out.println();
            //get name of board
            System.out.print("Please enter the name of the board you would like to remove (case sensitive): ");

            String boardName = input.next();

            if (boardCollection.isInCollection(boardName)) {
                //remove board
                boardCollection.removeFromCollection(boardName);
            } else {
                System.out.print("Sorry the board does not exist.\n");
            }
        }
    }
}