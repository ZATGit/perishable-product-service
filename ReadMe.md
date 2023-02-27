<div id="top">

# perishable-product-service

</div>

A near-RESTful API for tracking a grocery store's inventory of perishable goods developed with Java and Spring.

<sup>Author: <em>Zach Trembly</em></sup>

## Technologies

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white)

Java 11, Spring 5.3, Spring Boot 2.7, Spring Data, Junit 5, Mockito 4, Lombok, Maven, H2 database, Docker, Redis

## Usage

This inventory service tracks each item's name, code, stock date, days until sell by date, and quality. Most items
follow standard depreciation rules, decreasing the quality by 1 as the sell by date approaches. Some items follow their
own custom depreciation rules; refer to the <a href="#future-day">/future/day</a> endpoint below or the application's 
Javadocs for more details. 

Items may be queried by several attributes.

#### Exception Handling

All exceptions are handled globally with ExceptionHandlerAdvice. Endpoints are case-insensitive and throw custom
exceptions for malformed JSON payloads, etc. See <a href="#exception-sample">sample</a> below.

#### Logging

Logs are handled with Slf4j, Logback, and by wrapping HandlerInterceptor.

#### Caching

Distributed caching is handled with Spring Data Redis. An embedded server is included for local development. 
Key generation uses method-specific custom annotations, found in the ``cache`` package.

<details style="font:20px Open sans;"><summary>Sample Key</summary></summary>
<p><code>/itemname-or-itemcode</code></p>
<pre>@CacheableNameOrCode
public ProductByItemNameOrItemCodeResponse getProductByItemNameOrItemCodeInventory(...);
</pre>
generates:
<pre>"products::PerishableProductInventoryServiceImpl,getProductByItemNameOrItemCodeInventory,Corn on the,OrgSpin"</pre></p>
</details>

<details style="font:20px Open sans;"><summary>Embedded Redis</summary>
<p> Toggle <code>local.redis.server.embedded=ON</code> in the <code>local</code> properties file 
to use the server.</p>

[Go to application-local.properties](src/main/resources/application-local.properties)
</details>

<details style="font:20px Open sans;"><summary>Standalone Redis</summary>
<p>If you'd prefer a non-embedded local redis server, use the <code>prod</code> profile. 
<p>Grab the redis docker image and run the server:</p>
<p><pre>$ docker pull redis && docker run --name zmart-redis -p 6379:6379 -d redis</pre></p>

</details>

#### Run Application

<pre>$ mvn spring-boot:run</pre>

#### Build a Container

Alternatively, you can run in a container.

<pre>
$ mvn clean install && docker build -t zmart-food:latest . && docker run -p 8181:8181 zmart-food:latest .
</pre>

## Endpoints

### /

#### http://localhost:8181/zmart/product/items/

Get all inventory ``POST``
<details><summary>Request Payload</summary>
<p>

```json
{}
```

</p>
</details>

<details><summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "count": 6,
    "products": [
      {
        "id": 1,
        "stockDate": "02-27-2023",
        "itemName": "Farm2Table Organic Spinach",
        "itemCode": "OrgSpinach",
        "quality": 20,
        "specialCase": 0,
        "sellBy": 10
      },
      {
        "id": 2,
        "stockDate": "02-27-2023",
        "itemName": "Corn on the cob",
        "itemCode": "CornCob",
        "quality": 0,
        "specialCase": 1,
        "sellBy": 2
      },
      {
        "id": 3,
        "stockDate": "02-27-2023",
        "itemName": "Grannysmith Apple",
        "itemCode": "ApplesGran",
        "quality": 7,
        "specialCase": 0,
        "sellBy": 5
      },
      {
        "id": 4,
        "stockDate": "02-27-2023",
        "itemName": "Twinkies",
        "itemCode": "Twinkies",
        "quality": 80,
        "specialCase": 3,
        "sellBy": 0
      },
      {
        "id": 5,
        "stockDate": "02-27-2023",
        "itemName": "3lb Ground Beef",
        "itemCode": "3lbGrBeed",
        "quality": 20,
        "specialCase": 2,
        "sellBy": 15
      },
      {
        "id": 6,
        "stockDate": "02-27-2023",
        "itemName": "Moonberries",
        "itemCode": "MoonBerr",
        "quality": 20,
        "specialCase": 0,
        "sellBy": 15
      }
    ]
  }
}
```

</p>
</details>
<hr></hr>

<div id="future-day">

### /future/day

</div>

#### http://localhost:8181/zmart/product/items/future/day/

Get all inventory based on day(s) in the future ``POST``
<details>
<summary>Request Payload</summary>
<p>

```json
{"dayOffset": 5}
```

</p>
</details>

<details>
<summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "count": 6,
    "products": [
      {
        "id": 1,
        "stockDate": "02-27-2023",
        "futureDate": "03-04-2023",
        "itemName": "Farm2Table Organic Spinach",
        "itemCode": "OrgSpinach",
        "quality": 15,
        "specialCase": 0,
        "sellBy": 5
      },
      {
        "id": 2,
        "stockDate": "02-27-2023",
        "futureDate": "03-04-2023",
        "itemName": "Corn on the cob",
        "itemCode": "CornCob",
        "quality": 5,
        "specialCase": 1,
        "sellBy": -3
      },
      {
        "id": 3,
        "stockDate": "02-27-2023",
        "futureDate": "03-04-2023",
        "itemName": "Grannysmith Apple",
        "itemCode": "ApplesGran",
        "quality": 2,
        "specialCase": 0,
        "sellBy": 0
      },
      {
        "id": 4,
        "stockDate": "02-27-2023",
        "futureDate": "03-04-2023",
        "itemName": "Twinkies",
        "itemCode": "Twinkies",
        "quality": 80,
        "specialCase": 3,
        "sellBy": 0
      },
      {
        "id": 5,
        "stockDate": "02-27-2023",
        "futureDate": "03-04-2023",
        "itemName": "3lb Ground Beef",
        "itemCode": "3lbGrBeed",
        "quality": 26,
        "specialCase": 2,
        "sellBy": 10
      },
      {
        "id": 6,
        "stockDate": "02-27-2023",
        "futureDate": "03-04-2023",
        "itemName": "Moonberries",
        "itemCode": "MoonBerr",
        "quality": 15,
        "specialCase": 0,
        "sellBy": 10
      }
    ]
  }
}
```

</p>
</details>

- At the end of each day, quality and sellBy decrement by 1.
- Once the sell by date has passed, quality degrades twice as fast.
- Special items don't follow normal quality depreciation rules.
  - Each product in the PRODUCTS table has a ``SPECIAL_CASE`` identifier. This number is used to determine which (if any) special
    rules apply to each product.
    - ``0``: Not a special product.
    - ``1``: Product quality may increment as the sellBy date approaches and continue incrementing up to 50 once it passes (e.g., Corn on the Cob).
    - ``2``: Product quality may increment as the sellBy date approaches then
      drop to 0 once it passes (e.g., 3lb Ground Beef).
    - ``3``: Product quality may never decrement (e.g., Twinkies).

- The ``dayOffset`` argument instructs the service to return a ``futureDate`` (``stockDate`` + ``dayOffset`` days) and updated ``quality`` &
  ``sellBy`` values. For example, ``10`` displays these values as they will be 10 days in the future.
<hr></hr>

### /itemname

#### http://localhost:8181/zmart/product/items/itemname

Find by item name ``POST``
<details>
<summary>Request Payload</summary>
<p>

```json
{"itemName":"Moonberries"}
```

</p>
</details>

<details>
<summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "product": {
      "id": 6,
      "stockDate": "02-27-2023",
      "itemName": "Moonberries",
      "itemCode": "MoonBerr",
      "quality": 20,
      "specialCase": 0,
      "sellBy": 15
    }
  }
}
```

</p>
</details>
<hr></hr>

### /itemcode

#### http://localhost:8181/zmart/product/items/itemcode

Find by item code ``POST``
<details>
<summary>Request Payload</summary>
<p>

```json
{"itemCode":"OrgSpinach"}
```

</p>
</details>

<details>
<summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "product": {
      "id": 1,
      "stockDate": "02-27-2023",
      "itemName": "Farm2Table Organic Spinach",
      "itemCode": "OrgSpinach",
      "quality": 20,
      "specialCase": 0,
      "sellBy": 10
    }
  }
}
```

</p>
</details>
<hr></hr>

### /itemname-or-itemcode

#### http://localhost:8181/zmart/product/items/itemname-or-itemcode

Find by itemName or itemCode ``POST``
<details><summary>Request Payload</summary>
<p>

```json
{
"itemName":"Corn on the",
"itemCode":"OrgSpin"
}
```

</p>
</details>

<details><summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "count": 2,
    "products": [
      {
        "id": 2,
        "stockDate": "02-27-2023",
        "itemName": "Corn on the cob",
        "itemCode": "CornCob",
        "quality": 0,
        "specialCase": 1,
        "sellBy": 2
      },
      {
        "id": 1,
        "stockDate": "02-27-2023",
        "itemName": "Farm2Table Organic Spinach",
        "itemCode": "OrgSpinach",
        "quality": 20,
        "specialCase": 0,
        "sellBy": 10
      }
    ]
  }
}
```

</p>
</details>

- Fields may be searched with partial names and codes such as ``Twin`` for ``Twinkies``.
<hr></hr>

### /quality

#### http://localhost:8181/zmart/product/items/quality/

Get all inventory by quality and chosen sort order. ``POST``

<details>
<summary>Request Payload</summary>
<p>

```json
{
  "quality":20,
  "orderByAttribute": "sellBy",
  "orderByAscOrDesc": "desc"
}
```

</p>
</details>

<details>
<summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "count": 3,
    "products": [
      {
        "id": 5,
        "stockDate": "02-27-2023",
        "itemName": "3lb Ground Beef",
        "itemCode": "3lbGrBeed",
        "quality": 20,
        "specialCase": 2,
        "sellBy": 15
      },
      {
        "id": 6,
        "stockDate": "02-27-2023",
        "itemName": "Moonberries",
        "itemCode": "MoonBerr",
        "quality": 20,
        "specialCase": 0,
        "sellBy": 15
      },
      {
        "id": 1,
        "stockDate": "02-27-2023",
        "itemName": "Farm2Table Organic Spinach",
        "itemCode": "OrgSpinach",
        "quality": 20,
        "specialCase": 0,
        "sellBy": 10
      }
    ]
  }
}
```

</p>
</details>

- ``orderByAttribute`` options: ``sellBy`` or ``itemName``
- ``orderByAscOrDesc`` options: ``asc`` or ``desc``

<hr></hr>

### /create-or-update

#### http://localhost:8181/zmart/product/items/create-or-update

Create or update product(s) ``PUT``
<details>
<summary>Request Payload</summary>
<p>

```json
{
  "products": [
    {
      "itemName": "Vanilla Pudding",
      "itemCode": "VanPudd",
      "sellBy": 30,
      "specialCase": 0,
      "quality": 40
    },
    {
      "itemName": "Chocolate Pudding",
      "itemCode": "ChocPudd",
      "sellBy": 30,
      "specialCase": 0,
      "quality": 40
    }
  ]
}
```

</p>
</details>

<details>
<summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "count": 2,
    "modified": [
      {
        "id": 7,
        "stockDate": "02-27-2023",
        "itemName": "Vanilla Pudding",
        "itemCode": "VanPudd",
        "quality": 40,
        "specialCase": 0,
        "sellBy": 30
      },
      {
        "id": 8,
        "stockDate": "02-27-2023",
        "itemName": "Chocolate Pudding",
        "itemCode": "ChocPudd",
        "quality": 40,
        "specialCase": 0,
        "sellBy": 30
      }
    ]
  }
}
```

</p>
</details>
<hr></hr>

### /delete

#### http://localhost:8181/zmart/product/items/delete

Delete product(s) ``DELETE``
<details>
<summary>Request Payload</summary>
<p>

```json
{
  "ids": [
    1,3,6
  ]
}
```

</p>
</details>

<details>
<summary>Response Payload</summary>
<p>

```json
{
  "data": {
    "count": 3,
    "deleted": [
      {
        "itemName": "Farm2Table Organic Spinach",
        "id": 1
      },
      {
        "itemName": "Grannysmith Apple",
        "id": 3
      },
      {
        "itemName": "Moonberries",
        "id": 6
      }
    ]
  }
}
```

</p>
</details>
<hr></hr>

<div id="exception-sample">

## Custom Exception Sample

</div>

### 400

<p>HttpMessageNotReadableException</p>
<details>
<summary>Request Payload</summary>
<p>

```json
{
  "quality": "wrong type here",
  "orderByAttribute": "sellBy",
  "orderByAscOrDesc": "desc"
}
```

</p>
</details>

<details>
<summary>Response Payload</summary>
<p>

```json
{
  "code": "400 BAD_REQUEST",
  "message": "Request body contains illegal argument or value type",
  "exception": "AppHttpMessageNotReadableException",
  "cause": {
    "nestedException": "HttpMessageNotReadableException",
    "message": "JSON parse error: Cannot deserialize value of type `java.lang.Integer` from String \"wrong type here\": not a valid `java.lang.Integer` value;",
    "class": "com.zmart.food.product.service.PerishableProductInventoryServiceImpl",
    "method": "getCurrentProductItemListByQualityInventory",
    "caller": "getAllInventoryByQuality",
    "line": 262
  }
}
```

</p>
</details>


## H2 Console

#### http://localhost:8181/h2-console

A database visualizer to view the application's data table(s) while running. Data files will be created and stored under
the ``resources`` directory.
See [application-h2.properties](src/main/resources/application-h2.properties) 
for username & password.

<hr></hr>

<p><a href="#top">Go to top</a></p>

