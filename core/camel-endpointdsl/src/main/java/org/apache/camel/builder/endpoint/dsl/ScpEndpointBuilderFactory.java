begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|EndpointConsumerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|EndpointProducerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * To copy files using the secure copy protocol (SCP).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ScpEndpointBuilderFactory
specifier|public
interface|interface
name|ScpEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the SCP component.      */
DECL|interface|ScpEndpointBuilder
specifier|public
interface|interface
name|ScpEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedScpEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedScpEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Hostname of the FTP server.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|host (String host)
specifier|default
name|ScpEndpointBuilder
name|host
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"host"
argument_list|,
name|host
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Port of the FTP server.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|port (int port)
specifier|default
name|ScpEndpointBuilder
name|port
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Port of the FTP server.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|port (String port)
specifier|default
name|ScpEndpointBuilder
name|port
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The starting directory.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|directoryName (String directoryName)
specifier|default
name|ScpEndpointBuilder
name|directoryName
parameter_list|(
name|String
name|directoryName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"directoryName"
argument_list|,
name|directoryName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not to disconnect from remote FTP server right after use.          * Disconnect will only disconnect the current connection to the FTP          * server. If you have a consumer which you want to stop, then you need          * to stop the consumer/route instead.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|disconnect (boolean disconnect)
specifier|default
name|ScpEndpointBuilder
name|disconnect
parameter_list|(
name|boolean
name|disconnect
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"disconnect"
argument_list|,
name|disconnect
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not to disconnect from remote FTP server right after use.          * Disconnect will only disconnect the current connection to the FTP          * server. If you have a consumer which you want to stop, then you need          * to stop the consumer/route instead.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|disconnect (String disconnect)
specifier|default
name|ScpEndpointBuilder
name|disconnect
parameter_list|(
name|String
name|disconnect
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"disconnect"
argument_list|,
name|disconnect
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to set chmod on the stored file. For example chmod=664.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|chmod (String chmod)
specifier|default
name|ScpEndpointBuilder
name|chmod
parameter_list|(
name|String
name|chmod
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"chmod"
argument_list|,
name|chmod
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Use Expression such as File Language to dynamically set the filename.          * For consumers, it's used as a filename filter. For producers, it's          * used to evaluate the filename to write. If an expression is set, it          * take precedence over the CamelFileName header. (Note: The header          * itself can also be an Expression). The expression options support          * both String and Expression types. If the expression is a String type,          * it is always evaluated using the File Language. If the expression is          * an Expression type, the specified Expression type is used - this          * allows you, for instance, to use OGNL expressions. For the consumer,          * you can use it to filter filenames, so you can for instance consume          * today's file using the File Language syntax:          * mydata-${date:now:yyyyMMdd}.txt. The producers support the          * CamelOverruleFileName header which takes precedence over any existing          * CamelFileName header; the CamelOverruleFileName is a header that is          * used only once, and makes it easier as this avoids to temporary store          * CamelFileName and have to restore it afterwards.          *           * The option is a:<code>org.apache.camel.Expression</code> type.          *           * Group: producer          */
DECL|method|fileName (Expression fileName)
specifier|default
name|ScpEndpointBuilder
name|fileName
parameter_list|(
name|Expression
name|fileName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"fileName"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Use Expression such as File Language to dynamically set the filename.          * For consumers, it's used as a filename filter. For producers, it's          * used to evaluate the filename to write. If an expression is set, it          * take precedence over the CamelFileName header. (Note: The header          * itself can also be an Expression). The expression options support          * both String and Expression types. If the expression is a String type,          * it is always evaluated using the File Language. If the expression is          * an Expression type, the specified Expression type is used - this          * allows you, for instance, to use OGNL expressions. For the consumer,          * you can use it to filter filenames, so you can for instance consume          * today's file using the File Language syntax:          * mydata-${date:now:yyyyMMdd}.txt. The producers support the          * CamelOverruleFileName header which takes precedence over any existing          * CamelFileName header; the CamelOverruleFileName is a header that is          * used only once, and makes it easier as this avoids to temporary store          * CamelFileName and have to restore it afterwards.          *           * The option will be converted to a          *<code>org.apache.camel.Expression</code> type.          *           * Group: producer          */
DECL|method|fileName (String fileName)
specifier|default
name|ScpEndpointBuilder
name|fileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"fileName"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use strict host key checking. Possible values are:          * no, yes.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|strictHostKeyChecking ( String strictHostKeyChecking)
specifier|default
name|ScpEndpointBuilder
name|strictHostKeyChecking
parameter_list|(
name|String
name|strictHostKeyChecking
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"strictHostKeyChecking"
argument_list|,
name|strictHostKeyChecking
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the known_hosts file, so that the jsch endpoint can do host key          * verification. You can prefix with classpath: to load the file from          * classpath instead of file system.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|knownHostsFile (String knownHostsFile)
specifier|default
name|ScpEndpointBuilder
name|knownHostsFile
parameter_list|(
name|String
name|knownHostsFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"knownHostsFile"
argument_list|,
name|knownHostsFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Password to use for login.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|password (String password)
specifier|default
name|ScpEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set a comma separated list of authentications that will be used in          * order of preference. Possible authentication methods are defined by          * JCraft JSCH. Some examples include:          * gssapi-with-mic,publickey,keyboard-interactive,password If not          * specified the JSCH and/or system defaults will be used.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|preferredAuthentications ( String preferredAuthentications)
specifier|default
name|ScpEndpointBuilder
name|preferredAuthentications
parameter_list|(
name|String
name|preferredAuthentications
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"preferredAuthentications"
argument_list|,
name|preferredAuthentications
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the private key bytes to that the endpoint can do private key          * verification. This must be used only if privateKeyFile wasn't set.          * Otherwise the file will have the priority.          *           * The option is a:<code>byte[]</code> type.          *           * Group: security          */
DECL|method|privateKeyBytes (Byte[] privateKeyBytes)
specifier|default
name|ScpEndpointBuilder
name|privateKeyBytes
parameter_list|(
name|Byte
index|[]
name|privateKeyBytes
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"privateKeyBytes"
argument_list|,
name|privateKeyBytes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the private key bytes to that the endpoint can do private key          * verification. This must be used only if privateKeyFile wasn't set.          * Otherwise the file will have the priority.          *           * The option will be converted to a<code>byte[]</code> type.          *           * Group: security          */
DECL|method|privateKeyBytes (String privateKeyBytes)
specifier|default
name|ScpEndpointBuilder
name|privateKeyBytes
parameter_list|(
name|String
name|privateKeyBytes
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"privateKeyBytes"
argument_list|,
name|privateKeyBytes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the private key file to that the endpoint can do private key          * verification. You can prefix with classpath: to load the file from          * classpath instead of file system.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|privateKeyFile (String privateKeyFile)
specifier|default
name|ScpEndpointBuilder
name|privateKeyFile
parameter_list|(
name|String
name|privateKeyFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"privateKeyFile"
argument_list|,
name|privateKeyFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the private key file passphrase to that the endpoint can do          * private key verification.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|privateKeyFilePassphrase ( String privateKeyFilePassphrase)
specifier|default
name|ScpEndpointBuilder
name|privateKeyFilePassphrase
parameter_list|(
name|String
name|privateKeyFilePassphrase
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"privateKeyFilePassphrase"
argument_list|,
name|privateKeyFilePassphrase
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Username to use for login.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|username (String username)
specifier|default
name|ScpEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If knownHostFile has not been explicit configured, then use the host          * file from System.getProperty(user.home) /.ssh/known_hosts.          *           * The option is a:<code>boolean</code> type.          *           * Group: security          */
DECL|method|useUserKnownHostsFile ( boolean useUserKnownHostsFile)
specifier|default
name|ScpEndpointBuilder
name|useUserKnownHostsFile
parameter_list|(
name|boolean
name|useUserKnownHostsFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"useUserKnownHostsFile"
argument_list|,
name|useUserKnownHostsFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If knownHostFile has not been explicit configured, then use the host          * file from System.getProperty(user.home) /.ssh/known_hosts.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: security          */
DECL|method|useUserKnownHostsFile ( String useUserKnownHostsFile)
specifier|default
name|ScpEndpointBuilder
name|useUserKnownHostsFile
parameter_list|(
name|String
name|useUserKnownHostsFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"useUserKnownHostsFile"
argument_list|,
name|useUserKnownHostsFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the SCP component.      */
DECL|interface|AdvancedScpEndpointBuilder
specifier|public
interface|interface
name|AdvancedScpEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ScpEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ScpEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedScpEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedScpEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the connect timeout for waiting for a connection to be          * established Used by both FTPClient and JSCH.          *           * The option is a:<code>int</code> type.          *           * Group: advanced          */
DECL|method|connectTimeout (int connectTimeout)
specifier|default
name|AdvancedScpEndpointBuilder
name|connectTimeout
parameter_list|(
name|int
name|connectTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"connectTimeout"
argument_list|,
name|connectTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the connect timeout for waiting for a connection to be          * established Used by both FTPClient and JSCH.          *           * The option will be converted to a<code>int</code> type.          *           * Group: advanced          */
DECL|method|connectTimeout (String connectTimeout)
specifier|default
name|AdvancedScpEndpointBuilder
name|connectTimeout
parameter_list|(
name|String
name|connectTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"connectTimeout"
argument_list|,
name|connectTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the so timeout FTP and FTPS Only for Camel 2.4. SFTP for Camel          * 2.14.3/2.15.3/2.16 onwards. Is the SocketOptions.SO_TIMEOUT value in          * millis. Recommended option is to set this to 300000 so as not have a          * hanged connection. On SFTP this option is set as timeout on the JSCH          * Session instance.          *           * The option is a:<code>int</code> type.          *           * Group: advanced          */
DECL|method|soTimeout (int soTimeout)
specifier|default
name|AdvancedScpEndpointBuilder
name|soTimeout
parameter_list|(
name|int
name|soTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"soTimeout"
argument_list|,
name|soTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the so timeout FTP and FTPS Only for Camel 2.4. SFTP for Camel          * 2.14.3/2.15.3/2.16 onwards. Is the SocketOptions.SO_TIMEOUT value in          * millis. Recommended option is to set this to 300000 so as not have a          * hanged connection. On SFTP this option is set as timeout on the JSCH          * Session instance.          *           * The option will be converted to a<code>int</code> type.          *           * Group: advanced          */
DECL|method|soTimeout (String soTimeout)
specifier|default
name|AdvancedScpEndpointBuilder
name|soTimeout
parameter_list|(
name|String
name|soTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"soTimeout"
argument_list|,
name|soTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedScpEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedScpEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the data timeout for waiting for reply Used only by FTPClient.          *           * The option is a:<code>int</code> type.          *           * Group: advanced          */
DECL|method|timeout (int timeout)
specifier|default
name|AdvancedScpEndpointBuilder
name|timeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the data timeout for waiting for reply Used only by FTPClient.          *           * The option will be converted to a<code>int</code> type.          *           * Group: advanced          */
DECL|method|timeout (String timeout)
specifier|default
name|AdvancedScpEndpointBuilder
name|timeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set a comma separated list of ciphers that will be used in order of          * preference. Possible cipher names are defined by JCraft JSCH. Some          * examples include:          * aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-cbc,aes256-cbc. If not specified the default list from JSCH will be used.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security (advanced)          */
DECL|method|ciphers (String ciphers)
specifier|default
name|AdvancedScpEndpointBuilder
name|ciphers
parameter_list|(
name|String
name|ciphers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ciphers"
argument_list|,
name|ciphers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * SCP (camel-jsch)      * To copy files using the secure copy protocol (SCP).      *       * Syntax:<code>scp:host:port/directoryName</code>      * Category: file      * Available as of version: 2.10      * Maven coordinates: org.apache.camel:camel-jsch      */
DECL|method|scp (String path)
specifier|default
name|ScpEndpointBuilder
name|scp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ScpEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ScpEndpointBuilder
implements|,
name|AdvancedScpEndpointBuilder
block|{
specifier|public
name|ScpEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"scp"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ScpEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

