
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Hernandes
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    JFrame janelaPrincipal;
    String verLogin;
    String idUsuario;
    String nomeUsuario;
    String nivelUsuario;

    public Login() {
        verLogin = new String("");
        idUsuario = new String("");
        nomeUsuario = new String("");
        nivelUsuario = new String("");
        
        initComponents();
    }

    public void setJanelaPrincipal(JFrame principal) {
        janelaPrincipal = principal;
    }

    public void escreveArquivoDisco(String nomeArquivo) {
        FileOutputStream arquivo;
        PrintStream escritor;

        try {
            arquivo = new FileOutputStream(nomeArquivo);
            escritor = new PrintStream(arquivo);
            escritor.print(jTextFieldUsuario.getText() + "=" + jPasswordFieldSenha.getText() + ";");
            arquivo.close();
        } catch (IOException erro) {
            JOptionPane.showMessageDialog(null, "Nâo Foi Possível Escrever o Arquivo no Disco!", "Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recebeDados() {
        File arquivo = new File(Login.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        FileInputStream arquivoRetorno;
        byte[] buffer = new byte[1000];

        do {
            try {
                arquivoRetorno = new FileInputStream(arquivo.getParent() + "/respostaDados.txt");                           
                char c = 0;
                
                while(c != ';'){
                    c = (char) arquivoRetorno.read();
                    
                    if(c != ';'){                       
                        verLogin += c;
                    }
                }
                
                if(verLogin.equals("true")){
                
                    while(c != '='){
                        c = (char) arquivoRetorno.read();

                        if(c != '='){                       
                            idUsuario += c;
                        }
                    }

                    while(c != '#'){
                        c = (char) arquivoRetorno.read();

                        if(c != '#'){                       
                            nomeUsuario += c;
                        }
                    }

                    while(c != '$'){
                        c = (char) arquivoRetorno.read();

                        if(c != '$'){                       
                            nivelUsuario += c;
                        }
                    }
                }
                    
                arquivoRetorno.close();
                File arquivoTemp = new File(arquivo.getParent() + "/respostaDados.txt");
                arquivoTemp.delete();

                if (verLogin.equals("true")) {               
                    //JOptionPane.showMessageDialog(null, "Logado Com Sucesso!", "Login", JOptionPane.INFORMATION_MESSAGE);
                    GerenciadorArquivos gerenciador = (GerenciadorArquivos) janelaPrincipal;
                    gerenciador.setNomeUsuario(nomeUsuario);
                    gerenciador.setIdUsuario(idUsuario);
                    gerenciador.setNivelUsuario(nivelUsuario);
                    gerenciador.construirJanela();
                    this.setVisible(false);                    
                    janelaPrincipal.setVisible(true);
                } else if (verLogin.equals("false")) {
                    JOptionPane.showMessageDialog(null, "Usuário ou Senha Incorretos!", "Login", JOptionPane.ERROR_MESSAGE);
                    verLogin = "";
                    jTextFieldUsuario.setText("");
                    jPasswordFieldSenha.setText("");
                } else if(verLogin.equals("erro")) {
                    JOptionPane.showMessageDialog(null, "Erro de Leitura no Disco!", "Login", JOptionPane.ERROR_MESSAGE);
                    verLogin = "";
                    jTextFieldUsuario.setText("");
                    jPasswordFieldSenha.setText("");
                }                
            } catch (Exception ex) {
                arquivoRetorno = null;
            }
        } while (arquivoRetorno == null);      
        
    }

    private void verificaLogin() {
        if (jTextFieldUsuario.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Preencha o Campo Usuário!", "Login", JOptionPane.ERROR_MESSAGE);
            jTextFieldUsuario.requestFocus();
            return;
        } else if (jPasswordFieldSenha.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Preencha o Campo Senha!", "Login", JOptionPane.ERROR_MESSAGE);
            jPasswordFieldSenha.requestFocus();
            return;
        }

        File arquivo = new File(Login.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String path = arquivo.getParent();

        String linhaComando = new String("cmd.exe /C start " + path + "/autenticaUsuario.exe");
        escreveArquivoDisco("informaDados.txt");

        try {
            Runtime.getRuntime().exec(linhaComando);
            recebeDados();
        } catch (IOException erro) {
            JOptionPane.showMessageDialog(null, "O Arquivo autenticaUsuario.exe NÃO foi encontrado!", "Login", JOptionPane.ERROR_MESSAGE);
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

        jLabelLogin = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordFieldSenha = new javax.swing.JPasswordField();
        jButtonLimpar = new javax.swing.JButton();
        jButtonLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Security - Login");

        jLabelLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N

        jLabel1.setText("Usuário:");

        jLabel2.setText("Senha:");

        jButtonLimpar.setText("Reset");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonLogin.setText("Login");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(45, 45, 45)
                            .addComponent(jButtonLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPasswordFieldSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(169, 169, 169)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelLogin)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPasswordFieldSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-444)/2, (screenSize.height-321)/2, 444, 321);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        jTextFieldUsuario.setText("");
        jPasswordFieldSenha.setText("");
    }//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        verificaLogin();
    }//GEN-LAST:event_jButtonLoginActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JPasswordField jPasswordFieldSenha;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
