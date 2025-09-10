package org.wind.sso.bg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.orm.bean.Page;
import org.wind.sso.bg.action.panret.ModuleAction;
import org.wind.sso.bg.model.Auth;
import org.wind.sso.bg.model.AuthSSO;
import org.wind.sso.bg.util.RegexUtil;
import org.wind.sso.bg.util.ResultUtil;
import org.wind.sso.bg.util.ValidateUtil;
import org.wind.sso.bg.util.system.SSOUtil;
import org.wind.sso.bg.util.system.SystemUtil;

/**
 * @描述 : SSO管理Action
 * @作者 : 胡璐璐
 * @时间 : 2021年5月13日 15:25:12
 */
@An_Controller(value="/sso",name="SSO管理")
public class SSOAction extends ModuleAction<AuthSSO>{

	/**首页**/
	@An_URL
	public String index(HttpServletRequest request,Long authId) {
		Auth objAuth=Table.findById(Auth.class, authId);
		request.setAttribute("objAuth", objAuth);
		return "index";
	}
	/**数据列表**/
	@An_URL("/list")
	public Map<Object,Object> list(Integer page,Integer limit,Long authId,String ssoId,String ip,String userName) {
		ValidateUtil.notEmpty(authId, "没有指定授权ID");
		ssoId=RegexUtil.clearEmpty(ssoId);
		ip=RegexUtil.clearEmpty(ip);
		userName=RegexUtil.clearEmpty(userName);
		//
		Page pageObj=new Page(SystemUtil.getPage(page), SystemUtil.getLimit(limit));
		StringBuffer tjSQL=new StringBuffer("1=1");
		List<Object> tjList=new ArrayList<Object>();
		//ssoId
		if(ssoId!=null) {
			tjSQL.append(" and id=?");
			tjList.add(ssoId);
		}
		//ip
		if(ip!=null) {
			tjSQL.append(" and ip=?");
			tjList.add(ip);
		}
		//用户名
		if(userName!=null) {
			tjSQL.append(" and user_name=?");
			tjList.add(userName);
		}
		AuthSSO obj=new AuthSSO(authId);
		List<AuthSSO> list=obj.find(tjSQL.toString(), tjList, false, pageObj);
		long size=obj.findSize();
		//
		Map<Object,Object> resultMap=new HashMap<Object, Object>();
		resultMap.put("data",ResultUtil.getResult_Collection(list));
		resultMap.put("count", size);
		return resultMap;
	}
	/**删除**/
	@An_URL("/delete")
	public void delete(Long authId,String id) {
		List<String> idList=new ArrayList<String>();
		idList.add(id);
		this.deleteSelect(authId,idList);
	}
	/**批量删除**/
	@An_URL("/deleteSelect")
	public void deleteSelect(Long authId,List<String> idList) {
		ValidateUtil.notEmpty(authId, "没有指定授权ID");
		if(idList!=null && idList.size()>0) {
			super.deleteSelect(this.tableClass, idList);
			//删除缓存数据
			for(String ssoId:idList) {
				SSOUtil.exit(authId,ssoId);
			}
		}
	}
	
}