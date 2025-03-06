import java.io.*;
import java.util.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.*;
import javax.swing.tree.*;

public class GerenciadorArquivos extends javax.swing.JFrame implements TreeSelectionListener, ListSelectionListener {

    JFrame janelaAbout;
    DefaultTreeModel modeloArvore;
    DefaultMutableTreeNode root;
    DefaultTableModel modeloTabela;
    FileSystemView fileSystemView;
    Desktop desktop;
    Object[][] rowData;
    String[] columnNames = {"Ícone", "Arquivo", "Caminho"};
    String pastaRaiz;
    String idUsuario;
    String nivelUsuario;
    String nomeUsuario;
    String tamanhoPasta;
    Icon img;
    int pastaRaizCountPastas;
    int pastaRaizCountArquivos;
    long pastaRaizSize;

    public GerenciadorArquivos() { 
        
    }
    
    public void construirJanela(){
        pastaRaiz = new File(".").getPath() + "/Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}/" + idUsuario;        
        img = createImageIcon("images/orange_folder.png", null);        

        root = new DefaultMutableTreeNode(pastaRaiz, true);
        desktop = Desktop.getDesktop();
        fileSystemView = FileSystemView.getFileSystemView();

        modeloArvore = new DefaultTreeModel(root);
        modeloTabela = new DefaultTableModel(rowData, columnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        initComponents();
        AtualizarStatusBar();
        ClearCrud();

        ListarArquivosArvore(pastaRaiz, root);
        ListarArquivos(pastaRaiz);

        jTreeArvore.setCellRenderer(new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row,
                    boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

                    if (node.isRoot()) {
                        setText("Pasta Privada");
                    }

                    if (node.getUserObject() instanceof File) {
                        File arquivo = (File) node.getUserObject();
                        
                        setIcon(fileSystemView.getSystemIcon(arquivo));
                        setText(arquivo.getName());
                        
                        //setText(((File) node.getUserObject()).getName());                          

                    }

                    if (node.isRoot()) {
                        setIcon(fileSystemView.getSystemIcon(new File("C:/")));
                    }
                }
                return this;
            }
        });             
        
        jTreeArvore.expandRow(0);
    }
    
    public void setJanelaAbout(JFrame janelaAbout){
        this.janelaAbout = janelaAbout;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNivelUsuario(String nivelUsuario) {
        this.nivelUsuario = nivelUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void AtualizarStatusBar() {
        pastaRaizSize = 0;
        pastaRaizCountArquivos = 0;
        pastaRaizCountPastas = 0;

        InfoPastaRaiz(pastaRaiz);

        jLabelStatusBar.setText("Usuario: " + nomeUsuario + "     |     Espaço Usado: " + String.valueOf(pastaRaizSize / 1024) + "KB - " + pastaRaizCountArquivos + " arquivos e " + pastaRaizCountPastas + " pastas");
    }

    public void InfoPastaRaiz(String diretorio) {
        File arquivo = new File(diretorio);
        File[] listaDiretorio = arquivo.listFiles();

        if (listaDiretorio != null) {

            for (int i = 0; i < listaDiretorio.length; i++) {
                File arquivoListado = listaDiretorio[i];

                if (arquivoListado.isFile()) {
                    pastaRaizSize += arquivoListado.length();
                    pastaRaizCountArquivos++;
                } else if (arquivoListado.isDirectory()) {
                    pastaRaizCountPastas++;
                    InfoPastaRaiz(arquivoListado.getPath());
                }

            }

        }
    }
    
    public void TamanhoPasta(String diretorio){        
        File arquivo = new File(diretorio);
        File[] listaDiretorio = arquivo.listFiles();
        
        if(listaDiretorio != null){
            for(int i = 0; i < listaDiretorio.length; i++){
                File arquivoListado = listaDiretorio[i];
                
                if(arquivoListado.isFile()){
                    tamanhoPasta += arquivoListado.length();
                } else if(arquivoListado.isDirectory()){
                    TamanhoPasta(arquivoListado.getPath());
                }
            }
        }
    }

    public void ListarArquivosArvore(String diretorio, DefaultMutableTreeNode root) {

        root.removeAllChildren();
        modeloArvore.reload();

        File arquivo = new File(diretorio);
        File[] listaDiretorio = arquivo.listFiles();

        if (listaDiretorio != null) {
            for (int i = 0; i < listaDiretorio.length; i++) {
                File arquivoListado = listaDiretorio[i];

                if (arquivoListado.isFile()) {
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(arquivoListado);
                    root.add(child);
                } else if (arquivoListado.isDirectory()) {
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(arquivoListado);
                    root.add(child);

                    ListarArquivosArvore(arquivoListado.getPath(), child);
                }
            }
            
        }

    }

    public void ListarArquivos(String diretorio) {
        File arquivo = new File(diretorio);
        File[] listaDiretorio = arquivo.listFiles();

        if (listaDiretorio != null) {

            modeloTabela.getDataVector().removeAllElements();
            modeloTabela.fireTableDataChanged();

            for (int i = 0; i < listaDiretorio.length; i++) {
                File arquivoListado = listaDiretorio[i];

                if (arquivoListado.isFile()) {
                    modeloTabela.insertRow(jTableListaArquivos.getRowCount(), new Object[]{new JLabel(), arquivoListado.getName(), arquivoListado});
                } else if (arquivoListado.isDirectory()) {
                    modeloTabela.insertRow(jTableListaArquivos.getRowCount(), new Object[]{new JLabel(), arquivoListado.getName(), arquivoListado});
                }

            }
            
            jTableListaArquivos.getColumnModel().getColumn(0).setCellRenderer(new Renderer(listaDiretorio));
        }

    }

    public boolean DeleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        DeleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        
        return (directory.delete());
    }

    public void ClearJtreeArquivo() {
        root.removeAllChildren();
        ListarArquivosArvore(idUsuario, root);
        modeloArvore.reload();
        jTreeArvore.addSelectionRow(0);
        jTreeArvore.expandRow(0);
    }
    
    public void ClearCrud(){
        jLabelNomeArquivo.setText("Arquivo:");
        jLabelIcon.setIcon(null);
        jLabelTamanhoArquivo.setText("");
        jButtonArquivoImprimir.setEnabled(false);    
        jButtonArquivoAbrir.setEnabled(false); 
        jButtonArquivoRenomear.setEnabled(false);
        jButtonArquivoExcluir.setEnabled(false);
        jButtonCriptografar.setEnabled(false);
        jButtonDescriptografar.setEnabled(false);
        jTextFieldNomeArquivo.setText("");
        jTextFieldPath.setText("");
        jTextFieldUltMod.setText("");
    }
    
    public boolean CifrarDescifrarArquivo(File arquivoOrigem, int opcao){        
        
        File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File arquivoExe = new File(arquivoRunable.getParent());
        File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
        File arquivoDestino  = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\temp_" + arquivoOrigem.getName());

        int c = 0;       
        
        if(opcao == 1){  
            JOptionPane.showMessageDialog(null, "Criptografar Arquivo: " + arquivoOrigem.getName() + ", isso pode levar alguns minutos!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
            
            try {
                FileInputStream entrada = new FileInputStream(arquivoOrigem);
                FileOutputStream saida = new FileOutputStream(arquivoDestino);               
                
                while(c != -1){
                    c = entrada.read();
                    
                    if(c != -1){
                        saida.write(c + 1);
                    }
                    
                }
                
                entrada.close();
                saida.close();
                arquivoOrigem.delete();
                arquivoDestino.renameTo(arquivoOrigem);  
                              
                return true;
                
            } catch (Exception ex) {
                return false;
            }
            
        } else if(opcao == 2){
            JOptionPane.showMessageDialog(null, "Descriptografar Arquivo: " + arquivoOrigem.getName() + ", isso pode levar alguns minutos!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
            
            try {
                FileInputStream entrada = new FileInputStream(arquivoOrigem);
                FileOutputStream saida = new FileOutputStream(arquivoDestino);
                
                while(c != -1){
                    c = entrada.read();
                    
                    if(c != -1){
                        saida.write(c - 1);
                    }
                    
                }
                
                entrada.close();
                saida.close();
                arquivoOrigem.delete();
                arquivoDestino.renameTo(arquivoOrigem);
                
                return true;
                
            } catch (Exception ex) {
                return false;
            }
        }    
        
        return false;
    }

    public ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooserCaminhoArquivo = new javax.swing.JFileChooser();
        jScrollPaneArquivos = new javax.swing.JScrollPane();
        jTableListaArquivos = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeArvore = new javax.swing.JTree(root);
        jLabelStatusBar = new javax.swing.JLabel();
        jPanelInfoArquivo = new javax.swing.JPanel();
        jLabelNomeArquivo = new javax.swing.JLabel();
        jButtonArquivoAbrir = new javax.swing.JButton();
        jButtonArquivoRenomear = new javax.swing.JButton();
        jButtonArquivoExcluir = new javax.swing.JButton();
        jTextFieldPath = new javax.swing.JTextField();
        jLabelUltModArquivo = new javax.swing.JLabel();
        jTextFieldNomeArquivo = new javax.swing.JTextField();
        jLabelPathArquivo = new javax.swing.JLabel();
        jTextFieldUltMod = new javax.swing.JTextField();
        jButtonArquivoImprimir = new javax.swing.JButton();
        jLabelIcon = new javax.swing.JLabel();
        jLabelRotuloIcone = new javax.swing.JLabel();
        jLabelRotuloTamanho = new javax.swing.JLabel();
        jLabelTamanhoArquivo = new javax.swing.JLabel();
        jButtonCriptografar = new javax.swing.JButton();
        jButtonDescriptografar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuInserir = new javax.swing.JMenu();
        jMenuNovaPasta = new javax.swing.JMenu();
        jMenuAbout = new javax.swing.JMenu();
        jMenuSair = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Security - Principal");

        jTableListaArquivos.setModel(modeloTabela);
        jTableListaArquivos.getSelectionModel().addListSelectionListener(this);
        jTableListaArquivos.setColumnSelectionAllowed(true);
        jTableListaArquivos.setFillsViewportHeight(true);
        jTableListaArquivos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableListaArquivos.getTableHeader().setResizingAllowed(false);
        jTableListaArquivos.getTableHeader().setReorderingAllowed(false);
        jScrollPaneArquivos.setViewportView(jTableListaArquivos);
        jTableListaArquivos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTableListaArquivos.getColumnModel().getColumn(0).setMinWidth(50);
        jTableListaArquivos.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableListaArquivos.getColumnModel().getColumn(0).setMaxWidth(75);
        jTableListaArquivos.getColumnModel().getColumn(1).setMinWidth(250);
        jTableListaArquivos.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTableListaArquivos.getColumnModel().getColumn(1).setMaxWidth(300);

        jTreeArvore.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTreeArvore.addTreeSelectionListener(this);
        jTreeArvore.setModel(modeloArvore);
        jScrollPane1.setViewportView(jTreeArvore);

        jPanelInfoArquivo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabelNomeArquivo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelNomeArquivo.setText("Arquivo:");

        jButtonArquivoAbrir.setText("Abrir");
        jButtonArquivoAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArquivoAbrirActionPerformed(evt);
            }
        });

        jButtonArquivoRenomear.setText("Renomear");
        jButtonArquivoRenomear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArquivoRenomearActionPerformed(evt);
            }
        });

        jButtonArquivoExcluir.setText("Excluir");
        jButtonArquivoExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArquivoExcluirActionPerformed(evt);
            }
        });

        jTextFieldPath.setEnabled(false);

        jLabelUltModArquivo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelUltModArquivo.setText("ÚltimaModificação:");

        jLabelPathArquivo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelPathArquivo.setText("Path:");

        jTextFieldUltMod.setEnabled(false);

        jButtonArquivoImprimir.setText("Imprimir");
        jButtonArquivoImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArquivoImprimirActionPerformed(evt);
            }
        });

        jLabelIcon.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabelRotuloIcone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelRotuloIcone.setText("Ícone:");

        jLabelRotuloTamanho.setText("Tamanho:");

        jButtonCriptografar.setText("Criptografar");
        jButtonCriptografar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCriptografarActionPerformed(evt);
            }
        });

        jButtonDescriptografar.setText("Descriptografar");
        jButtonDescriptografar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDescriptografarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelInfoArquivoLayout = new javax.swing.GroupLayout(jPanelInfoArquivo);
        jPanelInfoArquivo.setLayout(jPanelInfoArquivoLayout);
        jPanelInfoArquivoLayout.setHorizontalGroup(
            jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoArquivoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoArquivoLayout.createSequentialGroup()
                        .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelUltModArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelInfoArquivoLayout.createSequentialGroup()
                                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelRotuloTamanho)
                                    .addComponent(jLabelRotuloIcone, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelTamanhoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelNomeArquivo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelPathArquivo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldNomeArquivo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldPath, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldUltMod, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(38, 38, 38))
                    .addGroup(jPanelInfoArquivoLayout.createSequentialGroup()
                        .addComponent(jButtonArquivoAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonArquivoRenomear, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonArquivoExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonArquivoImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCriptografar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDescriptografar)
                        .addContainerGap())))
        );
        jPanelInfoArquivoLayout.setVerticalGroup(
            jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoArquivoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonArquivoAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonArquivoRenomear, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonArquivoExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonArquivoImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCriptografar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDescriptografar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoArquivoLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldNomeArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelNomeArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelInfoArquivoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelRotuloIcone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldPath, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabelPathArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelRotuloTamanho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelTamanhoArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelInfoArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldUltMod, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelUltModArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        jMenuInserir.setText("Inserir Arquivo");
        jMenuInserir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuInserirMousePressed(evt);
            }
        });
        jMenuBar1.add(jMenuInserir);

        jMenuNovaPasta.setText("Nova Pasta");
        jMenuNovaPasta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuNovaPastaMousePressed(evt);
            }
        });
        jMenuBar1.add(jMenuNovaPasta);

        jMenuAbout.setText("About");
        jMenuAbout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuAboutMousePressed(evt);
            }
        });
        jMenuBar1.add(jMenuAbout);

        jMenuSair.setText("Sair");
        jMenuSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuSairMousePressed(evt);
            }
        });
        jMenuBar1.add(jMenuSair);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPaneArquivos, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                            .addComponent(jPanelInfoArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPaneArquivos, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelInfoArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-951)/2, (screenSize.height-615)/2, 951, 615);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonArquivoExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonArquivoExcluirActionPerformed
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult;                
        
        if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo;
                        
                        if(treeFile.isFile()){
                            dialogResult = JOptionPane.showConfirmDialog (null, "Você Deseja Realmente Excluir o Arquivo: " + treeFile.getName() + "?","Excluir Arquivo",dialogButton);

                            if(dialogResult != 0){
                                JOptionPane.showMessageDialog(null, "Exclusão de Arquivo Cancelada!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }

                            if (treeFile.delete()) {
                                JOptionPane.showMessageDialog(null, "Arquivo: " + treeFile.getName() + " excluído com sucesso!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao Excluir o Arquivo: " + treeFile.getName(), "Excluir Arquivo", JOptionPane.ERROR_MESSAGE);
                            }
                        } else if(treeFile.isDirectory()){
                            dialogResult = JOptionPane.showConfirmDialog (null, "Você Deseja Realmente Excluir a Pasta: " + treeFile.getName() + "?","Excluir Arquivo",dialogButton);

                            if(dialogResult != 0){
                                JOptionPane.showMessageDialog(null, "Exclusão de Pasta Cancelada!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }

                            if(DeleteDirectory(treeFile)){
                                JOptionPane.showMessageDialog(null, "Pasta: " + treeFile.getName() + " excluída com sucesso!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao Excluir a Pasta: " + treeFile.getName(), "Excluir Arquivo", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        
                        ClearJtreeArquivo();
                        AtualizarStatusBar();

                        jTextFieldNomeArquivo.setText("");
                        jTextFieldPath.setText("");
                        jTextFieldUltMod.setText("");

                        ClearCrud();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        } else {
            if (jTableListaArquivos.getSelectedRow() < 0) {
                return;
            }
            
            File arquivo = (File) jTableListaArquivos.getValueAt(jTableListaArquivos.getSelectedRow(), 2);

            if (arquivo.isFile()) {                       
                dialogResult = JOptionPane.showConfirmDialog (null, "Você Deseja Realmente Excluir o Arquivo: " + arquivo.getName() + "?","Excluir Arquivo",dialogButton);

                if(dialogResult != 0){
                    JOptionPane.showMessageDialog(null, "Exclusão de Arquivo Cancelada!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (arquivo.delete()) {
                    JOptionPane.showMessageDialog(null, "Arquivo: " + arquivo.getName() + " excluído com sucesso!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Excluir o Arquivo: " + arquivo.getName(), "Excluir Arquivo", JOptionPane.ERROR_MESSAGE);
                }
            } else if (arquivo.isDirectory()) {
                dialogResult = JOptionPane.showConfirmDialog (null, "Você Deseja Realmente Excluir a Pasta: " + arquivo.getName() + "?","Excluir Arquivo",dialogButton);

                if(dialogResult != 0){
                    JOptionPane.showMessageDialog(null, "Exclusão de Pasta Cancelada!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if(DeleteDirectory(arquivo)){
                    JOptionPane.showMessageDialog(null, "Pasta: " + arquivo.getName() + " excluída com sucesso!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Excluir a Pasta: " + arquivo.getName(), "Excluir Arquivo", JOptionPane.ERROR_MESSAGE);
                }
            }

            ClearJtreeArquivo();
            AtualizarStatusBar();

            jTextFieldNomeArquivo.setText("");
            jTextFieldPath.setText("");
            jTextFieldUltMod.setText("");

            ClearCrud();
        }
    }//GEN-LAST:event_jButtonArquivoExcluirActionPerformed

    private void jButtonArquivoRenomearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonArquivoRenomearActionPerformed
        if (jTextFieldNomeArquivo.getText().equals("")) {
            return;
        }
        
        if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo;
                        
                        File novoArquivo = new File(treeFile.getParent() + "\\" + jTextFieldNomeArquivo.getText());

                        if (treeFile.renameTo(novoArquivo)) {
                            JOptionPane.showMessageDialog(null, "Arquivo: " + treeFile.getName() + " renomeado com sucesso!", "Excluir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Renomear o Arquivo: " + treeFile.getName(), "Excluir Arquivo", JOptionPane.ERROR_MESSAGE);
                        }

                        ClearJtreeArquivo();
                        ListarArquivos(treeFile.getParent());

                        jTextFieldNomeArquivo.setText(novoArquivo.getName());
                        jTextFieldPath.setText(novoArquivo.getPath());
                        jTextFieldUltMod.setText(new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(novoArquivo.lastModified())));
                        
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        } else {        
            if (jTableListaArquivos.getSelectedRow() < 0) {
                return;
            }

            if (jTextFieldNomeArquivo.getText().equals("")) {
                return;
            }

            File arquivo = (File) jTableListaArquivos.getValueAt(jTableListaArquivos.getSelectedRow(), 2);
            File novoArquivo = new File(arquivo.getParent() + "\\" + jTextFieldNomeArquivo.getText());

            if (arquivo.renameTo(novoArquivo)) {
                JOptionPane.showMessageDialog(null, "Arquivo: " + arquivo.getName() + " renomeado com sucesso!", "Renomear Arquivo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Renomear o Arquivo: " + arquivo.getName(), "Renomear Arquivo", JOptionPane.ERROR_MESSAGE);
            }

            ClearJtreeArquivo();
            ListarArquivos(arquivo.getParent());

            jTextFieldNomeArquivo.setText(novoArquivo.getName());
            jTextFieldPath.setText(novoArquivo.getPath());
            jTextFieldUltMod.setText(new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(novoArquivo.lastModified())));
        }
    }//GEN-LAST:event_jButtonArquivoRenomearActionPerformed

    private void jMenuSairMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuSairMousePressed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_jMenuSairMousePressed

    private void jButtonArquivoAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonArquivoAbrirActionPerformed
        if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo;
                        
                        File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                        File arquivoExe = new File(arquivoRunable.getParent());
                        File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
                        File cmd = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + treeFile.getName());

                        try {
                            desktop.open(cmd);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao Abrir o Arquivo: " + cmd.getName(), "Abrir Arquivo", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        } else {
        
            if (jTableListaArquivos.getSelectedRow() < 0) {
                return;
            }

            File arquivo = (File) jTableListaArquivos.getValueAt(jTableListaArquivos.getSelectedRow(), 2);
            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            File arquivoExe = new File(arquivoRunable.getParent());
            File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
            File cmd = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + arquivo.getName());

            try {
                desktop.open(cmd);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao Abrir o Arquivo: " + cmd.getName(), "Abrir Arquivo", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_jButtonArquivoAbrirActionPerformed

    private void jMenuInserirMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuInserirMousePressed
        int returnVal = jFileChooserCaminhoArquivo.showOpenDialog(this);
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            
            if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo;
                        
                        if(treeFile.isDirectory()){
                        
                            File arquivo = jFileChooserCaminhoArquivo.getSelectedFile();

                            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                            File arquivoExe = new File(arquivoRunable.getParent());
                            File arquivoPastaRaiz = new File(treeFile.getPath().substring(1, pastaRaiz.length()));
                            File arquivoAlvo = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + treeFile.getName());

                            Path source = arquivo.toPath();
                            Path target = arquivoAlvo.toPath();

                            try {
                                Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                                JOptionPane.showMessageDialog(null, "Arquivo Copiado com sucesso!", "Inserir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao Copiar o Arquivo! " + arquivoAlvo.getPath(), "Inserir Arquivo", JOptionPane.ERROR_MESSAGE);
                            }
                                                        
                            AtualizarStatusBar();
                            ListarArquivos(pastaRaiz);
                            ClearJtreeArquivo();
                            ClearCrud();
                        }
                        else
                            return;
                    }
                }
            } else {
                
                File arquivo = jFileChooserCaminhoArquivo.getSelectedFile();              
       
                File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                File arquivoExe = new File(arquivoRunable.getParent());
                File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));      
                File arquivoAlvo = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath());
                
                Path source = arquivo.toPath();
                Path target = arquivoAlvo.toPath();
                
                try {
                    Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(null, "Arquivo Copiado com sucesso!", "Inserir Arquivo", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro ao Copiar o Arquivo! " + arquivoAlvo.getPath(), "Inserir Arquivo", JOptionPane.ERROR_MESSAGE);
                }
            }            
            
            AtualizarStatusBar();
            ListarArquivos(pastaRaiz);
            ClearJtreeArquivo();
            ClearCrud();
        }
        
    }//GEN-LAST:event_jMenuInserirMousePressed

    private void jButtonArquivoImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonArquivoImprimirActionPerformed
         if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo;
                        
                        File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                        File arquivoExe = new File(arquivoRunable.getParent());
                        File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
                        File cmd = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + treeFile.getName());

                        try {
                            desktop.print(cmd);
                        } catch (IOException ex) {
                            Logger.getLogger(GerenciadorArquivos.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
         } else {
        
            if (jTableListaArquivos.getSelectedRow() < 0) {
                return;
            }

            File arquivo = (File) jTableListaArquivos.getValueAt(jTableListaArquivos.getSelectedRow(), 2);
            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            File arquivoExe = new File(arquivoRunable.getParent());
            File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
            File cmd = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + arquivo.getName());

            try {
                desktop.print(cmd);
            } catch (IOException ex) {
                Logger.getLogger(GerenciadorArquivos.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
    }//GEN-LAST:event_jButtonArquivoImprimirActionPerformed

    private void jMenuNovaPastaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuNovaPastaMousePressed
        String nomeArquivo = JOptionPane.showInputDialog (this, "Digite o Nome da Pasta:","Nova Pasta",JOptionPane.PLAIN_MESSAGE);
        
        if(nomeArquivo == null || nomeArquivo.equals("")){
            JOptionPane.showMessageDialog(null, "O Nome da Pasta NÃO Pode Estar em Branco!", "Criar Pasta", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo; 
                        
                        if(treeFile.isDirectory()){                        
                            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                            File arquivoExe = new File(arquivoRunable.getParent());
                            File arquivoPastaRaiz = new File(treeFile.getPath().substring(1, pastaRaiz.length()));
                            File pasta = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + treeFile.getName() + "\\" + nomeArquivo);
                            
                            if(pasta.exists()){
                                JOptionPane.showMessageDialog(null, "Já Existe uma Pasta com Esse Nome!", "Criar Pasta", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            if(pasta.mkdir()){
                                JOptionPane.showMessageDialog(null, "Pasta: " + pasta.getName() + " criada com sucesso!", "Criar Pasta", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao Criar a Pasta: " + pasta.getName(), "Criar Pasta", JOptionPane.ERROR_MESSAGE);
                            }                            
                            
                            AtualizarStatusBar();
                            ClearJtreeArquivo();
                            ListarArquivos(treeFile.getParent());
                            ClearCrud();
                            
                        } else if(treeFile.isFile()){
                            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                            File arquivoExe = new File(arquivoRunable.getParent());
                            File arquivoPastaRaiz = new File(treeFile.getPath().substring(1, pastaRaiz.length()));
                            File pasta = new File(arquivoExe.getPath() + "\\" + treeFile.getParent() + "\\" + nomeArquivo);                           
                                             
                            if(pasta.exists()){
                                JOptionPane.showMessageDialog(null, "Já Existe uma Pasta com Esse Nome!", "Criar Pasta", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            if(pasta.mkdir()){
                                JOptionPane.showMessageDialog(null, "Pasta: " + pasta.getName() + " criada com sucesso!", "Criar Pasta", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao Criar a Pasta: " + pasta.getName(), "Criar Pasta", JOptionPane.ERROR_MESSAGE);
                            }
                            
                            AtualizarStatusBar();
                            ClearJtreeArquivo();
                            ListarArquivos(treeFile.getParent());
                            ClearCrud();
                        }
                            
                    }
                }
        } else {        
            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            File arquivoExe = new File(arquivoRunable.getParent());
            File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));      

            File pasta = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "/" + nomeArquivo);

            if(pasta.exists()){
                JOptionPane.showMessageDialog(null, "Já Existe uma Pasta com Esse Nome!", "Criar Pasta", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(pasta.mkdir()){
                JOptionPane.showMessageDialog(null, "Pasta: " + pasta.getName() + " criada com sucesso!", "Criar Pasta", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Criar a Pasta: " + pasta.getName(), "Criar Pasta", JOptionPane.ERROR_MESSAGE);
            }
            
            ClearJtreeArquivo();
            AtualizarStatusBar();
            ListarArquivos(pastaRaiz);
            ClearCrud();
        }        
        
    }//GEN-LAST:event_jMenuNovaPastaMousePressed

    private void jMenuAboutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuAboutMousePressed
        this.setEnabled(false);
        janelaAbout.setVisible(true);
    }//GEN-LAST:event_jMenuAboutMousePressed

    private void jButtonCriptografarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCriptografarActionPerformed
        if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo; 
                        
                        if(CifrarDescifrarArquivo(treeFile, 1)){
                            JOptionPane.showMessageDialog(null, "Arquivo: " + treeFile.getName() + " criptografado com sucesso!", "Criptografar Arquivo", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Cifrar o Arquivo: " + treeFile.getName(), "Criptografar Arquivo", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        AtualizarStatusBar();
                        ClearJtreeArquivo();
                        ListarArquivos(treeFile.getParent());
                        ClearCrud();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        } else {
        
            if (jTableListaArquivos.getSelectedRow() < 0) {
                return;
            }

            File arquivo = (File) jTableListaArquivos.getValueAt(jTableListaArquivos.getSelectedRow(), 2);
            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            File arquivoExe = new File(arquivoRunable.getParent());
            File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
            File arquivoCifrar = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + arquivo.getName());

            if(CifrarDescifrarArquivo(arquivoCifrar, 1)){
                JOptionPane.showMessageDialog(null, "Arquivo: " + arquivoCifrar.getName() + " criptografado com sucesso!", "Criptografar Arquivo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Criptografar o Arquivo: " + arquivoCifrar.getName(), "Criptografar Arquivo", JOptionPane.ERROR_MESSAGE);
            }
            
            ClearJtreeArquivo();
            AtualizarStatusBar();
            ListarArquivos(pastaRaiz);
            ClearCrud();
        }
    }//GEN-LAST:event_jButtonCriptografarActionPerformed

    private void jButtonDescriptografarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDescriptografarActionPerformed
        if (jTreeArvore.getLastSelectedPathComponent() != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();
                
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    
                    if (nodeInfo instanceof File) {
                        File treeFile = (File) nodeInfo;                       
                        
                        if(CifrarDescifrarArquivo(treeFile, 2)){
                            JOptionPane.showMessageDialog(null, "Arquivo: " + treeFile.getName() + " descriptografado com sucesso!", "Descriptografar Arquivo", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Descriptografar o Arquivo: " + treeFile.getName(), "Descriptografar Arquivo", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        AtualizarStatusBar();
                        ClearJtreeArquivo();
                        ListarArquivos(treeFile.getParent());
                        ClearCrud();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        } else {
        
            if (jTableListaArquivos.getSelectedRow() < 0) {
                return;
            }

            File arquivo = (File) jTableListaArquivos.getValueAt(jTableListaArquivos.getSelectedRow(), 2);
            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            File arquivoExe = new File(arquivoRunable.getParent());
            File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
            File arquivoCifrar = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + arquivo.getName());

            if(CifrarDescifrarArquivo(arquivoCifrar, 2)){
                JOptionPane.showMessageDialog(null, "Arquivo: " + arquivoCifrar.getName() + " descriptografado com sucesso!", "Descriptografar Arquivo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Descriptografar o Arquivo: " + arquivoCifrar.getName(), "Descriptografar Arquivo", JOptionPane.ERROR_MESSAGE);
            }
            
            ClearJtreeArquivo();
            AtualizarStatusBar();
            ListarArquivos(pastaRaiz);
            ClearCrud();
        }
    }//GEN-LAST:event_jButtonDescriptografarActionPerformed

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (jTreeArvore.getLastSelectedPathComponent() == null) {
            return;
        }
        
        ClearCrud();

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeArvore.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();

        if (nodeInfo instanceof File) {
            File arquivo = (File) nodeInfo;

            if(arquivo.isFile()){
                String dataUltMod = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(arquivo.lastModified()));
            
                jLabelNomeArquivo.setText("Arquivo:");
                jButtonArquivoImprimir.setEnabled(true);    
                jButtonArquivoAbrir.setEnabled(true);
                jButtonArquivoRenomear.setEnabled(true);
                jButtonArquivoExcluir.setEnabled(true);
                jButtonCriptografar.setEnabled(true);
                jButtonDescriptografar.setEnabled(true);
                jLabelIcon.setIcon(fileSystemView.getSystemIcon(arquivo));
                jLabelTamanhoArquivo.setText(String.valueOf(arquivo.length() / 1024) + "KB");
                jTextFieldNomeArquivo.setText(arquivo.getName());
                jTextFieldPath.setText(arquivo.getPath());
                jTextFieldUltMod.setText(dataUltMod);
            }
            else if (arquivo.isDirectory()) {
                String dataUltMod = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(arquivo.lastModified()));
            
                File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                File arquivoExe = new File(arquivoRunable.getParent());
                File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
                File diretorio = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + arquivo.getName());

                tamanhoPasta = "0";

                TamanhoPasta(diretorio.getPath());

                jLabelNomeArquivo.setText("Pasta:");
                jButtonArquivoImprimir.setEnabled(false);    
                jButtonArquivoAbrir.setEnabled(false);  
                jButtonArquivoRenomear.setEnabled(true);
                jButtonArquivoExcluir.setEnabled(true);
                jButtonCriptografar.setEnabled(false);
                jButtonDescriptografar.setEnabled(false);
                jLabelIcon.setIcon(fileSystemView.getSystemIcon(arquivo));
                jLabelTamanhoArquivo.setText(String.valueOf(Integer.parseInt(tamanhoPasta) / 1024) + "KB");
                jTextFieldNomeArquivo.setText(arquivo.getName());
                jTextFieldPath.setText(arquivo.getPath());
                jTextFieldUltMod.setText(dataUltMod);
            } else {
                ClearCrud();
            }
            
            ListarArquivos(arquivo.getPath());
        }

        if (node.isRoot()) {
            ClearCrud();
            ListarArquivosArvore(pastaRaiz, node);
            ListarArquivos(pastaRaiz);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (jTableListaArquivos.getSelectedRow() < 0) {
            return;
        }

        jTreeArvore.clearSelection();

        File arquivo = (File) jTableListaArquivos.getValueAt(jTableListaArquivos.getSelectedRow(), 2);
        
        if(arquivo.isFile()){
            String dataUltMod = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(arquivo.lastModified()));
            
            jLabelNomeArquivo.setText("Arquivo:");
            jButtonArquivoImprimir.setEnabled(true);    
            jButtonArquivoAbrir.setEnabled(true);
            jButtonArquivoRenomear.setEnabled(true);
            jButtonArquivoExcluir.setEnabled(true);
            jButtonCriptografar.setEnabled(true);
            jButtonDescriptografar.setEnabled(true);
            jLabelIcon.setIcon(fileSystemView.getSystemIcon(arquivo));
            jLabelTamanhoArquivo.setText(String.valueOf(arquivo.length() / 1024) + "KB");
            jTextFieldNomeArquivo.setText(arquivo.getName());
            jTextFieldPath.setText(arquivo.getPath());
            jTextFieldUltMod.setText(dataUltMod);
        } else if(arquivo.isDirectory()) {
            String dataUltMod = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(arquivo.lastModified()));
            
            File arquivoRunable = new File(GerenciadorArquivos.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            File arquivoExe = new File(arquivoRunable.getParent());
            File arquivoPastaRaiz = new File(pastaRaiz.substring(1, pastaRaiz.length()));
            File diretorio = new File(arquivoExe.getPath() + arquivoPastaRaiz.getPath() + "\\" + arquivo.getName());
            
            tamanhoPasta = "0";
            
            TamanhoPasta(diretorio.getPath());
            
            jLabelNomeArquivo.setText("Pasta:");
            jButtonArquivoImprimir.setEnabled(false);    
            jButtonArquivoAbrir.setEnabled(false);  
            jButtonArquivoRenomear.setEnabled(true);
            jButtonArquivoExcluir.setEnabled(true);
            jButtonCriptografar.setEnabled(false);
            jButtonDescriptografar.setEnabled(false);
            jLabelIcon.setIcon(fileSystemView.getSystemIcon(arquivo));
            jLabelTamanhoArquivo.setText(String.valueOf(Integer.parseInt(tamanhoPasta) / 1024) + "KB");
            jTextFieldNomeArquivo.setText(arquivo.getName());
            jTextFieldPath.setText(arquivo.getPath());
            jTextFieldUltMod.setText(dataUltMod);
        } else {
            ClearCrud();
        }        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonArquivoAbrir;
    private javax.swing.JButton jButtonArquivoExcluir;
    private javax.swing.JButton jButtonArquivoImprimir;
    private javax.swing.JButton jButtonArquivoRenomear;
    private javax.swing.JButton jButtonCriptografar;
    private javax.swing.JButton jButtonDescriptografar;
    private javax.swing.JFileChooser jFileChooserCaminhoArquivo;
    private javax.swing.JLabel jLabelIcon;
    private javax.swing.JLabel jLabelNomeArquivo;
    private javax.swing.JLabel jLabelPathArquivo;
    private javax.swing.JLabel jLabelRotuloIcone;
    private javax.swing.JLabel jLabelRotuloTamanho;
    private javax.swing.JLabel jLabelStatusBar;
    private javax.swing.JLabel jLabelTamanhoArquivo;
    private javax.swing.JLabel jLabelUltModArquivo;
    private javax.swing.JMenu jMenuAbout;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuInserir;
    private javax.swing.JMenu jMenuNovaPasta;
    private javax.swing.JMenu jMenuSair;
    private javax.swing.JPanel jPanelInfoArquivo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneArquivos;
    private javax.swing.JTable jTableListaArquivos;
    private javax.swing.JTextField jTextFieldNomeArquivo;
    private javax.swing.JTextField jTextFieldPath;
    private javax.swing.JTextField jTextFieldUltMod;
    private javax.swing.JTree jTreeArvore;
    // End of variables declaration//GEN-END:variables
}
