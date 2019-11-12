var TouchMan=new function(){
	//是否触屏
	var touch=('ontouchstart' in window);
	//事件容器
	var downEvents=[],moveEvents=[],endEvents=[],scrollEvents=[],path=null;	
	function noStop(e){return !e.isDefaultPrevented;}
	//处理path
	function pathop(p){
		if(path==null)
			path=[];
		var l=path.length;
		if(l>0){
			if(p==path[l-1])return;
			if(l>=5)path.shift();
		}
		
		path.push(p);		
	}
	//判断方向，负向上，正向下，0不动
	function calcDe(){
		if(path==null||path.length<2)return 0;
		var s=path[0];
		for (var i=1;i<path.length;i++){
			s-=path[i];
		}
		console.log(s);
		return s;
	}
	//document点击/触摸事件
	function down(e){
		path=[];
		var pe=touch?e.touches[0]:e;
		pathop(pe.pageY);
		if (downEvents.length>0){
			for (var i in downEvents){
				if(noStop(e))
					downEvents[i](e,downEvents[i]);	
			}	
		}
	}
	//document移动事件
	function move(e){
		var pe=touch?e.touches[0]:e;
		pathop(pe.pageY);
		if (moveEvents.length>0){
			for (var i in moveEvents){
				if(noStop(e))
					moveEvents[i](e,moveEvents[i]);	
			}
		}
	}
	//document滚动条事件
	function scrol(e){
		for (var i in scrollEvents){
			if(noStop(e))
				endEvents[i](e,scrollEvents[i]);	
		}	
	}
	//触摸结束事件
	function end(e){
		path=null;
		var pe=touch?e.changedTouches[0]:e;
		if (endEvents.length>0){
			for (var i in endEvents){
				if(noStop(e))
					endEvents[i](e,endEvents[i]);	
			}	
		}
	}
	//根据类别，返回事件容器
	function getEventByType(etype){
		switch(etype){
			case 'down':return downEvents;
			case 'move':return moveEvents;
			case 'end':return endEvents;
			case 'scroll':return scrollEvents;
		}
		return null;
	}
	//注册原生事件
	function regEvent(etype){
		var m,e,h;
		switch(etype){
			case 'down':m=downEvents,e=touch?'touchstart':'mousedown',h=down; break;
			case 'move':m=moveEvents,e=touch?'touchmove':'mouseover',h=move;break;
			case 'end':m=endEvents,e=touch?'touchend':'mouseup',h=end;break;
			case 'scroll':m=scrollEvents,e='scroll',h=scrol;break;
			default:return;
		}
		document[m.length==0?'removeEventListener':'addEventListener'](e,h, false);
	}
	//判断当前事件是事存在注册列表中
	function getEback(Events,eback){
		return 	Events.indexOf(eback);
	}
	//注册事件,回调方法
	function reg(etype,eback){
		var es=getEventByType(etype);
		if(es){
			if(getEback(es,eback)==-1){
				es.push(eback);	
				regEvent(etype);
			}
		}
	}
	//移除注册的方法
	function remove(etype,eback){
		var es=getEventByType(etype);
		if(es){
			var i=getEback(es,eback);
			if(i>-1){
				es.splice(i,1);	
				regEvent(etype);
			}
		}
	}
	
	//下拉刷新事件
	var topShow={
		box:null,//容器
		s:false,//是否显示
		l:false,//是否执行回调中
		e:null,//回调事件
		w:100.0,//实际宽度
		init:function(e){//初始化
			if(topShow.box==null){
				topShow.e=e;
				topShow.box=document.createElement('DIV');
				topShow.box.id='TopLoad';
				topShow.box.innerHTML='<div id="TopLoadbar"></div><span></span>';
				$('section').prepend(topShow.box);
				//注册移动事件
				//reg('move',topShow.checkmove);
				document.querySelector('section').addEventListener('touchmove',topShow.checkmove, false);
				//注册end事件
				reg('end',topShow.checkend);
			}			
		},
		setwidth:function(){//设置进度条长度
			$('#TopLoadbar').css('width',topShow.w+'%');
		},
		checkscroll:function(e){//检查滚动条事件
			if(topShow.s&&!topShow.l&&$('body').scrollTop()>=3){
				topShow.show(false);
			}
		},
		checkmove:function(){//手指移动事件
			var stp=$('body').scrollTop();
			if(stp==0&&!topShow.s)
				topShow.show(true);
			else if(topShow.s&&stp>=3)
				topShow.show(false)
		},
		checkend:function(){//手指离开事件
			if(topShow.s&&!topShow.l){
				topShow.show(false);
			}
		},
		calc:function(){//计算状态
			//如果已显示，并且未加载才执行
			if(topShow.s&&!topShow.l){
				//递减
				topShow.w-=3;
				//如果宽度小于等于0时
				if(topShow.w<=0){
					//改变显示状态
					$(topShow.box).attr('class','tloading');
					//设置为加载状态
					topShow.l=true;
					//执行回调方法
					topShow.e();
				}
				else{
					//刷新新的长度
					topShow.setwidth();
					//继续动画
					Comm.run(topShow.calc);
				}
			}
		},
		//事件执行完成
		finish:function(){
			//只在加载状态中有效
			if(topShow.l){
				topShow.l=false;
				topShow.show(false);
			}
		},
		show:function(s){//显示或关闭容器
			//如果未注册事件，则不用执行下去
			if(topShow.e==null)return;
			if(s){
				//如果手势方向为向下
				if(calcDe()<0){
					//如果请求显示
					//如果已经显示或加载中则返回
					if(topShow.s||topShow.l)return;
					//注册滚动条事件
					TouchMan.reg('scroll',topShow.checkscroll);
					//标记已显示
					topShow.s=true;
					//标记未加载
					topShow.l=false;
					//初始宽度100%
					topShow.w=100;
					topShow.setwidth();
					//显示容器
					$(topShow.box).attr('class','');
					$(topShow.box).show();
					//开启动画
					topShow.calc();
				}
			}
			else{//如果请求关闭
				if(!topShow.s||topShow.l)return;
				topShow.s=false;
				TouchMan.remove('scroll',topShow.checkscroll);
				$(topShow.box).hide();
				$(topShow.box).attr('class','');
			}			
		}
	};
	
	//上拉加载
	var bottomShow={
		box:null,
		s:false,
		e:null,
		can:true,
		reg:function(e){
			bottomShow.e=e;
			//注册移动事件
			reg('move',bottomShow.calc);			
		},
		calc:function(){
			//只有在未显示状态下才执行
			if(bottomShow.can&&!bottomShow.s){
				var stp=$('body').scrollTop();
				if(stp + $(window).height() >= $(document).height()){
					bottomShow.show(true);
				}
			}
		},
		setCant:function(){
			bottomShow.init();
			bottomShow.can=false;
			bottomShow.box.innerHTML='<b>- 暂无更多数据 -<b>';
			bottomShow.show(true);
		},
		init:function(){
			if(bottomShow.box==null){
				bottomShow.box=document.createElement('div');
				bottomShow.box.innerHTML='<span><span>';
				bottomShow.box.id='BottomLoad';
				$('section').append(bottomShow.box);
			}
		},
		finish:function(){
			if(bottomShow.can)
				bottomShow.show(false);
		},
		show:function(s){
			if(bottomShow.e==null)return;
			if(s){
				//如果手势方向为向上
				if(calcDe()>0){
					//如果已经显示或窗口存在则不执行
					if(bottomShow.s||bottomShow.box)return;
					//每次初始化
					bottomShow.init();
					//标记
					bottomShow.s=true;
					//回调
					bottomShow.e();	
				}
			}
			else{
				if(bottomShow.s){
					//删除窗口
					$(bottomShow.box).remove();	
					//取消标记
					bottomShow.box=null;
					bottomShow.s=false;
				}
			}
		}
	};
	
	//开放接口
	return {
		//通用事件注册
		reg:reg,
		//通用事件清除
		remove:remove,
		//注册顶部刷新
		regtop:function(e){topShow.init(e);},
		//隐藏顶部刷新
		hidetop:topShow.finish,
		//注册底部刷新
		regbotton:function(e){bottomShow.reg(e);},
		//隐藏底部刷新
		hidebot:bottomShow.finish,
		//设置底部没有更多数据
		nomore:bottomShow.setCant
	}
};