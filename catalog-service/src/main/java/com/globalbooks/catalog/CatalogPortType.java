package com.globalbooks.catalog;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(targetNamespace = "http://catalog.globalbooks.com/", name = "CatalogPortType")
public interface CatalogPortType {

    @WebMethod(operationName = "getBook")
    @WebResult(name = "getBookResponse", targetNamespace = "http://catalog.globalbooks.com/")
    CatalogServiceImpl.GetBookResponse getBook(
        @WebParam(name = "getBookRequest", targetNamespace = "http://catalog.globalbooks.com/")
        CatalogServiceImpl.GetBookRequest request);
}
