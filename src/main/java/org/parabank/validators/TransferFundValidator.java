package org.parabank.validators;

import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.List;

public class TransferFundValidator {

    public static boolean verifyCreditedAmountAfterTransfer(Response response, int amount) throws IOException {
        XmlPath xmlPath = new XmlPath(response.asInputStream());
        List<String> amountList = xmlPath.getList("transactions.transaction.amount");
        return amountList.contains(amount + ".00");
    }
}
