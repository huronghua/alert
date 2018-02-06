$(function () {
    /*预警通知联系人初始化*/
    $('#contacts_overhigh').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        selectAll:true,
        selectAllText:'全部',
        buttonWidth: '150px',
        dropRight: true,
        maxHeight: 200,
        nonSelectedText: '请选择被提醒人',
        numberDisplayed: 10,
        allSelectedText:'全部'
    });

    $('#contacts_abnormal').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        selectAll:true,
        selectAllText:'全部',
        buttonWidth: '150px',
        dropRight: true,
        maxHeight: 200,
        nonSelectedText: '请选择被提醒人',
        numberDisplayed: 10,
        allSelectedText:'全部'
    });

    /*添加成员取消按钮点击事件*/
    $("#cancel_button").click(function () {
        layer.closeAll(); //关闭单个
    });

    /*订单成本异常联系人设置*/
    $("#contact_input_abnormal").click(function () {
        $("#AlertAbnormal").attr("style","overflow: auto;height: 540px");
        $("#AlertAbnormal").show();
        $("#AlertOverHigh").hide();
    });

    $("#confirAbnormal").click(function () {
        $("#AlertAbnormal").hide();
    });

    $("#cancleAbnormal").click(function () {
        /*var citynodes = $.parseJSON($("#contact").val());
        var zTreeDemo = $.fn.zTree.init($("#cityTreeAbnormal"), settingAbnormal, citynodes);
        $("#contact_input_abnormal").val("");*/
        $("#AlertAbnormal").hide();
    });
    /*动态成本过高联系人设置*/
    $("#contact_input_overhigh").click(function () {
        $("#AlertOverHigh").attr("style","overflow: auto;height: 540px");
        $("#AlertOverHigh").show();
        $("#AlertAbnormal").hide();
    });

    $("#confirOverHigh").click(function () {
        $("#AlertOverHigh").hide();
    });

    $("#cancleOverHigh").click(function () {
        /*var citynodes = $.parseJSON($("#contact").val());
        var zTreeDemo = $.fn.zTree.init($("#cityTreeOverHigh"), settingOverHigh, citynodes);
        $("#contact_input_overhigh").val("");*/
        $("#AlertOverHigh").hide();
    });
});

var destination_id;
function alertConfig(obj){
    var title = $(obj).val();
    destination_id = $(obj).attr("id");
    $("#destination_id").val(destination_id);
    getConfigInfo();
    index = layer.open({
        skin: 'layui-layer-rim',
        title: [title,'text-align:center;background: #f95d5d;'],
        type: 1,
        area: ['800px', '600px'],
        shadeClose: false, //点击遮罩关闭
        scrollbar: false,
        content: $("#config_detail")
    });

}

function getConfigInfo() {
    $.ajax({
        type: "GET",
        url: 'getConfigInfo/' + destination_id,
        dataType: 'json',
        cache: false,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
        },
        success: function (data) {
            $.fn.zTree.init($("#cityTreeAbnormal"),settingAbnormal, data.abnormalContacts);
            $.fn.zTree.init($("#cityTreeOverHigh"),settingOverHigh, data.overHighContacts);
            var contact_input_overhigh = "";
            var contact_input_abnormal = "";
            var contactListOverHigh = "";
            var contactListAbnormal = "";
            for (var i=0;i<data.overHighContacts.length;i++){
                if (data.overHighContacts[i].checked!=null){
                    contact_input_overhigh += data.overHighContacts[i].name + ",";
                    contactListOverHigh += data.overHighContacts[i].id + ",";
                }
            }
            $("#contact_input_overhigh").val(contact_input_overhigh.substring(0,contact_input_overhigh.length-1));
            $("#contactListOverHigh").val(contactListOverHigh.substring(0,contactListOverHigh.length-1));

            for (var i=0;i<data.abnormalContacts.length;i++){
                if (data.abnormalContacts[i].checked!=null){
                    contact_input_abnormal += data.abnormalContacts[i].name + ",";
                    contactListAbnormal += data.abnormalContacts[i].id + ",";
                }
            }
            $("#contact_input_abnormal").val(contact_input_abnormal.substring(0,contact_input_abnormal.length-1));
            $("#contactListAbnormal").val(contactListAbnormal.substring(0,contactListAbnormal.length-1));

            var obj = data.basicInfo;
            if(typeof(obj.over_cost_1)!="undefined"){
                $("#over_cost_1").val(obj.over_cost_1);
                $("#over_cost_2").val(obj.over_cost_2);
                $("#over_cost_3").val(obj.over_cost_3);
                $("#lower_cost_1").val(obj.lower_cost_1);
                $("#time_point_before").val(obj.time_point_before);
                $("#time_point_after").val(obj.time_point_after);
            }
        }

    })
    
}

function checkData(obj) {
    if (!/^[0-9]+([.]{1}[0-9]+){0,1}$/.test($(obj).val())) {
        alert('只能输入数字');
        $(obj).val("");
    }

}

