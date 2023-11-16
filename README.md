<h3>Task</h3>
<li>Создать любой gradle проект</li>
<li>Проект должен быть совместим с java 17</li>
<li>Придерживаться GitFlow: master -> develop -> feature/fix</li>
<li>Создать реализацию кэша, используя алгоритмы LRU и LFU</li>
<li>Создать в приложении слои service и dao (service будет вызывать слой dao, слой dao будет временная замена database). В этих сервисах реализовать CRUD операции для работы с entity. Работу организовать через интерфейсы.</li>
<li>Результат работы dao должен синхронизироваться с кешем через proxy (или кастомная аннотация, или АОП/aspectj). При работе с entity оперируем id. Алгоритм работы с кешем:</li>
<li>GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем</li>
<li>POST - сохраняем в dao и потом сохраняем в кеше</li>
<li>DELETE - удаляем из dao и потом удаляем из кеша</li>
<li>PUT - обновление/вставка в dao и потом обновление/вставка в кеше</li>
<li>Алгоритм и максимальный размер коллекции должны читаться из файла resources/application.yml</li>
<li>Создать entity, в нем должно быть поле id и еще минимум 4 поля</li>
<li>Service работает с dto</li>
<li>Объекты (dto), которые принимает service, должны валидироваться. В т.ч. добавить regex валидацию</li>
<li>Кеши должны быть покрыты unit tests</li>
<li>Должен содержать javadoc и описанный README.md</li>
<li>Использовать lombok</li>
<li>*Реализовать метод для получения информации в формате xml</li>

<h3>Solution</h3>
<p>* перед началом работы настройте application.yaml (src/main/resources/application.yaml) и выполните скрипты (src/main/resources/db, 3 шт.) из src/main/resources/db</p>
<p>** для тестов дополнительно создайте БД по скрипту db_create.sql из src/test/resources/db_create.sql</p>
<li>Алгоритмы кеширования реализованы через интерфейс Cache (src/main/java/by/clevertec/proxy/cache/Cache.java) в пакете impl.</li>
<li>Слой dao разу реализован в виде базы postgreSql*.</li>
<li>Для синхронизации с кешем использована реализация через aspectj (src/main/java/by/clevertec/proxy/proxy/CacheableAspect.java)</li>
<li>При работе с БД роль ID выполняет UUID. Примеры валидного UUID - "UUID.fromString("76dbb74c-2f08-4bc0-8029-aed02147e737")"</li>
<li>Для entity реализована валидация, требования указаны над сущностью Product (src/main/java/by/clevertec/proxy/entity/Product.java)</li>
<li>Необходимые методы сервиса под будущие запросы GET/POST/DELETE/PUT реализованы в сервисе ProductService (src/main/java/by/clevertec/proxy/service/ProductService.java)</li>
<li>Взаимодействие с БД через слой репозитория ProductRepository (src/main/java/by/clevertec/proxy/repository/ProductRepository.java). Для работы с запросами использован аналог QueryRunner и BasicDataSource в качестве пула соединений (org.apache.commons)</li>
<li>Чтение настроечных переменных осуществляется классом AppConfig (src/main/java/by/clevertec/proxy/config/AppConfig.java)</li>