// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private final String COMMENT_ENTITY_ID = "Comment";
  private final String TEXT_PARAMETER_KEY = "text";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int numberComments = Integer.parseInt(request.getParameter("number-of-comments"));

    Query query = new Query(COMMENT_ENTITY_ID);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery queryResults = datastore.prepare(query);

    ArrayList<String> comments = new ArrayList<String>();  
  
    Iterable<Entity> resultsIteratable = queryResults.asIterable();
    Iterator<Entity> resultsIterator = resultsIteratable.iterator();
    for (int i = 0; i < numberComments; i++) {
      if (resultsIterator.hasNext()) {
        String commentText = (String) resultsIterator.next().getProperty(TEXT_PARAMETER_KEY);
        comments.add(commentText);
      }
    }
    
    String jsonComments = convertToJsonUsingGson(comments);

    response.setContentType("application/json;");
    response.getWriter().println(jsonComments);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("text-input");

    Entity commentEntity = new Entity(COMMENT_ENTITY_ID);
    commentEntity.setProperty(TEXT_PARAMETER_KEY, comment);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }

  private String convertToJsonUsingGson(ArrayList<String> messages) {
    Gson gson = new Gson();
    String jsonMessages = gson.toJson(messages);
    return jsonMessages;
  }

}
