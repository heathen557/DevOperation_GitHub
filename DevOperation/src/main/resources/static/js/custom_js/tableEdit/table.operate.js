var baseUrl = "";
$table = $("#departTable");
$remove = $('#remove');
$(document).ready(function(){
    initDepartSelect();
    //表格初始化
    var oTable = new TableInit();
    oTable.Init();
    //查询
   /* $("#queryBtn").click(function(){
        $table.bootstrapTable('destroy');//表格销毁
        oTable.Init()
    });*/
    $("#add").click(function(){
        var name = $("#nameInp").val();
        var parentId =$("#departSel").val();
            parentId = parentId=="*"?"":parentId;
        var  $remind = $(".remind");
        if($(this).find("i").hasClass("adding")){//提交新建数据
            if(name==''){
                $remind.html(' <i class="icon icon-info-sign">部门名不能为空!</i>');
                return;
            }
            if(/(^\s+)|(\s+$)/g.test(name)){
                $remind.html(' <i class="icon icon-info-sign">部门名不能输入空格!</i>');
                return;
            }
            if(name.length>20){
                $remind.html(' <i class="icon icon-info-sign">部门名不能超过20个字符!</i>');
                return;
            }
  //博主封装的ajax方法，详见我另外一篇博客   http://blog.csdn.net/u010543785/article/details/52366495          
  $.postJSON(baseUrl+"department/add",'{"name":"'+name+'","parentdepartId":"'+parentId+'"}',function(data){
                if(data>0){
                    $("#departSel").css("display","none");
                    $("#departSel").val("*");
                    $("#nameInp").css("display","none");
                    $("#nameInp").val("");
                    $("#cancel").css("display", "none");
                    $remind.html('');
                    $("#add").html('<i class="glyphicon glyphicon-plus"></i> 新建');
                    $table.bootstrapTable('destroy');//表格销毁
                    oTable.Init();
                    $.gritter.add({
                        title: '提示',
                        text: '新建部门成功!',
                        sticky: false,
                        time: 2500
                    });
                    initDepartSelect();
                }else{
                    alert("新建部门失败，请联系技术人员!");
                }
            })
        }else {
            $("#nameInp").css("display", "block");
            $("#departSel").css("display", "block");
            $("#cancel").css("display", "inline-block");
            $("#nameInp").focus();
            $("#add").html('<i class="glyphicon glyphicon-ok-circle adding"></i> 确认');
        }
    });
});
$("#cancel").click(function(){
    $("#departSel").css("display","none");
    $("#departSel").val("*");
    $("#nameInp").css("display","none");
    $("#nameInp").val("");
    $("#add").html('<i class="glyphicon glyphicon-plus"></i> 新建');
    $(".remind").html('');
    $(this).css("display","none");
});
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $table.bootstrapTable({
            pagination: false,
            url: baseUrl + "department/queryAll",
            columns: [
                {
                    field: 'state',
                    checkbox: true
                }, {
                    title: '部门名',
                    field: 'name',
                    align: 'left',
                    formatter: spanFormatter,
                    sortable: true
                }, {
                    title: '上级部门名',
                    field: 'parentdepartName',
                    align: 'left',
                    formatter: nullFormatter,
                    sortable: true
                }, {
                    field: 'id',
                    title: '操作',
                    align: 'left',
                    events: operateEvents,
                    formatter: operateFormatter
                }
            ]
        });
    }
    return oTableInit;
}

//操作
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
        index = index+1;//tr所在行
        var $row = $table.find("tbody tr:nth-child("+index+")");
        //各字段的编辑
        var $name = $row.find("td:nth-child(2) span");
        var  $parentdepartName= $row.find("td:nth-child(3) span");

        if($(this).hasClass("editting")){//编辑完成
            $(this).siblings("a").css("display","none");
            $(this).find("i").removeClass("glyphicon-ok");
            $(this).find("i").addClass("glyphicon-edit");
            $(this).removeClass("editting");
            $row.find(".editable").editable('toggleDisabled');//转成不可编辑状态

            row.name = $name.html();
            var parentId = $parentdepartName.attr("data-value");
            row.parentdepartId = parentId=="null"?"":parentId;

             //博主封装的ajax方法，详见我另外一篇博客   http://blog.csdn.net/u010543785/article/details/52366495
     $.putJSON(baseUrl+"department/update", JSON.stringify(row),function(data){//编辑请求  并返回值
                if(data==1) {
                    $.gritter.add({
                        title: '提示',
                        text: '编辑部门成功!',
                        sticky: false,
                        time: 2000,
                    });
                }else{
                    alert("编辑失败，请联系技术人员！");
                }
            });
        }else{//开始编辑
            $(this).siblings("a").css("display","inline-block");
            $row.find(".editable").editable('toggleDisabled');
            $(this).find("i").removeClass("glyphicon-edit");
            $(this).find("i").addClass("glyphicon-ok");
            $(this).addClass("editting");
            //变成可编辑状态
            $name.editable({//部门名
                validate: function(value) {
                    if(/(^\s+)|(\s+$)/g.test(value)){
                        return '部门名不能输入空格!';
                    }
                    if(value.length>20){
                        return '部门名不能超过20个字符!';
                    }
                    if($.trim(value) == '') return '部门名不能为空！';

                }
            });
            $parentdepartName.editable({//部门
                type:'select2',
                emptytext: '无上级',
                showbuttons: false,
                select2: {
                    width: '230px',
                    minimumInputLength: 0,
                    minimumResultsForSearch: -1,
                    id: function (e) {
                        return e.id;
                    },
                    ajax: {
                        url: baseUrl+"department/queryAll",
                        dataType: 'json',
                        data: function (term, page) {
                            return { query: term };
                        },
                        results: function (data, page) {
                            var datas =[{"id":"null","name":"无上级"}];
                            for(var i in data){
                                datas.push(data[i]);
                            }
                            return { results: datas };
                        }
                    },
                    formatResult: function (depart) {
                        return depart.name;
                    },
                    formatSelection: function (depart) {
                        $parentdepartName.attr("data-value",depart.id);
                        return depart.name;
                    },
                    initSelection: function (element, callback) {
                        /* if(element.val()==""){
                            return;
                        }
                     return $.get(baseUrl+"department/queryById/"+element.val(), {}, function (data) {
                            callback(data);
                        }, 'json'); *///added dataType
                    }
                }
            });
        }
    },
    'click .cancel': function (e, value, row, index) {//取消编辑
        index = index+1;//tr所在行
        var $row = $table.find("tbody tr:nth-child("+index+")");
        $row.find(".editable").editable('toggleDisabled');
        $(this).siblings("a").find("i").removeClass("glyphicon-ok");
        $(this).siblings("a").find("i").addClass("glyphicon-edit");
        $(this).siblings("a").removeClass("editting");
        $(this).css("display","none");
    }
};
//批量删除操作
$table.on('check.bs.table uncheck.bs.table ' +
    'check-all.bs.table uncheck-all.bs.table', function () {
    $remove.prop('disabled', !$table.bootstrapTable('getSelections').length);
    // save your data, here just save the current page
    selections = getIdSelections();
    // push or splice the selections if you want to save all data selections
});
$table.on('expand-row.bs.table', function (e, index, row, $detail) {
    if (index % 2 == 1) {
        $detail.html('Loading from ajax request...');
        $.get('LICENSE', function (res) {
            $detail.html(res.replace(/\n/g, '<br>'));
        });
    }
});
$remove.click(function () {
    var ids = getIdSelections();
    //博主封装的ajax方法，详见我另外一篇博客   http://blog.csdn.net/u010543785/article/details/52366495    
    $.putJSON(baseUrl+"department/deletes","["+ids.toString()+"]",function(data){
        if(data>0){
            $table.bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
            $.gritter.add({
                title: '提示',
                text: '删除部门成功!',
                sticky: false,
                time: 2500
            });
        }else{
            alert("删除失败，请联系技术人员!");
        }
    });
    $remove.prop('disabled', true);
});
function getIdSelections() {
    return $.map($table.bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}


//---formatter 格式化返回td
function spanFormatter(data) {
    return "<span style='cursor:pointer'>"+data+"</span>";
}
function nullFormatter(value, row) {
    if(value==""||value==null||value=="null") {
        return "<span style='cursor:pointer' data-value='"+row.parentdepartId+"'></span>";
    }
    return "<span style='cursor:pointer' data-value='"+row.parentdepartId+"'>"+value+"</span>";
}
function operateFormatter(value, row, index) {
    return [
        '<a class="edit" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>  ',
        '<a class="cancel" href="javascript:void(0)" title="取消编辑" style="display:none">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>'
    ].join('');
}
//初始化部门下拉框
function initDepartSelect() {
 //博主封装的ajax方法，详见我另外一篇博客   http://blog.csdn.net/u010543785/article/details/52366495
  $.sanjiGetJSON(baseUrl + "department/queryAll", '', function (data) {

            $("#departSel").html("");
            $("#departSel").append('<option value="*">请选择上级部门</option>');
            for (var i = 0; i < data.length; i++) {
                $("#departSel").append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
            }
            //$("#departSel").select2();
        });
}