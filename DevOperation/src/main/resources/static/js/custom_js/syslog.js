var globalParams  = {};
var $table = $('#reportTable');


$(function () {
		   
	    dataInit();
	    //1.初始化Table
	    warnTableInit();
//	
	    operationTableInit();
	    

	    dateEditInit();
	    initTreeView();
	});
	
	
//----------------------组织机构初始化-------------------------------------
	
	function dataInit(){

		globalParams['nodeid'] = '7';
		globalParams['devid'] = '0';
	}
	
//******************日历控件的初始化	
	function dateEditInit() {
		var picker1 = $('#datetimepicker1')
				.datetimepicker(
						{
							format : 'YYYY-MM-DD HH:mm',
							locale : moment
									.locale('zh-cn'),
							defaultDate : "2018-03-14 00:00",
							sideBySide : true,
							showClear : true,
						});
		var picker2 = $('#datetimepicker2')
				.datetimepicker(
						{
							format : 'YYYY-MM-DD HH:mm',
							locale : moment
									.locale('zh-cn'),
							defaultDate : "2018-03-15 00:00",
							sideBySide : true,
							showClear : true,
						});
		
		var picker3 = $('#datetimepicker3')
		.datetimepicker(
				{
					format : 'YYYY-MM-DD HH:mm',
					locale : moment
							.locale('zh-cn'),
					defaultDate : "2018-03-14 00:00",
					sideBySide : true,
					showClear : true,
				});
		
		var picker4 = $('#datetimepicker4')
		.datetimepicker(
				{
					format : 'YYYY-MM-DD HH:mm',
					locale : moment
							.locale('zh-cn'),
					defaultDate : "2018-03-15 00:00",
					sideBySide : true,
					showClear : true,
				});
		
		
		//动态设置最小值  
		picker1.on('dp.change', function(e) {
			picker2.data('DateTimePicker').minDate(
					e.date);
		});
		//动态设置最大值  
		picker2.on('dp.change', function(e) {
			picker1.data('DateTimePicker').maxDate(
					e.date);
		});
		picker3.on('dp.change', function(e) {
			picker2.data('DateTimePicker').minDate(
					e.date);
		});
		picker4.on('dp.change', function(e) {
			picker1.data('DateTimePicker').maxDate(
					e.date);
		});
	}


	
//*****************************树状图初始化
		
	
	//组织机构检索查询
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
		console.log("searchnodes:" + searchNodes);
		$('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true } ]);
//		if(searchNodes.length==1){
//			nodeId = searchNodes[0].nodeId;
//			$("#treeview").treeview('selectNode',[nodeId,{silent:true}]);
//		}else{
//			$("#treeview").treeview('selectNode',[0,{silent:true}]);
//		}
		if(searchNodes[0].level == 6){
			globalParams['nodeid'] = nodeId;
//			changeAllChannelRealTime();
		}

	}
	

	
	
	/////////////////////////	
	function initTreeView(){
		 $.ajax({
		        type: "Post",
		        url: BASE_URL + "/orgnization/inittree",
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

		               	console.log(" data.level" + data.level);
		               	$('#addberthbtn').prop('disable',true);
		               	if( data.level == 6){
		               		console.log("select data is :" + data.href);
		               		
		               		globalParams['nodeid'] = data.href;
//		               		globalParams['devid'] = '0';
		               		$('#txt_devid').val("");
		               		$table.bootstrapTable('refresh');//告警日志执行重新查询
		               		$("#operation_reportTable").bootstrapTable('refresh');//操作日志执行重新查询
		               		$('#addberthbtn').prop('disable',false);
		               	}
		               }
		            });
		            //
		            
		        },
		        error: function () {
		        	$('#addberthbtn').prop('disable',true);
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
	}

	
	
	//--------------------告警日志查询---------------------------------------
	   
	 function warnTableInit() {
	        $('#reportTable').bootstrapTable({

				dataType : "json",  // 数据类型
				uniqueId: "loggid",//每行的唯一标识 
	        	
				pagination: true, //分页
		        pageNumber: 1,//如果设置了分页，首页页码		      
	        	url:'WarnLogServlet',
	            method: 'get',                      //请求方式（*）
	            toolbar: '#toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	           
	            sortable: false,                     //是否启用排序
	            //sortOrder: "asc",                   //排序方式
//	            queryParams: oTableInit.queryParams,//传递参数（*）
	            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber:1,                       //初始化加载第一页，默认第一页
	            pageSize: 5,                       //每页的记录行数（*）
	            pageList: [5],        //可供选择的每页的行数（*）
	            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	            strictSearch: false,
	            showColumns: true,                  //是否显示所有的列
	            showRefresh: true,                  //是否显示刷新按钮
	            //minimumCountColumns: 2,             //最少允许的列数
	            clickToSelect: true,                //是否启用点击选中行
//	            height: 15,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	            //showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
	            responseHandler: responseHandler,
	            queryParams: queryParams,//传递参数（*）
	            dataField: "rows", //后端 json 对应的表格数据 key
	            columns: [
	            
				{field:"loggid",title:"行号",align:"center",valign:"middle",sortable:"false",visible:"false"},
				//{field:"devid",title:"设备号",align:"center",valign:"middle",sortable:"false"},
				{field:"type",title:"告警类型",align:"center",valign:"middle",sortable:"false"},
				{field:"gjnr",title:"告警内容",align:"center",valign:"middle",sortable:"false"},
				{field:"czr",title:"管理人员",align:"center",valign:"middle",sortable:"false"},
				{field:"gjsj",title:"告警时间",align:"center",valign:"middle",sortable:"false"},
				{field:"bz",title:"备注",align:"center",valign:"middle",sortable:"false"},	            
	            ],
	            
	            
	    	    onLoadSuccess: function () {
	                //加载成功时执行
	                console.log("加载成功!");
	               
	            }, 
	            onLoadError: function () {
	                //加载失败时执行
	                 console.log("加载失败!");
	              
	            }, 
	            formatLoadingMessage: function () {
	                //正在加载
	                return "请稍等，正在加载中...";
	            },
	            formatNoMatches: function () {
	                //没有匹配的结果
	                return '没有查找到无符合条件的记录';
	            }
	        
	        });
	    }
	

	function queryParams(params) {
		var param = {};

		 $('#datetimepicker1').find('[name]').each(function () {
		        var value = $(this).val();
		         
		        if (value != '') {
//		        	console.log("查询的开始时间"+value);
		            param['starttime'] = value;
		        }
		    });
		     $('#datetimepicker2').find('[name]').each(function() {
		        var value = $(this).val();
		         
		        if (value != '') {
//		        	console.log("查询的结束时间：",value);
		        	param['stoptime'] = value;
		        }
		    });
		
		param['devid'] = globalParams['devid'];
	    param['pageSize'] = params.limit;   //页面大小， 找多少条
	    param['pageNumber'] = params.offset/params.limit + 1;   //页码
	    param['nodeid'] = globalParams['nodeid'];
	    
	    
	    console.log('参数：告警日志的节点号:'+ param['nodeid']);
	    console.log("参数：开始时间"+param['starttime']);
	    console.log("参数：结束时间"+param['stoptime']);
	    return param;
	}
	
	//请求成功方法
	function responseHandler(result){
		console.log("result.total:", result.total);
		console.log("result.rows:", result.rows);

	    return {
	        total : result.total, //总页数,前面的key必须为"total"
	        rows : result.rows //行数据，前面的key要与之前设置的dataField的值一致.
	    };
	}
	
	
	
	
//---------------------操作日志查询----------------------------------------

	 
	   function  operationTableInit() {
	        $('#operation_reportTable').bootstrapTable({
				dataType : "json",  // 数据类型
				uniqueId: "loggid",//每行的唯一标识 
	        	
				pagination: true, //分页
		        pageNumber: 1,//如果设置了分页，首页页码
//		        paginationPreText: '‹',//指定分页条中上一页按钮的图标或文字,这里是<
//		        paginationNextText: '›',//指定分页条中下一页按钮的图标或文字,这里是>
	        	url:'OperationLogServlet',
	            method: 'get',                      //请求方式（*）
	            toolbar: '#toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	           
	            sortable: false,                     //是否启用排序
	            sortOrder: "asc",                   //排序方式
//	            queryParams: oTableInit.queryParams,//传递参数（*）
	            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber:1,                       //初始化加载第一页，默认第一页
	            pageSize: 5,                       //每页的记录行数（*）
	            pageList: [5],        //可供选择的每页的行数（*）
	            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	            strictSearch: false,
	            showColumns: true,                  //是否显示所有的列
	            showRefresh: true,                  //是否显示刷新按钮
	            minimumCountColumns: 2,             //最少允许的列数
	            clickToSelect: true,                //是否启用点击选中行
//	            height: 15,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
	            cardView: false,                    //是否显示详细视图
	            detailView: false,                   //是否显示父子表
	            responseHandler: responseHandler2,
	            queryParams: queryParams2,//传递参数（*）
	            dataField: "rows", //后端 json 对应的表格数据 key
	            columns: [
	            
				{field:"lOGGID",title:"行号",align:"center",valign:"middle",sortable:"false"},
				{field:"yWLX",title:"业务类型",align:"center",valign:"middle",sortable:"false"},
				{field:"cZLX",title:"操作类型",align:"center",valign:"middle",sortable:"false"},
				{field:"cZDX",title:"操作对象",align:"center",valign:"middle",sortable:"false"},
				{field:"cZNR",title:"操作内容",align:"center",valign:"middle",sortable:"false"},
				{field:"cZR",title:"操作人",align:"center",valign:"middle",sortable:"false"},
				{field:"cZSJ",title:"操作时间",align:"center",valign:"middle",sortable:"false"},
				{field:"bZ",title:"备注",align:"center",valign:"middle",sortable:"false"},
			            
	            ],
	            
	            
	    	    onLoadSuccess: function () {
	                //加载成功时执行
	                console.log("操作日志加载成功!");
	               
	            }, 
	            onLoadError: function () {
	                //加载失败时执行
	                 console.log("操作日志加载失败!");
	              
	            }, 
	            formatLoadingMessage: function () {
	                //正在加载
	                return "操作日志请稍等，正在加载中...";
	            },
	            formatNoMatches: function () {
	                //没有匹配的结果
	                return '操作日志没有查找到无符合条件的记录';
	            }
	        
	        });
	   
	
	  
	}
	
	function queryParams2(params) {
		var param = {};
		
		 $('#datetimepicker3').find('[name]').each(function () {
		        var value = $(this).val();
		         
		        if (value != '') {
//		        	console.log("操作日志查询的开始时间"+value);
		            param['starttime'] = value;
		        }
		    });
		     $('#datetimepicker4').find('[name]').each(function() {
		        var value = $(this).val();
		         
		        if (value != '') {
//		        	console.log("操作日志查询的结束时间：",value);
		        	param['stoptime'] = value;
		        }
		    });
		
	 
	    param['pageSize'] = params.limit;   //页面大小， 找多少条
	    param['pageNumber'] = params.offset/params.limit + 1;   //页码
	    param['nodeid'] = globalParams['nodeid'];
	    param['devid'] = globalParams['devid'];
	    console.log("操作日志查询时发送的参数"+param);
	    return param;
	}
	
	//请求成功方法
	function responseHandler2(result){
		console.log("result.total:", result.total);
		console.log("result.rows:", result.rows);

	    return {
	        total : result.total, //总页数,前面的key必须为"total"
	        rows : result.rows //行数据，前面的key要与之前设置的dataField的值一致.
	    };
	}
	
	
//----------------查询-----------------------
	
//告警日志设备号查询
	function customSearch() {
		
		var value = $("#txt_warn_devid").val();
        if (value != '') {
    	   
    	   globalParams['devid'] = value;
    	   //console.log("输入的设备号为："+value);
        }else {
        	globalParams['devid'] = '0';
	     }  
        
        
//	    $('#tree').treeview('clearSearch');
	    //通过设备号查找节点
	    $.ajax({
	        type: "get",
	        url: BASE_URL+"/orgnization/searchnode",
	        data:{'devId':globalParams['devid']},
	        dataType: "json",
	        success: function (result) {
	       	//树形菜单初始化
	          console.log("search nodename is :"  + result.nodename);
	          if(result == ''){
	        	  alert("搜索结果为空！");
	          }else{
	        	  
	        	  console.log("设备查询时收到的节点号码：" + result.nodeid);
	        	  globalParams['nodeid'] = result.nodeid;
	        	  var searchNodes = $('#tree').treeview('search', [result.nodename, {
	        		  ignoreCase: true,     // case insensitive
	        		  exactMatch: false,    // like or equals
	        		  revealResults: true,  // reveal matching nodes
	        		}]);
	        	  console.log("searchnodes:" + searchNodes);
	        	  $('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true } ]);
	      	      $table.bootstrapTable('refresh');//刷新Table，Bootstrap Table 会自动执行重新查询
	          }
	          
	        }
	        
	    });

    

	}
	
	
	//操作日志的查询函数
    function custom_operation_Search(){
	    console.log("操作日志查询函数已经执行");
	    $("#operation_reportTable").bootstrapTable('refresh');
     }
	
	

	