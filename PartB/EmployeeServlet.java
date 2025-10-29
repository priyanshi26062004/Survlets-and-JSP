import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/companydb";
    private static final String USER = "root";
    private static final String PASS = "yourpassword";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USER, PASS);

            if (id == null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM employee");

                out.println("<h2>Employee List</h2>");
                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Dept</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>"
                            + rs.getString("name") + "</td><td>"
                            + rs.getString("department") + "</td></tr>");
                }
                out.println("</table>");
            } else {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM employee WHERE id = ?");
                ps.setInt(1, Integer.parseInt(id));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    out.println("<h3>Employee Details:</h3>");
                    out.println("ID: " + rs.getInt("id") + "<br>");
                    out.println("Name: " + rs.getString("name") + "<br>");
                    out.println("Department: " + rs.getString("department"));
                } else {
                    out.println("<p>No employee found with ID " + id + "</p>");
                }
            }

            con.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
