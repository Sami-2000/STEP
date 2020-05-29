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
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['I have lived in the same house for my entire life!', 'I cage-dived with great white sharks.', 'Hablo español.', 'I used to perform on the flying trapeze.'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

//Causes the bubble to slide down the page.
function bubbleFall() {
    //Find the bubble.
    var elem = document.getElementById('bubble');
    var pos = 0;
    //Make the mobement visible and fluid by moving every 10 milliseconds;
    var id = setInterval(frame, 10)
    //For each movement, move the element one pixel down. At the bottom, stop moving.
    function frame(){
        if (pos == 1000){
            clearInterval(id);
        }
        else{
            pos++;
            elem.style.top = pos + 'px';
        }
    }
}
