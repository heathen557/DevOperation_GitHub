

//获取选中的id
function getIdSelections() {
    return $.map($table.bootstrapTable('getSelections'), function (row) {
    	//console.log(row);
        return row.rowid
    });
}//打开模态框

//关闭模态框时重置表单
$('#formModal').on('hidden.bs.modal', function() { 
	
	console.log("关闭模态框时清空");
	$("#txt_id").val("");
    $("#txt_devid").val("");
    $("#txt_devarea").val("");     
    $("#txt_tcc").val("");  
    $("#txt_devpostion").val(""); 
    
});  

//保存数据
function submitData(){       
    var param = $("#myform").serialize();
        console.log(param);
          $.ajax({  
           type: "get", 
           async:false, 
           url: BASE_URL + "/params/savePara",  
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
            url: BASE_URL+ "/params/editPara",  
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
                  $('#txt_id').val(data.rowid);
                  $('#txt_deltathup').val(data.deltathup);
                  $('#txt_deltathdown').val(data.deltathdown);
                  $('#txt_initval').val(data.updatinitval);
                  $('#txt_appdownload').val(data.appdownload);
                  $('#txt_version').val(data.version);
                 $("#modalTitle").text("设备参数配置");
                  $('#formModal').modal('show');
            }
          
        });  
    }
}


    
