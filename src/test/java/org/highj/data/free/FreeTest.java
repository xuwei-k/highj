package org.highj.data.free;

import org.highj.data.functions.F0;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class FreeTest {

    private static Free<F0.μ, Long> fib(final Long n) {
        if (n < 2) {
            return Free.done(2L);
        } else {
            return
            Free.suspendF0(() -> fib(n - 1)).flatMap(x ->
                Free.suspendF0(() -> fib(n - 2)).map(y ->
                    x + y
                )
            );
        }
    }

    @Test
    public void fibonacciTest(){
        assertEquals(Long.valueOf(78176338), Free.runF0(fib(37L)));
    }
}
