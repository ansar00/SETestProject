import com.rabbitmq.client.AMQP;
import com.rentalcars.lib.bookings.dto.BookingInfoRs;
import com.rentalcars.lib.core.util.JsonConverterComponent;
import com.rentalcars.lib.rest.domain.vehicle.VehicleRS;
import com.rentalcars.lib.rest.domain.vehicle.XmlVehicleInfo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import javax.sql.rowset.spi.XmlReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class FirstTest {

    private static final String baseURL = "http://se-ua-stage-01.lhr4.traveljigsaw.com:8080/web-search-api/";

    @Test public void
    status_code_check() {

        RestAssured.baseURI = baseURL;
        LocalDateTime picktime = LocalDateTime.now().plusMonths(2);
        String pickformat = picktime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime droptime = picktime.plusDays(2);
        String dropformat = droptime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String requestBody = "<VehicleRQ cor=\"gb\" channel=\"web\" lang=\"361\" returnErrors=\"1\">\r\n"+
                "<PickTime>"+pickformat.toString()+"</PickTime>\r\n"+
                "<DropTime>"+dropformat.toString()+"</DropTime>\r\n"+
                "<DrvAge>30</DrvAge>\r\n"+
                "<OnSale>2</OnSale>\r\n"+
                "<SourceMarket>UK</SourceMarket>\r\n"+
                "<PickLoc>435323</PickLoc>\r\n"+
                "<DropLoc>435323</DropLoc>\r\n"+
                "</VehicleRQ>";

        Response response = null;

        response = given().
                contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(requestBody)
                .when()
                .post("/search.do");
       // XmlReader.
        //Assert.assertThat(response,is(not(0)));

        String jsonString = response.asString().substring(response.asString().indexOf("<VehicleRS"));
        VehicleRS vehicleRS = getXmlStringAsObject(VehicleRS.class, response.asString());
        //VehicleRS vehicleRS = JsonConverterComponent.getInstance().toObject(jsonString, VehicleRS.class);

        //Assert.assertThat(vehicleRS.getVehicleInfo(), containsString("test"));

        for (XmlVehicleInfo vehicleInfo :vehicleRS.getVehicleInfo()){
            Assert.assertNotNull(vehicleInfo.getCar());
            Assert.assertNotNull(vehicleInfo.getOnStopSell());
            //Assert.assertNotNull(vehicleInfo.getPaymentMethods());
            Assert.assertNotNull(vehicleInfo.getPrice());
            //Assert.assertNotNull(vehicleInfo.getFees());
            Assert.assertNotNull(vehicleInfo.getClass());
            Assert.assertNotNull(vehicleRS.getPricingExperiments());
            Assert.assertNotNull(vehicleRS.getPricingRequestId());
            //Assert.assertNotNull(vehicleInfo.);
            //Assert.assertNotNull(vehicleInfo.getFees());
            //Assert.assertNotNull(vehicleInfo.getXmlExtras());
            //Assert.assertNotNull(vehicleInfo.getAdditionalPriceInfo());
        }

       // vehicleRS.getVehicleInfo(); assert true;



        //Assert.assertEquals(200, response.getStatusCode());
        //Assert.assertThat(response.asString(), is(notNullValue()));


       // Assert.assertThat(response.asString(), containsString("dropOffHours"));
       // Assert.assertThat(response.asString(), containsString("openingHours"));
        //Assert.assertThat(response.asString(), containsString("depotId"));
       // Assert.assertThat(response.asString(), containsString("xmlModule"));
        //Assert.assertThat(response.asString(), containsString("hash"));
       // Assert.assertThat(response.asString(), containsString("currency=\"GBP\""));
       // Assert.assertThat(response.asString(), containsString("requestId"));


        System.out.println("Post Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());
        System.out.println(requestBody);
    }
    @SuppressWarnings("unchecked")
    public static <T> T getXmlStringAsObject(Class<T> aClass, String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(aClass);
            Unmarshaller um = context.createUnmarshaller();
            return (T) um.unmarshal(new StringReader(xml));
        } catch (JAXBException ex) {
            throw new RuntimeException("Cannot parse content", ex);
        }
    }
}
