package org.wind.sso.bg.util.jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @描述 : Jedis工具类
 * @版权 : 胡璐璐
 * @时间 : 2018年9月6日 09:41:58
 */
public final class JedisUtil {

//	private static final Logger logger=Logger.getLogger(JedisUtil.class);
	private static final Map<String, JedisPool> poolMap=new ConcurrentHashMap<String, JedisPool>();

	/**获取 : 连接池**/
	public static JedisPool getJedisPool(JedisPoolConfig config,String ip,int port,String passWord) {
		JedisPool jedisPool=getJedisPool_notGenerate(ip, port);
		if(jedisPool==null) {
			jedisPool=new JedisPool(config, ip, port,10*1000,passWord);
			String key=ip+":"+port;
			poolMap.put(key, jedisPool);
		}
		return jedisPool;
	}
	/**获取 : 连接池（不生成连接池）**/
	public static JedisPool getJedisPool_notGenerate(String ip,int port) {
		String key=ip+":"+port;
		JedisPool jedisPool=poolMap.get(key);
		return jedisPool;
	}

	/**获取 : 指定的jedis连接池（database=指定的数据库，默认：0）**/
	public static Jedis getJedis(JedisPoolConfig config,String ip,int port,String passWord,Integer database) {
		JedisPool jedisPool=getJedisPool(config,ip, port, passWord);
		Jedis jedis=jedisPool.getResource();
		if(database!=null && database!=0) {
			jedis.select(database);
		}
        return jedis;
	}
	/**设置 : Map的值。key或value其中一个为null,则不加入**/
	public static void put(Map<String,String> map,String key,Object value) {
		if(map!=null) {
			if(key!=null && value!=null) {
				map.put(key, value.toString());
			}
		}
	}
}