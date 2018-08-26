$(function(){
    // 设置CSRF
    // var token = $("meta[name='_csrf']").attr("content");
    // var header = $("meta[name='_csrf_header']").attr("content");
    // $(document).ajaxSend(function(e, xhr, options) {
    //     xhr.setRequestHeader(header, token);
    // });

    // 设置jQuery Ajax全局的参数
    $.ajaxSetup({
        type: "POST",
        error: function(result){
            switch (result.status){
                case(500):
                    swal({
                        type: 'error',
                        title: 'Oops...',
                        text: result.responseJSON.message
                    });
                    break;
                case(401):
                    alert("未登录");
                    break;
                case(403):
                    swal({
                        type: 'error',
                        title: '无权限执行此操作',
                        text: '请联系管理员!'
                    });
                    break;
                case(405):
                    swal({
                        type: 'error',
                        title: '错误的请求',
                    });
                    break;
                case(408):
                    swal({
                        type: 'error',
                        title: '请求超时',
                        text: '请联系管理员!'
                    });
                    break;
                default:
                    swal({
                        type: 'error',
                        title: '系统错误',
                        text: result.responseJSON.message
                    });
            }
        },
        success: function(data){
            swal(
                '操作成功!',
                'success'
            )
        }
    });
});