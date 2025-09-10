package org.wind.sso.bg.model;

import org.wind.orm.annotation.Column;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权表
 * @作者 : 胡璐璐
 * @时间 : 2021年4月24日 10:57:44
 */
public class Auth extends Model_Long{

	private String domain;		//域名
	private String name;		//名称
	@Column("is_ip_limit")
	private Boolean isIpLimit;		//是否启用ip访问限制（部分功能失效，如：SSO登录页）
	@Column("login_url")
	private String loginUrl;		//登录界面URL
	@Column("is_allow_delete")
	private Boolean isAllowDelete;		//是否允许删除
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsIpLimit() {
		return isIpLimit;
	}
	public void setIsIpLimit(Boolean isIpLimit) {
		this.isIpLimit = isIpLimit;
	}
	public Boolean getIsAllowDelete() {
		return isAllowDelete;
	}
	public void setIsAllowDelete(Boolean isAllowDelete) {
		this.isAllowDelete = isAllowDelete;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
}
