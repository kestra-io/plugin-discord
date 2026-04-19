# Kestra Discord Plugin

## What

- Provides plugin components under `io.kestra.plugin.discord`.
- Includes classes such as `DiscordIncomingWebhook`, `DiscordExecution`, `DiscordTemplate`.

## Why

- What user problem does this solve? Teams need to send notifications to Discord via webhooks from orchestrated workflows instead of relying on manual console work, ad hoc scripts, or disconnected schedulers.
- Why would a team adopt this plugin in a workflow? It keeps Discord steps in the same Kestra flow as upstream preparation, approvals, retries, notifications, and downstream systems.
- What operational/business outcome does it enable? It reduces manual handoffs and fragmented tooling while improving reliability, traceability, and delivery speed for processes that depend on Discord.

## How

### Architecture

Single-module plugin. Source packages under `io.kestra.plugin`:

- `discord`

Infrastructure dependencies (Docker Compose services):

- `app`

### Key Plugin Classes

- `io.kestra.plugin.discord.DiscordExecution`
- `io.kestra.plugin.discord.DiscordIncomingWebhook`

### Project Structure

```
plugin-discord/
├── src/main/java/io/kestra/plugin/discord/
├── src/test/java/io/kestra/plugin/discord/
├── build.gradle
└── README.md
```

## References

- https://kestra.io/docs/plugin-developer-guide
- https://kestra.io/docs/plugin-developer-guide/contribution-guidelines
