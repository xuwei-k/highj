package org.highj.data.transformer.state;

import org.highj._;
import org.highj.__;
import org.highj.___;
import org.highj.data.transformer.StateT;
import org.highj.data.tuple.T2;
import org.highj.typeclass1.functor.Functor;

import java.util.function.Function;

/**
 * @author Clinton Selke
 */
public interface StateTFunctor<S, M> extends Functor<__.µ<___.µ<StateT.µ, S>, M>> {

    public Functor<M> m();

    @Override
    public default <A, B> StateT<S, M, B> map(Function<A, B> fn, _<__.µ<___.µ<StateT.µ, S>, M>, A> nestedA) {
        return (S s) -> m().map(
                (T2<A, S> x) -> T2.of(fn.apply(x._1()), x._2()),
                StateT.narrow(nestedA).run(s)
        );
    }
}