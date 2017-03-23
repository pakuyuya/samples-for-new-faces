package org.kdk.sample.servlet.index;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.kdk.sample.util.KdkStringUtils;

/**
 * フォームデータ処理サーブレット。
 * <pre>
 * ファイルアップロードを含むFormDataのPOSTを受けて、
 * POSTされた情報を attribute["msg"]にデバッグメッセージとして設定し、
 * indexに処理移移譲します。
 * </pre>
 *
 * <h3>HTTPメソッドインタフェース</h3>
 * <dl>
 *   <dt>GET</dt>
 *   <dd>対応しません。</dd>
 *   <dt>POST</dt>
 *   <dd>GETと同様の処理を実施します。</dd>
 * </dl>
 *
 *  <h3>リクエストパラメータ</h3>
 *  <dl>
 *    <dt>field1</dt>
 *    <dd>テキスト。msgの出力に使用します。</dd>
 *    <dt>field2</dt>
 *    <dd>テキスト。msgの出力に使用します。</dd>
 *    <dt>file1</dt>
 *    <dd>ファイル。msgの出力に使用します。</dd>
 *    <dt>file2</dt>
 *    <dd>ファイル。msgの出力に使用します。</dd>
 *  </dl>
 *
 *  <h3>出力</h3>
 *  <dl>
 *    <dt>request.setAttribute("msg")</dt>
 *    <dd></dd>
 *  </dl>
 *
 *  <h3>移譲先</h3>
 *  <pre>/</pre>
 */
public class IndexPostServlet extends HttpServlet {
    /** デフォルト遷移先 */
    private static final String DEFAULT_FOWARDTO = "/";

    /**
     * index.jspフォームからのPOSTリクエストを受け持ちます。
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forwardTo = DEFAULT_FOWARDTO;

        // リクエストをパース
        RequestParameters params = parseRequestParam(request);

        // デバッグメッセージを取得
        String msg = createMsg(params);

        // msgをHTMLエスケープ後、request.attributeに設定
        // Note: 本当はライブラリとか、JSPのカスタムタグでエスケープするけど、今回は自前で簡易処理のみをする。
        String safeMsg = msg.replaceAll("&", "&amp;")
                             .replaceAll("<", "&lt;")
                             .replaceAll(">", "&gt;");

        request.setAttribute("msg", safeMsg);

        // リソースを解放
        releaseResources(params);

        request.getServletContext().getRequestDispatcher(forwardTo).forward(request, response);
    }


    /**
     * リクエストパラメータをパースします。
     * @param request リクエスト
     * @return リクエスト情報。設定されていないパラメータはnullになります。
     * @throws IOException
     * @throws ServletException
     */
    private RequestParameters parseRequestParam(HttpServletRequest request) throws IOException, ServletException {
        RequestParameters params = new RequestParameters();

        // Javaの環境設定からテンポラリディレクトリを取得
        final String tmpdir = System.getProperty("java.io.tmpdir");

        // POSTされたデータパートを分析
        for (Part part : request.getParts()) {
            if (part.getName().equalsIgnoreCase("field1")) {
                // パラメータfield1の取得

                params.setField1(KdkStringUtils.toString(part.getInputStream()));

            } else if (part.getName().equalsIgnoreCase("field2")) {
                // パラメータfield2の取得

                params.setField2(KdkStringUtils.toString(part.getInputStream()));

            } else if (part.getName().equalsIgnoreCase("file1")) {
                // パラメータfile1の取得

                if (part.getSubmittedFileName().isEmpty()) {
                    // ファイル名が未指定の場合はパラメータ指定なし
                    continue;
                }

                // テンポラリディレクトリに、アップロードファイル名そのままで保存する用のパス作成
                String filename = part.getSubmittedFileName();
                String saveto = Paths.get(tmpdir, filename).toString();

                // ファイル内容を書き出す
                part.write(saveto);

                // ファイルの参照情報を設定
                File file = new File(saveto);
                params.setFile1(file);

            } else if (part.getName().equalsIgnoreCase("file2")) {
                // パラメータfile2の取得

                if (part.getSubmittedFileName().isEmpty()) {
                    // ファイル名が未指定の場合はパラメータ指定なし
                    continue;
                }

                // テンポラリディレクトリに、アップロードファイル名そのままで保存する用のパス作成
                String filename = part.getSubmittedFileName();
                String saveto = Paths.get(tmpdir, filename).toString();

                // ファイル内容を書き出す
                part.write(saveto);

                // ファイルの参照情報を設定
                File file = new File(saveto);
                params.setFile2(file);
            }
        }

        return params;
    }

    /**
     * デバッグメッセージを作成する。
     * @param params リクエスト情報
     * @return デバッグメッセージ
     */
    private String createMsg(RequestParameters params) {
        StringBuilder sb = new StringBuilder();

        if (params.getField1() != null) {
            String text = MessageFormat.format("[field1]={0}、", params.getField1());
            sb.append(text);
        }

        if (params.getField2() != null) {
            String text = MessageFormat.format("[field2]={0}、", params.getField2());
            sb.append(text);
        }

        if (params.getFile1() != null) {
            String text = MessageFormat.format("[file1] filename={0} length={1}、", params.getFile1().getName() , params.getFile1().length());
            sb.append(text);
        }

        if (params.getFile2() != null) {
            String text = MessageFormat.format("[file2] filename={0} length={1}、", params.getFile2().getName() , params.getFile2().length());
            sb.append(text);
        }

        return sb.toString();
    }

    /**
     * 処理に使用したリソースを解放する。
     * @param request リクエスト
     */
    private void releaseResources(RequestParameters params) {
        // 一時保存したファイルの削除
        if (params.getFile1() != null && params.getFile1().isFile()) {
            params.getFile1().delete(); // Note: 失敗しても例外送出なし。本当は引数みてエラーハンドリング
        }

        // 一時保存したファイルの削除
        if (params.getFile2() != null && params.getFile2().isFile()) {
            params.getFile2().delete(); // Note: 失敗しても例外送出なし。本当は引数みてエラーハンドリング
        }
    }

    /**
     * リクエストパラメータ情報
     */
    private class RequestParameters {
        private String field1;
        private String field2;
        private File file1;
        private File file2;

        public String getField1() {
            return field1;
        }
        public void setField1(String field1) {
            this.field1 = field1;
        }
        public String getField2() {
            return field2;
        }
        public void setField2(String field2) {
            this.field2 = field2;
        }
        public File getFile1() {
            return file1;
        }
        public void setFile1(File file1) {
            this.file1 = file1;
        }
        public File getFile2() {
            return file2;
        }
        public void setFile2(File file2) {
            this.file2 = file2;
        }

    }
}
