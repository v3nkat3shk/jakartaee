package com.learn.jakartaee;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(value = "/report", asyncSupported = true)
public class ReportServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(90000);

        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                String name = Optional
                        .ofNullable(asyncContext.getRequest().getParameter("name"))
                        .orElse("");
                try {
                    PrintWriter writer = asyncContext.getResponse().getWriter();
                    writer.println("<html>");
                    writer.println("<body>");
                    writer.println(generateReport(name));
                    writer.println("</body>");
                    writer.println("</html>");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    asyncContext.complete();
                }
            }
        });
    }

    private String generateReport(String name) {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new StringBuilder("<div>")
                .append("<span>")
                .append("name: ")
                .append("</span>")
                .append("<span>")
                .append(name)
                .append("</span>")
                .append("</div>")
                .toString();
    }
}
