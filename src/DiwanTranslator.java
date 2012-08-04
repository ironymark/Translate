/*------------------------------------------------------------------------------


                     COPYRIGHT 2001-2012 DIWAN SOFTWARE LTD
   
                    DIWAN SOFTWARE LTD PROPRIETARY INFORMATION
   
        This software is supplied under the terms of a license agreement
        or non-disclosure agreement with Diwan Software Ltd and may not
        be copied or disclosed or modified except in accordance with the
        terms of that agreement.
   
   
   
        FileName:       DiwanTranslator.java
        Version:        1.0.5
        Description:    Main class for command line utility

------------------------------------------------------------------------------
CHANGES
1.0.5 A.Allawi - First release to Zynga
------------------------------------------------------------------------------*/
import com.diwan.translation.Translate;
import com.diwan.translation.TranslateFault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiwanTranslator {

	public static void main(String[] args) throws TranslateFault {

		String AppId = "6C9A92CF0DDDEF484F4C4ECEA2C82D8CE591A2AD";
		String[] textsArray = { "I want this translated", "to something",
				"in another language" };
        int action = Translate.SHAPE;

        if (args.length >= 2)
        {
            FileInputStream fis = null;
            try {
                System.out.println("TranslateXML");
                Translate t = new Translate(AppId, "text/plain", "general", "username", null);

                File theXMLFile = new File(args[0]);
                byte[] xmlbytes = new byte[(int) theXMLFile.length()];
                fis = new FileInputStream(theXMLFile);
                fis.read(xmlbytes);
                byte[] xmlout = t.translateXML(xmlbytes, "en", "ar", action);
                FileOutputStream fos = new FileOutputStream(args[1]);
                fos.write(xmlout);
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(DiwanTranslator.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(DiwanTranslator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.exit(0);
        }

		System.out.println("Diwan Translator Usage: in-file.xml out-file.xml SHAPE|TRANSLATE|BOTH");
	}

}
