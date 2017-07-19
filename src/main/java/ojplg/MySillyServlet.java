package ojplg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MySillyServlet extends HttpServlet {

    static final String MESSAGE = "message";

    private String message;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("The silly servlet init");

        super.init(config);
        message = config.getInitParameter(MESSAGE);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("SillyServlet.doGet");

        String pathInfo = req.getPathInfo();

        String responseString = handlePath(pathInfo);

        PrintWriter writer = resp.getWriter();
        writer.write(responseString);
        writer.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private String handlePath(String pathInfo){
        switch(pathInfo) {
            case "/foo":
                return "In the foo branch: " + message;
            case "/bar":
                return "In the bar branch: " + message;
            default:
                return "In unrecognized path (" + pathInfo + ")";
        }
    }

}