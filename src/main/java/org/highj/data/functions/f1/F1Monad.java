package org.highj.data.functions.f1;

import org.highj._;
import org.highj.__;
import org.highj.data.functions.F1;
import org.highj.typeclass1.monad.Applicative;
import org.highj.typeclass1.monad.Monad;

import java.util.function.Function;

public class F1Monad<R> implements Monad<__.µ<F1.µ, R>> {

    @Override
    public <A> F1<R, A> pure(A a) {
        //pure = const
        return F1.constant(a);
    }

    @Override
    public <A, B> F1<R, B> ap(_<__.µ<F1.µ, R>, Function<A, B>> fn, _<__.µ<F1.µ, R>, A> nestedA) {
        //(<*>) f g x = f x (g x)
        final F1<R, Function<A, B>> fRAB = F1.narrow(fn);
        final F1<R, A> fRA = F1.narrow(nestedA);
        return r -> fRAB.apply(r).apply(fRA.apply(r));
    }

    @Override
    public <A, B> F1<R, B> map(Function<A, B> fAB, _<__.µ<F1.µ, R>, A> nestedA) {
        return F1.compose((F1<A, B>) fAB::apply, F1.narrow(nestedA));
    }

    @Override
    public <A, B> F1<R, B> bind(_<__.µ<F1.µ, R>, A> a, Function<A, _<__.µ<F1.µ, R>, B>> fn) {
        //m >>= k  = Reader $ \r -> runReader (k (runReader m r)) r
        return r -> F1.narrow(fn.apply(F1.narrow(a).apply(r))).apply(r);
    }
}