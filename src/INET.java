import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class INET implements InetTemplate {

    /**
     * The purpose of this method is to accept a full file path and name and determine if it exists on disk.
     * @param strFileName The file to check
     * @return boolExists
     */
    @Override
    public boolean fileExists(String strFileName) {
        boolean boolExists =false;
        File myFile = new File(strFileName);
        if (myFile.exists()) {
            boolExists = true;
            //System.out.println("boolExists = true");        //Testing
        }
        else {
            //System.out.println("boolExists = false");       //Testing
        }
        return boolExists;
    }

    /**
     * The purpose of this method is to accept a full file path and name as a parameter
     * and check to see if it exists using the fileExists method. Then return the file as a string.
     * @param strFileName The file to check.
     * @return strFileContents The contents of the file.
     * @throws Exception May throw IOException if there is a file access issue.
     */
    @Override
    public String getFromFile(String strFileName) throws Exception {
        ArrayList<String> staList = new ArrayList<>();
        ArrayList <String> staIndex = new ArrayList<>();

        String strFileContents = "";
        File myFile = new File(strFileName);
        if (myFile.exists())
        {
            // Yea! File Exists (pg 249)
            Scanner inputFile = new Scanner(myFile);
            // Initialize a Counter
            int intCount = 0;
            while (inputFile.hasNext())
            {
                String strRecord = inputFile.nextLine();

                //System.out.println(strRecord);              //testing

                //if (intCount > 7) {
                    //write station weather to ArrayList
                    staList.add(strRecord);

                    //write StationID to Index Arraylist
                    staIndex.add(strRecord.substring(7, 11));

                //}
                //System.out.println(staList.get(intCount));      //testing
                //System.out.println(staIndex.get(intCount));     //testing
                intCount++;
            }
            inputFile.close();
            //System.out.println(staList.toString());         //testing
        }
        strFileContents = staList.toString();
        return strFileContents;
    }

    /**
     * The purpose of this method is to accept an HTML web page as a string and extract
     * the data that is between the PRE tags using the getRegEx function.
     * @param strPage The raw page output
     * @return all data between PRE tags
     */
    @Override
    public String getPREData(String strPage) {
        return getRegEx(strPage,"<PRE>(.)*</PRE>");
    }

    /**
     * The purpose of this method is to extract a small string from a larger one using a
     * Regular Expression and the pattern specified as a parameter.
     * @param strInput The string to be searched
     * @param strPattern The pattern to match
     * @return The matching string
     */
    @Override
    public String getRegEx(String strInput, String strPattern) {
        String strRet = "";
        Pattern pattern = Pattern.compile(strPattern,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(strInput);

        while (matcher.find())
        {
            strRet = strRet + "\r\n" + matcher.group();
        }
        if (strRet.length() < 1 )
        {
            strRet = "String Not Found";
        }
        return strRet.trim();
    }

    /**
     * The purpose of this method is to accept a string URL, create a string using the StringBuilder
     * class and return it as one long string.
     * @param strURL The URL to read.
     * @return The raw data read from the URL
     * @throws Exception May throw IOException or MalformedURLException.
     */
    @Override
    public String getURLRaw(String strURL) throws Exception {
        StringBuilder stbContent = new StringBuilder("");
        try
        {
            URL myWebAddress= new URL(strURL);
            URLConnection myConn = myWebAddress.openConnection();
            myConn.setConnectTimeout(0); // to prevent timeout errors
            InputStream myStrIn = myConn.getInputStream();
            BufferedReader inputFile = new
                    BufferedReader(new InputStreamReader(myStrIn));
            while(inputFile.ready())
            {
                // read a line and append it to strContent
                String strRecord = inputFile.readLine() + "\r\n";
                stbContent.append(strRecord);
            }
        }
        catch (MalformedURLException errnum)
        {
            // display error if URL is messed up
            System.out.println(errnum.getMessage());
        }
        catch (IOException errnum)
        {
            // display error if Internet connection is flaky
            System.out.println(errnum.getMessage());
        }
        // At this point strContent contains the
        //    raw HTML code of your web page
        //    or just a blank
        return stbContent.toString();  // return string builder as a string
    }

    /**
     * The purpose of this method is to accept a string and convert the whole string into lower case then upper case
     * only the first letter of each word.
     * @param strInput The string to be formatted.
     * @return The reformatted string.
     */
    @Override
    public String properCase(String strInput) {
        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
        // to be capitalized

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : strInput.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }

    /**
     * The purpose of this method is to accept a string and save it to a local file.
     * @param strFileName The file name to output to.
     * @param strContent The content of the file to write.
     * @throws Exception May throw IOException on file access error.
     */
    @Override
    public void saveToFile(String strFileName, String strContent) throws Exception {
        try (PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter(strFileName, false)))) {
            outputFile.println(strContent);
            outputFile.close();
        } catch (IOException e) {
            System.out.println("Error IOException" + e.getMessage());
        }
    }

    /**
     * The purpose of this method is to accept a filename and append the contents to the file.  The saveToFile method
     * in thisvclass erases the file before creating a new one so this one features the append parameter as explained
     * in chapter four around page 240.
     * @param strFileName The name of the file to append to.
     * @param strContent The content to append.
     * @throws Exception In case there are any file errors.
     */
    public void appendToFile(String strFileName, String strContent) throws Exception {
        //code goes here :-)
        try (PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter(strFileName, true)))) {
            outputFile.println(strContent);
            outputFile.close();
        } catch (IOException e) {
            System.out.println("Error IOException" + e.getMessage());
        }
    }
}
