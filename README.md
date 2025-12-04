# Sınıf Kütüphanesi Uygulaması

Bu proje, sınıf içi kitap takibi yapmak için geliştirilmiş, internet gerektirmeyen, tamamen yerel (native) bir Android uygulamasıdır.

## Özellikler
- **Kitap Ekleme:** Kütüphanenize sınırsız sayıda kitap ekleyebilirsiniz.
- **Öğrenci Ekleme:** Sınıfınızdaki öğrencileri kaydedebilirsiniz.
- **Kitap Verme (Ödünç):** Bir kitabı seçip kayıtlı bir öğrenciye verebilirsiniz.
- **İade Alma:** Verilen kitabı geri alıp tekrar rafa kaldırabilirsiniz.
- **Takip:** Hangi kitabın kimde olduğunu liste üzerinde görebilirsiniz.
- **Veri Saklama:** Tüm veriler telefon hafızasında (SQLite veritabanı) saklanır, internet gerektirmez.

## Kurulum ve APK Oluşturma

Bu bir Android Studio projesidir. APK dosyasını oluşturmak için aşağıdaki adımları izleyin:

1. **Android Studio'yu İndirin:** Bilgisayarınızda Android Studio kurulu değilse [buradan](https://developer.android.com/studio) indirip kurun.
2. **Projeyi Açın:**
   - Android Studio'yu başlatın.
   - "Open" seçeneğine tıklayın.
   - Bu dosyaların bulunduğu klasörü (`e:\kutuphane`) seçin ve "OK" deyin.
   - Projenin senkronize olmasını (Gradle Sync) bekleyin. İnternet bağlantısı gerekebilir (sadece ilk kurulumda kütüphaneleri indirmek için).
3. **APK Oluşturun:**
   - Üst menüden **Build > Build Bundle(s) / APK(s) > Build APK(s)** seçeneğine tıklayın.
   - İşlem tamamlandığında sağ altta "APK(s) generated successfully" uyarısı çıkacaktır. "Locate" yazısına tıklayarak APK dosyasını bulabilirsiniz.
4. **Telefona Yükleyin:**
   - Oluşan `app-debug.apk` dosyasını telefonunuza gönderin (USB, WhatsApp, Mail vb.).
   - Telefonda dosyayı açıp yükleyin. (Bilinmeyen kaynaklara izin vermeniz gerekebilir).

## Kullanım
1. Uygulamayı açın.
2. Sağ üstteki menüden (üç nokta) **"Öğrenci Ekle"** diyerek öğrencilerinizi ekleyin.
3. Sağ alttaki **"+"** butonuna basarak kitaplarınızı ekleyin.
4. Listeden bir kitaba tıklayarak **"Kitap Ver"** diyebilir ve öğrenci seçebilirsiniz.
5. Kırmızı renkli (verilmiş) bir kitaba tıklayarak iade alabilirsiniz.
6. Bir kitabı silmek için üzerine basılı tutun.

İyi dersler!
