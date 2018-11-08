
document.write ("<script src='js/custom_js/utils/func.js'></script>");
var rowsdata = {};
var globalParams = {};
var totalpages = 1;
var curtpage = 1;
var tree;

function dataInit(){
	tree =[{"text":"智慧停车管理平台接口配置","href":"0"},{"text":"网络运营商平台接口配置","href":"1","nodes":[{"text":"中国移动物联网平台","href":"2"},{"text":"中国电信物联网平台","href":"3"}]}]; 

	
	globalParams['nodeid'] = '0';
	globalParams['devId'] = '0';
	globalParams['pageSize'] = 1;
	globalParams['pageNumber'] = 1;
}

$(function(){
	 dataInit();
	 treeinit();
})



function treeinit() {  
        	//树形菜单初始化
             $('#tree').treeview({
                 data: tree,         
                 //showCheckbox: true,  
                 highlightSelected: true,    
                 //nodeIcon: 'glyphicon glyphicon-user',    //节点上的图标
                 nodeIcon: 'glyphicon glyphicon-globe', 
                 emptyIcon: '',    //没有子节点的节点图标
                 multiSelect: false,    //多选
                 onNodeSelected: function(event, data) {
                	    // Your logic goes here
                	console.log("onNodeSelected");
                	var html = [];
                	//temp = JSON.stringify(data);
                	if(data.text == "智慧停车管理平台接口配置"){
                		html.push(renderItem());
                	}else if(data.text == "中国移动物联网平台"){
                		html.push(renderItem1());
                	}
                	 $('#syssettingShow').html(html.join(''));
                	
                }
             });
             //   
        
 }

function renderItem(){
	
	//var total;
	var str = '<div class="box box-info">'+
    '<div class="box-header with-border">'+
'<h3 class="box-title">智慧停车管理平台接口配置</h3>'+
'</div>'+
'<!-- /.box-header -->'+
'<!-- form start -->'+
'<form class="form-horizontal" id="companyparaform"> '+
'<div class="box-body"> '+
  '<div class="form-group"> '+
    '<label   class="col-sm-2 control-label">IP地址</label>'+
    '<div class="col-sm-10">'+
      '<input type="text" class="form-control" name="ipadress" id="txt_IPAdress">'+
    '</div>'+
  '</div>'+
  '<div class="form-group">'+
    '<label   class="col-sm-2 control-label">合作方</label>'+
    '<div class="col-sm-10">'+
     '<input type="text" class="form-control" name="partner" id="txt_partner" >'+
   '</div>'+
  '</div>'+
  '<div class="form-group">'+
    '<label   class="col-sm-2 control-label">API版本</label>'+
    '<div class="col-sm-10">'+
      '<input type="text" class="form-control" name="apiversion" id="txt_apiVersion" >'+
    '</div>'+
  '</div>'+	
	'<div class="form-group">'+
    '<label   class="col-sm-2 control-label">签名</label>'+
    '<div class="col-sm-10">'+
     ' <input type="text" class="form-control" name="sign" id="txt_sign" >'+
    '</div>'+
  '</div>'+
' </div>'+
' <!-- /.box-body -->'+
'<div class="box-footer">'+
 ' <button type="submit" onclick="CompanyNetparaSubmit()" class="btn btn-info pull-right">配置</button>'+
'</div>'+
'<!-- /.box-footer -->'+
'</form>'+
'</div>';
	
	   //console.log("html content is :"+ str);
	   return str;
}
function renderItem1(){
	
	//var total;
	
		var str = '<div class=" box box-info">'+
	    '<div class="box-header with-border">'+
	'<h3 class="box-title">BG36</h3>'+
	'</div>'+
	'<!-- /.box-header -->'+
	'<!-- form start -->'+
	'<form class="form-horizontal" id="onenetparaform"> '+
	'<div class="box-body"> '+
	  '<div class="form-group"> '+
	    '<label   class="col-sm-2 control-label">IP地址</label>'+
	    '<div class="col-sm-10">'+
	      '<input type="text" class="form-control" name="bg36ipadress" id="txt_IPAdress">'+
	    '</div>'+
	  '</div>'+
	  '<div class="form-group">'+
	    '<label   class="col-sm-2 control-label">MasterKey</label>'+
	    '<div class="col-sm-10">'+
	     '<input type="text" class="form-control" name="bg36masterkey" id="txt_maskerkey" >'+
	   '</div>'+
	  '</div>'+
	  '<div class="form-group">'+
	    '<label   class="col-sm-2 control-label">注册码</label>'+
	    '<div class="col-sm-10">'+
	      '<input type="text" class="form-control" name="bg36regcode" id="txt_regcode" >'+
	    '</div>'+
	  '</div>'+	
	' </div>'+
	' <!-- /.box-body -->'+
	'<div class="box-footer">'+
	 ' <button type="submit" onclick="OneNetparaSubmit()" class="btn btn-info pull-right">配置</button>'+
	'</div>'+
	'<!-- /.box-footer -->'+
	'</form>'+
	'</div>';
	
	 str += '<div class=" box box-info">'+
	    '<div class="box-header with-border">'+
	'<h3 class="box-title">BC95</h3>'+
	'</div>'+
	'<!-- /.box-header -->'+
	'<!-- form start -->'+
	'<form class="form-horizontal" id="onenetparaform1"> '+
	'<div class="box-body"> '+
	  '<div class="form-group"> '+
	    '<label   class="col-sm-2 control-label">IP地址</label>'+
	    '<div class="col-sm-10">'+
	      '<input type="text" class="form-control" name="bc95ipadress" id="txt_IPAdress1">'+
	    '</div>'+
	  '</div>'+
	  '<div class="form-group">'+
	    '<label   class="col-sm-2 control-label">MasterKey</label>'+
	    '<div class="col-sm-10">'+
	     '<input type="text" class="form-control" name="bc95masterkey" id="txt_maskerkey1" >'+
	   '</div>'+
	  '</div>'+
	  '<div class="form-group">'+
	    '<label   class="col-sm-2 control-label">注册码</label>'+
	    '<div class="col-sm-10">'+
	      '<input type="text" class="form-control" name="bc95regcode" id="txt_regcode1" >'+
	    '</div>'+
	  '</div>'+	
	' </div>'+
	' <!-- /.box-body -->'+
	'<div class="box-footer">'+
	 ' <button type="submit" onclick="OneNetparaSubmit1()" class="btn btn-info pull-right">配置</button>'+
	'</div>'+
	'<!-- /.box-footer -->'+
	'</form>'+
	'</div>';

	
	   //console.log("html content is :"+ str);
	   return str;
}

function renderItem2(){
	
	//var total;
	
		var str = '<div class=" box box-info">'+
	    '<div class="box-header with-border">'+
	'<h3 class="box-title">BC95B5</h3>'+
	'</div>'+
	'<!-- /.box-header -->'+
	'<!-- form start -->'+
	'<form class="form-horizontal" id="oceanparaform"> '+
	'<div class="box-body"> '+
	  '<div class="form-group"> '+
	    '<label   class="col-sm-2 control-label">服务器地址</label>'+
	    '<div class="col-sm-10">'+
	      '<input type="text" class="form-control" name="callbackUrl" id="txt_callbackUrl">'+
	    '</div>'+
	  '</div>'+
	  '<div class="form-group">'+
	    '<label   class="col-sm-2 control-label">产品应用ID</label>'+
	    '<div class="col-sm-10">'+
	     '<input type="text" class="form-control" name="appID" id="txt_appID" >'+
	   '</div>'+
	  '</div>'+
	  '<div class="form-group">'+
	    '<label   class="col-sm-2 control-label">产品密码</label>'+
	    '<div class="col-sm-10">'+
	      '<input type="text" class="form-control" name="secert" id="txt_secert" >'+
	    '</div>'+
	  '</div>'+	
	' </div>'+
	' <!-- /.box-body -->'+
	'<div class="box-footer">'+
	 ' <button type="submit" onclick="OceanparaSubmit()" class="btn btn-info pull-right">配置</button>'+
	'</div>'+
	'<!-- /.box-footer -->'+
	'</form>'+
	'</div>';
	

	   //console.log("html content is :"+ str);
	   return str;
}

function CompanyNetparaSubmit(){
	 var param = $("#companyparaform").serialize();
     console.log('param:'+param);
     //var a = $table.bootstrapTable('getSelections');   
     //param['lastnodeid'] =  a[0].nodeid;
     
		$.ajax({  
	           type: "get", 
	           async:false, 
	           url: BASE_URL+"/syssetting/companynetparasetting",  
	           data:param,
	           dataType: 'json',  
	          error : function() {
	               alert('请求失败！ ');
	           },
	           success: function(data) {  
//	                  console.log("保存返回的数据:"+JSON.stringify(data));
	                 // $('#addorgModal').modal('hide');
	                  if(data['msg']){
	                      toastr.success(data['msg']);
	                  }else{
	                      toastr.error(data['msg']);
	                  }          
	                 // initTable(null);
	                  //initTreeView();
	           }
	     
	        }); 
}

function OneNetparaSubmit(){
	 var param = $("#onenetparaform").serialize();
    console.log('bg36param:'+param);
    //var a = $table.bootstrapTable('getSelections');   
    //param['lastnodeid'] =  a[0].nodeid;
    
		$.ajax({  
	           type: "get", 
	           async:false, 
	           url: BASE_URL+"/syssetting/onenetparasetting",  
	           data:param,
	           dataType: 'json',  
	          error : function() {
	               alert('请求失败！ ');
	           },
	           success: function(data) {  
//	                  console.log("保存返回的数据:"+JSON.stringify(data));
	                  //$('#addorgModal').modal('hide');
	                  if(data['msg']){
	                      toastr.success(data['msg']);
	                  }else{
	                      toastr.error(data['msg']);
	                  }          
	                 // initTable(null);
	                  //initTreeView();
	           }
	     
	        }); 
}

function OneNetparaSubmit1(){
	var param = $("#onenetparaform1").serialize();
    console.log('param:'+param);
    //var a = $table.bootstrapTable('getSelections');   
    //param['lastnodeid'] =  a[0].nodeid;
    
		$.ajax({  
	           type: "get", 
	           async:false, 
	           url: BASE_URL+"/syssetting/onenetparasetting1",  
	           data:param,
	           dataType: 'json',  
	          error : function() {
	               alert('请求失败！ ');
	           },
	           success: function(data) {  
//	                  console.log("保存返回的数据:"+JSON.stringify(data));
	                  //$('#addorgModal').modal('hide');
	                  if(data['msg']){
	                      toastr.success(data['msg']);
	                  }else{
	                      toastr.error(data['msg']);
	                  }          
	                 // initTable(null);
	                  //initTreeView();
	           }
	     
	        }); 
}

function OceanparaSubmit(){
	
	var param = $("#oceanparaform").serialize();
    console.log('param:'+param);
    //var a = $table.bootstrapTable('getSelections');   
    //param['lastnodeid'] =  a[0].nodeid;
    
		$.ajax({  
	           type: "get", 
	           async:false, 
	           url: BASE_URL+"/syssetting/oceanparasetting",  
	           data:param,
	           dataType: 'json',   
	          error : function() {
	               alert('请求失败！ ');
	           },
	           success: function(data) {  
//	                  console.log("保存返回的数据:"+JSON.stringify(data));
	                  //$('#addorgModal').modal('hide');
	                  if(data['msg']){
	                      toastr.success(data['msg']);
	                  }else{
	                      toastr.error(data['msg']);
	                  }          
	                 // initTable(null);
	                  //initTreeView();
	           }
	     
	        }); 
}




