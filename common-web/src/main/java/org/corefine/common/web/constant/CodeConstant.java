package org.corefine.common.web.constant;

public interface CodeConstant {

    /**
     * 操作成功
     */
    int SUCCESS = 200;

    /**
     * 拒绝访问
     */
    int ACCESS_DENIED = 403;

    /**
     * 身份认证失败，请重新登录
     */
    int LOGIN_AGAIN = 401;

    /**
     * 错误
     */
    int ERROR = 480;

    /**
     * 请求方式错误
     */
    int METHOD_ERROR = 481;

    /**
     * 参数验证错误
     */
    int PARAMETER_ERROR = 482;

    /**
     * 服务异常
     */
    int SERVICE_ERROR = 483;

    /**
     * 系统异常
     */
    int SYSTEM_ERROR = 500;
}
