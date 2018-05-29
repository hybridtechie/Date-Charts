package me.nithin.james.freqchart;

import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StringUtils {
    private static StandardToStringStyle toStringStyle = null;

    public static ToStringStyle defaultToStringStyle() {
        if (toStringStyle == null) {
            toStringStyle = new StandardToStringStyle();
            toStringStyle.setFieldSeparator(", ");
            toStringStyle.setUseClassName(false);
            toStringStyle.setUseIdentityHashCode(false);
            toStringStyle.setContentStart("{");
            toStringStyle.setContentEnd("}");
            toStringStyle.setFieldNameValueSeparator(": ");
            toStringStyle.setArrayStart("[");
            toStringStyle.setArrayEnd("]");
        }

        return toStringStyle;
    }
}
