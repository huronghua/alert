$(function () {
    $(window).resize(function () {
        $('#alertDetailGrid').datagrid('resize');
    });

    $("#operating_status").click(function () {
        reloadgrid();
        if ($("#operating_status").prop('checked')) {
            $("#operating_status").attr("value", true);
        } else {
            $("#operating_status").attr("value", false);
        }
    });

    var userId = $("#userId").val();
    var operating_status = $("#operating_status").prop("checked");
    $('#alertDetailGrid').datagrid({
        height: 600,
        url: '/getAlertMessageDetail',
        queryParams:{
            userId:userId,
            operatingStatus:operating_status
        },
        method: 'GET',
        idField: 'messageId',
        striped: true,
        width:'auto',
        fitColumns: true,
        border:true,
        singleSelect: false,
        rownumbers: false,
        pagination: true,
        nowrap: false,
        pageSize: 20,
        pageList: [20, 40, 60, 100, 200, 400],
        showFooter: true,
        columns: [[
            { field: 'messageId', title: '消息ID', width: 150, align: 'center',hidden:true
            },
            { field: 'messageStatus', title: '已读/未读状态', width: 150, align: 'center',hidden:true
            },
            { field: 'orderId', title: '斑马订单号', width: 120, align: 'center',
                formatter:function (value, row) {
                var orderId;
                    orderId = value + 200;
                    orderId = 'YQH' + orderId;
                var rowStatus = row.messageStatus == 1 ? 'inline-block' : 'none';
                    return '<span class="row_new" style="display: ' + rowStatus + '"></span>' + orderId;
                }
            },
            { field: 'alertTime', title: '预警日期', width: 140, align: 'center' ,
                formatter:function(value){
                    if(value!=null){
                        return DateTimeFormatter(value)
                    }else {
                        return "";
                    }
                }
            },
            { field: 'departureDate', title: '出发日期', width: 140, align: 'center',sortable:true ,
                formatter:function(value){
                    if(value!=null){
                        return DateTimeFormatter(value)
                    }else {
                        return "";
                    }
                }
            },
            { field: 'returnDate', title: '归来日期', width: 140, align: 'center',sortable:true ,
                formatter:function(value){
                    if(value!=null){
                        return DateTimeFormatter(value)
                    }else {
                        return "";
                    }
                }
            },
            { field: 'estimateCost', title: '预估成本', width: 150, align: 'center' },
            { field: 'dynamicCost', title: '动态成本', width: 150, align: 'center' },
            { field: 'costDifference', title: '成本差异额', width: 150, align: 'center',sortable:true },
            { field: 'costDifferenceRate', title: '成本差异百分比', width: 150, align: 'center',sortable:true,
                formatter:function(value){
                if(value!=null){
                        return value + '%';
                    }else {
                        return "";
                    }
                }
            },
            { field: 'differenceReason', title: '差异原因', width: 260, align: 'center',
                formatter:function(value,row,index) {
                var select;
                    if(row.operatingStatus==2){
                        select = '<select class="differenceReason" disabled="true" style="width: 180px; margin-top: 10px;" onchange="updateAlertOrderdifferenceReason(' + index + ',this)"><option value="0">请选择差异原因</option><option value="1">拼单成本合并</option><option value="2">重复录入成本</option><option value="3">手抖录错</option><option value="4">币种未转换</option><option value="5">录入不及时</option><option value="6">非正常额外资源添加</option><option value="7">其他原因</option><option value="8">合理变动（退增资源）</option></select>';
                    }else {
                        select = '<select class="differenceReason" style="width: 180px; margin-top: 10px;" onchange="updateAlertOrderdifferenceReason(' + index + ',this)"><option value="0">请选择差异原因</option><option value="1">拼单成本合并</option><option value="2">重复录入成本</option><option value="3">手抖录错</option><option value="4">币种未转换</option><option value="5">录入不及时</option><option value="6">非正常额外资源添加</option><option value="7">其他原因</option><option value="8">合理变动（退增资源）</option></select>';
                    }
                    return select;
                }
            },
            { field: 'operatingStatus', title: '操作处理', width: 120, align: 'center',
                formatter:function(value,row,index) {
                    var operate_Status;
                    if(value == 2) {
                        operate_Status = '<span style="color: #ccc;">处理完成</span>'
                    } else {
                        // 成本变动在+-5%之间或差异原因为"合理变动" 或"拼单成本合并"
                        if((parseFloat(row.costDifferenceRate) >= -5 && parseFloat(row.costDifferenceRate) <= 5) || (row.differenceReason == 1) || (row.differenceReason == 8)) {
                            operate_Status = '<button type="button" value="2" class="btn btn-primary" onclick="updateAlertOrderoperatingStatus(' + index + ',this)">待处理</button>'
                        } else {
                            operate_Status = '<button type="button" value="1" class="btn btn-primary" disabled>待处理</button>'
                        }

                    }

                    return operate_Status;
                }
            }
        ]],
        rowStyler:function(index,row){
            if (row.operatingStatus==2){
                return 'color:#ccc;';
            }
        },
        onBeforeLoad: function (param) {
        },
        onLoadSuccess: function (data) {
            $("#unHandleInfo").html(data.unHandleInfoCount);
            var row = $('#alertDetailGrid').datagrid('getRows');

            $(".differenceReason").each(function(index){
                if(row[index].differenceReason!="") {
                    $(this).val(row[index].differenceReason);
                }
            });
            $(".operatingStatus").each(function(index){
                $(this).val(row[index].operatingStatus);
            });
            var columns = $('#alertDetailGrid').datagrid("options").columns;
            $('.pagination-page-list').hide();
        },
        onLoadError: function () {

        },
        onDblClickRow :function(row,index){
            var orderId = index.orderId;
            window.open('http://admintest.banma_cefu.com:8080/UED/OrderOperation/resource.html?order_id=' + orderId);
            //window.open('http://admintest.banma_cefu_all.com:8080/UED/OrderOperation/resource.html?order_id=' + orderId);
            //window.open('http://admin.bmtrip.com:8080/UED/OrderOperation/resource.html?order_id=' + orderId);
        },
        onClickCell: function (rowIndex, field, value) {

        }
    });

    $("#download").click(function () {
        $("#downloadDetail").submit()
    });

});

function DateTimeFormatter(value) {
    return moment(value).format("YYYY-MM-DD");
}

//增加查询参数，重新加载表格
function reloadgrid() {

    //查询参数直接添加在queryParams中

    var queryParams = $('#alertDetailGrid').datagrid('options').queryParams;
    getQueryParams(queryParams);
    $('#alertDetailGrid').datagrid('options').queryParams = queryParams;//传递值

    $("#alertDetailGrid").datagrid('load');//重新加载table
}

function getQueryParams(queryParams) {
    var userId = $("#userId").val();
    var operatingStatus = $("#operating_status").prop("checked");
    queryParams.operatingStatus = operatingStatus;
    queryParams.userId = userId;
    return queryParams;
}

// 更新预警订单差异原因信息
function updateAlertOrderdifferenceReason(index,obj) {
    //var column = $(obj).parent().parent().attr("field");
    var row = $('#alertDetailGrid').datagrid('getRows');
    var differenceReason = $(obj).val();
    var operatingStatus = row[index].operatingStatus;
    updateAlertOrderInfo(index,row,operatingStatus,differenceReason);
}

// 更新预警订单操作状态信息
function updateAlertOrderoperatingStatus(index,obj) {
    $(obj).parent().parent().attr("field");
    var row = $('#alertDetailGrid').datagrid('getRows');
    var operatingStatus = $(obj).val();
    var differenceReason = row[index].differenceReason;
    updateAlertOrderInfo(index,row,operatingStatus,differenceReason);
}

function updateAlertOrderInfo(index,row,operatingStatus,differenceReason) {
    $.ajax({
        type : "POST",
        url : '/order/updateAlertOrderInfo',
        data : {
            'orderId' : row[index].orderId,
            'operatingStatus' : operatingStatus,
            'differenceReason' : differenceReason,
            'alertTime' : row[index].alertTime
        },
        dataType : 'json',
        cache : false,
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
        },
        success : function(data) {
            if ("success" == data) {
                $('#alertDetailGrid').datagrid('reload');
            }
        }
    });
    
}

