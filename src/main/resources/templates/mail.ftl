<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Sonar规则自动检测</title>
    <style type="text/css">
        body {
            font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
            color: #4f6b72;
            background: #E6EAE9;
        }

        a {
            color: #c75f3e;
        }

        #mytable {
            width: 700px;
            padding: 0;
            margin: 0;
        }

        caption {
            padding: 0 0 5px 0;
            width: 700px;
            font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
            text-align: right;
        }

        th {
            font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
            color: #4f6b72;
            border-right: 1px solid #C1DAD7;
            border-bottom: 1px solid #C1DAD7;
            border-top: 1px solid #C1DAD7;
            letter-spacing: 2px;
            text-transform: uppercase;
            text-align: left;
            padding: 6px 6px 6px 12px;
            background: #CAE8EA url(images/bg_header.jpg) no-repeat;
        }

        th.nobg {
            border-top: 0;
            border-left: 0;
            border-right: 1px solid #C1DAD7;
            background: none;
        }

        td {
            border-right: 1px solid #C1DAD7;
            border-bottom: 1px solid #C1DAD7;
            background: #fff;
            font-size: 11px;
            padding: 6px 6px 6px 12px;
            color: #4f6b72;
        }

        td.alt {
            background: #F5FAFA;
            color: #797268;
        }

        th.spec {
            border-left: 1px solid #C1DAD7;
            border-top: 0;
            background: #fff url(images/bullet1.gif) no-repeat;
            font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
        }

        th.specalt {
            border-left: 1px solid #C1DAD7;
            border-top: 0;
            background: #f5fafa url(images/bullet2.gif) no-repeat;
            font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
            color: #797268;
        }

        /*---------for IE 5.x bug*/
        html > body td {
            font-size: 11px;
        }
    </style>
</head>
<body>

<div align="center" class="login">
    <p> Sonar规则对比结果: </p>
    <table id="mytable" cellspacing="0" summary="The technical specifications of the Apple PowerMac G5 series">
        <caption></caption>
        <tr>
            <th scope="col" abbr="规则名称" class="nobg">规则名称</th>
            <th scope="col" abbr="对比结果">对比结果</th>
            <th scope="col" abbr="Dual 2">规则差异个数</th>
        </tr>
        <#list mailModel as model>
            <tr>
                <th scope="row" abbr="Model" class="spec">${model.ruleName}</th>

                <#if model.result == "一致">
                    <td style="color:green">${model.result}</td>
                </#if>
                <#if model.result != "一致">
                    <td style="color:red">${model.result}</td>
                </#if>
                <#if model.number == 0>
                    <td style="color:green">${model.number}</td>
                </#if>
                <#if model.number != 0>
                    <td style="color:red">${model.number}</td>
                </#if>
            </tr>
        </#list>
    <#--<tr>-->
    <#--<th scope="row" abbr="Model" class="spec">Java</th>-->
    <#--<td>一致</td>-->
    <#--<td>0</td>-->
    <#--</tr>-->
    </table>
    <p> 查看规则对比详情请访问 <a href='#'>x.x.x</a></p>
</div>
<!-- <div align="right">
        <p> 此致 </p>
        <p> 敬礼 </p>
        <p>祝您工作、生活愉快~~~</p>
</div> -->
</body>
</html>