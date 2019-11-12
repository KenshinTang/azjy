var fullAreaObj = {};

function pushFullAreaObj() {
  for (var i in _area) {
    fullAreaObj[_area[i].i] = _area[i];
    for (var ii in _area[i].c) {
      fullAreaObj[_area[i].c[ii].i] = _area[i].c[ii];
      for (var iii in _area[i].c[ii].c) {
        fullAreaObj[_area[i].c[ii].c[iii].i] = _area[i].c[ii].c[iii];
      }
    }
  }
}

function getFullAreaName(id) {
  var arr1 = fullAreaObj[id]["if"].split("/");
  var p, c, d;
  
  if (arr1[0] == 0) {
    p = "钓鱼岛";
    c = "";
    d = "";
  } else {
    p = fullAreaObj[arr1[0]].n;
    c = fullAreaObj[arr1[1]].n;
    d = fullAreaObj[arr1[2]].n;
  }
  
  return p + " " + c + " " + d;
}

function getFullAreaName2(id) {
  var arr1 = fullAreaObj[id]["if"].split("/");
  var p;
  console.log(arr1);
  
  if (arr1[2] == 900000) {
    p = "钓鱼岛";
  } else {
    p = fullAreaObj[arr1[2]].n;
  }
  
  return p;
}