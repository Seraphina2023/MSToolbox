package tech.msop.core.file.storage.tika;

import org.apache.tika.Tika;

/**
 * Tika 工厂类接口
 */
public interface TikaFactory {

    /**
     * 获取Tika
     *
     * @return Tika
     */
    Tika getTika();
}
