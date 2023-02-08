import com.rentalcars.lib.rest.domain.vehicle.VehicleRS;
import com.rentalcars.lib.rest.domain.vehicle.XmlVehicleInfo;
import com.rentalcars.lib.rest.domain.vehicle.premiumservices.XmlPremiumService;
import com.rentalcars.lib.rest.domain.vehicle.premiumservices.XmlPremiumServices;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;



public class PremiumServices {

    private static final String baseURL = "http://se-ua-stage-01.lhr4.traveljigsaw.com:8080/web-search-api/";

    @Test public void
    status_code_check() {

        RestAssured.baseURI = baseURL;
        LocalDateTime picktime = LocalDateTime.now().plusMonths(2);
        String pickformat = picktime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime droptime = picktime.plusDays(2);
        String dropformat = droptime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String requestBody = "<VehicleRQ cor=\"gb\" channel=\"web\" lang=\"361\" returnErrors=\"1\">\r\n" +
                "<PickTime>" + pickformat.toString() + "</PickTime>\r\n" +
                "<DropTime>" + dropformat.toString() + "</DropTime>\r\n" +
                "<DrvAge>30</DrvAge>\r\n" +
                "<OnSale>2</OnSale>\r\n" +
                "<SourceMarket>UK</SourceMarket>\r\n" +
                "<PickLoc>435323</PickLoc>\r\n" +
                "<DropLoc>435323</DropLoc>\r\n" +
                "</VehicleRQ>";

        Response response = null;

        response = given().
                contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(requestBody)
                .when()
                .post("/search.do");

        String jsonString = response.asString().substring(response.asString().indexOf("<VehicleRS"));
        VehicleRS vehicleRS = getXmlStringAsObject(VehicleRS.class, response.asString());
        //VehicleRS vehicleRS = JsonConverterComponent.getInstance().toObject(jsonString, VehicleRS.class);

        //Assert.assertThat(vehicleRS.getVehicleInfo(), containsString("test"));

        for (XmlVehicleInfo vehicleInfo : vehicleRS.getVehicleInfo()) {
            Assert.assertNotNull(vehicleInfo.getCar());
            Assert.assertNotNull(vehicleInfo.getOnStopSell());
            XmlPremiumServices premiumServicesElement = vehicleInfo.getPremiumServices();
            //Assert.assertNotNull(premiumServicesElement);
            //List<XmlPremiumService> premiumServices = premiumServicesElement.getPremiumServices();
            //Assert.assertEquals(premiumServices);
        }
    }
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
