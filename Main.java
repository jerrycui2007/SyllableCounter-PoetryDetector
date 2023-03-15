/**
* [Main.java]
* Count the number of syllables per line in a poem, and try to indentify the type of poem it is
* @author Jerry Cui
* @version 1.0 Feb 27, 2023
*/
import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int totalNumberOfLines = 0;
    String syllableStructure = "";  // syllableStructure is a string that contains three-digit numbers (with opening 0s in case of a number < 100), the number of syllables in a line (syllableStructure[i] to syllableStructure[i+2] is the number of syllables in the i/3+1th line)
    int totalSyllables = 0;  // counts syllables in the entire poem
    int syllables;  // counts syllables per each line

    String string1;  // used during the algorithm
    String string2;
    int syllablesInWord;
    String character;
    boolean detectedSpace;
    String word;
    boolean wasOnVowel;
    int word_length;
    int vowelGroups;

    String vowels = "AEIOU";
    String consonants = "QWRTYPSDFGHJKLZXCVBNM";

    boolean couldBeSijo = true; // will become false after encountering a line not between 14-16 syllables

    String line = input.nextLine();
    while (! line.equals("quit")) {
      syllables = 0;
      totalNumberOfLines++;
      
      while (! line.equals("")) {
        // keep splitting the line at the first space. Eventually, the line will become empty, which means that the entire line has been read
        string1 = "";
        string2 = "";
        detectedSpace = false;
        
        for (int i = 0; i < line.length(); i++) {
          character = line.substring(i, i+1);
          if (!((character.equals(" ")) && (!detectedSpace))) {  // Keep adding characters to string1 until it encounters a space, at which point it ignores that space and adds all remaining characters to string2
            if (! detectedSpace) {
              if (!character.equals(",")) {  // ignore commas
                if (!character.equals(".")) {  // ignore periods
                  string1 = string1 + character;
                }
              }
            } else {
              string2 = string2 + character;
            }
          } else {
            detectedSpace = true;
          }
        }
        line = string2;

        syllablesInWord = 0;
        word = string1;
        word = word.toUpperCase();
        word_length = word.length();
        vowelGroups = 0;
        wasOnVowel = false;

        // Find number of vowel groups (continuous vowels) in the words
        for (int i = 0; i < word_length; i++) {
            character = word.substring(i, i + 1);
            if (!(vowels.indexOf(character) == -1)) {  // if character in vowels:
                if (!wasOnVowel) {
                    vowelGroups++;
                }
                wasOnVowel = true;
            } else {
                wasOnVowel = false;
            }
        }

        // If a "y" is surrounded by two consonants, it is a vowel
        for (int i = 1; i < word_length - 1; i++) {
            if (!(consonants.indexOf(word.charAt(i - 1)) == -1) && (word.charAt(i) == 'Y') && (!(consonants.indexOf(word.charAt(i + 1)) == -1))) {
                vowelGroups++;
            }
        }

        // A word that ends with -[consonant]y has an extra syllable
        if ((word.charAt(word_length - 1) == 'Y') && !(consonants.indexOf(word.charAt(word_length - 2)) == -1)) {
            vowelGroups++;
        }

        // If there are 0 vowel groups so far, count y as a vowel
        if (vowelGroups == 0) {
            for (int i = 0; i < word_length; i++) {
                character = word.substring(i, i + 1);
                if (character.equals("Y")) {
                    vowelGroups++;
                }
            }
        }

        syllablesInWord = syllablesInWord + vowelGroups;

        // Delete silent e that were counted as a syllable and the variations (y does'nt count for this)
        // Word ends in e that isn't -[consonant]le
        if (word.charAt(word_length - 1) == 'E') {
            // Exception: the word ends with -[consonant]le
            if (!(word.charAt(word_length - 2) == 'L') || (consonants.indexOf(word.charAt(word_length - 3)) == -1)) {
                if (!(word.equals("FINALE"))) {  // special exception
                    syllablesInWord--;
                }
            }
        }
        // Plural words that end in silent e
        if ((word.charAt(word_length - 1) == 'S') && (word.charAt(word_length - 2) == 'E')) {
            // Same exception as above
            if (!(word.charAt(word_length - 3) == 'L') || (consonants.indexOf(word.charAt(word_length - 4)) == -1)) {
                if (!(word.equals("FINALES"))) {  // special exception
                    syllablesInWord--;
                }
            }
        }
        // Same rule for -ly words that follow a silent e
        if ((word.charAt(word_length - 1) == 'Y') && (word.charAt(word_length - 2) == 'L') && (word.charAt(word_length - 3) == 'E')) {
            // Same exception as above
            if (!(word.charAt(word_length - 4) == 'L') || (consonants.indexOf(word.charAt(word_length - 5)) == -1)){
                syllablesInWord--;
            }
        }
        // Same rule but for -d (past tense) that follow a silent e, in which case instead of L, any consonant counts
        if (word_length >= 4) {  // too short words will result in error
          if ((word.charAt(word_length - 1) == 'D') && (word.charAt(word_length - 2) == 'E')) {
            // Same exception as above
            if ((consonants.indexOf(word.charAt(word_length - 3)) == -1) || (consonants.indexOf(word.charAt(word_length - 4)) == -1)) {
                // "naked" is an exception
                if (!word.equals("NAKED")) {
                    syllablesInWord--;
                }
            }
          }
          if ((vowels.indexOf(word.charAt(word_length - 4)) != -1) && (word.substring(word_length - 3, word_length).equals("TED"))) {  // words that end with [vowel]ted are exception to the above rule
            syllablesInWord++;
          }
          if (((word.substring(word_length - 4, word_length)).equals("AZES")) || (((word.substring(word_length - 4, word_length)).equals("ACES")))) {
            syllablesInWord++;  // these words have extra syllables
          }
        }
        if (word.indexOf("URLED") != -1) {  // exception to the past tense rule (i.e. curled)
          syllablesInWord--;
        }
        if (word.indexOf("ISPED") != -1) {  // exception to the past tense rule (i.e. crisped)
          syllablesInWord--;
        }

        // Special letter combinations that result in an extra syllable/less syllable
        if (word.indexOf("THM") == word_length - 3) {  // THM is the last letters of the word, then it is an extra syllable (i.e. rhythm but not rhythmic)
            syllablesInWord++;
        }
        if (word.indexOf("BYE") == word_length - 3) {  // Words that end with BYE with an extra syllable to counter the silent e rule
            syllablesInWord++;
        }
        if (word.indexOf("IO") != -1) {  // IO is two syllables (i.e. pious)
            // these are exceptions to the IO rule
            if ((word.indexOf("CIOUS") == -1) && (word.indexOf("VIOR") == -1) && (word.indexOf("VIOUR") == -1)) {
                syllablesInWord++;
            }
        }
        if (word.indexOf("TION") != -1) {  // TION is the only exception to the IO rule
            if ((!word.equals("CATION")) && (!word.equals("CATIONS"))) {  // cation (chemistry) is the only exception to this exception
                syllablesInWord--;
            }
        }
        if (word.indexOf("IA") != -1) {  // IA is two syllables (i.e. triangle)
          if ((!word.equals("DIAMOND")) && (!word.equals("DIAMONDS"))) {
            syllablesInWord++;
          } 
        }
        if ((word.indexOf("TIAN") != -1) || (word.indexOf("CIAL") != -1)) {  // TIAN and CIAL are exceptions to the IA rule (i.e. martian, special)
            syllablesInWord--;
        }
        if (word.indexOf("IE") != -1) {  // IE is two syllables (i.e. diet), except for IEF
                if (word.indexOf("IEF") == -1) {
                    syllablesInWord++;
                }
            }
        if (word.indexOf("CREAT") != -1) {  // only case of EA that is two syllables
            syllablesInWord++;
        }
        if (word.indexOf("UA") != -1) {  // UA is two syllables (i.e. situation)
            syllablesInWord++;
        }
        if (word.indexOf("UI") != -1) {  // UI is two syllables (i.e. required), except for UICK, or QUIT
                if ((word.indexOf("UICK") == -1) && (word.indexOf("QUIT") == -1)) {
                    syllablesInWord++;
                }
            }
        if (word.indexOf("EODE") != -1) {  // EO is two syllables only if followed by DE (i.e. geode)
            syllablesInWord++;
        }
        if (word.indexOf("IUM") != -1) {  // IU is two syllables if followed by M (i.e. potassium)
            syllablesInWord++;
        }
        if (word.indexOf("IUS") != -1) {  // IU is two syllables if followed by S (i.e. radius)
            syllablesInWord++;
        }
        if (word.indexOf("UU") != -1) {  // UU is two syllables (i.e. continuum)
            if ((!word.equals("VACUUM")) & (!word.equals("VACUUMS"))) {  // only exception
                syllablesInWord++;
            }
        }
        if (word.indexOf("'VE") != -1) {  // "'VE" is an extra syllable (i.e. would've)
            if ((!word.equals("VACUUM")) & (!word.equals("VACUUMS"))) {  // only exception
                syllablesInWord++;
            }
        }
        if (word.indexOf("N'T") != -1) {  // "N'T" is an extra syllable (i.e. wouldn't)
            if ((!word.equals("VACUUM")) & (!word.equals("VACUUMS"))) {  // only exception
                syllablesInWord++;
            }
        }

        // Silent e suffixes - compound words that start with a word with silent e (i.e. cheesecake)
        // Detect these if the suffix is in the word but the length is longer than the plural suffix
        if ((word.indexOf("CHEESE") == 0) && (word_length > 7)) {
            syllablesInWord--;
        }
        if ((word.indexOf("BARE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("BASE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("FIRE") == 0) && (word_length > 5)) {  // Fire is one syllable!!!
            syllablesInWord--;
        }
        if ((word.indexOf("GRAPE") == 0) && (word_length > 6)) {
            syllablesInWord--;
        }
        if ((word.indexOf("NONE") == 0) && (word_length > 5)) { 
            syllablesInWord--;
        }
        if ((word.indexOf("NOTE") == 0) && (word_length > 5)) { 
            syllablesInWord--;
        }
        if ((word.indexOf("SKATE") == 0) && (word_length > 6)) {
            syllablesInWord--;
        }
        if ((word.indexOf("SOME") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("LIFE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("SHOE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("FARE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("FORE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("NOSE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("SCARE") == 0) && (word_length > 6)) {
            syllablesInWord--;
        }
        if ((word.indexOf("TIME") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("EYE") == 0) && (word_length > 4)) {
            syllablesInWord--;
        }
        if ((word.indexOf("RSE") != word_length - 3) && (word.indexOf("RSE") != -1) && (word_length > 4)) {
            syllablesInWord--;
        }
        if ((word.indexOf("LONE") == 0) && (word_length > 6)) {
            syllablesInWord--;
        }
        if ((word.indexOf("CAVE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("ACE") != word_length - 3) && (word.indexOf("ACE") != -1) && (word_length > 6)) {  // all -ace compound words, makes sure that the full word doesn't end with ace though
            syllablesInWord--;
        }
        if ((word.indexOf("OME") != word_length - 3) && (word.indexOf("OME") != -1) && (word_length > 5)) {  // all -ome compound words
          if (!word.equals("HOMEOWNER")) {  // homeowner is an exception
            syllablesInWord--;
          }
        }
        if ((word.indexOf("ORE") != word_length - 3) && (word.indexOf("ORE") != -1) && (word_length > 5)) {  // all -ore compound words
            syllablesInWord--;
        }
        if ((word.indexOf("NAME") == 0) && (word_length > 6)) {
            syllablesInWord--;
        }
        if ((word.indexOf("ELSE") == 0) && (word_length > 5)) {
            syllablesInWord--;
        }
        if ((word.indexOf("ENCE") != word_length - 4) && (word.indexOf("ENCE") != -1) && (word_length > 6)) {  // all -ace compound words
            syllablesInWord--;
        }
        if ((word.indexOf("HERE") == 0) && (word_length > 6)) {
            syllablesInWord--;
        }
        if ((word.indexOf("OUSE") != word_length - 4) && (word.indexOf("OUSE") != -1) && (word_length > 6)) {  // all -ace compound words
            syllablesInWord--;
        }
        

        // coin- words are +2 except for coin, coins, and coined, so test for coin- words with length greater than 6
        if (((word.indexOf("COIN")) == 0) && (word_length > 6)) {
          syllablesInWord++;
        }
        
        // Words cannot have 0 syllables, they need to have at least one
        if (syllablesInWord <= 0) {
            syllablesInWord = 1;
        }

        // Extra specific exceptions
        if (word.equals("COLONEL")) {
            syllablesInWord = 2;
        } else if (word.equals("WEDNESDAY")) {
          syllablesInWord = 2;
        }
        if (word_length == 2) {  // My program returns 3 for two letter words for some reason
          syllablesInWord = 1;
        }

        
        syllables = syllables + syllablesInWord;
        totalSyllables = totalSyllables + syllablesInWord;
      } 

      if ((syllables < 14) || (syllables > 16)) {
        // Not a Sijo anymore
        couldBeSijo = false;
      }
      syllableStructure = syllableStructure + String.format("%03d", syllables);
      
      line = input.nextLine();
    }

    System.out.println(totalNumberOfLines + " lines");
    System.out.println(totalSyllables + " syllables");
    for (int i = 0; i < syllableStructure.length(); i = i + 3) {
      syllables = Integer.parseInt(syllableStructure.substring(i, i + 3));  // take the syllables and remove opening 0s by converting to int
      System.out.print(syllables + " ");
    }
    System.out.print("\n");

    // Each three digits represents the number of syllables on a line. A poem with 5 syllables on line 1, 7 syllables on line 2, and 5 syllables on line 3 would be 005007005
    if (syllableStructure.equals("005007005")) {
      System.out.println("Haiku");
    } else if (syllableStructure.equals("005007005007007")) {
      System.out.println("Tanka");
    } else if (syllableStructure.equals("002004006008002")) {
      System.out.println("Cinquain");
    } else if (syllableStructure.equals("009008007006005004003002001")) {
      System.out.println("Nonet");
    } else if ((totalNumberOfLines == 3) && (42 <= totalSyllables) && (totalSyllables <= 48) && (couldBeSijo)) {
      System.out.println("Sijo");
    } else {
      System.out.println("This is not a known type of poem.");
    }

    input.close();
  }
}
