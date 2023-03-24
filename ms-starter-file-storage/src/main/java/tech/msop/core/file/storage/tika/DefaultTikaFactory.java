package tech.msop.core.file.storage.tika;

import org.apache.tika.Tika;

/**
 * 默认的tika 工厂类
 */
public class DefaultTikaFactory implements TikaFactory {
    private Tika tika;

    /**
     * 获取Tika
     *
     * @return Tika
     */
    @Override
    public Tika getTika() {
        if (tika == null) {
            tika = new Tika();
        }
        return tika;
    }
}
