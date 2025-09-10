package org.wind.sso.bg.model;

import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权_登录表（含：接口、页面文件）
 * @作者 : 胡璐璐
 * @时间 : 2021年04月16日 00:27:00
 */
@Tables("auth_login")
public class AuthLogin extends Model_Long{

	@ForeignKey@Column("auth_id")
	private Auth authId;		//授权ID
	private String url;		//接口服务端URL根目录（接后面的相对路径：/login=登录；exist=验证用户名是否存在【code=1是存在】）
	@Column("param_user_name")
	private String paramUserName;		//接口参数_用户名
	@Column("param_pass_word")
	private String paramPassWord;		//接口参数_密码
	@Column("page_file")
	private byte[] pageFile;		//登录页面文件
	@Column("pwd_error_num_yzm")
	private Integer pwdErrorNumYzm;		//开启验证码（密码错误多少次，-1=永远不开启）
	@Column("is_return sso_id")
	private Boolean isReturnSsoId;		//是否返回ssoId（前端登录成功后，jsom是否增加返回ssoId数据）
	@Column("is_valid_ip")
	private Boolean isValidIp;		//是否验证客户IP（验证ssoId所属的ip，是不是当前客户端的IP）
	
	public Auth getAuthId() {
		return authId;
	}
	public void setAuthId(Auth authId) {
		this.authId = authId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParamUserName() {
		return paramUserName;
	}
	public void setParamUserName(String paramUserName) {
		this.paramUserName = paramUserName;
	}
	public String getParamPassWord() {
		return paramPassWord;
	}
	public void setParamPassWord(String paramPassWord) {
		this.paramPassWord = paramPassWord;
	}
	public byte[] getPageFile() {
		return pageFile;
	}
	public void setPageFile(byte[] pageFile) {
		this.pageFile = pageFile;
	}
	public Integer getPwdErrorNumYzm() {
		return pwdErrorNumYzm;
	}
	public void setPwdErrorNumYzm(Integer pwdErrorNumYzm) {
		this.pwdErrorNumYzm = pwdErrorNumYzm;
	}
	public Boolean getIsReturnSsoId() {
		return isReturnSsoId;
	}
	public void setIsReturnSsoId(Boolean isReturnSsoId) {
		this.isReturnSsoId = isReturnSsoId;
	}
	public Boolean getIsValidIp() {
		return isValidIp;
	}
	public void setIsValidIp(Boolean isValidIp) {
		this.isValidIp = isValidIp;
	}
	
}