package org.highj.data.free;

import org.highj._;
import org.highj.typeclass1.functor.Functor;
import org.highj.typeclass1.monad.Monad;
import java.util.function.Function;

public final class FreeMonad<S> implements Monad<Free<S, ?>> {

    private final Functor<S> F;

    public FreeMonad(final Functor<S> F) {
        this.F = F;
    }

    @Override
    public <A> _<Free<S, ?>, A> pure(A a) {
        return Free.done(a);
    }

    @Override
    public <A, B> _<Free<S, ?>, B> bind(_<Free<S, ?>, A> fa, Function<A, _<Free<S, ?>, B>> f) {
        return Free.narrow(fa).flatMap(a -> Free.narrow(f.apply(a)));
    }

    @Override
    public <A, B> _<Free<S, ?>, B> ap(_<Free<S, ?>, Function<A, B>> f, _<Free<S, ?>, A> fa) {
        return Free.narrow(f).flatMap(a -> Free.narrow(map(a, fa)));
    }

    @Override
    public <A, B> _<Free<S, ?>, B> map(Function<A, B> fn, _<Free<S, ?>, A> fa) {
        return Free.narrow(fa).map(fn);
    }
}
