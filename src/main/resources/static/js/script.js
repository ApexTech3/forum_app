function scrollToCommentForm() {
    document.getElementById('newComment').scrollIntoView();
}


function loadReply(content, username) {
    const quotedContent = '<div style="background-color: gray; color: black; padding: 5px; font-style: italic; border-radius: 10px;">' +
        '<strong>' + username + ':</strong> "' + content + '"</div>';

    document.getElementById('quotedContent').innerHTML = quotedContent;


    scrollToCommentForm();
}

function addTag(tagName) {
    // Get the tags tab
    const tagsTab = document.getElementById('tagsTab');

    // Create a new span element for the tag
    const tagSpan = document.createElement('span');
    tagSpan.textContent = tagName;
    tagSpan.className = 'badge badge-primary';

    // Add the tag to the tab
    tagsTab.appendChild(tagSpan);
    tagsTab.append(document.createTextNode(' '));
}

function gatherTags() {
    // Get the tagsTab div
    const tagsTab = document.getElementById('tagsTab');

    // Get all the spans inside the tagsTab div
    const spans = tagsTab.getElementsByTagName('span');

    // Initialize an empty array to store the tag names
    const tagsList = [];

    // Loop through the spans and add their text content to the tagsList array
    for (var i = 0; i < spans.length; i++) {
        tagsList.push(spans[i].textContent);
    }

    // Set the value of the hidden input field to the tagsList array
    document.getElementById('tagsList').value = tagsList.join(',');
}