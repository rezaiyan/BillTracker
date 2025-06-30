# PostgreSQL Setup Guide

## Current Status
The connection check has confirmed that PostgreSQL is either not installed or not running on your system. The error message was:
```
Failed to connect to PostgreSQL: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
```

## Installing PostgreSQL

### On macOS
1. Using Homebrew:
   ```
   brew install postgresql
   ```
2. Start PostgreSQL service:
   ```
   brew services start postgresql
   ```

### On Windows
1. Download the installer from [PostgreSQL official website](https://www.postgresql.org/download/windows/)
2. Run the installer and follow the instructions
3. Make sure to remember the password you set for the 'postgres' user
4. The installer will also install pgAdmin, a GUI tool for managing PostgreSQL

### On Linux (Ubuntu/Debian)
1. Install PostgreSQL:
   ```
   sudo apt update
   sudo apt install postgresql postgresql-contrib
   ```
2. Start PostgreSQL service:
   ```
   sudo systemctl start postgresql
   sudo systemctl enable postgresql
   ```

## Setting Up the Database

After installing PostgreSQL, you need to create a database for the application:

1. Connect to PostgreSQL as the postgres user:
   ```
   # On macOS/Linux
   sudo -u postgres psql
   
   # On Windows (using Command Prompt as Administrator)
   psql -U postgres
   ```

2. Create the database:
   ```sql
   CREATE DATABASE subscriptiondb;
   ```

3. (Optional) Create a dedicated user for the application:
   ```sql
   CREATE USER subscriptionuser WITH ENCRYPTED PASSWORD 'yourpassword';
   GRANT ALL PRIVILEGES ON DATABASE subscriptiondb TO subscriptionuser;
   ```

4. Exit PostgreSQL:
   ```
   \q
   ```

## Updating Application Configuration

If you created a dedicated user, update the `application.properties` file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/subscriptiondb
spring.datasource.username=subscriptionuser
spring.datasource.password=yourpassword
```

## Using the Development Profile

Until PostgreSQL is properly set up, you can use the H2 in-memory database for development:

```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

This will use the configuration in `application-dev.properties` which is already set up to use H2.

## Verifying the Connection

After setting up PostgreSQL, run the connection checker again:

```
./gradlew checkPostgreSQL
```

If everything is set up correctly, you should see:
```
Successfully connected to PostgreSQL. Version: [PostgreSQL version information]
```

## Troubleshooting

1. **PostgreSQL service not running**
   - Check the status of the PostgreSQL service:
     ```
     # On macOS
     brew services list
     
     # On Linux
     sudo systemctl status postgresql
     
     # On Windows
     services.msc (look for PostgreSQL service)
     ```

2. **Firewall blocking connections**
   - Make sure your firewall allows connections to port 5432

3. **PostgreSQL not accepting remote connections**
   - Edit `postgresql.conf` to include:
     ```
     listen_addresses = '*'
     ```
   - Edit `pg_hba.conf` to allow connections from your application

4. **Wrong credentials**
   - Double-check the username and password in `application.properties`

5. **Database doesn't exist**
   - Make sure you've created the `subscriptiondb` database