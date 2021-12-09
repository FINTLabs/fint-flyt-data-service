package no.fintlabs.arkiv.kodeverk;


import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceCache<R extends FintLinks> extends Cache<R> {

    public ResourceCache(Function<R, String> keyMapper, ObjectMapper mapper, Class<R> clazz) {
        super(r -> {
                    String systemId = keyMapper.apply(r);
                    List<String> selfLinks = r.getSelfLinks().stream().map(Link::getHref).collect(Collectors.toList());
                    return Stream.concat(Stream.of(systemId), selfLinks.stream()).collect(Collectors.toList());
                },
                mapper,
                clazz
        );
    }

}
