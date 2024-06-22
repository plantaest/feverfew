# Feverfew

Comprehensive link checker tool for Wikipedia

Project homepage: [User:Plantaest/Feverfew](https://en.wikipedia.org/wiki/User:Plantaest/Feverfew) on English Wikipedia

## Usage

Visit the homepage [feverfew.toolforge.org](https://feverfew.toolforge.org/) to use.

## Developing

### feverfew-server

Step 1: Open the project with IntelliJ IDEA

Step 2: Open Docker

Step 3: 

```
docker-compose up -d
```

Step 4: Create a `.env` file and provide values for the two keys: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`.

Step 5: Press <kbd>Ctrl+R</kbd>, the application will run on port 8001.

### feverfew-client

Step 1: Open the project with IntelliJ IDEA

Step 2:

```
pnpm i
```

Step 3:

```
pnpm dev
```

The application will run on port 8020.

### feverfew-request-links-lambda

Step 1: Open the project with IntelliJ IDEA

Step 2: Press <kbd>Ctrl+R</kbd>, the application will run on port 8010.

## Licensing

```
(c) Plantaest

License:
* GNU Affero General Public License version 3 for Feverfew application
* Apache License version 2 for Composite library
```
