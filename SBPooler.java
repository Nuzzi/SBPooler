/**
 * Created by Nuzzi on 1/21/17.
 */

import SBPooler.cSBPooler;

import java.io.File;
import java.util.Date;

public class SBPooler extends cSBPooler
{
   public static int parseToInt(String stringToParse, int defaultValue)
   {
      int ret;
      try
      {
         ret = Integer.parseInt(stringToParse);
      }
      catch(NumberFormatException ex)
      {
         ret = defaultValue; //Use default value if parsing failed
      }
      return ret;
   }

   public static void main( String[] args )
   {
      SBPooler pool = new SBPooler();

      // Get log filename first if it exists
      for ( int i = 0; i < args.length; i++ )
         if ( args[i].toLowerCase().equals( "/l" ))
         {
            pool.s_log = args[++i];
            pool.log( "Used Log File: " + pool.s_log );
         }

      for ( int i = 0; i < args.length; i++ )
      {
         if ( args[i].toLowerCase().equals( "/n" ))
         {
            pool.s_names = args[++i];
            pool.log( "Used Name File: " + pool.s_names );
         }
         else if ( args[i].toLowerCase().equals( "/p" ))
         {
            pool.setPostfix( args[++i] );
            pool.log( "Used Postfix: " + pool.getPostfix());
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
            pool.log( "Using Directory: " + pool.s_dir );
         }
         else if ( args[i].toLowerCase().equals( "/afc" ))
         {
            pool.setAFC( args[++i] );
            pool.log( "AFC Team Name: " + pool.getAFC());
         }
         else if ( args[i].toLowerCase().equals( "/nfc" ))
         {
            pool.setNFC( args[++i] );
            pool.log( "NFC Team Name: " + pool.getNFC());
         }
         else if ( args[i].toLowerCase().equals( "/t" ))
         {
            pool.setTitle( args[++i] );
            pool.log( "Using Title: " + pool.getTitle());
         }
         else if ( args[i].toLowerCase().equals( "/t1" ))
         {
            pool.setLabel(0, args[++i] );
            //pool.log( "Using Title: " + pool.title );
         }
         else if ( args[i].toLowerCase().equals( "/t2" ))
         {
            pool.setLabel(1, args[++i] );
            //pool.log( "Using Title: " + pool.title );
         }
         else if ( args[i].toLowerCase().equals( "/t3" ))
         {
            pool.setLabel(2, args[++i] );
            //pool.log( "Using Title: " + pool.title );
         }
         else if ( args[i].toLowerCase().equals( "/t4" ))
         {
            pool.setLabel(3, args[++i] );
            //pool.log( "Using Title: " + pool.title );
         }
         else if ( args[i].toLowerCase().equals( "/j" ))
         {
            pool.setJumbles( parseToInt( args[++i], 1 ));
            pool.log( "Using " + pool.getJumbles() + " Jumbles" );
         }
         else if ( args[i].toLowerCase().equals( "/q1" ))
         {
            pool.setQCount( 1 );
            pool.log( "Using only one set of numbers (Final)" );
         }
         else if ( args[i].toLowerCase().equals( "/q2" ))
         {
            pool.setQCount( 2 );
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
            System.out.println( "/t1..4 <text> - \"Quarter\" Title (default: 1st Quarter, Halftime, 3rd Quarter, Final)" );
            System.out.println( "/afc <text> - AFC Team Name (default: AFC)" );
            System.out.println( "/nfc <text> - NFC Team Name (default: NFC)" );
            System.out.println( "/j <number> - Randomization iterations (default: 1)" );
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

      pool.jumble_names();
      pool.create_grid();
      // Short format no longer needed with the gris on one sheet
      // pool.create_short_format();

      System.out.println( "Process Complete" );
      pool.log( "\nProcess Complete" );

      d = new Date();
      pool.log( "\nProgram Stop: " + d.toString());

   }
}
