package uk.gov.dwp.personal.details.server.migration;

import org.junit.Test;
import uk.gov.dwp.personal.details.server.migration.NameSplitter.Names;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class NameSplitterTest {

    private final NameSplitter underTest = new NameSplitter();

    @Test
    public void attemptToSplitSingleName() throws Exception {
        assertThat(underTest.split("Bob"), is(equalTo(new Names("Bob", null))));
    }

    @Test
    public void attemptToSplitNameWithSingleSpace() {
        assertThat(underTest.split("Bob Builder"), is(equalTo(new Names("Bob", "Builder"))));
    }

    @Test
    public void attemptToSplitNameWithMoreThanOneSpace() throws Exception {
        assertThat(underTest.split("Bob The Builder"), is(equalTo(new Names("Bob", "The Builder"))));
    }
}