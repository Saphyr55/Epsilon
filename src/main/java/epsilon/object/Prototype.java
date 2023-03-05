package epsilon.object;

import epsilon.type.Type;

import java.util.List;

public class Prototype {

    private final String name;
    private final List<Type> types;
                                                       
    public Prototype(String name, List<Type> types) {
        this.name = name;
        this.types = types;
    }

    public Prototype(String name, Type... types) {
        this.name = name;
        this.types = List.of(types);
    }

    public String getName() {
        return name;
    }

    public List<Type> getTypes() {
        return types;
    }

    public boolean same(String name, List<String> types) {
        return sameArgs(types) && sameName(name);
    }

    public boolean sameName(String name) {
        return  name.equals(this.name);
    }

    public boolean sameArgs(List<String> types) {
        if (this.types.size() != types.size()) return false;
        for (int i = 0; i < types.size(); i++) {
            if (!types.get(i).equals(this.types.get(i).name()))
                return false;
        }
        return true;
    }

    public boolean sameArgsT(List<Type> types) {
        if (this.types.size() != types.size()) return false;
        for (int i = 0; i < types.size(); i++) {
            if (!types.get(i).name().equals(this.types.get(i).name()))
                return false;
        }
        return true;
    }

    public boolean isInstanceTypes(List<Type> types) {
        if (this.types.size() != types.size()) return false;
        for (int i = 0; i < types.size(); i++) {
            if (!this.types.get(i).isInstance(types.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Prototype{" +
                "name='" + name + '\'' +
                ", types=" + types +
                '}';
    }
}
