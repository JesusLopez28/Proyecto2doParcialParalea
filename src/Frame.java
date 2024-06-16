import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class Frame extends JFrame {
    protected final JLabel titulo;
    protected final JLabel labelRuta;
    protected final JTextField textFieldRuta;
    protected final JLabel labelRutaGuardar;
    protected final JTextField textFieldRutaGuardar;
    protected final JButton buttonRuta;
    protected final JButton buttonRutaGuardar;
    protected final JLabel labelFiltro;
    protected final JComboBox<String> comboBoxFiltro;
    protected final JButton buttonLimpiar;
    protected final JButton buttonSecuencial;
    protected final JButton buttonForkJoin;
    protected final JButton buttonExecutorService;
    protected final JLabel labelTiempoSecuencial;
    protected final JLabel labelTiempoForkJoin;
    protected final JLabel labelTiempoExecutorService;
    protected final JButton buttonRutaOriginal;
    protected final JButton buttonRutaFiltrada;

    public Frame() {
        setTitle("Aplicador de filtros");
        setSize(720, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titulo = new JLabel("Aplicador de filtros", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(titulo);
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(null);

        labelRuta = new JLabel("Ruta de las imágenes:");
        labelRuta.setBounds(20, 20, 200, 30);
        panelCentral.add(labelRuta);

        textFieldRuta = new JTextField(10);
        textFieldRuta.setBounds(190, 20, 320, 30);
        panelCentral.add(textFieldRuta);

        buttonRuta = new JButton("Seleccionar ruta");
        buttonRuta.setBounds(530, 20, 150, 30);
        panelCentral.add(buttonRuta);

        labelRutaGuardar = new JLabel("Ruta para guardar:");
        labelRutaGuardar.setBounds(20, 70, 200, 30);
        panelCentral.add(labelRutaGuardar);

        textFieldRutaGuardar = new JTextField(10);
        textFieldRutaGuardar.setBounds(190, 70, 320, 30);
        panelCentral.add(textFieldRutaGuardar);

        buttonRutaGuardar = new JButton("Seleccionar ruta");
        buttonRutaGuardar.setBounds(530, 70, 150, 30);
        panelCentral.add(buttonRutaGuardar);

        labelFiltro = new JLabel("Filtro a aplicar:");
        labelFiltro.setBounds(20, 120, 200, 30);
        panelCentral.add(labelFiltro);

        String[] filtros = {
                "Seleccione un filtro",
                "Escala de grises",
                "Invertir colores",
                "Binarizar",
                "Posterizar",
                "Brillo",
                "Sepia"
        };
        comboBoxFiltro = new JComboBox<>(filtros);
        comboBoxFiltro.setBounds(190, 120, 320, 30);
        panelCentral.add(comboBoxFiltro);

        buttonLimpiar = new JButton("Limpiar");
        buttonLimpiar.setBounds(530, 120, 150, 30);
        panelCentral.add(buttonLimpiar);

        JPanel panelBotones = new JPanel(new GridLayout(4, 2, 10, 20));
        buttonSecuencial = new JButton("Secuencial");
        buttonSecuencial.setEnabled(false);
        buttonForkJoin = new JButton("ForkJoin");
        buttonForkJoin.setEnabled(false);
        buttonExecutorService = new JButton("ExecutorService");
        buttonExecutorService.setEnabled(false);
        labelTiempoSecuencial = new JLabel();
        labelTiempoSecuencial.setText("Tiempo: ");
        labelTiempoForkJoin = new JLabel();
        labelTiempoForkJoin.setText("Tiempo: ");
        labelTiempoExecutorService = new JLabel();
        labelTiempoExecutorService.setText("Tiempo: ");

        buttonRutaOriginal = new JButton("Ver imágenes originales");
        buttonRutaFiltrada = new JButton("Ver imágenes filtradas");
        buttonRutaOriginal.setEnabled(false);
        buttonRutaFiltrada.setEnabled(false);

        panelBotones.add(buttonSecuencial);
        panelBotones.add(labelTiempoSecuencial);
        panelBotones.add(buttonForkJoin);
        panelBotones.add(labelTiempoForkJoin);
        panelBotones.add(buttonExecutorService);
        panelBotones.add(labelTiempoExecutorService);
        panelBotones.add(buttonRutaOriginal);
        panelBotones.add(buttonRutaFiltrada);

        panelCentral.add(panelBotones);
        panelBotones.setBounds(20, 170, 660, 190);

        add(panelCentral, BorderLayout.CENTER);

        buttonRuta.addActionListener(e -> chooseDirectory(textFieldRuta, textFieldRutaGuardar, buttonRutaOriginal));
        buttonRutaGuardar.addActionListener(e -> chooseDirectory(textFieldRutaGuardar, textFieldRuta, buttonRutaFiltrada));
        comboBoxFiltro.addActionListener(e -> checkComboBoxSelection());
        buttonLimpiar.addActionListener(e -> clearFields());
        buttonSecuencial.addActionListener(e -> secuencialProcess());
        buttonForkJoin.addActionListener(e -> forkJoinProcess());
        buttonExecutorService.addActionListener(e -> executorServiceProcess());
        buttonRutaOriginal.addActionListener(e -> showOriginalImages());
        buttonRutaFiltrada.addActionListener(e -> showFilteredImages());
    }

    private void chooseDirectory(JTextField textField, JTextField textFieldRutaContra, JButton button) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int selection = fileChooser.showOpenDialog(this);

        if (selection == JFileChooser.APPROVE_OPTION && !textFieldRutaContra.getText().equals(fileChooser.getSelectedFile().getAbsolutePath())) {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            enableButtons();
            button.setEnabled(true);
        } else {
            disableButtons();
            JOptionPane.showMessageDialog(this, "Las rutas no pueden ser iguales, ni estar vacías", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkComboBoxSelection() {
        if (comboBoxFiltro.getSelectedIndex() != 0 && !textFieldRuta.getText().isEmpty() && !textFieldRutaGuardar.getText().isEmpty()) {
            if (!textFieldRuta.getText().equals(textFieldRutaGuardar.getText())) {
                enableButtons();
            } else {
                disableButtons();
                JOptionPane.showMessageDialog(this, "Las rutas no pueden ser iguales", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            disableButtons();
        }
    }

    private void enableButtons() {
        buttonSecuencial.setEnabled(true);
        buttonForkJoin.setEnabled(true);
        buttonExecutorService.setEnabled(true);
    }

    private void disableButtons() {
        buttonSecuencial.setEnabled(false);
        buttonForkJoin.setEnabled(false);
        buttonExecutorService.setEnabled(false);
    }

    private void clearFields() {
        textFieldRuta.setText("");
        textFieldRutaGuardar.setText("");
        comboBoxFiltro.setSelectedIndex(0);
        labelTiempoSecuencial.setText("Tiempo: ");
        labelTiempoForkJoin.setText("Tiempo: ");
        labelTiempoExecutorService.setText("Tiempo: ");
    }

    private void secuencialProcess() {
        if (validateAllFields()) {
            File[] archivos = new File(textFieldRuta.getText()).listFiles();
            int option = comboBoxFiltro.getSelectedIndex();
            SecuencialProcess secuencialProcess = new SecuencialProcess(archivos, new File(textFieldRutaGuardar.getText()), option);
            long tiempoInicio = System.currentTimeMillis();
            secuencialProcess.applyFilter();
            long tiempoFin = System.currentTimeMillis();
            labelTiempoSecuencial.setText("Tiempo: " + (tiempoFin - tiempoInicio) + " ms" + " / " + (tiempoFin - tiempoInicio) / 1000 + " sgs" + " / " + (tiempoFin - tiempoInicio) / 60000 + " mins");
        }
    }

    private void forkJoinProcess() {
        if (validateAllFields()) {
            File[] archivos = new File(textFieldRuta.getText()).listFiles();
            int option = comboBoxFiltro.getSelectedIndex();
            ForkJoinPool pool = ForkJoinPool.commonPool();
            long tiempoInicio = System.currentTimeMillis();
            pool.invoke(new ForkJoinProcess(archivos, new File(textFieldRutaGuardar.getText()), option));
            long tiempoFin = System.currentTimeMillis();
            labelTiempoForkJoin.setText("Tiempo: " + (tiempoFin - tiempoInicio) + " ms" + " / " + (tiempoFin - tiempoInicio) / 1000 + " sgs" + " / " + (tiempoFin - tiempoInicio) / 60000 + " mins");
        }
    }

    private void executorServiceProcess() {
        if (validateAllFields()) {
            File[] archivos = new File(textFieldRuta.getText()).listFiles();
            int option = comboBoxFiltro.getSelectedIndex();
            ExecutorServiceProcess executorServiceProcess = new ExecutorServiceProcess(archivos, new File(textFieldRutaGuardar.getText()), option);
            long tiempoInicio = System.currentTimeMillis();
            executorServiceProcess.applyFilter();
            long tiempoFin = System.currentTimeMillis();
            labelTiempoExecutorService.setText("Tiempo: " + (tiempoFin - tiempoInicio) + " ms" + " / " + (tiempoFin - tiempoInicio) / 1000 + " sgs" + " / " + (tiempoFin - tiempoInicio) / 60000 + " mins");
        }
    }

    private void showOriginalImages() {
        try {
            Desktop.getDesktop().open(new File(textFieldRuta.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showFilteredImages() {
        try {
            Desktop.getDesktop().open(new File(textFieldRutaGuardar.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateAllFields() {
        if (textFieldRuta.getText().isEmpty() || textFieldRutaGuardar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Las rutas no pueden estar vacías", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (comboBoxFiltro.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un filtro", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (textFieldRuta.getText().equals(textFieldRutaGuardar.getText())) {
            JOptionPane.showMessageDialog(this, "Las rutas no pueden ser iguales", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
