package org.wind.sso.bg.util.jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @描述 : Jedis篇日志类（各redis模式通用的配置）
 * @版权 : 胡璐璐
 * @时间 : 2020年1月21日 11:15:28
 */
public final class JedisAbstract {

//	private static final Logger logger=Logger.getLogger(JedisUtil.class);
	private static final Map<String, JedisPoolConfig> configMap=new ConcurrentHashMap<String, JedisPoolConfig>();

	/**获取 :  Jedis连接池配置**/
	public static JedisPoolConfig getJedisPoolConfig(String ip,Integer port,Integer minIdle,Integer maxIdle,Integer maxTotal,Long maxWaitMillis,Boolean testOnBorrow,Boolean testOnReturn,Boolean jmxEnabled,Boolean testWhileIdle,Long timeBetweenEvictionRunsMillis,Long minEvictableIdleTimeMillis,Integer numTestsPerEvictionRun) {
		String ipAndPort=ip+":"+port;
		JedisPoolConfig config = configMap.get(ipAndPort);
		if(config==null) {
			config=new JedisPoolConfig();
			configMap.put(ipAndPort, config);
		}
		if(minIdle!=null){config.setMinIdle(minIdle);} 				//最少空闲连接数
		if(minIdle!=null){config.setMaxIdle(maxIdle);}				//最大空闲连接数
		if(minIdle!=null){config.setMaxTotal(maxTotal);}			//最大连接数
		if(minIdle!=null){config.setMaxWaitMillis(maxWaitMillis);}		//最大等待时间数
		if(minIdle!=null){config.setTestOnBorrow(testOnBorrow);}	//在借时，是否验证可用（建议：true）
		if(minIdle!=null){config.setTestOnReturn(testOnReturn);}		//返回时，是否验证可用（建议：false）
		if(minIdle!=null){config.setJmxEnabled(jmxEnabled);}			//是否开启JMX监控
        //空闲对象检测
		if(minIdle!=null){config.setTestWhileIdle(testWhileIdle);}		//是否开启空闲资源检测
		if(minIdle!=null){config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);}		//空闲资源的检测周期（单位：毫秒）
		if(minIdle!=null){config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);}		//资源池中资源的最小空闲时间（单位为毫秒），达到此值后空闲资源将被移除。
		if(minIdle!=null){config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);}			//做空闲资源检测时，每次检测资源的个数。
        return config;
	}
		
	
}