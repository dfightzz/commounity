package cn.dzz.community;

public enum CommentType {

    COMMENT_TYPE(1), QUESTION_TPYE(0);
    private int code;
    CommentType(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
