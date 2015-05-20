package org.highj.data.functions;

import org.highj._;
import org.highj.typeclass1.functor.Functor;

import java.util.function.Function;

@FunctionalInterface
public interface F0<A> extends _<F0.μ, A> {

    public abstract A apply();

    public default <B> F0<B> map(final Function<A, B> f) {
        return () -> f.apply(this.apply());
    }

    public static final class μ {
    }

    public static <B> F0<B> narrow(final _<F0.μ, B> a){
        return (F0<B>)a;
    }

    public static final Functor<μ> functor = new Functor<F0.μ>() {
        @Override
        public <A, B> _<μ, B> map(Function<A, B> fn, _<μ, A> nestedA) {
            return narrow(nestedA).map(fn);
        }
    };

}
