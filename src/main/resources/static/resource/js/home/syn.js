$(function () {
    $('#syn_button').off('click').on('click', function () {

        // const {value: password} = await swal({
        //     title: 'Enter your password',
        //     input: 'password',
        //     inputPlaceholder: 'Enter your password',
        //     inputAttributes: {
        //         maxlength: 10,
        //         autocapitalize: 'off',
        //         autocorrect: 'off'
        //     }
        // });
        // if (password) {
        //     swal('Entered password: ' + password)
        // }

        swal({
            type: 'info',
            title: '请输入执行码进行操作!',
            // text: "You won't be able to revert this!",
            // type: 'warning',
            input: 'password',
            inputPlaceholder: '输入执行码...',
            showCancelButton: true,
            showLoaderOnConfirm: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            preConfirm: function(password) {
                console.log(password)
                return new Promise(function(resolve, reject) {
                    setTimeout(function() {
                        if (password) {
                            resolve();
                        } else {
                            reject('执行码错误！');
                        }
                    }, 2000);
                });
            },
            allowOutsideClick: false
        }).then(function(password) {
            var code = password.value;
            $.ajax({
                type: "POST",
                data: {"code":code},
                async:false,
                url: baseUrl + '/autoSyn',
                // data: {"page":5},
                success: function(data){
                    // console.log(data);
                    swal(
                        '自动同步成功!',
                        'Sonar规则已经自动同步完成.',
                        'success'
                    ).then(function (isConfirm) {
                        location.href = location.href;
                    });
                },
                complete: function () {
                    // $.bootstrapLoading.end();
                }
                // error: function (result) {
                //     console.log(result);
                //     console.log(result.status);
                //     console.log(result.responseJSON.message);
                // }
            });
        });



        // swal({
        //     type: 'info',
        //     title: '请输入执行码进行操作!',
        //     // text: "You won't be able to revert this!",
        //     // type: 'warning',
        //     input: 'password',
        //     inputPlaceholder: '输入执行码...',
        //     showCancelButton: true,
        //     showLoaderOnConfirm: true,
        //     confirmButtonColor: '#3085d6',
        //     cancelButtonColor: '#d33',
        //     // showLoaderOnConfirm: true,
        //     confirmButtonText: '确定',
        //     cancelButtonText: '取消'
        // }).then(function (isConfirm) {
        //     // console.log(isConfirm)
        //     if (isConfirm.dismiss) {
        //         // swal(
        //         //     '取消',
        //         //     '',
        //         //     'error'
        //         // );
        //         return;
        //     }
        //     if (isConfirm) {
        //         var code = isConfirm.value;
        //         // console.log(code);
        //         // $.bootstrapLoading.start({ loadingTips: "正在处理数据，请稍候..." });
        //         setTimeout(function(){
        //             // 方法B()
        //         },5000);
        //         $.ajax({
        //             type: "POST",
        //             data: {"code":code},
        //             async:false,
        //             url: baseUrl + '/autoSyn',
        //             // data: {"page":5},
        //             success: function(data){
        //                 // console.log(data);
        //                 swal(
        //                     '自动同步成功!',
        //                     'Sonar规则已经自动同步完成.',
        //                     'success'
        //                 ).then(function (isConfirm) {
        //                     location.href = location.href;
        //                 });
        //             },
        //             complete: function () {
        //                 // $.bootstrapLoading.end();
        //             }
        //             // error: function (result) {
        //             //     console.log(result);
        //             //     console.log(result.status);
        //             //     console.log(result.responseJSON.message);
        //             // }
        //         });
        //     }
        // })
    })
});