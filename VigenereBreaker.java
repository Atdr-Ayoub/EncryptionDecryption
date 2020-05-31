import java.util.*;
import java.util.Arrays;
import java.util.List;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder test = new StringBuilder();
        for(int i= whichSlice; i < message.length() ; i += totalSlices){
           test.append(message.charAt(i));
        }
        return test.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for(int i=0; i < klength; i++){
            String currSlice = sliceString(encrypted, i, klength);
            int dkey = cc.getKey(currSlice);
            //System.out.println(dkey);
            key[i] = dkey;
        }
        //System.out.println(key);
        return key;
    }
    
    public void breakVigenere () {
        DirectoryResource dr = new DirectoryResource();
        HashMap<String, HashSet<String>> hm = new HashMap<String, HashSet<String>>();
        for(File f : dr.selectedFiles()){
            String fileName = f.getName();
            //System.out.println(fileName);
            FileResource fr = new FileResource(fileName);
            HashSet<String> hs = readDictionary(fr);
            
            if(!hm.containsKey(fileName)){
                hm.put(fileName, hs);
            }
  
        }
        FileResource fr = new FileResource();
        String text = fr.asString();
        
        String breakForAll =breakForAllLangs(text, hm);
        System.out.println("after all :\n" + breakForAll);
        
        /*
        FileResource fr = new FileResource();
        String text = fr.asString();
        
        FileResource fr1 = new FileResource();
        HashSet<String> myHash = readDictionary(fr1);
        
        String decrypt = breakForLanguage(text, myHash);
        System.out.println(decrypt);
        */
       
        //int[] test = tryKeyLength(text,4 , 'e');
        //VigenereCipher vc = new VigenereCipher(test);
        //String afterDecrypt = vc.decrypt(text);
        //String[] spliit = afterDecrypt.split("\n");
        //System.out.println("*********\n\n"+spliit[0]);
        //System.out.println("*********\n\n"+afterDecrypt);
    }
    
    
    
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> myHash = new HashSet();
        //System.out.println("heheee");
        for(String line : fr.lines()){
           String currLine = line.toLowerCase();
           //System.out.println(currLine);
           myHash.add(currLine);
        }
        //System.out.println(myHash);
        return myHash;
    }
    
    public int countWords(String message, HashSet<String> dictionary){
        int count = 0;
        String[] myWords = message.split("\\W+");
        for(int i=0; i < myWords.length; i++){
           String currWord = myWords[i];
           if(dictionary.contains(currWord.toLowerCase())){
              count += 1; 
           }
        }
        //System.out.println(count);
        return count;
    }
    
    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
        int maxKey = 0;
        int maxWords = 0 ;
        for(int i=1; i <= 100 ; i++){
           int[] test = tryKeyLength(encrypted ,i , 'e');
     
           VigenereCipher vc = new VigenereCipher(test);
           String afterDecrypt = vc.decrypt(encrypted);
           int howManyWords = countWords(afterDecrypt, dictionary);
           if(maxWords < howManyWords){
               maxWords = howManyWords ;
               maxKey = i ;
           }
        }
        //System.out.println("*******\nThe key wanted = "+ maxKey);
        //System.out.println("max words = "+ maxWords);
        char mostChar = mostCommonCharIn(dictionary);
        int[] test = tryKeyLength(encrypted , maxKey, mostChar);
        /*
           System.out.println("{");
           for(int j=0; j < test.length; j++){
               System.out.print(test[j]+",");
           }
           System.out.print("}");
        */   
        VigenereCipher vc = new VigenereCipher(test);
        String actualText = vc.decrypt(encrypted);
        //System.out.println(actualText);
        String[] text = actualText.split("\n");
        //return text[0] ;
        return actualText;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
        StringBuilder sb = new StringBuilder();
        CaesarCracker cc = new CaesarCracker();
        String alph = "abcdefghijklmnopqrstuvwxyz";
        for(String s: dictionary){
            sb = sb.append(s);

        }
        int[] countL = cc.countLetters(sb.toString());
        int max = cc.maxIndex(countL);
        System.out.println("the most common char : " + alph.charAt(max));
        return alph.charAt(max);
    }
    
    public String breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages){
        // String breakForLanguage(String encrypted, HashSet<String> dictionary)
        // int countWords(String message, HashSet<String> dictionary)
        String bestBreak = "";
        int bestCount = 0;
        String keyLanguage = "";
        for(String key : languages.keySet()){
            HashSet<String> myHash = languages.get(key);
            String breakL = breakForLanguage(encrypted, myHash);
            int countW = countWords(breakL, myHash);
            if(bestCount < countW){
                bestCount = countW;
                bestBreak = breakL;
                keyLanguage = key ;
            }
        }
         System.out.println("***************************************************");
         System.out.println("\nafter breaking :\n"+ bestBreak);
         System.out.println("\nWith the language : "+ keyLanguage);
         System.out.println("***************************************************");
        return bestBreak;
    }
    
    public void tester(){
        /*
       System.out.println(sliceString("abcdefghijklm", 0, 3) );
       System.out.println(sliceString("abcdefghijklm", 1, 3) );
       System.out.println(sliceString("abcdefghijklm", 2, 3) );
       System.out.println(sliceString("abcdefghijklm", 0, 4) );
        */
       
        /*
       FileResource fr = new FileResource();
       String text = fr.asString();
       int[] test = tryKeyLength(text, 4, 'e');
       System.out.println("{");
       for(int i=0; i < test.length; i++){
            System.out.print(test[i]+",");
       }
       System.out.print("}");
         */
        
       FileResource fr = new FileResource();
       //tryKeyLength(fr.asString(), 38, 'e');
       HashSet<String> hs = readDictionary(fr);
       char c = mostCommonCharIn(hs);
       
       
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
