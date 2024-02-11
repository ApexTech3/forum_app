function scrollToCommentForm() {
    document.getElementById('newComment').scrollIntoView();
}

// function submitForm() {
//     document.forms[0].submit();
// }

//  function loadReply(content, username) {
//     // Populate the newComment section with the reply content and mention the username
//     let commentForm = document.getElementById('newComment');
//     let contentInput = commentForm.querySelector('input[type="text"]');
//     contentInput.value = '@' + username + ' ' + content;
//
//     // Scroll to the newComment section
//     commentForm.scrollIntoView();
// // }
// <button className="btn btn-xs text-muted has-icon"
//         onClick="loadReply('@{${reply.content}}','@{${reply.getCreatedBy().getUsername()}}')" title="Reply">
//     <i className="fa fa-reply fa-2x"></i>
// </button>