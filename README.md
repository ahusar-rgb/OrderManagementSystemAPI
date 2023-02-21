# OrderManagementSystemAPI
A prototype for a backend of an order management system

Functionality:
- Creating customers
- Creating orders
- Creating products
- Searching orders by date
- Extended functionality: 
  - Searching orders by customer
  - Searching orders by product
  - Modifiying product quantity in an order

Built with:
- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL

The project includes Unit tests covering nearly all services and controllers as well as user-defined repository methods.

The api receives and sends messages in **Json**

Searching functions in "Extended functionality" section are implemented using both JPQL and Criteria queries. 
In code these functions are called through an interface **OrderFinder**

***Public endpoints:***

Orders:
  - Create:
    - POST
    - */api/v1/order*  
    - Request Body - OrderCreateRequest object
    - Response Body - OrderDto object
  - Find By ID: 
    - GET
    - */api/v1/order/{id}*
    - {id} - id of the needed order
    - Response Body - OrderDto object
  - Delete By ID:
    - DELETE
    - */api/v1/order/{id}
    - {id} - id of the needed order
  - Find By Date
    - GET
    - */api/v1/orders*
    - Request Body - String in format "YYYY-mm-dd"
    - Response Body - List of OrderDto objects

Customers:
  - Create:
    - POST
    - */api/v1/customer*
    - Request Body - Customer object
    - Response Body - Customer Object
  - Find By Code:
    - GET
    - */api/v1/customer/{code}*
    - {code} - registration code of the needed customer
    - Response Body - Customer Object
  - Delete By Code:
    - DELETE
    - */api/v1/customer/{code}*
    - {code} - registration code of the needed customer
  - Find Orders By Customer Code:
    - GET
    - */api/v1/customer/{code}/orders*
    - {code} - registration code of the needed customer
    - Response Body - List of OrderDto objects
    
Products:
  - Create:
    - POST
    - */api/v1/product*
    - Request Body - Product object
    - Response Body - Product Object
  - Find By SKU code:
    - GET
    - */api/v1/product/{skuCode}*
    - {skuCode} - SKU code of the needed product
    - Response Body - Product Object
  - Delete By Code:
    - DELETE
    - */api/v1/product/{skuCode}*
    - {skuCode} - SKU code of the needed product
 - Find Orders By Product SKU code:
    - GET
    - */api/v1/product/{skuCode}/orders*
    - {skuCode} - SKU code of the needed product
    - Response Body - List of OrderDto objects

Order Lines:
  - Update Product Quantity:
    - POST
    - */api/v1/order-line/{id}*
    - {id} - id of the updated product
    - Requst Body - Integer number
