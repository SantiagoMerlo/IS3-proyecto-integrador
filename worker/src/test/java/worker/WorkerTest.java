package worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.*;
import org.mockito.*;

import redis.clients.jedis.Jedis;

public class WorkerTest {
    
    //@Test
    //static void validateConnectionTest(String host){
    //    String connstr = Worker.getConnectionString(host);
    //    assertEquals("jdbc:postgresql://db/postgres", connstr );
    //}

    //@Test
    //static void JedisHostTest(String host){
    //    assertEquals(new Jedis(host), Worker.JedisHost(host));
    //}

    @Test
    static void redisPingTest(){
        Jedis conn = mock(Jedis.class);
        assertEquals("Server is running: PONG", Worker.redisPing(conn));
    }

    //@Test
    //static void ConnectToDBTest(){
    //    Jedis conn = mock(Jedis.class);
    //    Assert(Worker
    //}
}