$(function() {
    // alert(baseUrl);

    $.ajax({
        type: "POST",
        url: baseUrl + '/getAllUser',
        // data: {"page":5},
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