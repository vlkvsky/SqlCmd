package src.test.integration;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import src.Main;
import src.model.*;
import src.test.BeforeTestsChangeNameAndPass;
import src.test.Support;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class IntegrationTest {

    private static final String DATABASE = BeforeTestsChangeNameAndPass.DATABASE;
    private static final String USER = BeforeTestsChangeNameAndPass.USER;
    private static final String PASSWORD = BeforeTestsChangeNameAndPass.PASSWORD;

    private final String commandConnect = "connect|" + DATABASE + "|" + USER + "|" + PASSWORD;
    private final String commandDisconnect = "connect|" + "" + "|" + USER + "|" + PASSWORD;
    private final String pleaseConnect = "Введите имя базы данных, с которой будем работать, имя пользователя и пароль в формате: " +
            "connect|database|userName|password\n";

    private static DatabaseManager manager;
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void buildDatabase() {
        manager = new PostgresManager();
        Support.setupData(manager);
    }

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @AfterClass
    public static void dropDatabase() {
        Support.deleteData(manager);
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // help
                "Существующие команды:\n" +
                "\tconnect|databaseName|userName|password\n" +
                "\t\tдля подключения к базе данных, с которой будем работать\n" +
                "\tdatabases\n" +
                "\t\tдля получения списка баз\n" +
                "\ttables\n" +
                "\t\tдля получения списка всех таблиц базы, к которой подключились\n" +
                "\tcreateDB|databaseName\n" +
                "\t\tдля создания новой Database. Имя базы должно начинаться с буквы.\n" +
                "\tdropDB|databaseName\n" +
                "\t\tдля удаления Database. База должна быть свободна от любого конекшина.\n" +
                "\tcreateTable\n" +
                "\t\tдля создания новой таблицы пошагово\n" +
                "\tcreateTableSQL|tableName(column1,column2,...,columnN)\n" +
                "\t\tдля создания новой таблицы знающих SQL, в круглых скобках вставить опиание колонок в SQL формате, пример:\n" +
                "\t\tcreateTableSQL|user(id SERIAL NOT NULL PRIMARY KEY,username varchar(225) NOT NULL UNIQUE, password varchar(225))\n" +
                "\tsize|tableName\n" +
                "\t\tколичество строк в таблице\n" +
                "\tclear|tableName\n" +
                "\t\tдля очистки всей таблицы\n" +
                "\tdropTable|tableName\n" +
                "\t\tдля удаления таблицы\n" +
                "\tinsertTable|tableName\n" +
                "\t\tдля пошагового создания записи в существующей таблице\n" +
                "\tinsert|tableName|column1|value1|column2|value2|...|columnN|valueN\n" +
                "\t\tдля создания записи в существующей таблице\n" +
                "\tfind|tableName\n" +
                "\t\tдля получения содержимого таблицы 'tableName'\n" +
                "\thelp\n" +
                "\t\tдля вывода этого списка на экран\n" +
                "\texit\n" +
                "\t\tдля выхода из программы\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r\n", "\n");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testExit() {
        // given
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testTablesWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // tables
                "Вы не можете пользоваться командами, пока не подключитесь с помощью комманды connect|databaseName|userName|password\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        // given
        in.add("find|user");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // find|user
                "Вы не можете пользоваться командами, пока не подключитесь с помощью комманды connect|databaseName|userName|password\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // unsupported
                "Вы не можете пользоваться командами, пока не подключитесь с помощью комманды connect|databaseName|userName|password\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("unsupported");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // unsupported
                "Несуществующая команда: unsupported\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testTablesAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("tables");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // tables
                "Существующие таблицы: users, test1, users2\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testFindAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("find|users");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "+-----+--------+--+\n" +
                "|name |password|id|\n" +
                "+-----+--------+--+\n" +
                "|Vasia|****    |22|\n" +
                "+-----+--------+--+\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("tables");
        in.add("connect|test|" + USER + "|" + PASSWORD);
        in.add("tables");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // tables
                "Существующие таблицы: users, test1, users2\n" +
                "Введи команду (или help для помощи):\n" +
                // connect test
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // tables
                "Существующие таблицы: qwe\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|" + DATABASE);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Неудача! по причине: Формат команды 'connect|databaseName|userName|password', а ты ввел: connect|sqlcmd5hope5never5exist\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testFindAfterConnect_withData() {
        // given
        in.add(commandConnect);
        in.add("clear|users");
        in.add("insert|users|id|13|name|Stiven|password|*****");
        in.add("insert|users|id|14|name|Eva|password|+++++");
        in.add("find|users");
        in.add("clear|users");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // clear|users
                "Таблица users была успешно очищена.\n" +
                "Введи команду (или help для помощи):\n" +
                // insert|users|id|13|name|Stiven|password|*****
                "В таблице 'users' была успешно добавлена запись:\n" +
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Stiven|*****   |\n" +
                "+--+------+--------+\n" +
                "Введи команду (или help для помощи):\n" +
                // insert|users|id|14|name|Eva|password|+++++
                "В таблице 'users' была успешно добавлена запись:\n" +
                "+--+----+--------+\n" +
                "|id|name|password|\n" +
                "+--+----+--------+\n" +
                "|14|Eva |+++++   |\n" +
                "+--+----+--------+\n" +
                "Введи команду (или help для помощи):\n" +
                // find|users
                "+------+--------+--+\n" +
                "|name  |password|id|\n" +
                "+------+--------+--+\n" +
                "|Stiven|*****   |13|\n" +
                "+------+--------+--+\n" +
                "|Eva   |+++++   |14|\n" +
                "+------+--------+--+\n" +
                "Введи команду (или help для помощи):\n" +
                "Таблица users была успешно очищена.\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add(commandConnect);
        in.add("clear|sadfasd|fsf|fdsf");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then

        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // clear|sadfasd|fsf|fdsf
                "Неудача! по причине: Формат команды 'clear|tableName', а ты ввел: clear|sadfasd|fsf|fdsf\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add(commandConnect);
        in.add("insert|user|error");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // insert|user|error
                "Неудача! по причине: Должно быть четное количество параметров в формате 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'insert|user|error'\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateTableSimple() {
        // given
        in.add(commandConnect);
        in.add("createTable");
        in.add("users5");
        in.add("id");
        in.add("name");
        in.add("password");
        in.add("5");
        in.add("dropTable|users5");
        in.add("Y");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите имя для создаваемой таблицы(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя новой базы: users5\n" +
                "Введите имя для колонки PRIMARY KEY(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя колонки PRIMARY KEY: id\n" +
                "Введите имя для еще одной колонки(имя должно начинаться с буквы) или '5' для создания базы с введенными колонками или '0' для выхода в основное меню\n" +
                "Имя еще одной колонки: name\n" +
                "Введите имя для еще одной колонки(имя должно начинаться с буквы) или '5' для создания базы с введенными колонками или '0' для выхода в основное меню\n" +
                "Имя еще одной колонки: password\n" +
                "Введите имя для еще одной колонки(имя должно начинаться с буквы) или '5' для создания базы с введенными колонками или '0' для выхода в основное меню\n" +
                "Таблица users5 была успешно создана.\n" +
                "+--+----+--------+\n" +
                "|id|name|password|\n" +
                "+--+----+--------+\n" +
                "Введи команду (или help для помощи):\n" +
                "Вы уверены, что хотите удалить users5? Y/N\n" +
                "Таблица users5 была успешно удалена.\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testTableSimpleExit() {
        // given
        in.add(commandConnect);
        in.add("createTable");
        in.add("");
        in.add("1");
        in.add("0");
        in.add("createTable");
        in.add("user6");
        in.add("");
        in.add("0");
        in.add("createTable");
        in.add("user6");
        in.add("id");
        in.add("");
        in.add("0");
        in.add("createTable");
        in.add("user6");
        in.add("id");
        in.add("name");
        in.add("0");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите имя для создаваемой таблицы(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Нужно ввести имя для создаваемой таблицы, а вы вели пустую строку\n" +
                "Введите имя для создаваемой таблицы(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя должно начинаться с буквы, а у тебя начинается с '1'\n" +
                "Введите имя для создаваемой таблицы(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Выход в основное меню\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите имя для создаваемой таблицы(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя новой базы: user6\n" +
                "Введите имя для колонки PRIMARY KEY(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Нужно ввести имя для колонки PRIMARY KEY, а вы вели пустую строку\n" +
                "Введите имя для колонки PRIMARY KEY(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Выход в основное меню\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите имя для создаваемой таблицы(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя новой базы: user6\n" +
                "Введите имя для колонки PRIMARY KEY(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя колонки PRIMARY KEY: id\n" +
                "Введите имя для еще одной колонки(имя должно начинаться с буквы) или '5' для создания базы с введенными колонками или '0' для выхода в основное меню\n" +
                "Нужно ввести имя для колонки, а вы вели пустую строку\n" +
                "Введите имя для еще одной колонки(имя должно начинаться с буквы) или '5' для создания базы с введенными колонками или '0' для выхода в основное меню\n" +
                "Выход в основное меню\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите имя для создаваемой таблицы(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя новой базы: user6\n" +
                "Введите имя для колонки PRIMARY KEY(имя должно начинаться с буквы) или '0' для выхода в основное меню\n" +
                "Имя колонки PRIMARY KEY: id\n" +
                "Введите имя для еще одной колонки(имя должно начинаться с буквы) или '5' для создания базы с введенными колонками или '0' для выхода в основное меню\n" +
                "Имя еще одной колонки: name\n" +
                "Введите имя для еще одной колонки(имя должно начинаться с буквы) или '5' для создания базы с введенными колонками или '0' для выхода в основное меню\n" +
                "Выход в основное меню\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testInsertSimple() {
        // given
        in.add(commandConnect);
        in.add("insertTable|users2");
        in.add("1");
        in.add("Frank");
        in.add("****");
        in.add("clear|users2");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите значение в поле 'id' или введите '0' для выхода в основное меню.\n" +
                "Введите значение в поле 'username' или введите '0' для выхода в основное меню.\n" +
                "Введите значение в поле 'password' или введите '0' для выхода в основное меню.\n" +
                "В таблице 'users2' была успешно добавлена запись:\n" +
                "+--+--------+--------+\n" +
                "|id|username|password|\n" +
                "+--+--------+--------+\n" +
                "|1 |Frank   |****    |\n" +
                "+--+--------+--------+\n" +
                "Введи команду (или help для помощи):\n" +
                "Таблица users2 была успешно очищена.\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testInsertSimpleExit() {
        // given
        in.add(commandConnect);
        in.add("insertTable|users2");
        in.add("");
        in.add("1");
        in.add("0");
        in.add("insertTable|users2");
        in.add("1");
        in.add("");
        in.add("Frank");
        in.add("0");
        in.add("insertTable|users2");
        in.add("1");
        in.add("Frank");
        in.add("");
        in.add("0");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите значение в поле 'id' или введите '0' для выхода в основное меню.\n" +
                "Нужно ввести имя для колонки PRIMARY KEY, а вы вели пустую строку\n" +
                "Введите значение в поле 'id' или введите '0' для выхода в основное меню.\n" +
                "Введите значение в поле 'username' или введите '0' для выхода в основное меню.\n" +
                "Выход в основное меню\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите значение в поле 'id' или введите '0' для выхода в основное меню.\n" +
                "Введите значение в поле 'username' или введите '0' для выхода в основное меню.\n" +
                "Нужно ввести имя для колонки PRIMARY KEY, а вы вели пустую строку\n" +
                "Введите значение в поле 'username' или введите '0' для выхода в основное меню.\n" +
                "Введите значение в поле 'password' или введите '0' для выхода в основное меню.\n" +
                "Выход в основное меню\n" +
                "Введи команду (или help для помощи):\n" +
                "Введите значение в поле 'id' или введите '0' для выхода в основное меню.\n" +
                "Введите значение в поле 'username' или введите '0' для выхода в основное меню.\n" +
                "Введите значение в поле 'password' или введите '0' для выхода в основное меню.\n" +
                "Нужно ввести имя для колонки PRIMARY KEY, а вы вели пустую строку\n" +
                "Введите значение в поле 'password' или введите '0' для выхода в основное меню.\n" +
                "Выход в основное меню\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDropDatabaseException() {
        // given
        in.add(commandConnect);
        in.add("dropDB|sqlcmd965823756925");
        in.add("y");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then

        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "Вы уверены, что хотите удалить sqlcmd965823756925? Y/N\n" +
                "Неудача! по причине: ERROR: database \"sqlcmd965823756925\" does not exist\n" +
                "Введи команду (или help для помощи):\n" +
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Ignore // тест занимает много времени, половина от всех вместе взятых...
    @Test
    public void testCreateDropDatabase() {
        // given
        in.add(commandConnect);
        in.add("createDB|sqlcmd9hope9never9exist");
        in.add("dropDB|sqlcmd9hope9never9exist");
        in.add("y");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then

        assertEquals(pleaseConnect +
                // connect
                "Успех!\n" +
                "Введи команду (или help для помощи):\n" +
                "Database sqlcmd9hope9never9exist была успешно создана.\n" +
                "Введи команду (или help для помощи):\n" +
                "Вы уверены, что хотите удалить sqlcmd9hope9never9exist? Y/N\n" +
                "Database 'sqlcmd9hope9never9exist' была успешно удалена.\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

}
