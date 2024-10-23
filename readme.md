Servisler
Order-Service
Order-Service, aşağıdaki iki endpoint'i sunar:

POST /order \
Açıklama: Bu endpoint, sipariş oluşturmak için kullanılır.

İşleyiş:\
Gelen sipariş isteğinde yer alan ürünler ve miktarları, Product-Service'in /validate endpoint'ine iletilir.\
Product-Service, ürünlerin geçerliliğini kontrol eder ve ürünlerin detay bilgilerini (isim, fiyat, vergi vb.) döner. \
Eğer ürünler geçerliyse, Product-Service'in /products/stock/decrement endpoint'ine istekte bulunarak stok miktarları düşürülür. \
Son olarak, sipariş bilgileri veritabanına kaydedilir.


GET /invoice/{orderId} \
Açıklama: Bu endpoint, belirli bir sipariş ID'sine göre fatura oluşturur.

İşleyiş:\
Veritabanına bir istek göndererek belirtilen orderId ile siparişi alır.\
Alınan sipariş bilgilerini kullanarak bir fatura oluşturur ve PDF formatında döner.


Çalıştırmak için: docker compose up\
Database için: http://localhost:8091 \
Swagger için: http://localhost:8080 \
product-service > init > CouchbaseHandler içersinde initial insert'ler mevcut buradan gerekirse eklenebilir.\
order-service > init > CouchbaseHandler içersinde initial insert'ler mevcut buradan gerekirse eklenebilir.


Örnek Request'ler:
order oluşturmak için: \
curl -X POST http://localhost:8080/order \
-H "Content-Type: application/json" \
-d '{
"products": [
{
"id": "apple-2000",
"quantity": 5
},
{
"id": "macbook-10000",
"quantity": 5
}
]
}'

Oluşturulan order'dan pdf halinde fatura oluşturmak için:\
curl -X GET http://localhost:8080/invoices/3edfb7d8-253f-4159-b008-8358ed1c9596
