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
name|LoggingLevel
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Language
import|;
end_import

begin_comment
comment|/**  * The controlbus component provides easy management of Camel applications based  * on the Control Bus EIP pattern.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ControlBusEndpointBuilderFactory
specifier|public
interface|interface
name|ControlBusEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Control Bus component.      */
DECL|interface|ControlBusEndpointBuilder
specifier|public
specifier|static
interface|interface
name|ControlBusEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedControlBusEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedControlBusEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Command can be either route or language.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|command (String command)
specifier|default
name|ControlBusEndpointBuilder
name|command
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"command"
argument_list|,
name|command
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to specify the name of a Language to use for evaluating          * the message body. If there is any result from the evaluation, then          * the result is put in the message body.          * The option is a<code>org.apache.camel.spi.Language</code> type.          * @group producer          */
DECL|method|language (Language language)
specifier|default
name|ControlBusEndpointBuilder
name|language
parameter_list|(
name|Language
name|language
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"language"
argument_list|,
name|language
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to specify the name of a Language to use for evaluating          * the message body. If there is any result from the evaluation, then          * the result is put in the message body.          * The option will be converted to a          *<code>org.apache.camel.spi.Language</code> type.          * @group producer          */
DECL|method|language (String language)
specifier|default
name|ControlBusEndpointBuilder
name|language
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"language"
argument_list|,
name|language
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To denote an action that can be either: start, stop, or status. To          * either start or stop a route, or to get the status of the route as          * output in the message body. You can use suspend and resume from Camel          * 2.11.1 onwards to either suspend or resume a route. And from Camel          * 2.11.1 onwards you can use stats to get performance statics returned          * in XML format; the routeId option can be used to define which route          * to get the performance stats for, if routeId is not defined, then you          * get statistics for the entire CamelContext. The restart action will          * restart the route.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|action (String action)
specifier|default
name|ControlBusEndpointBuilder
name|action
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to execute the control bus task asynchronously. Important: If          * this option is enabled, then any result from the task is not set on          * the Exchange. This is only possible if executing tasks synchronously.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|async (boolean async)
specifier|default
name|ControlBusEndpointBuilder
name|async
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"async"
argument_list|,
name|async
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to execute the control bus task asynchronously. Important: If          * this option is enabled, then any result from the task is not set on          * the Exchange. This is only possible if executing tasks synchronously.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|async (String async)
specifier|default
name|ControlBusEndpointBuilder
name|async
parameter_list|(
name|String
name|async
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"async"
argument_list|,
name|async
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Logging level used for logging when task is done, or if any          * exceptions occurred during processing the task.          * The option is a<code>org.apache.camel.LoggingLevel</code> type.          * @group producer          */
DECL|method|loggingLevel (LoggingLevel loggingLevel)
specifier|default
name|ControlBusEndpointBuilder
name|loggingLevel
parameter_list|(
name|LoggingLevel
name|loggingLevel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"loggingLevel"
argument_list|,
name|loggingLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Logging level used for logging when task is done, or if any          * exceptions occurred during processing the task.          * The option will be converted to a          *<code>org.apache.camel.LoggingLevel</code> type.          * @group producer          */
DECL|method|loggingLevel (String loggingLevel)
specifier|default
name|ControlBusEndpointBuilder
name|loggingLevel
parameter_list|(
name|String
name|loggingLevel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"loggingLevel"
argument_list|,
name|loggingLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The delay in millis to use when restarting a route.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|restartDelay (int restartDelay)
specifier|default
name|ControlBusEndpointBuilder
name|restartDelay
parameter_list|(
name|int
name|restartDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"restartDelay"
argument_list|,
name|restartDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The delay in millis to use when restarting a route.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|restartDelay (String restartDelay)
specifier|default
name|ControlBusEndpointBuilder
name|restartDelay
parameter_list|(
name|String
name|restartDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"restartDelay"
argument_list|,
name|restartDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To specify a route by its id. The special keyword current indicates          * the current route.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|routeId (String routeId)
specifier|default
name|ControlBusEndpointBuilder
name|routeId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"routeId"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Control Bus component.      */
DECL|interface|AdvancedControlBusEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedControlBusEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ControlBusEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ControlBusEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedControlBusEndpointBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedControlBusEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedControlBusEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedControlBusEndpointBuilder
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
block|}
comment|/**      * The controlbus component provides easy management of Camel applications      * based on the Control Bus EIP pattern. Creates a builder to build      * endpoints for the Control Bus component.      */
DECL|method|controlBus (String path)
specifier|default
name|ControlBusEndpointBuilder
name|controlBus
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ControlBusEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ControlBusEndpointBuilder
implements|,
name|AdvancedControlBusEndpointBuilder
block|{
specifier|public
name|ControlBusEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"controlbus"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ControlBusEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

