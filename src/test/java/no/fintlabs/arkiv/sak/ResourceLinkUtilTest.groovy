package no.fintlabs.arkiv.sak

import no.fint.model.resource.Link
import no.fintlabs.arkiv.ResourceLinkUtil
import no.fintlabs.exceptions.NoSuchLinkException
import spock.lang.Specification

import static java.util.Arrays.asList
import static java.util.Collections.emptyList

class ResourceLinkUtilTest extends Specification {

    def 'should get link if it exists'() {
        expect:
        ResourceLinkUtil.getFirstLink(
                () -> asList(new Link("testHref")),
                "TestResource",
                "testKey"
        ) == "testHref"
    }

    def 'should throw exception if link does not exist'() {
        when:
        ResourceLinkUtil.getFirstLink(
                () -> emptyList(),
                "TestResource",
                "testKey"
        )
        then:
        thrown NoSuchLinkException
    }
}
