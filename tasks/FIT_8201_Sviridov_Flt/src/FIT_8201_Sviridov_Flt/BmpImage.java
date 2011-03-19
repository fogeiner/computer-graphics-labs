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

    public static final int BMP_HEADER_LENGTH = 14;
    public static final int DIB_HEADER_LENGTH = 40;

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

        BmpImage img = null;
        int header_size, width, height, num_of_color_planes,
                bits_per_pixel, compression;


        // magic

        try {

            if (ledis.readUnsignedByte() != 'B') {
                throw exc;
            }
            if (ledis.readUnsignedByte() != 'M') {
                throw exc;
            }

            ledis.readInt();
            ledis.skipBytes(4);
            ledis.readInt();

            header_size = ledis.readInt();
            if (header_size != DIB_HEADER_LENGTH) {
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

            ledis.readInt();
            ledis.readInt();
            ledis.readInt();
            ledis.readInt();
            ledis.readInt();

            img = new BmpImage(width, height);




            for (int i = height - 1; i >= 0; --i) {
                int j;
                for (j = 0; j < width; ++j) {
                    int B = ledis.readUnsignedByte();
                    int G = ledis.readUnsignedByte();
                    int R = ledis.readUnsignedByte();

                    img.setRGB(j, i, (new Color(R, G, B)).getRGB());
                }
                j = j * 3;
                if (j % 4 != 0) {
                    ledis.skipBytes(4 - (j + 4) % 4);
                }

            }
        } catch (IOException ex) {
            dis.close();
            throw ex;
        }

        dis.close();
        return img;
    }

    static public void writeBmpImage(BufferedImage img, File file) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DataOutputStream dos = new DataOutputStream(bos);
        LittleEndianDataOutputStream ledos = new LittleEndianDataOutputStream(dos);

        int width = img.getWidth();
        int height = img.getHeight();


        byte pad[] = new byte[4 - (3 * width + 4) % 4];
        int file_size = height * width * 3 + (width % 4 == 0 ? 0 : height * pad.length) + BMP_HEADER_LENGTH + DIB_HEADER_LENGTH;
  
        try {
            ledos.writeByte('B');
            ledos.writeByte('M');

            //file_size
            ledos.writeInt(file_size);

            // skipBytes 4
            ledos.writeInt(0);

            //offset 
            ledos.writeInt(BMP_HEADER_LENGTH + DIB_HEADER_LENGTH);

            // header_size 
            ledos.writeInt(DIB_HEADER_LENGTH);

            //width 
            ledos.writeInt(width);

            // height 
            ledos.writeInt(height);


            //num_of_color_planes 
            ledos.writeShort(1);

            //bits_per_pixel 
            ledos.writeShort(24);

            //compression 
            ledos.writeInt(0);

            //image_size 
            ledos.writeInt(file_size - BMP_HEADER_LENGTH - DIB_HEADER_LENGTH);

            // hor_res 
            ledos.writeInt(0);
            // ver_res 
            ledos.writeInt(0);

            //num_of_colors
            ledos.writeInt(0);
            //num_of_imp_color 
            ledos.writeInt(0);

            byte bgr[] = new byte[3];
            for (int i = height - 1; i >= 0; --i) {
                for (int j = 0; j < width; ++j) {
                    Color c = new Color(img.getRGB(j, i));
                    bgr[0] = (byte) c.getBlue();
                    bgr[1] = (byte) c.getGreen();
                    bgr[2] = (byte) c.getRed();

                    ledos.write(bgr);
                }

                if (width % 4 != 0) {
                    ledos.write(pad);
                }

            }
        } catch (IOException ex) {
            dos.close();
            throw ex;
        }

        dos.close();
    }
}
