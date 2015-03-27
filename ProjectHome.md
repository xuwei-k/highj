ATTENTION: This project is just an experiment, it relies heavily on Java 8 features, and is **not** intended for production. A lot of bad things might happen:
  * Recursion is sometimes hard to avoid, which might lead to `StackOverflowError`s
  * The code isn't very efficient, there might be excessive object creation going on
  * Lazy behavior might lead to unexpected results, as beginners often face in Haskell
  * I tried hard to avoid casting / raw typing, but I won't put my hands into fire that I got it right everywhere

HighJ tries to hack around Java's lack of higher order type polymorphism (a.k.a. "higher kinded types" in Scala land). It translates several well known type-classes from Haskell (including `Applicative`, `Monad` and `Foldable`). However, the amount of generic declarations and Java's lack of first class function make sometimes even simple tasks overly verbose, so expect angle brackets, many, many angle brackets.

As Java's collection classes are not very useful in a functional setting, highJ uses their own versions (similar to Haskell's and [functionaljava](http://functionaljava.org)'s collections). The design of the type-class hierarchy follows roughly Edward Kmett's excellent [semigroupoids package](http://hackage.haskell.org/package/semigroupoids).


So how does highJ code looks like?

```
//Example: foldl over a List

//the Foldable type-class instance for Lists
Foldable<List.µ> foldable = List.foldable;

//a list
List<String> strList = List.of("one", "two", "three");

//the call to foldl
String result = foldable.foldl(a -> b -> a + " + " + b, "zero", strList);

System.out.println(result);
//result is "zero + one + two + three"
```

I'm very interested in your feedback, and would be more than glad to hear if actually something useful grows out of this project. Have fun!

NOTE FOR FORMER USERS: As planned, I switched to Java 8 and refactored the whole codebase in the process. The changes are incompatible, but I hope the improvements compensate you for the inconvenience.