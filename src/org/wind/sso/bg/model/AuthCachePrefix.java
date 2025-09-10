package org.wind.sso.bg.model;

import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权_缓存_key前缀表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月25日 14:40:13
 */
@Tables("auth_cache_prefix")
public class AuthCachePrefix extends Model_Long{

	@ForeignKey@Column("auth_id")
	private Auth authId;		//授权ID
	@Column("sso_id")
	private String ssoId;		//ssoId
	private String captcha;		//验证码
	@Column("pwd_error_num")
	private String pwdErrorNum;		//密码错误数
	
	/**获取**/
	public static AuthCachePrefix get(Long authId) {
		return Table.findOne(AuthCachePrefix.class, "auth_id=?", new Object[] {authId}, false);
	}
	
	public Auth getAuthId() {
		return authId;
	}
	public void setAuthId(Auth authId) {
		this.authId = authId;
	}
	public String getSsoId() {
		return ssoId;
	}
	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getPwdErrorNum() {
		return pwdErrorNum;
	}
	public void setPwdErrorNum(String pwdErrorNum) {
		this.pwdErrorNum = pwdErrorNum;
	}

}