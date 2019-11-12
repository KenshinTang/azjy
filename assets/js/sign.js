var Sign=new function(){
  var days=30;//最大只能30
  var sp='/';
  //now
  function getnow(){
    var t=new Date();
    return new Date(t.getFullYear()+sp+(t.getMonth()+1)+sp+t.getDate());
  }
  //Distance
  function getDistance(last,now){
    now=now|| getnow();
    return Math.floor((now.getTime()-last.getTime())/(24*60*60*1000));
  }
  //show
  this.show=function(data,time){
    if(!time || !data)return 0;
    var m=getDistance(new Date(time));
    if(m>days)return 0;
    if(m>0)
      data>>=m;
    return data.toString(2).replace(/0/g,'').length;
  }
  //设置成功返回data对象，如果无修改则返回null
  this.setSin=function (data,time,now){
    var r={data:0,time:0},x=1<<(days-1),last,m=-1;
    now= now?new Date(now):getnow();
    if(time>0){
      last=new Date(time);
      m=getDistance(last,now);
    }
    if(time==getnow().getTime()){
      now=getnow();
    }
    else{
      if(m<days&&m>0)
        data=(data>>m)|x;
      else if (m==0)
        return null;
      else
        data=x;
    }
    return {data:data,time:now.getTime()};
  }
}