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

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    ArrayList<String> messages = new ArrayList<>();
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    //Intialize a new array to be filled with comments as JSON Objects
    String[] json_comment_array = new String[messages.size()];


    for(int i = 0; i < messages.size(); i++){
        Date current_date = new Date();
        Comment c = new Comment("None", messages.get(i), current_date);
        String comment_as_json = commentToJson(c);
        json_comment_array[i] = comment_as_json;
        
    }
    //our finished json 
    String json = new Gson().toJson(json_comment_array);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    Date current_date = new Date();
    String text = getParameter(request, "comment-input", "");
    Comment c = new Comment("None", text, current_date);
    String comment_as_json  = commentToJson(c);
    messages.add(comment_as_json);


    // Respond with the result.
    response.setContentType("application/json;");
    response.getWriter().println(comment_as_json);
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
}
