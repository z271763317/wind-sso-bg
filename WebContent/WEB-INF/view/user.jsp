<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>SSO后台管理系统</title>
	<jsp:include page="/globalFile/global.jsp" />
	<base href="${path}" />
	<link rel="stylesheet" href="css/user/main.css" media="all" />
	<script type="text/javascript" src="js/user/main.js"></script>
	<script type="text/javascript" src="js/user/cache.js"></script>
	<script type="text/javascript" >
		var rightHeight;
		var rightContentHeight;
		//页面渲染后执行
		$(document).ready(function(){
			init_height();
			//
			$("#contentIframe").attr("src","user/index");
			$("#contentIframe").css("height", rightContentHeight + "px");
		});
		//初始化_高度
		function init_height() {
			rightHeight = window.screen.height - 208;
			rightContentHeight = rightHeight - 57;
		}
		//退出
		function exit(){
			confirm("退出","您确定要退出系统吗？",function(){
				ajax("user/exit",{},function(result){
					parent.window.location.href=result["href"];
				});
			});
		}
		//修改密码UI
		function modifyPassWordUI(obj){
			var html='';
			html+='<form action="'+pathName+'/modifyPassWord" method="post" class="layui-form" >';
			//
			html+='<div class="layui-form-item">';
			html+='<label class="layui-form-label">旧密码：</label>';
			html+='<div class="layui-input-block">';
			html+='<input type="password" name="oldPassWord" class="layui-input" />';
			html+='</div>';
			html+='</div>';
			//
			html+='<div class="layui-form-item">';
			html+='<label class="layui-form-label">新密码：</label>';
			html+='<div class="layui-input-block">';
			html+='<input type="password" name="newPassWord" class="layui-input" />';
			html+='</div>';
			html+='</div>';
			//
			html+='<div class="layui-form-item">';
			html+='<label class="layui-form-label">确认密码：</label>';
			html+='<div class="layui-input-block">';
			html+='<input type="password" name="confirmNewPassWord" class="layui-input" />';
			html+='</div>';
			html+='</div>';
			//
			html+='<div style="text-align:center;padding:10px">';
			html+='<button type="button" class="layui-btn" onClick="modifyPassWord(this)">修 改</button>';
			html+='</div>';
			//
			html+='</form>';
			global_dialog_id=htmlDialog("修改密码",html,360);
		}
		//修改密码
		function modifyPassWord(obj){
			save(obj,function(){
				closeDialog(global_dialog_id);
				promptDialog(true,"修改成功");
			});
		}
	</script>
</head>
<body class="main_body">
	<div class="layui-layout layui-layout-admin">
		<!-- 顶部 -->
		<div class="layui-header header">
			<div class="layui-main mag0">
				<a href="#" class="logo" style="margin-left:-15px;font-size:18px">SSO后台管理系统</a>
				<!-- 显示/隐藏菜单 -->
				<a href="javascript:;" class="seraph hideMenu" >
					<div class="box" style="padding-top:4px;" ></div>
					<div class="box"></div>
					<div class="box"></div>
				</a>
				<!-- 顶级菜单 -->
				<ul class="layui-nav mobileTopLevelMenus" mobile>
					<li class="layui-nav-item" data-menu="mainSystem">
						<a href="javascript:;"><i class="seraph icon-caidan"></i><cite>系统列表</cite></a>
						<dl class="layui-nav-child">
							<dd class="layui-this" data-menu="mainSystem"><a href="javascript:;"><cite>主系统</cite></a></dd>
						</dl>
					</li>
				</ul>
				<ul class="layui-nav topLevelMenus" pc>
					<li class="layui-nav-item layui-this" data-menu="mainSystem"><a href="javascript:;"><cite>主系统</cite></a></li>
				</ul>
			    <!-- 顶部右侧菜单 -->
			    <ul class="layui-nav top_menu">
					<li class="layui-nav-item" pc>
						<a href="javascript:;" class="clearCache"><i class="layui-icon" data-icon="&#xe640;">&#xe640;</i><cite>清除缓存</cite></a>
					</li>
					<li class="layui-nav-item lockcms" pc>
						<a href="javascript:;"><i class="seraph icon-lock"></i><cite>锁屏</cite></a>
					</li>
					<li class="layui-nav-item" id="userInfo">
						<a href="javascript:;"><cite class="adminName" >${userName}</cite></a>
						<dl class="layui-nav-child">
							<dd><a href="javascript:;"  onClick="modifyPassWordUI(this)"><cite>修改密码</cite></a></dd>
							<dd pc><a href="javascript:;" class="functionSetting"><cite>功能设定</cite></a></dd>
							<dd pc><a href="javascript:;" class="changeSkin"><cite>更换皮肤</cite></a></dd>
							<dd><a href="javascript:exit();" class="signOut"><cite>退出</cite></a></dd>
						</dl>
					</li>
				</ul>
			</div>
		</div>
		<!-- 左侧导航 -->
		<div class="layui-side layui-bg-black">
			<!-- 搜索 -->
			<div class="layui-form component">
				<select name="search" id="search" lay-search lay-filter="searchPage" ></select>
				<i class="layui-icon">&#xe615;</i>
			</div>
			<div class="navBar layui-side-scroll" id="navBar" >
				<ul class="layui-nav layui-nav-tree">
					<div id="navBar_inner"></div>
				</ul>
			</div>
		</div>
		<!-- 右侧内容 -->
		<div class="layui-body layui-form">
			<div class="layui-tab mag0" lay-filter="bodyTab" id="top_tabs_box" style="margin-top:0px">
				<ul class="layui-tab-title top_tab" id="top_tabs">
					<li class="layui-this" lay-id=""><cite>首页</cite></li>
				</ul>
				<ul class="layui-nav closeBox">
				  <li class="layui-nav-item">
				    <a href="javascript:;"><i class="layui-icon caozuo" style="font-size:16px">&#xe643;</i>页面操作</a>
				    <dl class="layui-nav-child">
					  <dd><a href="javascript:;" class="refresh refreshThis">刷新当前</a></dd>
				      <dd><a href="javascript:;" class="closePageOther">关闭其他</a></dd>
				      <dd><a href="javascript:;" class="closePageAll">关闭全部</a></dd>
				    </dl>
				  </li>
				</ul>
				<div class="layui-tab-content clildFrame">
					<div class="layui-tab-item layui-show">
						<!-- 显示内容 -->
						<iframe id="contentIframe" scrolling="yes"></iframe>
					</div>
				</div>
			</div>
		</div>
		<!-- 底部 -->
		<div class="layui-footer footer">
			<p><span>copyright @2022 wind</span>
		</div>
	</div>

	<!-- 移动导航 -->
	<div class="site-tree-mobile"><i class="layui-icon">&#xe602;</i></div>
	<div class="site-mobile-shade"></div>
</body>
</html>