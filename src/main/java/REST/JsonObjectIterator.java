package REST;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by alex on 08/10/15.
 */

public class JsonObjectIterator implements Iterator<Map<String, Object>>, Closeable {

    private final InputStream inputStream;
    private JsonParser jsonParser;
    private boolean isInitialized;

    private Map<String, Object> nextObject;

    public JsonObjectIterator(final InputStream inputStream) {
        this.inputStream = inputStream;
        this.isInitialized = false;
        this.nextObject = null;
    }

    private void init() {
        this.initJsonParser();
        this.initFirstElement();
        this.isInitialized = true;
    }

    private void initJsonParser() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonFactory jsonFactory = objectMapper.getJsonFactory();

        try {
            this.jsonParser = jsonFactory.createJsonParser(inputStream);
        } catch (final IOException e) {
            throw new RuntimeException("There was a problem setting up the JsonParser: " + e.getMessage(), e);
        }
    }

    private void initFirstElement() {
        try {
            // Check that the first element is the start of an array
            final JsonToken arrayStartToken = this.jsonParser.nextToken();
            if (arrayStartToken != JsonToken.START_ARRAY) {
                throw new IllegalStateException("The first element of the Json structure was expected to be a start array token, but it was: " + arrayStartToken);
            }

            // Initialize the first object
            this.initNextObject();
        } catch (final Exception e) {
            throw new RuntimeException("There was a problem initializing the first element of the Json Structure: " + e.getMessage(), e);
        }

    }

    private void initNextObject() {
        try {
            final JsonToken nextToken = this.jsonParser.nextToken();

            // Check for the end of the array which will mean we're done
            if (nextToken == JsonToken.END_ARRAY) {
                this.nextObject = null;
                return;
            }

            // Make sure the next token is the start of an object
            if (nextToken != JsonToken.START_OBJECT) {
                throw new IllegalStateException("The next token of Json structure was expected to be a start object token, but it was: " + nextToken);
            }

            // Get the next product and make sure it's not null
            this.nextObject = this.jsonParser.readValueAs(new TypeReference<Map<String, Object>>() {
            });
            if (this.nextObject == null) {
                throw new IllegalStateException("The next parsed object of the Json structure was null");
            }
        } catch (final Exception e) {
            throw new RuntimeException("There was a problem initializing the next Object: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasNext() {
        if (!this.isInitialized) {
            this.init();
        }

        return this.nextObject != null;
    }

    @Override
    public Map<String, Object> next() {
        // This method will return the current object and initialize the next object so hasNext will always have knowledge of the current state

        // Makes sure we're initialized first
        if (!this.isInitialized) {
            this.init();
        }

        // Store the current next object for return
        final Map<String, Object> currentNextObject = this.nextObject;

        // Initialize the next object
        this.initNextObject();

        return currentNextObject;
    }

    @Override
    public void remove() {
        return;
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(this.jsonParser);
        IOUtils.closeQuietly(this.inputStream);
    }

}