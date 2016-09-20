begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
operator|.
name|SparkBinding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The spark-rest component is used for hosting REST services which has been  * defined using Camel rest-dsl.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.spark-rest"
argument_list|)
DECL|class|SparkComponentConfiguration
specifier|public
class|class
name|SparkComponentConfiguration
block|{
comment|/**      * Port number. Will by default use 4567      */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**      * Set the IP address that Spark should listen on. If not called the default      * address is '0.0.0.0'.      */
DECL|field|ipAddress
specifier|private
name|String
name|ipAddress
decl_stmt|;
comment|/**      * Minimum number of threads in Spark thread-pool (shared globally)      */
DECL|field|minThreads
specifier|private
name|Integer
name|minThreads
decl_stmt|;
comment|/**      * Maximum number of threads in Spark thread-pool (shared globally)      */
DECL|field|maxThreads
specifier|private
name|Integer
name|maxThreads
decl_stmt|;
comment|/**      * Thread idle timeout in millis where threads that has been idle for a      * longer period will be terminated from the thread pool      */
DECL|field|timeOutMillis
specifier|private
name|Integer
name|timeOutMillis
decl_stmt|;
comment|/**      * Configures connection to be secure to use the keystore file      */
DECL|field|keystoreFile
specifier|private
name|String
name|keystoreFile
decl_stmt|;
comment|/**      * Configures connection to be secure to use the keystore password      */
DECL|field|keystorePassword
specifier|private
name|String
name|keystorePassword
decl_stmt|;
comment|/**      * Configures connection to be secure to use the truststore file      */
DECL|field|truststoreFile
specifier|private
name|String
name|truststoreFile
decl_stmt|;
comment|/**      * Configures connection to be secure to use the truststore password      */
DECL|field|truststorePassword
specifier|private
name|String
name|truststorePassword
decl_stmt|;
comment|/**      * To use the shared SparkConfiguration      */
DECL|field|sparkConfiguration
specifier|private
name|SparkConfigurationNestedConfiguration
name|sparkConfiguration
decl_stmt|;
comment|/**      * To use a custom SparkBinding to map to/from Camel message.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|sparkBinding
specifier|private
name|SparkBinding
name|sparkBinding
decl_stmt|;
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getIpAddress ()
specifier|public
name|String
name|getIpAddress
parameter_list|()
block|{
return|return
name|ipAddress
return|;
block|}
DECL|method|setIpAddress (String ipAddress)
specifier|public
name|void
name|setIpAddress
parameter_list|(
name|String
name|ipAddress
parameter_list|)
block|{
name|this
operator|.
name|ipAddress
operator|=
name|ipAddress
expr_stmt|;
block|}
DECL|method|getMinThreads ()
specifier|public
name|Integer
name|getMinThreads
parameter_list|()
block|{
return|return
name|minThreads
return|;
block|}
DECL|method|setMinThreads (Integer minThreads)
specifier|public
name|void
name|setMinThreads
parameter_list|(
name|Integer
name|minThreads
parameter_list|)
block|{
name|this
operator|.
name|minThreads
operator|=
name|minThreads
expr_stmt|;
block|}
DECL|method|getMaxThreads ()
specifier|public
name|Integer
name|getMaxThreads
parameter_list|()
block|{
return|return
name|maxThreads
return|;
block|}
DECL|method|setMaxThreads (Integer maxThreads)
specifier|public
name|void
name|setMaxThreads
parameter_list|(
name|Integer
name|maxThreads
parameter_list|)
block|{
name|this
operator|.
name|maxThreads
operator|=
name|maxThreads
expr_stmt|;
block|}
DECL|method|getTimeOutMillis ()
specifier|public
name|Integer
name|getTimeOutMillis
parameter_list|()
block|{
return|return
name|timeOutMillis
return|;
block|}
DECL|method|setTimeOutMillis (Integer timeOutMillis)
specifier|public
name|void
name|setTimeOutMillis
parameter_list|(
name|Integer
name|timeOutMillis
parameter_list|)
block|{
name|this
operator|.
name|timeOutMillis
operator|=
name|timeOutMillis
expr_stmt|;
block|}
DECL|method|getKeystoreFile ()
specifier|public
name|String
name|getKeystoreFile
parameter_list|()
block|{
return|return
name|keystoreFile
return|;
block|}
DECL|method|setKeystoreFile (String keystoreFile)
specifier|public
name|void
name|setKeystoreFile
parameter_list|(
name|String
name|keystoreFile
parameter_list|)
block|{
name|this
operator|.
name|keystoreFile
operator|=
name|keystoreFile
expr_stmt|;
block|}
DECL|method|getKeystorePassword ()
specifier|public
name|String
name|getKeystorePassword
parameter_list|()
block|{
return|return
name|keystorePassword
return|;
block|}
DECL|method|setKeystorePassword (String keystorePassword)
specifier|public
name|void
name|setKeystorePassword
parameter_list|(
name|String
name|keystorePassword
parameter_list|)
block|{
name|this
operator|.
name|keystorePassword
operator|=
name|keystorePassword
expr_stmt|;
block|}
DECL|method|getTruststoreFile ()
specifier|public
name|String
name|getTruststoreFile
parameter_list|()
block|{
return|return
name|truststoreFile
return|;
block|}
DECL|method|setTruststoreFile (String truststoreFile)
specifier|public
name|void
name|setTruststoreFile
parameter_list|(
name|String
name|truststoreFile
parameter_list|)
block|{
name|this
operator|.
name|truststoreFile
operator|=
name|truststoreFile
expr_stmt|;
block|}
DECL|method|getTruststorePassword ()
specifier|public
name|String
name|getTruststorePassword
parameter_list|()
block|{
return|return
name|truststorePassword
return|;
block|}
DECL|method|setTruststorePassword (String truststorePassword)
specifier|public
name|void
name|setTruststorePassword
parameter_list|(
name|String
name|truststorePassword
parameter_list|)
block|{
name|this
operator|.
name|truststorePassword
operator|=
name|truststorePassword
expr_stmt|;
block|}
DECL|method|getSparkConfiguration ()
specifier|public
name|SparkConfigurationNestedConfiguration
name|getSparkConfiguration
parameter_list|()
block|{
return|return
name|sparkConfiguration
return|;
block|}
DECL|method|setSparkConfiguration ( SparkConfigurationNestedConfiguration sparkConfiguration)
specifier|public
name|void
name|setSparkConfiguration
parameter_list|(
name|SparkConfigurationNestedConfiguration
name|sparkConfiguration
parameter_list|)
block|{
name|this
operator|.
name|sparkConfiguration
operator|=
name|sparkConfiguration
expr_stmt|;
block|}
DECL|method|getSparkBinding ()
specifier|public
name|SparkBinding
name|getSparkBinding
parameter_list|()
block|{
return|return
name|sparkBinding
return|;
block|}
DECL|method|setSparkBinding (SparkBinding sparkBinding)
specifier|public
name|void
name|setSparkBinding
parameter_list|(
name|SparkBinding
name|sparkBinding
parameter_list|)
block|{
name|this
operator|.
name|sparkBinding
operator|=
name|sparkBinding
expr_stmt|;
block|}
DECL|class|SparkConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|SparkConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
operator|.
name|SparkConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * If this option is enabled, then during binding from Spark to Camel          * Message then the headers will be mapped as well (eg added as header          * to the Camel Message as well). You can turn off this option to          * disable this. The headers can still be accessed from the          * org.apache.camel.component.sparkrest.SparkMessage message with the          * method getRequest() that returns the Spark HTTP request instance.          */
DECL|field|mapHeaders
specifier|private
name|Boolean
name|mapHeaders
decl_stmt|;
comment|/**          * Determines whether or not the raw input stream from Spark          * HttpRequest#getContent() is cached or not (Camel will read the stream          * into a in light-weight memory based Stream caching) cache. By default          * Camel will cache the Netty input stream to support reading it          * multiple times to ensure Camel can retrieve all data from the stream.          * However you can set this option to true when you for example need to          * access the raw stream, such as streaming it directly to a file or          * other persistent store. Mind that if you enable this option, then you          * cannot read the Netty stream multiple times out of the box, and you          * would need manually to reset the reader index on the Spark raw          * stream.          */
DECL|field|disableStreamCache
specifier|private
name|Boolean
name|disableStreamCache
decl_stmt|;
comment|/**          * If this option is enabled, then during binding from Spark to Camel          * Message then the header values will be URL decoded (eg %20 will be a          * space character.)          */
DECL|field|urlDecodeHeaders
specifier|private
name|Boolean
name|urlDecodeHeaders
decl_stmt|;
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type.          *<p/>          * This is by default turned off. If you enable this then be aware that          * Java will deserialize the incoming data from the request to Java and          * that can be a potential security risk.          */
DECL|field|transferException
specifier|private
name|Boolean
name|transferException
decl_stmt|;
comment|/**          * Whether or not the consumer should try to find a target consumer by          * matching the URI prefix if no exact match is found.          */
DECL|field|matchOnUriPrefix
specifier|private
name|Boolean
name|matchOnUriPrefix
decl_stmt|;
DECL|method|getMapHeaders ()
specifier|public
name|Boolean
name|getMapHeaders
parameter_list|()
block|{
return|return
name|mapHeaders
return|;
block|}
DECL|method|setMapHeaders (Boolean mapHeaders)
specifier|public
name|void
name|setMapHeaders
parameter_list|(
name|Boolean
name|mapHeaders
parameter_list|)
block|{
name|this
operator|.
name|mapHeaders
operator|=
name|mapHeaders
expr_stmt|;
block|}
DECL|method|getDisableStreamCache ()
specifier|public
name|Boolean
name|getDisableStreamCache
parameter_list|()
block|{
return|return
name|disableStreamCache
return|;
block|}
DECL|method|setDisableStreamCache (Boolean disableStreamCache)
specifier|public
name|void
name|setDisableStreamCache
parameter_list|(
name|Boolean
name|disableStreamCache
parameter_list|)
block|{
name|this
operator|.
name|disableStreamCache
operator|=
name|disableStreamCache
expr_stmt|;
block|}
DECL|method|getUrlDecodeHeaders ()
specifier|public
name|Boolean
name|getUrlDecodeHeaders
parameter_list|()
block|{
return|return
name|urlDecodeHeaders
return|;
block|}
DECL|method|setUrlDecodeHeaders (Boolean urlDecodeHeaders)
specifier|public
name|void
name|setUrlDecodeHeaders
parameter_list|(
name|Boolean
name|urlDecodeHeaders
parameter_list|)
block|{
name|this
operator|.
name|urlDecodeHeaders
operator|=
name|urlDecodeHeaders
expr_stmt|;
block|}
DECL|method|getTransferException ()
specifier|public
name|Boolean
name|getTransferException
parameter_list|()
block|{
return|return
name|transferException
return|;
block|}
DECL|method|setTransferException (Boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|Boolean
name|transferException
parameter_list|)
block|{
name|this
operator|.
name|transferException
operator|=
name|transferException
expr_stmt|;
block|}
DECL|method|getMatchOnUriPrefix ()
specifier|public
name|Boolean
name|getMatchOnUriPrefix
parameter_list|()
block|{
return|return
name|matchOnUriPrefix
return|;
block|}
DECL|method|setMatchOnUriPrefix (Boolean matchOnUriPrefix)
specifier|public
name|void
name|setMatchOnUriPrefix
parameter_list|(
name|Boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|this
operator|.
name|matchOnUriPrefix
operator|=
name|matchOnUriPrefix
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

