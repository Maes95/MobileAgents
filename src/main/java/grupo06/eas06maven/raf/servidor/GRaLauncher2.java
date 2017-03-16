package grupo06.eas06maven.raf.servidor;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import grupo06.eas06maven.raf.principal.*;
import grupo06.eas06maven.raf.utils.AgentTable;
import grupo06.eas06maven.raf.utils.TableListener;

/**
 *
 * @author Michel
 */
public class GRaLauncher2 extends javax.swing.JFrame implements AgencyListener, TableListener{
    
    /**
     * Donde esta la configuracion del servidor.
     */
    String strConfigFile =  "src\\main\\java\\grupo06\\eas06maven"
                          + File.separator
                          + "raf" 
                          + File.separator
                          + "config"
                          + File.separator
                          + "movil2.config";
    
    /**
     * Tabla mis agentes
     */
    private AgentTable myAgentsTable;
    
    /**
     * Tabla agentes externos
     */
    private AgentTable foreignAgentsTable;
    
    /**
     * Tabla de log de un agente en la agencia
     */
    private AgentTable detailTable;
    
    /**
     * Agentes
     */
    private HashMap<String, List<RaInfo>> agents;

    /**
     * Maneja los byte codes de las clases cargadas.
     */
    ClassManager classManager;

    /**
     * Nombre del agente que fue seleccionado en la lista.
     */
    String selectedRa = null;

    /**
     * La agencia que maneja todos los agentes.
     */
    private final RaAgency raAgency;

    /**
     * Direccion del servidor que registra todos los servidores de agentes del dominio.
     */
    RaAddress raServer;

    /**
     * Nº puerto para las conexiones, por defecto 10101.
     */
    int port;
    /**
     * Crea un nuevo lanzador GRaLauncher.
     */
    public GRaLauncher2(){
        super("GRaLauncher 2");
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("No se ha podido establecer el look and feel multiplataforma: " + e);
        }
        //</editor-fold>
        System.out.println("Agencia 2");
        long byteCodeDelay;
        String strRaServer = null;
        int raPort;
        Properties props = new Properties ();
        this.agents = new HashMap<>();

        // lee las propiedades desde el fichero
        try (FileInputStream in = new FileInputStream (strConfigFile)) {
            props.load (in);
        }
        catch (FileNotFoundException e){
            System.err.println ("GraLauncher: No se puede abrir el fichero de configuración!");
        }
        catch (IOException e){
            System.err.println ("GRaLauncher: Ha fallado la lectura del fichero!");
        }

        try {
            port = Integer.parseInt(props.getProperty("port", "10101"));
        }
        catch (NumberFormatException e){
            port = 10101;
        }
        try {
            byteCodeDelay = Long.parseLong(props.getProperty("byteCodeDelay", "100000"));
        }
        catch (NumberFormatException e){
            byteCodeDelay = 100000;
        }
        try {
            strRaServer = props.getProperty("raServer");
            raPort = Integer.parseInt(props.getProperty("raPort", "10102"));
        }
        catch (NumberFormatException e){
            raPort = 10102;
        }

        try{
            if (strRaServer == null){
                raServer = null;
            }
            else{
                InetAddress server = InetAddress.getByName (strRaServer);
                raServer = new RaAddress (server, raPort, null);
            }
        }
        catch (UnknownHostException e){
            System.out.println("! GRaLauncher: raServer no valido." + e);
            raServer = null;
        }

        System.out.println ("puerto: " + port);

        // lanza una nuva agencia
        classManager = new ClassManager (byteCodeDelay, props.getProperty("agentsPath"));

        raAgency = new RaAgency (this, classManager);
        raAgency.addAgencyListener (this);
        
        startAgency();
        initComponents();
        setAfterInit();
        buscarAgentesLocales();
    }
    
    /**
     *  Llamada para el limpiado de las conexiones de red.
     */
    @Override
    public void dispose() {
        setVisible(false);
        stopAgency();
        super.dispose();
        System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPasswordField1 = new javax.swing.JPasswordField();
        CargarAgente = new javax.swing.JButton();
        ListaAgentes = new javax.swing.JComboBox<>();
        panelIzquierdo1 = new javax.swing.JPanel();
        panelIzquierdo2 = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        agentDetail = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        agentName = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        agentDescription = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        createdAt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        creator = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        state = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        arrivedDate = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        destination = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        detailLogTablePanel = new javax.swing.JPanel();
        deleteRa = new javax.swing.JButton();
        editRa = new javax.swing.JToggleButton();
        launchRa = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        CargarAgente.setText("Cargar Agente");
        CargarAgente.setFocusable(false);
        CargarAgente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CargarAgente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        CargarAgente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargarAgenteActionPerformed(evt);
            }
        });

        ListaAgentes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        panelIzquierdo1.setBackground(new java.awt.Color(255, 255, 255));
        panelIzquierdo1.setForeground(new java.awt.Color(255, 255, 255));
        panelIzquierdo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout panelIzquierdo1Layout = new javax.swing.GroupLayout(panelIzquierdo1);
        panelIzquierdo1.setLayout(panelIzquierdo1Layout);
        panelIzquierdo1Layout.setHorizontalGroup(
            panelIzquierdo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelIzquierdo1Layout.setVerticalGroup(
            panelIzquierdo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 193, Short.MAX_VALUE)
        );

        panelIzquierdo2.setBackground(new java.awt.Color(255, 255, 255));
        panelIzquierdo2.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelIzquierdo2Layout = new javax.swing.GroupLayout(panelIzquierdo2);
        panelIzquierdo2.setLayout(panelIzquierdo2Layout);
        panelIzquierdo2Layout.setHorizontalGroup(
            panelIzquierdo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelIzquierdo2Layout.setVerticalGroup(
            panelIzquierdo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        titulo.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 36)); // NOI18N
        titulo.setForeground(new java.awt.Color(102, 102, 102));
        titulo.setText("Agentes Móviles");

        jLabel1.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Agentes lanzados desde esta agencia");

        jLabel2.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Agentes externos");

        jLabel3.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Detalles del Agente");

        agentDetail.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Nombre del Agente: ");

        agentName.setText("Ningun agente seleccionado");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Descripción del Agente");

        agentDescription.setEditable(false);
        agentDescription.setColumns(20);
        agentDescription.setRows(5);
        jScrollPane2.setViewportView(agentDescription);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Fecha de creación:");

        createdAt.setText("<vacio>");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Creador:");

        creator.setText("<vacio>");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Destino:");

        state.setText("<vacio>");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Fecha de llegada:");

        arrivedDate.setText("<vacio>");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Estado:");

        destination.setText("<vacio>");

        javax.swing.GroupLayout detailLogTablePanelLayout = new javax.swing.GroupLayout(detailLogTablePanel);
        detailLogTablePanel.setLayout(detailLogTablePanelLayout);
        detailLogTablePanelLayout.setHorizontalGroup(
            detailLogTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        detailLogTablePanelLayout.setVerticalGroup(
            detailLogTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 141, Short.MAX_VALUE)
        );

        deleteRa.setText("No se puede eliminar");
        deleteRa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRaActionPerformed(evt);
            }
        });

        editRa.setText("Editar");
        editRa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout agentDetailLayout = new javax.swing.GroupLayout(agentDetail);
        agentDetail.setLayout(agentDetailLayout);
        agentDetailLayout.setHorizontalGroup(
            agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(agentDetailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailLogTablePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(agentDetailLayout.createSequentialGroup()
                        .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(creator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                                .addComponent(createdAt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(agentDetailLayout.createSequentialGroup()
                                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(agentDetailLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel17)))
                                    .addGroup(agentDetailLayout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel11)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(agentDetailLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(arrivedDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(agentDetailLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(destination, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(state, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(agentDetailLayout.createSequentialGroup()
                        .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(agentDetailLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(agentName, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(agentDetailLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(79, 79, 79)
                                .addComponent(jLabel13)))
                        .addGap(0, 76, Short.MAX_VALUE))
                    .addGroup(agentDetailLayout.createSequentialGroup()
                        .addComponent(deleteRa, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editRa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        agentDetailLayout.setVerticalGroup(
            agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(agentDetailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(agentName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createdAt)
                    .addComponent(arrivedDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creator)
                    .addComponent(destination))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(state)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailLogTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(agentDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteRa)
                    .addComponent(editRa))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        launchRa.setText("Lanzar Agente");
        launchRa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchRaActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\Michel\\Documents\\agent50.png")); // NOI18N
        jLabel6.setText("jLabel6");
        jLabel6.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelIzquierdo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelIzquierdo1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ListaAgentes, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CargarAgente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(titulo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(agentDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(launchRa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(launchRa)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(CargarAgente)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ListaAgentes, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelIzquierdo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelIzquierdo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(agentDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        CargarAgente.getAccessibleContext().setAccessibleDescription("Carga un agente de tu lista de agentes");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CargarAgenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargarAgenteActionPerformed
        
        String nombreAgente = (String) ListaAgentes.getSelectedItem();
        this.selectedRa = nombreAgente;
        System.out.println("Agente: "+nombreAgente);         
        if (nombreAgente != null) {
            nombreAgente = nombreAgente.trim();
            if (nombreAgente.length() > 0 ) {
                loadRa(nombreAgente);
            }
        }                     
    }//GEN-LAST:event_CargarAgenteActionPerformed

    private void launchRaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchRaActionPerformed
        if(currentAgentCreated != null){
            raAgency.addRaOnCreation (currentAgentCreated, null);
            currentAgentCreated = null;
            launchRa.setEnabled(false);
            launchRa.setText("Agente ya lanzado");
        }else{
            System.out.println("No hay ningun agente cargado: Esto no deberia llegar aqui");
        }    
    }//GEN-LAST:event_launchRaActionPerformed

    private void deleteRaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRaActionPerformed
        System.out.println("Llamado a borrar: "+this.selectedRa);
        editDestroy();
    }//GEN-LAST:event_deleteRaActionPerformed

    private void editRaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRaActionPerformed
        int i =0; 
                Object[] v = new Object[50];
                Collection elements = raAgency.getServers(this).values();
                Iterator it = elements.iterator();
                while (it.hasNext()){
                    RaAddress address = (RaAddress) it.next();
                    System.out.println("Address: "+address.toString());
                    v[i] = (Object) address;
                    
                      i = i + 1;
                }
		RaAddress ra = (RaAddress) JOptionPane.showInputDialog(
                     new JFrame(),
                     "Elige una Agencia",
                     "Agencia Destino",
                     JOptionPane.PLAIN_MESSAGE,
		     null,
		     v,
                     null);
                String s = ra.host.getHostName();
                System.out.println("Edit: "+s);
                if (s != null) {
                    s = s.trim();
                    if (s.length() >0 ) {
                      System.out.println (s);
                        editSendTo (s);
                        }
                }
     
            editSendTo(s);
    }//GEN-LAST:event_editRaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new GRaLauncher2();
            WindowListener l = new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    ((GRaLauncher2) e.getWindow()).dispose();                   
                }
            };
            frame.addWindowListener(l);
            frame.pack();
            frame.setVisible(true);
        });
    }
    public void buscarAgentesLocales(){
        File agentsPath = new File ( "src\\main\\java\\grupo06\\eas06maven" +File.separator + "raf" + File.separator + "agentes" + File.separator );
        // Cargar los nombred de los agentes
        String[] lista = agentsPath.list();
        System.out.println ("Lista de agentes: "+Arrays.toString(lista));
        ListaAgentes.setModel(new javax.swing.DefaultComboBoxModel<>(lista));
    }
    
    /**
     * Carga un agente desde un fichero (poner un string como parametro)
     * @param s
     */
    public void loadRa(String s){
      
        String name;

        name = "raf.agentes." + s;
        try{
            Class result;
            RaClassLoader loader = new RaClassLoader(classManager, null, null);
            result = loader.loadClass(name);
            if (result == null){
                System.err.println ("GRaLauncher: No se pudo cargar la clase! clase no encontrada!");
                return;
            }
            Constructor cons[] = result.getConstructors();
            Object obs[] = {raAgency.generateName()};
	    currentAgentCreated = (Ra) cons[0].newInstance(obs);
            
            // Creamos un nuevo registro para nuestro agente
            RaInfo newAgentInfo = new RaInfo(currentAgentCreated.getName(),currentAgentCreated.getDescription(),
                                                raAgency.getAdress());
            List<RaInfo> listInfo = new ArrayList<>();
            listInfo.add(newAgentInfo);
            agents.put(newAgentInfo.raName.split(" ")[0], listInfo);
            
            setAgentDetail(newAgentInfo.raName);
            
            // Activar boton lanzar
            launchRa.setEnabled(true);
            launchRa.setText("Lanzar Agente");
            deleteRa.setEnabled(false);
            deleteRa.setText("No se puede borrar");
        }
        catch (InvocationTargetException e){
            System.err.println ("! GRaLauncher: No se ha podidio cargar la clase " + e);
        }
        catch (SecurityException | IllegalAccessException | InstantiationException e){
            System.err.println ("! GRaLauncher: No se ha podido cargar la clase! " + e);
        }
        catch (ClassNotFoundException e){
            System.err.println ("! GRaLauncher: No se ha podido cargar la clase!  " + e);
        }
    }
    
    protected Ra currentAgentCreated;
    
    /**
     * Reaccion cuando se ha puesto a un agente ha sido seleccionado en una tabla
     * @param e
     */
    @Override
    public void raSelectedChange(TableEvent e) {       
        deleteRa.setEnabled(true);
        deleteRa.setText("Eliminar agente");
        setAgentDetail(e.getValue());
    }
    
    private void setAgentDetail(String name){
        List<RaInfo> infoList = agents.get(name);
        if(infoList != null){
            this.selectedRa = infoList.get(0).raFullName;
            RaInfo info = infoList.get(infoList.size()-1); 
            // Siempre que se crea una lista de logs, se mete almenos 1
            agentName.setText(info.raName);
            String description = "";
            // Separamos la descripción por lineas
            String[] separate = info.raDescription.split(" ");
            int size = 0;
            for(String s: separate){
                // Ajusta el texto al tamaño del cuadro de texto (agentDescription)
                boolean b = (size + s.length()) > agentDescription.getColumns()*2;
                if( b && description.length() != 0){
                    description+= "\n "+s;
                    size=s.length()+2;
                }else{
                    description+= " "+s;
                    size+= s.length()+1;
                }         
            }
            
            agentDescription.setText(description);
            arrivedDate.setText(info.arrivalDate);
            createdAt.setText(info.createdAt);
            creator.setText(info.creator);
            destination.setText(info.destination);
            state.setText(info.state+" a las "+info.infoDate);
            
            this.detailTable.deleteData();
            for(RaInfo in: infoList){
                String s = in.state+" a las "+in.infoDate;
                String[] log = {s,in.arrivalDate};
                this.detailTable.putLog(log);
            }
            
                 
        }else{
            System.err.println("ERROR: EL AGENTE A VER EN DETALLE NO EXISTE");
        }
    }
    

    /**
     * Envia el agente seleccionado a otra agencia.
     */
    void editSendTo(String s){
        InetAddress destination;
        String server;
        String servername;
        String strLoPort;
        int loPort = port;
        
        server = s;

        try{ 
            // split server address into servername and port and determine host address
            server = server.trim();
            int portDelimiter = server.indexOf (':');
            if (portDelimiter != -1) {
                servername = server.substring (0, portDelimiter);
                strLoPort  = server.substring (portDelimiter + 1);
                loPort = Integer.parseInt (strLoPort);
            }   
            else{
                servername = server;          
            }
            destination = InetAddress.getByName (servername);
            System.out.println("Agente mandado a otra agencia (editSendTo): "+selectedRa);
            raAgency.dispatchRa (this, selectedRa, new RaAddress (servername, loPort, null));
        }
        catch (IndexOutOfBoundsException | NumberFormatException e){
            System.err.println("! GRaLauncher.editSendTo: Formato erroneo del puerto " + e);
        }
        catch (UnknownHostException e){
            System.err.println("! GRaLauncher.editSendTo: No se puede determinar la dirccion del host " + e);
        }
    }


    /**
     * Borra el agente seleccionado.
     */
    void editDestroy(){
        raAgency.destroyRa (this, selectedRa);
    }

    /**
     * Inicializa el Thread del RaAgency.
     */
    void startAgency(){
	System.out.println ("Inicializando la Agencia");
        raAgency.startAgency (this, port, raServer);
    }

    /**
     * Para el Thread de la agencia.
     */
    void stopAgency(){
	System.out.println ("Parando la Agencia");
        raAgency.stopAgency (this);
    }


    /**
     * Reaccion cuando se ha puesto a un agente en el estado onCreate
     * @param e
     */
    @Override
    public void agencyRaCreated (AgencyEvent e){  
        saveRaInfo(e.getInfo(), " Creado");     
    }

    /**
     * Reaccion cuando se ha puesto a un agente en el estado onArrival
     * @param e
     */
    @Override
    public void agencyRaArrived (AgencyEvent e){
        deleteRa.setEnabled(true);
        deleteRa.setText("Eliminar agente");
        saveRaInfo(e.getInfo(), " Llego");   
    }

    /**
     * Reaccion cuando un agente abandona la agencia.
     * @param e
     */
    @Override
    public void agencyRaLeft (AgencyEvent e){
        saveRaInfo(e.getInfo(), " Se fué");
    }
    
    /**
     * Reaccion cuando se ha borrado un agente.
     * @param e
     */
    @Override
    public void agencyRaDestroyed (AgencyEvent e){
        saveRaInfo(e.getInfo(), " Murió");  
    }
    
    protected void saveRaInfo(RaInfo info, String state){
        info.state = state;
        List<RaInfo> infoList = agents.get(info.raName);
        
        // Si existe registro de ese agente
        if(agents.get(info.raName) != null){
            infoList.add(info);
        }
        // Si no existe registro de ese agente
        else{
            List<RaInfo> newInfoList = new ArrayList<>();
            newInfoList.add(info);
            agents.put(info.raName, newInfoList);
        }
        
        // Si es una agente local
        if(info.creator.equals(raAgency.getAdress())){
            String[] newData = {info.raName, state, info.infoDate};
            myAgentsTable.putLog(newData);
        }      
        // Si es un agente externo
        else{
            String[] newData = {info.raName, state, info.creator};
            foreignAgentsTable.putLog(newData);
        }
    }


    
    public void setAfterInit(){

        // Cargar Tabla de mis agentes
        String[] columnNamesMyAgents = {"Agente","Estado", "Completado"};
        this.myAgentsTable = new AgentTable(this,columnNamesMyAgents, true);
        panelIzquierdo1.setLayout(new BorderLayout());
        panelIzquierdo1.add(myAgentsTable.getTableHeader(), BorderLayout.PAGE_START);
        panelIzquierdo1.add(myAgentsTable, BorderLayout.CENTER);
        
        // Cargar Tabla de agentes extranjeros
        String[] columnNamesForeignAgents = {"Agente","Estado", "Origen"};
        this.foreignAgentsTable = new AgentTable(this,columnNamesForeignAgents, true);        
        panelIzquierdo2.setLayout(new BorderLayout());
        panelIzquierdo2.add(foreignAgentsTable.getTableHeader(), BorderLayout.PAGE_START);
        panelIzquierdo2.add(foreignAgentsTable, BorderLayout.CENTER);
        
        // Cargar Tabla de mis agentes
        String[] columnNamesLog = {"Estado","Llegada"};
        this.detailTable = new AgentTable(this,columnNamesLog, false);
        detailLogTablePanel.setLayout(new BorderLayout());
        detailLogTablePanel.add(detailTable.getTableHeader(), BorderLayout.PAGE_START);
        detailLogTablePanel.add(detailTable, BorderLayout.CENTER);
        
        // Otros
        launchRa.setEnabled(false);
        launchRa.setText("Selecciona para lanzar");
        deleteRa.setEnabled(false);
        editRa.setEnabled(false);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CargarAgente;
    private javax.swing.JComboBox<String> ListaAgentes;
    private javax.swing.JTextArea agentDescription;
    private javax.swing.JPanel agentDetail;
    private javax.swing.JLabel agentName;
    private javax.swing.JLabel arrivedDate;
    private javax.swing.JLabel createdAt;
    private javax.swing.JLabel creator;
    private javax.swing.JButton deleteRa;
    private javax.swing.JLabel destination;
    private javax.swing.JPanel detailLogTablePanel;
    private javax.swing.JToggleButton editRa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton launchRa;
    private javax.swing.JPanel panelIzquierdo1;
    private javax.swing.JPanel panelIzquierdo2;
    private javax.swing.JLabel state;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables


}
