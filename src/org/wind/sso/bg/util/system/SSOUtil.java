package org.wind.sso.bg.util.system;

import org.wind.sso.bg.model.AuthCachePrefix;
import org.wind.sso.bg.model.AuthSSO;

import redis.clients.jedis.Jedis;

/**
 * @描述 : SSO工具类
 * @数据格式 : json
 * @作者 : 胡璐璐
 * @时间 : 2021年5月12日 22:02:29
 */
public final class SSOUtil {
	
	//不允许实例化
	private SSOUtil(){
		//请不要试图实例化我
	}
	
	/*********************其他*********************/
	/**删除（退出） : 当前会话的所有信息（含：seesion）**/
	public static void exit(Long authId,String ssoId){
		if(ssoId!=null){
			Jedis jedis=SystemUtil.getJedis(authId);
			try {
				AuthCachePrefix objPrefix=AuthCachePrefix.get(authId);
				jedis.del(objPrefix.getSsoId()+ssoId);
				AuthSSO obj=new AuthSSO(authId);
				obj.setId(ssoId);
				obj.delete();
			}finally{
				jedis.close();
			}
		}
	}
	
}