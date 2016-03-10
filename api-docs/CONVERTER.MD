Features
===========

 - Convert raw HTML to PDF.
 - Convert raw HTML to EXCEL.


###Convert raw HTML to PDF

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus-converter/{version}/html-to-pdf |
| Auth | Required |
| Request Body Type | None |
| Request Body  | None |
| Response Code | 201 Successful |


**Authorization Header**

```
Authorization: Token MTQ1NTc5OTI3NDU0Mzphbm9ueW1vdXM6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```

**Response body**

```json
  {
    "html": "<h2>Hello world!!</h2>",
    "filename": "helloworld"
  }
```

** Location Header **

```
    Location: //test-content-bucket01.s3-us-west-1.amazonaws.com/helloworld.pdf
```

**CURL snippet**

```posh

curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NTc5OTI3NDU0Mzphbm9ueW1vdXM6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "html": "<h2>Hello world!!</h2>",
    "filename": "helloworld"
}' http://nucleus.gooru.org/api/nucleus-converter/v1/html-to-pdf
```

###Convert raw HTML to EXCEL

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus-converter/{version}/html-to-excel |
| Auth | Required |
| Request Body Type | None |
| Request Body  | None |
| Response Code | 201 Successful |


**Authorization Header**

```
Authorization: Token MTQ1NTc5OTI3NDU0Mzphbm9ueW1vdXM6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```

**Response body**

```json
  {
    "html": "<html><body><table border=1><tr><th>Name</th><th>Date of birth</th></tr><tr><td>Gooru</td><td>11/12/1987</td></tr></table></body>",
    "filename": "student"
  }
```

**Location Header**

```
    Location: //test-content-bucket01.s3-us-west-1.amazonaws.com/student.xlsx
```

**CURL snippet**

```posh

curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NTc5OTI3NDU0Mzphbm9ueW1vdXM6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "html": "<html><body><table border=1><tr><th>Name</th><th>Date of birth</th></tr><tr><td>Gooru</td><td>11/12/1987</td></tr></table></body>",
    "filename": "student"
  }' http://nucleus.gooru.org/api/nucleus-converter/v1/html-to-excel
```
