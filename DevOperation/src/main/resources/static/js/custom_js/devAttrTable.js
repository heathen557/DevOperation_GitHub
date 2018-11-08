
var $table = $('#reportTable');

var globalParams = {};
var tree;
var updateoraddFlag ;
var currentTcc = '莱斯停车场';
var selectRowid;
//获取选中的id
function getIdSelections() {
    return $.map($table.bootstrapTable('getSelections'), function (row) {
    	console.log(row);
        return row.attrid;
    });
}
function dataInit(){
	tree =[{"text":"中心","href":"0"}];
	
	globalParams['nodeid'] = '1';
	globalParams['devId'] = '0';
	$('#addberthbtn').prop('disable',true);
}

$(function(){
	 dataInit();
	 initTreeView();
	 initTable(null);
	 //initTreeView();
})


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
	               	temp = JSON.stringify(data);
//	               	console.log(" data" + temp);
//	               	
//	               	console.log(" data.level" + data.level);
	               	$('#addberthbtn').attr('disabled',true);
	               	if( data.level == 6){
	               		currentTcc = data.text;
	               		console.log("select data is :" + data.href);
	               		globalParams['nodeid'] = data.href;
	               		globalParams['devId'] = '0';
	               		$('#txt_devid').val("");
	               		$table.bootstrapTable('refresh');//刷新Table，Bootstrap Table 会自动执行重新查询
	               		$('#addberthbtn').attr('disabled',false);
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

//$(function () {
//    $('#query-form1').bootstrapValidator({
//　　　　message: 'This value is not valid',
//        　feedbackIcons: {
//            　　　　　　　　valid: 'glyphicon glyphicon-ok',
//            　　　　　　　　invalid: 'glyphicon glyphicon-remove',
//            　　　　　　　　validating: 'glyphicon glyphicon-refresh'
//        　　　　　　　　   },
//        fields: {
//            sn: {
//                message: '用户名验证失败',
//                validators: {
//                    notEmpty: {
//                        message: 'sn不能为空'
//                    }
//                }
//            },
//            title: {
//                validators: {
//                    notEmpty: {
//                        message: 'title不能为空'
//                    }
//                }
//            }
//        }
//    });
//});

//增加停车位，显示modal
function addBerth(){
	
	
	//显示停车场ajax
//	$.ajax({  
//        type: "get", 
//        async:false, 
//        url: BASE_URL + "/orgnization/findtcc",         
//        dataType: 'json',  
//       error : function() {
//            alert('请求失败！ ');
//        },
//        success: function(data) {  
////               console.log("保存返回的数据:"+JSON.stringify(data));
//        	$("#txt_tcc1").empty();
////        	for (var i = 0; i < data.tcclist.length; i++) {
////		          if(data.tcc != data.tcclist[i]){
////		        	 　$("#txt_tcc1").append("<option value='"+data.tcclist[i]+"'>"+data.tcclist[i]+"</option>");  
////		          }
////		         } 
//        	$("#txt_tcc1").append("<option value='"+data.tcclist[i]+"'>"+data.tcclist[i]+"</option>");
//		         // $('#txt_tcc1').val(data.tcc);
//        	
//        }
//  
//     }); 
	$("#txt_tcc1").empty();
	$("#txt_tcc1").append("<option value='"+currentTcc+"'>"+currentTcc+"</option>");
	$('#BerthModal').modal('show');
}

//增加停车位
function submitData(){
	 var param = $("#myberthform").serialize();
     console.log('param:'+param);

		$.ajax({  
	           type: "get", 
	           async:false, 
	           url: BASE_URL + "/orgnization/addberth",  
	           data:param,
	           dataType: 'json',  
	          error : function() {
	               alert('请求失败！ ');
	           },
	           success: function(data) {  
//	                  console.log("保存返回的数据:"+JSON.stringify(data));
	                  $('#BerthModal').modal('hide');
//	                  if(data['msg']){
//	                      toastr.success(data['msg']);
//	                  }else{
//	                      toastr.error(data['msg']);
//	                  }          
	                  initTable(null);
	           }
	     
	        }); 
	    
}

//编辑
function submitData1(){
	 var param = $("#myberthform1").serialize();
    console.log('param:'+param);

		$.ajax({  
	           type: "get", 
	           async:false, 
	           url: BASE_URL + "/orgnization/editberth",  
	           data:param,
	           dataType: 'json',  
	          error : function() {
	               alert('请求失败！ ');
	           },
	           success: function(data) {  
//	                  console.log("保存返回的数据:"+JSON.stringify(data));
	                  $('#EditBerthModal').modal('hide');
	                  $("#txt_tcc").html("");
	                  if(data['msg']){
	                      toastr.success(data['msg']);
	                  }else{
	                      toastr.error(data['msg']);
	                  }   
	                  
	                  initTable(null);
	           }
	     
	        }); 
	    
}

window.operateEvents = {
		"click #RoleOfCancel":function(e,value,row,index){//删除 
			//模态框显示
			$('#cancelModal').modal('show');
			
		},
		"click #RoleOfEdit":function(e,value,row,index){//编辑
			//模态框显示
			
			//ajax请求
			var ids = row.attrid;
			 console.log("id："+ids);
			if(ids == null){
				alert('选中为空！ ');
				return;
			}
			if(Array.isArray(ids))
			{
				if(ids.length != 1){
					return;
				}
			}
		   
		        console.log("编辑的id："+ids);
		        $.ajax({  
		            type: "get",  
		            url: BASE_URL + "/orgnization/findberth",  
		            data: {id:ids},
		            dataType: 'json',  
		            error : function() {
		                alert('请求失败！ ');
		            },
		            success: function(data) { 
		            	//$("#txt_tcc").value("");
		                console.log("获取单条数据:"+JSON.stringify(data)); 
		                  $('#txt_id').val(data.rows[0].attrid);
		                  $('#txt_devid').val(data.rows[0].devid);
		                  $('#txt_berth').val(data.rows[0].devpostion);
		                  //$("#txt_berth").prepend("<option value='"+data.rows[0].devpostion+"'>"+data.rows[0].devpostion+"</option>");//添加第一个option值
		                  $('#txt_operator').val(data.rows[0].operator);
		                  $('#txt_protocol').val(data.rows[0].protocol);
		                  $('#txt_imei').val(data.rows[0].imei); 
		                  $('#txt_regcode').val(data.rows[0].regcode); 
		                 
		                  $("#txt_tcc").prepend("<option value='"+data.tcc+"'>"+data.tcc+"</option>");//添加第一个option值
				              　　　　for (var i = 0; i < data.tcclist.length; i++) {
				          if(data.tcc != data.tcclist[i]){
				        	 　$("#txt_tcc").append("<option value='"+data.tcclist[i]+"'>"+data.tcclist[i]+"</option>");  
				          }
				         } 
				          $('#txt_tcc').val(data.tcc);
		                  $('#txt_imsi').val(data.rows[0].imsi);
		                 //$("#modalTitle").text("设备参数配置");
		                 $('#EditBerthModal').modal('show');
		            }
		          
		        });  
		    
			
			
		}
}


function cancelDev(){
	// ajax请求
	//获取id
	
        console.log("selectRowid："+selectRowid);
        $.ajax({  
            type: "get",  
            url: BASE_URL + "/orgnization/deletenode",  
            data: {id:selectRowid},
            dataType: 'json',  
            error : function() {
                alert('请求失败！ ');
            },
            success: function(data) { 
                if(data['delmsg']){
                    toastr.success(data['delmsg']);
                }else{
                    toastr.error(data['delmsg']);
                } 
                //重新初始化表格
                initTable(null);
                $('#cancelModal').modal('hide');
            }
          
        });  
  
}

function operateFormatter(value, row, index) {
    return [
        '<div class="btn-group">',
        '<button type="button" id="RoleOfCancel" class="btn btn-default  btn-sm"><span class="fa fa-trash" aria-hidden="true"></span></button>',
        '<button type="button" id="RoleOfEdit" class="btn btn-default  btn-sm" ><span class="fa fa-pencil" aria-hidden="true"></span></button>',
        '</div>'
    ].join('');
}

function initTable(searchArgs) {
	$table.bootstrapTable('destroy');
	
	
		$table.bootstrapTable({
			method: 'get',
			url : BASE_URL + '/attribute/findAll',
			cache: false,
			dataType : "json",  // 数据类型
			uniqueId: "attrid",//每行的唯一标识 
			//height: 400,
			toolbar:'#toolbar',
			striped: true,
			pagination: true,
			pageSize: 20,
			pageNumber:1,
			pageList: [5,10, 20, 50, 100],
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
			//editable:true,//开启编辑模式    
			columns: 
			[
//				{
//		            field: 'state',
//		            checkbox: true,
//		            align: 'center',
//		            valign: 'middle'
//		        }, 
				//{field:"rowid",title:"序列号",align:"center",valign:"middle",sortable:"false",visible:false},
		        {field:"attrid",title:"序列号",align:"center",valign:"middle",sortable:"false" ,visible:false},
				{field:"devid",title:"设备ID",align:"center",valign:"middle",sortable:"false",visible:false},
				{field:"devpostion",title:"泊位号",align:"center",valign:"middle",sortable:"false"},
				{field:"operator",title:"运营商",align:"center",valign:"middle",sortable:"false",visible:false,
					formatter: function(value,row,index){
					    if(value == '01'){
							
							return '移动';
						}else if(value == '02'){
							
							return '电信';
						}
					
				}
				},
				{field:"protocol",title:"协议",align:"center",valign:"middle",sortable:"false",visible:false},
				{field:"imei",title:"设备号",align:"center",valign:"middle",sortable:"false"},
				{field:"devicetype",title:"设备型号",align:"center",valign:"middle",sortable:"false"},
				{field:"imsi",title:"卡号",align:"center",valign:"middle",sortable:"false"},
				{
				       field: 'operate',
				       title: '操作',
				       align: 'center',
				       events: "operateEvents",
				       formatter: operateFormatter
				}
			],
			onClickRow:function(row){
				selectRowid = row.attrid;
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
    param['pageNumber'] = params.offset/params.limit + 1;   //页码
    param['nodeid'] = globalParams['nodeid'];
    console.log('params:'+params.offset);
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

function customSearch(text) {
	
    	var value = $("#IMEI").val();
        //console.log(value);
        if (value != '') {
        	//console.log(value);
        	globalParams['devId'] = value;
        }else {
			alert("设备号不能为空！");
			$('#addberthbtn').attr('disabled',true);
			return;
		}
  
    $('#tree').treeview('clearSearch');
   //通过设备号查找节点
    $.ajax({
        type: "get",
        url: BASE_URL + "/orgnization/searchnode",
        data:{'devId':globalParams['devId']},
        dataType: "json",
        success: function (data) {
       	//树形菜单初始化
        
          console.log("search nodeid is :"  + data.nodename);
          if(data == ''){
        	  alert("搜索结果为空！");
          }else{
        	   globalParams['nodeid'] = data.nodeid;
        	  var searchNodes = $('#tree').treeview('search', [data.nodename, {
        		  ignoreCase: true,     // case insensitive
        		  exactMatch: false,    // like or equals
        		  revealResults: true,  // reveal matching nodes
        		}]);
        	  $('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true } ]);
          }
          
        }
        
    });
   
    $('#addberthbtn').attr('disabled',true);
    $table.bootstrapTable('refresh');//刷新Table，Bootstrap Table 会自动执行重新查询
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

function resetSearch() {
    $('#query-form').find('[name]').each(function () {
        $(this).val('');
    });
}

