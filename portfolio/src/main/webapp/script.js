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
  const rand_padding = Math.floor(Math.random() * 100)
  const fact_element = document.createElement('div');
  fact_element.setAttribute("class", "fact-text");
  fact_element.setAttribute("style", "margin-left: "  + rand_padding.toString() + "%;");
  fact_element.innerText = "\n" + fact;

  if (Math.floor(Math.random() * 2) == 0) {
      document.getElementById("fact-box-1").append(fact_element);
  } else {
      document.getElementById("fact-box-2").append(fact_element);
  }

}

async function fetchCommentContent() {
  fetch('/data')  // sends a request to /my-data-url
.then(response => response.json()) // parses the response as JSON
.then((comment_array) => { // now we can reference the fields in myObject!
  console.log(comment_array[1]);
});
}