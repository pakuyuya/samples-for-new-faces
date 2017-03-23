package org.kdk.sample.servlet.index;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * index.jspを表示するServletです。<br>
 *
 * <h3>HTTPメソッドインタフェース</h3>
 * <dl>
 *   <dt>GET</dt>
 *   <dd>index.jspを表示します。</dd>
 *   <dt>POST</dt>
 *   <dd>GETと同様の処理を実施します。</dd>
 * </dl>
 *
 *  <h3>出力</h3>
 *  <pre>なし</pre>
 *
 *  <h3>移譲先</h3>
 *  <pre>index.jsp</pre>
 */
public class IndexViewServlet extends HttpServlet {
    /** デフォルト遷移先 */
    private static final String DEFAULT_FOWARDTO = "/WEB-INF/page/index.jsp";

    /**
     * index.jspを表示します。
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forwardTo = DEFAULT_FOWARDTO;

        request.getServletContext().getRequestDispatcher(forwardTo).forward(request, response);
    }

    /**
     * GETと同様の処理を実施します。
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
