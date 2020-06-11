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
import com.google.sps.data.Comment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap; 

import java.lang.Integer;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    HashMap<Integer, String> cursors = new HashMap<Integer, String>();
    int pageCount = 0;
    
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ArrayList<String> messages = new ArrayList<>();

    String nextPage = request.getParameter("next");
    String cursor = request.getParameter("cursor"); 
    pageCount += 1;
    if(cursor != null && nextPage.equals("false")){
        pageCount -= 2;
        cursor = cursors.get(pageCount);
    }

    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(5);
    if(cursor != null){

        fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
    } else {
        cursors.put(pageCount, "");
    }



    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    if(nextPage == "false"){
        query = new Query("Comment").addSort("timestamp", SortDirection.ASCENDING);
    }
    PreparedQuery resultsPQ = datastore.prepare(query);

    QueryResultList<Entity> results;
    try {
      results = resultsPQ.asQueryResultList(fetchOptions);
    } catch (IllegalArgumentException e) {
        response.sendRedirect("/");
        return;
    }
    for (Entity entity : results) {
            Object user = entity.getProperty("user");
            Object content = entity.getProperty("content");
            Object timestamp = entity.getProperty("timestamp");
            Object fileUrl = entity.getProperty("fileUrl");
            Comment c = new Comment(user, content, fileUrl, timestamp);
            messages.add(commentToJson(c));
    }
    String cursorString = results.getCursor().toWebSafeString();
    cursors.put(pageCount, cursorString);
    messages.add(cursorString);
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String user = "None";
    String text = getParameter(request, "comment-input", "");
    
    Entity comment = commentToEntity("None", text);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(comment);
    response.sendRedirect("/index.html");

  }


    private String commentToJson(Comment comment) {
    Gson gson = new Gson();

    String json = gson.toJson(comment);
    return json;
  }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
            return value;
  }

  private Entity commentToEntity(String user, String content){
    Entity commentEntity = new Entity("Comment");
    long current_time = System.currentTimeMillis();
    commentEntity.setProperty("user", user);
    commentEntity.setProperty("timestamp", current_time);
    commentEntity.setProperty("content", content);
    return commentEntity;
  }

}
