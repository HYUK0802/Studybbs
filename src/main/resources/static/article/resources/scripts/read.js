const commentForm = document.getElementById('commentForm');
const commentContainer = document.getElementById('commentContainer');

function postComment(content, commentIndex, toFocus, refreshCommentAfter) {
    refreshCommentAfter ??= true;

    const articleIndex = commentForm['articleIndex'].value;
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('articleIndex', articleIndex);
    formData.append('content', content);
    if (commentIndex) {
        formData.append('commentIndex', commentIndex);
    }
    xhr.open('POST', '/article/comment');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                if (xhr.responseText === 'true') {
                    if (toFocus) {
                        toFocus.value = '';
                        toFocus.focus();
                    }
                    if (refreshCommentAfter === true) {
                        refreshComment();
                    }
                } else {
                    alert('댓글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}

function refreshComment() {
    commentContainer.innerHTML = '';

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/article/comment?articleIndex=${commentForm['articleIndex'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const createComment = function (comment, indent) {
                    const tr = document.createElement('tr');
                    const td = document.createElement('td');
                    const headDiv = document.createElement('div');
                    const bodyDiv = document.createElement('div');
                    const dtDate = comment['createdAt'].split('T')[0];
                    const dtTime = comment['createdAt'].split('T')[1].split('.')[0];
                    headDiv.innerText = `${dtDate} ${dtTime} `;
                    if (comment['deleted'] === false) {
                        const deleteAnchor = document.createElement('a');
                        deleteAnchor.setAttribute('href', '#');
                        deleteAnchor.innerText = '삭제';
                        deleteAnchor.onclick = function(e) {
                            e.preventDefault();
                            if (!confirm('정말로 해당 댓글을 삭제할까요?')) {
                                return;
                            }
                            const xhr = new XMLHttpRequest();
                            const formData = new FormData();
                            formData.append('index', comment['index']);
                            xhr.open('DELETE', '/article/comment');
                            xhr.onreadystatechange = () => {
                                if (xhr.readyState === XMLHttpRequest.DONE) {
                                    if (xhr.status >= 200 && xhr.status < 300) {
                                        if (xhr.responseText === 'true') {
                                            refreshComment();
                                        } else {
                                            alert('알 수 없는 이유로 댓글을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                                        }
                                    } else {
                                        alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                                    }
                                }
                            };
                            xhr.send(formData);
                        };
                        headDiv.append(deleteAnchor);
                        bodyDiv.innerText = comment['content'];
                    } else {
                        bodyDiv.innerText = '삭제된 댓글입니다.';
                        bodyDiv.style.color = '#a0a0a0';
                        bodyDiv.style.fontStyle = 'italic';
                    }
                    td.append(headDiv, bodyDiv);

                    if (comment['deleted'] === false) {
                        const replyForm = document.createElement('form');
                        const replyTopHr = document.createElement('hr');
                        const replyBottomHr = document.createElement('hr');
                        const replyCommentIndexInput = document.createElement('input');
                        const replyContentInput = document.createElement('input');
                        const replySubmitInput = document.createElement('input');
                        replyCommentIndexInput.setAttribute('type', 'hidden');
                        replyCommentIndexInput.setAttribute('name', 'commentIndex');
                        replyCommentIndexInput.value = comment['index'];
                        replyContentInput.setAttribute('type', 'text');
                        replyContentInput.setAttribute('maxlength', '100');
                        replyContentInput.setAttribute('name', 'content');
                        replySubmitInput.setAttribute('type', 'submit');
                        replySubmitInput.value = '작성';
                        replyForm.style.display = 'none';
                        replyForm.style.marginLeft = '1rem';
                        replyForm.onsubmit = function (e) {
                            e.preventDefault();
                            if (replyForm['content'].value === '') {
                                alert('내용을 입력해 주세요.');
                                replyForm['content'].focus();
                                return;
                            }
                            postComment(replyForm['content'].value, replyForm['commentIndex'].value, replyForm['content']);
                        };
                        replyForm.append(replyTopHr, replyCommentIndexInput, replyContentInput, replySubmitInput, replyBottomHr);

                        const replyHeadDiv = document.createElement('div');
                        const replyToggleAnchor = document.createElement('a');
                        replyToggleAnchor.setAttribute('href', '#');
                        replyToggleAnchor.innerText = '답글 달기';
                        replyToggleAnchor.dataset.toggled = 'false';
                        replyToggleAnchor.onclick = function (e) {
                            e.preventDefault();
                            if (replyToggleAnchor.dataset.toggled === 'false') {
                                replyToggleAnchor.innerText = '취소';
                                replyToggleAnchor.dataset.toggled = 'true';
                                replyForm.style.display = 'block';
                                replyForm['content'].value = '';
                                replyForm['content'].focus();
                            } else {
                                replyToggleAnchor.innerText = '답글 달기';
                                replyToggleAnchor.dataset.toggled = 'false';
                                replyForm.style.display = 'none';
                            }
                        };
                        replyHeadDiv.append(replyToggleAnchor);
                        td.append(replyHeadDiv, replyForm);
                    }

                    const hr = document.createElement('hr');
                    td.append(hr);

                    tr.style.position = 'relative';
                    tr.style.left = `${indent}px`;
                    tr.append(td);
                    return tr;
                };
                const handleSubComments = function (commentCollection, subComments, level = 0) {
                    const indentFactor = 50;
                    const indent = level * indentFactor;
                    for (const subComment of subComments) {
                        const tr = createComment(subComment, indent);
                        commentContainer.append(tr);

                        const subSubComments = commentCollection.filter(x => x['commentIndex'] === subComment['index']);
                        handleSubComments(commentCollection, subSubComments, level + 1);
                    }
                };
                const comments = JSON.parse(xhr.responseText);
                const parentComments = comments.filter(x => x['commentIndex'] === null);
                handleSubComments(comments, parentComments);

                // for (const comment of comments.filter(x => x['commentIndex'] === null)) {
                //     const tr = createComment(comment);
                //     commentContainer.append(tr);
                //
                //     const subComments = comments.filter(x => x['commentIndex'] === comment['index']);
                //     handleSubComments(comments, subComments);
                // }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
}

commentForm.onsubmit = function (e) {
    e.preventDefault();
    if (commentForm['content'].value === '') {
        alert('댓글을 입력해 주세요.');
        commentForm['content'].focus();
        return;
    }
    postComment(commentForm['content'].value, undefined, commentForm['content']);
};

document.getElementById('refreshComment').onclick = refreshComment;