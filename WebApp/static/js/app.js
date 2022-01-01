
$(document).ready(function () {
    $("#verificationSection").hide();
    $("#submitPhoneNoBtn").click(function () {
        $("#phoneNumberSection").hide();
        $("#verificationSection").show();
    });

    $("#editPhoneBtn").click(function () {
        $("#phoneNumberSection").show();
        $("#verificationSection").hide();
    });

});