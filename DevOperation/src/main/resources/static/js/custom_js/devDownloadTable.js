var timer;
var globalParams = {};
var rowsdata = {};
var tree;
var $table = $('#reportTable');
var selectids;
var download_flag;
//获取选中的id
function getIdSelections() {
    return $.map($('#reportTable').bootstrapTable('getSelections'), function (row) {
    	console.log(row);
        return row.rowid;
    });
}

function dataInit(){
	tree =[{"text":"中心","href":"0"}];
	
	globalParams['nodeid'] = '1';
	globalParams['devId'] = '0';

}

$(function(){ 
	 dataInit();
     initTable(null);
     //starttimer();
}); 

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
        error: function() {
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

function starttimer(){
	 timer = setInterval(function() {
		    
			changeAllChannelRealTime();
			
		}, 5000);

}

function stoptimer(){
	
	clearInterval(timer);
}

function changeAllChannelRealTime() {
	
$.ajax({

          type: "get",
          async: true,
         
          url:BASE_URL + "/upload/findDownPara",
          data: globalParams ,
          dataType: 'json',
         
          timeout:30000,
          success: function(msg) {
        	//var msg =   JSON.stringify(msg1);
           console.log( "msg1.rows" + msg.rows);
        	  //var msg = JSON.stringify(msg1);// 转成JSON格式
        	  //console.log( "jsonData:" + jsonData);
        	  //var msg = $.parseJSON(jsonData);// 转成JSON对象
           //var msg = $.parseJSON(msg1);// 转成JSON对象
            console.log( "msg.rows.length" + msg.rows.length);
           
            for (var j = 0; j < msg.rows.length; j++) {
          	  
          	  //if(!Compare(rowsdata[j], msg.rows[j])){
            	  console.log('j:'+j);
          		   //console.log('msg.rows[j].status:'+JSON.stringify(msg.rows[j]));
          	   if(msg.rows[j].hasOwnProperty("status")){
//	     		   rowsdata[j].status = msg.rows[j].status;
//	     		   rowsdata[j].progress = msg.rows[j].progress;
          		 
	     		   if(msg.rows[j].status== '4' || msg.rows[j].status == '5'){
	     			   download_flag = 1;
	     		   }else if (msg.rows[j].status == '3'){ 
	     			  download_flag = 0;
	     		   }
	          	   changeData(j, msg);
          	   }
          	//  }
          	  
           }
            
            if (download_flag == 0){
            	stoptimer();
            }
  
      },
      error: function () {
    	  stoptimer();
      	toastr.error("更新数据出错！");	
      	alert('网络断开！');
      }
  });

}
//
function changeData(i,msg){

$table.bootstrapTable('updateRow', {
  //i表示第几行，从0开始
      index: i,
      row: 
      {
          //这里也就是data-field的名称,*表示字段名
    	  status: msg.rows[i].status,
    	  //mode:msg.rows[i].mode,
    	  //updatetime:msg.rows[i].updatetime,
    	  //version:msg.rows[i].version,
    	  progress:msg.rows[i].progress,
    
      }
  });         
}

window.operateEvents = {
		"click #RoleOfDnld":function(e,value,row,index){//下载 
			//模态框显示
			 selectids = row.rowid;
			 $('#formModal').modal('show');
			 $('#RoleOfDnld').attr('disabled',true);
		},
		"click #RoleOfCel":function(e,value,row,index){//取消
			//模态框显示
			 $('#myModal1').modal('show');
			
			 //stoptimer();
		    }
			
			
}

		
function submit1(){
	//ajax请求
	var ids = getIdSelections();
    if (ids.length != 1) {
        toastr.info('仅选择一条数据进行编辑!'); 
        }else {  
        console.log("编辑的id："+ids);
        $.ajax({  
            type: "get",  
            url: BASE_URL +"/upload/uploadcancel",  
            data: {id:ids},
            dataType: 'json',  
            error : function() {
                alert('请求失败！ ');
            },
            success: function(data) { 

                console.log("获取单条数据:"+JSON.stringify(data)); 
                $('#myModal1').modal('hide');
               
            }
          
        }); 
}
}
		
function operateFormatter(value, row, index) {
    return [
        
        '<button type="button" id="RoleOfDnld" class="btn btn-default  btn-sm" style="margin-right:15px;"><span aria-hidden="true">下载</span></button>',
        '<button type="button" id="RoleOfCel" class="btn btn-default  btn-sm" style="margin-right:15px;"><span aria-hidden="true">取消</span></button>'
    ].join('');
}

function initTable(searchArgs) {
	$table.bootstrapTable('destroy');
	
	
		$table.bootstrapTable({
			method: 'get',
			url :BASE_URL + '/upload/findDownPara',
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
				//{field:"rowid",title:"序列号",align:"center",valign:"middle",sortable:"false",visible:false},
		        {field:"imei",title:"设备号",align:"center",valign:"middle",sortable:"false"},
				{field:"devpostion",title:"泊位号",align:"center",valign:"middle",sortable:"false"},
				{field:"status",title:"下载更新状态",align:"center",valign:"middle",sortable:"false",
					formatter: function(value,row,index){
						    if(value == '5'){
								
								return '<span style="color:#0000ff">'+'正在下载'+'</span>';
							}else if(value == '3'){
								 $('#RoleOfDnld').attr('disabled',false);
								return '<span style="color:#FF0000">'+'更新完成'+'</span>';
							}else if(value == '4'){
								
								return '<span style="color:#FF0000">'+'下载完成'+'</span>';
							}
						
					}
				},
				{field:"mode",title:"更新方式",align:"center",valign:"middle",sortable:"false" ,
					formatter: function(value,row,index){
					    if(value == '01'||value == '1'){
					    	//$('#txt_appupdatetime').attr('disabled',true);
						return '<span style="color:#00ff00">'+'自动更新'+'</span>';
						}else if(value == '02'||value == '2'){
							//$('#txt_appupdatetime').attr('disabled',false);
							return '<span style="color:#0000ff">'+'手动更新'+'</span>';
						}else if(value == '03'|| value == '3'){
							//$('#txt_appupdatetime').attr('disabled',false);
							return '<span style="color:#FF0000">'+'默认更新'+'</span>';
						}
					
				}},
				{field:"updatetime",title:"更新时间",align:"center",valign:"middle",sortable:"false"},
				{field:"version",title:"版本号",align:"center",valign:"middle",sortable:"false"},
				{field:"progress",title:"进度(%)",align:"center",valign:"middle",sortable:"false",
					formatter: function(value,row,index){
						//if(value == '1'){
//							return '<img  src="img/car.png" height="20" width="20">';
//							}else {
//								return '无车';
//							}	
							var res = row.progress;
							
					        return ["<div class='progress'> <div class='progress-bar' role='progressbar' aria-valuenow='50' aria-valuemin='0' aria-valuemax='100' style='width:"+res+"%'>"+res+"</div> </div>"];
					}
				},
				{
				       field: 'operate',
				       title: '操作',
				       align: 'center',
				       events: "operateEvents",
				       formatter: operateFormatter
				}
			],
			onEditableSave: function (field, row, oldValue, $el) {
				editdownpara(row);
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

function editdownpara(row){
	 $.ajax({
         type: "get",
         url:BASE_URL + "/upload/editDownPara", 
         data: row,
//         dataType: 'json',
//         crossDomain: true,
//         jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
         success: function (data, status) {
             if (status == "success") {
             	toastr.info("提交成功！");	
             }
         },
         error: function () {
             alert('编辑失败');
         },
         complete: function () {

         }

     });
}
function queryParams(params) {
    var param = {};
    param['devId'] = globalParams['devId'];//默认为devid=0
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
        url:BASE_URL + "/orgnization/searchnode",
        data:{'devId':globalParams['devId']},
        dataType: "json",
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

//提交
function submitData(){
	//获取id
	selectids = getIdSelections().toString();

	 var param = $("#myform").serialize();
	 param+="&ids=";
	 param+=selectids;
	// param['ids'] = ids;
	// console.log('ids:' + ids + ',param:'+ param);
     

		 $.ajax({  
	           type: "get", 
	           async:false, 
	           url:BASE_URL + "/upload/savednldPara",  
	           data:param,
	           dataType: 'json',  	         
	           error : function() {
	               alert('请求失败！ ');
	           },
	           success: function(data) {  
//	                  console.log("保存返回的数据:"+JSON.stringify(data));
	                  $('#formModal').modal('hide');
	                  if(data['msg']){
	                      toastr.success(data['msg']);
	                  }else{
	                      toastr.error(data['msg']);
	                  }          
	                  initTable(null);
	                  starttimer();
	           }
	     
	        }); 
		 
		//
   
}

function uploadAll(){
	
	$('#formModal').modal('show');
}
