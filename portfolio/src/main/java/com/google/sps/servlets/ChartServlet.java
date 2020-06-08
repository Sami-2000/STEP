
package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns gender representation data as a JSON object, e.g. {"2016": 5.3, "2017": 8.6}] */
@WebServlet("/chart-data")
public class ChartServlet extends HttpServlet {

  private LinkedHashMap<Integer, Double> percentRepresentation = new LinkedHashMap<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/women_board_members.csv"));
    String line = scanner.nextLine();
    while (scanner.hasNextLine()) {
      line = scanner.nextLine();
      String[] cells = line.split(",");

      Integer year = Integer.valueOf(cells[0]);
      Double percentage = Double.parseDouble(cells[1]);

      percentRepresentation.put(year, percentage);
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(percentRepresentation);
    response.getWriter().println(json);
  }
}