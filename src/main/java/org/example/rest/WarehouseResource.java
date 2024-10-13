package org.example.rest;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.interceptor.Log;
import org.example.service.WarehouseService;

import java.util.List;
import java.util.Optional;

@Log
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseResource {
    private WarehouseService warehouse;

    public WarehouseResource() {
    }

    @Inject
    public WarehouseResource(WarehouseService warehouse) {
        this.warehouse = warehouse;
    }


    @GET
    @Path("/products")
    public Response getAllProducts() {
        List<Product> products = warehouse.getAllProducts();
        return Response.ok(products).build();
    }

    @GET
    @Path("/products/{id}")
    public Response getProductById(@PathParam("id") @Valid String id) {
        Optional<Product> product = warehouse.getProductById(id);
        if (product.isPresent()) {
            return Response.ok(product.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/products/category/{category}")
    public Response getProductsByCategory(@PathParam("category") @Valid String category) {
        Category cat;
        try {
            cat = Category.valueOf(category.toUpperCase());
            return Response.ok(warehouse.getProductsByCategory(cat)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/products")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(@Valid Product product) {
        try {
            warehouse.addProduct(product);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
