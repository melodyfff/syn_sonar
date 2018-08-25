$(function() {
    // alert(baseUrl);

    $.ajax({
        type: "POST",
        url: baseUrl + '/getAllUser',
        success: function(data){
            $('#userList').html(data);
            console.log(data);
        },
        // error: function (result) {
        //     console.log(result);
        //     console.log(result.status);
        //     console.log(result.responseJSON.message);
        // }
    });
});