<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_moduleName}</title>
	<jsp:include page="/globalFile/global.jsp" />
	<style type="text/css">
		
	</style>
	<script type="text/javascript" >
		//页面渲染后执行
		$(document).ready(function(){
			//表格配置
			var tableConfig={
				cols: [[
					{type:'checkbox', fixed: 'left'}
					,{field:'domain', title: '域名'}
					,{field:'name', title: '名称'}
					/* ,{field:'status', title: '状态',width:60,toolbar:'#bar_status'} */
					,{field:'isIpLimit', title: 'ip访问限制',width:100,toolbar:'#bar_isIpLimit'}
					,{field:'loginUrl', title: '登录界面URL'}
					,{field:'createTime', title: '创建时间',width:145}
					,{fixed: 'right', title:'操作', toolbar: '#bar',width:250}
				]]
			}
			renderTable(tableConfig,function(){
				//监听行工具事件
				layui_table.on('tool(list)', function(obj){
					var data = obj.data;
				    var id=data["id"];
				    var name=data["name"];
				    var event=obj.event;
				    switch(event){
						//编辑
				    	case "edit":{
				    		updateUI(null,id);
				    		break;
						}
				    	//删除
				    	case "del":{
				    		del(null,id,null,null);
				    		break;
						}
				    	//白名单
				    	case "white":{
				    		ajaxSimple("auth/white",{id:id},function(result){
				    			var t_dataArr=result["data"];
				    			var html='<form action="auth/saveWhite" method="post" class="layui-form" style="width:100%">';
				    			html+='<div id="panel_white" style="width:100%;height:220px;overflow:scroll">';
				    			var whiteList='';
				    			for(var i=0;i<t_dataArr.length;i++){
				    				var t_json=t_dataArr[i];
				    				var t_ip=t_json["ip"];
				    				//
				    				whiteList+='<input type="text" name="whiteList" class="input" value="'+t_ip+'" placeholder="ip地址" style="width:125px;margin-right:5px;margin-bottom:5px"/>';
				    			}
				    			html+=whiteList;
				    			html+='</div>';
				    			html+='<input type="hidden" name="id" value="'+id+'" />';
				    			html+='<div style="padding:20px;text-align:center">';
				    			html+='<button type="button" class="layui-btn layui-btn-normal" onClick="addWhiteInput(this)">添加新的IP输入框</button>';
				    			html+='<button type="button" class="layui-btn" onClick="save(this)">保 存</button>';
				    			html+='</div>';
				    			html+='</form>';
				    			htmlDialog("【"+name+"】白名单",html,930,365);
				    		});
				    		break;
						}
					}
				});
			});
			//起始时间
			renderLaydate({elem: '#startTime',type: 'datetime'});
			//结束时间
			renderLaydate({elem: '#endTime',type: 'datetime'});
		});
		//添加新的白名单输入框
		function addWhiteInput(obj){
			var html='<input type="text" name="whiteList" class="input" placeholder="ip地址" style="width:125px;margin-right:5px;margin-bottom:5px"/>';
			$("#panel_white").append(html);
		}
	</script>
</head>
<body>
	<div>
		<!-- 条件区 -->
		<blockquote class="layui-elem-quote quoteBox">
			<form class="layui-form" style="width:100%">
				<table class="table" style="width:100%">
					<tr>
						<td style="width:50px;text-align:right">域名：</td>
						<td style="width:140px;">
							<div class="layui-input-inline" >
								<input type="text" name="domain" class="layui-input" placeholder="模糊匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="width:50px;text-align:right">名称：</td>
						<td style="width:140px;">
							<div class="layui-input-inline" >
								<input type="text" name="name" class="layui-input" placeholder="模糊匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
					<!-- 	<td style="width:50px;text-align:right">状态：</td>
						<td style="width:110px;">
							<div class="layui-input-inline" style="width:100%">
								<select class="select" name="status">
									<option value="">全部</option>
									<option value="1">启用</option>
									<option value="0">禁用</option>
								</select>
							</div>
						</td> -->
						<td style="width:80px;text-align:right">ip访问限制：</td>
						<td style="width:110px;">
							<div class="layui-input-inline" style="width:100%">
								<select class="select" name="isIpLimit">
									<option value="">全部</option>
									<option value="true">是</option>
									<option value="false">否</option>
								</select>
							</div>
						</td>
						<td style="text-align:right;" rowspan="2">
							<div class="layui-btn-group">
								<button type="button" class="layui-btn layui-btn-normal " onClick="search(this)"><i class="layui-icon layui-icon-search" ></i> 搜 索</button>
								<button type="button"  class="layui-btn" onClick="addUI(this)"><i class="layui-icon layui-icon-add-circle" ></i> 添 加</button>
								<!-- <button type="button"  class="layui-btn" onClick="enableSelect(this)">批量启用</button>
								<button type="button"  class="layui-btn layui-btn-danger" onClick="disableSelect(this)">批量禁用</button> -->
							</div>
						</td>
					</tr>
				</table>
			</form>
		</blockquote>
		<div style="padding:0px 10px;font-size:12px;color:#7A7A7A;line-height:22px">
			<div><b style="color:red">说明</b>：【ip访问限制】=是否开启了ip访问限制（部分功能失效，如：SSO登录页），此时需要设置【白名单】</div>
		</div>
		<!-- 列表数据 -->
		<div id="list" lay-filter="list"  style="display: none;"></div>
		
		<!-- 每行按钮 -->
		<script type="text/html" id="bar" style="display: none;">
			<div class="layui-btn-group">
				<button type="button" class="layui-btn layui-btn-sm" lay-event="edit">编辑</button>
				{{#if (d.isAllowDelete == true) { }}
					<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">删除</button>
				{{# } }}
				<button type="button" class="layui-btn layui-btn-sm layui-btn-normal" lay-event="white">白名单</button>
				<a href="sso?authId={{d.id}}" target="_blank" class="layui-btn layui-btn-sm">SSO管理</a>
			</div>
		</script>
		<div type="text/html" id="bar_status" style="display: none;">
			{{#if (d.status == 1) { }}
			 	<span style="color:green">启用</span>
			{{# }else if(d.status == 2){ }}
				<span style="color:#EE9A00">未审核</span>
			{{# }else if(d.status == 0){ }}
				<span style="color:red">禁用</span>
			{{# }else{ }}
				<span>未知</span>
			{{# } }}
		 </div>
		 <div type="text/html" id="bar_isIpLimit" style="display: none;">
			{{#if (d.isIpLimit == true) { }}
			 	<span>是</span>
			{{# }else if(d.isIpLimit == false){ }}
				<span>否</span>
			{{# }else{ }}
				<span>未知</span>
			{{# } }}
		 </div>
		 
	</div>
</body>
</html>