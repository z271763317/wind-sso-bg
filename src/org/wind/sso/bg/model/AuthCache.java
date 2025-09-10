package org.wind.sso.bg.model;

import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权_缓存表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月16日 00:27:00
 */
@Tables("auth_cache")
public class AuthCache extends Model_Long{

	@ForeignKey@Column("auth_id")
	private Auth authId;		//授权ID
	private Integer type;		//类型（1=redis）
	
	public Auth getAuthId() {
		return authId;
	}
	public void setAuthId(Auth authId) {
		this.authId = authId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

}