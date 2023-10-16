# System Architecture Lab Two
This project involves creating a REST API with Jakarta EE 10 and JAX-RS.
The project implement parts of our previous project "warehouse" from JavaLabThree.

### Resources
`GET` `/api/product` - Retrieves all the products
<br>
`GET` `/api/product/{id}` - Retrieves one product with the given id
<br>
`GET` `/api/product/category{name}` - Retrieves a list of products with the given category.
<br>
`POST` `/api/product` - Creates a new product.
```jsonc
body:
{
    "id":  1,
    "name":  "Mac",
    "rating": 7,
    "category": "tech"
}
```

`PUT` `/api/product` - Updates a product.
```jsonc
body:
{
    "id":  1,
    "name":  "Ring",
    "rating": 7,
    "category": "jewelry"
}
```

### Info
The server will always respond with a json that contains a "meta" object with information about the request.
The json could also contain a "data" object if you are requesting data.
