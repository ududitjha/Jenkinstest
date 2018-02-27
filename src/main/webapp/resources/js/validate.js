/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function validateEmail(field, msg) {
    var val = $("#" + field).val();
    var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    
    if(val == "")
        return true;
    if(!re.test(val))
    {
        showErrorPopup("please enter valid " + msg);
        return false;
    }
    
    return true;
    
}

function highlightEmail(field, msg) {
    var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    
    if($("#" + field).val() == "")
    {
        removeErrorArray(field);
        return true;
    }
    
    if(!re.test($("#" + field).val()))
    {
        addErrorArray(field,"Please enter valid " + msg);
        return false;
    }
    
    removeErrorArray(field);
    return true;
}

function highlightBlankField(field, msg) {

    if($("#" + field).val() == "")
    {
        addErrorArray(field,"Please enter " + msg);
        return false;
    }
    removeErrorArray(field);
    return true;
}

function validateBlankField(field, msg) {

    if($("#" + field).val() == "")
    {
        showErrorPopup("Please enter " + msg);
        return false;
    }
    return true;
}

function highlightRange(field, msg, min, max) {
    var selector = $("#" + field);
    var error = msg + " length should be between " + min + " and " + max;

    if(selector.val().length < min || selector.val().length > max )
    {
        addErrorArray(field,error);
        return false;
    }
    
    removeErrorArray(field);
    return true;
}

function validateRange(field, msg, min, max) {

    var val = $("#" + field).val();

    if(val.length < min || val.length > max )
    {
        showErrorPopup(msg + " length should be between " + min + " and " + max);
        return false;
    }
    return true;
}

function highlightLength(field, msg, length) {

    var selector = $("#" + field);
    var error = msg + " length should be " + length;

    if(selector.val() == "")
    {
        removeErrorArray(field);
        return true;
    }

    if(selector.val().length != length)
    {
        addErrorArray(field,error);
        return false;
    }
    
    removeErrorArray(field);
    return true;
}

function validateLength(field, msg, length)
{
    var val = $("#" + field).val();

    if(val == "")
        return true;

    if(val.length != length)
    {
        showErrorPopup(msg + " length should be " + length);
        return false;
    }
    return true;
    
}

function highlightAlphabetOnly(field, msg) {

    var selector = $("#" + field);
    var regex = /^[A-Za-z]+$/;  
    var error = "only alphabets allowed in " + msg;

    if(!selector.val().match(regex))
    {
        addErrorArray(field,error);
        return false;
    }
    
    removeErrorArray(field);
    return true;
    
}

function validateAlphabetOnly(field, msg)
{
    var val = $("#" + field).val();
    var regex = /^[A-Za-z]+$/;  
    
    if(!val.match(regex))  
    {  
        showErrorPopup("only alphabets allowed in " + msg);
        //alert("only alphabets allowed in " + msg);
        return false;
    }  
    
    return true;
    
}

function highlightIndexOfString(field, msg, string){

    var selector = $("#" + field);
    var error = msg;

    if(selector.val() == undefined)
    {
        removeErrorArray(field);
        return true;
    }

    if(selector.val().indexOf(string)!=-1)
    {
        addErrorArray(field,error);
        return false;
    }
    
    removeErrorArray(field);
    return true;
    
}

function validateIndexOfString(field, msg, string)
{
    var val = $("#" + field).val();
    if(val == undefined)
        return true;
    if(val.indexOf(string)!=-1)  
    {  
        showErrorPopup(msg);
        return false;
    }
    return true;
}

function maxLengthCheck(object)
{
    if (object.value.length > object.maxLength)
        object.value = object.value.slice(0, object.maxLength)
}
