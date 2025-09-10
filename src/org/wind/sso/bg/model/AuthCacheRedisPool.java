package org.wind.sso.bg.model;

import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.ForeignKey;
import org.wind.orm.annotation.Tables;
import org.wind.sso.bg.model.parent.Model_Long;

/**
 * @描述 : 授权_缓存_redis_连接池表
 * @作者 : 胡璐璐
 * @时间 : 2021年04月25日 11:10:58
 */
@Tables("auth_cache_redis_pool")
public class AuthCacheRedisPool extends Model_Long{

	@ForeignKey@Column("auth_id")
	private Auth authId;		//授权ID
	@ForeignKey@Column("auth_cache_redis_id")
	private AuthCacheRedis authCacheRedisId;		//授权_缓存_redis的ID
	@Column("min_idle")
	private Integer minIdle;		//最少空闲连接数
	@Column("max_idle")
	private Integer maxIdle;		//最大空闲连接数
	@Column("max_total")
	private Integer maxTotal;		//最大连接数
	@Column("max_wait_millis")
	private Long maxWaitMillis;		//最大等待时间数
	@Column("test_on_borrow")
	private Boolean testOnBorrow;		//在借时，是否验证可用（建议：true）
	@Column("test_on_return")
	private Boolean testOnReturn;		//返回时，是否验证可用（建议：false）
	@Column("jmx_enabled")
	private Boolean jmxEnabled;		//是否开启JMX监控
	@Column("test_while_idle")
	private Boolean testWhileIdle;		//是否开启空闲资源检测
	@Column("time_between_eviction_runs_millis")
	private Long timeBetweenEvictionRunsMillis;		//空闲资源的检测周期（单位：毫秒）
	@Column("min_evictable_idle_time_millis")
	private Long minEvictableIdleTimeMillis;		//资源池中资源的最小空闲时间（单位为毫秒），达到此值后空闲资源将被移除。
	@Column("num_tests_per_eviction_run")
	private Integer numTestsPerEvictionRun;		//做空闲资源检测时，每次检测资源的个数。
	
	@Column("other_config")
	private String otherConfig;		//其他配置（json格式。key=JedisPoolConfig类的字段名；value=值）
	
	/**获取**/
	public static AuthCacheRedisPool get(Long authId) {
		return Table.findOne(AuthCacheRedisPool.class, "auth_id=?", new Object[] {authId}, false);
	}
	public Auth getAuthId() {
		return authId;
	}
	public void setAuthId(Auth authId) {
		this.authId = authId;
	}
	public AuthCacheRedis getAuthCacheRedisId() {
		return authCacheRedisId;
	}
	public void setAuthCacheRedisId(AuthCacheRedis authCacheRedisId) {
		this.authCacheRedisId = authCacheRedisId;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Long getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(Long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public Boolean getTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	public Boolean getJmxEnabled() {
		return jmxEnabled;
	}
	public void setJmxEnabled(Boolean jmxEnabled) {
		this.jmxEnabled = jmxEnabled;
	}
	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}
	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	public Long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}
	public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}
	public Long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}
	public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}
	public Integer getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}
	public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}
	public String getOtherConfig() {
		return otherConfig;
	}
	public void setOtherConfig(String otherConfig) {
		this.otherConfig = otherConfig;
	}

}