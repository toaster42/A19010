import java.sql.*;

/**
 * @author Michael P. Troester
 * @version 1.01 - 10/28/2015
 * @studentid 5061001
 * @email michaelp.troester@gmail.com
 * @assignment.number PACKAGE_NAME
 * @screenprint <a href='PACKAGE_NAME.gif'>ScreenPrint</a>
 * @prgm.usage Called directly from OS
 * @link <a href='http://jcouture.net/cisc190/PACKAGE_NAME.php'>Program Specification</a>
 * @link <br><a href='http://docs.oracle.com/javase/8/docs/
 * technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 * IntelliJ Template 14.10 - 10/19/2014
 */

public class dbUpdate                                              // implements DBTemplate
{
    // Class Level Variables
    Connection dbConn;
    Statement  dbCmdText;
    ResultSet  dbRecordset;
    String     dbTable;
    String     dbKeyField;

    public dbUpdate()
    {
    }

    public Boolean openConnection(String strDataSourceName)
    {
        Boolean blnStatus=false;
        try
        {
            dbConn=DriverManager.getConnection("jdbc:derby:" +
                    strDataSourceName + ";create=true");
            if (dbConn == null)
            {
                status("Connection Failed");
            }
            else
            {
                status("Connection Successful");
                // pg 1062 - to enable the getRecordCount method
                dbCmdText = dbConn.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                blnStatus = true;
            }
        }
        catch (Exception e)
        {
            status("Problems opening connection");
        }
        return blnStatus;
    }

    public Boolean execute(String strSQL)
    {
        Boolean blnStatus = false;

        try
        {
            dbCmdText.execute(strSQL);
            blnStatus = true;
        }
        catch (Exception ex)
        {
            status("Execute command failed " + strSQL);

        }
        return blnStatus;
    }

    public Boolean query(String strSQL)
    {
        Boolean blnStatus = false;
        try
        {
            dbRecordset = dbCmdText.executeQuery(strSQL);
            blnStatus = true;
        }
        catch (Exception ex)
        {
            status("Query Failed " + strSQL);
        }
        return blnStatus;
    }

    public Boolean addRecord(String strTable,
                             String strKeyName, String strKeyContents)
    {
        String strSQL ="";
        Boolean blnStatus = false;
        try
        {
            // check to see if the record exists
            dbCmdText = dbConn.createStatement();
            strSQL = "SELECT * FROM " + strTable + " WHERE " +
                    strKeyName + "='" + strKeyContents + "'";
            query(strSQL);
            if(!moreRecords())
            {
                // the record does not exist, therefore needs to be added to the table
                strSQL = "INSERT INTO " + strTable + " (" + strKeyName +
                        ") VALUES ('" + strKeyContents + "')";
                execute(strSQL);
                status("Record added");
                blnStatus = true;
            }
            else
            {
                status("Record NOT added " + strSQL);
            }
        }
        catch (Exception e)
        {
            status("SELECT command failed " + strSQL);
        }
        return blnStatus;
    }

    public Boolean moreRecords()
    {
        Boolean blnStatus = false;
        try
        {
            blnStatus = dbRecordset.next();
        }
        catch (Exception e)
        {
            blnStatus = false;
        }
        return blnStatus; // only one RETURN in each function!
    }

    public String getField(String strFieldName)
    {
        String strRet="";
        try
        {
            strRet = dbRecordset.getString(strFieldName);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return strRet;
    }

    public void close()
    {
        try
        {
            dbRecordset.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Boolean setField(
            String strTable,
            String strKeyName,String strKeyContents,
            String strFieldName, String strFieldContents)
    {
        Boolean blnStatus = false;

        // goal is = UPDATE customer SET city='SAN DIEGO' WHERE customerID='2100'

        String strSQL = "UPDATE " + strTable + " SET " + strFieldName + "='"
                + strFieldContents + "' " +
                " WHERE " + strKeyName + "='" + strKeyContents + "' ";
        execute(strSQL);
        return blnStatus;
    }

    public int getRecordCount(String strTable)
    {
        int intRows = 0;
        try
        {
            dbRecordset.last();
            intRows = dbRecordset.getRow();
            dbRecordset.first();
        } catch (Exception e)
        {
            status("problem using getRecordCount");
        }
        return intRows;
    }

    public void status(String strMsg)
    {
        System.out.println(strMsg);
    }
}