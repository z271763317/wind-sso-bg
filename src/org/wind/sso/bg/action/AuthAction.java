package org.wind.sso.bg.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.bean.Page;
import org.wind.orm.util.TableUtil;
import org.wind.sso.bg.action.panret.ModuleAction;
import org.wind.sso.bg.annotation.An_Add;
import org.wind.sso.bg.annotation.An_Update;
import org.wind.sso.bg.model.Auth;
import org.wind.sso.bg.model.AuthCache;
import org.wind.sso.bg.model.AuthCachePrefix;
import org.wind.sso.bg.model.AuthCacheRedis;
import org.wind.sso.bg.model.AuthCacheRedisPool;
import org.wind.sso.bg.model.AuthCacheRedisSentinel;
import org.wind.sso.bg.model.AuthCookie;
import org.wind.sso.bg.model.AuthLogin;
import org.wind.sso.bg.model.AuthSSO;
import org.wind.sso.bg.model.AuthWhite;
import org.wind.sso.bg.util.RegexUtil;
import org.wind.sso.bg.util.ResultUtil;
import org.wind.sso.bg.util.ValidateUtil;
import org.wind.sso.bg.util.system.SystemUtil;

/**
 * @描述 : 授权Action
 * @作者 : 胡璐璐
 * @时间 : 2020年9月3日 18:06:43
 */
@An_Controller(value="/auth",name="授权管理")
public class AuthAction extends ModuleAction<Auth>{

	public static Logger logger=Logger.getLogger(AuthAction.class);
	
	/**数据列表**/
	@An_URL("/list")
	public Map<Object,Object> list(Integer page,Integer limit,String domain,String name,Integer status,Boolean isIpLimit) {
		domain=RegexUtil.clearEmpty(domain);
		name=RegexUtil.clearEmpty(name);
		//
		Page pageObj=new Page(SystemUtil.getPage(page), SystemUtil.getLimit(limit));
		StringBuffer tjSQL=new StringBuffer("1=1");
		List<Object> tjList=new ArrayList<Object>();
		//域名
		if(domain!=null) {
			tjSQL.append(" and domain like ?");
			tjList.add("%"+domain+"%");
		}
		//名称
		if(name!=null) {
			tjSQL.append(" and name like ?");
			tjList.add("%"+name+"%");
		}
		//状态
		if(status!=null) {
			tjSQL.append(" and status=?");
			tjList.add(status);
		}
		//是否启用了IP访问限制
		if(isIpLimit!=null) {
			tjSQL.append(" and is_ip_limit=?");
			tjList.add(isIpLimit);
		}
		List<Auth> list=Table.find(this.tableClass, tjSQL.toString(), tjList, true, pageObj);
		long size=Table.findSize(this.tableClass, tjSQL.toString(), tjList);
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ResultUtil.getResult_Collection(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**添加UI**/
	@An_URL("/add")@An_Add
	public String add(HttpServletRequest request) {
		request.setAttribute("useWayMap", AuthCacheRedis.getUseWayMap());
		return "edit";
	}
	/**更新UI**/
	@An_URL("/update")@An_Update
	public String update(HttpServletRequest request,Long id) {
		String tjSQL="auth_id=?";
		List<Long> authIdList=new ArrayList<Long>();
		authIdList.add(id);
		//
		request.setAttribute("obj", Table.findById(this.tableClass, id));
		request.setAttribute("objCache", Table.findOne(AuthCache.class, tjSQL, authIdList, false));
		request.setAttribute("objCachePrefix", Table.findOne(AuthCachePrefix.class, tjSQL, authIdList, false));
		request.setAttribute("objCacheRedis", Table.findOne(AuthCacheRedis.class, tjSQL, authIdList, false));
		request.setAttribute("objCacheRedisPool", Table.findOne(AuthCacheRedisPool.class, tjSQL, authIdList, false));
		request.setAttribute("objCacheRedisSentinelList", Table.find(AuthCacheRedisSentinel.class, tjSQL, authIdList, false,null));
		request.setAttribute("objCookie", Table.findOne(AuthCookie.class, tjSQL, authIdList, false));
		request.setAttribute("objLogin", Table.findOne(AuthLogin.class, tjSQL, authIdList, false));
		//
		request.setAttribute("useWayMap", AuthCacheRedis.getUseWayMap());
		return "edit";
	}
	/**保存**/
	@An_URL("/save")
	public void save(Auth obj,AuthLogin objLogin,Part loginPageFile,Boolean isDeleteLoginFile,AuthCache objCache,AuthCacheRedis objCacheRedis,List<AuthCacheRedisSentinel> cacheRedisSentinelList,AuthCacheRedisPool objCacheRedisPool,AuthCachePrefix objCachePrefix,AuthCookie objCookie) throws Exception {
		if(obj!=null) {
			String domain=RegexUtil.clearEmpty(obj.getDomain());
			String name=RegexUtil.clearEmpty(obj.getName());
			Boolean isIpLimit=obj.getIsIpLimit();
			if(domain==null) {
				throw new IllegalArgumentException("未填写【域名】");
			}
			if(isIpLimit==null) {
				throw new IllegalArgumentException("未选择【ip访问限制】");
			}
			Long id=obj.getId();
			StringBuffer tjSQL=new StringBuffer("domain=?");
			List<Object> tjList=new ArrayList<Object>();
			tjList.add(domain);
			if(id!=null) {
				tjSQL.append(" and id!=?");
				tjList.add(id);
			}
			Auth obj_source=Table.findOne(this.tableClass, tjSQL.toString(), tjList, false, "id");
			if(obj_source!=null) {
				throw new IllegalArgumentException("您填写的【域名】已存在");
			}
			obj.setDomain(domain);
			obj.setName(name);
			
			/**登录信息**/
			if(objLogin==null) {
				throw new IllegalArgumentException("缺少【登录配置】数据");
			}
			String url=RegexUtil.clearEmpty(objLogin.getUrl());
			String paramUserName=RegexUtil.clearEmpty(objLogin.getParamUserName());
			String paramPassWord=RegexUtil.clearEmpty(objLogin.getParamPassWord());
			//是否删除登录文件
			if(isDeleteLoginFile!=null && isDeleteLoginFile.equals(true)) {
				Long loginId=objLogin.getId();
				if(loginId!=null) {
					List<Object> placeholderList=new ArrayList<Object>();
					placeholderList.add(loginId);
					Table.update(AuthLogin.class, "page_file=null", "id=?", placeholderList);
				}
			}else{
				if(loginPageFile!=null && loginPageFile.getSize()>0) {
					InputStream is=loginPageFile.getInputStream();
					byte filePage[]=new byte[(int) loginPageFile.getSize()];
					is.read(filePage);
					objLogin.setPageFile(filePage);		//登录页面文件
				}
			}
			if(url==null) {
				throw new IllegalArgumentException("未填选【登录配置_URL根目录】数据");
			}
			objLogin.setAuthId(obj);
			objLogin.setUrl(url);
			objLogin.setParamUserName(paramUserName);
			objLogin.setParamPassWord(paramPassWord);
		
			/**缓存**/
			if(objCache==null) {
				throw new IllegalArgumentException("缺少【缓存配置】数据1");
			}
			Integer cacheType=objCache.getType();
			if(cacheType==null) {
				throw new IllegalArgumentException("缺少【缓存配置_类型】数据");
			}
			objCache.setAuthId(obj);
			
			/**缓存—reids**/
			if(objCacheRedis==null) {
				throw new IllegalArgumentException("缺少【缓存配置】数据2");
			}
			String redis_ip=RegexUtil.clearEmpty(objCacheRedis.getIp());
			Integer useWay=objCacheRedis.getUseWay();
			String sentinelMasterName=RegexUtil.clearEmpty(objCacheRedis.getSentinelMasterName());
			//
			if(useWay==null) {
				throw new IllegalArgumentException("缺少【缓存配置_使用方式】");
			}
			if(objCacheRedis.getCaptchaExpiry()==null) {
				throw new IllegalArgumentException("缺少【缓存配置_验证码过期时间】");
			}
			if(objCacheRedis.getSsoIdExpiry()==null) {
				throw new IllegalArgumentException("缺少【缓存配置_ssoId过期时间】");
			}
			switch(useWay) {
				//单节点
				case	AuthCacheRedis.useWay_single:{
					ValidateUtil.notEmpty(redis_ip, "缺少【缓存配置_redis_单节点_ip】");
					ValidateUtil.notEmpty(objCacheRedis.getPort(), "缺少【缓存配置_redis_单节点_端口】");
					break;
				}
				//哨兵
				case	AuthCacheRedis.useWay_sentinel:{
					ValidateUtil.notEmpty(sentinelMasterName, "缺少【缓存配置_redis_哨兵_主服务名】");
					if(cacheRedisSentinelList!=null && cacheRedisSentinelList.size()>0) {
						for(int i=0;i<cacheRedisSentinelList.size();i++) {
							AuthCacheRedisSentinel t_obj=cacheRedisSentinelList.get(i);
							if(t_obj!=null) {
								String t_ip=RegexUtil.clearEmpty(t_obj.getIp());
								Integer t_port=t_obj.getPort();
								//都为空
								if(t_ip==null && t_port==null) {
									cacheRedisSentinelList.remove(i);
									i--;
									continue;
								}
								ValidateUtil.notEmpty(t_ip, "缺少【缓存配置_redis_哨兵_ip】");
								if(t_port==null) {
									throw new IllegalArgumentException("缺少【缓存配置_redis_哨兵_端口】");
								}
								t_obj.setId(null);
								t_obj.setIp(t_ip);
								t_obj.setAuthId(obj);
								t_obj.setAuthCacheRedisId(objCacheRedis);
							}else{
								cacheRedisSentinelList.remove(i);
								i--;
							}
						}
					}
					if(cacheRedisSentinelList==null || cacheRedisSentinelList.size()<=0) {
						throw new IllegalArgumentException("缺少【缓存配置_redis_哨兵列表（ip+断口集）】");
					}
					break;
				}
				default:{
					throw new IllegalArgumentException("不支持的【使用方式】");
				}
			}
			//
			objCacheRedis.setAuthId(obj);
			objCacheRedis.setAuthCacheId(objCache);
			objCacheRedis.setIp(redis_ip);
			objCacheRedis.setSentinelMasterName(sentinelMasterName);
			
			/**缓存—reids—连接池**/
			if(objCacheRedisPool==null) {
				objCacheRedisPool=new AuthCacheRedisPool();
			}
			objCacheRedisPool.setAuthId(obj);
			objCacheRedisPool.setAuthCacheRedisId(objCacheRedis);
			
			/**缓存—key前缀**/
			if(objCachePrefix==null) {
				throw new IllegalArgumentException("缺少【缓存_key前缀】数据");
			}
			String ssoId=RegexUtil.clearEmpty(objCachePrefix.getSsoId());
			String captcha=RegexUtil.clearEmpty(objCachePrefix.getCaptcha());
			String pwdErrorNum=RegexUtil.clearEmpty(objCachePrefix.getPwdErrorNum());
			ValidateUtil.notEmpty(ssoId, "缺少【缓存_key前缀_ssoId】");
			ValidateUtil.notEmpty(captcha, "缺少【缓存_key前缀_验证码】");
			ValidateUtil.notEmpty(pwdErrorNum, "缺少【缓存_key前缀_密码错误数】");
			//
			objCachePrefix.setAuthId(obj);
			objCachePrefix.setSsoId(ssoId);
			objCachePrefix.setCaptcha(captcha);
			objCachePrefix.setPwdErrorNum(pwdErrorNum);
			
			/**Cookie配置—ssoId**/
			if(objCookie==null) {
				throw new IllegalArgumentException("缺少【Cookie配置_ssoId】数据");
			}
			String ssoIdName=RegexUtil.clearEmpty(objCookie.getSsoIdName());
			String ssoIdPath=RegexUtil.clearEmpty(objCookie.getSsoIdPath());
			//
			ValidateUtil.notEmpty(ssoIdName, "缺少【Cookie配置_ssoId_name】");
			ValidateUtil.notEmpty(ssoIdPath, "缺少【Cookie配置_ssoId_path】");
			ValidateUtil.notEmpty(objCookie.getSsoIdExpiry(), "缺少【Cookie配置_ssoId_过期时间】");
			//
			objCookie.setAuthId(obj);
			
			/**最后统一保存**/
			obj.save();
			objLogin.save();
			objCache.save();
			objCacheRedis.save();
			if(cacheRedisSentinelList!=null && cacheRedisSentinelList.size()>0) {
				/*先删除*/
				List<Object> authIdList=new ArrayList<Object>();
				authIdList.add(obj.getId());
				Table.delete(AuthCacheRedisSentinel.class, "auth_id=?", authIdList);
				/*保存*/
				Table.save(cacheRedisSentinelList);
			}
			objCacheRedisPool.save();
			objCachePrefix.save();
			objCookie.save();
			
			/**sso分表处理**/
			boolean isExist=Table.isTableExist(Auth.class,AuthSSO.tablePrefix+obj.getId());
			if(!isExist) {
				Table.copy(AuthSSO.class, TableUtil.getTable(AuthSSO.class)+"_"+obj.getId(),false);
			}
		}else{
			throw new IllegalArgumentException("未填选任何数据");
		}
	}
	/**白名单**/
	@An_URL("/white")
	public Map<Object,Object> white(Long id){
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		if(id!=null) {
			String tjSQL="auth_id=?";
			List<Long> authIdList=new ArrayList<Long>();
			authIdList.add(id);
			List<AuthWhite> listAuthWhite=Table.find(AuthWhite.class, tjSQL, authIdList, false, null);
			resultMap.put("data", ResultUtil.getResult_Collection(listAuthWhite));
		}
		return resultMap;
	}
	/**保存 : 白名单**/
	@An_URL("/saveWhite")
	public void saveWhite(Long id,List<String> whiteList){
		if(id!=null) {
			List<AuthWhite> saveList=new ArrayList<AuthWhite>();
			if(whiteList!=null && whiteList.size()>0) {
				Auth obj=new Auth();
				obj.setId(id);
				for(String t_ip:whiteList) {
					t_ip=RegexUtil.clearEmpty(t_ip);
					if(t_ip!=null && t_ip.length()>0) {
						AuthWhite t_obj=new AuthWhite();
						t_obj.setAuthId(obj);
						t_obj.setIp(t_ip);
						saveList.add(t_obj);
					}
				}
			}
			/*先删除*/
			String tjSQL="auth_id=?";
			List<Long> authIdList=new ArrayList<Long>();
			authIdList.add(id);
			Table.delete(AuthWhite.class, tjSQL, authIdList);
			
			/*保存*/
			if(saveList!=null && saveList.size()>0) {
				Table.save(saveList);
			}
		}else{
			throw new IllegalArgumentException("没有指定授权项");
		}
	}
	/**删除**/
	@An_URL("/delete")
	public void delete(Long id) {
		ValidateUtil.notEmpty(id, "未指定项");
		isAllowDelete(id);
		super.delete(this.tableClass, id);
		String conditionsSQL="auth_id=?";
		List<Object> conditionsList=new ArrayList<Object>();
		conditionsList.add(id);
		/*缓存*/
		Table.delete(AuthCache.class, conditionsSQL, conditionsList);			//缓存
		Table.delete(AuthCachePrefix.class, conditionsSQL, conditionsList);			//缓存_前缀
		Table.delete(AuthCacheRedis.class, conditionsSQL, conditionsList);			//缓存_redis
		Table.delete(AuthCacheRedisPool.class, conditionsSQL, conditionsList);			//缓存_redis_连接池
		Table.delete(AuthCacheRedisSentinel.class, conditionsSQL, conditionsList);			//缓存_redis_哨兵
		
		/*cookie*/
		Table.delete(AuthCookie.class, conditionsSQL, conditionsList);			//cookie
		
		/*登录*/
		Table.delete(AuthLogin.class, conditionsSQL, conditionsList);			//缓存_登录（含：接口、页面文件）
		
		/*白名单*/
		Table.delete(AuthWhite.class, conditionsSQL, conditionsList);			//缓存_白名单
		
		/*sso已登录的会话*/
		String tableName=AuthSSO.getTableName(id);
		boolean isExist=Table.isTableExist(Auth.class, tableName);
		if(isExist) {
			try {
				Table.drop(Auth.class,"DROP TABLE "+tableName);
			}catch(Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
	}
	//是否允许删除（主键式）
	private static void isAllowDelete(Long id) {
		Auth obj=Table.findById(Auth.class, id, "isAllowDelete");
		isAllowDelete(obj);
	}
	//是否允许删除（对象式）
	private static void isAllowDelete(Auth obj) {
		Boolean isAllowDelete=obj.getIsAllowDelete();
		if(!isAllowDelete) {
			throw new IllegalArgumentException("该项不允许删除/禁用");
		}
	}
	//是否允许删除（对象式）
	private static void isAllowDelete(List<Long> idList) {
		List<Auth> objList=Table.findByIdList(Auth.class, idList, "isAllowDelete","domain");
		if(objList.size()>0) {
			for(Auth obj:objList) {
				Boolean isAllowDelete=obj.getIsAllowDelete();
				String domain=obj.getDomain();
				if(!isAllowDelete) {
					throw new IllegalArgumentException("域名为【"+domain+"】项不允许删除/禁用");
				}
			}
		}
	}
	/**批量启用**/
	@An_URL("/enableSelect")
	public void enableSelect(List<Long> idList) {
		super.enableSelect(this.tableClass, idList);
	}
	/**批量禁用**/
	@An_URL("/disableSelect")
	public void disableSelect(List<Long> idList) {
		isAllowDelete(idList);
		super.disableSelect(this.tableClass, idList);
	}
	
}
