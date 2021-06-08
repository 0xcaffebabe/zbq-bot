package wang.ismy.dto;

/**
 * @author MY
 * @date 2021/6/8 22:27
 */
public class MessageBO {
    private String msg;
    private boolean isImg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isImg() {
        return isImg;
    }

    public void setImg(boolean img) {
        isImg = img;
    }

    @Override
    public String toString() {
        return "MessageBO{" +
                "msg='" + msg + '\'' +
                ", isImg=" + isImg +
                '}';
    }
}
