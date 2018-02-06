/**
 * Created by banma on 2017/9/21.
 */
/**ztree的参数配置，setting主要是设置一些tree的属性，是本地数据源，还是远程，动画效果，是否含有复选框等等**/
var settingAbnormal = {
    check: { /**复选框**/
        enable: true,
        chkboxType: {"Y":"s", "N":"ps"},
        nocheckInherit: false,
        chkStyle: "checkbox",
    },
    view: {
        dblClickExpand: true,
        showIcon: true,
        showLine: true
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: 0   //根节点
        }
    },
    callback: {
    onCheck: onCheckAbnormal
    }
};
var settingOverHigh = {
    check: { /**复选框**/
    enable: true,
        chkboxType: {"Y":"s", "N":"ps"},
        nocheckInherit: false,
        chkStyle: "checkbox",
    },
    view: {
        dblClickExpand: true,
        showIcon: true,
        showLine: true
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: 0   //根节点
        }
    },
    callback: {
        onCheck: onCheckOverHigh
    }
};
function onCheckAbnormal(e,setting,treeNode) {
    var treeObj = $.fn.zTree.getZTreeObj("cityTreeAbnormal");
    nodes = treeObj.getCheckedNodes(true);
    var v = "";
    var n = "";
    for (var i = 0; i < nodes.length; i++) {
        if (!nodes[i].isParent){
            v += nodes[i].id + ",";
            n += nodes[i].name + ",";
        }
    }
     v=v.substring(0,v.length-1);
     $("#contactListAbnormal").val(v);
     $("#contact_input_abnormal").val(n.substring(0,n.length-1));

}

function onCheckOverHigh(e,setting,treeNode) {
    var treeObj = $.fn.zTree.getZTreeObj("cityTreeOverHigh");
    nodes = treeObj.getCheckedNodes(true);
    var v = "";
    var n = "";
    for (var i = 0; i < nodes.length; i++) {
        if (!nodes[i].isParent){
            v += nodes[i].id + ",";
            n += nodes[i].name + ",";
        }
    }
    v=v.substring(0,v.length-1);
    $("#contactListOverHigh").val(v);
    $("#contact_input_overhigh").val(n.substring(0,n.length-1));

}
/*
$(document).ready(function(){//初始化ztree对象
    var citynodes = $.parseJSON($("#contact").val());
    var zTreeDemo = $.fn.zTree.init($("#cityTreeAbnormal"),settingAbnormal, citynodes);
    var zTreeDemo = $.fn.zTree.init($("#cityTreeOverHigh"),settingOverHigh, citynodes);
});
*/


