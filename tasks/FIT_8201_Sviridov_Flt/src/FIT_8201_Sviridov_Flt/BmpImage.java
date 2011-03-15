package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author alstein
 */
public class BmpImage extends BufferedImage {

    public BmpImage(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_RGB);
    }

    static public BmpImage readBmpImage(File file) throws FileNotFoundException, IOException {
        /*
        BMP
        'B''M' 2b|filesize 4b|-- 4b|offset 4b

        DIB
        size of header 4b|width 4b|height 4b|
        num of color planes 2b|num of bits per pixel 2b|
        compression 4b|image size 4b| hor res 4b|
        ver res 4b|num of colors in color palette 4b|
        num of imp. colors 4b*/
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        IllegalArgumentException exc = new IllegalArgumentException();

        int file_size, offset, header_size, width, height, num_of_color_planes,
                bits_per_pixel, compression, image_size, hor_res, ver_res,
                num_of_colors, num_of_imp_color;


        // magic

        if (ledis.readUnsignedByte() != 'B') {
            throw exc;
        }
        if (ledis.readUnsignedByte() != 'M') {
            throw exc;
        }

        file_size = ledis.readInt();
        ledis.skipBytes(4);
        offset = ledis.readInt();

        header_size = ledis.readInt();
        if (header_size != 40) {
            throw exc;
        }
        width = ledis.readInt();
        height = ledis.readInt();
        num_of_color_planes = ledis.readShort();
        if (num_of_color_planes != 1) {
            throw exc;
        }
        bits_per_pixel = ledis.readShort();
        if (bits_per_pixel != 24) {
            throw exc;
        }
        compression = ledis.readInt();
        if (compression != 0) {
            throw exc;
        }

        image_size = ledis.readInt();
        hor_res = ledis.readInt();
        ver_res = ledis.readInt();
        num_of_colors = ledis.readInt();
        num_of_imp_color = ledis.readInt();

        BmpImage img = new BmpImage(width, height);



        int read_row;
        for (int i = height - 1; i >= 0; --i) {
            read_row = 0;
            for (int j = 0; j < width; ++j) {
                int B = ledis.readUnsignedByte();
                int G = ledis.readUnsignedByte();
                int R = ledis.readUnsignedByte();

                read_row += 3;
                img.setRGB(j, i, (new Color(R, G, B)).getRGB());
            }

            if (read_row % 4 != 0) {
                ledis.skipBytes(4 - (read_row + 4) % 4);
            }

        }

        return img;
    }

    static public void writeBmpImage(BmpImage img, File file) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DataOutputStream dos = new DataOutputStream(bos);
        LittleEndianDataOutputStream ledos = new LittleEndianDataOutputStream(dos);


    }
}
