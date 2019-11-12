var ImgUpload=new function(){
	var token=null,expire=0,isup=false,box=null,g_filename='';
	var tokenapi='admin/imgupload/getImgPolicy';
	document.write('<script src="' + bootPATH + 'plupload.full.min.js" type="text/javascript"></sc' + 'ript>');
	var _resize=_callback=_uploader=null;	
	function get_signature()
	{
		now = Date.parse(new Date()) / 1000; 
		if (expire < now + 3){
			var xmlhttp = new XMLHttpRequest();			
			serverUrl = AJAX.Uri()+tokenapi;
			xmlhttp.open( "GET", serverUrl, false );
			xmlhttp.send( null );
			token=JSON.parse(xmlhttp.responseText);
			expire = parseInt(token['expire']);
		}
	}
	function calcName(filename)
	{
		var pos = filename.lastIndexOf('.'),tag = '';
    	if (pos != -1)
        	tag = filename.substring(pos);    
		return g_filename= token.dir +'/'+ token.filename +tag;
	}
	function initParam(up, filename, ret)
	{
		if (!ret)
			get_signature();
		doupload(up, filename);    
	}	
	function doupload(up, filename){
		var mps = {
			'key' : calcName(filename),
			'policy':token.policy,
			'OSSAccessKeyId': token.accessid, 
			'success_action_status' : '200',
			'callback' : token.callbackbody,
			'signature': token.signature,
		};
		up.setOption({
			'url': token.host,
			'multipart_params': mps
		});
		up.start();
	}
	function addFile(up, files){
		setMark(0); 
		setMess(false);
		if(files.length>1){
			files.splice(0,files.length-1);	
		}			
		var file=files[0];						
		var preloader = new mOxie.Image();
		preloader.onload = function () {
			preloader.downsize(300, 300,90);
			var imgsrc = preloader.getAsDataURL();
			$('#UpImageBox .content').css('backgroundImage','url('+imgsrc+')'); 
			preloader.destroy();
			preloader = null;
		};
		preloader.load(file.getSource());
		Comm.bg(true);
		$(box).show();
	}
	function closeSelf(){
		if(isup){
			_uploader.stop();
			isup=false;	
		}
		$(box).hide();
		$('#UpImageBox .content').css("backgroundImage","none"); 
		Comm.bg(false);		
	}
	function Error(msg){
		Comm.message(msg);
		closeSelf();
	}
	function initWindow(){
		if(box==null){
			box=document.createElement('DIV');
			box.id='UpImageBox';
			box.innerHTML='<div class="nav"><span class="back"></span><span class="select">使用</span></div>'
				+'<div class="content"><div class="mask"></div></div><div class="upmess"></div>';
			document.body.appendChild(box);
		}	
	}
	function setMess(l){
		$('#UpImageBox .upmess').html(l?'正在努力上传，请稍候...':'');
	}
	function setMark(w){
		$('#UpImageBox .mask').css("width",w==0?'0px':w+'%'); 	
	}
	function init(button){
		initWindow();
		_uploader = new plupload.Uploader({
			runtimes : 'html5',
			browse_button :button, 
			multi_selection: false,
			container:document.getElementById(button).parentNode,
			url : 'http://oss.aliyuncs.com',
			filters: {
				mime_types : [
					{ title: "图片文件", extensions: "jpg,png" }
				],
				max_file_size : '2mb',
				prevent_duplicates : true 
			},
			resize: _resize,
			init: {
				PostInit: function() {
					$('#UpImageBox .back').on('click',closeSelf);
					$('#UpImageBox .select').on('click', function() {
						if (isup)return;
						isup=true;
						setMess(true);
						setMark(100); 
					  	initParam(_uploader, '', false);
					  	return false;
					});
				},
				FilesAdded:addFile,
				BeforeUpload: function(up, file) {initParam(up, file.name, true);},
				UploadProgress: function(up, file) {
					setMark(100-file.percent);  
				},
				FileUploaded: function(up, file, info) {
					isup=false;
					setMess(false);
					if (info.status == 200){
						_callback&&_callback(g_filename);
						closeSelf();
					}
					else
						Error(info.response);
				},
				Error: function(up, err) {Error( err.response);}
			}
		});		
		_uploader.init();
	}
	/*
	button:激活事件的按钮
	cb:回调事件
	resize: {
		width: 100,指定压缩后图片的宽度
		height: 100,指定压缩后图片的高度
		crop: true,是否裁剪图片
	}非必填
	*/
	return {
		init:function(button,cb,resize){			
			if(resize==null)
				resize= {width: 800,crop: false}
			_resize=resize;
			_callback=cb;
			init(button);
		},
		close:closeSelf
	}	
}