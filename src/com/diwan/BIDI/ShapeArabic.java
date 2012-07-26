/*------------------------------------------------------------------------------


                     COPYRIGHT 2001-2012 DIWAN SOFTWARE LTD
   
                    DIWAN SOFTWARE LTD PROPRIETARY INFORMATION
   
        This software is supplied under the terms of a license agreement
        or non-disclosure agreement with Diwan Software Ltd and may not
        be copied or disclosed or modified except in accordance with the
        terms of that agreement.
   
   
   
        FileName:       ShapeArabic.java
        Version:        1.0.5
        Description:    Arabic glyph shaping code

------------------------------------------------------------------------------
CHANGES
1.0.5 A.Allawi - First release to Zynga
------------------------------------------------------------------------------*/
package com.diwan.BIDI;

import java.util.StringTokenizer;

/**
 *
 * @author adilmbpro
 */
public class ShapeArabic {

    private StringBuffer output;
    BidiCharmap charmap = BidiCharmap.ARABIC;

    public static final int no_Ligature = 0;
    public static final int first_Ligature = 1;
    public static final int isolated = 0;
    public static final int ending = 1;
    public static final int initial = 2;
    public static final int medial = 3;
    public static final int ISOLATED_ONLY = 4;
    public static final int sawHebrewLetter = 5;
    public static final int ARABIC_START = 0x600;


    static final char ligatures[] = {
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        0x0101,
        no_Ligature,
        0x0101,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        0x0103,
        no_Ligature,
        0x0101,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        0x0101,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature,
        no_Ligature
    };

    static final char arabicUnicode[][] = {
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0x060C, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0x061B, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0x061F, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe80, 0, 0, 0},
        {0xfe81, 0xfe82, 0, 0},
        {0xfe83, 0xfe84, 0, 0},
        {0xfe85, 0xfe86, 0, 0},
        {0xfe87, 0xfe88, 0, 0},
        {0xfe89, 0xfe8a, 0xfe8b, 0xfe8c},
        {0xfe8d, 0xfe8e, 0, 0},
        {0xfe8f, 0xfe90, 0xfe91, 0xfe92},
        {0xfe93, 0xfe94, 0, 0},
        {0xfe95, 0xfe96, 0xfe97, 0xfe98},
        {0xfe99, 0xfe9a, 0xfe9b, 0xfe9c},
        {0xfe9d, 0xfe9e, 0xfe9f, 0xfea0},
        {0xfea1, 0xfea2, 0xfea3, 0xfea4},
        {0xfea5, 0xfea6, 0xfea7, 0xfea8},
        {0xfea9, 0xfeaa, 0, 0},
        {0xfeab, 0xfeac, 0, 0},
        {0xfead, 0xfeae, 0, 0},
        {0xfeaf, 0xfeb0, 0, 0},
        {0xfeb1, 0xfeb2, 0xfeb3, 0xfeb4},
        {0xfeb5, 0xfeb6, 0xfeb7, 0xfeb8},
        {0xfeb9, 0xfeba, 0xfebb, 0xfebc},
        {0xfebd, 0xfebe, 0xfebf, 0xfec0},
        {0xfec1, 0xfec2, 0xfec3, 0xfec4},
        {0xfec5, 0xfec6, 0xfec7, 0xfec8},
        {0xfec9, 0xfeca, 0xfecb, 0xfecc},
        {0xfecd, 0xfece, 0xfecf, 0xfed0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe75, 0xfe75, 0xfe75, 0xfe75},
        {0xfed1, 0xfed2, 0xfed3, 0xfed4},
        {0xfed5, 0xfed6, 0xfed7, 0xfed8},
        {0xfed9, 0xfeda, 0xfedb, 0xfedc},
        {0xfedd, 0xfede, 0xfedf, 0xfee0},
        {0xfee1, 0xfee2, 0xfee3, 0xfee4},
        {0xfee5, 0xfee6, 0xfee7, 0xfee8},
        {0xfee9, 0xfeea, 0xfeeb, 0xfeec},
        {0xfeed, 0xfeee, 0, 0},
        {0xfeef, 0xfef0, 0, 0},
        {0xfef1, 0xfef2, 0xfef3, 0xfef4},
        {0x064B, 0, 0, 0},
        {0x064C, 0, 0, 0},
        {0x064D, 0, 0, 0},
        {0x064E, 0, 0, 0},
        {0x064F, 0, 0, 0},
        {0x0650, 0, 0, 0},
        {0x0651, 0, 0, 0},
        {0x0652, 0, 0, 0},
        {0x0653, 0, 0, 0},
        {0x0654, 0, 0, 0},
        {0x0655, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0x0660, 0, 0, 0},
        {0x0661, 0, 0, 0},
        {0x0662, 0, 0, 0},
        {0x0663, 0, 0, 0},
        {0x0664, 0, 0, 0},
        {0x0665, 0, 0, 0},
        {0x0666, 0, 0, 0},
        {0x0667, 0, 0, 0},
        {0x0668, 0, 0, 0},
        {0x0669, 0, 0, 0},
        {0x066A, 0, 0, 0},
        {0x066B, 0, 0, 0},
        {0x002c, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe04, 0xfe05, 0xfe06, 0xfe07},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe00, 0xfe01, 0xfe02, 0xfe03},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe08, 0xfe09, 0xfe0a, 0xfe0b},
        {0, 0, 0, 0},
        {0xfe18, 0xfe19, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe1a, 0xfe1b, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe1c, 0xfe1d, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe10, 0xfe11, 0xfe12, 0xfe13},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe14, 0xfe15, 0xfe16, 0xfe17},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe50, 0xfe51, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe0c, 0xfe0d, 0xfe0e, 0xfef},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0xfe1e, 0xfe1f, 0, 0}
    };

    private Boolean isAccent(char theChar)
    {
        Boolean result = false;
        if (theChar >= 0x064b && theChar <= 0x065f)
            result = true;
        return result;
    }
    
    public ShapeArabic(String input) {
        int shapingState = isolated;
        char chars[] = input.toCharArray();
        int numChars = chars.length;
        output = new StringBuffer(chars.length);
        char theNextChar;
        char accentNext;
        for (int n = 0; n < numChars; n++) {
            char theChar = chars[n];
            char theResultChar = theChar;

            if (n < numChars - 1)
                theNextChar = chars[n + 1];
            else
                theNextChar = ARABIC_START;

            accentNext = isAccent(theNextChar) ? theNextChar : 0;
            if (accentNext != 0) {
                if (n < numChars - 2)
                    theNextChar = chars[n + 2];
                else
                    theNextChar = ARABIC_START;
            }

            if (theNextChar < ARABIC_START || theNextChar > 0x6d2)
                theNextChar = ARABIC_START;

            if (theChar >= ARABIC_START && theChar <= 0x6d2) {

                if (shapingState == isolated || shapingState == ending) {
                    if (arabicUnicode[theChar - ARABIC_START][initial] != 0 && arabicUnicode[theNextChar - ARABIC_START][ending] != 0) {
                        theResultChar = arabicUnicode[theChar - ARABIC_START][initial];
                        shapingState = initial;
                    } else {
                        theResultChar = arabicUnicode[theChar - ARABIC_START][isolated];
                        shapingState = isolated;
                    }
                } else if (shapingState == initial || shapingState == medial) {
                    if (arabicUnicode[theChar - ARABIC_START][medial] != 0 && arabicUnicode[theNextChar - ARABIC_START][ending] != 0) {
                        theResultChar = arabicUnicode[theChar - ARABIC_START][medial];
                        shapingState = medial;
                    } else {
                        theResultChar = arabicUnicode[theChar - ARABIC_START][ending];
                        shapingState = ending;
                    }
                }

                /*ligatures*/
                if (ligatures[theChar - ARABIC_START] != no_Ligature) {
                    char ligatureChar = 0;
                    switch (theResultChar) {
                        case 0xfee0:
                            if (theNextChar == 0x0622) /*lam-alefmadda final*/ {
                                ligatureChar = 0xfef6;
                            } else if (theNextChar == 0x0623) /*lam-alefhamzaup final*/ {
                                ligatureChar = 0xfef8;
                            } else if (theNextChar == 0x0625) /*lam-alefhamzadown final*/ {
                                ligatureChar = 0xfefa;
                            } else if (theNextChar == 0x0627) /*lam-alef final*/ {
                                ligatureChar = 0xfefc;
                            }

                            if (ligatureChar != 0) {
                                shapingState = ending;
                            }

                            break;
                        case 0xfedf:
                            if (theNextChar == 0x0622) /*lam-alefmadda final*/ {
                                ligatureChar = 0xfef5;
                            } else if (theNextChar == 0x0623) /*lam-alefhamzaup final*/ {
                                ligatureChar = 0xfef7;
                            } else if (theNextChar == 0x0625) /*lam-alefhamzadown final*/ {
                                ligatureChar = 0xfef9;
                            } else if (theNextChar == 0x0627) /*lam-alef final*/ {
                                ligatureChar = 0xfefb;
                            }

                            if (ligatureChar != 0) {
                                shapingState = ending;
                            }

                            break;

                        default:
                            break;
                    }

                    if (ligatureChar != 0) {
                        theResultChar = ligatureChar;
                        n++;
                    }
                }
            }

            output.append(theResultChar);
            if (accentNext != 0) {
                output.append(accentNext);
                n++;
            }
        }
    }

    public String getShaped() {
        return output.toString();
    }

    public String getReorderedAndShaped() {
        StringBuilder result = new StringBuilder (output.length());
        StringTokenizer lines = new StringTokenizer(output.toString(), "\n\r", true);

        while (lines.hasMoreTokens()) {
            char [] outputArray = lines.nextToken().toCharArray();
            if (outputArray.length > 1) {
                byte[] codes = charmap.getCodes(outputArray);
                BidiReference bidi = new BidiReference(codes, (byte) 1);
                int[] reorder = bidi.getReordering(new int[]{codes.length});
                for (int n = 0; n < outputArray.length; n++) {
	                char c = outputArray[reorder[n]];
	                if (codes[reorder[n]] == BidiReference.R)
	                {
		                if (c == '{')
			                c = '}';
		                else if (c == '}')
		                    c = '{';
	                }
	                result.append(c);
                }
            }
            else
                result.append(outputArray);
        }
        System.out.println(result.toString());
        return result.toString();
    }
}
