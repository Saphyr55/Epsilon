package epsilon;

public class Module {

    private String path;
    private String name;
    private boolean loaded;

    public Module(String id, String path) {
        this.name = id;
        this.path = path;
        this.loaded = false;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
