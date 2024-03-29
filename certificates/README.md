# Certificates

This directory contains scripts to create new certificates that is required by the solution.
The directory also contains the created certificates.

## Script

The script `create-certificate.sh` will create a new certificate with the name `tes-enterprise-labs.p12`.

It uses the following variables to set the information for the certificate:

| **Variable**                   | **Value**             | **Description**                                                                                                                          |
|--------------------------------|-----------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| ALIAS                          | `tes-enterprise-labs` | The alias for the key pair                                                                                                               |
| KEY_ALGORITHM                  | `RSA`                 | Specifies the key algorithm (RSA)                                                                                                        |
| KEY_SIZE_IN_BITS               | `2048`                | Specifies the key size (2048 bits)                                                                                                       |
| KEYSTORE_TYPE                  | `PKCS12`              | Specifies the type of the keystore                                                                                                       |
| KEYSTORE_FILE                  | `$ALIAS.p12`          | Key store file name.                                                                                                                     |
| KEYSTORE_PASSWORD              | -                     | Password for the key store                                                                                                               |
| CERTIFICATE_VALIDITY_IN_DAYS   | `365`                 | Specifies the validity period of the certificate in days.                                                                                |
| CERTIFICATE_DISTINGUISHED_NAME | -                     | Specifies the distinguished name for the certificate.                                                                                    |
| SUBJECT_ALTERNATIVE_NAME       | -                     | Specifies the Subject Alternative Name (SAN) extension for the certificate, allowing it to be used for both `localhost` and `127.0.0.1`. |

`CERTIFICATE_DISTINGUISHED_NAME` is generated from these variables:

| **Variable** | **Description**                       |
|--------------|---------------------------------------|
| CN           | Name of the holder of the certificate |
| ORGANISATION | Organisation                          |
| COMPANY      | Company name                          |
| CITY         | City name                             |
| STATE        | State or province                     |
| COUNTRY      | Country code                          |

### Execution

The script can be executed with standard `bash`:

```bash
./create-certificate.sh
```

## Passwords

Passwords are created with:

```bash
openssl rand -base64 48
```
