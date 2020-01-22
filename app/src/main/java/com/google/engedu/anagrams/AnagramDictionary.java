/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static final String TAG = "someTag";
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<Integer, ArrayList<String>>();
    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            if (lettersToWord.containsKey(sortLetters(word))) {
                lettersToWord.get(sortLetters(word)).add(word);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                lettersToWord.put(sortLetters(word), list);
            }

            if (sizeToWord.containsKey(word.length())) {
                sizeToWord.get(word.length()).add(word);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                sizeToWord.put(word.length(), list);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word) || word.contains(base)) {
            return false;
        } else {
            return true;
        }
    }

    public String sortLetters(String word) {
        char[] ary = word.toCharArray();
        Arrays.sort(ary);


        String result = new String(ary);
        return result;
    }


    public List<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }


    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> item = lettersToWord.get(sortLetters(word));
        for (int i = 0; i < item.size(); i++) {
            for (char alpha = 'a'; alpha <= 'z'; alpha++) {
                result.add(item.get(i) + alpha);
            }
        }


        return result;
    }


    public String pickGoodStarterWord() {
        String curr = wordList.get(0);
        int j = 0;
        Random rand = new Random();
        int start = rand.nextInt(wordList.size());
        for (int i = start ; i < wordList.size(); i++) {
            if (wordList.get(i).length() == wordLength && (getAnagramsWithOneMoreLetter(wordList.get(i)).size() > MIN_NUM_ANAGRAMS)) {
                curr = wordList.get(i);
                break;
            }
            if (i==wordList.size() && j<start){
                i=j;
                if (wordList.get(j).length() == wordLength && (getAnagramsWithOneMoreLetter(wordList.get(j)).size() > MIN_NUM_ANAGRAMS)) {
                    curr = wordList.get(j);
                    break;
                }
                j++;

            }


        }

        Log.d(TAG, "start");
        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }
            return curr;

    }

}
