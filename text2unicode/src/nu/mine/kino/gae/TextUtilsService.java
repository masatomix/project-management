package nu.mine.kino.gae;

import org.apache.commons.lang3.StringEscapeUtils;

public class TextUtilsService {

    public String unicode2Text(String unicode) {
        return StringEscapeUtils.unescapeJava(unicode);
    }

    public String text2Unicode(String text) {
        return StringEscapeUtils.escapeJava(text);
    }
    


    public UnicodeTextObject create(String sourece, char c) {
        UnicodeTextObject ret = new UnicodeTextObject();
        switch (c) {
        case 'u':
            String unicode2Text = unicode2Text(sourece);
            ret.setText(unicode2Text);
            ret.setUnicode(sourece);
            break;
        case 't':
            String text2Unicode = text2Unicode(sourece);
            ret.setUnicode(text2Unicode);
            ret.setText(sourece);
            break;
        default:
            ret.setText(sourece);
            ret.setUnicode(sourece);
            break;
        }
        return ret;
    }

}
