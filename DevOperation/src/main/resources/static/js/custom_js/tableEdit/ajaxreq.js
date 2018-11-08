//获取选中的id

function getIdSelections() {
    return $.map($table.bootstrapTable('getSelections'), function (row) {
    	console.log(row);
        return row.attrid
    });
}//打开模态框
function addData(){
	$("#txt_id").val("");
    $("#txt_devid").val("");
    $("#txt_devarea").val("");     
    $("#txt_tcc").val("");  
    $("#txt_devpostion").val(""); 
    $("#txt_imei").val("");  
    $("#txt_devicetype").val(""); 
    $("#modalTitle").text("添加设备");
    $('#formModal').modal('show');
}

//关闭模态框时重置表单
$('#formModal').on('hidden.bs.modal', function() { 
	
	console.log("关闭模态框时清空");
	$("#txt_id").val("");
    $("#txt_devid").val("");
    $("#txt_devarea").val("");     
    $("#txt_tcc").val("");  
    $("#txt_devpostion").val(""); 
    $("#txt_imei").val("");  
    $("#txt_devicetype").val(""); 
    
});  

//保存数据
function submitData(){       
    var param = $("#myform").serialize();
        console.log(param);
          $.ajax({  
           type: "get", 
           async:false, 
           url: BASE_URL+ "/attribute/addattr",  
           data: param ,
           dataType: 'jsonp',  
           jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
           jsonpCallback: "userHandler",  //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
           contentType: "application/x-www-form-urlencoded; charset=utf-8", 
           error : function() {
               alert('请求失败！ ');
           },
           success: function(data) {  
//                  console.log("保存返回的数据:"+JSON.stringify(data));
                  $('#formModal').modal('hide');
                  if(data['msg']){
                      toastr.success(data['msg']);
                  }else{
                      toastr.error(data['msg']);
                  }          
                  initTable(null);
           }
     
        });  
} 


//编辑数据
function editData(){
    var ids = getIdSelections();
    if (ids.length != 1) {
        toastr.info('选择一条数据进行编辑!'); 
    } else {    
        var id= parseInt(ids);
        console.log("编辑的id："+id);
        $.ajax({  
            type: "get",  
            url: BASE_URL+"/attribute/editAttr",  
            data: {id:id},
            dataType: 'jsonp',  
            jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
            jsonpCallback: "userHandler",  //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数
            contentType: "application/x-www-form-urlencoded; charset=utf-8",  
            error : function() {
                alert('请求失败！ ');
            },
            success: function(data) { 
            	
                console.log("获取单条数据:"+JSON.stringify(data)); 
                  $('#txt_id').val(data.attrid);
                  $('#txt_devid').val(data.devid);
                  $('#txt_devarea').val(data.devarea);
                  $('#txt_tcc').val(data.tcc);
                  $('#txt_devpostion').val(data.devpostion);
                  $("#txt_imei").val(data.imei);  
                  $("#txt_devicetype").val(data.devicetype); 
                 $("#modalTitle").text("设备属性编辑");
                  $('#formModal').modal('show');
            }
          
        });  
    }
}

//删除数据
function deleteData(){
    var ids = getIdSelections().toString();
    if (ids.length > 0) {
        console.log("删除的ids："+ids);
         if(confirm("确定要删除选中的数据吗？")){
                $.ajax({  
                      type: "get",  
                      url: BASE_URL + "/attribute/deleteById",  
                      data: {"ids":ids},
                      dataType: 'jsonp', 
                      jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
                      jsonpCallback: "userHandler",  //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理
                      contentType: "application/x-www-form-urlencoded; charset=utf-8",  
                      error : function() {
                          alert('请求失败！ ');
                      },
                      success: function(data) { 
                            console.log("删除返回的数据:"+JSON.stringify(data));
                            if(data['msg']){
                                  toastr.success(data['msg']);
                              }else{
                                  toastr.error(data['msg']);
                              }     
                            initTable(null);
                      }
                    
                      });
         }
      
    } else {
        toastr.warning('选择要删除的数据！');
    }  
    
}

function serializeDo(){
    var param = {};
    $('#query-form1').find('[name]').each(function () {
        var value = $(this).val();
        //console.log(value);
        if (value != '') {
        	//console.log(value);
            param[$(this).attr('name')] = value;
        }
    });
    return param;
}
function registerDevice(){
	//$('#regcode').val(REGCODE);
	var param = serializeDo();
	 //var param = $("#query-form1").serialize();
	 //console.log("query-form1"+param);
        $.ajax({  
            type: "get",  
            url: BASE_URL+"/attribute/registerdevice",  
            data: param,
            dataType: 'jsonp',  
            jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
            jsonpCallback: "userHandler",  //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数
            contentType: "application/x-www-form-urlencoded; charset=utf-8",  
            error : function() {
                alert('设备注册请求失败！ ');
            },
            success: function(data) { 
            	
                console.log("注册设备结果:"+JSON.stringify(data)); 
                if(data['msg']){
                    toastr.success(data['msg']);
                }else{
                    toastr.error(data['msg']);
                }     
            }
          
        });  
    
}