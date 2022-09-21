package no.sikt.nva.vms.kaltura;

import java.util.Map;
import java.util.Objects;

public class KalturaIdNotFoundResponse {

    private final String code;
    private final String message;
    private final String objectType;
    private final Map<String,String> args;

    public KalturaIdNotFoundResponse(String code, String message, String objectType, Map<String,String> args) {
        this.code = code;
        this.message = message;
        this.objectType = objectType;
        this.args = args;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, objectType, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KalturaIdNotFoundResponse that = (KalturaIdNotFoundResponse) o;
        return Objects.equals(code, that.code)
               && Objects.equals(message, that.message)
               && Objects.equals(objectType, that.objectType)
               && Objects.equals(args, that.args);
    }

    @Override
    public String toString() {
        return "KalturaIdNotFoundResponse{"
               + "code='"
               + code
               + '\''
               + ", message='"
               + message
               + '\''
               + ", objectType='"
               + objectType
               + '\''
               + ", args="
               + args
               + '}';
    }
}
