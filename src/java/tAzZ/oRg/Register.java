/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tAzZ.oRg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author n1sh1kanT
 */
public class Register extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
             if(!request.getParameter("t1").equals("")&&!request.getParameter("t2").equals("")&&!request.getParameter("t3").equals("")&&(request.getParameter("t3").indexOf("@")!=-1)&&!request.getParameter("t4").equals("")&&!request.getParameter("ta1").equals("")&&!request.getParameter("r1").equals("")){
           Connection conn;
           Class.forName("oracle.jdbc.driver.OracleDriver");
           conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "SYSTEM", "imbagaming");
           String un=request.getParameter("t1");
           String pass=request.getParameter("t2");
           String email=request.getParameter("t3");
           String address=request.getParameter("ta1");
           String sex=request.getParameter("r1");
           String occ=request.getParameter("t4");
           Statement s=conn.createStatement();
           String sql=("INSERT INTO REGISTRATION VALUES('"+un+"','"+pass+"','"+email+"','"+address+"','"+sex+"','"+occ+"')");
           s.execute(sql);
           conn.close();
           response.sendRedirect("login.htm");
           }
           else{out.println("theek se form bhar gadhe!!!");
            }

           }
        catch(Exception e)
             {
            System.out.println(e);
        } finally {
            out.close();
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
