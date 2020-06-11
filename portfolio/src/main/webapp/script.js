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

/**
 * Adds a fact greeting to the page.
 */

window.addEventListener("load", myInit, true); function myInit(){
  checkLogin();
  getBlobstoreURL();
}
  const facts =
      ['I\'ve read all of One Piece', 'I like Winnie the Pooh!', 'Favourite pianist is either Ahmad Jamal or Ryo Fukui', 'Took 2 years of Chinese!', 'My cat\'s name is Kai!', 'I interned at Google!', 'Why are these sideways!?', 'I have 2 brothers!', 'I like to bake pastries!'];

function addFact() {


  // Pick a random greeting.
  if(facts.length == 0){
      alert("I'm all out of facts, sorry!");
      return;
  }
  const factIndex = Math.floor(Math.random() * facts.length)
  const fact = facts[factIndex];
  facts.splice(factIndex, 1);
  // Add it to the page, randomly selecting one of 2 divs to add it to
  const randPadding = Math.floor(Math.random() * 100)
  const factElement = document.createElement('div');
  factElement.setAttribute("class", "fact-text");
  factElement.setAttribute("style", "margin-left: "  + randPadding.toString() + "%;");
  factElement.innerText = "\n" + fact;

  if (Math.floor(Math.random() * 2) == 0) {
      document.getElementById("fact-box-1").append(factElement);
  } else {
      document.getElementById("fact-box-2").append(factElement);
  }

}

async function fetchCommentContent(cursor, next) {
  fetch('/data?cursor=' + cursor + "&next=" + next)  // sends a request to /my-data-url
.then(response => response.json()) // parses the response as JSON
.then((commentArray) => { // now we can reference the fields in myObject!
    var i = 0;
    document.getElementById("comment-area").innerHTML = ""
    for(i = 0; i < commentArray.length - 1; i++){
        var c = JSON.parse(commentArray[i]);
        document.getElementById("comment-area").innerHTML += "<p>" + c.user + "<br>" + c.content + "<img src = '" + c.fileUrl +   "'/><p>";
    }

    document.getElementById("cursor").value = commentArray[commentArray.length -1];
});
}

async function checkLogin() {
    fetch('/log')  // sends a request to /my-data-url
  .then(response => response.json()) // parses the response as JSON
  .then((logged) => { // now we can reference the fields in myObject!
      if(logged.loginUrl != ""){
        console.log(logged);
        document.getElementById("comment-form").setAttribute("hidden", "true");
        document.getElementById("logout-link").setAttribute("hidden", "true");
        document.getElementById("login-link").removeAttribute("hidden");
        document.getElementById("login-link").setAttribute("href", logged.loginUrl);
      } else {
        console.log(logged);
        document.getElementById("comment-form").removeAttribute("hidden");
        document.getElementById("login-link").setAttribute("hidden", "true");
        document.getElementById("logout-link").removeAttribute("hidden");
        document.getElementById("logout-link").setAttribute("href", logged.logoutUrl);
      }
  });
  }

  async function getBlobstoreURL() {
    fetch('/bsupload-url')  // sends a request to /my-data-url
    .then(response => response.text()) // parses the response as JSON
    .then((url) => { // now we can reference the fields in myObject!
      document.getElementById("comment-form").setAttribute("action", url);
    });
  }