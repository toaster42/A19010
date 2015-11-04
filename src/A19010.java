import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Michael P. Troester
 * @version 1.01 - 11/1/2015
 * @studentid 5061001
 * @email michaelp.troester@gmail.com
 * @assignment.number A19010
 * @screenprint <a href='A19010.gif'>ScreenPrint</a>
 * @sampleoutput <a href='../data/commands.txt'>Sample Output</a>
 * @prgm.usage Called directly from OS
 * @see <a href='https://sites.google.com/site/jc2015sp190/course-overview/module-10/module-10-assignment' target='_blank'>Program Specification</a>
 * @see <br><a href='http://docs.oracle.com/javase/8/docs/technotes/guides/Javadoc/index.html' target='_blank'>Javadoc
 * Documentation</a>
 */
public class A19010 extends JDialog {
    private JPanel contentPane;
    private JButton btnDownloadSchemas;
    private JButton btnDropTable;
    private JButton btnCreateTable;
    private JTextField txtInput;
    private JLabel lblStatus;
    final String strSQLCommandFileName = "data/commands.txt";

    /**
     * The purpose of this constructor is to set up the JDialog when an object of the class is instantiated.
     */
    public A19010() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnDownloadSchemas);

        btnDownloadSchemas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDownloadSchemas();
            }
        });

        btnDropTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDropTable();
            }
        });

        btnCreateTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreateTable();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * The purpose of this method is to take the file name from the textbox and apply the appropriate prefix and suffix
     * to build the URL and the file name on disk.  For example, if the textbox contained "students" the URL would be
     * "http://jcouture.net/data/schema_students.txt" and the file name would be "data/schema_students.txt".  This method
     * downloads the file and stores it in the /data folder.  Then it sends a string to the status() method saying
     * that that files have been successfully downloaded.
     */
    private void onDownloadSchemas() {
        String strSchemaURL = "";
        String strSchema = "";
        String strSchemaFile = "data/schema_" + txtInput.getText() + ".txt";
        strSchemaURL = "http://jcouture.net/" + strSchemaFile;
        //System.out.println("Downloading " + strSchemaURL);        //testing
        INET inet = new INET();

        try {
            strSchema = inet.getURLRaw(strSchemaURL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        status("Schema downloaded");
        //System.out.println("Schema: " + strSchema);               //testing
        try {
            inet.saveToFile(strSchemaFile, strSchema);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //dispose();
    }

    /**
     * The purpose of this method is to take the name of the table from the text box and build an SQL command to drop
     * that table. It then appends that command to a file called "data/commands.txt" so that it can be reviewed later.
     *
     * Test it with a table name of "grades". Send a string to the status() method saying that the table has been
     * dropped.  Include the name of the table in the message.
     */
    private void onDropTable() {
        String strTable = txtInput.getText();
        String strSQLCommand = "DROP TABLE " + strTable;
        INET inet = new INET();

        try {
            inet.appendToFile(strSQLCommandFileName, strSQLCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        status("Table \"" + strTable + "\" Dropped");
        //dispose();
    }

    /**
     * The purpose of this method is to fire when the "Create Table" button is clicked.  It calls createTableSQL(String strTable)
     */
    private void onCreateTable() {
        createTableSQL(txtInput.getText());
    }

    /**
     * The purpose of this method is to  accept a string as a parameter and then build a filename out of the string
     * parameter by adding a prefix of "data/schema_" and a suffix of ".txt".  It then checks to see if the file exists
     * using the INET class.  If it does not exist, it calls popup() with a message that says that the file does not
     * exist along with the filename.<br>
     *
     * Next it opens the file as a text file, and creates a sql command that will create a table in the database.  Once
     * it has created the SQL command as a string, it append it to a file called "data/commands.txt" for review.  Latstly
     * it sends a string including the name of the table to the status() method saying that the table has been created.
     *
     * @param strTable The name of the table to be created.
     */
    private void createTableSQL(String strTable) {
        //popup("Creating table: " + strTable);                     //testing
        String strSchemaFile = "data/schema_" + strTable + ".txt";  //format schema filename
        String strFields="";

        INET inet = new INET();
        if(!inet.fileExists(strSchemaFile)){                        //if schema file doesn't exist
            popup("File " + strSchemaFile + " does not exists.");
            /*try {
                System.out.println(inet.getURLRaw("http://jcouture.net/" + strSchemaFile));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }*/
        }

        else {
            try {
                File myFile = new File(strSchemaFile);                  //open schema file
                String[] staSchemaFields;
                if (myFile.exists()) {
                    // Yea! File Exists (pg 249)
                    Scanner inputFile = new Scanner(myFile);
                    // Initialize a Counter
                    int intCount = 0;
                    while (inputFile.hasNext()) {
                        String strRecord = inputFile.nextLine();
                        //System.out.println(strRecord);                //testing
                        intCount++;
                        if (intCount > 10) {                              //skip first 10 lines of schema file
                            staSchemaFields = strRecord.split(" ", 2);  //split schema file line apart by spaces
                            //System.out.println();                     //testing
                        /*
                        * Since every field in this assignment are varchar's, cut the first 8 characters ("varchar("),
                        * and then everything after and including the ")"
                        * */
                            strFields = strFields + staSchemaFields[0] + " CHAR(" +
                                    staSchemaFields[1].toString().substring(8, staSchemaFields[1].toString().indexOf(')'))
                                    + "), ";

                        }
                    }
                        if (strFields.length() > 2) {
                            //int intCutoff = strFields.length() - 2;
                            strFields = strFields.substring(0, (strFields.length() - 2));  //remove the last ", "
                        }
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String strSQLCommand = "CREATE TABLE " + strTable + "(" + strFields + ")";
        try {
            inet.appendToFile(strSQLCommandFileName, strSQLCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }

        status("Table \"" + strTable + "\" Created.");
    }

    /**
     * The purpose of this method is to close the JDialog if needed
     */
    private void onCancel() {
        dispose();
    }

    /**
     * The purpose of this method is to accept a string message called strStatus and will update the lblStatus variable with it.
     *
     * @param strStatus the status message to update on the status txtlabel.
     */
    private void status(String strStatus) {
        lblStatus.setText(strStatus);
    }

    /**
     * The purpose of this method is to accept a string message called strStatus and will display a JOptionPane with the message.
     *
     * @param strStatus the status message to update on the JDialog.
     */
    private void popup(String strStatus) {
        //Create a new dialogPopup object
        dialogPopup popup = new dialogPopup();
        //Set the status message in the popup's txtlbl
        popup.lblPopup.setText(strStatus);
        popup.pack();
        //display the popup
        popup.setVisible(true);
        //System.exit(0);
    }

    /**
     * The purpose of this method is to provide an entry point to the program.  It takes no arguments, though it does
     * accept them so that it doesn't face a runtime error.
     * @param args Placeholder for arguments passed to the application.
     */
    public static void main(String[] args) {
        A19010 dialog = new A19010();
        dialog.pack();
        dialog.setTitle("A19010 - Michael P. Troester");
        dialog.setVisible(true);
        System.exit(0);
    }
}
