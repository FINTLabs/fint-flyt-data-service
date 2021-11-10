package no.fintlabs.kafka;

public class FintTopicUtilities {

    public static <T> String getTopicNameFromFintClassResource(Class<T> clazz) throws IllegalArgumentException {
        String canonicalName = clazz.getCanonicalName();

        if (canonicalName.contains("no.fint.model")) {
            return canonicalName.replace("no.fint.model.", "").toLowerCase();
        }

        throw new IllegalArgumentException(canonicalName + " is not a FINT class");
    }
}
