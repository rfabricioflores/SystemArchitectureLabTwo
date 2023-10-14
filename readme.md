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
    "category": "TECH"
}
```