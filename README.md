[ ![Download](null/packages/arulmani/WebKit/wvl/images/download.svg?version=1.0.4) ](https://bintray.com/arulmani/WebKit/wvl/1.0.4/link)

# WebView Logger
A WebView Logger get the logs from console without using chrome://inspect.

## Usage
For the application which is use WebView when its reach to the client inorder to find bugs and resolve fix.
WVL save the log in device it can be view as separete and as well send log in mail.

Notable usage
 1. View Logs from webview
 2. Send logs to mail

## Implementation

Add below line in Project.gradle 
  
    repositories {
       //... other repos
       maven {
           url  "https://dl.bintray.com/arulmani/WebKit" 
       }
    }

Add below line in App.gradle under Dependecy section

    implementation 'com.arul.webkit:wvl:1.0.4'
    
That's it sync the project.

# Configurations
We can configure mail and cache size of log tobe store.
Add below line in ```strings.xml``` .

    <string name="wvl_report_email">sample@mail.com</string>
    <string name="wvl_log_cache_size">20</string>

# Screen shots

When install your webview application wvl launcher icon also added into launcher screen like below spcified.




    
