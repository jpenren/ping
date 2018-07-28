package ping;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/")
public class PingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String APPLICATION_JSON = "application/json";
    private static final String JSON_TEMPLATE = "{\"hostname\":\"%s\", \"startup\":%d}";
    private final Data data;
    
    public PingServlet() {
        this.data = new Data(getHostname(), System.currentTimeMillis());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setCharacterEncoding("UTF-8");
        
        final PrintWriter writer = resp.getWriter();
        if(supportsJson(req)) {
            resp.setContentType(APPLICATION_JSON);
            final String json = String.format(JSON_TEMPLATE, data.hostname, data.startup);
            writer.println( json );
        } else {
            resp.setContentType("text/plain");
            writer.println(data.hostname);
            writer.println(data.startup);
        }
        resp.flushBuffer();
    }
    
    private String getHostname() {
        try {
            return Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "NA";
        }
    }
    
    private boolean supportsJson(HttpServletRequest req) {
        String acceptHeader = req.getHeader("Accept");
        return APPLICATION_JSON.equalsIgnoreCase(acceptHeader);
    }

    private class Data {
        final String hostname;
        final long startup;

        public Data(String hostname, long startup) {
            this.hostname = hostname;
            this.startup = startup;
        }
    }
    
}
