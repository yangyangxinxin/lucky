package com.luckysweetheart.ocr;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 压缩图片
 * Created by yangxin on 2017/8/11.
 */
@Service
public class ThumbnailUtilService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int WIDTH = 450;

    /**
     * 宽固定为450，按照比例进行压缩。
     *
     * <br/>
     * <code>
     * 宽 > 高的情况
     * - - - - - - - - -
     * | - - - - - - - |
     * - - - - - - - - -
     *
     * 比如，一张1000(w) * 600(h) 的一张图片，宽高比例为5/3,宽固定为450，则压缩后的图片高为450 / (5/3) = 270
     * 最后这张图片的大小为450(w) * 270(h)
     *
     *
     * 高 > 宽的情况
     * - - -
     * | - |
     * | - |
     * | - |
     * | - |
     * | - |
     * | - |
     * - - -
     *
     * 一张600(w) * 1000(h) 的图片，宽高比例为 6/10 = 0.6,压缩后的高为450/0.6=750
     * 则最后的图片大小为 450(w) * 750(h)
     *
     * </code>
     * @param sourceFile
     * @return
     * @throws IOException
     */
    public byte[] createThumbnail(byte[] sourceFile) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(sourceFile);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(inputStream).size(WIDTH, getHeight(sourceFile)).toOutputStream(outputStream);

        byte[] bytes = outputStream.toByteArray();
        inputStream.close();
        outputStream.close();
        return bytes;
    }

    private int getHeight(byte[] bytes) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage buff = ImageIO.read(inputStream);

        int width = buff.getWidth(); // 得到图片的宽度
        int height = buff.getHeight(); // 得到图片的高度

        inputStream.close();

        if (width < WIDTH) {
            return height;
        }

        double a = (double) width / (double) height;

        height = (int) (WIDTH / a);

        return height;
    }
}
