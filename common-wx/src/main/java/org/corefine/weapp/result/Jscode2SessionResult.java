package org.corefine.weapp.result;

/**
 * <pre>
 * code换取session_key接口的响应
 * 文档地址：https://mp.weixin.qq.com/debug/wxadoc/dev/api/api-login.html#wxloginobject
 * 微信返回报文：{"session_key":"nzoqhc3OnwHzeTxJs+inbQ==","openid":"oVBkZ0aYgDMDIywRdgPW8-joxXc4"}
 * </pre>
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class Jscode2SessionResult extends WeappErrorResult {
  private String sessionKey;
  private String openid;
  private String unionid;

  public String getSessionKey() {
    return sessionKey;
  }

  public void setSessionKey(String sessionKey) {
    this.sessionKey = sessionKey;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getUnionid() {
    return unionid;
  }

  public void setUnionid(String unionid) {
    this.unionid = unionid;
  }
}
