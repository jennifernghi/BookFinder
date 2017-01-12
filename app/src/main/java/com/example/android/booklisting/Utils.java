package com.example.android.booklisting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public final class Utils {
    final static String LOG_TAG = Utils.class.getSimpleName();
    final String JSON_RESPONSE = "{\n" +
            " \"kind\": \"books#volumes\",\n" +
            " \"totalItems\": 8438,\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"Ll1hhbKSw4cC\",\n" +
            "   \"etag\": \"aXPfaMwF1V4\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/Ll1hhbKSw4cC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Graphic Design, Print Culture, and the Eighteenth-Century Novel\",\n" +
            "    \"authors\": [\n" +
            "     \"Janine Barchas\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Cambridge University Press\",\n" +
            "    \"publishedDate\": \"2003-06-05\",\n" +
            "    \"description\": \"The uniformity of graphic design in contemporary paperback and critical editions of the eighteenth-century novel no longer conveys the visual appeal of early editions. Janine Barchas explains how the novel's material embodiment as printed book rivalled its narrative content in diversity and creativity in the first half of the eighteenth century. Prose writers such as Daniel Defoe, Jonathan Swift, and Henry and Sarah Fielding experimented with the novel's physical appearance from the beginning of its emergence in Britain.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"0521819083\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9780521819084\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": false,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 296,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Literary Criticism\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": false,\n" +
            "    \"contentVersion\": \"1.0.1.0.preview.1\",\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=Ll1hhbKSw4cC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=Ll1hhbKSw4cC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com/books?id=Ll1hhbKSw4cC&printsec=frontcover&dq=novel&hl=&cd=1&source=gbs_api\",\n" +
            "    \"infoLink\": \"http://books.google.com/books?id=Ll1hhbKSw4cC&dq=novel&hl=&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"http://books.google.com/books/about/Graphic_Design_Print_Culture_and_the_Eig.html?hl=&id=Ll1hhbKSw4cC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"saleability\": \"NOT_FOR_SALE\",\n" +
            "    \"isEbook\": false\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://books.google.com/books/reader?id=Ll1hhbKSw4cC&hl=&printsec=frontcover&output=reader&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"Barchas explains how from the beginning of the novel&#39;s emergence in Britain, prose writers experimented with its appearance.\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"1498GeN0W3EC\",\n" +
            "   \"etag\": \"wo2QbaeN1W8\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/1498GeN0W3EC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"The Novel in the Ancient World\",\n" +
            "    \"authors\": [\n" +
            "     \"Gareth L. Schmeling\"\n" +
            "    ],\n" +
            "    \"publisher\": \"BRILL\",\n" +
            "    \"publishedDate\": \"1996\",\n" +
            "    \"description\": \"This is the second publication in Brill's handbook series \\\"The Classical Tradition,\\\" The subject of this volume is that group of works of extended prose narrative fiction which bears many similarities to the modern novel and which appeared in the later classical periods in Greece and Rome. The ancient novel has enjoyed renewed popularity in recent years not only among students of literature, but also among those looking for new sources on the popular culture of antiquity and among scholars of religion. The volume surveys the new insights and approaches to the ancient novel which have emerged form the application of a variety of disciplines in the recent years. The 25 senior scholars contributing to the volume are drawn from a broad range of European and North American traditions of scholarship. Chapters cover the important issues dealing with the novel, novelists, novel-like works of fiction, their development, transformation, Christianisation and Nachleben, as well as a broad range of matters, from literary/philological to cultural/historical and religious, which concerns modern scholars in the field. This publication has also been published in paperback, please click here for details.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"9004096302\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9789004096301\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": false,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 876,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"History\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": false,\n" +
            "    \"contentVersion\": \"0.0.1.0.preview.1\",\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=1498GeN0W3EC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=1498GeN0W3EC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com/books?id=1498GeN0W3EC&printsec=frontcover&dq=novel&hl=&cd=2&source=gbs_api\",\n" +
            "    \"infoLink\": \"http://books.google.com/books?id=1498GeN0W3EC&dq=novel&hl=&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"http://books.google.com/books/about/The_Novel_in_the_Ancient_World.html?hl=&id=1498GeN0W3EC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"saleability\": \"NOT_FOR_SALE\",\n" +
            "    \"isEbook\": false\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://books.google.com/books/reader?id=1498GeN0W3EC&hl=&printsec=frontcover&output=reader&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"y2cDcsputw8C\",\n" +
            "   \"etag\": \"pHYtddS15Ww\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/y2cDcsputw8C\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"The Novel in Antiquity\",\n" +
            "    \"authors\": [\n" +
            "     \"Tomas Hägg\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Univ of California Press\",\n" +
            "    \"publishedDate\": \"1991\",\n" +
            "    \"description\": \"\\\"This much needed book is superlatively well done; it is both very readable and thoroughly scholarly, sympathetic to its topic, critical in approach, and authoritative.\\\"--B. P. Reardon, editor of Collected Ancient Greek Novels\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"0520076389\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9780520076389\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 264,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Literary Criticism\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": false,\n" +
            "    \"contentVersion\": \"0.0.3.0.preview.3\",\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=y2cDcsputw8C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=y2cDcsputw8C&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com/books?id=y2cDcsputw8C&printsec=frontcover&dq=novel&hl=&cd=3&source=gbs_api\",\n" +
            "    \"infoLink\": \"http://books.google.com/books?id=y2cDcsputw8C&dq=novel&hl=&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"http://books.google.com/books/about/The_Novel_in_Antiquity.html?hl=&id=y2cDcsputw8C\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"saleability\": \"NOT_FOR_SALE\",\n" +
            "    \"isEbook\": false\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com/books/download/The_Novel_in_Antiquity-sample-epub.acsm?id=y2cDcsputw8C&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://books.google.com/books/reader?id=y2cDcsputw8C&hl=&printsec=frontcover&output=reader&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"&quot;This much needed book is superlatively well done; it is both very readable and thoroughly scholarly, sympathetic to its topic, critical in approach, and authoritative.&quot;--B. P. Reardon, editor of Collected Ancient Greek Novels\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    private Utils(){}


    public ArrayList<Book> extractBook(){
        ArrayList<Book> books = null;
        String[] isbns = null;

        try {
            JSONObject root = new JSONObject(JSON_RESPONSE);
            JSONArray items = root.getJSONArray("items");
            for(int i=0; i<items.length();i++)
            {
                JSONObject item = (JSONObject) items.get(i);
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                // get book title
                String title = volumeInfo.getString("title");

                //get array of authors
                JSONArray authors = volumeInfo.getJSONArray("authors");

                //get array of isbn code
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                isbns = new String[industryIdentifiers.length()];
                for(int j=0; j<industryIdentifiers.length();j++)
                {
                  // JSONObject
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }
}
