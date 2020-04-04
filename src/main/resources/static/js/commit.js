// 为该问题提交回复
function postCommit() {
    var question_id = $("#question_id").val();
    var question_content = $("#question_content").val();
    if (!question_content) {
        alert("评论内容不能为空");
        return;
    }
    postCommitforQuestion(question_id, question_content, 0, false);
}

// 为评论提交回复
function postCommitforComment(e) {
    var id = e.id.replace("btn-", "");
    var content = $("#input-" + id).val();
    $("#input-" + id).val("");
    postCommitforQuestion(id, content, 1, true);
}

function postCommitforQuestion(question_id, question_content, type, flag) {
    var data = {
        "type": type,
        "parentId": question_id,
        "content": question_content
    };
    $.ajax({
        type: "POST",
        url: "/comment/post",
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: 'application/json',
        success: function (response) {
            if (response.errorCode == 200) {
                if (flag) {
                    refreshCommentList("comment-" + question_id, false);
                } else {
                    window.location.reload();
                }
            } else {
                if (response.errorCode == 2001) {
                    var accepted = confirm(response.errorMsg);
                    if (accepted) {
                        window.localStorage.setItem("closeable", "true");
                        window.open("https://github.com/login/oauth/authorize?client_id=4e8e5f192043f32fd813&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                    }
                }
            }
        }
    })
}

function toggleChildDiv(e) {
    var id = e.id;
    refreshCommentList(id, true);
}

// 查看回复列表
function refreshCommentList(id, flag) {
    var isOpen = true;
    if (flag) {
        $("#" + id).toggleClass("comment-icon");
        id = id.replace("comment", "commentList");
        $("#" + id).toggleClass("in");
        isOpen = $("#" + id).hasClass("in");
    }

    id = id.replace("commentList-", "");
    id = id.replace("comment-", "");
    if (!isOpen) {
        return;
    }
    $.getJSON("/comment/getByCommentId/" + id, function (data) {

        var parentEle = $("#commentView-" + id);
        parentEle.empty();
        parentEle.append('<input type="text" id="input-' + id + '" style="margin-bottom: 5px; margin-top: 5px" class="form-control" placeholder="回复...">' +
            '<button onclick="postCommitforComment(this)" type="button" id="btn-' + id + '" style="margin-bottom: 10px" class="btn btn-success btn-sm btn-publish">提交</button>');

        if (data.errorCode == '200') {
            $.each(data.commentList.reverse(), function (index, element) {
                var ele = $('<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">' +
                    '<div class="media">' +
                    '<div class="media-left media-middle">' +
                    '<a href="#">' +
                    '<img class="media-object img-rounded" src="' + element.user.avatarUrl + '">' +
                    '</a>' +
                    '</div>' +
                    '<div class="media-body" style="padding-top: 10px">' +
                    '<span>' + element.user.login + '</span>' +
                    '<div>' + element.content + '</div>' +
                    '</div>' +
                    '</div>' +
                    '<hr style="margin-top: 10px; margin-bottom: 5px"/></div>');
                parentEle.prepend(ele);
            })
        }
    })
}