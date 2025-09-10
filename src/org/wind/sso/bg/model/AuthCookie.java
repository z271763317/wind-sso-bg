package org.wind.sso.bg.model;

import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权_cookie表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月16日 00:27:00
 */
@Tables("auth_cookie")
public class AuthCookie extends Model_Long{

	@ForeignKey@Column("auth_id")
	private Auth authId;		//授权ID
	@Column("domain")
	private String domain;		//cookie的domain（存储的域名，必须是当前sso系统所属域名的某上级域名）
	@Column("sso_id_name")
	private String ssoIdName;		//ssoId的name（key名、cookie名）
	@Column("sso_id_path")
	private String ssoIdPath;		//ssoId的path（存储的目录，适用的目录）
	@Column("sso_id_expiry")
	private Integer ssoIdExpiry;		//ssoId的过期时间（单位：秒）
	
	public Auth getAuthId() {
		return authId;
	}
	public void setAuthId(Auth authId) {
		this.authId = authId;
	}
	public String getSsoIdName() {
		return ssoIdName;
	}
	public void setSsoIdName(String ssoIdName) {
		this.ssoIdName = ssoIdName;
	}
	public String getSsoIdPath() {
		return ssoIdPath;
	}
	public void setSsoIdPath(String ssoIdPath) {
		this.ssoIdPath = ssoIdPath;
	}
	public Integer getSsoIdExpiry() {
		return ssoIdExpiry;
	}
	public void setSsoIdExpiry(Integer ssoIdExpiry) {
		this.ssoIdExpiry = ssoIdExpiry;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}