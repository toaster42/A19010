/**
 * @author Michael P. Troester
 * @version 1.01 - 10/16/2015
 * @studentid 5061001
 * @email michaelp.troester@gmail.com
 * @assignment.number A1900008
 * @screenprint <a href='A1900008.gif'>ScreenPrint</a>
 * @prgm.usage Called directly from OS
 * @see <a href='http://jcouture.net/cisc190/A19008.php' target='_blank'>Program Specification</a>
 * @see <br><a href='http://docs.oracle.com/javase/8/docs/technotes/guides/Javadoc/index.html' target='_blank'>Javadoc
 * Documentation</a>
 */
public interface InetTemplate {

    boolean fileExists(String strFileName);
    String getFromFile(String strFileName) throws Exception;
    String getPREData(String strPage);
    String getRegEx(String strInput, String strPattern);
    String getURLRaw(String strURL) throws Exception;
    String properCase(String strInput);
    void saveToFile(String strFileName, String strContent)throws Exception;

}
