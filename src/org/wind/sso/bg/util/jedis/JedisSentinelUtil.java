package org.wind.sso.bg.util.jedis;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * @描述 : Jedis工具类（哨兵模式）
 * @版权 : 胡璐璐
 * @时间 : 2020年1月21日 11:31:42
 */
public class JedisSentinelUtil {

//	private static final Logger logger=Logger.getLogger(JedisUtil.class);
	private static final Map<String, JedisSentinelPool> poolMap=new ConcurrentHashMap<String, JedisSentinelPool>();

	/**获取 : 连接池（masterName=哨兵的主服务名）**/
	public static JedisSentinelPool getJedisPool(JedisPoolConfig config,Set<String> sentinelsSet,String passWord,String masterName) {
		JedisSentinelPool jedisPool=getJedisPool_notGenerate(masterName);
		if(jedisPool==null) {
			jedisPool=new JedisSentinelPool(masterName, sentinelsSet, config, 10*1000, passWord);
			poolMap.put(masterName, jedisPool);
		}
		return jedisPool;
	}
	/**获取 : 连接池（不生成连接池）**/
	public static JedisSentinelPool getJedisPool_notGenerate(String masterName) {
		JedisSentinelPool jedisPool=poolMap.get(masterName);
		return jedisPool;
	}
	
	/**获取 : 指定的jedis连接池（database=指定的数据库，默认：0）**/
	public static Jedis getJedis(JedisPoolConfig config,Set<String> sentinelsSet,String passWord,Integer database,String masterName) {
		JedisSentinelPool jedisPool=getJedisPool(config,sentinelsSet, passWord,masterName);
		Jedis jedis=jedisPool.getResource();
		if(database!=null && database!=0) {
			jedis.select(database);
		}
        return jedis;
	}
}