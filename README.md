**Demo Publish Android Library To Maven Central Step By Step**

1. Create account or login Sonatype Jira
   at: [https://issues.sonatype.org/](https://issues.sonatype.org/)
    - Create new project ticket
      at: [https://issues.sonatype.org/secure/CreateIssue.jspa?issuetype=21&pid=10134](https://issues.sonatype.org/secure/CreateIssue.jspa?issuetype=21&pid=10134)
    - Fill summary, group id, project url, scm url and create
    - E.g: Summary: Create repository for vn.alphalabs.worldtimeapi
      Group Id: vn.alphalabs
      Project URL: https://github.com/whiskyblack/world-time-api
      SCM url: https://github.com/whiskyblack/world-time-api.git
    - About 2-3 minutes, you’ll receive an automatic comment on your ticket saying:
      `To register this Group Id you must prove ownership of the domain alphalabs.vn. Please
      complete the following steps to continue:

      *Add a DNS TXT record to your
      domain* with the text: {{OSSRH-96022}}. Please read https://central.sonatype.org/faq/how-to-set-txt-record/
      
      *Edit this ticket* and set Status to Open.`
2. DNS Referencing
   - Go to DNS Manager
   - Add new record below:
     Type: TXT | Host: @ | TXT Value: [your ticket reference in Sonatype Jira] | TTL: [1 Hour or Auto]
   - E.g: Type: TXT | Host: @ | TXT Value: OSSRH-96022 | TTL: Auto (Config on cloudflare)
   - Test: host -t txt alphalabs.vn
3. Create account or login Sonatype OSS Index at: [https://ossindex.sonatype.org/](https://ossindex.sonatype.org/)
4. After login Sonatype OSS Index success, login to Nexus UI at: [https://s01.oss.sonatype.org/](https://s01.oss.sonatype.org/index.html#staging-upload)
5. You can upload aar at [https://s01.oss.sonatype.org/index.html#staging-upload](https://s01.oss.sonatype.org/index.html#staging-upload) if have pom file and signature library
6. If not, config library like this demo
7. Generating a GPG Key for 
   - Download GPG command line tools from https://www.gnupg.org/download/ and install them.
   - Open your Terminal.
   - Type gpg --full-generate-key
   - When asked what kind of key you want, press Enter to select the default RSA and RSA.
   - When asked about the keysize, type 4096.
   - When asked about the duration of key to be valid, press Enter to select the default, indicating that the key doesn’t expire.
   - When asked about the information for creating a new user ID, provide your name and email address.
   - Enter ‘O’ for Okay.
   - When prompted, create a passphrase to protect your key.
   - This creates your key in ~/.gnupg/openpgp-revocs.d/ with .rev format. Since we need our key in .gpg format, here’s how to create that:
     + In your Terminal, type gpg --export-secret-keys -o secring.gpg .
     + When prompted with the passphrase, type the passphrase you set for the key you created.
   This should create a file named secring.gpg in your root directory.