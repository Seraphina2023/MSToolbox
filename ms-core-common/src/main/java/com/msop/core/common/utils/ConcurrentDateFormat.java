package com.msop.core.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * 参考Tomcat8 中的并发DateFormat
 * <p>
 * {@link SimpleDateFormat}的线程安全包装器。
 * 不使用ThreadLocal，创建足够的SimpleDateFormat对象来满足并发性要求。
 * </p>
 *
 * @author ruozhuliufeng
 */
public class ConcurrentDateFormat {
    private final String format;
    private final Locale locale;
    private final TimeZone timeZone;
    private final Queue<SimpleDateFormat> queue = new ConcurrentLinkedQueue<>();

    private ConcurrentDateFormat(String format, Locale locale, TimeZone timeZone) {
        this.format = format;
        this.locale = locale;
        this.timeZone = timeZone;
        SimpleDateFormat initial = createInstance();
        queue.add(initial);
    }

    public static ConcurrentDateFormat of(String format) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), TimeZone.getDefault());
    }

    public static ConcurrentDateFormat of(String format, TimeZone timeZone) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), timeZone);
    }

    public static ConcurrentDateFormat of(String format, Locale locale, TimeZone timeZone) {
        return new ConcurrentDateFormat(format, locale, timeZone);
    }

    public String format(Date date) {
        SimpleDateFormat simpleDateFormat = queue.poll();
        if (simpleDateFormat == null) {
            simpleDateFormat = createInstance();
        }
        String result = simpleDateFormat.format(date);
        queue.add(simpleDateFormat);
        return result;
    }

    public Date parse(String source) throws ParseException {
        SimpleDateFormat simpleDateFormat = queue.poll();
        if (simpleDateFormat == null) {
            simpleDateFormat = createInstance();
        }
        Date result = simpleDateFormat.parse(source);
        queue.add(simpleDateFormat);
        return result;
    }


    private SimpleDateFormat createInstance() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, locale);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat;
    }

}
