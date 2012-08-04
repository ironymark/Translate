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

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
			"FlashUpdate",
			"Help",
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
		    "Footer_Help",
			"Browser_Insufficient_Version_Firefox_Line_One",
			"Browser_Insufficient_Version_Firefox_Line_Two",
			"Browser_Insufficient_Version_Explorer_Line_One",
			"Browser_Insufficient_Version_Explorer_Line_Two",
			"Browser_Insufficient_Version_Explorer_Update_Link",
			"Browser_Insufficient_Version_Chrome_Line_One",
			"Browser_Insufficient_Version_Chrome_Line_Two",
			"Browser_Insufficient_Version_Chrome_Update_Link",
			"Browser_Insufficient_Version_Safari_Line_One",
			"Browser_Insufficient_Version_Safari_Line_Two",
			"Browser_Insufficient_Version_Safari_Update_Link",
			"Browser_Insufficient_Version_Opera_Line_One",
			"Browser_Insufficient_Version_Opera_Line_Two",
			"Browser_Insufficient_Version_Opera_Update_Link",
			"Browser_Insufficient_Version_Unknown_Line_One",
			"Browser_Insufficient_Version_Unknown_Line_Two",
			"Browser_Insufficient_Version_Unknown_Update_Link",
			"HUD_assets_left"
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
                                    blockText.append(aToken);
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
}
