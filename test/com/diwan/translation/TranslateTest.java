/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.diwan.translation;

import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author adilmbpro
 */
public class TranslateTest {
    String AppId = "6C9A92CF0DDDEF484F4C4ECEA2C82D8CE591A2AD";
    String[] textsArray = { "I want this translated", "to something", "in another language" };
	String text = "Translate this text into German";
	Translate t = null;

    public TranslateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Terminal output encoding is: " + System.getProperty("file.encoding"));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        t = new Translate(AppId, "text/plain", "general", "username", null);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of TranslateXML method, of class Translate.
     */
    @Test
    public void testTranslateXML() throws Exception {
        System.out.println("TranslateXML");
		File theXMLFile = new File("en_US.xml");
		byte[] xmlbytes = new byte[(int) theXMLFile.length()];
		FileInputStream fis = new FileInputStream(theXMLFile);
		fis.read(xmlbytes);
		byte[] xmlout = t.translateXML(xmlbytes, "en", "ar", Translate.TRANSLATE_AND_SHAPE);
		String value = new String(xmlout);
        FileOutputStream fos = new FileOutputStream("ar_EG.Test.xml");
        fos.write(xmlout);
        fos.close();
		System.out.println(value);
    }
}