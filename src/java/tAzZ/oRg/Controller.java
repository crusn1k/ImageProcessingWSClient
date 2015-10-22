/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tAzZ.oRg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author nishikant
 */
public class Controller {
public String getWSDL(String effect,int w,int h)
    {
    String wsdlString="null";
    try{
    Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection wsdircon=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "imbagaming");
                PreparedStatement st=wsdircon.prepareStatement("SELECT wsdl,count FROM WEBSERVICEDATABASE WHERE "+effect+"='yes' and status='up' ORDER BY count");
                ResultSet rs=st.executeQuery();
               int count=0;
                if(rs.next())
                {
                    wsdlString=rs.getString(1);
                    count=rs.getInt(2);
                }

                PreparedStatement st1=wsdircon.prepareStatement("UPDATE WEBSERVICEDATABASE SET count="+(count+w*h)+"WHERE wsdl=\'"+wsdlString+"\'");
                //System.out.println(count);
                st1.execute();
        }
    catch(Exception e)
    {
        System.out.println(e);
    }
                return wsdlString;
}
public void freeResource(String wsdlString,int w,int h)
    {
    try{
        int count=0;
        Connection wsdircon=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "imbagaming");
        ResultSet rs=null;
        PreparedStatement stc=wsdircon.prepareStatement("SELECT count FROM WEBSERVICEDATABASE WHERE wsdl='"+wsdlString+"'");
                rs=stc.executeQuery();
                if(rs.next())
                    count=rs.getInt(1);
                count-=w*h;
                if(count<0)count=0;
                PreparedStatement st2=wsdircon.prepareStatement("UPDATE WEBSERVICEDATABASE SET count="+(count)+"WHERE wsdl=\'"+wsdlString+"\'");
               // System.out.println(count);
                st2.execute();
                wsdircon.close();
        }
    catch(Exception e)
    {
        System.out.println(e);
    }
}
}
