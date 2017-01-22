import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import SBPooler.cSBPooler;


/**
 * Created by Nuzzi on 1/15/17.
 */
public class SBPoolerGUI
{
   private JButton btnCreate;
   private JPanel pnlMain;
   private JTabbedPane tabPages;
   private JTable tblNames;
   private JPanel tabNames;
   private JPanel tabMain;
   private JButton btnImport;
   private JTextField edNFC;
   private JTextField edAFC;
   private JCheckBox cbJumble;
   private JTextField edLogFile;
   private JLabel lblNFC;
   private JLabel lblAFC;
   private JLabel lblLogFile;
   private JButton btnLogFile;
   private JLabel lblPostFix;
   private JTextField edPostFix;
   private JTextField edTitle;
   private JLabel lblTitle;
   private JTextField edQ1;
   private JTextField edQ2;
   private JTextField edQ3;
   private JTextField edQ4;
   private JLabel lblLabels;
   private JLabel lblQ1;
   private JLabel lblQ2;
   private JLabel lblQ3;
   private JLabel lblQ4;
   private JRadioButton rbQ4;
   private JRadioButton rbQ2;
   private JRadioButton rbQ1;
   private JLabel lblQuarters;
   private JButton btnSave;
   private cSBPooler pool;

   public SBPoolerGUI()
   {
      pool = new SBPooler();
      btnCreate.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed( ActionEvent e )
         {
            Date d = new Date();
            pool.log( "Program Start: " + d.toString());

            pool.afc_team = edAFC.getText();
            pool.nfc_team = edNFC.getText();
            pool.s_log = edLogFile.getText();
            pool.title = edTitle.getText();
            pool.s_labels[0] = edQ1.getText();
            pool.s_labels[1] = edQ2.getText();
            pool.s_labels[2] = edQ3.getText();
            pool.s_labels[3] = edQ4.getText();
            pool.q_cnt = ( rbQ4.isSelected() ? 4 : rbQ2.isSelected() ? 2 : 1 );

            for ( int i = 0; i < 100; i++ )
            {
               pool.add_name( tblNames.getValueAt( i, 1 ).toString());
            }

            if ( ! cbJumble.isSelected()) pool.jumble_names();

            pool.create_grid();
            pool.create_short_format();

            pool.log( "\nProcess Complete" );

            d = new Date();
            pool.log( "\nProgram Stop: " + d.toString());
         }
      } );

      btnImport.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed( ActionEvent e )
         {
            JFrame parentFrame = new JFrame();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Import Names.");
            fileChooser.setCurrentDirectory( new File( System.getProperty( "user.dir" )));

            int userSelection = fileChooser.showSaveDialog( parentFrame );

            if ( userSelection == JFileChooser.APPROVE_OPTION )
            {
               loadFile( fileChooser.getSelectedFile().getName());
            }

         }
      });


      btnLogFile.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed( ActionEvent e )
         {
            JFrame parentFrame = new JFrame();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify an Output Directory.");
            fileChooser.setCurrentDirectory( new File( System.getProperty( "user.dir" )));
            fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
            fileChooser.setAcceptAllFileFilterUsed(false);

            int userSelection = fileChooser.showSaveDialog( parentFrame );

            if ( userSelection == JFileChooser.APPROVE_OPTION )
            {
               edLogFile.setText( fileChooser.getCurrentDirectory().toString() );
            }
         }
      });
      btnSave.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed( ActionEvent e )
         {
            writeProperties();
         }
      });
   }

   private void loadFile( String fn )
   {
      try
      {
         FileReader fileReader = new FileReader( fn );
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         String str = null;
         int i = 0;
         while(( str = bufferedReader.readLine()) != null )
         {
            tblNames.setValueAt( str, i++, 1 );
         }
      }
      catch ( IOException e )
      {
         System.out.println( "Unable to open file '" + fn + "'" );

      }
   }

   public static void main( String[] args )
   {

      JFrame frame = new JFrame("Superbowl Pooler");
      frame.setContentPane(new SBPoolerGUI().pnlMain);
      frame.setMinimumSize(new Dimension(500, 500));
      frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
      frame.pack();
      frame.setVisible(true);
   }

   private void createUIComponents()
   {
      tblNames = new JTable( 100, 2 );

      TableColumn column = tblNames.getColumnModel().getColumn( 0 );
      column.setHeaderValue( "Index" );
      column.setMaxWidth( 50 );
      column.setResizable( false );

      column = tblNames.getColumnModel().getColumn( 1 );
      column.setHeaderValue( "Name" );

      for ( int i = 0; i < 100; i++ )
         tblNames.setValueAt( Integer.toString(i + 1 ), i, 0 );

      rbQ4 = new JRadioButton();
      rbQ2 = new JRadioButton();
      rbQ1 = new JRadioButton();

      readProperties();

   }

   private void readProperties()
   {
      Properties props = new Properties();
      try
      {
         FileInputStream fis = new FileInputStream("SBPooler.xml");
         props.loadFromXML( fis );

      }
      catch ( IOException e )
      {
      }
      edNFC = new JTextField( props.getProperty("NFC Team", "NFC" ));
      edAFC = new JTextField( props.getProperty("AFC Team", "AFC" ));
      edLogFile = new JTextField( props.getProperty("LogFile", "log.txt" ));
      edPostFix = new JTextField( props.getProperty("PostFix", "" ));
      edTitle = new JTextField( props.getProperty("Title", "Superbowl Pool" ));
      edQ1 = new JTextField( props.getProperty("Label1", "1st Quarter" ));
      edQ2 = new JTextField( props.getProperty("Label2", "Halftime" ));
      edQ3 = new JTextField( props.getProperty("Label3", "3rd Quarter" ));
      edQ4 = new JTextField( props.getProperty("Label4", "Final" ));
      int i =  Integer.parseInt( props.getProperty( "Quarters", "4" ));
      rbQ4.setSelected( i == 4 );
      rbQ2.setSelected( i == 2 );
      rbQ1.setSelected( i == 1 );
      boolean b = Boolean.parseBoolean( props.getProperty( "NoJumble", "false" ));
      cbJumble = new JCheckBox();
      cbJumble.setSelected( b );

   }

   private void writeProperties()
   {
      Properties props = new Properties();
      try
      {
         FileOutputStream fos = new FileOutputStream("SBPooler.xml");
         props.setProperty( "NFC Team", edNFC.getText());
         props.setProperty( "AFC Team", edAFC.getText());
         props.setProperty( "LogFile", edLogFile.getText());
         props.setProperty( "PostFix", edPostFix.getText());
         props.setProperty( "Title", edTitle.getText());
         props.setProperty( "Label1", edQ1.getText());
         props.setProperty( "Label2", edQ2.getText());
         props.setProperty( "Label3", edQ3.getText());
         props.setProperty( "Label4", edQ4.getText());
         props.setProperty( "Quarters", rbQ4.isSelected() ? "4" : rbQ2.isSelected() ? "2" : "1" );
         props.setProperty( "NoJumble", cbJumble.isSelected() ? "true" : "false" );

         props.storeToXML( fos, "" );

      }
      catch ( IOException e )
      {
      }

   }
}
