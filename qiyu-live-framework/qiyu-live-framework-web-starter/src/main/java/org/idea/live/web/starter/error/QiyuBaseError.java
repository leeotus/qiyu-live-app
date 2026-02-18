package org.idea.live.web.starter.error;

public interface QiyuBaseError {

    /**
     * 获取错误码的方法
     * @return 返回一个整数类型的错误码
     */
    int getErrorCode();

    /**
     * 获取错误信息的方法
     * 该方法用于返回错误信息字符串
     *
     * @return 返回错误信息的字符串内容
     */
    String getErrorMsg();
}
