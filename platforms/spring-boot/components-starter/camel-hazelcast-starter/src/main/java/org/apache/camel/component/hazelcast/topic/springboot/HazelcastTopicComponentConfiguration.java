begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.topic.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|topic
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
comment|/**  * The hazelcast-topic component is used to access Hazelcast distributed topic.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.hazelcast-topic"
argument_list|)
DECL|class|HazelcastTopicComponentConfiguration
specifier|public
class|class
name|HazelcastTopicComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the hazelcast-topic component.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The hazelcast instance reference which can be used for hazelcast      * endpoint. If you don't specify the instance reference, camel use the      * default hazelcast instance from the camel-hazelcast instance. The option      * is a com.hazelcast.core.HazelcastInstance type.      */
DECL|field|hazelcastInstance
specifier|private
name|String
name|hazelcastInstance
decl_stmt|;
comment|/**      * The hazelcast mode reference which kind of instance should be used. If      * you don't specify the mode, then the node mode will be the default.      */
DECL|field|hazelcastMode
specifier|private
name|String
name|hazelcastMode
init|=
literal|"node"
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
DECL|method|getHazelcastInstance ()
specifier|public
name|String
name|getHazelcastInstance
parameter_list|()
block|{
return|return
name|hazelcastInstance
return|;
block|}
DECL|method|setHazelcastInstance (String hazelcastInstance)
specifier|public
name|void
name|setHazelcastInstance
parameter_list|(
name|String
name|hazelcastInstance
parameter_list|)
block|{
name|this
operator|.
name|hazelcastInstance
operator|=
name|hazelcastInstance
expr_stmt|;
block|}
DECL|method|getHazelcastMode ()
specifier|public
name|String
name|getHazelcastMode
parameter_list|()
block|{
return|return
name|hazelcastMode
return|;
block|}
DECL|method|setHazelcastMode (String hazelcastMode)
specifier|public
name|void
name|setHazelcastMode
parameter_list|(
name|String
name|hazelcastMode
parameter_list|)
block|{
name|this
operator|.
name|hazelcastMode
operator|=
name|hazelcastMode
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

