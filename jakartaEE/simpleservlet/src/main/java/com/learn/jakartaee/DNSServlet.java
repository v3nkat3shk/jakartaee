package com.learn.jakartaee;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebServlet(name = "DNS Servlet", value = "/dns/*")
public class DNSServlet extends HttpServlet {

    private ConcurrentMap<String, String> ipMap;

    @Override
    public void init() throws ServletException {
        ipMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String domainName = req.getParameter("domainName");
        String ip = req.getParameter("ip");

        if (domainName == null || ip == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ipMap.put(domainName, ip);

        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String domainName = req.getParameter("domainName");

        if (domainName == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        PrintWriter writer = resp.getWriter();
        writer.println(Optional
                .ofNullable(ipMap.get(domainName))
                .orElse(""));
    }
}











































































