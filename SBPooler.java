/**
 * Created by Nuzzi on 12/30/16.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.xml.transform.OutputKeys;

class SBPooler
{


   private FileWriter f_log = null;
   private final String s_log = "log.txt";
   private String s_dir = "";
   private String s_log_final = "log.txt";
   private String s_names = "names.txt";
   private String afc_team = "AFC";
   private String nfc_team = "NFC";
   private String s_postfix = "";
   private int q_cnt = 4;
   private boolean b_jumble = true;
   private ArrayList<String> c_names = new ArrayList<String>();
   private Random rn = new Random();
   private String title = "Grid";

   private int [][] afc_arr = {
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }};
   private int [][] nfc_arr = {
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
           {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }};


   private SBPooler()
   {
      try
      {
         f_log = new FileWriter( s_log, false );
         f_log.close();
      }
      catch ( java.io.IOException e )
         {
            System.out.println( e.getMessage());
            System.exit( 0 );
         }
   }

   private void log( String str )
   {
      try
      {
         f_log = new FileWriter( s_log, true );
         f_log.write( str + "\n" );
         f_log.close();
      }
      catch ( java.io.IOException e )
      {
         System.out.println( e.getMessage());
         System.exit( 0 );
      }
   }

   private void swap( int a, int b )
   {
      log( "Swapping '" + c_names.get( a ) + "' at spot " + ( a + 1 ) + " for '" +
              c_names.get( b ) + "' at spot " + ( b + 1 ));
      String tmp = c_names.get( a );
      c_names.set( a, c_names.get( b ));
      c_names.set( b, tmp );
   }

   private void jumble_ints( int[] arr )
   {
      for ( int i : arr )
      {
         int pos = rn.nextInt( arr.length );
         int tmp = arr[i];
         arr[i] = arr[pos];
         arr[pos] = tmp;
      }
      String log_str = "New Order Looks Like: ";
      for ( int i = 0; i < arr.length; i++ )
      {
         log_str += arr[i];
         if ( i < arr.length - 1 )
            log_str = log_str + ", ";
      }
      log( log_str );
   }

   private void jumble_names()
   {
      log( "\nJumbling Names" );
      for ( int i = 0; i < c_names.size(); i++ )
      {
         swap( i, rn.nextInt( c_names.size()));
      }

      log( "\nNew List Looks Like:" );
      for ( int i = 0; i < c_names.size(); i++ )
         log( "Name " + ( i + 1 ) + ": " + c_names.get( i ));
   }

   private int read_names()
   {
      try
      {
         BufferedReader f_names = new BufferedReader( new FileReader( s_names ));
         String tmp_str;
         log( "\nGetting Player Names from " + s_names );

         while (( tmp_str = f_names.readLine()) != null )
         {

            if ( tmp_str.length() > 0 )
            {
               c_names.add( tmp_str );
               log( "Name " + c_names.size() + ": " + tmp_str );
            }
         }
         f_names.close();
         return c_names.size();
      }
      catch ( java.io.IOException e )
      {
         return 0;
      }
   }

   private Element add_element( Document xml, Element parent, String name, String txt )
   {
      Element ele = xml.createElement( name );
      if ( txt.length() > 0 ) ele.setTextContent( txt );
      parent.appendChild( ele );
      return ele;
   }

   private void create_short_format()
   {
      log( "\nCreating short format template to filename:" + s_dir + " Short_Format" + s_postfix + ".csv" );

      String[][][] short_format = new String[10][10][q_cnt];
      for ( int k = 0; k < q_cnt; k++ )
         for ( int n = 0; n < 10; n++ )
            for ( int a = 0; a < 10; a++ )
            {
               int _n = nfc_arr[k][n];
               int _a = afc_arr[k][a];
               short_format[_n][_a][k] = c_names.get( n + a * 10 );
            }

      try
      {
         FileWriter writer = new FileWriter( s_dir + "Short_Format" + s_postfix + ".csv" );
         String append = ",1st Quarter,Halftime,3rd Quarter,Final,";
         if ( q_cnt == 2 )
            append = ",Halftime,Final,";
         if ( q_cnt == 1 )
            append = ",Final,";
         writer.write( nfc_team + "," + afc_team + append );
         writer.write( nfc_team + "," + afc_team + append + "\n" );
         for ( int i = 0; i < 5; i++ )
            for ( int j = 0; j < 10; j++ )
            {
               writer.write( Integer.toString( i ) + "," + Integer.toString( j ) + "," );
               for ( int k = 0; k < q_cnt; k++ )
                  writer.write( short_format[i][j][k] + "," );

               writer.write( Integer.toString( i + 5 ) + "," + Integer.toString( j ) + "," );
               for ( int k = 0; k < q_cnt; k++ )
                  writer.write( short_format[i + 5][j][k] + "," );
               writer.write( "\n" );
            }

         writer.close();
      }
      catch ( IOException e )
      {
         System.out.println( e.getMessage());
      }
      log( "Short format created" );
   }

   private void create_grid()
   {

      for ( int i = 0; i < 4; i++ )
      {
         log( "\nJumbling AFC Quarter: " + ( i + 1 ));
         jumble_ints( afc_arr[i] );
         log( "\nJumbling NFC Quarter: " + ( i + 1 ));
         jumble_ints( nfc_arr[i] );
      }

      try
      {
         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

         //root elements
         Document xml = docBuilder.newDocument();


         Element root_node = xml.createElement( "html" );
         root_node.setAttribute( "xmlns", "http://www.w3.org/1999/xhtml" );
         xml.appendChild( root_node );

         log( "\nCreating Grid" );

         Element head_node = add_element( xml, root_node, "head", "" );
         add_element( xml, head_node, "title", "Superbowl Pool - " + title );

         Element link_node = add_element( xml, head_node, "link", "" );
         link_node.setAttribute( "rel", "stylesheet" );
         link_node.setAttribute( "type", "text/css" );
         link_node.setAttribute( "href", "main.css" );

         Element body_node = add_element( xml, root_node, "body", "" );
         body_node.setAttribute( "class", "squares" );

         add_element( xml, body_node, "h2", title );

         Element table_node = add_element( xml, body_node, "table", "" );
         table_node.setAttribute( "class", "grid" );
         table_node.setAttribute( "id", "grid" );

         // Write Header Table Row
         Element table_row = add_element( xml, table_node, "tr", "" );
         Element table_col = add_element( xml, table_row, "td", " " ); // Left, upper corner
         table_col.setAttribute( "class", "clt" );
         table_col.setAttribute( "colspan", Integer.toString( q_cnt + 1 )); //

         table_col = add_element( xml, table_row, "td", "" ); // NFC team name
         for ( int i = 0; i < nfc_team.length(); i++ )
         {
            String s = table_col.getTextContent();
            table_col.setTextContent( s += nfc_team.substring( i, i + 1 ) + " " );
         }
         table_col.setAttribute( "class", "nfc_team" );
         table_col.setAttribute( "colspan", "10" );

         for ( int j = 0; j < q_cnt; j++ )
         {
            // Write NFC First Quarter Row
            table_row = add_element( xml, table_node, "tr", "" );

            for ( int k = 0; k < ( j + 1 ); k++ )
            {
               table_col = add_element( xml, table_row, "td", "" );
               table_col.setAttribute( "class", "cl" );
            }
            String str;
            switch ( j )
            {
               case 0 :
                  str = ( q_cnt == 4 ? "1st Quarter" : ( q_cnt == 2 ? "Halftime" : "Final"));
                  table_col = add_element( xml, table_row, "td", str );
                  table_col.setAttribute( "class", "clt q1" );

                  if ( q_cnt > 1 )
                  {
                     table_col = add_element( xml, table_row, "td", "" );
                     table_col.setAttribute( "class", "ct q1" );
                     table_col.setAttribute( "colspan", Integer.toString( q_cnt - 1 - j ) );
                  }
                  break;
               case 1 :
                  str = ( q_cnt == 4 ? "Halftime" : "Final" );
                  table_col = add_element( xml, table_row, "td", str );
                  table_col.setAttribute( "class", "clt q2" );

                  if ( q_cnt > 2 )
                  {
                     table_col = add_element( xml, table_row, "td", "" );
                     table_col.setAttribute( "class", "ct q2" );
                     table_col.setAttribute( "colspan", Integer.toString( q_cnt - 1 - j ) );
                  }
                  break;
               case 2 :
                  table_col = add_element( xml, table_row, "td", "3rd Quarter" );
                  table_col.setAttribute( "class", "clt q3" );
                  table_col = add_element( xml, table_row, "td", "" );
                  table_col.setAttribute( "class", "ct q3" );
                  break;
               case 3 :
                  table_col = add_element( xml, table_row, "td", "Final" );
                  table_col.setAttribute( "class", "clt q4" );
                  break;
            }

            for ( int i = 0; i <= 9; i++ )
            {
               table_col = add_element( xml, table_row, "td", Integer.toString( nfc_arr[j][i] ));
               String s = "nfc " + "q" + Integer.toString( j + 1 ) + " c" + Integer.toString( i );
               table_col.setAttribute( "class", s );
               s = "n" + Integer.toString( j + 1 ) + Integer.toString( nfc_arr[j][i] );
               table_col.setAttribute( "id", s );
            }
         }


         // Write AFC rows and names
         table_row = add_element( xml, table_node, "tr", "" );
         table_col = add_element( xml, table_row, "td", "" );
         for ( int i = 0; i < afc_team.length(); i++ )
         {
            Element block = add_element( xml, table_col, "span", "" );
            block.setTextContent( afc_team.substring( i, i + 1 ));
         }

         table_col.setAttribute( "rowspan", "10" );
         table_col.setAttribute( "class", "afc_team" );

         for ( int i = 0; i <= 9; i++ )
         {
            if ( i > 0 ) table_row = add_element( xml, table_node, "tr", "" );

            for ( int j = 0; j < q_cnt; j++ )
            {
               table_col = add_element( xml, table_row, "td", Integer.toString( afc_arr[j][i] ) );
               String s = "afc " + "q" + Integer.toString( j + 1) + " r" + Integer.toString( i );
               table_col.setAttribute( "class", s );
               s = "a" + Integer.toString( j + 1) + Integer.toString( afc_arr[j][i] );
               table_col.setAttribute( "id", s );
            }

            for ( int j = 0; j <= 9; j++ )
            {
               table_col = add_element( xml, table_row, "td", c_names.get(( i * 10 ) + j ));
               String str = "r" + Integer.toString( i ) + " c" + Integer.toString( j );
               table_col.setAttribute( "class", str );
               table_col.setAttribute( "id", Integer.toString( i * 10 + j ));
               //short_format[nfc_arr[j]][afc_arr[i]][short_idx] = c_names.get(( i * 10 ) + j );
            }
         }

         // write the content into xml file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource( xml );
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         StreamResult result = new StreamResult( new File( s_dir + "Grid" + s_postfix + ".htm" ));

         transformer.transform( source, result );
      }
      catch ( ParserConfigurationException | TransformerException e )
      {
         System.out.println( e.getMessage());
      }

      log( "\n" + title + " table created and stored to " + s_dir + "Grid" + s_postfix + ".htm" );

   }


   public static void main( String[] args )
   {
      SBPooler pool = new SBPooler();
      for ( int i = 0; i < args.length; i++ )
      {
         if ( args[i].toLowerCase().equals( "/n" ))
         {
            pool.s_names = args[++i];
            pool.log( "Used Name File: " + pool.s_names );
         }
         else if ( args[i].toLowerCase().equals( "/l" ))
         {
            pool.s_log_final = args[++i];
            pool.log( "Used Log File: " + pool.s_log_final );
         }
         else if ( args[i].toLowerCase().equals( "/p" ))
         {
            pool.s_postfix = args[++i];
            pool.log( "Used Postfix: " + pool.s_postfix );
         }
         else if ( args[i].toLowerCase().equals( "/d" ))
         {
            pool.s_dir = args[++i];
            if (( ! pool.s_dir.endsWith( "/" )) && ( ! pool.s_dir.endsWith( "\\" )))
               pool.s_dir = pool.s_dir.concat( "/" );
            if ( pool.s_dir.length() > 0 )
            {
               File dir = new File( pool.s_dir );
               if ( ! dir.exists())
                  dir.mkdir();
            }
            pool.log( "Using Directory: " + pool.s_postfix );
         }
         else if ( args[i].toLowerCase().equals( "/afc" ))
         {
            pool.afc_team = args[++i];
            pool.log( "AFC Team Name: " + pool.afc_team );
         }
         else if ( args[i].toLowerCase().equals( "/nfc" ))
         {
            pool.nfc_team = args[++i];
            pool.log( "NFC Team Name: " + pool.nfc_team );
         }
         else if ( args[i].toLowerCase().equals( "/t" ))
         {
            pool.title = args[++i];
            pool.log( "Using Title: " + pool.title );
         }
         else if ( args[i].toLowerCase().equals( "/nj" ))
         {
            pool.b_jumble = false;
            pool.log( "Name jumbling disabled." );
         }
         else if ( args[i].toLowerCase().equals( "/q1" ))
         {
            pool.q_cnt = 1;
            pool.log( "Using only one set of numbers (Final)" );
         }
         else if ( args[i].toLowerCase().equals( "/q2" ))
         {
            pool.q_cnt = 2;
            pool.log( "Using only two sets of numbers (Halftime, Final)" );
         }
         else if ( args[i].toLowerCase().equals( "/?" ))
         {
            System.out.println( "\nSBPooler <options> </l file> </n file>" );
            System.out.println( "/n <file> - Names File (default: names.txt. Text file containing 100 names)" );
            System.out.println( "/l <file> - Log File (default: log.txt)" );
            System.out.println( "/p <text> - Postfix to the output file names" );
            System.out.println( "/d <text> - Directory to put the output files" );
            System.out.println( "/t <text> - Title (default: Grid)" );
            System.out.println( "/afc <text> - AFC Team Name (default: AFC)" );
            System.out.println( "/nfc <text> - NFC Team Name (default: NFC)" );
            System.out.println( "/nj - \"No Jumbling,\" do not randomize the input names list" );
            System.out.println( "/q1 - Only use one set of random numbers (Final)" );
            System.out.println( "/q2 - Only use two sets of random numbers (Halftime, Final)." );
            return;
         }

      }

      File f = new File( pool.s_names );
      if ( !f.exists())
      {
         System.out.println( "\nPlease create a \"names.txt\" file or specify a valid names file.\n" );
         return;
      }

      Date d = new Date();
      pool.log( "Program Start: " + d.toString());

      if ( pool.read_names() != 100 )
      {
         String tmp_str = "Number of names is different than 100, errors possible";
         System.out.println( tmp_str + "\n" );
         pool.log( tmp_str );
      }

      if ( pool.b_jumble ) pool.jumble_names();
      pool.create_grid();
      pool.create_short_format();

      System.out.println( "Process Complete" );
      pool.log( "\nProcess Complete" );

      d = new Date();
      pool.log( "\nProgram Stop: " + d.toString());

      if ( !pool.s_log_final.equals( pool.s_log ))
      {
         File flog = new File( pool.s_log );
         flog.renameTo( new File( pool.s_log_final ));
      }
   }
}
