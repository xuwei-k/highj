package org.highj.data.tuple.t3;

import org.highj._;
import org.highj.__;
import org.highj.___;
import org.highj.data.tuple.T3;
import org.highj.typeclass0.group.Semigroup;
import org.highj.typeclass1.monad.Apply;

import java.util.function.Function;

public interface T3Apply<S,T> extends T3Functor<S,T>, Apply<__.µ<___.µ<T3.µ, S>,T>> {
    public Semigroup<S> getS();
    public Semigroup<T> getT();

    @Override
    public default <A, B> T3<S, T, B> ap(_<__.µ<___.µ<T3.µ, S>,T>, Function<A, B>> fn,
                                                          _<__.µ<___.µ<T3.µ, S>,T>, A> nestedA) {
        T3<S, T, Function<A, B>> fnTriple = T3.narrow(fn);
        T3<S, T, A> aTriple = T3.narrow(nestedA);
        return T3.of(getS().apply(fnTriple._1(), aTriple._1()),
                getT().apply(fnTriple._2(), aTriple._2()),
                fnTriple._3().apply(aTriple._3()));
    }
}
