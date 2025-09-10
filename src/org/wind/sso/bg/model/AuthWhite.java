package org.wind.sso.bg.model;

import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权_白名单表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月22日 20:36:00
 */
@Tables("auth_white")
public class AuthWhite extends Model_Long{
	
	@ForeignKey@Column("auth_id")
	private Auth authId;		//授权ID
	private String ip;		//白名单地址
	
	public Auth getAuthId() {
		return authId;
	}
	public void setAuthId(Auth authId) {
		this.authId = authId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

}