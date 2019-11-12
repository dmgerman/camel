begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch
operator|.
name|springboot
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_comment
comment|/**  * The elasticsearch component is used for interfacing with ElasticSearch server  * using REST API.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.elasticsearch-rest"
argument_list|)
DECL|class|ElasticsearchComponentConfiguration
specifier|public
class|class
name|ElasticsearchComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the elasticsearch-rest component.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use an existing configured Elasticsearch client, instead of creating a      * client per endpoint. This allow to customize the client with specific      * settings. The option is a org.elasticsearch.client.RestClient type.      */
DECL|field|client
specifier|private
name|String
name|client
decl_stmt|;
comment|/**      * Comma separated list with ip:port formatted remote transport addresses to      * use. The ip and port options must be left blank for hostAddresses to be      * considered instead.      */
DECL|field|hostAddresses
specifier|private
name|String
name|hostAddresses
decl_stmt|;
comment|/**      * The timeout in ms to wait before the socket will timeout.      */
DECL|field|socketTimeout
specifier|private
name|Integer
name|socketTimeout
init|=
literal|30000
decl_stmt|;
comment|/**      * The time in ms to wait before connection will timeout.      */
DECL|field|connectionTimeout
specifier|private
name|Integer
name|connectionTimeout
init|=
literal|30000
decl_stmt|;
comment|/**      * Basic authenticate user      */
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
comment|/**      * Password for authenticate      */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * Enable SSL      */
DECL|field|enableSSL
specifier|private
name|Boolean
name|enableSSL
init|=
literal|false
decl_stmt|;
comment|/**      * The time in ms before retry      */
DECL|field|maxRetryTimeout
specifier|private
name|Integer
name|maxRetryTimeout
init|=
literal|30000
decl_stmt|;
comment|/**      * Enable automatically discover nodes from a running Elasticsearch cluster      */
DECL|field|enableSniffer
specifier|private
name|Boolean
name|enableSniffer
init|=
literal|false
decl_stmt|;
comment|/**      * The interval between consecutive ordinary sniff executions in      * milliseconds. Will be honoured when sniffOnFailure is disabled or when      * there are no failures between consecutive sniff executions      */
DECL|field|snifferInterval
specifier|private
name|Integer
name|snifferInterval
init|=
literal|300000
decl_stmt|;
comment|/**      * The delay of a sniff execution scheduled after a failure (in      * milliseconds)      */
DECL|field|sniffAfterFailureDelay
specifier|private
name|Integer
name|sniffAfterFailureDelay
init|=
literal|60000
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the producer should be started lazy (on the first message). By      * starting lazy you can use this to allow CamelContext and routes to      * startup in situations where a producer may otherwise fail during starting      * and cause the route to fail being started. By deferring this startup to      * be lazy then the startup failure can be handled during routing messages      * via Camel's routing error handlers. Beware that when the first message is      * processed then creating and starting the producer may take a little time      * and prolong the total processing time of the processing.      */
DECL|field|lazyStartProducer
specifier|private
name|Boolean
name|lazyStartProducer
init|=
literal|false
decl_stmt|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler,      * which mean any exceptions occurred while the consumer is trying to pickup      * incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler. By default the consumer will use      * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that      * will be logged at WARN or ERROR level and ignored.      */
DECL|field|bridgeErrorHandler
specifier|private
name|Boolean
name|bridgeErrorHandler
init|=
literal|false
decl_stmt|;
DECL|method|getClient ()
specifier|public
name|String
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (String client)
specifier|public
name|void
name|setClient
parameter_list|(
name|String
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getHostAddresses ()
specifier|public
name|String
name|getHostAddresses
parameter_list|()
block|{
return|return
name|hostAddresses
return|;
block|}
DECL|method|setHostAddresses (String hostAddresses)
specifier|public
name|void
name|setHostAddresses
parameter_list|(
name|String
name|hostAddresses
parameter_list|)
block|{
name|this
operator|.
name|hostAddresses
operator|=
name|hostAddresses
expr_stmt|;
block|}
DECL|method|getSocketTimeout ()
specifier|public
name|Integer
name|getSocketTimeout
parameter_list|()
block|{
return|return
name|socketTimeout
return|;
block|}
DECL|method|setSocketTimeout (Integer socketTimeout)
specifier|public
name|void
name|setSocketTimeout
parameter_list|(
name|Integer
name|socketTimeout
parameter_list|)
block|{
name|this
operator|.
name|socketTimeout
operator|=
name|socketTimeout
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|Integer
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (Integer connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|Integer
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|setUser (String user)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getEnableSSL ()
specifier|public
name|Boolean
name|getEnableSSL
parameter_list|()
block|{
return|return
name|enableSSL
return|;
block|}
DECL|method|setEnableSSL (Boolean enableSSL)
specifier|public
name|void
name|setEnableSSL
parameter_list|(
name|Boolean
name|enableSSL
parameter_list|)
block|{
name|this
operator|.
name|enableSSL
operator|=
name|enableSSL
expr_stmt|;
block|}
DECL|method|getMaxRetryTimeout ()
specifier|public
name|Integer
name|getMaxRetryTimeout
parameter_list|()
block|{
return|return
name|maxRetryTimeout
return|;
block|}
DECL|method|setMaxRetryTimeout (Integer maxRetryTimeout)
specifier|public
name|void
name|setMaxRetryTimeout
parameter_list|(
name|Integer
name|maxRetryTimeout
parameter_list|)
block|{
name|this
operator|.
name|maxRetryTimeout
operator|=
name|maxRetryTimeout
expr_stmt|;
block|}
DECL|method|getEnableSniffer ()
specifier|public
name|Boolean
name|getEnableSniffer
parameter_list|()
block|{
return|return
name|enableSniffer
return|;
block|}
DECL|method|setEnableSniffer (Boolean enableSniffer)
specifier|public
name|void
name|setEnableSniffer
parameter_list|(
name|Boolean
name|enableSniffer
parameter_list|)
block|{
name|this
operator|.
name|enableSniffer
operator|=
name|enableSniffer
expr_stmt|;
block|}
DECL|method|getSnifferInterval ()
specifier|public
name|Integer
name|getSnifferInterval
parameter_list|()
block|{
return|return
name|snifferInterval
return|;
block|}
DECL|method|setSnifferInterval (Integer snifferInterval)
specifier|public
name|void
name|setSnifferInterval
parameter_list|(
name|Integer
name|snifferInterval
parameter_list|)
block|{
name|this
operator|.
name|snifferInterval
operator|=
name|snifferInterval
expr_stmt|;
block|}
DECL|method|getSniffAfterFailureDelay ()
specifier|public
name|Integer
name|getSniffAfterFailureDelay
parameter_list|()
block|{
return|return
name|sniffAfterFailureDelay
return|;
block|}
DECL|method|setSniffAfterFailureDelay (Integer sniffAfterFailureDelay)
specifier|public
name|void
name|setSniffAfterFailureDelay
parameter_list|(
name|Integer
name|sniffAfterFailureDelay
parameter_list|)
block|{
name|this
operator|.
name|sniffAfterFailureDelay
operator|=
name|sniffAfterFailureDelay
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|method|getLazyStartProducer ()
specifier|public
name|Boolean
name|getLazyStartProducer
parameter_list|()
block|{
return|return
name|lazyStartProducer
return|;
block|}
DECL|method|setLazyStartProducer (Boolean lazyStartProducer)
specifier|public
name|void
name|setLazyStartProducer
parameter_list|(
name|Boolean
name|lazyStartProducer
parameter_list|)
block|{
name|this
operator|.
name|lazyStartProducer
operator|=
name|lazyStartProducer
expr_stmt|;
block|}
DECL|method|getBridgeErrorHandler ()
specifier|public
name|Boolean
name|getBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
DECL|method|setBridgeErrorHandler (Boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|Boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
block|}
end_class

end_unit

