var A_INPUT_ID = "#aInput"
var B_INPUT_ID = "#bInput"
var RESULT_WRAPPER_ID = "#resultWrapper"


function doSolve() {
  var aReq = getRequestFromInput(A_INPUT_ID);
  var bReq = getRequestFromInput(B_INPUT_ID);
  var requestDesc = {}
  requestDesc.data = {
    "a" : aReq,
    "b" : bReq
  }
  requestDesc.type = "POST"
  requestDesc.success = onResult
  requestDesc.url = "process.do"
  $.ajax(requestDesc);
}

function onResult(data) {
  $(RESULT_WRAPPER_ID).html("[ " + data.replace(",", ", ") + " ] ")
}

function getRequestFromInput(inputId) {
  var aMatrixString = $(inputId).val()

  var aMatrixReqString = "";
  var aItems = aMatrixString.split(/[ \n\t]+/)
  for(var i in aItems) {
    var itemString = aItems[i];
    if(itemString.match(/^[0-9]*.?[0-9]+$/) == null) {
      continue;
    }
    aMatrixReqString += itemString + ",";
  }
  return aMatrixReqString.substr(0, aMatrixReqString.length - 1)

}