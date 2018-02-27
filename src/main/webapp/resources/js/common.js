/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function removeErrorArray(_field)
{
    $.each(errArray, function(i){
        if(errArray[i].field === _field) {
            errArray.splice(i,1);
            return false;
        }
    });
}

function addErrorArray(_field, _msg)
{
    var result = $.grep(errArray, function(e){
        return e.field == _field;
    });

    if(result.length == 0)
    {
        errArray.push({
            field:_field,
            msg:_msg
        });
    }
}

function showError()
{
    $("input").removeClass("error");
    $("select").removeClass("error");
    
    var errHTML="";
    $( errArray ).each(function( index ) {
        if(index>0)
            errHTML +="<br/>";
        errHTML+= "* " + this.msg ;
        $("#" + this.field).addClass("error");        
    });
    
    if(errHTML=="")
        $("#divError").hide();
    else
        $("#divError").html(errHTML).show();
}

function LoadFromLocalStorage(key, defaultval) {
    if (!defaultval)
        defaultval = "";
    var v = window.localStorage.getItem("'" + key + "'");
    return v ? v : defaultval;
}

function SaveInLocalStorage(key, val) {
    if (typeof (localStorage) != 'undefined') {
        window.localStorage.removeItem("'" + key + "'");
        window.localStorage.setItem("'" + key + "'", val);
        return true;
    }
    else {
        alert("Your browser does not support local storage, please upgrade to latest browser");
        return false;
    }
}

function RemoveFromLocalStorage(key) {
    window.localStorage.removeItem("'" + key + "'");
}



function getQueryParams(key) {
    var re=new RegExp('(?:\\?|&)'+key+'=(.*?)(?=&|$)','gi');
    var r=[], m;
    while ((m=re.exec(document.location.search)) != null) r[r.length]=m[1];
    return r;
}


function showErrorPopup(msg)
{
    $("#spanErrorMsg").html(msg);
    lightbox_Error();
}


function getCookie(c_name)
{
    var i, x, y, ARRcookies = document.cookie.split(";");

    for (i = 0; i < ARRcookies.length; i++)
    {
        x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
        y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
        x = x.replace(/^\s+|\s+$/g, "");
        if (x === c_name)
        {

            return unescape(y);
        }
    }
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}

function eraseAllCookie()
{
    var cookies = document.cookie.split(";");
    for (var i = 0; i < cookies.length; i++)
        eraseCookie(cookies[i].split("=")[0]);
}

function createCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        if(name!="serviceName"){
            document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;domain=.staging.nkn.in;";
        }
    }
}

function deleteCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;domain=.staging.nkn.in;";
        
    }
}