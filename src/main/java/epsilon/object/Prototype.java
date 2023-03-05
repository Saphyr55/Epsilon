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
        for (int i = 0; i < types.size(); i++) {
            if (!types.get(i).equals(this.types.get(i).name()))
                return false;
        }
        return true;
    }

    public boolean sameArgsT(List<Type> types) {
        for (int i = 0; i < types.size(); i++) {
            if (!types.get(i).name().equals(this.types.get(i).name()))
                return false;
        }
        return true;
    }

}
