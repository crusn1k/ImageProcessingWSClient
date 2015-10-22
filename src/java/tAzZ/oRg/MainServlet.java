/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tAzZ.oRg;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 *
 * @author n1sh1kanT
 */
public class // <editor-fold defaultstate="collapsed" desc="comment">
        MainServlet// </editor-fold>
 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         response.setContentType( "application/octet-stream" );
         try {
                //get the image file from the user
             int temp=0;
             String efffect="";
           if (ServletFileUpload.isMultipartContent(request)) {
           FileItemFactory factory = new DiskFileItemFactory();
	   ServletFileUpload upload = new ServletFileUpload(factory);
	   List items = null;
	   try {
		   items = upload.parseRequest(request);
	   } catch (FileUploadException e) {
               System.out.println(e);
	   }
	   Iterator itr = items.iterator();
	   while (itr.hasNext()) {
	   FileItem item = (FileItem) itr.next();
	   if (!item.isFormField()) {
                String itemName = item.getName();
                File savedFile = new File("/home/nishikant/"+itemName);
                InputStream is=item.getInputStream();
                byte[] image=new byte[(int)item.getSize()];
                is.read(image);
                byte[] img=new byte[image.length];
                for(int i=0;i<image.length;++i)img[i]=image[i];
                ByteArrayInputStream bais=new ByteArrayInputStream(img);
                BufferedImage bufimg=ImageIO.read(bais);
              
                //parse the wsdl and get the details from it
                String wsdlString=new Controller().getWSDL(efffect, bufimg.getWidth(), bufimg.getHeight());
                if(wsdlString.equalsIgnoreCase("null"))
                {
                    response.sendRedirect("ImageUploade.jsp");
                }
                else{
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
               // System.out.println(info[0]);
                info[1]=ss.substring(ss.indexOf("name=\"")+6,ss.indexOf("\">"));
               // System.out.println(info[1]);
                info[2]=ss.substring(ss.indexOf("port name=\"")+11,ss.indexOf("\" binding=\""));
               // System.out.println(info[2]);
                info[3]=ss.substring(ss.indexOf("<message name=\"")+15,ss.indexOf("\">\n<part"));

                //teh dii part :D

                String ns=info[0];
                String svcName=info[1];
                QName svcQName=new QName(ns,svcName);
                Service service=Service.create(wsdl, svcQName);
                String portName=info[2];
                QName portQName=new QName(ns,portName);
                Dispatch<SOAPMessage> dispatch =service.createDispatch(portQName,SOAPMessage.class, Service.Mode.MESSAGE);
                SOAPMessage soapMsg=MessageFactory.newInstance().createMessage();
                SOAPPart soapPart=soapMsg.getSOAPPart();
                SOAPEnvelope soapEnv=soapPart.getEnvelope();
                SOAPBody soapBody=soapEnv.getBody();
                SOAPHeader soapHeader=soapEnv.getHeader();
                SOAPFactory soapFactory=SOAPFactory.newInstance();
                Name bodyName=soapFactory.createName(info[3],"m",ns);
                SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);
                Name name=soapFactory.createName("image");
                Name name1=soapFactory.createName("effect");
                SOAPElement param = bodyElement.addChildElement(name);
                SOAPElement effect=bodyElement.addChildElement(name1);
                String s=Base64.encode(image);
                param.addTextNode(s);
                effect.addTextNode(efffect);
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
             
                //redirect user to the download page
                new Controller().freeResource(wsdlString, bufimg.getWidth(), bufimg.getHeight());
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
             else{
               if(item.getFieldName().equalsIgnoreCase("effect")){
                  efffect=item.getString();
               }
             }
           System.out.println(efffect);
	   }

        }

        }
        catch(MalformedURLException e){
                 response.sendRedirect("ImageUploade.jsp");
        }
     
         catch(SOAPException e)
         {
            response.sendRedirect("ImageUploade.jsp");
         }
         catch(Base64DecodingException e)
         {
            response.sendRedirect("ImageUploade.jsp");
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
