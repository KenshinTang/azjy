
function pageload(){
	$('i').on('click',function(){
		menuTool.show($(this));
	});
	
	
	TouchMan.regtop(topLoad);
	
	
	//TouchMan.regbotton(bottomLoad);
	TouchMan.nomore();
}



function shoinfo(){
	document.location.reload();
}
function wcallback(m,d){
	console.log(m,d);
}
//新闻菜单
var menuTool={
	d:0,
	show:function(v){
		var y =	$(v).offset().top,x=$(v).offset().left-105; 
		if(y+200<$('body').height()){
			$('#ItemOp').attr("class","up");
			y+=10;
		}
		else{
			$('#ItemOp').attr("class","down");
			y-=135;
		}
		$('#ItemOp').css({top:y,left:x});
		menuTool.d=1;
		$('#ItemOp').show();
		TouchMan.reg('down',menuTool.hide);
		
	},
	hide:function(){
		if(menuTool.d){
			menuTool.d=0;
			$('#ItemOp').hide();
			TouchMan.remove('down',menuTool.hide);
		}	
	}
};



//顶栏刷新
function topLoad(){
	console.log('顶栏加载中');
	setTimeout(function(){
		TouchMan.hidetop();	
	},3000);
}


//尾栏刷新
function bottomLoad(){
	console.log('底部加载中');
	setTimeout(function(){
		TouchMan.hidebot();	
	},3000);
}