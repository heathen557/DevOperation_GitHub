 
var globalParams = {};
var tree;

$(function(){
	 dataInit();
})

function dataInit(){
	tree =[{"text":"中心","href":"0"}];
	
	globalParams['nodeid'] = '1';
	globalParams['devId'] = '0';

}

$(function () {
    $.ajax({
        type: "Post",
        url: BASE_URL +"/orgnization/inittree",
        dataType: "json",
        success: function (result) {
       	//树形菜单初始化
            $('#tree').treeview({
                data: result,         
                //showCheckbox: true,  
                highlightSelected: true,    
                //nodeIcon: 'glyphicon glyphicon-user',    //节点上的图标
                nodeIcon: 'glyphicon glyphicon-globe',
                emptyIcon: '',    //没有子节点的节点图标
                multiSelect: false,    //多选
                onNodeSelected: function(event, data) {
               	    // Your logic goes here
               	console.log("onNodeSelected");
               	//temp = JSON.stringify(data);
               	console.log(" data.level" + data.level);
               	if( data.level == 6){
               		console.log("select data is :" + data.href);
               		globalParams['nodeid'] = data.href;
               		globalParams['devId'] = '0';
               		$('#txt_devid').val("");
               		$('#reportTable').bootstrapTable('refresh');//刷新Table，Bootstrap Table 会自动执行重新查询
               	
               	}
               }
            });
            //
            
        },
        error: function () {
                 $('#tree').treeview({
                data: tree,         // 数据源
                //showCheckbox: true,   //是否显示复选框
               	showBorder: false,
                highlightSelected: true,    //是否高亮选中
                //nodeIcon: 'glyphicon glyphicon-user',    //节点上的图标
                nodeIcon: 'glyphicon glyphicon-globe',
                emptyIcon: '',    //没有子节点的节点图标
                multiSelect: false,    //多选
                onNodeChecked: function (event,data) {
                    //alert(data.nodeId);
                },
                onNodeSelected: function (event, data) {
                    //alert(data.nodeId);
                }
            }); 
        }
    });
    //5级以下展开
    //$('#tree').treeview('expandNode', [2, { levels: 2, silent: true } ]);
    //获取展开的nodeid
       
})

var $table = $('#reportTable');
$(function(){ 
    initTable(null);
}); 
function initTable(searchArgs) {
	$table.bootstrapTable('destroy');
	
	
		$table.bootstrapTable({
			method: 'get',
			url : BASE_URL +'/params/findPara',
			cache: false,
			dataType : "json",  // 数据类型
			uniqueId: "rowid",//每行的唯一标识 
			//height: 400,
			toolbar:'#toolbar',
			striped: true,
			pagination: true,
			pageSize: 20,
			pageNumber:1,
			pageList: [5,10, 20, 50, 100, 200, 500],
			search: false,
			showColumns: true,
			showRefresh: true,
			showExport: false,
			exportTypes: ['csv','txt','xml'],
			queryParams: queryParams,
			responseHandler: responseHandler,
			clickToSelect: true,
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*） 
			dataField: 'rows', //后端 json 对应的表格数据 key
			editable:true,//开启编辑模式    
			columns: 
			[
				{
		            field: 'state',
		            checkbox: true,
		            align: 'center',
		            valign: 'middle'
		        }, 
				{field:"rowid",title:"序列号",align:"center",valign:"middle",sortable:"false",visible:false},
				{field:"imei",title:"设备号",align:"center",valign:"middle",sortable:"false"},
				{field:"devpostion",title:"泊位号",align:"center",valign:"middle",sortable:"false"},
				{field:"deltathup",title:"上阈值",align:"center",valign:"middle",sortable:"false",
				 editable: {
		                    type: 'text',
		                    title: '上阈值',
		                    validate: function (v) {
		                        if (!v) return '值不能为空';

		                    }}
				},			
				{field:"deltathdown",title:"下阈值",align:"center",valign:"middle",sortable:"false",
					 editable: {
		                    type: 'text',
		                    title: '下阈值',
		                    validate: function (v) {
		                        if (!v) return '值不能为空';

		                    }}},
				{field:"updatinitval",title:"环境值",align:"center",valign:"middle",sortable:"false",
		                  editable:{
		                	  type: "select",
		                	  title:"环境值更新",
		                	  source:[{value:"0",text:"不更新"},{value:"1",text:"更新"}]
		                  }  	
				},
				{
				       field: 'operate',
				       title: '状态',
				       align: 'center',
				       events: "operateEvents",
				       formatter: operateFormatter
				}
				
			],
			onEditableSave: function (field, row, oldValue, $el) {
				
                $.ajax({
                    type: "get",
                    url:BASE_URL + "/params/editPara", 
                    data: row,
//                    dataType: 'json',
//                    crossDomain: true,
//                    jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
                    success: function (data, status) {
                    	console.log("参数提交成功！");
                        
                        toastr.info("提交成功！");	
                      
                    },
                    error: function () {
                        alert('提交失败');
                    },
                    complete: function () {

                    }

                });
            },
			 onLoadSuccess: function () {
            //加载成功时执行
            console.log("加载成功!");
        }, onLoadError: function () {
            //加载失败时执行
             console.log("加载失败!");
          
        }, formatLoadingMessage: function () {
            //正在加载
            return "请稍等，正在加载中...";
        }, formatNoMatches: function () {
            //没有匹配的结果
            return '无符合条件的记录';
        }
        
		});										
		
}

function operateFormatter(value, row, index) {
    return [
        
        '<span id="RoleOfLoad" aria-hidden="true">未加载</span>'
        
    ].join('');
}

function queryParams(params) {
    var param = {};
    param['devid'] = globalParams['devId'];//默认为devid=0
    $('#query-form').find('[name]').each(function () {
        var value = $(this).val();
        console.log(value);
        if (value != '') {
        	console.log(value);
            param[$(this).attr('name')] = value;
        }
    });
 
    param['pageSize'] = params.limit;   //页面大小
    param['pageNumber'] = params.offset/params.limit +1;   //页码
    param['nodeid'] = globalParams['nodeid'];
    console.log(param);
    return param;
}

//请求成功方法
function responseHandler(result){
	console.log("result.total:", result.total);
	console.log("result.rows:", result.rows);
//    var errcode = result.errcode;//在此做了错误代码的判断
//    
//    if(errcode != 0){
//        alert("错误代码" + errcode);
//        return;
//    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : result.total, //总页数,前面的key必须为"total"
        rows : result.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

//设备号查询
function customSearch() {
    $('#query-form').find('[name]').each(function () {
    	var value = $(this).val();
        //console.log(value);
        if (value != '') {
        	//console.log(value);
        	globalParams['devId'] = value;
        }else {
			alert("设备号不能为空！");
			return;
		}
    });
    $('#tree').treeview('clearSearch');
   //通过设备号查找节点
    $.ajax({
        type: "get",
        url: BASE_URL +"/orgnization/searchnode",
        dataType: "json",
        data:{'devId':globalParams['devId']},
        success: function (result) {
       	//树形菜单初始化
          console.log("search nodeid is :"  + result);
          if(result == ''){
        	  alert("搜索结果为空！");
          }else{
        	  globalParams['nodeid'] = result.nodeid;
        	  var searchNodes = $('#tree').treeview('search', [result.nodename, {
        		  ignoreCase: true,     // case insensitive
        		  exactMatch: false,    // like or equals
        		  revealResults: true,  // reveal matching nodes
        		}]);
        	  $('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true } ]);
          }
          
        }
        
    });
   
    $('#reportTable').bootstrapTable('refresh');//刷新Table，Bootstrap Table 会自动执行重新查询
    //dataInit();
}

//查找组织机构
function orgSearch(){
	dataInit();
	var nodeId;
	var orgnization = $("#orgsearch").val();
	console.log("orgnization is :" + orgnization); 
	var searchNodes = $('#tree').treeview('search', [orgnization, {
		  ignoreCase: true,     // case insensitive
		  exactMatch: false,    // like or equals
		  revealResults: true,  // reveal matching nodes
		}]);
	$('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true } ]);
	console.log("searchnodes:" + searchNodes);
//	if(searchNodes.length==1){
//		nodeId = searchNodes[0].nodeId;
//		$("#treeview").treeview('selectNode',[nodeId,{silent:true}]);
//	}else{
//		$("#treeview").treeview('selectNode',[0,{silent:true}]);
//	}
	if(searchNodes[0].level == 6){
		globalParams['nodeid'] = nodeId;
		
	}

}



