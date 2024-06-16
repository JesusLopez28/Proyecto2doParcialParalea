import java.io.File;
import java.util.concurrent.RecursiveAction;

public class ForkJoinProcess extends RecursiveAction {
    File[] archivos;
    File directorioFiltros;
    int option;

    public ForkJoinProcess(File[] archivos, File directorioFiltros, int option) {
        this.archivos = archivos;
        this.directorioFiltros = directorioFiltros;
        this.option = option;
    }

    @Override
    protected void compute() {
        if (archivos.length <= 2) {
            SecuencialProcess secuencialProcess = new SecuencialProcess(archivos, directorioFiltros, option);
            secuencialProcess.applyFilter();
        } else {
            int mitad = archivos.length / 2;
            File[] primera = new File[mitad];
            File[] segunda = new File[archivos.length - mitad];
            System.arraycopy(archivos, 0, primera, 0, mitad);
            System.arraycopy(archivos, mitad, segunda, 0, segunda.length);

            invokeAll(new ForkJoinProcess(primera, directorioFiltros, option),
                    new ForkJoinProcess(segunda, directorioFiltros, option));
        }
    }
}
