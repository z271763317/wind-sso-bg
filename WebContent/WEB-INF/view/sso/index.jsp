<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>${_moduleName}（${objAuth.domain}）</title>
	<jsp:include page="/globalFile/global.jsp" />
	<style type="text/css">
		
	</style>
	<script type="text/javascript" >
		var authId="${objAuth.id}";
		//页面渲染后执行
		$(document).ready(function(){
			//表格配置
			var tableConfig={
				url:pathName+'/list?authId='+authId
				,cols: [[
					{type:'checkbox', fixed: 'left'}
					,{field:'id', title: 'ssoId'}
					,{field:'expiry', title: '过期时间（单位：秒）',width:170}
					,{field:'ip', title: '客户端IP',width:130}
					,{field:'userName', title: '用户名',width:140}
					,{field:'createTime', title: '创建时间',width:145}
					,{fixed: 'right', title:'操作', toolbar: '#bar'}
				]]
			}
			renderTable(tableConfig,function(){
				//监听行工具事件
				layui_table.on('tool(list)', function(obj){
					var data = obj.data;
				    var id=data["id"];
				    var event=obj.event;
				    switch(event){
				    	//删除
				    	case "del":{
				    		del("delete?authId="+authId,id,null,null);
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
	</script>
</head>
<body>
	<div>
		<!-- 条件区 -->
		<blockquote class="layui-elem-quote quoteBox">
			<form class="layui-form" style="width:100%">
				<table class="table" style="width:100%">
					<tr>
						<td style="width:100px;text-align:right"><b style="color:red">当前域名</b>：</td>
						<td style="width:200px;">
							<div class="layui-input-inline"  style="width:100%">
								<input type="text" class="layui-input" value="${objAuth.domain}" placeholder="全字匹配"  readonly="readonly" />
							</div>
						</td>
						<td style="width:50px;text-align:right">ssoId：</td>
						<td style="width:250px;">
							<div class="layui-input-inline"  style="width:100%">
								<input type="text" name="ssoId" class="layui-input" placeholder="全字匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="width:70px;text-align:right">客户端ip：</td>
						<td style="width:120px;">
							<div class="layui-input-inline" >
								<input type="text" name="ip" class="layui-input" placeholder="全字匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="width:70px;text-align:right">用户名：</td>
						<td style="width:120px;">
							<div class="layui-input-inline" >
								<input type="text" name="userName" class="layui-input" placeholder="全字匹配" onkeydown="if(event.keyCode==13) {search(this)}"/>
							</div>
						</td>
						<td style="text-align:right">
							<div class="layui-btn-group">
								<button type="button" class="layui-btn layui-btn-normal " data-type="reload" onClick="search(this)"><i class="layui-icon layui-icon-search" style="font-size: 18px;"></i> 搜 索</button>
								<button type="button"  class="layui-btn layui-btn-danger" onClick="delSelect(this)" url="deleteSelect?authId=${objAuth.id}">批量下线</button>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</blockquote>
		<!-- 列表数据 -->
		<div id="list" lay-filter="list"  style="display: none;"></div>
		<!-- 每行按钮 -->
		<div type="text/html" id="bar" style="display: none;">
			<div class="layui-btn-group">
				<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">下线</button>
			</div>
		</div>
	</div>
</body>
</html>