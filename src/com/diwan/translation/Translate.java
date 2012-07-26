/*------------------------------------------------------------------------------


                     COPYRIGHT 2001-2012 DIWAN SOFTWARE LTD
   
                    DIWAN SOFTWARE LTD PROPRIETARY INFORMATION
   
        This software is supplied under the terms of a license agreement
        or non-disclosure agreement with Diwan Software Ltd and may not
        be copied or disclosed or modified except in accordance with the
        terms of that agreement.
   
   
   
        FileName:       Translate.java
        Version:        1.0.5
        Description:    XML Parsing and Bing Translator Invocation

------------------------------------------------------------------------------
CHANGES
1.0.5 A.Allawi - First release to Zynga
------------------------------------------------------------------------------*/
package com.diwan.translation;

import com.diwan.BIDI.ShapeArabic;
import com.diwan.soap.SoapServiceStub;
import com.diwan.soap.SoapServiceStub.*;
import org.apache.axis2.AxisFault;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.String;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Reham Diwan Software Limited
 */
public class Translate {

    private String appid;
    private String contenttype;
    private String category;
    private String user;
    private String uri;
    private SoapServiceStub stub = null;
    private TranslateOptions options = null;

    public static final int TRANSLATE = 1;
    public static final int SHAPE = 2;
    public static final int TRANSLATE_AND_SHAPE = 3;
	public static final String[] FORCE = {
			"out_of_visit_energy",
			"viral_sent"
	};
	public static final String[] EXCEPTIONS = {
		    "MOTD",
		    "Tabs",
		    "Virals",
		    "mfs_search_text",
		    "Suspension_Line_One",
		    "Suspension_Line_Two",
		    "Suspension_Line_Three",
		    "Suspension_Line_Four",
		    "Suspension_Line_Five",
		    "Banned_Line_One",
		    "Banned_Line_Two",
		    "Banned_Line_Three",
		    "Banned_Line_Four",
		    "Warning_Line_One",
		    "Warning_Line_Two",
		    "Warning_Line_Three",
		    "Warning_Line_Four",
		    "Footer_Support",
		    "Footer_Forums",
		    "Footer_FanPage",
		    "Footer_PrivacyPolicy",
		    "Footer_TOS",
		    "Footer_ReportAbuse",
		    "Footer_Help"
    };

    /**
     * Constructs class with default parameters.
     */
    public Translate() {
        this("6C9A92CF0DDDEF484F4C4ECEA2C82D8CE591A2AD", "text/plain", "general", "diwan", null);
    }

    /**
     * Constructs class with specific settings
     * @param id
     *  The application id used by the Microsoft Translator API
     * @param type
     *  The format of the text being translated.
     *  The supported formats are "text/plain" and "text/html". Any HTML needs to be well-formed.
     * @param catg
     *  The category of the text to translate. The only supported category is "general".
     * @param use
     *  A string used to track the originator of submissions to the translator.
     * @param ur
     *  Optional. A string containing the content location of submitted translations.
     */
    public Translate(String id, String type, String catg, String use, String ur) {
        appid = id;
        contenttype = type;
        category = catg;
        user = use;
        uri = ur;
    }

    /**
     * Initialize the stub and add translation options
     * @throws TranslateFault
     */
    public void init() throws TranslateFault {
        try {
            stub = new SoapServiceStub();
            options = new SoapServiceStub.TranslateOptions();
            options.setCategory("general");
            options.setContentType("text/plain");
            options.setUser("siteuser");

        } catch (AxisFault e) {
            throw new TranslateFault(e.getReason());
        }
    }

    /**
     * Add translation to the translation memory
     * @param original
     *  text to translate from
     * @param text
     * translated text
     * @param from
     * the language code of the source
     *  @param to
     * the target language
     * @param rate
     * represents the quality rating for this text
     * @throws TranslateFault
     */
    public void addTranslation(String original, String text, String from, String to, int rate) throws TranslateFault {
        try {
            SoapServiceStub.AddTranslation addTranslation = new SoapServiceStub.AddTranslation();
            addTranslation.setAppId(appid);
            addTranslation.setOriginalText(original);
            addTranslation.setTranslatedText(text);
            addTranslation.setFrom(from);
            addTranslation.setTo(to);
            addTranslation.setRating(rate);
            addTranslation.setContentType(contenttype);
            addTranslation.setCategory(category);
            addTranslation.setUser(user);
            addTranslation.setUri(uri);
            stub.addTranslation(addTranslation);
            System.out.println("Your translation has been added");
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Breaks a piece of text into sentences and returns an array of integers that
     * contains the length in each sentence.
     * @param text
     * the text you need to split into sentences
     * @param language
     * the language code of the given text
     * @return array that contains length of each sentence
     * @throws TranslateFault
     */
    public int[] breakSentences(String text, String language) throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            long startTime = System.currentTimeMillis();
            SoapServiceStub.BreakSentences breakSentence = new SoapServiceStub.BreakSentences();
            breakSentence.setLanguage(language);
            breakSentence.setAppId(appid);
            breakSentence.setText(text);

            BreakSentencesResponse sentenceLen = stub.breakSentences(breakSentence);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            if (totalTime < 1200) {
                Thread.sleep(1200 - totalTime);
            }

            return sentenceLen.getBreakSentencesResult().get_int();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        } catch (InterruptedException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Identify the language of given text
     * @param text
     * the text to detect
     * @return the detected language
     * @throws TranslateFault
     */
    public String detect(String text) throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            String detectString = "I have no idea what this language may be";
            SoapServiceStub.Detect detect = new SoapServiceStub.Detect();
            detect.setAppId(appid);
            detect.setText(detectString);
            DetectResponse detectResponse = stub.detect(detect);
            return detectResponse.getDetectResult();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Identify the language of an array of string,Performs independent detection
     * of each individual array element and returns a result for each row of the array
     * @param texts
     * array of strings that need to detect
     * @return array of detected languages
     * @throws TranslateFault
     */
    public String[] detectArray(String[] texts) throws TranslateFault {

        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.DetectArray detectArray = new SoapServiceStub.DetectArray();
            SoapServiceStub.ArrayOfstring detectText = new SoapServiceStub.ArrayOfstring();
            detectText.setString(texts);
            detectArray.setAppId(appid);
            detectArray.setTexts(detectText);
            DetectArrayResponse detectedText = stub.detectArray(detectArray);
            return detectedText.getDetectArrayResult().getString();

        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }

    }

    /**
     * Returns a tokenized AppID which can be used as the AppID parameter in any method
     * @param minratingread
     * minimum rating translations require to be returned. Recommended is 5
     * as this will only include automatic translations and authority approved translations
     * @param maxratingwrite
     *  maximum rating that a user can write with using this token.
     * The recommended maxRatingWrite is from 1 to 4 for anonymous users, and from 6 to 10 for authoritative users whom you trust.
     * @param expireseconds
     * defines the duration in seconds from now that the token is valid. The value can be between 1 and 86400 (24 hours).
     * @return AppID value
     * @throws TranslateFault
     */
    public String getAppIdToken(int minratingread, int maxratingwrite, int expireseconds) throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetAppIdToken appIdToken = new SoapServiceStub.GetAppIdToken();
            appIdToken.setAppId(appid);
            appIdToken.setMinRatingRead(minratingread);
            appIdToken.setMaxRatingWrite(maxratingwrite);
            appIdToken.setExpireSeconds(expireseconds);
            GetAppIdTokenResponse appIdTokenRespons = stub.getAppIdToken(appIdToken);
            return appIdTokenRespons.getGetAppIdTokenResult();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Retrieves friendly names for the languages passed in languageCodes
     * @param locale
     * representing a combination of an ISO 639 two-letter lowercase culture code associated with a language
     * and an ISO 3166 two-letter uppercase subculture code to localize the language names or a ISO 639 lowercase culture code by itself.
     * @param codeString
     * representing the ISO 639-1 language codes to retrieve the friendly name for.
     * @return names for languages passed in languageCodes
     * @throws TranslateFault
     */
    public String[] getLanguagesNames(String locale, String[] codeString)
            throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetLanguageNames languageNames = new SoapServiceStub.GetLanguageNames();
            languageNames.setAppId(appid);

            SoapServiceStub.ArrayOfstring languageCodes = new SoapServiceStub.ArrayOfstring();
            languageCodes.setString(codeString);
            languageNames.setLanguageCodes(languageCodes);
            languageNames.setLocale(locale);
            SoapServiceStub.GetLanguageNamesResponse languageNamesResponse = stub.getLanguageNames(languageNames);
            return languageNamesResponse.getGetLanguageNamesResult().getString();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Retrieves the languages available for speech synthesis.
     * @return array of languages available for speak
     * @throws TranslateFault
     */
    public String[] getLanguageForSpeak() throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetLanguagesForSpeak languageSpeak = new SoapServiceStub.GetLanguagesForSpeak();
            languageSpeak.setAppId(appid);
            GetLanguagesForSpeakResponse languageSpeakA = stub.getLanguagesForSpeak(languageSpeak);
            return languageSpeakA.getGetLanguagesForSpeakResult().getString();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Obtain a list of language codes representing languages that are supported by the Translation Service.
     * @return array of languages available for translation
     * @throws TranslateFault
     */
    public String[] getLanguagesForTranslate() throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetLanguagesForTranslate languagesTranslate = new SoapServiceStub.GetLanguagesForTranslate();
            languagesTranslate.setAppId(appid);
            GetLanguagesForTranslateResponse languagesForTranslate = stub.getLanguagesForTranslate(languagesTranslate);
            return languagesForTranslate.getGetLanguagesForTranslateResult().getString();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Retrieves an array of translations for a given language pair from the store and the MT engine.
     * @param text
     *  text to translate.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @param maxtranslations
     * representing the maximum number of translations to return.
     * @return array of translations
     * @throws TranslateFault
     */
    public String[] getTranslations(String text, String from, String to, int maxtranslations)
            throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            String[] matches;
            SoapServiceStub.GetTranslations getTranslation = new SoapServiceStub.GetTranslations();
            getTranslation.setAppId(appid);
            getTranslation.setText(text);
            getTranslation.setFrom(from);
            getTranslation.setTo(to);
            getTranslation.setMaxTranslations(maxtranslations);
            getTranslation.setOptions(options);
            GetTranslationsResponse0 translationResponse = stub.getTranslations(getTranslation);

            TranslationMatch[] translationMatches = translationResponse.getGetTranslationsResult().getTranslations().getTranslationMatch();
            matches = new String[translationMatches.length];
            int i = 0;
            for (TranslationMatch theTranslationMatch : translationMatches) {
                matches[i++] = theTranslationMatch.getTranslatedText();
            }
            return matches;

        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }

    }

    /**
     * Retrieve multiple translation candidates for multiple source texts.
     * @param texts
     * array containing the texts for translation. All strings should be of the same language.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @param maxtranslations
     * representing the maximum number of translations to return.
     * @return array of strings of translations for each text
     * @throws TranslateFault
     */
    public String[] getTranslationsArray(String[] texts, String from, String to, int maxtranslations)
            throws TranslateFault {
        String[] matches = null;
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetTranslationsArray translationArray = new SoapServiceStub.GetTranslationsArray();
            SoapServiceStub.ArrayOfstring translateArrayString = new SoapServiceStub.ArrayOfstring();
            translateArrayString.setString(texts);
            translationArray.setAppId(appid);
            translationArray.setTexts(translateArrayString);
            translationArray.setFrom(from);
            translationArray.setTo(to);
            translationArray.setMaxTranslations(maxtranslations);
            translationArray.setOptions(options);
            GetTranslationsArrayResponse getTranslationResponses = stub.getTranslationsArray(translationArray);
            GetTranslationsResponse[] translationMatches = getTranslationResponses.getGetTranslationsArrayResult().getGetTranslationsResponse();
            matches = new String[translationMatches.length];
            int i = 0;
            for (GetTranslationsResponse translationResponse : translationMatches) {
                for (SoapServiceStub.TranslationMatch match : translationResponse.getTranslations().getTranslationMatch()) {
                    matches[i++] = match.getTranslatedText();
                }
            }

        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }

        return matches;
    }

    /**
     * Returns a string which is a URL to a wave stream of the passed-in text being spoken in the desired language.
     * @param text
     * Containing a sentence or sentences of the specified language to be spoken for the wave stream.
     * @param language
     * representing the supported language code to speak the text in.
     * @param format
     * A string specifying the content-type ID. The default value is "audio/wav" which is the only currently allowed value.
     * @return URL to wave stream of text spoken
     * @throws TranslateFault
     */
    public String speak(String text, String language, String format)
            throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.Speak speak = new SoapServiceStub.Speak();
            speak.setAppId(appid);
            speak.setText(text);
            speak.setLanguage(language);
            speak.setFormat(format);
            SpeakResponse speakOut = stub.speak(speak);
            return speakOut.getSpeakResult();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Translates a text string from one language to another.
     * @param text
     * string representing the text to translate.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @return the translated line
     * @throws TranslateFault
     */
    public String translateLine(String text, String from, String to) throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            long startTime = System.currentTimeMillis();
            SoapServiceStub.Translate translate = new SoapServiceStub.Translate();
            translate.setAppId(appid);
            translate.setText(text);
            translate.setFrom(from);
            translate.setTo(to);
            TranslateResponse result;
            result = stub.translate(translate);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            if (totalTime < 1200) {
                Thread.sleep(1200 - totalTime);
            }
            return result.getTranslateResult();
        }
        catch (InterruptedException e) {
            throw new TranslateFault(e.getMessage());
        } catch (RemoteException e) {
            if (e.getMessage().contains("NoTranslationFound")) {
                return text;
            } else if (e.getMessage().contains("V2_Soap")) {
                try {
                    Thread.sleep(60000);
                    SoapServiceStub.Translate translate = new SoapServiceStub.Translate();
                    translate.setAppId(appid);
                    translate.setText(text);
                    translate.setFrom(from);
                    translate.setTo(to);
                    TranslateResponse result;
                    result = stub.translate(translate);
                    return result.getTranslateResult();
                } catch (InterruptedException ex) {
                    throw new TranslateFault(e.getMessage());
                } catch (RemoteException ex) {
                    throw new TranslateFault(e.getMessage());
                }
            } else {
                throw new TranslateFault(e.getMessage());
            }
        }

    }

    /**
     * Retrieve translations for multiple source texts.
     * @param texts
     * array containing the texts for translation. All strings should be of the same language.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @return the translated array
     * @throws TranslateFault
     */
    public TranslateArrayResult[] translateArray(String[] texts, String from, String to) throws TranslateFault {
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.TranslateArray translateArray = new SoapServiceStub.TranslateArray();
            SoapServiceStub.ArrayOfstring transArray1 = new SoapServiceStub.ArrayOfstring();
            transArray1.setString(texts);
            translateArray.setAppId(appid);
            translateArray.setTexts(transArray1);
            translateArray.setFrom(from);
            translateArray.setTo(to);
            translateArray.setOptions(options);
            TranslateArrayResponse1 translateArrayResponse = stub.translateArray(translateArray);
            TranslateArrayResponse[] t = translateArrayResponse.getTranslateArrayResult().getTranslateArrayResponse();
            TranslateArrayResult[] result = new TranslateArrayResult[t.length];
            int i = 0;
            for (TranslateArrayResponse t1 : t) {
                result[i++] = new TranslateArrayResult(t1);
            }
            return result;
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Translates an array of bytes extracted from an alto file and returns an array
     * of bytes that contains the translation with addition of needed tags
     * @param in
     * array of bytes extracted from an alto file
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @return array of bytes of the xml file
     * @throws TranslateFault
     */
    public byte[] translateXML(byte[] in, String from, String to, int action) throws TranslateFault {
        ByteArrayOutputStream xmlbytesout = new ByteArrayOutputStream();
        XMLEventWriter writer;
        StringBuffer blockText = null;
        
        Boolean inTranslateable = false;
        Boolean inString = false;
        Boolean dontShapePkg = false;
	    Boolean force_shape = false;
        Boolean dontShapeString = false;
        boolean skipVariation = false;
        Boolean stopNow = false;
        Boolean inPackage = false;

	    HashSet exceptions = new HashSet (Arrays.asList(EXCEPTIONS));
	    HashSet force_exceptions = new HashSet (Arrays.asList(FORCE));

	    if (stub == null) {
            init();
        }

        try {
            EventProducerConsumer ms = new EventProducerConsumer();
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(new ByteArrayInputStream(in));
            writer = XMLOutputFactory.newInstance().createXMLEventWriter(xmlbytesout, "utf-8");
            try {
                while (reader.hasNext() && !stopNow) {
                    XMLEvent event = (XMLEvent) reader.next();

                    if (event.getEventType() == XMLEvent.START_ELEMENT) {
                        StartElement startEvent = event.asStartElement();
                        String startEventName = startEvent.getName().getLocalPart();

                        if (startEventName.equalsIgnoreCase("pkg")) {
                            inPackage = true;
                            String name = iterateAttibutes(startEvent, "name");
                            if (exceptions.contains(name))
                                dontShapePkg = true;
                            System.out.print(name + ": ");
                        } else if (startEventName.equalsIgnoreCase("string")) {
                            inString = true;
                            String key = iterateAttibutes(startEvent, "key");
                            if (exceptions.contains(key))
                                dontShapeString = true;
	                        if (force_exceptions.contains(key))
		                        force_shape = true;
	                        System.out.print(key + ": ");
                        } else if (startEventName.equalsIgnoreCase("original")) {
                            inTranslateable = true;
                            blockText = new StringBuffer();
                        } else if (startEventName.equalsIgnoreCase("variation")
                                && action != TRANSLATE_AND_SHAPE) {
                            inTranslateable = true;
                            blockText = new StringBuffer();
                        }
                        else if (startEventName.equalsIgnoreCase("variation")) {
                            skipVariation = true;
                        }
                        writer.add(event);
                    }
                    else if (event.getEventType() == XMLEvent.END_ELEMENT) {
                        EndElement endEvent = event.asEndElement();
                        String endEventName = endEvent.getName().getLocalPart();
                        
                        if (endEventName.equalsIgnoreCase("pkg")) {
                            inPackage = false;
                            dontShapePkg = false;
                        }
                        else if (endEventName.equalsIgnoreCase("string")) {
                            inString = false;
                            dontShapeString = false;
	                        force_shape = false;
                        }
                        else if (endEventName.equalsIgnoreCase("original") || endEventName.equalsIgnoreCase("variation"))
                        {
                           // detect end of a text block
                            if (endEventName.equalsIgnoreCase("original")) {
                                inTranslateable = false;
                            } else if (endEventName.equalsIgnoreCase("variation")) {
                                skipVariation = false;
                            }
                            if (blockText.toString().length() > 0) {
	                            Boolean canShape = (!dontShapePkg && !dontShapeString) || force_shape;
                                if ((action == TRANSLATE_AND_SHAPE || action == SHAPE) && canShape)
                                {
                                    ShapeArabic shaper = new ShapeArabic(blockText.toString());
                                    writer.add(ms.getNewCharactersEvent(shaper.getReorderedAndShaped()));
                                } else {
                                    writer.add(ms.getNewCharactersEvent(blockText.toString()));
                                    System.out.println (blockText.toString());
                                }
                            }
                        }
                        writer.add(event);
                    } // I am at the closing text block tag so insert sentences
                    else if (event.getEventType() == XMLEvent.CHARACTERS && inTranslateable) 
                    {
                        Characters charEvent = event.asCharacters();
                        if (charEvent.isWhiteSpace()) {
                            writer.add(event);
                        }
                        // keep the old blockText if I am skipping variations
                        else if (!skipVariation) {
                            String theTranslateable = charEvent.getData();
                            StringTokenizer tokened = new StringTokenizer(theTranslateable, "\t\n\r\f{}", true);
                            Boolean inVariable = false;
                            while (tokened.hasMoreTokens()) {
                                String aToken = tokened.nextToken();
                                if (aToken.length() > 1 && !inVariable) {
                                    //only translate if the action is for translating
                                    if (action == TRANSLATE_AND_SHAPE || action == TRANSLATE) {
                                        String translatedString = translateLine(aToken, from, to);
                                        blockText.append(translatedString);
                                    } else {
                                        blockText.append(aToken);
                                    }
                                } else if (aToken.equals("}")) {
                                    inVariable = false;
                                    blockText.append(aToken);
                                } else if (aToken.equals("{") || inVariable) {
                                    inVariable = true;
                                    blockText.append(aToken);
                                } else {
                                    blockText.append(aToken);
                                }
                            }
                        }
                    } else {
                        writer.add(event);
                    }
                }
            } catch (Exception ex) {
                //don't quit on failure here just report and stop normally
                ex.printStackTrace(System.out);
            } finally {
               writer.flush();
            }
        }
        catch (XMLStreamException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(System.out);
            throw new TranslateFault(ex.getMessage());
        }

        return xmlbytesout.toByteArray();
    }

    /**
     * Iterate the attributes of start elements until getting a specific attribute and then return the value of that attribute
     * @param startEvent
     * the start element we are searching for
     * @param theAttribute
     * string that contains the name of the attribute
     * @return the value of the attribute
     */
    private static String iterateAttibutes(StartElement startEvent,
            String theAttribute) {
        String returnValue = null;
        Iterator<Attribute> blockAttributes = startEvent.getAttributes();
        while (blockAttributes.hasNext()) {
            Attribute attr = blockAttributes.next();
            if (attr.getName().getLocalPart().equalsIgnoreCase(theAttribute)) {
                returnValue = attr.getValue();
                break;
            }
        }

        return returnValue;
    }

    /**
     * @return the appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid
     *            the appid to set
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * @return the contenttype
     */
    public String getContenttype() {
        return contenttype;
    }

    /**
     * @param contenttype
     *            the contenttype to set
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the stub
     */
    public SoapServiceStub getStub() {
        return stub;
    }

    /**
     * @param stub
     *            the stub to set
     */
    public void setStub(SoapServiceStub stub) {
        this.stub = stub;
    }

    /**
     * @return the options
     */
    public TranslateOptions getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(TranslateOptions options) {
        this.options = options;
    }

    private static class EventProducerConsumer {

        XMLEventFactory m_eventFactory = XMLEventFactory.newInstance();

        /** Creates a new instance of EventProducerConsumer */
        public EventProducerConsumer() {
        }

        /**
         * @throws XMLStreamException
         * @throws IOException
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        /**
         * Create a new tag with name Sentence where it is a start element
         */
        public StartElement getNewSentenceEvent() {
            String name = "Sentence";
            return m_eventFactory.createStartElement("", null, name);
        }

        /**
         * Adding the sentence start ID
         * @param startId
         * the sentence start ID
         * @return attribute start
         */
        public Attribute getNewSentenceStartId(String startId) {
            Attribute start = m_eventFactory.createAttribute("Start", startId);
            return start;
        }

        /**
         * Adding the sentence end ID
         * @param endId
         * the sentence end ID
         * @return attribute end
         */
        public Attribute getNewSentenceEndId(String endId) {
            Attribute end = m_eventFactory.createAttribute("End", endId);
            return end;
        }

        /**
         * Adding characters
         * @param characters
         * Current character event.
         * @return Characters New Characters
         *            event.
         */
        public Characters getNewCharactersEvent(String characters) {
            return m_eventFactory.createCharacters(characters);
        }

        /**
         * Ending the sentence
         * @return the end element
         */
        public EndElement getSentenceEndEvent() {
            String name = "Sentence";
            return m_eventFactory.createEndElement("", null, name);
        }

        /**
         * Adding an alt event
         * @return Alt start element
         */
        public StartElement getNewAltEvent() {
            String name = "Alt";
            return m_eventFactory.createStartElement("", null, name);
        }

        /**
         * Get the Alt language
         * @param language
         * Alt language
         * @return the lang attribute
         */
        public Attribute getNewAltLang(String language) {
            Attribute lang = m_eventFactory.createAttribute("lang", language);
            return lang;
        }

        /**
         * Ending the Alt event
         * @return the Alt end element
         */
        public EndElement getAltEndEvent() {
            String name = "Alt";
            return m_eventFactory.createEndElement("", null, name);
        }
    }

    public static class TranslateArrayResult {

        /**
         * field for Error
         */
        protected java.lang.String localError;
        /**
         * field for From
         */
        protected java.lang.String localFrom;
        /*  This tracker boolean wil be used to detect whether the user called the set method
         *  for this attribute. It will be used to determine whether to include this field
         *  in the serialized XML
         */
        /**
         * field for OriginalTextSentenceLengths
         */
        protected int[] localOriginalTextSentenceLengths;
        /**
         * field for State
         */
        protected java.lang.String localState;
        /**
         * field for TranslatedText
         */
        protected java.lang.String localTranslatedText;
        /**
         * field for TranslatedTextSentenceLengths
         */
        protected int[] localTranslatedTextSentenceLengths;

        public TranslateArrayResult(TranslateArrayResponse t) {
            localError = t.getError();
            localState = t.getState();
            localFrom = t.getFrom();
            localOriginalTextSentenceLengths = t.getOriginalTextSentenceLengths().get_int();
            localTranslatedText = t.getTranslatedText();
            localTranslatedTextSentenceLengths = t.getTranslatedTextSentenceLengths().get_int();
        }

        /**
         * @return the localError
         */
        public java.lang.String getError() {
            return localError;
        }

        /**
         * @param localError the localError to set
         */
        public void setError(java.lang.String localError) {
            this.localError = localError;
        }

        /**
         * @return the localFrom
         */
        public java.lang.String getFrom() {
            return localFrom;
        }

        /**
         * @param localFrom the localFrom to set
         */
        public void setFrom(java.lang.String localFrom) {
            this.localFrom = localFrom;
        }

        /**
         * @return the localOriginalTextSentenceLengths
         */
        public int[] getOriginalTextSentenceLengths() {
            return localOriginalTextSentenceLengths;
        }

        /**
         * @param localOriginalTextSentenceLengths the localOriginalTextSentenceLengths to set
         */
        public void setOriginalTextSentenceLengths(int[] localOriginalTextSentenceLengths) {
            this.localOriginalTextSentenceLengths = localOriginalTextSentenceLengths;
        }

        /**
         * @return the localState
         */
        public java.lang.String getState() {
            return localState;
        }

        /**
         * @param localState the localState to set
         */
        public void setState(java.lang.String localState) {
            this.localState = localState;
        }

        /**
         * @return the localTranslatedText
         */
        public java.lang.String getTranslatedText() {
            return localTranslatedText;
        }

        /**
         * @param localTranslatedText the localTranslatedText to set
         */
        public void setTranslatedText(java.lang.String localTranslatedText) {
            this.localTranslatedText = localTranslatedText;
        }

        /**
         * @return the localTranslatedTextSentenceLengths
         */
        public int[] getTranslatedTextSentenceLengths() {
            return localTranslatedTextSentenceLengths;
        }

        /**
         * @param localTranslatedTextSentenceLengths the localTranslatedTextSentenceLengths to set
         */
        public void setTranslatedTextSentenceLengths(int[] localTranslatedTextSentenceLengths) {
            this.localTranslatedTextSentenceLengths = localTranslatedTextSentenceLengths;
        }
    }
}
