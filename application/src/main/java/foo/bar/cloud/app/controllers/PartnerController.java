package foo.bar.cloud.app.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.s4hana.connectivity.DefaultErpHttpDestination;
import com.sap.cloud.sdk.s4hana.connectivity.ErpHttpDestination;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;

import foo.bar.cloud.app.models.HelloWorldResponse;


@RestController
@RequestMapping( "/partner" )
public class PartnerController
{
    private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);
    private static final String CATEGORY_PERSON = "1";

    @RequestMapping( method = RequestMethod.GET )
    public ResponseEntity<HelloWorldResponse> getHello( @RequestParam( defaultValue = "id" ) final String id )
    {
        logger.info("I am running!");
//        return ResponseEntity.ok(new HelloWorldResponse(name));
        
//        final String id = request.getParameter("id");
        

        final String jsonResult;
        
        final ErpHttpDestination destination = DestinationAccessor.getDestination("MyErpSystem").asHttp().decorate(DefaultErpHttpDestination::new);

        final List<BusinessPartner> businessPartners =
                new DefaultBusinessPartnerService()
                        .getAllBusinessPartner()
                        .select(BusinessPartner.BUSINESS_PARTNER,
                                BusinessPartner.LAST_NAME,
                                BusinessPartner.FIRST_NAME,
                                BusinessPartner.IS_MALE,
                                BusinessPartner.IS_FEMALE,
                                BusinessPartner.CREATION_DATE)
                        .executeRequest(destination);
        jsonResult = new Gson().toJson(businessPartners);
        
        return ResponseEntity.ok(new HelloWorldResponse(jsonResult));
        
    }
}
