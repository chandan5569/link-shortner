#  Shortly : The simple link-shortner

Test link :-  https://newi.ga/  

## API 

 `/{shortCode}`   :  **GET**  ->  Redirects to the specified URL, 400 Bad request if invalid code
 <br>
`/generate  `    : **POST**  JSON  Body   { "link" : "http://apple.com" }   ->
 Return a shortned url as    {  "shortUrl": "https://newi.ga/p5l0pttp" }

## Intalling 

```
sudo apt install openjdk-11-jre-headless
```
Clone the  repository and configure the server.prop
 
    rootUrl= http://newi.ga  ## The public url of the server
    port=80  ## The port number 

Then execute the command to start the server
```
 java -jar build/libs/shortly.jar 
```
##Author: Chandan Kumar Singh
