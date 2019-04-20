package com.gmail.randzjx.words.translator;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Translation {
    Item item;

    public Translation(String word) {
        String response = translate(word);
        Log.d("response", String.valueOf(response));
        if (response != null)
            item = new Item(response);
    }

    public String translate(String word) {
        word = word.replaceAll(" ", "");
        Token generator = new Token();
        String token = generator.getToken(word);
        if (token == null) return null;
        System.out.println(token);
        StringBuilder path = new StringBuilder(), response = new StringBuilder();
        path.append("https://translate.google.com/translate_a/single?")
                .append("client=webapp&")
                .append("sl=en&")
                .append("tl=ru&")
                .append("dt=at&")
                .append("dt=bd&")
                .append("dt=ex&")
                .append("dt=ld&")
                .append("dt=md&")            //description
                .append("dt=qca&")
                .append("dt=rw&")            //default
                .append("dt=rm&")
                .append("dt=ss&")
                .append("dt=t&")
                .append("pc=1&")
                .append("otf=1&")
                .append("ssel=0&")
                .append("tsel=0&")
                .append("kc=1&")
                .append("tk=");
        path.append(token).append("&q=").append(word);
        try {
            URL translateURL = new URL(path.toString());

            HttpsURLConnection c = (HttpsURLConnection) translateURL.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");

            int responseCode = c.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(c.getErrorStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.d("logi", "error " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response.toString();
    }

    public String getDescription() {
        if (item != null && item.array != null && item.array.length > 12 && item.array[12].array != null) {
            Item[] a = item.array[12].array;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
//                sb.append(a[i].array[0].string).append("\n\n");
                sb.append(a[i].array[1].array[0].array[0].string).append('\n');
                sb.append('\n');
            }
            sb.delete(sb.length() - 2, sb.length());
            return sb.toString();
        }
        return "";
    }

    public String[] getAlternative() {
        if (item != null && item.array != null && item.array.length > 14) {
            String[] out = new String[item.array[14].array.length];
            for (int i = 0; i < out.length; i++) {
                out[i] = item.array[14].array[i].string;
            }
            return out;
        }
        return null;
    }

    public String getTranslate() {
        if (item != null && item.array != null) {
            if (item.array[1].array != null) {
                Item[] a = item.array[1].array;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < a.length; i++) {
//                    switch (a[i].array[0].string) {
//                        case "verb":
//                            sb.append("глагол\n\n");
//                            break;
//                        case "adjective":
//                            sb.append("прилагательное\n\n");
//                            break;
//                        case "noun":
//                            sb.append("существительное\n\n");
//
//                    }
                    if (a[i].array[1].array != null) {
                        for (int j = 0; j < a[i].array[1].array.length && j < 3; j++) {
                            sb.append(a[i].array[1].array[j].string).append('\n');
                        }
                    } else {
                        sb.append(a[i].array[1].string).append('\n');
                    }

                    sb.append('\n');
                }
                sb.delete(sb.length() - 2, sb.length());
                return sb.toString();
            } else if (item.array[0].array != null && item.array[0].array[0] != null && item.array[0].array[0].array[0] != null) {
                return item.array[0].array[0].array[0].string;
            }
        }
        return "";
    }

    public boolean success() {
        return item != null;
    }
}
