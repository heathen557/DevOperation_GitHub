//document.write ("<script src='js/custom_js/utils/globalData.js'></script>");

var websocket = null;

//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
	console.log("start shake hands!");
    websocket = new WebSocket(WEBSOCKET_URL);
}
else{
    alert('Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function(){
	alert('网络连接异常！')
    //setMessageInnerHTML("error");
};

//连接成功建立的回调方法
websocket.onopen = function(event){
	console.log("shake hands success!");
    //setMessageInnerHTML("open");
}

//接收到消息的回调方法
websocket.onmessage = function(event){
	//toastr.info();
	var buffer = event.data;
	console.log(buffer);
	if(!isJSON(buffer)){
		return;
	}
	var obj  = $.parseJSON( buffer );
	if(obj.hasOwnProperty("battery")){
		 $('#powerAlert').attr("class","alert-warning");
		 toastr.error("设备电量警告！");
	}else if(obj.hasOwnProperty("Para_cmd")){
		toastr.info("设备参数配置成功！");
	}else if(obj.hasOwnProperty("ds_id")){
		var ds_id = obj['ds_id'];
		var devid = obj['dev_id'];
		
		if(ds_id == "21"){
			if(obj['operation_type'] == "01"){toastr.info("设备： " + devid + "，文件上传完成！");}
			else if(obj['operation_type'] == "02"){toastr.info("设备： " + devid + "，文件更新完成！");}
		}else if(ds_id == "03"){
			toastr.info("设备：" + devid + "，校时完成！");
		}
	}
	
	//如果注册成功，则刷新
	if(obj.hasOwnProperty("regmsg")){
		if(obj['regmsg'] == '0'){
			toastr.info("设备： 注册成功！");
		}else{
			toastr.info("设备： 注册失败！");
		}
		
	}
	if(obj.hasOwnProperty("deldevmsg")){
		if(obj['deldevmsg'] == 'ok'){
			toastr.info("设备删除成功！");
		}else{
			toastr.info("设备删除失败！");
		}
		
	}	
	if(obj.hasOwnProperty("addorgmsg")){
		if(obj['addorgmsg'] == 'ok'){
			toastr.info("机构增加成功！");
		}else{
			toastr.info("机构增加失败！");
		}
		
	}
	if(obj.hasOwnProperty("deleteorg")){
		if(obj['deleteorg'] == 'ok'){
			toastr.info("机构删除成功！");
		}else{
			toastr.info("机构删除失败！");
		}
		
	} 
	if(obj.hasOwnProperty("editorg")){
		if(obj['editorg'] == 'ok'){
			toastr.info("机构修改成功！");
		}else{
			toastr.info("机构修改失败！");
		}
		
	}  
	if(obj.hasOwnProperty("addberth")){
		if(obj['addberth'] == 'ok'){
			toastr.info("停车位增加成功！");
		}else{
			toastr.info("停车位增加失败！");
		}
		
	}
	if(obj.hasOwnProperty("paramsg")){
		if(obj['paramsg'] == 'ok'){
			toastr.info("加载参数成功！");
		}else{
			toastr.info("加载参数失败！");
		}
		
	}	
	if(obj.hasOwnProperty("downpara")){
		if(obj['downpara'] == 'ok'){
			toastr.info("加载下载参数成功！");
		}else{
			toastr.info("加载下载参数失败！");
		}
		
	}
	
//	 $("#my-modal-alert").modal("toggle");  
//     $(".modal-backdrop").remove();//删除class值为modal-backdrop的标签，可去除阴影  
//     //设置提示信息  
//     $("#message").text(event.data);  
	
    //setMessageInnerHTML(event.data);
}

function isJSON(str) {
    if (typeof str == 'string') {
        try {
            var obj=JSON.parse(str);
            if(typeof obj == 'object' && obj ){
                return true;
            }else{
            	toastr.info('信息：'+str+'!!!');
                return false;
            }

        } catch(e) {
            toastr.info('信息：'+str+'!!!');
            return false;
        }
    }
    console.log('It is not a string!');
}
//连接关闭的回调方法
websocket.onclose = function(){
	console.log("wesocket close!");
    //setMessageInnerHTML("close");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function(){
    websocket.close();
}

//将消息显示在网页上
//function setMessageInnerHTML(innerHTML){
//    document.getElementById('message').innerHTML += innerHTML + '<br/>';
//}

//关闭连接
function closeWebSocket(){
    websocket.close();
}

//发送消息
function send(){
//    var message = document.getElementById('text').value;
//    websocket.send(message);
}