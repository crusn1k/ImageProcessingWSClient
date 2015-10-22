/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tAzZ.oRg;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author nishikant
 */
public class PanoramaServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/octet-stream");
        try {
            if(ServletFileUpload.isMultipartContent(request))
            {
                FileItemFactory fif=new DiskFileItemFactory();
                ServletFileUpload sfu=new ServletFileUpload(fif);
                List items=sfu.parseRequest(request);
                Iterator itr=items.iterator();
                FileItem image1=null,image2=null;
                if(itr.hasNext())
                    image1=(FileItem)itr.next();
                if(itr.hasNext())
                    image2=(FileItem)itr.next();
                byte[] bimage1=new byte[(int)image1.getSize()];
                byte[] bimage2=new byte[(int)image2.getSize()];

                InputStream in=image1.getInputStream();
                in.read(bimage1);
                in.close();
                in=image2.getInputStream();
                in.read(bimage2);
                 byte[] img1=new byte[bimage1.length];
                for(int i=0;i<bimage1.length;++i)img1[i]=bimage1[i];
                ByteArrayInputStream bais=new ByteArrayInputStream(img1);
                BufferedImage bufimg1=ImageIO.read(bais);
                 byte[] img2=new byte[bimage2.length];
                for(int i=0;i<bimage2.length;++i)img2[i]=bimage2[i];
                ByteArrayInputStream bais1=new ByteArrayInputStream(img2);
                BufferedImage bufimg2=ImageIO.read(bais1);
                ProgressListener progressListener = new ProgressListener(){
   public void update(long pBytesRead, long pContentLength, int pItems) {
       System.out.println("We are currently reading item " + pItems);
       if (pContentLength == -1) {
           System.out.println("So far, " + pBytesRead + " bytes have been read.");
       } else {
           System.out.println("So far, " + pBytesRead + " of " + pContentLength
                              + " bytes have been read.");
       }
   }
};
sfu.setProgressListener(progressListener);
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection wsdircon=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "imbagaming");
                PreparedStatement st=wsdircon.prepareStatement("SELECT wsdl,count FROM WEBSERVICEDATABASE WHERE panorama='yes' and status='up' ORDER BY count");
                ResultSet rs=st.executeQuery();
                String wsdlString="";
                int count=0;
                if(rs.next())
                {
                    System.out.println(wsdlString=rs.getString(1));
                    count=rs.getInt(2);
                }
                else
                {
                    response.sendRedirect("effectchoosere.htm");
                }
                PreparedStatement st1=wsdircon.prepareStatement("UPDATE WEBSERVICEDATABASE SET count="+(count+(bufimg1.getWidth()*bufimg1.getHeight()+bufimg2.getWidth()*bufimg2.getHeight()))+"WHERE wsdl=\'"+wsdlString+"\'");
                System.out.println(count);
                st1.execute();

                //parse the wsdl and get the details from it

                URL wsdl=new URL(wsdlString);
                //URL wsdl=new URL("http://localhost:8084/IPWebServices/ImageProcess?wsdl");
                URLConnection conn= wsdl.openConnection();
                InputStream wsdlin=conn.getInputStream();
                BufferedReader bin=new BufferedReader(new InputStreamReader(wsdlin));
                char[] asdf=new char[1024*100];
                bin.read(asdf);
                String ss=new String(asdf);
                bin.close();
                wsdlin.close();
                String[] info=new String[6];
                info[0]=ss.substring(ss.indexOf("targetNamespace=\"")+17,ss.indexOf("\" name"));
                System.out.println(info[0]);
                info[1]=ss.substring(ss.indexOf("name=\"")+6,ss.indexOf("\">"));
                System.out.println(info[1]);
                info[2]=ss.substring(ss.indexOf("port name=\"")+11,ss.indexOf("\" binding=\""));
                System.out.println(info[2]);
                info[3]=ss.substring(ss.indexOf("<message name=\"")+15,ss.indexOf("\">\n<part"));

                //DII

                String svcName=info[1];
                String ns=info[0];
                QName svcQname=new QName(ns,svcName);
                String portName=info[2];
                QName portQname=new QName(ns,portName);
                Service service=Service.create(wsdl, svcQname);
                Dispatch<SOAPMessage> dispatch=service.createDispatch(portQname, SOAPMessage.class, Service.Mode.MESSAGE);
                SOAPMessage soapMsg=MessageFactory.newInstance().createMessage();
                SOAPPart soapPart=soapMsg.getSOAPPart();
                SOAPEnvelope soapEnv=soapPart.getEnvelope();
                SOAPBody soapBody=soapEnv.getBody();
                Name bodyName=SOAPFactory.newInstance().createName("gogogo_1","m",ns);
                SOAPBodyElement bodyElement=soapBody.addBodyElement(bodyName);
                Name param1=SOAPFactory.newInstance().createName("image1");
                Name param2=SOAPFactory.newInstance().createName("image2");
                Name param3=SOAPFactory.newInstance().createName("effect");
                SOAPElement seimage1=bodyElement.addChildElement(param1);
                SOAPElement seimage2=bodyElement.addChildElement(param2);
                SOAPElement effect=bodyElement.addChildElement(param3);
                seimage1.addTextNode(Base64.encode(bimage1));
                seimage2.addTextNode(Base64.encode(bimage2));
                effect.addTextNode("panorama");
                SOAPMessage resp=dispatch.invoke(soapMsg);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();

                //handle the response from web service to obtain the processed image

                resp.writeTo(baos);
                String saveImg=new String(baos.toByteArray());
                int lastI=saveImg.lastIndexOf("<return>")+8;
                int firstI=saveImg.indexOf("</return>");
                saveImg=saveImg.substring(lastI, firstI);
                byte[] dwnld=new byte[saveImg.length()];
                dwnld=Base64.decode(saveImg);
                //decrease the count in the service directory

                PreparedStatement stc=wsdircon.prepareStatement("SELECT count FROM WEBSERVICEDATABASE WHERE wsdl='"+wsdlString+"'");
                rs=stc.executeQuery();
                if(rs.next())
                    count=rs.getInt(1);
                count-=(bufimg1.getWidth()*bufimg1.getHeight()+bufimg2.getWidth()*bufimg2.getHeight());
                if(count<0)count=0;
                PreparedStatement st2=wsdircon.prepareStatement("UPDATE WEBSERVICEDATABASE SET count="+(count)+"WHERE wsdl=\'"+wsdlString+"\'");
                System.out.println(count);
                st2.execute();
                wsdircon.close();
                //redirect user to the download page

                ServletOutputStream op       = response.getOutputStream();
                ServletContext      context  = getServletConfig().getServletContext();
                String              mimetype = context.getMimeType( "application/octet-stream" );
                response.setContentLength( dwnld.length );
                response.setHeader( "Content-Disposition", "attachment; filename=\"" + "processedImage.jpg" + "\"" );
                int length   = 0;
                byte[] bbuf = new byte[1000];
                ByteArrayInputStream iin=new ByteArrayInputStream(dwnld);
                while ((iin != null) && ((length = iin.read(bbuf)) != -1))
                {
                  op.write(bbuf,0,length);
                }
                //in.close();
                iin.close();
                op.flush();
                op.close();

            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        } finally {

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
