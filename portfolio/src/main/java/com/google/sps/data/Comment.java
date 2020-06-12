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
package com.google.sps.data;
import java.util.Date;
public final class Comment{
    private final Object user;
    private final Object content;
    private final Object timestamp;
    private final Object fileUrl;
    public Comment(Object _user, Object _content){
        this.user = _user;
        this.content = _content;
        this.timestamp = new Date();
        this.fileUrl = null;
    }
    public Comment(Object _user, Object _content, Object _fileUrl){
        this.user = _user;
        this.content = _content;
        this.fileUrl = _fileUrl;
        this.timestamp = new Date();
    }
    public Comment(Object _user, Object _content, Object _fileUrl, Object _timestamp){
        this.user = _user;
        this.content = _content;
        this.fileUrl = _fileUrl;
        this.timestamp = _timestamp;
    }

    public Object getUser(){
        return this.user;
    }

    public Object getContent(){
        return this.content;
    }

    public Object getFileURL(){
        return this.fileUrl;
    }

    public Object getTimestamp(){
        return this.timestamp;
    }
}