package epsilonc;

import epsilonc.core.Callable;

import java.util.List;

public interface FunctionCallable extends Callable {

    default int arity() { return 0; }

    class Native implements FunctionCallable {

        private final int arity;
        private final Callable callable;

        public Native(int arity, Callable callable) {
            this.arity = arity;
            this.callable = callable;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            return callable.call(interpreter, arguments);
        }

        @Override
        public int arity() {
            return arity;
        }

        @Override
        public String toString() {
            return "native function";
        }


    }

}
