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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Date;
import com.google.sps.data.Comment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    ArrayList<String> messages = new ArrayList<>(Arrays.asList("Hey", "Hi", "Whats up", "Hows it goin"));
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

    private String commentToJson(Comment comment) {
    Gson gson = new Gson();

    String json = gson.toJson(comment);
    return json;
  }
}
