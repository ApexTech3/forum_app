function scrollToCommentForm() {
    let element = document.getElementById('commentInput');
    element.focus();
    element.scrollIntoView({block: 'center', behavior: 'smooth'});

}


function loadReply(content, username) {
    const quotedContent = '<div style="background-color: gray; color: black; padding: 5px; font-style: italic; border-radius: 10px;">' +
        '<strong>' + username + ':</strong> "' + content + '"</div>';

    document.getElementById('quotedContent').innerHTML = quotedContent;


    scrollToCommentForm();
}

function addTag(tagName) {
    const tagsTab = document.getElementById('tagsTab');

    const tagSpan = document.createElement('span');
    tagSpan.textContent = tagName;
    tagSpan.className = 'badge badge-primary';

    tagsTab.appendChild(tagSpan);
    tagsTab.append(document.createTextNode(' '));
}

function gatherTags() {
    const tagsTab = document.getElementById('tagsTab');

    const spans = tagsTab.getElementsByTagName('span');

    const tagsList = [];


    for (var i = 0; i < spans.length; i++) {
        tagsList.push(spans[i].textContent);
    }

    document.getElementById('tagsList').value = tagsList.join(',');
}
function submitPost() {
    gatherTags();
    const form = document.getElementById('newPostForm');
    form.action = "/posts/new";
    form.submit();
}