Teamplanner
===========

Teamplanner helps sports clubs with multiple teams plan their activities: who is available for a training or match, and who is in the boat. See [the website](https://martijnburger.github.io/teamplanner/) for the background and user stories.

## Project layout

| Folder | Contents |
| --- | --- |
| [`backend/`](backend/README.md) | REST API built with Quarkus (Java 21), PostgreSQL and Elasticsearch |
| [`frontend/`](frontend/README.md) | Web app built with Angular and Angular Material |
| `docs/` | The website, built with Jekyll and published with GitHub Pages |

## Running locally

Start the backend with `./mvnw quarkus:dev` in `backend/` (needs Java 21 and Docker), then the frontend with `npm install && npm start` in `frontend/`, and open http://localhost:4200/. The READMEs of both folders have the details.

## Contributing

Bug reports and feature requests are welcome as [GitHub issues](https://github.com/martijnburger/teamplanner/issues), and fixes as pull requests.
