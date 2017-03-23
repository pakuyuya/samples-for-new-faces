<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<head>
  <title>ファイルアップロードサンプル</title>
  <meta charset="UTF-8">
</head>
<body>

<%-- attributeにmsgが指定されていた場合のみ、msgを表示する --%>
<% if (request.getAttribute("msg") != null) { %>
<p>
  ${msg}
</p>
<% } %>

<%-- フォーム --%>
<%-- ポイント：「method="POST" enctype="multipart/form-data"」は必須。 --%>
<form action="${pageContext.request.contextPath}/post" method="POST" enctype="multipart/form-data">
  <table>
    <tr>
      <th>項目 filed1</th>
      <td><input type="text" name="field1"></td>
    </tr>
    <tr>
      <th>項目 filed2</th>
      <td><input type="text" name="field2"></td>
    </tr>
    <tr>
      <th>ファイル１</th>
      <td><input type="file" name="file1"></td>
    </tr>
    <tr>
      <th>ファイル２</th>
      <td><input type="file" name="file2"></td>
    </tr>
    <tr>
      <td colspan="2">
        <button type="submit">送信します</button>
      </td>
    </tr>
  </table>
</form>

</body>
</html>