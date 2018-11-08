//var tree =[{"text":"中心"}];
//
//   $(function () {
//        $.ajax({
//            type: "Post",
//            url: "/devoperation/orgnization/inittree",
//            dataType: "json",
//            success: function (result) {
//                $('#tree').treeview({
//                    data: result,         // 数据源
//                    showCheckbox: true,   //是否显示复选框
//                    highlightSelected: true,    //是否高亮选中
//                    //nodeIcon: 'glyphicon glyphicon-user',    //节点上的图标
//                    nodeIcon: 'glyphicon glyphicon-globe',
//                    emptyIcon: '',    //没有子节点的节点图标
//                    multiSelect: false,    //多选
//                    onNodeChecked: function (event,data) {
//                        alert(data.nodeId);
//                    },
//                    onNodeSelected: function (event, data) {
//                        alert(data.nodeId);
//                    }
//                });
//            },
//            error: function () {
//                     $('#tree').treeview({
//                    data: tree,         // 数据源
//                    showCheckbox: true,   //是否显示复选框
//                   	showBorder: false,
//                    highlightSelected: true,    //是否高亮选中
//                    //nodeIcon: 'glyphicon glyphicon-user',    //节点上的图标
//                    nodeIcon: 'glyphicon glyphicon-globe',
//                    emptyIcon: '',    //没有子节点的节点图标
//                    multiSelect: false,    //多选
//                    onNodeChecked: function (event,data) {
//                        //alert(data.nodeId);
//                    },
//                    onNodeSelected: function (event, data) {
//                        //alert(data.nodeId);
//                    }
//                });
//            }
//        });
//    })
//   
  