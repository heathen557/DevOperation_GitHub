//获取选中的id
function getIdSelections() {
    return $.map($table.bootstrapTable('getSelections'), function (row) {
    	//console.log(row);
        return row.rowid
    });
}//打开模态框
//function addData(){
//
//    $("#modalTitle").text("设备下载参数设置");
//    $('#formModal').modal('show');
//    
//}

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
     console.log('param:'+param);

		 $.ajax({  
	           type: "get", 
	           async:false, 
	           url: BASE_URL+"/params/savePara",  
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
	           }
	     
	        }); 
   
           
} 


//确认提交
function editData(){
 
	$('#myModal').modal('show');

}

//确认提交
function submit() {
	
	var ids = getIdSelections().toString();
    if (ids.length <= 0) {
        toastr.info('选择一条数据进行编辑!'); 
    } else {       	
        console.log("编辑的id："+ids);
        $.ajax({  
            type: "get",  
            url: BASE_URL+"/params/ParaAppSetting",  
            data: {id:ids},
            dataType: 'json',  
            error : function() {
                alert('请求失败！ ');
            },
            success: function(data) { 
                if(data['paramsg']){
                    toastr.success(data['paramsg']);
                }else{
                    toastr.error(data['paramsg']);
                }          
                initTable(null);
                $('#myModal').modal('hide');
                $('#RoleOfLoad').val('已加载');
//                console.log("获取单条数据:"+JSON.stringify(data)); 
//                  $('#txt_id').val(data.rowid);
//                  $('#txt_deltathup').val(data.deltathup);
//                  $('#txt_deltathdown').val(data.deltathdown);
//                  $('#txt_initval').val(data.updatinitval);
//                  $('#txt_appdownload').val(data.appdownload);
//                  $('#txt_version').val(data.version);
//                 $("#modalTitle").text("设备参数配置");
//                  $('#formModal').modal('show');
            }
          
        });  
    }
}
    
