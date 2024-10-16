# Spring Boot ile JWT ve Redis Tabanlı Kimlik Doğrulama

Bu proje, **Spring Boot** kullanarak geliştirilen JWT (JSON Web Token) tabanlı bir kimlik doğrulama sistemidir. Projede, kullanıcı oturum yönetimi için JWT token'ları kullanılırken, token'ların güvenli ve hızlı bir şekilde yönetilmesi için Redis kullanılmıştır.


## Neden Redis Kullandık?
JWT (JSON Web Token) ile kimlik doğrulama yaparken, token'ı Redis gibi bir önbellekleme sistemine kaydetmenin birkaç avantajı vardır. Ancak bu avantajlar, kullanım senaryosuna ve güvenlik gereksinimlerine bağlıdır. JWT ile Redis kullanımının avantajlarını ve dezavantajlarını gözden geçirelim:


- **Token Geçersiz Kılma (Revocation) Kolaylığı:** JWT'ler genellikle sunucusuz (stateless) bir yapıda çalışır ve sunucuda depolanmaz. Ancak bu, bir JWT'nin geçersiz kılınmasını zorlaştırabilir. Eğer bir kullanıcının oturumu sonlandırılmak veya bir token iptal edilmek istenirse, token'in geçerliliğini kontrol etmenin bir yolu yoktur. Redis'e token'ı kaydetmek, gerektiğinde token'ı geçersiz kılmayı kolaylaştırır. Örneğin, kullanıcı "çıkış yap" dediğinde Redis'teki token silinebilir, böylece geçersiz olur.
- **Kara Liste (Blacklist) Uygulaması:** Token süresi dolmadan önce geçersiz kılınması gereken bir durum olduğunda (örneğin, kullanıcının yetkileri değiştiğinde veya bir güvenlik ihlali fark edildiğinde), token'lar Redis üzerinde kara listeye alınabilir. Bu, token'ların geçerliliğini dinamik olarak kontrol etme imkanı sağlar.
- **Oturum Yönetimi:** JWT'ler stateless olsa da, bazı durumlarda kullanıcı oturumlarının izlenmesi gerekebilir. Redis, kullanıcıların token'larını saklayarak oturum yönetimi yapmayı sağlar. Bu, aynı zamanda aynı kullanıcının birden fazla cihazda oturum açıp açmadığını takip etmenizi kolaylaştırır.
- **Token Yenileme (Refresh) ve Uzatma Kolaylığı:** Eğer token'ın ömrü uzatılmak isteniyorsa, Redis'teki token'ların TTL (Time-to-Live) değerleri güncellenerek, bu işlem kolayca gerçekleştirilebilir. Bu sayede sürekli olarak yeni token üretmek yerine mevcut token'ın ömrü uzatılabilir.

## Ne Zaman Redis Kullanmalıyız?
- Eğer token geçersiz kılma veya oturum yönetimi gibi özelliklere ihtiyaç duyuyorsanız, Redis mantıklı bir seçim olabilir.
- Eğer çok cihazlı giriş veya anlık oturum sonlandırma gibi özellikler isteniyorsa, Redis kullanımı bu tür gereksinimleri karşılayabilir.
- Ancak JWT'yi tamamen stateless ve basit tutmak istiyorsanız, Redis'e ihtiyaç olmayabilir.

## Akışı Şu Şekilde

- **Token Oluşturma:**
    - Kullanıcı giriş yaptığında, sunucu bir JWT oluşturur ve bu token'ı istemciye döner.
    - Aynı zamanda bu token'ı Redis'e kaydeder (örneğin, bir süre ile birlikte). Bu kayıt, token'ın belirli bir süre boyunca geçerli olduğunu gösterir.
- **Token İptali:**
    - Kullanıcı çıkış yaptığında ya da belirli bir güvenlik durumunda (örneğin, kullanıcının yetkilerinin değiştirilmesi) token geçersiz kılınır.
    - Burada iki yaklaşım olabilir:
      - Kayıt Silme: Kullanıcı çıkış yaptığında, ilgili token'ı Redis'ten silmek. Bu durumda, bir sonraki istek geldiğinde token Redis'te bulunmaz ve geçersiz kabul edilir.
      - Kara Listeye Alma: Token geçersiz kılındığında, bunu bir kara listeye almak. Bu yöntem, belirli durumlarda (örneğin, güvenlik ihlali) token'ın iptal edilmesini kolaylaştırır. Burada token'ın süresi dolana kadar geçersiz olduğu bilgisi Redis'te tutulur.
- **Token Kontrolü:**
    - Kullanıcı, korumalı bir endpoint'e istek gönderdiğinde, istemciden gelen JWT token'ı kontrol edilir.
    - İlk Adım: Sunucu, token'ın imzasını doğrular (kriptografik kontrol).
    - İkinci Adım: Redis'te bu token'ın kaydı kontrol edilir:
        - Eğer token Redis'te bulunmuyorsa: Token geçersizdir ve sunucu 401 Unauthorized döner.
        - Eğer token Redis'te bulunuyorsa: Token geçerlidir ve sunucu isteği işleme alır.

## Özetle
Redis, token'ın geçerliliğini yönetmek için iki ana amaç taşır:
- Token'ın iptal edilip edilmediğini kontrol etmek.
- Kullanıcının güvenlik durumuna göre oturum yönetimini sağlamak.
