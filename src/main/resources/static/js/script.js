function scrollToCommentForm() {
    document.getElementById('newComment').scrollIntoView();
}


function loadReply(content, username) {
    let quotedContent = '<div style="background-color: gray; color: black; padding: 5px; font-style: italic; border-radius: 10px;">' +
        '<strong>' + username + ':</strong> "' + content + '"</div>';

    document.getElementById('quotedContent').innerHTML = quotedContent;


    scrollToCommentForm();
}


