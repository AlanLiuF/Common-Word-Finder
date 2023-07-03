import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * class which finds the n most common words in a document and print the result by using
 * one of the three data structure: Binary Search Tree Map, AVL Tree Map, or Hash Map.
 * @author Fengzhe Liu (fl2635)
 * @version 1.0 December 18, 2022
 */

public class CommonWordFinder {
    /**
     * the main method is used to firstly fetch input parameters and check if they are valid;
     * secondly call BSTMap_Solution, AVLTreeMap_Solution or HashMap_Solution;
     * and finally print the n most common words in the desired format.
     */
    public static void main(String[] args) {

        // check if the number of input parameters is correct.
        if (args.length > 3 || args.length < 2) {
            System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }

        // check if the first parameter (file name) is valid using try-catch clause,
        // and at the same time, fill the contents in the file into a StringBuilder.
        StringBuilder contents = new StringBuilder();
        // reference: https://blog.csdn.net/yongbutingxide/article/details/82024211
        BufferedReader b_read = null;
        try {
            File file = new File(args[0]);
            FileReader f_read = new FileReader(file);
            b_read = new BufferedReader(f_read);
            String str;
            while ((str = b_read.readLine()) != null) {
                for (char ch: str.toCharArray()) {
                    contents.append(ch);
                }
            }
            b_read.close();
        } catch (FileNotFoundException e) {          // catch the FileNotFoundException.
            System.out.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {    // if nothing is input, then print error.
            System.out.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        } catch (IOException e) {                   // catch IOException.
            System.out.println("Error: An I/O error occurred reading '" + args[0] + "'.");
            System.exit(1);
        }
        contents.append(" ");             // append a space at the end of contents for further use.
        String text = contents.toString();


        // check if the second parameter (type of data structure) is valid.
        String data_structure = switch (args[1]) {
            case "bst" -> "bst";
            case "avl" -> "avl";
            case "hash" -> "hash";
            default -> null;
        };
        if (data_structure == null) {
            System.err.println("Error: Invalid data structure '" + args[1] + "' received.");
            System.exit(1);
        }

        // check if the third optional parameter is valid.
        int limit = 10;      // if the limit is not specified, it is set to 10.
        if (args.length == 3) {
            try {
                limit = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid limit '" + args[2] + "' received.");
                System.exit(1);
            }
            if (limit < 1) {
                System.err.println("Error: Invalid limit '" + args[2] + "' received.");
            }
        }

        // print the desired output by using selected data structure type.
        if (data_structure.equals("bst")) {
            BSTMap_Solution(text, limit);
        } else if (data_structure.equals("avl")) {
            AVLTreeMap_Solution(text, limit);
        } else {
            HashMap_Solution(text, limit);
        }
    }


    /**
     * the method which uses Binary Search Map as the data structure to work out the problem.
     * @param text: a String representation of file contents.
     * @param limit: user-required limitation of the number of output lines.
     */
    public static void BSTMap_Solution(String text, int limit) {
        MyMap<String, Integer> map = new BSTMap<>();      // initiate a BSTMap as the map.
        Parse_Text(text, map);
        String[] key_arr = new String[map.size()];
        int max_key_len = Sorted_Key_Map(key_arr, map);
        String [][] arr = new String [map.size()][2];      // create a two-dimensional array.
        Sorted_Pairs(arr, key_arr, map);
        Print_Output(max_key_len, map, arr, limit);
    }

    /**
     * the method which uses AVL Tree Map as the data structure to work out the problem.
     * @param text: a String representation of file contents.
     * @param limit: user-required limitation of the number of output lines.
     */
    public static void AVLTreeMap_Solution(String text, int limit) {
        MyMap<String, Integer> map = new AVLTreeMap<>();      // initiate a AVLTreeMap as the map.
        Parse_Text(text, map);
        String[] key_arr = new String[map.size()];
        int max_key_len = Sorted_Key_Map(key_arr, map);
        String [][] arr = new String [map.size()][2];
        Sorted_Pairs(arr, key_arr, map);
        Print_Output(max_key_len, map, arr, limit);
    }

    /**
     * the method which uses Hash Map as the data structure to work out the problem.
     * @param text: a String representation of file contents.
     * @param limit: the user-required limitation of the number of output lines.
     */
    public static void HashMap_Solution(String text, int limit) {
        MyMap<String, Integer> map = new MyHashMap<>();      // initiate a HashMap as the map.
        Parse_Text(text, map);
        String[] key_arr = new String[map.size()];
        int max_key_len = Sorted_Key_Map(key_arr, map);
        String [][] arr = new String [map.size()][2];
        Sorted_Pairs(arr, key_arr, map);
        Print_Output(max_key_len, map, arr, limit);
    }




    /**
     * The method use for loop to split the text into words, then put them into a map with corresponding count.
     * @param text: a String representation of file contents.
     * @param map: the second parameter: a map to contain words and its counts.
     */
    private static void Parse_Text(String text, MyMap<String, Integer> map) {
        StringBuilder word = new StringBuilder();     // create a StringBuilder to join single ch into word
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 65 && c <= 90) {           // convert uppercase to lowercase
                c |= 32;
            }
            if (i != 0) {
                boolean valid_dash = text.charAt(i - 1) >= 97 && text.charAt(i - 1) <= 122 || text.charAt(i - 1) == 39;
                if (c >= 97 && c <= 122 || c == 39 || c == 45 && valid_dash) {     // verify valid characters
                    word.append(c);
                } else if (c == 32) {     // if it is a space or newline, put the collected word into map
                    if (map.get(word.toString()) == null) {        // insert for the first time
                        map.put(word.toString(), 1);
                    } else if (map.get(word.toString()) != null) {   // inserted before
                        int new_value = map.get(word.toString()) + 1;
                        map.put(word.toString(), new_value);
                    }
                    word.setLength(0);
                }
            } else {
                if (c >= 97 && c <= 122 || c == 39) {
                    word.append(c);
                }
            }
        }
    }



    /**
     * The method creates an array to hold keys (unique words) in map and sort them alphabetically,
     * then this method computes the maximum length of all the words.
     * @param key_arr: a String array to hold all unique words.
     * @param map: the map contain words and its counts which is created before.
     * @return the maximum length among all the length of words.
     */
    private static int Sorted_Key_Map(String[] key_arr, MyMap<String, Integer> map) {
        int i = 0;
        // reference: https://blog.csdn.net/weixin_30496431/article/details/99397094
        Iterator<Entry<String, Integer>> iter = map.iterator();    // use iterator to iterate through map
        while (iter.hasNext()) {
            Entry<String, Integer> entry = iter.next();
            String key = entry.key;
            key_arr[i] = key;
            i++;
        }
        Arrays.sort(key_arr);        // sort the unique keys alphabetically.

        int max_key_len = 0;
        for (String s: key_arr) {
            if (s.length() > max_key_len) {
                max_key_len = s.length();
            }
        }
        return max_key_len;
    }




    /**
     * this method creates a two-dimensional array to hold keys (unique words) and values (the frequency of word),
     * and sort the array in descending order by its values.
     * @param arr: a two-dimensional array to hold words and their counts.
     * @param key_arr: the String array which contains all unique words.
     * @param map: the map contain words and its counts which is created before.
     */
    private static void Sorted_Pairs(String[][] arr, String[] key_arr, MyMap<String, Integer> map) {
        int p = 0;
        //reference: https://zhidao.baidu.com/question/497543231.html
        for (String s : key_arr) {
            String value = map.get(s).toString();
            arr[p] = new String[]{s, value};
            p++;
        }
        // sort the two-dimensional array by the values:
        Arrays.sort(arr, new Comparator<String[]>() {
            public int compare(String[] a, String[] b) {
                return Integer.parseInt(b[1]) - Integer.parseInt(a[1]);   // sort by the second column
            }
        });

    }





    /**
     * the method uses for loop to print the words and their
     * @param max_key_len: the maximum length among all the length of words.
     * @param map: the map contain words and its counts which is created before.
     * @param arr: the two-dimensional array created before.
     * @param limit: the user-required limitation of output lines.
     */


    private static void Print_Output(int max_key_len, MyMap<String, Integer> map, String[][] arr, int limit) {
        String newline = System.lineSeparator();
        System.out.println("Total unique words: " + map.size());
        if (limit > map.size()) {
            limit = map.size();
        }
        for (int m = 0; m < limit; m++) {
            String final_key = arr[m][0];
            int back_space_num = max_key_len - final_key.length() + 1;
            String back_spaces = " ".repeat(back_space_num);
            if (String.valueOf(map.size()).length() != String.valueOf(m + 1).length()) {
                int front_space_num = String.valueOf(map.size()).length() - String.valueOf(m + 1).length();
                String front_spaces = " ".repeat(front_space_num);
                System.out.print(front_spaces + (m + 1) + ". " + final_key + back_spaces + arr[m][1] + newline);
            } else {
                System.out.print((m + 1) + ". " + final_key + back_spaces + arr[m][1] + newline);
            }
        }
    }







}
