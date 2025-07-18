# SwingSocketChat

تطبيق مكتبي بلغة Java باستخدام WebSocket وواجهة Swing. يتيح للمستخدمين تسجيل الدخول أو إنشاء حساب، ثم إنشاء أو الانضمام إلى غرف دردشة والتواصل مع الآخرين في الوقت الحقيقي. جميع البيانات تُخزن في قاعدة بيانات MySQL.

## المميزات

- تسجيل مستخدم جديد
- تسجيل الدخول
- إنشاء غرف دردشة
- عرض الغرف المتاحة
- الانضمام إلى غرفة
- إرسال واستقبال الرسائل لحظيًا
- واجهات رسومية مبنية باستخدام Java Swing

## الهيكل العام


## قاعدة البيانات

### اسم القاعدة: `chat_db`

### الجداول:

#### users

| الحقل       | النوع           | الملاحظات                    |
|-------------|------------------|-------------------------------|
| id          | int (PK, AI)     | رقم المستخدم                  |
| username    | varchar(50)      | اسم المستخدم (فريد)          |
| email       | varchar(255)     | البريد الإلكتروني (اختياري)  |
| password    | varchar(255)     | كلمة المرور (مشفر)           |
| created_at  | timestamp        | تاريخ الإنشاء                 |

#### rooms

| الحقل       | النوع            | الملاحظات                   |
|-------------|------------------|------------------------------|
| id          | int (PK, AI)     | رقم الغرفة                   |
| name        | varchar(100)     | اسم الغرفة (فريد)           |

#### room_members

| الحقل       | النوع            | الملاحظات                               |
|-------------|------------------|------------------------------------------|
| id          | int (PK, AI)     | رقم السطر                                |
| user_id     | int (FK)         | معرف المستخدم                            |
| room_id     | int (FK)         | معرف الغرفة                              |
| (user_id, room_id) | فريد     | يمنع التكرار في نفس الغرفة لنفس المستخدم |

#### messages

| الحقل       | النوع            | الملاحظات                   |
|-------------|------------------|------------------------------|
| id          | int (PK, AI)     | رقم الرسالة                 |
| user_id     | int (FK)         | مرسل الرسالة                |
| room_id     | int (FK)         | الغرفة التي أرسلت فيها الرسالة |
| content     | text             | محتوى الرسالة               |
| sent_at     | timestamp        | توقيت الإرسال               |


