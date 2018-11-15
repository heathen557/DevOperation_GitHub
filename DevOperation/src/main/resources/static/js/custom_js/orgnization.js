var $table = $('#reportTable');

var globalParams = {};
var tree;
var updateoraddFlag;
var datas = [];
var deletenodeid;
var orglevel = [ '平台级', '市级', '区级', '街道级', '停车场级' ];
datas[0] = {
	"nodeid" : "1",
	"nodename" : "平台",
	"path" : "/1",
	"remark" : "初始节点"
};
var selectPath;
var allTableData = [];
var sameorg;
// 获取选中的id
function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'), function(row) {
		console.log(row);
		return row.path;
	});
}

$(function() {
	initTable(null);

});

// 新增下级机构
function addOrg() {

	$('#addorgModal').modal('show');

}

// 新增下级机构 提交按钮
function submitaddorgData() {

	var param = $("#myaddorgform").serialize();

	// var a = $table.bootstrapTable('getSelections');
	param += '&lastpath=' + selectPath;
	console.log('新增下级机构的参数：param:' + param);
	$.ajax({
		type : "get",
		async : false,
		url : BASE_URL + "/orgnization/addorg",
		data : param,
		dataType : 'json',
		error : function() {
			alert('请求失败！ ');
		},
		success : function(data) {
			// console.log("保存返回的数据:"+JSON.stringify(data));
			$('#addorgModal').modal('hide');
			// if(data['msg']){
			// toastr.success(data['msg']);
			// }else{
			// toastr.error(data['msg']);
			// }
			initTable(null);
			// initTreeView();
		}

	});

}

function sameDev() {

	var param = $("#myeditorgform").serialize();

	param += "&sameorgname=";
	param += sameorg;
	console.log('myeditorgform:' + param);

	$.ajax({
		type : "get",
		async : false,
		url : BASE_URL + "/orgnization/editorg",
		data : param,
		dataType : 'json',
		error : function() {
			alert('请求失败！ ');
		},
		success : function(data) {
			// console.log("保存返回的数据:"+JSON.stringify(data));
			$('#EditorgModal').modal('hide');
			// if(data['msg']){
			// toastr.success(data['msg']);
			// }else{
			// toastr.error(data['msg']);
			// }
			initTable(null);
		}

	});

	$('#sameModal').modal('hide');
}

function submitEditData() {
	
	for (var i = 0; i < allTableData.length; i++) 
	{
		if (allTableData[i].nodename == $("#txt_orgname").val()) 
		{
			$('#sameModal').modal('show');
			sameorg = allTableData[i].nodename;
			 alert("机构名称不可重复！");
			return;
		}
	}
	
//	console.log("已经运行带这里了~");
	
	sameorg = null;
	var param = $("#myeditorgform").serialize();
	console.log('myeditorgform:' + param);
	param += "&sameorgname=";
	param += sameorg;
	$.ajax({
		type : "get",
		async : false,
		url : BASE_URL + "/orgnization/editorg",
		data : param,
		dataType : 'json',
		error : function() {
			alert('请求失败！ ');
		},
		success : function(data) {
			// console.log("保存返回的数据:"+JSON.stringify(data));
			$('#EditorgModal').modal('hide');
			// if(data['msg']){
			// toastr.success(data['msg']);
			// }else{
			// toastr.error(data['msg']);
			// }
			initTable(null);
		}

	});
}

function cancelDev() {

	// deletenodeid = '';
	// var param = $("#myeditorgform").serialize();
	// console.log('myeditorgform:'+param);
	$.ajax({
		type : "get",
		async : false,
		url : BASE_URL + "/orgnization/deleteorg",
		data : {
			deletenodeid : deletenodeid
		},
		dataType : 'json',
		error : function() {
			alert('请求失败！ ');
		},
		success : function(data) {
			// console.log("保存返回的数据:"+JSON.stringify(data));
			$('#cancelModal').modal('hide');
			// if(data['msg']){
			// toastr.success(data['msg']);
			// }else{
			// toastr.error(data['msg']);
			// }
			initTable(null);
		}

	});
}

window.operateEvents = {
		
	"click #RoleOfCancel" : function(e, value, row, index) {// 删除
		// 模态框显示
		deletenodeid = row.nodeid;

		$('#cancelModal').modal('show');

	},
	"click #RoleOfEdit" : function(e, value, row, index) {// 编辑

		$('#txt_editnodeid').val(row.nodeid);
		var regex = new RegExp('/', 'g');
		var result = row.path.match(regex);
		var count = !result ? 0 : result.length;
		console.log("row =" + row.toString +" 数量为 " + count);
		
		$('#txt_level').val(count);                     //市级、区级、街道级、停车场及
		$('#txt_orgname').val(row.nodename);            //机构名称
		$('#txt_lastlevel').empty();					//上级级别
		
		var count = $('#txt_level').val();
		if ((count - 1) == 1) {
			$('#txt_lastlevel').attr("disabled", true);
		} else {

			// console.log("上级级别 处理");
			$('#txt_lastlevel').attr("disabled", false);
			
			// for (var i = 2; i < count; i++) {
			//					
			// $("#txt_lastlevel").append("<option value='" + String(i) + "'>" +
			// orglevel[i-1]+ "</option>");
			// }
			
			$("#txt_lastlevel").append(
					"<option value='" + String(count - 1) + "'>"
							+ orglevel[count - 2] + "</option>");

		}
		
		
		
		
		$("#txt_lastorgname").empty();         //机构名称
		var level = $("#txt_lastlevel").val();
		$.ajax({
			type : "get",
			async : false,
			url : BASE_URL + "/orgnization/findorgBylevel",
			data : {
				level : level
			},
			dataType : 'json',
			error : function() {
				alert('请求失败！ ');
			},
			success : function(data) {
				// console.log("保存返回的数据:"+JSON.stringify(data));
				for (var i = 0; i < data.rows.length; i++) {
					$("#txt_lastorgname").append(
							"<option value='" + data.rows[i] + "'>"
									+ data.rows[i] + "</option>");
				}
			}

		});
		$('#EditorgModal').modal('show');
	}
}

function changeLastlevel() {

	$('#txt_lastlevel').empty();
	var count = $('#txt_level').val();
	if ((count - 1) == 1) {
		$('#txt_lastlevel').attr("disabled", true);
	} else {

		// console.log("上级级别 处理");
		$('#txt_lastlevel').attr("disabled", false);
		// for (var i = 2; i < count; i++) {
		//			
		// $("#txt_lastlevel").append("<option value='" + String(i) + "'>" +
		// orglevel[i-1]+ "</option>");
		// }
		$("#txt_lastlevel").append(
				"<option value='" + String(count - 1) + "'>"
						+ orglevel[count - 2] + "</option>");

		$("#txt_lastorgname").empty();
		var level = $("#txt_lastlevel").val();
		$.ajax({
			type : "get",
			async : false,
			url : BASE_URL + "/orgnization/findorgBylevel",
			data : {
				level : level
			},
			dataType : 'json',
			error : function() {
				alert('请求失败！ ');
			},
			success : function(data) {
				// console.log("保存返回的数据:"+JSON.stringify(data));
				for (var i = 0; i < data.rows.length; i++) {
					$("#txt_lastorgname").append(
							"<option value='" + data.rows[i] + "'>"
									+ data.rows[i] + "</option>");
				}
			}

		});

	}
}

function getLastorgname() {
	// $("#txt_lastorgname").empty();
	// var level = $("#txt_lastlevel").val();
	// $.ajax({
	// type: "get",
	// async:false,
	// url:BASE_URL + "/orgnization/findorgBylevel",
	// data:{level:level},
	// dataType: 'json',
	// error : function() {
	// alert('请求失败！ ');
	// },
	// success: function(data) {
	// // console.log("保存返回的数据:"+JSON.stringify(data));
	// for (var i = 0; i < data.rows.length; i++) {
	// $("#txt_lastorgname").append("<option value='" +data.rows[i] +"'>" +
	// data.rows[i]+ "</option>");
	// }
	// }
	//     
	// });
}

function operateFormatter(value, row, index) {
	return [

			'<button type="button" id="RoleOfCancel" class="btn btn-default  btn-sm" style="margin-right:15px;"><span class="fa fa-trash" aria-hidden="true"></span></button>',
			'<button type="button" id="RoleOfEdit" class="btn btn-default  btn-sm" style="margin-right:15px;"><span class="fa fa-pencil" aria-hidden="true"></span></button>' ]
			.join('');
}

function initTable(searchArgs) {
	$table.bootstrapTable('destroy');

	$table.bootstrapTable({
		method : 'get',
		// url : '/devoperation/orgnization/GetParentNode',
		cache : false,
		dataType : "json", // 数据类型
		uniqueId : "nodeid",// 每行的唯一标识
		// height: 400,
		toolbar : '#toolbar',
		striped : true,
		pagination : true,
		pageSize : 20,
		pageNumber : 1,
		pageList : [ 20, 50, 100, 250 ],
		search : false,
		showColumns : true,
		showRefresh : true,
		showExport : false,
		exportTypes : [ 'csv', 'txt', 'xml' ],
		// queryParams: queryParams,
		responseHandler : responseHandler,
		clickToSelect : false,
		showHeader : false,

		// sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
		// dataField: 'rows', //后端 json 对应的表格数据 key
		// editable:true,//开启编辑模式
		detailView : true,// 父子表
		data : datas,
		columns : [ {
			field : 'state',
			checkbox : true,
			align : 'center',
			valign : 'middle'
		},
		// {field:"rowid",title:"序列号",align:"center",valign:"middle",sortable:"false",visible:false},
		{
			field : "nodeid",
			title : "节点ID",
			align : "center",
			valign : "middle",
			sortable : "false",
			visible : false
		}, {
			field : "nodename",
			title : "机构",
			align : "center",
			valign : "middle",
			sortable : "false"
		}, {
			field : "path",
			title : "级别",
			align : "center",
			valign : "middle",
			sortable : "false",
			visible : false
		}, {
			field : "remark",
			title : "备注",
			align : "center",
			valign : "middle",
			sortable : "false",
			visible : false
		},
		// {
		// field: 'operate',
		// title: '操作',
		// align: 'center',
		// events: "operateEvents",
		// formatter: operateFormatter
		// }
		],
		// 注册加载子表的事件。注意下这里的三个参数！

		// index：父表当前行的行索引。

		// row：父表当前行的Json数据对象。

		// $detail：当前行下面创建的新行里面的td对象。

		// 第三个参数尤其重要，因为生成的子表的table在装载在$detail对象里面的。bootstrap
		// table为我们生成了$detail这个对象，然后我们只需要往它里面填充我们想要的table即可。

		onExpandRow : function(index, row, $detail) {

			var regex = new RegExp('/', 'g');
			var result = row.path.match(regex);
			var count = !result ? 0 : result.length;
			console.log("数量为 " + count);
			if (count < 5) {
				InitSubTable(index, row, $detail);
			}
		},

		onLoadSuccess : function() {
			// 加载成功时执行
			console.log("加载成功!");

		},
		onLoadError : function() {
			// 加载失败时执行
			console.log("加载失败!");

		},
		formatLoadingMessage : function() {
			// 正在加载
			return "请稍等，正在加载中...";
		},
		formatNoMatches : function() {
			// 没有匹配的结果
			return '无符合条件的记录';
		}

	});

}

// 初始化子表格(无线循环)
function InitSubTable(index, row, $detail) {
	var parentid = row.nodeid;
	console.log("parentid:" + parentid);

	var cur_table = $detail.html('<table></table>').find('table');
	$(cur_table).bootstrapTable(
			{
				url : BASE_URL + '/orgnization/GetParentNode',
				method : 'get',
				cache : false,
				dataType : "json", // 数据类型
				queryParams : {
					parentid : parentid
				},
				clickToSelect : true,
				detailView : true,// 父子表
				uniqueId : "nodeid",
				pageSize : 100,
				pageList : [ 100, 250 ],
				showHeader : false,
				
				sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
				dataField : 'rows', // 后端 json 对应的表格数据 key
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle'
				},
				// {field:"rowid",title:"序列号",align:"center",valign:"middle",sortable:"false",visible:false},
				{
					field : "nodeid",
					title : "节点ID",
					align : "center",
					valign : "middle",
					sortable : "false",
					visible : false
				}, {
					field : "nodename",
					title : "机构",
					align : "center",
					valign : "middle",
					sortable : "false"
				}, {
					field : "path",
					title : "级别",
					align : "center",
					valign : "middle",
					sortable : "false",
					visible : false
				}, {
					field : "remark",
					title : "备注",
					align : "center",
					valign : "middle",
					sortable : "false",
					visible : false
				}, {
					field : 'operate',
					title : '操作',
					align : 'center',
					events : "operateEvents",
					formatter : operateFormatter
				} ],
				// 无线循环取子表，直到子表里面没有记录
				onExpandRow : function(index, row, $Subdetail) {
					InitSubTable(index, row, $Subdetail);
				},
				onCheck : function(row) {

					$("#txt_addorglevel").empty();
					var curpath = row.path;
					selectPath = curpath;
					var regex = new RegExp('/', 'g');
					console.log("curpath:" + curpath);
					var result = curpath.match(regex);
					var count = !result ? 0 : result.length;
					console.log("数量为 " + count);
					if (count == 5) {
						$("#addOrgBtn").attr("disabled", true);
					} else {
						$("#addOrgBtn").attr("disabled", false);
					}
					var level = count + 1;
					$("#txt_addorglevel").append(
							"<option value='" + String(count) + "'>"
									+ orglevel[count] + "</option>");
				},
				onClickRow : function(row) {
					allTableData = $(cur_table).bootstrapTable('getData');// 获取表格的所有内容行
					console.log("alltabledata:" + allTableData);

				}
			});

};

function queryParams(params) {
	var param = {};
	// param[$(this).attr('name')] = globalParams['devid'];//默认为devid=0
	// $('#query-form').find('[name]').each(function () {
	// var value = $(this).val();
	// console.log(value);
	// if (value != '') {
	// console.log(value);
	// param[$(this).attr('name')] = value;
	// }
	// });

	param['pageSize'] = params.limit; // 页面大小
	param['pageNumber'] = params.offset/params.limit + 1;   // 页码
	// param['nodeid'] = globalParams['nodeid'];
	console.log('params:' + params.offset);
	console.log(param);
	return param;
}

// 请求成功方法
function responseHandler(result) {
	console.log("result.total:", result.total);
	console.log("result.rows:", result.rows);
	// var errcode = result.errcode;//在此做了错误代码的判断
	//    
	// if(errcode != 0){
	// alert("错误代码" + errcode);
	// return;
	// }
	// 如果没有错误则返回数据，渲染表格
	return {
		total : result.total, // 总页数,前面的key必须为"total"
		rows : result.rows
	// 行数据，前面的key要与之前设置的dataField的值一致.
	};
};

function customSearch(text) {
	$('#query-form').find('[name]').each(function() {
		var value = $(this).val();
		// console.log(value);
		if (value != '') {
			// console.log(value);
			globalParams['devId'] = value;
		} else {
			alert("设备号不能为空！");
			$('#addberthbtn').prop('disable', true);
			return;
		}
	});
	$('#tree').treeview('clearSearch');
	// 通过设备号查找节点
	$.ajax({
		type : "get",
		url : BASE_URL + "/orgnization/searchnode",
		dataType : "json",
		success : function(result) {
			// 树形菜单初始化
			console.log("search nodeid is :" + result);
			if (result == '') {
				alert("搜索结果为空！");
			} else {

				var searchNodes = $('#tree').treeview('search', [ result, {
					ignoreCase : true, // case insensitive
					exactMatch : false, // like or equals
					revealResults : true, // reveal matching nodes
				} ]);
				$('#tree').treeview('expandNode', [ searchNodes, {
					levels : 2,
					silent : true
				} ]);
			}

		}

	});
	globalParams['nodeid'] = result;
	$('#addberthbtn').prop('disable', true);
	$table.bootstrapTable('refresh');// 刷新Table，Bootstrap Table 会自动执行重新查询
}

// 查找组织机构
// function orgSearch(){
// dataInit();
// var nodeId;
// var orgnization = $("#orgsearch").val();
// console.log("orgnization is :" + orgnization);
// var searchNodes = $('#tree').treeview('search', [orgnization, {
// ignoreCase: true, // case insensitive
// exactMatch: false, // like or equals
// revealResults: true, // reveal matching nodes
// }]);
// $('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true }
// ]);
// console.log("searchnodes:" + searchNodes);
// // if(searchNodes.length==1){
// // nodeId = searchNodes[0].nodeId;
// // $("#treeview").treeview('selectNode',[nodeId,{silent:true}]);
// // }else{
// // $("#treeview").treeview('selectNode',[0,{silent:true}]);
// // }
// if(searchNodes[0].level == 6){
// globalParams['nodeid'] = nodeId;
//		
// }
//
// }

function resetSearch() {
	$('#query-form').find('[name]').each(function() {
		$(this).val('');
	});
}
