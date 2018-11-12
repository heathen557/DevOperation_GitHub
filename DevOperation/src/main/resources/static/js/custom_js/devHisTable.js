//手动制造30条数据

var rowsdata = {};
var globalParams = {};
var tree;
var search_flag = true; // 用户点击查询的时候，将初始页码置为1

$(function() {
	dataInit();
})

function dataInit() {
	tree = [ {
		"text" : "中心",
		"href" : "0"
	} ];

	globalParams['nodeid'] = '1';
	globalParams['devId'] = '0';

}

$(function() {
	$.ajax({
		type : "Post",
		url : BASE_URL + "/orgnization/inittree",
		dataType : "json",
		success : function(result) {
			// 树形菜单初始化
			$('#tree').treeview({
				data : result,
				// showCheckbox: true,
				highlightSelected : true,
				// nodeIcon: 'glyphicon glyphicon-user', //节点上的图标
				nodeIcon : 'glyphicon glyphicon-globe',
				emptyIcon : '', // 没有子节点的节点图标
				multiSelect : false, // 多选
				onNodeSelected : function(event, data) {
					// Your logic goes here
					console.log("onNodeSelected");
					// temp = JSON.stringify(data);
					console.log(" data.level" + data.level);
					if (data.level == 6) {
						console.log("select data is :" + data.href);
						globalParams['nodeid'] = data.href;
						globalParams['devId'] = '0';
						$('#txt_devid').val("");
						$('#devhisTable').bootstrapTable('refresh');// 刷新Table，Bootstrap
																	// Table
																	// 会自动执行重新查询

					}
				}
			});
			//

		},
		error : function() {
			$('#tree').treeview({
				data : tree, // 数据源
				// showCheckbox: true, //是否显示复选框
				showBorder : false,
				highlightSelected : true, // 是否高亮选中
				// nodeIcon: 'glyphicon glyphicon-user', //节点上的图标
				nodeIcon : 'glyphicon glyphicon-globe',
				emptyIcon : '', // 没有子节点的节点图标
				multiSelect : false, // 多选
				onNodeChecked : function(event, data) {
					// alert(data.nodeId);
				},
				onNodeSelected : function(event, data) {
					// alert(data.nodeId);
				}
			});
		}
	});
	// 5级以下展开
	// $('#tree').treeview('expandNode', [2, { levels: 2, silent: true } ]);
	// 获取展开的nodeid

})

$(function() {
	$('#devhisTable').bootstrapTable('destroy');
	$('#devhisTable').bootstrapTable({
		method : 'get',
		url : BASE_URL + '/geohistorydata/findAll',
		cache : false,
		dataType : "json", // 数据类型
		// height: 400,
		striped : true,
		pagination : true,
		pageSize : 20,
		pageNumber : 1,
		pageList : [ 5, 10, 20, 50, 100, 200, 500 ],
		search : false,
		showColumns : true,
		showRefresh : true,
		showExport : false,
		// rows: 'rows',
		exportTypes : [ 'csv', 'txt', 'xml' ],
		queryParams : queryParams,
		responseHandler : responseHandler,
		clickToSelect : true,
		sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
		dataField : "rows", // 后端 json 对应的表格数据 key

		columns : [ {
			field : "id",
			title : "序号",
			align : "center",
			valign : "middle",
			sortable : "true"
		},
		// {field:"devpostion",title:"泊位号",align:"center",valign:"middle",sortable:"true"},
		// {field:"devarea",title:"区域号",align:"center",valign:"middle",sortable:"true"},
		{
			field : "imei",
			title : "设备号",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "devid",
			title : "设备ID",
			align : "center",
			valign : "middle",
			sortable : "true",
			visible : false
		},
		// {field:"devid",title:"设备ID",align:"center",valign:"middle",sortable:"true"},
		{
			field : "initx",
			title : "初始x值",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "inity",
			title : "初始y值",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "initz",
			title : "初始z值",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "curx",
			title : "环境x值",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "cury",
			title : "环境y值",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "curz",
			title : "环境z值",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "carinfo",
			title : "泊位状态",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "rtc",
			title : "时间",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, {
			field : "pairid",
			title : "配对号",
			align : "center",
			valign : "middle",
			sortable : "true"
		}, ],

	});
});

function queryParams(params) {
	var param = {};
	param['devid'] = globalParams['devId'];// 默认为devid=0
	// param[$(this).attr('name')] = globalParams['devid'];//默认为devid=0
	$('#query-form').find('[name]').each(function() {
		var value_1 = $(this).val();
		var value = $.trim(value_1);
		
		console.log(value);
		if (value != '') {
			console.log("这里的设备号："+value);
			param[$(this).attr('name')] = value;
		}
	});
	$('#datetimepicker1').find('[name]').each(function() {
		var value = $(this).val();

		if (value != '') {
			console.log(value);
			param[$(this).attr('name')] = value;
		}
	});
	$('#datetimepicker2').find('[name]').each(function() {
		var value = $(this).val();

		if (value != '') {
			console.log("the stoptime is :", value);
			param[$(this).attr('name')] = value;
		}
	});
	param['pageSize'] = params.limit; // 页面大小

	if (search_flag == true) {
		param['pageNumber'] = 1
		search_flag = false;
	} else {
		param['pageNumber'] = params.offset/params.limit + 1;      // 页码
	}

	param['nodeid'] = globalParams['nodeid'];
	console.log('params:' + params.offset + '节点编号' + globalParams['nodeid']);
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

function resetSearch() {
	$('#query-form').find('[name]').each(function() {
		$(this).val('');
	});
}

// 设备号查询
function customSearch() {

	
	search_flag = true;
	
	console.log("这里那里");

	var value_1 = $("#txt_devid").val();
	var value = $.trim(value_1);
	
	console.log("搜索函数已经进来了！ value 's length =" + value.length);
	
	if (value != '') {
		// console.log(value);
		globalParams['devId'] = value;

		$('#tree').treeview('clearSearch');
		// 通过设备号查找节点

		$.ajax({
			type : "get",
			url : BASE_URL + "/orgnization/searchnode",
			dataType : "json",
			data : {
				'devId' : globalParams['devId']
			},
			success : function(result) {
				// 树形菜单初始化
				console.log("search nodeid is :" + result);
				if (result == '') {
					alert("搜索结果为空！");
				} else {
					globalParams['nodeid'] = result.nodeid;
					var searchNodes = $('#tree').treeview('search',
							[ result.nodename, {
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
	} else {

		globalParams['devId'] = '0';
	}

//	console.log("传递的参数为：" + globalParams['devId']);
	
	$('#devhisTable').bootstrapTable('refresh');// 刷新Table，Bootstrap Table
												// 会自动执行重新查询
	// dataInit();
}

// 查找组织机构
function orgSearch() {
	dataInit();
	var nodeId;
	var orgnization = $("#orgsearch").val();
	console.log("orgnization is :" + orgnization);
	var searchNodes = $('#tree').treeview('search', [ orgnization, {
		ignoreCase : true, // case insensitive
		exactMatch : false, // like or equals
		revealResults : true, // reveal matching nodes
	} ]);
	console.log("searchnodes:" + searchNodes);
	$('#tree').treeview('expandNode', [ searchNodes, {
		levels : 2,
		silent : true
	} ]);
	// if(searchNodes.length==1){
	// nodeId = searchNodes[0].nodeId;
	// $("#treeview").treeview('selectNode',[nodeId,{silent:true}]);
	// }else{
	// $("#treeview").treeview('selectNode',[0,{silent:true}]);
	// }
	if (searchNodes[0].level == 6) {
		nodeId = searchNodes[0].href;
		globalParams['nodeid'] = nodeId;
		$('#devhisTable').bootstrapTable('refresh'); // 控件刷新

	}
}

// 响应回车按键，进行组织机构检索
document.onkeydown = function(e) {
	var theEvent = window.event || e;
	var code = theEvent.keyCode || theEvent.which;
	if (code == 13) {
		console.log("组织机构检索    回车键开启响应");
		orgSearch();
	}
}