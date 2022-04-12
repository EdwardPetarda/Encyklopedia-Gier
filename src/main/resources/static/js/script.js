function showEditForm(commentId){

    for (let i = 0; i < document.getElementsByClassName("box" + commentId).length; i++) {
        document.getElementsByClassName("box" + commentId).item(i).hidden=true;
    }
    document.getElementById("form"+commentId).hidden=false;

}


function showRateForm(){

    if(document.getElementById("rateForm").hidden===true) {
        document.getElementById("div-rate").style.width = "240px";
        document.getElementById("rateForm").hidden = false;
        document.getElementById("rateButton").hidden=true;
    }
    else {
        document.getElementById("rateForm").hidden = true;
        document.getElementById("rateButton").innerText = "Dodaj ocene";
        document.getElementById("div-rate").style.width = "auto";
        document.getElementById("rateButton").hidden=false;
    }
}