package tech.msop.core.cloud.version;


import lombok.Getter;
import org.springframework.http.MediaType;

/**
 * MS 媒体类型：application/vnd.ms.VERSION+json
 *
 * <p>
 * <a href="https://developer.github.com/v3/media/">...</a>
 * </p>
 *
 * @author ruozhuliufeng
 */
@Getter
public class MsMediaType {
    private static final String MEDIA_TYPE_TEMP = "application/vnd.%s.%s+json";
    private final String appName = "ms";
    private final String version;
    private final MediaType mediaType;

    public MsMediaType(String version) {
        this.version = version;
        this.mediaType = MediaType.valueOf(String.format(MEDIA_TYPE_TEMP, appName, version));
    }

    @Override
    public String toString() {
        return mediaType.toString();
    }
}
