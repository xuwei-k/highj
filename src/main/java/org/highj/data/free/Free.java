package org.highj.data.free;

import org.highj._;
import org.highj.data.collection.Either;
import org.highj.data.collection.Maybe;
import org.highj.data.functions.F0;
import org.highj.typeclass1.functor.Functor;
import org.highj.typeclass1.monad.Applicative;

import java.util.function.Function;

public abstract class Free<F, A> implements _<Free<F, ?>, A> {
    private Free() {
    }

    public static final class µ {
    }

    public static <G, B> Free<G, B> done(final B b) {
        return new Done<>(b);
    }

    public static <G, B> Free<G, B> liftF(final _<G, B> value) {
        return new Suspend<>(value);
    }

    public static <G, A> Free<G, A> roll(final _<G, Free<G, A>> f) {
        return Free.liftF(f).flatMap(a -> a);
    }

    public static <G, B> Free<G, B> suspend(final Free<G, B> b, final Applicative<G> F) {
        return liftF(F.pure(Maybe.Nothing())).flatMap(a -> narrow(b));
    }

    public static <G, B> Free<G, B> narrow(final _<Free<G, ?>, B> f) {
        return (Free<G, B>) f;
    }

    public final A go(final Function<_<F, Free<F, A>>, Free<F, A>> f, final Functor<F> F) {
        Free<F, A> current = this;
        while (true) {
            final Either<_<F, Free<F, A>>, A> either = current.resume(F);
            if (either.isLeft()) {
                current = f.apply(either.getLeft());
            } else {
                return either.getRight();
            }
        }
    }

    private static <X1, X2, F, A> Either<_<F, Free<F, A>>, A> resume(Free<F, A> current, final Functor<F> F) {
        while (true) {
            if (current instanceof Done) {
                return Either.newRight(current.asDone().a);
            } else if (current instanceof Suspend) {
                return Either.newLeft(F.map(Free::done, current.asSuspend().a));
            } else {
                final Gosub<F, X1, A> gosub1 = current.asGosub();
                if (gosub1.a instanceof Done) {
                    current = gosub1.f.apply(gosub1.a.asDone().a);
                } else if (gosub1.a instanceof Suspend) {
                    return Either.newLeft(F.map(gosub1.f, gosub1.a.asSuspend().a));
                } else {
                    final Gosub<F, X2, X1> gosub2 = gosub1.a.asGosub();
                    current = gosub2.a.flatMap(o ->
                                    gosub2.f.apply(o).flatMap(gosub1.f)
                    );
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <X> Gosub<F, X, A> asGosub() {
        return (Gosub<F, X, A>) this;
    }

    private Suspend<F, A> asSuspend() {
        return (Suspend<F, A>) this;
    }

    private Done<F, A> asDone() {
        return (Done<F, A>) this;
    }

    public abstract <B> Free<F, B> flatMap(final Function<A, Free<F, B>> f);

    public final Either<_<F, Free<F, A>>, A> resume(final Functor<F> F) {
        return resume(this, F);
    }

    public final <B> Free<F, B> map(final Function<A, B> f) {
        return flatMap(a -> new Done<>(f.apply(a)));
    }

    private static final class Done<F, A> extends Free<F, A> {
        private final A a;

        private Done(final A a) {
            this.a = a;
        }

        @Override
        public <B> Free<F, B> flatMap(Function<A, Free<F, B>> f) {
            return new Gosub<>(this, f);
        }
    }

    private static final class Suspend<F, A> extends Free<F, A> {
        private final _<F, A> a;

        private Suspend(final _<F, A> a) {
            this.a = a;
        }

        @Override
        public <B> Free<F, B> flatMap(Function<A, Free<F, B>> f) {
            return new Gosub<>(this, f);
        }
    }

    private static final class Gosub<F, A, B> extends Free<F, B> {
        private final Free<F, A> a;
        private final Function<A, Free<F, B>> f;

        private Gosub(final Free<F, A> a, final Function<A, Free<F, B>> f) {
            this.a = a;
            this.f = f;
        }

        @Override
        public <C> Free<F, C> flatMap(final Function<B, Free<F, C>> g) {
            return new Gosub<>(a, aa -> new Gosub<>(f.apply(aa), g));
        }
    }

    public static <A> Free<F0.μ, A> rollF0(final F0<Free<F0.μ, A>> f) {
        return Free.liftF(f).flatMap(a -> a);
    }

    public static <A> A runF0(final Free<F0.μ, A> f) {
        return f.go(a -> ((F0<Free<F0.μ, A>>)a).apply(), F0.functor);
    }
}
