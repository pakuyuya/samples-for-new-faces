package org.kdk.sample.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KdkStringUtils {
    /**
     * 入力された InputStream の内容をUTF-8の文字列としてすべて読み進め、内容を文字列として返却します。
     * @param is 入力ストリーム
     * @return 結果
     * @throws IOException
     */
    public static String toString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();

        InputStreamReader reader = new InputStreamReader(is);
        char buf[] = new char [4096];
        int numRead;
        while (0 <= (numRead = reader.read(buf))) {
            sb.append(buf, 0, numRead);
        }
        return sb.toString();
    }
}
