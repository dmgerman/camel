begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.controlbus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|controlbus
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|RuntimeCamelException
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
name|CamelLogger
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The controlbus component provides easy management of Camel applications based on the Control Bus EIP pattern.  *  * For example, by sending a message to an Endpoint you can control the lifecycle of routes, or gather performance statistics.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.11.0"
argument_list|,
name|scheme
operator|=
literal|"controlbus"
argument_list|,
name|title
operator|=
literal|"Control Bus"
argument_list|,
name|syntax
operator|=
literal|"controlbus:command:language"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"core,monitoring"
argument_list|)
DECL|class|ControlBusEndpoint
specifier|public
class|class
name|ControlBusEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Command can be either route or language"
argument_list|,
name|enums
operator|=
literal|"route,language"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|command
specifier|private
name|String
name|command
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"bean,constant,el,exchangeProperty,file,groovy,header,jsonpath,mvel,ognl,ref,simple,spel,sql,terser,tokenize,xpath,xquery,xtokenize"
argument_list|)
DECL|field|language
specifier|private
name|Language
name|language
decl_stmt|;
annotation|@
name|UriParam
DECL|field|routeId
specifier|private
name|String
name|routeId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"start,stop,suspend,resume,restart,status,stats"
argument_list|)
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|restartDelay
specifier|private
name|int
name|restartDelay
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|async
specifier|private
name|boolean
name|async
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"INFO"
argument_list|)
DECL|field|loggingLevel
specifier|private
name|LoggingLevel
name|loggingLevel
init|=
name|LoggingLevel
operator|.
name|INFO
decl_stmt|;
DECL|method|ControlBusEndpoint (String endpointUri, Component component)
specifier|public
name|ControlBusEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelLogger
name|logger
init|=
operator|new
name|CamelLogger
argument_list|(
name|ControlBusProducer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|loggingLevel
argument_list|)
decl_stmt|;
return|return
operator|new
name|ControlBusProducer
argument_list|(
name|this
argument_list|,
name|logger
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot consume from a ControlBusEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// we dont want to be enlisted in JMX, so lets just be non-singleton
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|ControlBusComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|ControlBusComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getLanguage ()
specifier|public
name|Language
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
comment|/**      * Allows you to specify the name of a Language to use for evaluating the message body.      * If there is any result from the evaluation, then the result is put in the message body.      */
DECL|method|setLanguage (Language language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|Language
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
comment|/**      * To specify a route by its id.      * The special keyword "current" indicates the current route.      */
DECL|method|setRouteId (String routeId)
specifier|public
name|void
name|setRouteId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
comment|/**      * To denote an action that can be either: start, stop, or status.      *<p/>      * To either start or stop a route, or to get the status of the route as output in the message body.      * You can use suspend and resume from Camel 2.11.1 onwards to either suspend or resume a route.      * And from Camel 2.11.1 onwards you can use stats to get performance statics returned in XML format;      * the routeId option can be used to define which route to get the performance stats for, if routeId is not defined,      * then you get statistics for the entire CamelContext. The restart action will restart the route.      */
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getRestartDelay ()
specifier|public
name|int
name|getRestartDelay
parameter_list|()
block|{
return|return
name|restartDelay
return|;
block|}
comment|/**      * The delay in millis to use when restarting a route.      */
DECL|method|setRestartDelay (int restartDelay)
specifier|public
name|void
name|setRestartDelay
parameter_list|(
name|int
name|restartDelay
parameter_list|)
block|{
name|this
operator|.
name|restartDelay
operator|=
name|restartDelay
expr_stmt|;
block|}
DECL|method|isAsync ()
specifier|public
name|boolean
name|isAsync
parameter_list|()
block|{
return|return
name|async
return|;
block|}
comment|/**      * Whether to execute the control bus task asynchronously.      *<p/>      * Important: If this option is enabled, then any result from the task is not set on the Exchange.      * This is only possible if executing tasks synchronously.      */
DECL|method|setAsync (boolean async)
specifier|public
name|void
name|setAsync
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|this
operator|.
name|async
operator|=
name|async
expr_stmt|;
block|}
DECL|method|getLoggingLevel ()
specifier|public
name|LoggingLevel
name|getLoggingLevel
parameter_list|()
block|{
return|return
name|loggingLevel
return|;
block|}
comment|/**      * Logging level used for logging when task is done, or if any exceptions occurred during processing the task.      */
DECL|method|setLoggingLevel (LoggingLevel loggingLevel)
specifier|public
name|void
name|setLoggingLevel
parameter_list|(
name|LoggingLevel
name|loggingLevel
parameter_list|)
block|{
name|this
operator|.
name|loggingLevel
operator|=
name|loggingLevel
expr_stmt|;
block|}
block|}
end_class

end_unit

