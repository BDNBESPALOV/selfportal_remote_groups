# selfportal_remote_groups
Анализирует файлы .txt из папки users (которую нужно расположить корне сборки) разбирая их для вставки в таблицу базы h2 и дальнейшей выборки через SQL запрос:

```sql
select lastname ,firstname,surname,mail, STRING_AGG(groupname,', ') AS Член_групп_СКДПУ from users group by lastname,firstname,surname,mail order by lastname;
```

После полученная выпорка отправляется в exel файл.     
