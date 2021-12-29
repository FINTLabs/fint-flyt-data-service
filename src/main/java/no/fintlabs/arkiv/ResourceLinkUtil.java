package no.fintlabs.arkiv;

import no.fint.model.resource.Link;
import no.fintlabs.exceptions.NoSuchLinkException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ResourceLinkUtil {

    public static String getFirstLink(Supplier<List<Link>> linkProducer, String resourceName, String linkKey) {
        return Optional.ofNullable(linkProducer.get())
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(Link::getHref)
                .orElseThrow(() -> new NoSuchLinkException(resourceName, linkKey));
    }

}
