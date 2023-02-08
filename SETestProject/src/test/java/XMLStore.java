import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.Calendar;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBodyExtractionOptions;
import static org.hamcrest.CoreMatchers.equalTo;
import java.text.SimpleDateFormat;
import org.hamcrest.CoreMatchers;
import io.restassured.mapper.ObjectMapper;

public class XMLStore {

    private String id;
    private String make;
    private String unit;
    private String internalClass;

    //Long id = response.
    //Long id = response.getBody();
    //assertThat(id, is(not(0)));

    //String id = response.getBody();
    //Assert.assertThat(id, is(not(0)));
    //String make = response.getBody();
    //Assert.assertThat();
          //  System.out.println(id);
}
