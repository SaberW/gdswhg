accessid = ''
accesskey = ''
host = ''
policyBase64 = ''
signature = ''
callbackbody = ''
filename = ''
key = ''
expire = 0
g_object_name = ''
g_object_name_type = ''
now = timestamp = Date.parse(new Date()) / 1000;

/**
 * 获取OSS签名
 * @returns {String|null|string|*}
 */
function send_request(){
    var xmlhttp = null;
    if (window.XMLHttpRequest){
        xmlhttp=new XMLHttpRequest();
    }else if (window.ActiveXObject){
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    if (xmlhttp!=null){
        serverUrl = basePath+'/signature?dir='+encodeURIComponent(_userDir)+'&libid='+encodeURIComponent(_libid)+'&uid='+encodeURIComponent(_uid)+'&t='+new Date().getTime();
        xmlhttp.open("GET", serverUrl, false);
        xmlhttp.send( null );
        return xmlhttp.responseText
    }else{
        alert("Your browser does not support XMLHTTP.");
    }
}

/**
 * local_name-保存本地文件名
 * random_name-随机生成文件名
 */
function check_object_radio() {
    var tt = document.getElementsByName('myradio');
    for (var i = 0; i < tt.length ; i++ ){
        if(tt[i].checked){
            g_object_name_type = tt[i].value;
            break;
        }
    }
}

/**
 * 获取签名信息
 * @returns {boolean}
 */
function get_signature(){
    //可以判断当前expire是否超过了当前时间,如果超过了当前时间,就重新取一下.3s 做为缓冲
    now = timestamp = Date.parse(new Date()) / 1000; 
    if (expire < now + 3){
        body = send_request();
        var obj = eval ("(" + body + ")");
        host = obj['host'];
        policyBase64 = obj['policy'];
        accessid = obj['accessid'];
        signature = obj['signature'];
        expire = parseInt(obj['expire']);
        callbackbody = obj['callback'];
        key = obj['dir'];
        return true;
    }
    return false;
};

/**
 * 根据指定长度获取随机字符串
 * @param len
 * @returns {string}
 */
function random_string(len) {
    return guid();
// 　　len = len || 32;
// 　　var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
// 　　var maxPos = chars.length;
// 　　var pwd = '';
// 　　for (i = 0; i < len; i++) {
//     　　pwd += chars.charAt(Math.floor(Math.random() * maxPos));
//     }
//     return pwd;
}

/**
 * JS生成UUID
 * @returns {string}
 */
function guid() {
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
    return uuid.replace(/-/g, '');
}

/**
 * 文件的后缀
 * @param filename
 * @returns {string}
 */
function get_suffix(filename) {
    pos = filename.lastIndexOf('.');
    suffix = '';
    if (pos != -1) {
        suffix = filename.substring(pos)
    }
    return suffix;
}

/**
 * 计算上传文件的名称
 * @param filename 选择的文件名
 * @returns {string}
 */
function calculate_object_name(filename){
    if (g_object_name_type == 'local_name')    {
        g_object_name += "${filename}"
    }else if (g_object_name_type == 'random_name'){
        suffix = get_suffix(filename);
        g_object_name = key + random_string(10) + suffix
    }
    return ''
}

/**
 * 获得上传对象的名称
 * @param filename
 * @returns {*}
 */
function get_uploaded_object_name(filename){
    if (g_object_name_type == 'local_name')    {
        tmp_name = g_object_name;
        tmp_name = tmp_name.replace("${filename}", subFileName(filename));
        return tmp_name;
    }else if(g_object_name_type == 'random_name'){
        return g_object_name;
    }
}

/**
 * 截取文件名
 * @param fname
 * @returns {*}
 */
function subFileName(fname){
	if(fname){
		var idx = fname.lastIndexOf('.');
		var preStr = fname.substring(0,idx);
		var nexStr = fname.substring(idx);
		if(preStr.length > 10){
			preStr = preStr.substring(0,10);
		}
		fname = preStr+nexStr;
	}
	return fname;
}

/**
 * 设置上传参数并上传
 * @param up
 * @param filename
 * @param ret
 */
function set_upload_param(up, filename, ret){
    if (ret == false)    {
        ret = get_signature();
    }
    g_object_name = key;
    if (filename != '') {
        suffix = get_suffix(filename)
        calculate_object_name(filename)
    }
    new_multipart_params = {
        'key' : g_object_name,
        'policy': policyBase64,
        'OSSAccessKeyId': accessid, 
        'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
        'callback' : callbackbody,
        'signature': signature
    };
    up.setOption({
        'url': host,
        'multipart_params': new_multipart_params
    });
    up.start();
}

//根据资源库的类型，确定可上传哪些文件
var allow_mime_types = [];
if('img' == _resourcetype){
    allow_mime_types.push({ title : "Image files", extensions : "jpg,gif,png,bmp" });
}else if('video' == _resourcetype){
    allow_mime_types.push({ title : 'Video files', extensions : "mp4,MP4"});
}else if('audio' == _resourcetype){
    allow_mime_types.push({ title : 'Audio files', extensions : "mp3,MP3"});
}else if('file' == _resourcetype){
    allow_mime_types.push({ title : 'Attachement files', extensions : "zip,doc,docx,xls,xlsx,pdf"});
}

/** 验证资源是否存在 */
function existRes(resname){
    var exist = true;
    $.ajax({
        url: basePath+'/admin/mass/libres/existResource',
        type: 'POST',
        async: false,
        data: {resName:resname, libid:_libid},
        success: function (data) {
            if(!$.isPlainObject(data)){
                data = $.parseJSON(data);
            }
            var existStr = data.data;
            exist = existStr == 'yes' ? true : false;
        }
    });
    return exist;
}

var uploader = new plupload.Uploader({
	runtimes : 'html5,flash,silverlight',
	browse_button : 'selectfiles', 
    //multi_selection: false,
	container: document.getElementById('container'),
	flash_swf_url : basePath+'/pages/admin/video/lib/plupload-2.1.2/js/Moxie.swf',
	silverlight_xap_url : basePath+'/pages/admin/video/lib/plupload-2.1.2/js/Moxie.xap',
    url : 'http://oss-cn-shenzhen.aliyuncs.com',//'http://oss.aliyuncs.com',

    filters: {
        mime_types : allow_mime_types,
        max_file_size : '1001mb', //最大只能上传10mb的文件
        prevent_duplicates : false //不允许选取重复文件
    },

	init: {
		PostInit: function() {
            document.getElementById('selectfiles').style.backgroundColor = '';
            document.getElementById('postfiles').style.backgroundColor = '';
            document.getElementById('filesTitle').style.display = '';
			document.getElementById('ossfile').innerHTML = '';
			document.getElementById('postfiles').onclick = function() {
                set_upload_param(uploader, '', false);
                return false;
			};
		},

		FilesAdded: function(up, files) {
            $('#console').hide();
		    var removeidx = [];
            for(var i in files){
                var filename = files[i].name;
                var pos = filename.lastIndexOf('.');
                var fname = filename.substring(0,pos);
                var pattern = /^[\u4E00-\u9FA5\w\-\(\)]{1,30}$/;
                var exist = existRes(fname);
                if(exist){//资源不能重复
                    $('#console').html("相同资源库下已经存在相同名称的资源，请将【"+filename+"】重命名后再上传").show();
                    removeidx.push(parseInt(i));
                    up.removeFile(files[i].id);
                }
                if(!pattern.test(fname)){
                    $('#console').html(filename+"不能上传，上传的文件名只支持数字，英文字母，下划线，中文，圆括号").show();
                    removeidx.push(parseInt(i));
                    up.removeFile(files[i].id);
                }
            }
            if(removeidx.length > 0){
                for(var i=0; i<removeidx.length; i++){
                    var idx = removeidx[i];
                    files.splice(idx, 1);
                }
                up.refresh();
            }
			plupload.each(files, function(file) {
                document.getElementById('ossfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                    +'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
                    +'</div>';
			});
		},

		BeforeUpload: function(up, file) {
            check_object_radio();
            set_upload_param(up, file.name, true);
        },

		UploadProgress: function(up, file) {
			var d = document.getElementById(file.id);
			d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
            var prog = d.getElementsByTagName('div')[0];
			var progBar = prog.getElementsByTagName('div')[0];
			progBar.style.width= 2*file.percent+'px';
			progBar.setAttribute('aria-valuenow', file.percent);
		},

		FileUploaded: function(up, file, info) {
            if (info.status == 200){
                //document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '100%&nbsp;&nbsp;————上传成功, 视频文件名称:' + get_uploaded_object_name(file.name);
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '100%&nbsp;&nbsp;————上传成功';
            }else{
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
            } 
		},

		Error: function(up, err) {
            if (err.code == -600) {
                $('#console').html("选择的文件过大，最多支持1G。").show();
            }else if (err.code == -601) {
                if('img' == _resourcetype){
                    $('#console').html("请选择有效的图片格式文件,支持的格式有jpg,gif,png,bmp。").show();
                }else if('video' == _resourcetype){
                    $('#console').html("请选择有效的视频格式文件,支持的格式有mp4。").show();
                }else if('audio' == _resourcetype){
                    $('#console').html("请选择有效的音频格式文件,支持的格式有mp3。").show();
                }else if('file' == _resourcetype){
                    $('#console').html("请选择有效的文件,支持的格式有zip,doc,docx,xls,xlsx,pdf。").show();
                }
            }else if (err.code == -602) {
                $('#console').html("这个文件已经上传过一遍了。").show();
            }else {
                if( 'rgb(187, 187, 187)' == document.getElementById('selectfiles').style.backgroundColor){
                    document.getElementById('ossfile').innerHTML = '你的浏览器不支持flash,Silverlight或者HTML5！请使用IE 11, 最新谷歌或者火狐浏览器';
                    document.getElementById('ossfile').style.color = "red";
                }else{
                    $('#console').html("错误消息:" + err.response).show();
                }
            }
		}
	}
});

uploader.init();