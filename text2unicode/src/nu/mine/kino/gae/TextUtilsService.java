package nu.mine.kino.gae;

import org.apache.commons.lang3.StringEscapeUtils;

public class TextUtilsService {

    public String unicode2Text(String unicode) {
        return StringEscapeUtils.unescapeJava(unicode);
    }

    public String text2Unicode(String text) {
        return StringEscapeUtils.escapeJava(text);
    }

    public String text2URLEncode(String text, String encoding) {
        return StringEscapeUtils.escapeJava(text);
    }

    // public String text2(String text, String encoding) {
    // return StringEscapeUtils.escapeJava(text);
    // }

    public UnicodeTextObject create(String source, char c) {
        UnicodeTextObject ret = new UnicodeTextObject();
        switch (c) {
        case 'u':
            String unicode2Text = unicode2Text(source);
            ret.setText(unicode2Text);
            ret.setUnicode(source);
            break;
        case 't':
            String text2Unicode = text2Unicode(source);
            ret.setUnicode(text2Unicode);
            ret.setText(source);
            break;
        default:
            ret.setText(source);
            ret.setUnicode(source);
            break;
        }
        return ret;
    }

}
