package io.kestra.plugin.discord;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.google.common.io.Files;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.models.flows.Flow;
import io.kestra.core.models.property.Property;
import io.kestra.core.models.validations.ModelValidator;
import io.kestra.core.runners.RunContext;
import io.kestra.core.runners.RunContextFactory;
import io.kestra.core.serializers.YamlParser;
import io.kestra.core.utils.IdUtils;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@KestraTest
public class DiscordIncomingWebhookTest {

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private RunContextFactory runContextFactory;

    @Inject
    private ModelValidator modelValidator;

    @Test
    void run() throws Exception {
        RunContext runContext = runContextFactory.of(
            Map.of(
                "title", "Discord test webhook notification",
                "username", "SomeUser",
                "content", "A message *with some bold text* and _some italicized text_.",
                "color", new int[] { 255, 255, 255 }
            )
        );

        EmbeddedServer embeddedServer = applicationContext.getBean(EmbeddedServer.class);
        embeddedServer.start();

        DiscordIncomingWebhook task = DiscordIncomingWebhook.builder()
            .url(embeddedServer.getURI() + "/webhook-unit-test")
            .payload(
                Property.ofExpression(
                    Files.asCharSource(
                        new File(
                            Objects.requireNonNull(
                                DiscordIncomingWebhookTest.class.getClassLoader()
                                    .getResource("discord.peb")
                            )
                                .toURI()
                        ),
                        StandardCharsets.UTF_8
                    ).read()
                )
            )
            .build();

        task.run(runContext);

        assertThat(FakeWebhookController.data, containsString("ge *with some bold text* an"));
    }

    @Test
    void embedAuthorWithoutOptionalUrls() throws Exception {
        RunContext runContext = runContextFactory.of(Map.of());

        DiscordTemplate.Embed embed = DiscordTemplate.Embed.builder()
            .authorName(Property.ofValue("Test Author"))
            .build();

        Map<String, Object> embedMap = embed.getEmbedMap(runContext, null);

        @SuppressWarnings("unchecked")
        Map<String, Object> author = (Map<String, Object>) embedMap.get("author");

        assertThat(author.get("name"), is("Test Author"));
        assertThat(author.containsKey("url"), is(false));
        assertThat(author.containsKey("icon_url"), is(false));
    }

    @SuppressWarnings("deprecation")
    @Test
    void flowSettingDeprecatedOptionsStillValidatesAndRuns() throws Exception {
        var embeddedServer = applicationContext.getBean(EmbeddedServer.class);
        embeddedServer.start();

        var message = "deprecated-options-" + IdUtils.create();
        var flow = YamlParser.parse(
            """
                id: discord_deprecated_options
                namespace: io.kestra.tests
                tasks:
                  - id: webhook
                    type: io.kestra.plugin.discord.DiscordIncomingWebhook
                    url: "%s/webhook-unit-test"
                    payload: '{"content": "%s"}'
                    options:
                      readTimeout: PT30S
                      connectionPoolIdleTimeout: PT10S
                      maxContentLength: 1024
                """.formatted(embeddedServer.getURI(), message),
            Flow.class
        );

        assertThat(modelValidator.isValid(flow).isEmpty(), is(true));

        var runContext = runContextFactory.of(Map.of());
        var task = (DiscordIncomingWebhook) flow.getTasks().getFirst();
        var options = task.getOptions();
        assertThat(runContext.render(options.getReadTimeout()).as(Duration.class).orElseThrow(), is(Duration.ofSeconds(30)));
        assertThat(runContext.render(options.getConnectionPoolIdleTimeout()).as(Duration.class).orElseThrow(), is(Duration.ofSeconds(10)));
        assertThat(runContext.render(options.getMaxContentLength()).as(Integer.class).orElseThrow(), is(1024));

        task.run(runContext);

        assertThat(FakeWebhookController.data, containsString(message));
    }
}
