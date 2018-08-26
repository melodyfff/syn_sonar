$(function () {
    $('#syn_button').off('click').on('click', function () {

        swal({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            // showLoaderOnConfirm: true,
            confirmButtonText: 'Yes, delete it!'
        }).then(function (isConfirm) {
            console.log(isConfirm)
            if (isConfirm.dismiss) {
                swal(
                    '取消',
                    '',
                    'error'
                );
                return;
            }
            if (isConfirm) {
                $.ajax({
                    type: "POST",
                    async:false,
                    url: baseUrl + '/autoSyn',
                    // data: {"page":5},
                    success: function(data){
                        console.log(data);
                        swal(
                            '自动同步成功!',
                            'Sonar规则已经自动同步完成.',
                            'success'
                        );
                    },
                    // error: function (result) {
                    //     console.log(result);
                    //     console.log(result.status);
                    //     console.log(result.responseJSON.message);
                    // }
                });
            }
        })
    })
});