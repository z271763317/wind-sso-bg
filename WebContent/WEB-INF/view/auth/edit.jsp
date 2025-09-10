<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_pageName} - ${_moduleName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<style type="text/css">
	
	</style>
	<script type="text/javascript" >
		//页面渲染后执行
		$(document).ready(function(){
			showRedisUseWayPanel(${objCacheRedis.useWay!=null?objCacheRedis.useWay:1});
			//
			renderForm(function(){
				layui_form.on('select(useWay)', function(data){
					showRedisUseWayPanel(data.value);
				});  
			});
		});
		//文件选择后的处理
		function fileSelectHandler(obj){
			$("#loginPageFileName").html("已选择");
			$("#isDeleteLoginFile").val("false");
		}
		//显示不同的redis使用方式面板
		function showRedisUseWayPanel(useWay){
			var t_jqArr=$("[type='redisUseWayGroup']");
			for(var i=0;i<t_jqArr.length;i++){
				var t_jq=$(t_jqArr[i]);
				t_jq.find("input").attr("disabled","disabled");
				t_jq.find("select").attr("disabled","disabled");
				t_jq.find("textarea").attr("disabled","disabled");
				t_jq.hide();
			}
			if(useWay!=null && useWay!=''){
				var t_useWay_jq=$("#redis_useWay_"+useWay);
				t_useWay_jq.find("input").removeAttr("disabled");
				t_useWay_jq.find("select").removeAttr("disabled");
				t_useWay_jq.find("textarea").removeAttr("disabled");
				t_useWay_jq.show();
			}
		}
		//显示不同的redis使用方式面板
		function showRedisUseWayPanelObj(obj){
			showRedisUseWayPanel($(obj).val());
		}
		//追加哨兵
		function addSentinel(obj){
			var lastIndex=parseInt($("#lastIndex").val());
			lastIndex++;
			//
			var html='<div class="layui-form-item" index="'+lastIndex+'">';
			
			html+='<div class="layui-inline">';
			html+='<label class="layui-form-label">ip</label>';
			html+='<div class="layui-input-inline">';
			html+='<input type="text" name="cacheRedisSentinelList['+lastIndex+'].ip" class="layui-input" placeholder="哨兵ip" />';
			html+='</div>';
			html+='</div>';
			
			html+=' <div class="layui-inline">';
			html+='<label class="layui-form-label">端口</label>';
			html+='<div class="layui-input-inline">';
			html+='<input type="text" name="cacheRedisSentinelList['+lastIndex+'].port" class="layui-input" placeholder="哨兵端口" />';
			html+='</div>';
			html+='</div>';
			
			html+=' <div class="layui-inline">';
			html+='<div class="layui-input-inline">';
			html+='<button type="button" class="layui-btn layui-btn-danger" onClick="deleteSentinel(this)"><i class="layui-icon layui-icon-reduce-circle"></i> 删除</button>';
			html+='</div>';
			html+='</div>';
			
			html+='</div>';
			//
			$("#shaoBingPanel").append(html);
			$("#lastIndex").val(lastIndex)
		}
		//删除哨兵
		function deleteSentinel(obj){
			confirm("删除哨兵","您确定要删除当前哨兵信息吗？",function(){
				$(obj).parent().parent().parent().remove();
			});
		}
		//删除上传选择的登录页面文件
		function deleteLoginPageFile(obj){
			$("#isDeleteLoginFile").val("true");
			$("#loginPageFileName").html("");
		}
	</script>
</head>
<body class="edit" style="padding-bottom:100px">
	<div class="edit_title">${_pageName} - ${_moduleName}</div>
	<form action="save"  method="post" class="layui-form layui-form-pane" >
		<div class="layui-collapse" >
		
			<!-- 授权信息 --> 
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">授权信息</h2>
				<div class="layui-colla-content layui-show panel_content">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">域名</label>
							<div class="layui-input-inline">
								<input type="text" name="obj.domain" value="${obj.domain}" val-type="empty" class="layui-input" placeholder="唯一" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">名称</label>
							<div class="layui-input-inline">
								<input type="text" name="obj.name" value="${obj.name}" val-type="empty" class="layui-input" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">ip访问限制</label>
							<div class="layui-input-inline">
								<select class="select" name="obj.isIpLimit">
									<option value="true" <c:if test="${obj.isIpLimit==true}"> selected="selected"</c:if>>是</option>
									<option value="false" <c:if test="${obj.isIpLimit==false}"> selected="selected"</c:if>>否</option>
								</select>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label" style="font-size:12px">登录界面URL</label>
						<div class="layui-input-block">
							<input type="text" name="obj.loginUrl" value="${obj.loginUrl}" val-type="empty" class="layui-input" />
						</div>
					</div>
				</div>
			</div>
			<!-- 登录配置 -->
			<div class="layui-colla-item">
				<input type="hidden" name="objLogin.id" value="${objLogin.id}" />
				<h2 class="layui-colla-title">登录配置（含：注册）</h2>
				<div class="layui-colla-content layui-show">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">URL根目录</label>
							<div class="layui-input-inline">
								<input type="text" name="objLogin.url" value="${objLogin.url}" val-type="empty" class="layui-input" placeholder="接口服务端根目录" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="前端登录成功后，jsom是否增加返回ssoId数据" >返回ssoId</label>
							<div class="layui-input-inline">
								<select class="select" name="objLogin.isReturnSsoId">
									<option value="true" <c:if test="${objLogin.isReturnSsoId==true}"> selected="selected"</c:if>>是</option>
									<option value="false" <c:if test="${objLogin.isReturnSsoId==false}"> selected="selected"</c:if>>否</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">自定义页面</label>
							<div class="layui-input-block">
								<input type="file" name="loginPageFile" id="loginPageFile" style="display: none" onchange="fileSelectHandler(this)" />
								<div class="layui-btn-group">
							    	<button type="button" class="layui-btn" onclick="callClick('loginPageFile')">选择文件</button>
							    	<button type="button"  class="layui-btn layui-btn-danger" onclick="deleteLoginPageFile(this)">删除文件</button>
						    	</div>
						    	<b id="loginPageFileName" style="padding:5px;color:green" ><c:if test="${objLogin.pageFile!=null}">已上传</c:if></b>
						    	<input type="hidden" name="isDeleteLoginFile" id="isDeleteLoginFile" value="false" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="设置登录失败几次后出现验证码功能，-1=永远不开启">开启验证码</label>
							<div class="layui-input-inline">
								<input type="number" step="1" min="-1" name="objLogin.pwdErrorNumYzm" value="${objLogin.pwdErrorNumYzm}" val-type="empty" class="layui-input" placeholder="默认：-1（永远不开启）" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="验证ssoId所属的ip，是不是当前客户端的IP"  style="font-size:12px">验证客户端IP</label>
							<div class="layui-input-inline">
								<select class="select" name="objLogin.isValidIp">
									<option value="true" <c:if test="${objLogin.isValidIp==true}"> selected="selected"</c:if>>开启</option>
									<option value="false" <c:if test="${objLogin.isValidIp==false}"> selected="selected"</c:if>>关闭</option>
								</select>
							</div>
						</div>
						<div style="padding-top:10px">
							<div><b style="color:red">说明</b>：</div>
							<ol style="margin-left:35px;line-height:26px">
								<li>1、若选择了自定义登录页面文件，登陆成功后，则需要自己手动跳转参数【referer】（来源链接），也可手动跳转自定义的链接（如：您的首页链接）</li>
								<li><div>2、SSO的登录接口：【URL根目录】/login（如：http://127.0.0.1:8090/wind-sso/login），拥有4个参数：</div>
									<div style="text-align:left;font-size:13px;color:#292929;padding:10px;padding-left:22px;font-family:Microsoft YaHei">
										userName=用户名；passWord=密码；captcha=验证码；expiry=ssoId会话过期时间（单位：秒，传空则取默认的）
									</div>
								</li>
								<li>3、SSO的验证码接口：【URL根目录】/index/captcha?userName=【用户名】（如：http://127.0.0.1:8090/wind-sso/index/captcha?userName=【用户名】）</li>
								<li>4、SSO的注册接口：【URL根目录】/reg（如：http://127.0.0.1:8090/wind-sso/reg），自定义参数，这些参数最终全部传入您的注册接口。</li>
								<li><div>5、SSO规定，您的登录接口（【URL根目录】/login），2个参数【userName=用户名】和【passWord=密码】，返回数据格式：</div>
									<div style="text-align:left;font-size:13px;color:#292929;padding-left:40px;font-family:Microsoft YaHei">
										<p>{</p>
										<p style="text-indent: 2em;">"code":"【状态码。1=成功，0=失败】",</p>
										<p style="text-indent: 2em;">"message":"【信息，一般是失败后返回的错误信息】",</p>
										<p style="text-indent: 2em;">"session":【json对象。存储传统形式的登录成功后的会话信息】</p>
										<p>}</p>
									</div>
								</li>
								<li><div>6、SSO规定，您的用户验证接口（【URL根目录】/exist），1个参数【userName=用户名】，返回数据格式：</div>
									<div style="text-align:left;font-size:13px;color:#292929;padding-left:40px;font-family:Microsoft YaHei">
										<p>{</p>
										<p style="text-indent: 2em;">"code":"【状态码。1=存在，0=不存在】",</p>
										<p>}</p>
									</div>
								</li>
								<li><div>7、SSO规定，您的注册接口（【URL根目录】/reg），注册页传入到SSO的参数全部传入该接口，返回数据格式：</div>
									<div style="text-align:left;font-size:13px;color:#292929;padding-left:40px;font-family:Microsoft YaHei">
										<p>{</p>
										<p style="text-indent: 2em;">"code":"【状态码。1=成功，0=失败】",</p>
										<p style="text-indent: 2em;">"message":"【信息，一般是失败后返回的错误信息】",</p>
										<p>}</p>
									</div>
								</li>
								<li>8、开启验证码：设置登录失败几次后出现验证码功能，-1=永远不开启</li>
								<li>9、返回ssoId：前端登录成功后，json是否增加返回ssoId数据（参数名：ssoId）</li>
								<li>10、验证客户端IP：验证ssoId所属的ip，是不是当前客户端的IP</li>
							</ol>
						</div>
					</div>
				</div>
			</div>
			<!-- 缓存配置 -->
			<div class="layui-colla-item">
				<input type="hidden" name="objCache.id" value="${objCache.id}" />
				<input type="hidden" name="objCacheRedis.id" value="${objCacheRedis.id}" />
				<h2 class="layui-colla-title">缓存配置</h2>
				<div class="layui-colla-content layui-show">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">产品</label>
							<div class="layui-input-inline">
								<select class="select" name="objCache.type">
									<option value="1">Redis</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">使用方式</label>
							<div class="layui-input-inline">
								<select class="select" name="objCacheRedis.useWay" lay-filter="useWay">
									<c:forEach items="${useWayMap}" var="t_map">
										<option value="${t_map.key}"  <c:if test="${objCacheRedis.useWay==t_map.key}"> selected="selected"</c:if>>${t_map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">密码</label>
							<div class="layui-input-inline">
								<input type="text" name="objCacheRedis.passWord" value="${objCacheRedis.passWord}" class="layui-input" placeholder="连接redis的密码" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">数据库</label>
							<div class="layui-input-inline">
								<input type="int" name="objCacheRedis.index" value="${objCacheRedis.index}" class="layui-input" placeholder="指定的数据库索引" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="闲置超过这个时间数后，将删除ssoId" style="font-size:12px">ssoId过期时间</label>
							<div class="layui-input-inline">
								<input type="int" name="objCacheRedis.ssoIdExpiry" value="${objCacheRedis.ssoIdExpiry!=null?objCacheRedis.ssoIdExpiry:1800}" val-type="empty" class="layui-input" placeholder="单位：秒" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="验证码过期时间" style="font-size:12px">验证码过期时间</label>
							<div class="layui-input-inline">
								<input type="int" name="objCacheRedis.captchaExpiry" value="${objCacheRedis.captchaExpiry!=null?objCacheRedis.captchaExpiry:180}" val-type="empty" class="layui-input" placeholder="单位：秒" />
							</div>
							<div class="layui-form-mid layui-word-aux" ><b style="color:red">*</b> 同步至【开启验证码】计数</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 缓存配置 —> redis（使用方式） —> 单节点 -->
			<div class="layui-colla-item" type="redisUseWayGroup" id="redis_useWay_1">
				<h2 class="layui-colla-title">缓存配置 —> redis（使用方式） —> 单节点</h2>
				<div class="layui-colla-content layui-show">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">ip</label>
							<div class="layui-input-inline">
								<input type="text" name="objCacheRedis.ip" value="${objCacheRedis.ip}" class="layui-input" placeholder="连接redis的ip" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">端口</label>
							<div class="layui-input-inline">
								<input type="int" name="objCacheRedis.port" value="${objCacheRedis.port}" class="layui-input" placeholder="连接redis的端口" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 缓存配置 —> redis（使用方式） —> 哨兵 -->
			<div class="layui-colla-item" type="redisUseWayGroup" id="redis_useWay_3">
				<h2 class="layui-colla-title">缓存配置 —> redis（使用方式） —> 哨兵</h2>
				<div class="layui-colla-content layui-show">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">主服务名</label>
							<div class="layui-input-inline">
								<input type="text" name="objCacheRedis.sentinelMasterName" value="${objCacheRedis.sentinelMasterName}" class="layui-input" placeholder="哨兵主服务名" />
							</div>
						</div>
					</div>
					<div id="shaoBingPanel">
						<c:set var="t_index" value="0" />
						<c:forEach items="${objCacheRedisSentinelList}" var="t_obj" varStatus="status_obj">
							<div class="layui-form-item" index="${status_obj.index}">
								<div class="layui-inline">
									<label class="layui-form-label">ip</label>
									<div class="layui-input-inline">
										<input type="text" name="cacheRedisSentinelList[${status_obj.index}].ip" value="${t_obj.ip}" class="layui-input" placeholder="哨兵ip" />
									</div>
								</div>
								<div class="layui-inline">
									<label class="layui-form-label">端口</label>
									<div class="layui-input-inline">
										<input type="text" name="cacheRedisSentinelList[${status_obj.index}].port" value="${t_obj.port}" class="layui-input" placeholder="哨兵端口" />
									</div>
								</div>
								<div class="layui-inline">
									<div class="layui-input-inline">
										<button type="button" class="layui-btn layui-btn-danger" onClick="deleteSentinel(this)"><i class="layui-icon layui-icon-reduce-circle"></i> 删除</button>
									</div>
								</div>
							</div>
							<c:set var="t_index" value="${t_index+1}" />
						</c:forEach>
						<div class="layui-form-item" index="${t_index}">
							<div class="layui-inline">
								<label class="layui-form-label">ip</label>
								<div class="layui-input-inline">
									<input type="text" name="cacheRedisSentinelList[${t_index}].ip" class="layui-input" placeholder="哨兵ip" />
								</div>
							</div>
							<div class="layui-inline">
								<label class="layui-form-label">端口</label>
								<div class="layui-input-inline">
									<input type="text" name="cacheRedisSentinelList[${t_index}].port" class="layui-input" placeholder="哨兵端口" />
								</div>
							</div>
							<div class="layui-inline">
								<div class="layui-input-inline">
									<button type="button" class="layui-btn layui-btn-danger" onClick="deleteSentinel(this)"><i class="layui-icon layui-icon-reduce-circle"></i> 删除</button>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<div class="layui-input-inline">
								<button type="button" class="layui-btn" onClick="addSentinel(this)"><i class="layui-icon layui-icon-add-circle" ></i> 追加新的哨兵</button>
							</div>
						</div>
					</div>
					<input type="hidden" id="lastIndex" value="${t_index}" />
				</div>
			</div>
			<!-- 缓存配置 —> redis（Jedis） —> 连接池 -->
			<div class="layui-colla-item">
				<input type="hidden" name="objCacheRedisPool.id" value="${objCacheRedisPool.id}" />
				<h2 class="layui-colla-title">缓存配置 —> redis（Jedis） —> 连接池</h2>
				<div class="layui-colla-content layui-show">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label" title="最大空闲连接数">最大空闲连接数</label>
							<div class="layui-input-inline">
								<input type="number" step="1" name="objCacheRedisPool.maxIdle" value="${objCacheRedisPool.maxIdle}" class="layui-input" placeholder="最大空闲连接数" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">最大连接数</label>
							<div class="layui-input-inline">
								<input type="number" step="1" name="objCacheRedisPool.maxTotal" value="${objCacheRedisPool.maxTotal}" class="layui-input" placeholder="最大连接数" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="最大等待时间数">最大等待时间数</label>
							<div class="layui-input-inline">
								<input type="number" step="1" name="objCacheRedisPool.maxWaitMillis" value="${objCacheRedisPool.maxWaitMillis}" class="layui-input" placeholder="单位：毫秒" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="在取时，是否验证可用">获取时验证可用</label>
							<div class="layui-input-inline">
								<select class="select" name="objCacheRedisPool.testOnBorrow">
									<option value="">请选择</option>
									<option value="true" <c:if test="${objCacheRedisPool.testOnBorrow==true}"> selected="selected"</c:if>>是</option>
									<option value="false" <c:if test="${objCacheRedisPool.testOnBorrow==false}"> selected="selected"</c:if>>否</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="回收时，是否验证可用">回收时验证可用</label>
							<div class="layui-input-inline">
								<select class="select" name="objCacheRedisPool.testOnReturn">
									<option value="">请选择</option>
									<option value="true" <c:if test="${objCacheRedisPool.testOnReturn==true}"> selected="selected"</c:if>>是</option>
									<option value="false" <c:if test="${objCacheRedisPool.testOnReturn==false}"> selected="selected"</c:if>>否</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="是否开启JMX监控">开启JMX监控</label>
							<div class="layui-input-inline">
								<select class="select" name="objCacheRedisPool.jmxEnabled">
									<option value="">请选择</option>
									<option value="true" <c:if test="${objCacheRedisPool.jmxEnabled==true}"> selected="selected"</c:if>>是</option>
									<option value="false" <c:if test="${objCacheRedisPool.jmxEnabled==false}"> selected="selected"</c:if>>否</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="是否开启空闲资源检测">开启空闲资源检测</label>
							<div class="layui-input-inline">
								<select class="select" name="objCacheRedisPool.testWhileIdle">
									<option value="">请选择</option>
									<option value="true" <c:if test="${objCacheRedisPool.testWhileIdle==true}"> selected="selected"</c:if>>是</option>
									<option value="false" <c:if test="${objCacheRedisPool.testWhileIdle==false}"> selected="selected"</c:if>>否</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="timeBetweenEvictionRunsMillis，空闲资源的检测周期">空闲资源的检测周期</label>
							<div class="layui-input-inline">
								<input type="number" step="1" name="objCacheRedisPool.timeBetweenEvictionRunsMillis" value="${objCacheRedisPool.timeBetweenEvictionRunsMillis}" class="layui-input" placeholder="单位：毫秒" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="minEvictableIdleTimeMillis，资源池中资源的最小空闲时间，达到此值后空闲资源将被移除">最小空闲时间</label>
							<div class="layui-input-inline">
								<input type="number" step="1" name="objCacheRedisPool.minEvictableIdleTimeMillis" value="${objCacheRedisPool.minEvictableIdleTimeMillis}" class="layui-input" placeholder="单位：毫秒" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="numTestsPerEvictionRun，做空闲资源检测时，每次检测资源的个数">检测资源的个数</label>
							<div class="layui-input-inline">
								<input type="number" step="1"  name="objCacheRedisPool.numTestsPerEvictionRun" value="${objCacheRedisPool.numTestsPerEvictionRun}" class="layui-input"  />
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 缓存配置 -> key前缀 -->
			<div class="layui-colla-item">
				<input type="hidden" name="objCachePrefix.id" value="${objCachePrefix.id}" />
				<h2 class="layui-colla-title">缓存配置 —> key前缀</h2>
				<div class="layui-colla-content layui-show">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">ssoId</label>
							<div class="layui-input-inline">
								<input type="text" name="objCachePrefix.ssoId" value="${objCachePrefix.ssoId!=null?objCachePrefix.ssoId:'sso:'}" val-type="empty" class="layui-input" placeholder="ssoId前缀" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">验证码</label>
							<div class="layui-input-inline">
								<input type="text" name="objCachePrefix.captcha" value="${objCachePrefix.captcha!=null?objCachePrefix.captcha:'captcha:'}" val-type="empty" class="layui-input" placeholder="验证码前缀" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">密码错误数</label>
							<div class="layui-input-inline">
								<input type="text" name="objCachePrefix.pwdErrorNum" value="${objCachePrefix.pwdErrorNum!=null?objCachePrefix.pwdErrorNum:'pwdErrorNum:'}" val-type="empty"  class="layui-input" placeholder="密码错误数前缀" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Cookie配置—> ssoId -->
			<div class="layui-colla-item">
				<input type="hidden" name="objCookie.id" value="${objCookie.id}" />
				<h2 class="layui-colla-title">Cookie配置 —> ssoId</h2>
				<div class="layui-colla-content layui-show">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">domain</label>
							<div class="layui-input-inline">
								<input type="text" name="objCookie.domain" value="${objCookie.ssoIdName}" val-type="empty" class="layui-input" placeholder="cookie的domain" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">name</label>
							<div class="layui-input-inline">
								<input type="text" name="objCookie.ssoIdName" value="${objCookie.ssoIdName!=null?objCookie.ssoIdName:'_wind-sso_ssoId'}" val-type="empty" class="layui-input" placeholder="ssoId的名称" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">path</label>
							<div class="layui-input-inline">
								<input type="text" name="objCookie.ssoIdPath" value="${objCookie.ssoIdPath!=null?objCookie.ssoIdPath:'/'}" val-type="empty" class="layui-input" placeholder="ssoId的适用路径" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" title="闲置超过这个时间数后，将删除该cookie">过期时间</label>
							<div class="layui-input-inline">
								<input type="int" name="objCookie.ssoIdExpiry" value="${objCookie.ssoIdExpiry!=null?objCookie.ssoIdExpiry:604800}" val-type="empty" class="layui-input" placeholder="单位：秒" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 按钮 -->
		<div style="text-align:center;padding:15px">
			<div class="layui-btn-group">
				<button type="button" class="layui-btn" onClick="save(this)">立 即 保 存</button>
				<button type="button" class="layui-btn layui-btn-normal" onClick="formReset(this)">重 置</button>
			</div>
		</div>
		<!-- 隐藏元素 -->
		<div style="display: none">
			<input type="hidden" name="obj.id" value="${obj.id}" />
		</div>	
	</form>
</body>
</html>