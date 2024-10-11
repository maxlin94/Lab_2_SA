package org.example.rest;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.service.Warehouse;

import java.util.List;
import java.util.Optional;

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

    @GET
    @Path("/products/{id}")
    public Response getProductById(@PathParam("id") String id) {
        Optional<Product> product = warehouse.getProductById(id);
        if (product.isPresent()) {
            return Response.ok(product.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/products/category/{category}")
    public Response getProductsByCategory(@PathParam("category") String category) {
        Category cat;
        try {
            cat = Category.valueOf(category.toUpperCase());
            return Response.ok(warehouse.getProductsByCategory(cat)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/products/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(Product product) {
        try {
            warehouse.addProduct(product);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
