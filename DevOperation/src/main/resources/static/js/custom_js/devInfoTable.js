
document.write ("<script src='js/custom_js/utils/func.js'></script>");
var rowsdata = {};
var globalParams = {};
var totalpages = 1;
var curtpage = 1;
var tree;


function ModalShow(){
	
	console.log("modalshow!");
    $("#modalTitle").text("添加车位");
    $('#formModal').modal('show');
    
}

// 关闭模态框时重置表单
$('#formModal').on('hidden.bs.modal', function() { 
	
	console.log("关闭模态框时清空");
	$("#txt_id").val("");
    $("#txt_operator").val("");
    $("#txt_protocol").val("");     
    $("#txt_tcc").val("");  
    $("#txt_regcode").val(""); 
    $("#txt_berth").val(""); 
    $("#txt_imei").val(""); 
    $("#txt_imsi").val(""); 
    
});  
function dataInit(){
	tree =[{"text":"中心","href":"0"}];
	
	globalParams['nodeid'] = '1';
	globalParams['devid'] = '0';
	globalParams['pageSize'] = 18;
	globalParams['pageNumber'] = 1;
}
$(function(){
	 dataInit();
})


$(function () {
     $.ajax({
         type: "Post",
         url: BASE_URL+"/orgnization/inittree",
         dataType: "json",
         success: function (result) {
        	// 树形菜单初始化
             $('#tree').treeview({
                 data: result,         
                 // showCheckbox: true,
//                 highlightSelected: true,    
                 // nodeIcon: 'glyphicon glyphicon-user', //节点上的图标
                 nodeIcon: 'glyphicon glyphicon-globe',
                 emptyIcon: '',    		// 没有子节点的节点图标
                 multiSelect: false,    // 多选
                 
//                 onNodeChecked: function(event, data){
//            	 alert("取消选中");
//             }
                 
                 onNodeSelected: function(event, data) {        //用户选择停车场时
                	    // Your logic goes here


                	 /**********************************/
                		var value_id;   //设备号
                	    $('#query-form').find('[name]').each(function () {
                	    	value_id = $(this).val();
                	        // console.log(value);
                	        if (value_id != '') {
                	        	globalParams['devid'] = value_id;
                	        }else{
                	        	globalParams['devid'] = '0';
                	        }
                	    });
                	/******************************/
                	 
                	console.log("onNodeSelected");
                	// temp = JSON.stringify(data);
                	console.log(" data.level = " + data.level);
                	if( data.level == 6){
                		console.log("select data is :" + data.href);
                		globalParams['nodeid'] = data.href;
                		changeAllChannelRealTime();
                		
                		// paginatorInit();
                	}
                }
             
             
             

             
             
             });  
         },
         error: function () {
                  $('#tree').treeview({
                 data: tree,         // 数据源
                 // showCheckbox: true, //是否显示复选框
                 showBorder: false,
                 highlightSelected: true,    // 是否高亮选中
                 // nodeIcon: 'glyphicon glyphicon-user', //节点上的图标
                 nodeIcon: 'glyphicon glyphicon-globe',
                 emptyIcon: '',    // 没有子节点的节点图标
                 multiSelect: false,    // 多选
                 onNodeChecked: function (event,data) {
                     // alert(data.nodeId);
                 },
                 onNodeSelected: function (event, data) {
                     // alert(data.nodeId);
                 }
             }); 
         }
     });
     // 5级以下展开
     // $('#tree').treeview('expandNode', [2, { levels: 2, silent: true } ]);
     // 获取展开的nodeid
        
 })



 // 请求默认的停车信息
// $(function(){
//	
// changeAllChannelRealTime();
// });

function changeAllChannelRealTime() {
	
	$.ajax({

        type: "get",
        async: true,
        // url不用说了吧，否则不知道向服务器哪个接口发送并请求
        url: BASE_URL+"/info/findInfo",
        data: globalParams ,
        dataType: 'json',
        // 超时时间
        timeout:30000,
        success: function(msg) {
        	
        	// console.log( "msg:" + msg);
     		   var html =[];
// var jsonstr = JSON.stringify(msg.rows);
              for (var j = 0; j < msg.rows.length; j++) {
            	  
          		  // console.log( "enter into ") ;
            	  // if(!Compare(rowsdata[j], msg.rows[j])){
            	  // changeData(j, msg);
            	  // rowsdata[j].carinfo = msg.rows[j]['carinfo'];
            	  // rowsdata[j].power = msg.rows[j]['power'];
            	  // rowsdata[j].rtc = msg.rows[j]['rtc'];
            	  // rowsdata[j].pairid = msg.rows[j]['pairid'];
            	  // rowsdata[j].onlinestat = msg.rows[j]['onlinestat'];
            	   // var item = msg.msg.row[j];
          		
            		   html.push(renderItem(msg.rows[j]));
            	// }
            	  
              }
// var temp = '<div class="col-lg-4"><div class="info-box"><a class="btn
// btn-app>"</div><div>';
// html.push(temp);
              if(msg.total != 0){
            	  totalpages = (msg.total%globalParams['pageSize'] == 0)?(msg.total/globalParams['pageSize']):(msg.total/globalParams['pageSize'] + 1);
                  console.log("totalpages :" + totalpages);
                  curtpage = globalParams['pageNumber'];
                  paginatorInit();
              }else{
            	  totalpages = 1;
                  console.log("totalpages :" + totalpages);
                  curtpage = globalParams['pageNumber'];
                  paginatorInit();
              }
            
             // $('#bp-element').bootstrapPaginator("setOptions",options)
             
          $('#devinfoshow').html(html.join(''));
         
    },
    error: function () {
    	clearInterval(int);
    	toastr.error("更新数据出错！");  	
    	// alert('网络断开！');
    }
});
}

function renderItem(item){
	
	// var total;
	var str = '<div class="col-lg-4"><div class="info-box">';
	console.log( "item.carinfo:" + item.carinfo) ;
	if(item.carinfo == "1"){
		// console.log( "item.carinfo:" + item.carinfo) ;
		str += '<span class="info-box-icon  bg-grey"><i class="fa fa-automobile"></i></span>' ;
	}else if(item.carinfo == "2"){
		str += '<span class="info-box-icon  bg-aqua"><i class="fa fa-automobile"></i></span>' ;
	}		
	    str += '<div class="info-box-content"> <span class="info-box-text"> <b>ID</b>:';  
	    str += item.imei;
	            	
	    str +='        		</span><span class="info-box-text"><b>POS</b>:';
	    str +=item.devpostion;
	    str +='</span><font size="1"><b>电池</b></font>';
	 if(item.power == "01"){
		str += '<img src="img/battery_empty_16.png"  alt="图片" class="img-circle" style="width:auto;height:19px;">';
	 }else if(item.power =="02"){
		str += '<img src="img/battery_full_16.png"  alt="图片" class="img-circle" style="width:auto;height:19px;"> '; 
	 }else{
		str += '<img src="img/clear.png"  alt="图片" class="img-circle" style="width:auto;height:19px;">' ;
	 }
	            	             	   
	 str += ' <font size="1"><b>网络</b></font>';
	 if(item.onlinestat == "0"){
		 str += '<img src="img/disconnected.png"  alt="图片" class="img-circle" style="width:auto;height:19px;"> ';
	 }else if(item.onlinestat == "1"){
		 str += '<img src="img/connect.png"  alt="图片" class="img-circle" style="width:auto;height:19px;">  ';
	 }else{
		 str += ' <img src="img/clear.png"  alt="图片" class="img-circle" style="width:auto;height:19px;"> ';
	 }
	  str += '<span class="info-box-text">';   
	  str += item.rtc;
      str += '</span>';
	             
	   str += '         </div>    </div>	          </div>';
	   // console.log("html content is :"+ str);
	   return str;
}
//
var int=setInterval(function() {
    
	changeAllChannelRealTime();
	
}, 20000);

$(function(){
	paginatorInit();
})

function paginatorInit(){
$('#bp-element').bootstrapPaginator({
		
		currentPage: curtpage,// 当前的请求页面。
		totalPages: totalpages,
// totalPages: 5,
		size:"normal",
		bootstrapMajorVersion: 3,
		alignment:"left",
		numberOfPages:3,
		itemTexts: function (type, page, current) {
		    switch (type) {
		    case "first": return "首页";
		    case "prev": return "<";
		    case "next": return ">";
		    case "last": return "末页";
		    case "page": return page;
		    }
		},
		onPageClicked: function (event, originalEvent, type, page){
			
			globalParams['pageNumber'] = page;
			globalParams['pageSize'] = 18;
			changeAllChannelRealTime();
		}
});
}

// function queryParams(params) {
// var param = {};
// //设备号等
// $('#query-form').find('[name]').each(function () {
// var value = $(this).val();
// //console.log(value);
// if (value != '') {
// //console.log(value);
// param[$(this).attr('name')] = value;
// }
// });
// param['nodeId'] = params.nodeId;
// param['pageSize'] = params.pageSize; //页面大小
// param['pageNumber'] = params.pageNumber; //页码
// //console.log(param);
// //globalParams = param;
// return param;
// }

// 设备号查询
function customSearch() {
	
	var value_id;   //设备号
    $('#query-form').find('[name]').each(function () {
    	value_id = $(this).val();
        // console.log(value);
        if (value_id != '') {
        	// console.log(value);
        	globalParams['devid'] = value_id;
        }
    });
    
    if(value_id == '')
    {
    	alert("搜索设备号不能为空!");
    	return;
    }
    
    $('#tree').treeview('clearSearch');
   // 通过设备号查找节点
    $.ajax({
        type: "get",
        url: BASE_URL+"/orgnization/searchnode",
        data:{'devId':globalParams['devid']},
        dataType: "json",
        success: function (result) {
       	// 树形菜单初始化
          console.log("search nodename is :"  + result.nodename);
          if(result == ''){
        	  alert("搜索结果为空！");
          }else{
        	  globalParams['nodeid'] = result.nodeid;
        	  var searchNodes = $('#tree').treeview('search', [result.nodename, {
        		  ignoreCase: true,     // case insensitive
        		  exactMatch: false,    // like or equals
        		  revealResults: true,  // reveal matching nodes
        		}]);
        	  console.log("searchnodes:" + searchNodes);
        	  $('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true } ]);
          }
          
        }
        
    });
    changeAllChannelRealTime();
    // dataInit();
}

function orgSearch(){
	dataInit();
	var nodeId;
	var orgnization = $("#orgsearch").val();
	
	
	//判断设备号 是否为空
	var value_id;   //设备号
    $('#query-form').find('[name]').each(function () {
    	value_id = $(this).val();
        // console.log(value);
        if (value_id != '') {
        	globalParams['devid'] = value_id;
        }else{
        	globalParams['devid'] = '0';
        }
    });
	
	
	console.log("orgnization is :" + orgnization);
	var searchNodes = $('#tree').treeview('search', [orgnization, {
		  ignoreCase: true,     // case insensitive
		  exactMatch: false,    // like or equals
		  revealResults: true,  // reveal matching nodes
		}]);
	
	
// console.log("searchnodes:" + searchNodes + "searchNodes[0].level =
// "+searchNodes[0].level);
	
	console.log("節點的節點編號->：" + searchNodes[0].href);
	nodeId = searchNodes[0].href;
	
	$('#tree').treeview('expandNode', [ searchNodes, { levels: 2, silent: true } ]);
// $('#tree').treeview('checkNode', [ searchNodes, { silent: true } ]);
	
// if(searchNodes.length==1){
// nodeId = searchNodes[0].nodeId;
// $("#treeview").treeview('selectNode',[nodeId,{silent:true}]);
// }else{
// $("#treeview").treeview('selectNode',[0,{silent:true}]);
// }
	
	if(searchNodes[0].level == 6){
		globalParams['nodeid'] = nodeId;
		changeAllChannelRealTime();
	}

}



// 响应回车按键，进行组织机构检索
document.onkeydown = function(e)
{
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) 
    {
       	console.log("组织机构检索    回车键开启响应");
      	orgSearch();
    }
}


	



