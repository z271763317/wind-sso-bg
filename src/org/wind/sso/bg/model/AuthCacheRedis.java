package org.wind.sso.bg.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权_缓存_redis表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月16日 00:27:00
 */
@Tables("auth_cache_redis")
public class AuthCacheRedis extends Model_Long{

	private static final Map<Integer,String> useWayMap=new LinkedHashMap<Integer,String>();
	//
	public static final int useWay_single=1;			//单节点
	public static final int useWay_masterSlave=2;			//主从复制
	public static final int useWay_sentinel=3;			//哨兵
	public static final int useWay_cluster=4;			//cluster（集群）
	
	static {
		useWayMap.put(useWay_single, "单节点");
//		useWayMap.put(useWay_masterSlave, "主从复制");
		useWayMap.put(useWay_sentinel, "哨兵");
//		useWayMap.put(useWay_cluster, "cluster集群");
	}
	
	@ForeignKey@Column("auth_id")
	private Auth authId;		//授权ID
	@ForeignKey@Column("auth_cache_id")
	private AuthCache authCacheId;		//缓存ID
	private String ip;		//连接ip
	private Integer port;		//端口
	@Column("pass_word")
	private String passWord;		//密码
	private Integer index;		//指定的数据库
	@Column("sentinel_master_name")
	private String sentinelMasterName;		//哨兵的主服务名
	@Column("captcha_expiry")
	private Integer captchaExpiry;		//验证码过期时间（单位：秒）
	@Column("use_way")
	private Integer useWay;		//使用方式（1=单节点；2=主从复制；3=哨兵；4=Cluster）
	@Column("sso_id_expiry")
	private Integer ssoIdExpiry;		//ssoId的过期时间（单位：秒）

	/**获取 : 使用方式**/
	public static Map<Integer,String> getUseWayMap(){
		return useWayMap;
	}
	/**获取**/
	public static AuthCacheRedis get(Long authId) {
		return Table.findOne(AuthCacheRedis.class, "auth_id=?", new Object[] {authId}, false);
	}
	public AuthCache getAuthCacheId() {
		return authCacheId;
	}
	public void setAuthCacheId(AuthCache authCacheId) {
		this.authCacheId = authCacheId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getSentinelMasterName() {
		return sentinelMasterName;
	}
	public void setSentinelMasterName(String sentinelMasterName) {
		this.sentinelMasterName = sentinelMasterName;
	}
	public Integer getCaptchaExpiry() {
		return captchaExpiry;
	}
	public void setCaptchaExpiry(Integer captchaExpiry) {
		this.captchaExpiry = captchaExpiry;
	}
	public Auth getAuthId() {
		return authId;
	}
	public void setAuthId(Auth authId) {
		this.authId = authId;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getUseWay() {
		return useWay;
	}
	public void setUseWay(Integer useWay) {
		this.useWay = useWay;
	}
	public Integer getSsoIdExpiry() {
		return ssoIdExpiry;
	}
	public void setSsoIdExpiry(Integer ssoIdExpiry) {
		this.ssoIdExpiry = ssoIdExpiry;
	}

}