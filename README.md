# cluster-management
set environment variables in setEnvironment.sh
call ./showUsage.sh to see usage


# configs
# notification configs
You have 3 options for the notification service. They are EMAIL, HIPCHAT, and CONSOLE. See the example configurations below for details. 
"notification":
{
  "EMAIL": {
    "HOST": "HOST.com",
    "PORT": 587,
    "SENDER": "SENDER@company.com",
    "SENDER_PASSWORD": "password",
    "RECIPIENTS": [
      "recipient@company.com"
    ],
    "AUTHENTICATION": "true",
    "STARTTLS": "true"
  },
  "HIPCHAT": {
    "RESTURI": "https://noetl.hipchat.com/v2/room/2398612/notification?auth_token=jfckaBNVM14j8gkiItWZBdUJceat6ODGaNnvwFjp",
    "NOTIFY": true,
    "MESSAGE_COLOR": "green"
  },
  "CONSOLE": {}
}


# monitor configs
PATH: the path you are monitoring
OPERATION: the operation you will do after you see an expected file. The options are "copy" and "delete"
EXPECTED_FILES.FREQUENT_FILES: provide an array of REGEX strings for the file names that you are expecting.
    e.g.:
EXPECTED_FILES.INFREQUENT_FILES: same as EXPECTED_FILES.FREQUENT_FILES   
BACKUP: specify the paths where you want to back up the original files. The file name will be appended with the batch id. Difference between LOCAL, S3_RAW and S3_STAGE can be found in the Desgin document

{
  "PATH": "/mnt/src/sftp_uploads",
  "OPERATION": "copy",
  "EXPECTED_FILES": {
    "FREQUENT_FILES": [
      "exp1$",
      "exp2$"
    ],
    "INFREQUENT_FILES": [
      "(hi|hello)"
    ]
  },
  "BACKUP": {
    "LOCAL": [
      "/mnt/dest/sftp_uploads"
    ],
    "S3_RAW": "s3://bkt/raw/",
    "S3_STAGE": "s3://bkt/stage/"
  }
}
