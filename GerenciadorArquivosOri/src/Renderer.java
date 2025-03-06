import java.awt.Component;  
import java.io.File;  
import java.io.FileNotFoundException;  
  
import javax.swing.Icon;  
import javax.swing.ImageIcon;  
import javax.swing.JLabel;  
import javax.swing.JTable;  
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;  
  
import sun.awt.shell.ShellFolder;  

class Renderer extends DefaultTableCellRenderer {

    private File[] file;  
    private Icon icon;
    private FileSystemView fileSystemView;
      
    public Renderer(){}  
      
    public Renderer(File[] file) {  
        this.file = file;  
        fileSystemView = FileSystemView.getFileSystemView();
    }  
      
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rows, int column) {  
          
        for(int row = 0 ; row < file.length ; row++ ) {  
          
            if(value instanceof JLabel && row == rows && column == 0) {  
                  
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rows, column);     
                
                setIcon(fileSystemView.getSystemIcon(file[row]));                 
                setText("");  
                setHorizontalAlignment(JLabel.CENTER);
            }  
        }  
        return this;  
    }  
     // The following methods override the defaults for performance reasons  
     public void validate() {}  
     public void revalidate() {}  
     protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}  
     public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

}