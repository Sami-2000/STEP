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
      ['I have lived in the same house for my entire life!', 
      'I cage-dived with great white sharks.', 'Hablo español.', 
      'I used to perform on the flying trapeze.'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// Causes the bubble to slide down the page.
function bubbleFall() {
    // Find the bubble.
    const bubbleElem = document.getElementById('bubble');
    let height = 0;
    // Make the movement visible and fluid by moving every 10 milliseconds;
    const id = setInterval(lowerBubbleOnePixel, 10);
    // For each movement, move the element one pixel down. At the bottom, stop moving.
    function lowerBubbleOnePixel() {
        if (height == 1000) {
            clearInterval(id);
        }
        else {
            height++;
            bubbleElem.style.top = height + 'px';
        }
    }
}

/**
 * Get comments from the data servlet.
 * @param {ArrayList<String>} comments
 */
function getComments() {
  const userNumComments = document.getElementById('number-of-comments').value;
  let baseUrl = '/data?number-of-comments=';
  let url = baseUrl + userNumComments.toString();

  fetch(url).then(response => response.json()).then((comments) => {
    console.log(comments);

    const commentSectionContainer = document.getElementById('comment-section-container');
    commentSectionContainer.innerHTML = '';
    
    for(const comment of comments) {
      commentSectionContainer.appendChild(createCommentElement(comment));
    }
  });
}

/**
 * Creates an <comment-container> element from input text.
 * @param {String} text
 * @return {<div id="comment-container">} commentContainer
 */
function createCommentElement(text) {
  const commentContainer = document.createElement('div');
  commentContainer.className = 'comment-container';
  commentContainer.innerText = text;
  return commentContainer;
}
