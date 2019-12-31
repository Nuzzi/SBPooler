package SBPooler; /**
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
import java.util.ArrayList;
import java.util.Random;
import javax.xml.transform.OutputKeys;

public class cSBPooler
{
   private FileWriter f_log = null;
   private ArrayList<String> c_names = new ArrayList<String>();
   private Random rn = new Random();

   public String s_log = "log.txt";
   public String s_dir = "";
   public String s_names = "names.txt";

   // AFC Team Name
   private String afc_team = "AFC";
   public String getAFC() { return afc_team; }
   public void setAFC( String afc_team ) { this.afc_team = afc_team; }

   // NFC Team Name
   private String nfc_team = "NFC";
   public String getNFC() { return nfc_team; }
   public void setNFC( String nfc_team ) { this.nfc_team = nfc_team; }

   // Postfix - additional text to add to the end of the generated files
   // Grid<postfix>.htm
   // Short_Format<postfix>.csv
   private String s_postfix = "";
   public String getPostfix() { return s_postfix; }
   public void setPostfix( String s_postfix ) { this.s_postfix = s_postfix; }

   // Title - The title of the webpage
   private String title = "Grid";
   public String getTitle() { return title; }
   public void setTitle( String title ) { this.title = title; }

   // Labels - The text for each quarter, half, and/or final.
   private String [] s_labels = { "1st Quarter", "Halftime", "3rd Quarter", "Final" };
   public String getLabel( int idx )
   {
      if ( idx >= 0  && idx < 4 )
         return s_labels[idx];
      else
         return "";
   }

   public void clear_names()
   {
      c_names.clear();
   }

   public void setLabel( int idx, String label )
   {
      if ( idx >= 0 && idx < 4 )
         s_labels[idx] = label;
   }

   // "Quarter" count - how many set of numbers to generate
   private int q_cnt = 4;
   public int getQCount() { return q_cnt; }
   public void setQCount( int cnt )
   {
      if ( cnt == 4 || cnt == 2 || cnt == 1 )
         this.q_cnt = cnt;
   }

   // Jumble Names - set this to false if you do not want to change the order of the names.
   private int i_jumbles = 1;
   public void setJumbles( int i ) { this.i_jumbles = i; }
   public int getJumbles() { return i_jumbles; }
   public boolean isJumbled() { return i_jumbles > 0; }

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

   public cSBPooler()
   {
   }

   public void log( String str )
   {
      try
      {
         if ( f_log == null )
            f_log = new FileWriter( s_log, false );
         else
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

   public void jumble_names()
   {
      if ( i_jumbles == 0 ) return;

      log( "\nJumbling Names" );

      for ( int j = 1; j <= i_jumbles; j++ )
      {
         log( "\nJumble " + j + ":\n" );
         for ( int i = 0; i < c_names.size(); i++ )
         {
            swap( i, rn.nextInt( c_names.size() ) );
         }
      }

      log( "\nNew List Looks Like:" );
      for ( int i = 0; i < c_names.size(); i++ )
         log( "Name " + ( i + 1 ) + ": " + c_names.get( i ) );

   }

   public int read_names()
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

   public boolean add_name( String name )
   {
      c_names.add( name );
      log( "Name " + c_names.size() + ": " + name );
      return true;
   }

   private Element add_element( Document xml, Element parent, String name, String txt )
   {
      Element ele = xml.createElement( name );
      if ( txt.length() > 0 ) ele.setTextContent( txt );
      parent.appendChild( ele );
      return ele;
   }

   public void create_short_format()
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
         writer.write( title + "\n"  );
         String append = "," + s_labels[0] + "," + s_labels[1] + ","  +  s_labels[2] + "," + s_labels[3] + ",";

         if ( q_cnt == 2 )
            append = "," + s_labels[1] + "," + s_labels[3] + ",";

         if ( q_cnt == 1 )
            append = "," + s_labels[3] + ",";

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

   public void create_grid()
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
         add_element( xml, head_node, "title",  title );

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
         table_col.setAttribute( "class", "clt q0" );
         table_col.setAttribute( "colspan", Integer.toString( q_cnt + 1 )); //

         table_col = add_element( xml, table_row, "td", "" ); // NFC team name
         for ( int i = 0; i < nfc_team.length(); i++ )
         {
            String s = table_col.getTextContent();
            table_col.setTextContent( s += nfc_team.substring( i, i + 1 ) + " " );
         }
         table_col.setAttribute( "class", "nfc_team" );
         table_col.setAttribute( "colspan", "10" );

         int q_factor = 4 / q_cnt;

         for ( int j = 0; j < q_cnt; j++ )
         {
            // Write NFC First Quarter Row
            table_row = add_element( xml, table_node, "tr", "" );

            table_col = add_element( xml, table_row, "td", "" );
            table_col.setAttribute( "class", "cl q" + Integer.toString( j ));
            table_col.setAttribute( "rowspan", Integer.toString( q_cnt - j ));

            String str;
            switch ( j )
            {
               case 0 :
                  str = ( q_cnt == 4 ? s_labels[0] : ( q_cnt == 2 ? s_labels[1] : s_labels[3] ));
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
                  str = ( q_cnt == 4 ? s_labels[1] : s_labels[3] );
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
                  table_col = add_element( xml, table_row, "td", s_labels[2] );
                  table_col.setAttribute( "class", "clt q3" );
                  table_col = add_element( xml, table_row, "td", "" );
                  table_col.setAttribute( "class", "ct q3" );
                  break;
               case 3 :
                  table_col = add_element( xml, table_row, "td", s_labels[3] );
                  table_col.setAttribute( "class", "clt q4" );
                  break;
            }

            for ( int i = 0; i <= 9; i++ )
            {
               table_col = add_element( xml, table_row, "td", Integer.toString( nfc_arr[j][i] ));
               String s = "nfc " + "q" + Integer.toString( j + 1 ) + " c" + Integer.toString( i );
               table_col.setAttribute( "class", s );
               s = "n" + Integer.toString((j + 1) * q_factor ) + Integer.toString( nfc_arr[j][i] );
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
               s = "a" + Integer.toString( (j + 1) * q_factor ) + Integer.toString( afc_arr[j][i] );
               table_col.setAttribute( "id", s );
            }

            for ( int j = 0; j <= 9; j++ )
            {
               table_col = add_element( xml, table_row, "td", c_names.get(( i * 10 ) + j ));
               String str = "r" + Integer.toString( i ) + " c" + Integer.toString( j );

               for ( int k = 0; k < q_cnt; k++ )
               {
                  str += " s" + Integer.toString(( k + 1 ) * q_factor );
                  str += Integer.toString( nfc_arr[k][j] );
                  str += Integer.toString( afc_arr[k][i] );
               }
               table_col.setAttribute( "class", str );
               table_col.setAttribute( "id", "i" + Integer.toString( i * 10 + j ));
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
}
