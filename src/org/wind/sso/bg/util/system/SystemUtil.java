package org.wind.sso.bg.util.system;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.wind.sso.bg.model.AuthCacheRedis;
import org.wind.sso.bg.model.AuthCacheRedisPool;
import org.wind.sso.bg.model.AuthCacheRedisSentinel;
import org.wind.sso.bg.util.jedis.JedisAbstract;
import org.wind.sso.bg.util.jedis.JedisSentinelUtil;
import org.wind.sso.bg.util.jedis.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @描述 : 系统重要工具类
 * @版权 : 胡璐璐
 * @时间 : 2019年11月22日 16:14:46
 */
public final class SystemUtil {
	
	/**获取 : 分页可以使用的limit**/
	public static int getPage(Integer page) {
		if(page!=null && page>0) {
			return page;
		}else {
			return 1;
		}
	}
	/**获取 : 分页可以使用的limit（最大1000）**/
	public static int getLimit(Integer limit) {
		if(limit!=null && limit>0) {
			if(limit>1000) {
				return 1000;
			}else{
				return limit;
			}
		}else {
			return 1;
		}
	}
	
	/**获取 ; Jedis对象（authId式）**/
	public static Jedis getJedis(Long authId) {
		if(authId!=null) {
			AuthCacheRedis objAcr=AuthCacheRedis.get(authId);
			AuthCacheRedisPool objAcrp=AuthCacheRedisPool.get(authId);
			//
			String ip=objAcr.getIp();
			Integer port=objAcr.getPort();
			String passWord=objAcr.getPassWord();
			Integer index=objAcr.getIndex();
			int userWay=objAcr.getUseWay();
			String sentinelMasterName=objAcr.getSentinelMasterName();		//哨兵主服务名
			//
			Integer minIdle=objAcrp.getMinIdle();
			Integer maxIdle=objAcrp.getMaxIdle();
			Integer maxTotal=objAcrp.getMaxTotal();
			Long maxWaitMillis=objAcrp.getMaxWaitMillis();
			Boolean testOnBorrow=objAcrp.getTestOnBorrow();
			Boolean testOnReturn=objAcrp.getTestOnReturn();
			Boolean jmxEnabled=objAcrp.getJmxEnabled();
			Boolean testWhileIdle=objAcrp.getTestWhileIdle();
			Long timeBetweenEvictionRunsMillis=objAcrp.getTimeBetweenEvictionRunsMillis();
			Long minEvictableIdleTimeMillis=objAcrp.getMinEvictableIdleTimeMillis();
			Integer numTestsPerEvictionRun=objAcrp.getNumTestsPerEvictionRun();
			//
			Jedis objJedis = null;
			JedisPoolConfig config=JedisAbstract.getJedisPoolConfig(ip, port, minIdle, maxIdle, maxTotal, maxWaitMillis, testOnBorrow, testOnReturn, jmxEnabled, testWhileIdle, timeBetweenEvictionRunsMillis, minEvictableIdleTimeMillis, numTestsPerEvictionRun);
			/**使用方式**/
			switch(userWay) {
				//单节点
				case AuthCacheRedis.useWay_single:{
					objJedis=JedisUtil.getJedis(config, ip, port, passWord, index);
					break;
				}
				//哨兵
				case AuthCacheRedis.useWay_sentinel:{
					List<AuthCacheRedisSentinel> objAcrsList=AuthCacheRedisSentinel.get(authId);
					if(objAcrsList!=null && objAcrsList.size()>0) {
						Set<String> sentinelsSet=new LinkedHashSet<String>();
						for(AuthCacheRedisSentinel t_objAcrs:objAcrsList) {
							String t_ip=t_objAcrs.getIp();
							int t_port=t_objAcrs.getPort();
							sentinelsSet.add(t_ip+":"+t_port);
						}
						objJedis=JedisSentinelUtil.getJedis(config, sentinelsSet, passWord, index, sentinelMasterName);
					}
					break;
				}
			}
			if(objJedis==null) {
				throw new RuntimeException("没有找到方式为【"+AuthCacheRedis.getUseWayMap().get(userWay)+"】的配置");
			}
			return objJedis;
		}else {
			throw new RuntimeException("没有找到缓存使用方式");
		}
	}
}