# How to use the Discord plugin

Send messages and execution summaries to Discord channels via incoming webhooks.

## Authentication

Set `url` to a Discord incoming webhook URL. Create one in Discord under channel Settings → Integrations → Webhooks. Store it in a [secret](https://kestra.io/docs/concepts/secret).

## Tasks

`DiscordIncomingWebhook` sends a message as a step within a flow — set `payload` to a JSON body in Discord's [webhook message format](https://discord.com/developers/docs/resources/webhook#execute-webhook).

`DiscordExecution` sends a structured execution summary including status, duration, and an execution link, and is designed for use with a [Flow trigger](https://kestra.io/docs/workflow-components/triggers) in a dedicated monitoring namespace that watches other namespaces for failures.
