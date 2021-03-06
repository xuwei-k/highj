/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.highj.data.stateful.effect1;

import java.util.function.Function;
import org.highj._;
import org.highj.data.stateful.Effect1;
import org.highj.typeclass1.contravariant.Contravariant;

/**
 *
 * @author clintonselke
 */
public interface Effect1Contravariant extends Contravariant<Effect1.µ> {

    @Override
    public default <A, B> Effect1<A> contramap(Function<A, B> fn, _<Effect1.µ, B> nestedB) {
        return (A a) -> Effect1.narrow(nestedB).run(fn.apply(a));
    }
}
