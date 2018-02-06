(function() {
    if (!window.client) {
        client = {};
    }
})();
client = {
    /**
     * 判断是否为空
     *
     * @param obj
     * @returns {Boolean}
     */
    isBlank : function(obj) {
        return obj == undefined || obj == null || $.trim(obj) == "";
    },

    /**
     * 判断是否非空
     *
     * @param obj
     * @returns {Boolean}
     */
    isNotBlank : function(obj) {
        return !this.isBlank(obj);
    },

    /**
     * 获取单选框值,如果有表单就在表单内查询,否则在全文查询
     *
     * @param{String}name radio名称
     * @param{$()} frm $ object
     * @returns
     */
    getRadioValue : function(name, frm) {
        if (frm && frm.find) {
            return frm.find('input[name="' + name + '"]:checked').val();
        }
        return $('input[name="' + name + '"]:checked').val();
    },

    /**
     * 在id区域内执行回车提交数据<br>
     * 实际处理中应该将提交按键放在id区域外,避免重复提交
     *
     * @param{String} id 被绑定对象的ID号
     * @param{Function} func 要选择的函数
     * @returns {Boolean}
     */
    bindingEnterKey : function(id, func) {
        $('#' + id).keydown(function(e) {
            if (e.keyCode == 13) {
                if ($.isFunction(func)) {
                    func();
                }
            }
        });
    },

    /**
     * 将输入控件集合序列化成对象<br>
     * 名称或编号作为键，value属性作为值
     *
     * @param {Array}
     *            inputs input/select/textarea的对象集合
     * @return {object} json 对象 {key:value,...}
     */
    _serializeInputs : function(inputs) {
        var json = {};
        if (!inputs) {
            return json;
        }
        for ( var i = inputs.length - 1; i >= 0; i--) {
            var input = $(inputs[i]);
            var type = input.attr('type');
            if (type) {
                type = type.toLowerCase();
            }
            var tagName = input.get(0).tagName;
            var id = input.attr('id');
            var name = input.attr('name');
            var value = null;
            // 判断输入框是否已经序列化过
            if (input.hasClass('_isSerialized')) {
                continue;
            }
            // input输入标签
            if (tagName == 'INPUT' && type) {
                switch (type) {
                    case 'checkbox': {
                        value = json[name || id];
                        if (input.is(':checked')) {
                            if (value) {
                                if (!(value instanceof Array)) {
                                    value = [];
                                    value.push(json[name || id]);
                                }
                                value.push(input.val());
                            } else {
                                value = input.val();
                            }
                        }
                    }
                        break;

                    case 'radio': {
                        if (input.is(':checked')) {
                            value = input.attr('value');
                        } else {
                            continue;
                        }
                    }
                        break;

                    default: {
                        if (input.hasClass('date')) {
                            value = client.date2Number(input.val(), 'd');
                        } else if (input.hasClass('time')) {
                            value = client.date2Number(input.val(), 't');
                        } else {
                            value = input.val();
                        }
                    }
                }
            } else {
                // 非input输入标签，如：select,textarea
                value = input.val();
            }
            json[name || id] = value;
            // 清除序列化标记
            input.removeClass('_isSerialized');
        }
        return json;
    },

    /**
     * @param{$()} frm jQuery表单对象
     * @returns {Object} json对象 最多包含两层结构
     */
    serialize : function(frm) {
        var json = {};
        frm = frm || $('body');
        if (!frm) {
            return json;
        }
        var groups = frm.find('div[fieldset]');
        var jsonGroup = this._serializeGroups(groups);
        var inputs = this._filterInputs(frm);
        var json = this._serializeInputs(inputs);
        for ( var key in jsonGroup) {
            json[key] = jsonGroup[key];
        }
        frm.find('._isSerialized').removeClass('_isSerialized');
        return json;
    },

    /**
     * 获取合法的输入标签 包含name属性
     *
     * @param {$()}
     *            container 标签容器
     * @returns {[]} inputs jQuery对象数组
     */
    _filterInputs : function(container) {
        var inputs = $(container
            .find(
                'input[type!=button][type!=reset][type!=submit][type!=image][type!=file],select,textarea')
            .filter('[name]'));
        return inputs;
    },

    /**
     * 查找符合条件的输入标签
     *
     * @param{Array} inputs jQuery输入标签数组
     * @param{String} key 查询关键字
     * @returns{Array} input 标签数组
     */
    _findInputs : function(inputs, key) {
        var input = $(inputs.filter('input[name=' + key + '],input[id=' + key
            + '],textarea[name=' + key + '],textarea[id=' + key
            + '],select[name=' + key + '],select[id=' + key + ']'));
        return input;
    },

    /**
     * 将json对象转成json字符串
     *
     * @param{Object} json对象或字符串值
     * @returns{String} json串
     */
    jsonToString : function(jsonObj) {
        // 字符串不处理，否则值到后台，会在两边各多出一个双引号"
        if (typeof jsonObj === 'string') {
            return jsonObj;
        }
        return JSON.stringify(jsonObj);
    },

    /**
     * 支持一维表单填充
     *
     * @param form
     * @param data
     */
    formData : function(form, data) {
        // 遍历当前对象中含有id属性所有dom
        form.find('[name]').each(
            function() {
                var name = $(this).attr('name');
                var value = data[name];
                if (value) {
                    var tagName = $(this).get(0).tagName;
                    // 如果是表单
                    if (tagName == 'INPUT') {
                        $(this).val(value);
                    } else if (tagName == 'SELECT') {
                        $(this).find('option[value="' + value + '"]').attr(
                            'selected', 'selected');
                    } else {
                        // TODO 待完善
                    }
                }
            });
    },

    /**
     * 异步ajax请求，一般不直接使用而是使用ayncGet或ayncPost
     *
     * @param url
     *            请求url
     * @param param
     *            请求参数，务必带上formId
     * @param func
     *            服务处理成功的回调
     * @param type
     *            请求类型 GET 或 POST
     *
     * @returns {Boolean}
     */
    ayncAjax : function(url, param, func, type) {
        if (this.isBlank(url)) {
            alert('未设置url');
            return false;
        }
        var ajaxParam = {
            url : url,
            type : type,
            contentType : 'application/json',
            data : this.jsonToString(param),
            dataType : 'json',
            success : function(data) {
                if(func)func(data);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                alert("操作执行失败！");
            }
        };
        $.ajax(ajaxParam);
    },

    /**
     * 异步get请求,一般用于获取数据
     *
     * @param url
     *            请求url
     * @param param
     *
     * @param func
     *            服务处理成功的回调
     */
    ayncGet : function(url, param, func) {
        this.ayncAjax(url, param, func, 'GET');
    },

    /**
     * 异步post请求,一般用于提交数据,做增删改操作
     *
     * @param url
     *            请求url
     * @param param
     *
     * @param func
     *            服务处理成功的回调
     */
    ayncPost : function(url, param, func) {
        this.ayncAjax(url, param, func, 'POST');
    },

    /**
     * 异步表单提交
     *
     * @param form
     *            要提交的表单
     * @param func
     *            服务处理成功的回调
     */
    ayncSubmit : function(form, func) {
        var param = this.serialize(form);
        param.formId = form.attr('id');
        var url = form.attr('action');
        if (this.isBlank(url)) {
            alert('未设置action');
            return false;
        }
        this.ayncPost(url, param, func);
    },

    /**
     * 将ajaxSubmit整合到helper下
     *
     * @param formId
     * @param func
     * @returns {Boolean}
     */
    ajaxSubmit : function(formId, func) {
        if (this.isBlank(formId)) {
            alert('未传formId');
            return false;
        }
        var form = $('#' + formId);
        var url = form.attr('action');
        if (this.isBlank(url)) {
            alert('未设置url');
            return false;
        }
        var method = form.attr('method') || 'POST';
        var dataType = form.attr('dataType') || 'json';
        form.ajaxSubmit({
            type : method,
            dataType : dataType,
            url : url,
            success : function(data) {
                data.formId = formId;
            }
        });
        return false;
    },

    /**
     * 日期格式化
     * @param val
     * @param format
     * @returns
     */
    dateFormat: function (val,format){
        if(!val){
            return "";
        }
        val =val.toString();
        //日期
        if(format == 'd'){
            return val.replace(/^(\d{4})(\d{2})(\d{2})$/,'$1-$2-$3');
        }else if(format == 't'){
            if(val.length<6){
                while(val.length<6){
                    val = '0'+val;
                }
            }
            return val.replace(/^(\d{2})(\d{2})(\d{2})$/,'$1:$2:$3');
        }else{
            return val.replace(/^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/,'$1-$2-$3 $4:$5:$6');
        }
    },

    /**
     * 格式化金额,加千分位
     *
     * @param value
     *            被格式化值
     * @param n
     *            小数点位数
     */
    amountFormat : function(value, n) {
        if (value && n) {
            value = value.replace(/(\s)*/g, "");
            // value = new BigNumber(value).toFixed(n);
            // value = parseFloat(value).toFixed(n);
            value = new BigNumber(value.replace(/[^\d\.-]/g, "")).toFixed(n);
            // value = parseFloat(value.replace(/[^\d\.-]/g, "")).toFixed(n);
            value = value.replace(".", ",");
            var re = /(\d)(\d{3},)/;
            while (re.test(value)) {
                value = value.replace(re, "$1,$2");
            }

            if(n==2){
                value = value.replace(/,(\d\d)$/, ".$1");
            }else if(n==6){
                value = value.replace(/,(\d\d\d\d\d\d)$/, ".$1");
            }else if(n==4){
                value = value.replace(/,(\d\d\d\d)$/, ".$1");
            }
        }
        return value;
    },

    /**
     * 日期格式化
     *
     * @param val
     * @param format
     * @returns
     */
    dateFormat : function(val, format) {
        if (!val) {
            return "";
        }
        val = val.toString();
        // 日期
        if (format == 'd') {
            return val.replace(/^(\d{4})(\d{2})(\d{2})$/, '$1-$2-$3');
        } else if (format == 't') {
            if (val.length < 6) {
                while (val.length < 6) {
                    val = '0' + val;
                }
            }
            return val.replace(/^(\d{2})(\d{2})(\d{2})$/, '$1:$2:$3');
        } else {
            return val.replace(/^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/,
                '$1-$2-$3 $4:$5:$6');
        }
    },

    /**
     * 是否金额格式判断
     */
    isAmount : function(s) {
        return /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,2})?$/.test(s);
    },

    /**
     * 日期\时间转数字
     *
     * @param date
     * @param format
     * @returns
     */
    date2Number : function(date, format) {
        if (format == 'd') {
            return date.replace(new RegExp('-', 'gm'), '');
        } else if (format == 't') {
            return date.replace(new RegExp(':', 'gm'), '');
        }
        return date;
    },

    /**
     * 处理class中包括date或time
     *
     * @param container
     *            目标表单容器，jQuery("#formId")
     */
    dateTimeHandle : function(container) {
        if (container) {
            container.find("input[type='text']").each(function() {
                var isAddhidden = false;
                var value = $(this).val();
                if ($(this).hasClass("date") || $(this).hasClass("Wdate")) {// 处理日期类型
                    if (value) {
                        value = value.replace(/[^0-9]/g, "");
                        isAddhidden = true;
                    }
                } else if ($(this).hasClass("time")) {// 处理时间类型
                    if (value) {
                        value = value.replace(/[^0-9]/g, "");
                        isAddhidden = true;
                    }
                }
                if (isAddhidden) {
                    var id = $(this).attr('id');
                    var name = $(this).attr('name');
                    // 1、清空元素name和id，用于显示
                    $(this).attr('id', '').attr('name', '');
                    // 2、新增hidden，用于提交
                    var hidden = $("<input type='hidden' />").attr(
                        "name", name).attr("id", id).val(value);
                    container.append(hidden);
                }
            });
        }
    },

    /**
     * 表单提交
     *
     * @param formId
     *            目标表单id，默认取"searchForm"
     */
    submitForm : function(formId) {
        var container = this.getContainer(this.getFormId(formId));
        this.dateTimeHandle(container);
        this.validateContainer(container);  //通过函数的形式校验
        //	container.submit();
    },


    /**
     * 勾选\取消勾选复选框
     *
     * @param obj
     * @param name
     */
    processCheckBox : function(obj, name) {
        var selects = $(":checkbox[name='" + name + "']");
        for ( var i = 0; i < selects.length; i++) {
            if (!selects[i].disabled) {
                selects[i].checked = obj.checked;
            }
        }
    },

    /**
     * 获取勾选复选框
     *
     * @param name
     * @returns {Array}
     */
    getCheckBoxByChecked : function(name) {
        var selects = $(":checkbox[name='" + name + "']:checked");
        var keys = [];
        if (selects.length > 0) {
            for ( var i = 0; i < selects.length; i++) {
                if (selects[i].value) {
                    keys.push(selects[i].value);
                }
            }
        }
        return keys;
    },

    /**
     * 获取表单id
     *
     * @param formId
     *            表单id，默认取"searchForm"
     * @returns
     */
    getFormId : function(formId) {
        return this.isNotBlank(formId) ? formId : 'searchForm';
    },

    /**
     * 获取容器
     *
     * @param id
     *            不传则取#main
     * @returns
     */
    getContainer : function(id) {
        return this.isNotBlank(id) ? $('#' + id) : $('#main');
    },



    formatDateString : function(value){
        if(value.length == 8){
            return value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
        } else if(value.length == 6){
            return value.substring(0, 4) + "-" + value.substring(4, 6);
        } else {
            return value;
        }
    }

};
