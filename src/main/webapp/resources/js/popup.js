//  Prateek Jain, Shounak Acharya, Amit Kumar (National Knowledge Network | National Informatics Centre) 

window.document.onkeydown = function (e)
{
    if (!e){
        e = event;
    }
    if (e.keyCode == 27){
        lightbox_close();
    }
}



function lightbox_RenameDevice(){
    window.scrollTo(0,0);
    document.getElementById('popUpRename').style.display='block';
    document.getElementById('fade').style.display='block';  
}
function lightbox_open(){
    window.scrollTo(0,0);
    document.getElementById('popUpMigrate').style.display='block';
    document.getElementById('fade').style.display='block';  
}

function lightbox_ChangePassword(){
    window.scrollTo(0,0);
    document.getElementById('popUpChangePassword').style.display='block';
    document.getElementById('fade').style.display='block';  
}

function lightbox_OtpVerify(){
    window.scrollTo(0,0);
    document.getElementById('popUpOtpVerify').style.display='block';
    document.getElementById('fade').style.display='block';  
}

function lightbox_Error(){
    window.scrollTo(0,0);
    document.getElementById('divErrorPopup').style.display='block';
    document.getElementById('fade').style.display='block';  
}



function lightbox_close(){
    var lightbox = document.getElementsByClassName('light');
    console.log(lightbox.length );
    for (var i = 0; i < lightbox.length; i ++) {
        lightbox[i].style.display = 'none';
    }
    document.getElementById('fade').style.display='none';
}


function loadingImg(){
    window.scrollTo(0,0);
    document.getElementById('loader').style.display='block';
    document.getElementById('fadeLoader').style.display='block';  
}