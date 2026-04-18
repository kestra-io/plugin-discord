# Kestra Discord Plugin

## What

- Provides plugin components under `io.kestra.plugin.discord`.
- Includes classes such as `DiscordIncomingWebhook`, `DiscordExecution`, `DiscordTemplate`.

## Why

- This plugin integrates Kestra with Discord.
- It provides tasks that send notifications to Discord via webhooks.

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
