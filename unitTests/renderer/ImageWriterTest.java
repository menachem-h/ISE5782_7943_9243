package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  test image writing functionality
 */
class ImageWriterTest {


    @Test
    void writeToImage() {
        int Nx = 800;
        int Ny = 500;
        Color yellow = new Color(255d,255d,0d);
        Color red = new Color(255d,0d,0d);
        ImageWriter imageWriter = new ImageWriter("yelloSubmatrine",Nx+1,Ny+1);

        for (int i = 0; i <= Nx; i++) {
            for (int j = 0; j <= Ny; j++) {
                if(i%50 ==0 || j%50==0)
                    imageWriter.writePixel(i,j,red);
                else
                    imageWriter.writePixel(i,j,yellow);
            }

        }
        imageWriter.writeToImage();
    }
}