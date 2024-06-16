import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceProcess {
    File[] archivos;
    File directorioFiltros;
    int option;

    public ExecutorServiceProcess(File[] archivos, File directorioFiltros, int option) {
        this.archivos = archivos;
        this.directorioFiltros = directorioFiltros;
        this.option = option;
    }

    public void applyFilter() {
        int numProcesadores = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numProcesadores);

        List<Future<?>> tareasFuturas = new ArrayList<>();

        for (File archivo : archivos) {
            TaskProcess tarea = new TaskProcess(archivo, directorioFiltros, option);
            Future<?> tareaFutura = executorService.submit(tarea);
            tareasFuturas.add(tareaFutura);
        }

        for (Future<?> tareaFutura : tareasFuturas) {
            try {
                tareaFutura.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

    }

    public static class TaskProcess implements Runnable {
        private final File archivo;
        private final File directorioFiltros;
        private final int option;

        public TaskProcess(File archivo, File directorioFiltros, int option) {
            this.archivo = archivo;
            this.directorioFiltros = directorioFiltros;
            this.option = option;
        }

        @Override
        public void run() {
            try {
                SecuencialProcess secuencialProcess = new SecuencialProcess(new File[]{archivo}, directorioFiltros, option);
                secuencialProcess.applyFilter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
