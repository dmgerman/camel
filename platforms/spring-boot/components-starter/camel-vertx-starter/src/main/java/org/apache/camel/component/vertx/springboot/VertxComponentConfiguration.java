begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vertx.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vertx
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
comment|/**  * The vertx component is used for sending and receive messages from a vertx  * event bus.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.vertx"
argument_list|)
DECL|class|VertxComponentConfiguration
specifier|public
class|class
name|VertxComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the vertx component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use a custom VertxFactory implementation. The option is a      * io.vertx.core.spi.VertxFactory type.      */
DECL|field|vertxFactory
specifier|private
name|String
name|vertxFactory
decl_stmt|;
comment|/**      * Hostname for creating an embedded clustered EventBus      */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * Port for creating an embedded clustered EventBus      */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**      * Options to use for creating vertx. The option is a      * io.vertx.core.VertxOptions type.      */
DECL|field|vertxOptions
specifier|private
name|String
name|vertxOptions
decl_stmt|;
comment|/**      * To use the given vertx EventBus instead of creating a new embedded      * EventBus. The option is a io.vertx.core.Vertx type.      */
DECL|field|vertx
specifier|private
name|String
name|vertx
decl_stmt|;
comment|/**      * Timeout in seconds to wait for clustered Vertx EventBus to be ready. The      * default value is 60.      */
DECL|field|timeout
specifier|private
name|Integer
name|timeout
init|=
literal|60
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
DECL|method|getVertxFactory ()
specifier|public
name|String
name|getVertxFactory
parameter_list|()
block|{
return|return
name|vertxFactory
return|;
block|}
DECL|method|setVertxFactory (String vertxFactory)
specifier|public
name|void
name|setVertxFactory
parameter_list|(
name|String
name|vertxFactory
parameter_list|)
block|{
name|this
operator|.
name|vertxFactory
operator|=
name|vertxFactory
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
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
DECL|method|getVertxOptions ()
specifier|public
name|String
name|getVertxOptions
parameter_list|()
block|{
return|return
name|vertxOptions
return|;
block|}
DECL|method|setVertxOptions (String vertxOptions)
specifier|public
name|void
name|setVertxOptions
parameter_list|(
name|String
name|vertxOptions
parameter_list|)
block|{
name|this
operator|.
name|vertxOptions
operator|=
name|vertxOptions
expr_stmt|;
block|}
DECL|method|getVertx ()
specifier|public
name|String
name|getVertx
parameter_list|()
block|{
return|return
name|vertx
return|;
block|}
DECL|method|setVertx (String vertx)
specifier|public
name|void
name|setVertx
parameter_list|(
name|String
name|vertx
parameter_list|)
block|{
name|this
operator|.
name|vertx
operator|=
name|vertx
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Integer
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Integer timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Integer
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
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

