package org.example.rest;


import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entities.Product;
import org.example.service.Warehouse;

import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseResource {
    @Inject
    private Warehouse warehouse;

    @GET
    @Path("/products")
    public Response getAllProducts() {
        List<Product> products = warehouse.getAllProducts();
        return Response.ok(products).build();
    }
}
