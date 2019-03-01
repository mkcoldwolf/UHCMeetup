package mitw.meetup.util;

public class CStringBuffer {

    private String string;

    public CStringBuffer(String string) {
        this.string = string;
    }

    public CStringBuffer() {
        this.string = "";
    }

    public CStringBuffer replaceAll(String target, String replacement) {
        this.string = StringUtil.replace(string, target, replacement);
        return this;
    }

    @Override
    public String toString() {
        return string;
    }
}
