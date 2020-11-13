package worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.*;
import org.mockito.*;

import redis.clients.jedis.Jedis;

public class WorkerTest {

    @Test
    static void redisPingTest(){
        Jedis conn = mock(Jedis.class);
        assertEquals("Server is running: PONG", Worker.redisPing(conn));
    }
}