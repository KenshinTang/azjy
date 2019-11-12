function Push(box,config) {
	var a=this;
    var startY=0,diff=0,scb,list,topbox,topc,bottom,distance=50,top_event=null,bottom_event=null,handle=0,lastsct=0,events=false,pausevalue=false;
    var state={t:4,b:4};//0.默认提示，1松手（没有数据），2，加载中,4无
    var loadstr='<span class="push_loading">加载中，请稍候...</span>';
    //private
    function createDIV(html,cn){var d=document.createElement('DIV');if(cn)d.className=cn;if(html)d.innerHTML=html;return d;}
    function event_touchstart(e){;
        if(!events||e.touches.length>1||pausevalue)return;
        clsend();
        setTimeout(function(){
            setAni(false);
        },20);
        startY = e.touches[0].pageY;
        olddiff=diff=0;
    }
    function event_touchmove(e){
        if(!events||e.touches.length>1||pausevalue)return;
        if(scb.scrollTop>0){
            if(state.b==0&&scb.offsetHeight!=scb.scrollHeight){
                calcButton();
            }
        }
        else if(state.t!=4){
            diff =e.touches[0].pageY- startY ;
            if(diff<0||state.t==2)return;
            e.preventDefault();
            e.stopPropagation();
            diff = Math.pow(diff, 0.8);
            topbox.style.height=diff+'px';
            //
            if (diff > distance) {
                if(state.t==0){
                    state.t=1;
                    topc.className='push_arrow push_up';
                    topc.innerHTML=getHtml(true);
                }
            } else {
                if(state.t==1){
                    state.t=0;
                    topc.className='push_arrow push_down';
                    topc.innerHTML=getHtml(true);
                }
            }
        }
    }
    function event_touchend(e){
        if(!events||e.touches.length>0||pausevalue)return;
        setAni(true);
        if(state.t==1){
            state.t=2;
            topc.className='';
            topc.innerHTML=getHtml(true);
            setTimeout(function(){
            		if(top_event){
                		top_event(a);
            			if(bottom_event)
            				state.b=0;
            		}
            },30);
        }
        if(topbox&&topbox.offsetHeight>0){
            topbox.style.height=(state.t==2?distance:0)+'px';
        }
        else{
            if(scb.offsetHeight!=scb.scrollHeight&&!calcButton()){
                if(state.b==0&&bottom_event){
                    lastsct=scb.scrollTop;
                    handle=setInterval(calcend,100);
                }
            }
        }
    }
    function calcButton(){
        if(scb.scrollTop+scb.offsetHeight+5>=scb.scrollHeight){
            showBottom();
            return true;
        }
        return false;
    }
    function clsend(){
        if(handle!=0){
            clearInterval(handle);
            handle=0;
        }
    }
    function calcend(){
        if(lastsct<scb.scrollTop)
            lastsct=scb.scrollTop;
        else{
            clsend();
            calcButton();
        }
    }
    function addTop(box,show){
        topbox=createDIV('',show?'push_topbox':'');
        box.appendChild(topbox);
        if(show){
        		state.t=0;
        		var temp=createDIV('<span class="push_arrow push_down">'+getHtml(true)+'</span>','push_topcontent');
        		topbox.appendChild(temp);
        		topc=temp.querySelector('span');
        }
    }
    function addBottom(box,show){
    		state.b=0;
        bottom=createDIV(show?getHtml(false):'',show?'push_bottombox':'');
        box.appendChild(bottom);
        if(show){
        		bottom.onclick=showBottom;
        }
        else
        		state.b=4;
    }
    function getHtml(istop){
        return istop?(['下拉刷新','松手加载',loadstr][state.t]):([(scb.offsetHeight==scb.scrollHeight?'点击加载更多':'拉到底加载更多'),'已到底部',loadstr][state.b]);
    }
    function endtop(){
        if(state.t==2){
            setAni(true);
            state.t=0;
            setTimeout(function(){
                topc.className='push_arrow push_down';
                topc.innerHTML=getHtml(true);
            },300);
            topbox.style.height='0px';
        }
    }
    function showBottom(){
        if(state.b==0&&!pausevalue){
            state.b=2;
            bottom.innerHTML=getHtml(false);
            setTimeout(function(){
                bottom_event&&bottom_event(a);
            },30);
        }
    }
    //处理动画
    function setAni(s){
        if(topbox){topbox.style.transition = s? "height 0.3s ease-out":"";}
    }
    //g
    function g(o){return document.getElementById(o);}
    //public
    //初始化config={top:下拉事件,bottom:上拉事件,scroll:是否显示滚动条:默认false,herder:固定头部内容ID，footer:固定脚部内容ID}
    a.init=function(box,config){
        if(box=g(box)){
            box.style.overflow='hidden';
            scb=createDIV('','push_list');
            //top
            var show=false;
            if(config&&config.top){top_event=config.top;show=true;}
            addTop(scb,show);
            box.appendChild(scb);
            //header
            if(config&&config.header){
            		var h=g(config.header);
            		if(h){
            			h.parentNode.removeChild(h);
            			scb.appendChild(h);
            		}
            		else{
            			scb.insertAdjacentHTML('beforeend',config.header);
            		}
            }
            //list
            list=createDIV();
            list.className='push_content';
            scb.appendChild(list);
            show=false;
            //footer
            if(config&&config.footer){
            		var f=g(config.footer);
            		if(f){
            			f.parentNode.removeChild(f);
            			scb.appendChild(f);
            		}
            		else{
            			scb.insertAdjacentHTML('beforeend',config.footer);
            		}
            }
            //bottom
            if(config&&config.bottom){
                bottom_event=config.bottom;
                show=true;
                scb.onresize=function(){if(bottom_event&&bottom)bottom.innerHTML=getHtml(true);}
            }
            addBottom(scb,show);
            //scrool
            if(config&&!config.scroll){
            		scb.className='push_list push_drag';
            }
            //event
            if(events=(!!top_event)||(!!bottom_event)){
                box.addEventListener("touchstart",event_touchstart);
                box.addEventListener("touchmove",event_touchmove);
                box.addEventListener("touchend",event_touchend);
            }
        }
    };
    //移除已注册事件
    a.removeEvent=function(istop){
        if(istop){
            if(top_event){
                top_event=null;
                state.t=4;
                topbox.className=topbox.innerHTML='';
            }
        }
        else{
            if(bottom_event){
                bottom_event=null;
                state.b=4;
                bottom.className=bottom.innerHTML='';
                scb.onresize=null;
            }
        }
        events=(!!top_event)||(!!bottom_event);
    };
    //没有更多数据，底部加载功能失效
    //必须在fillHtml 之后执行，以防止状态重置（第一页）
    a.noMore=function(dontshow){
        if(state.b==1)return;
        state.b=1;
        if(dontshow)
        		bottom.style.display='none';
        	else
        		bottom.innerHTML=getHtml(false);
    };
    //填充代码append：是否追加，默认为false
    a.fillHtml=function(html,append){
        if(list==null)return;
        if(append){
        		list.insertAdjacentHTML('beforeend',html);
        }
        else{
        		scb.scrollTop=0;
        		list.innerHTML=html;
        		state.b=2;
        }
        this.clear();
    };
    //填充代码,并自动处理分页数据，需要会话服务返回的原始对象（分页信息）
    a.Fill=function(html,dat){
        this.fillHtml(html,dat.curPage>1);
        if(Math.ceil(dat.totalCount/dat.pageSize)>=dat.curPage)
            this.noMore();
    };
    //清除加载效果(返回：是否继续执行)
    a.clear=function(){
        if(list==null)return true;
        endtop();
        bottom.style.display='block';
        if(state.b==2){
            state.b=0;
            bottom.innerHTML=getHtml(false);
        }
        return false;
    };
    //设置暂停功能，，如果value为true,各种推拉事件不激活
    a.pause=function(value){
        pausevalue=!!value;
    };
    //获取内部容器
    a.getBox=function(){
        return scb;
    };
    a.init(box, config);
}