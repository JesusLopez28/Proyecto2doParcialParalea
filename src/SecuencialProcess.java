import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SecuencialProcess {
    File[] archivos;
    File directorioFiltros;
    int option;

    public SecuencialProcess(File[] archivos, File directorioFiltros, int option) {
        this.archivos = archivos;
        this.directorioFiltros = directorioFiltros;
        this.option = option;
    }

    public void applyFilter() {
        for (File archivo : archivos) {
            try {
                BufferedImage imagen = ImageIO.read(archivo);
                switch (option) {
                    case 1:
                        PhotoEffects.grayscale(imagen);
                        break;
                    case 2:
                        PhotoEffects.invertColors(imagen);
                        break;
                    case 3:
                        PhotoEffects.binarize(imagen, 128);
                        break;
                    case 4:
                        PhotoEffects.posterize(imagen, 8);
                        break;
                    case 5:
                        PhotoEffects.brightness(imagen, 50);
                        break;
                    case 6:
                        PhotoEffects.sepia(imagen);
                        break;
                }
                ImageIO.write(imagen, "png", new File(directorioFiltros, archivo.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
