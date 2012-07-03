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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.diwan.translation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiwanTranslator {

	public static void main(String[] args) throws TranslateFault {

		String AppId = "6C9A92CF0DDDEF484F4C4ECEA2C82D8CE591A2AD";
		String[] textsArray = { "I want this translated", "to something",
				"in another language" };
        int action = Translate.TRANSLATE_AND_SHAPE;

        if (args.length >= 2)
        {
            FileInputStream fis = null;
            try {
                System.out.println("TranslateXML");
                Translate t = new Translate(AppId, "text/plain", "general", "username", null);

                if (args.length > 2) {
                    if (args[2].equalsIgnoreCase("translate")) {
                        action = Translate.TRANSLATE;
                    }
                    else if (args[2].equalsIgnoreCase("shape")) {
                        action = Translate.SHAPE;
                    }
                }
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
        
		try {
            System.out.println("Terminal output encoding is: " + System.getProperty("file.encoding"));
			Translate t = new Translate(AppId, "text/plain", "general", "username", null);
			String text = "Translate this text into German";
			t.init();

			// Break Sentences
			String breaksentence = "Please break up this string into sentences! I would like this string to be broken into its respective sentences. Is this possible?";
			System.out.println("The text is: " + breaksentence);
			System.out
					.println("BreakSentences broke up the above sentence into "
							+ t.breakSentences(breaksentence, "en").length
							+ " sentences.");

			// Detect Language
			String detect = t
					.detect("I have no idea what this language may be");
			System.out.println("The detected language friendly code is: "
					+ detect);

			// Detect Language Array
			String[] detectArray = t.detectArray(textsArray);
			for (int i = 0; i < detectArray.length; i++) {
				System.out.println("The detected languages are: "
						+ detectArray[i]);
			}

			// App ID Token
			System.out.println("Your tokenized AppId is: "
					+ t.getAppIdToken(5, 4, 300));

			// Language Names
			String[] codeString = { "de", "en", "fr", "ko" };
			String languageNames[] = t.getLanguagesNames("en", codeString);
			for (int i = 0; i < languageNames.length; i++) {
				System.out.println("Language Name for " + codeString[i]
						+ " is " + languageNames[i]);
			}

			// Speak Languages
			String[] speakLanguages = t.getLanguageForSpeak();
			for (int i = 0; i < speakLanguages.length; i++) {
				System.out.println("The languages available are:"
						+ speakLanguages[i]);
			}

			// Translate Languages
			String[] translateLanguages = t.getLanguagesForTranslate();
			System.out.println("The languages available for translation are: ");
			for (int i = 0; i < translateLanguages.length; i++) {
				System.out.println(translateLanguages[i]);
			}

			// Translations
			String[] translations = t.getTranslations(
					"To change this template, choose Tools", "en", "de", 5);
			for (int i = 0; i < translations.length; i++) {
				System.out.println("The matches are :" + translations[i]);
			}
			// Translation Array

			String[] translationarray = t.getTranslationsArray(textsArray,
					"en", "fr", 5);
			for (int i = 0; i < translationarray.length; i++) {
				System.out.println("The array of matches : "
						+ translationarray[i]);
			}

			/*
			 * System.out.println("The translation array matches:" +
			 * t.getTranslationsArray(textsArray, "en", "fr")[0]);
			 */
			// Speak
			System.out.println("Speak Method : "
					+ t.speak("je suis pianiste", "fr", "audio/wav"));
			// Translate line
			try {

				BufferedReader in = new BufferedReader(new FileReader("in.txt"));
				BufferedWriter out = new BufferedWriter(new FileWriter(
						"out.txt"));
				String str;
				out.write(0xfeff);
				System.out.println("The translated lines are:");
				while ((str = in.readLine()) != null) {
					str = t.translateLine(str, "en", "ar");
					System.out.println(str);
					out.write(str+"\r");
				}

				out.close();
				in.close();
			} catch (IOException e) {
			}

			// Array Translation
			for (int i = 0; i < textsArray.length; i++) {
				System.out.println("Array Translation: "
						+ t.translateArray(textsArray, "en", "fr")[i]
								.getTranslatedText());
			}

		}

		catch (TranslateFault e) {
			e.printStackTrace();
		}
	}

}
