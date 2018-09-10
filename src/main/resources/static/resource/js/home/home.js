$(function() {
    // alert(baseUrl);

    $.ajax({
        type: "POST",
        url: baseUrl + '/history',
        // data: {"page":5},
        success: function(data){
            $('#historyList').html(data);
            console.log(data);
        },
        // error: function (result) {
        //     console.log(result);
        //     console.log(result.status);
        //     console.log(result.responseJSON.message);
        // }
    });

    $.ajax({
        type: "POST",
        url: baseUrl + '/latest',
        // data: {"page":5},
        success: function(data){
            $('#latest').html(data);
            console.log(data);
        },
        // error: function (result) {
        //     console.log(result);
        //     console.log(result.status);
        //     console.log(result.responseJSON.message);
        // }
    });
});