import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The Class DictionaryManager.
 */
public class DictionaryManager {

    /** The instance. */
    private static DictionaryManager INSTANCE;

    /** The file path. */
    private String filePath;

    /** The dictionary. */
    private JSONObject dictionary;

    /**
     * Gets the single instance of DictionaryManager.
     *
     * @return single instance of DictionaryManager
     */
    public static DictionaryManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DictionaryManager.class) {
                if (INSTANCE == null)
                    INSTANCE = new DictionaryManager();
            }
        }

        return INSTANCE;
    }

    /**
     * Load dictionary.
     *
     * @param filePath the file path
     */
    public boolean loadDictionary(String filePath) {
        boolean result = true;

        try {
            JSONParser parser = new JSONParser();
            this.dictionary = (JSONObject) parser
                    .parse(new FileReader(filePath));
            this.filePath = filePath;
        } catch (ParseException ex) {
            result = false;
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * Persist dictionary.
     */
    public void persistDictionary() {
        if (this.dictionary != null) {
            if (!StringHelper.isNullOrEmpty(this.filePath)) {

                try (FileWriter file = new FileWriter(this.filePath)) {
                    file.write(this.dictionary.toJSONString());
                    file.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println(
                        "The output dictionary file path is NULL or empty.");
            }
        }
    }

    /**
     * Search.
     *
     * @param word the word
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public synchronized JSONObject search(String word) {
        JSONObject response = new JSONObject();
        String searchKey = word.toLowerCase();

        if (this.dictionary.containsKey(searchKey)) {
            String meaning = this.dictionary.get(searchKey).toString();
            response.put(Constants.RESPONSE_KEY, meaning);
        } else {
            response.put(Constants.RESPONSE_KEY,
                    String.format(Constants.WORD_NOT_FOUND, word));
        }

        return response;
    }

    /**
     * Adds the.
     *
     * @param word    the word
     * @param meaning the meaning
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public synchronized JSONObject add(String word, String meaning) {
        JSONObject response = new JSONObject();
        String searchKey = word.toLowerCase();

        if (this.dictionary.containsKey(searchKey)) {
            response.put(Constants.RESPONSE_KEY,
                    String.format(Constants.WORD_ALREADY_EXIST, word));
        } else {
            this.dictionary.put(searchKey, meaning);
            response.put(Constants.RESPONSE_KEY,
                    String.format(Constants.SUCCESSFULLY_ADDED_WORD, word));
        }

        return response;
    }

    /**
     * Delete.
     *
     * @param word the word
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public synchronized JSONObject delete(String word) {
        JSONObject response = new JSONObject();
        String searchKey = word.toLowerCase();

        if (this.dictionary.containsKey(searchKey)) {
            this.dictionary.remove(searchKey);
            response.put(Constants.RESPONSE_KEY,
                    String.format(Constants.SUCCESSFULLY_DELETED_WORD, word));
        } else {
            response.put(Constants.RESPONSE_KEY,
                    String.format(Constants.WORD_NOT_FOUND, word));
        }

        return response;
    }

}
