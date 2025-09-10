package org.wind.sso.bg.model;

import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.Tables;
import org.wind.orm.util.TableUtil;
import org.wind.sso.bg.model.parent.Model_String;

/**
 * @描述 : 授权_SSO表（已登录的）
 * @作者 : 胡璐璐
 * @时间 : 2021年05月12日 14:47:46
 */
@Tables("auth_sso")
public class AuthSSO extends Model_String{

	public static final String tablePrefix=TableUtil.getTable(AuthSSO.class)+"_";		//原表+前缀
	
	private Integer expiry;		//过期时间（单位：秒）
	private String ip;		//登录者的IP
	@Column("user_name")
	private String userName;		//登录的用户名
	@Column("pass_word")
	private String passWord;		//登录的密码
	
	/**非表数据**/
	private Auth objAuth;
	
	/**获取 : 表名**/
	public static String getTableName(Long authId) {
		return tablePrefix+authId;
	}
	
	public AuthSSO() {
		
	}
	public AuthSSO(Long authId) {
		super.setTable(getTableName(authId));
	}
	public Integer getExpiry() {
		return expiry;
	}
	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public Auth getObjAuth() {
		return objAuth;
	}
	public void setObjAuth(Auth objAuth) {
		this.objAuth = objAuth;
	}
	
}