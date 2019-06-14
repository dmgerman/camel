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
name|ExchangePattern
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
name|ExceptionHandler
import|;
end_import

begin_comment
comment|/**  * Solves the planning problem contained in a message with OptaPlanner.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|OptaPlannerEndpointBuilderFactory
specifier|public
interface|interface
name|OptaPlannerEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the OptaPlanner component.      */
DECL|interface|OptaPlannerEndpointConsumerBuilder
specifier|public
interface|interface
name|OptaPlannerEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedOptaPlannerEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Specifies the location to the solver file.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|configFile ( String configFile)
specifier|public
specifier|default
name|OptaPlannerEndpointConsumerBuilder
name|configFile
parameter_list|(
name|String
name|configFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"configFile"
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the solverId to user for the solver instance key.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|solverId ( String solverId)
specifier|public
specifier|default
name|OptaPlannerEndpointConsumerBuilder
name|solverId
parameter_list|(
name|String
name|solverId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"solverId"
argument_list|,
name|solverId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|public
specifier|default
name|OptaPlannerEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|public
specifier|default
name|OptaPlannerEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the OptaPlanner component.      */
DECL|interface|AdvancedOptaPlannerEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedOptaPlannerEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|OptaPlannerEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|OptaPlannerEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          * The option is a<code>org.apache.camel.ExchangePattern</code> type.          * @group consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          * @group consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
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
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
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
DECL|method|synchronous ( String synchronous)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the OptaPlanner component.      */
DECL|interface|OptaPlannerEndpointProducerBuilder
specifier|public
specifier|static
interface|interface
name|OptaPlannerEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedOptaPlannerEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Specifies the location to the solver file.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|configFile ( String configFile)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
name|configFile
parameter_list|(
name|String
name|configFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"configFile"
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the solverId to user for the solver instance key.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|solverId ( String solverId)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
name|solverId
parameter_list|(
name|String
name|solverId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"solverId"
argument_list|,
name|solverId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies to perform operations in async mode.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|async (boolean async)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
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
comment|/**          * Specifies to perform operations in async mode.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|async (String async)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
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
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the thread pool size to use when async is true.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|threadPoolSize ( int threadPoolSize)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
name|threadPoolSize
parameter_list|(
name|int
name|threadPoolSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"threadPoolSize"
argument_list|,
name|threadPoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the thread pool size to use when async is true.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|threadPoolSize ( String threadPoolSize)
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
name|threadPoolSize
parameter_list|(
name|String
name|threadPoolSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"threadPoolSize"
argument_list|,
name|threadPoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the OptaPlanner component.      */
DECL|interface|AdvancedOptaPlannerEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedOptaPlannerEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|OptaPlannerEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|OptaPlannerEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointProducerBuilder
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
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointProducerBuilder
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
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointProducerBuilder
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
DECL|method|synchronous ( String synchronous)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the OptaPlanner component.      */
DECL|interface|OptaPlannerEndpointBuilder
specifier|public
specifier|static
interface|interface
name|OptaPlannerEndpointBuilder
extends|extends
name|OptaPlannerEndpointConsumerBuilder
extends|,
name|OptaPlannerEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedOptaPlannerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Specifies the location to the solver file.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|configFile (String configFile)
specifier|public
specifier|default
name|OptaPlannerEndpointBuilder
name|configFile
parameter_list|(
name|String
name|configFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"configFile"
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the solverId to user for the solver instance key.          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|solverId (String solverId)
specifier|public
specifier|default
name|OptaPlannerEndpointBuilder
name|solverId
parameter_list|(
name|String
name|solverId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"solverId"
argument_list|,
name|solverId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the OptaPlanner component.      */
DECL|interface|AdvancedOptaPlannerEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedOptaPlannerEndpointBuilder
extends|extends
name|AdvancedOptaPlannerEndpointConsumerBuilder
extends|,
name|AdvancedOptaPlannerEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|OptaPlannerEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|OptaPlannerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointBuilder
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
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointBuilder
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
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointBuilder
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
DECL|method|synchronous ( String synchronous)
specifier|public
specifier|default
name|AdvancedOptaPlannerEndpointBuilder
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
comment|/**      * Solves the planning problem contained in a message with OptaPlanner.      * Creates a builder to build endpoints for the OptaPlanner component.      */
DECL|method|optaPlanner (String path)
specifier|public
specifier|default
name|OptaPlannerEndpointBuilder
name|optaPlanner
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|OptaPlannerEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|OptaPlannerEndpointBuilder
implements|,
name|AdvancedOptaPlannerEndpointBuilder
block|{
specifier|public
name|OptaPlannerEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"optaplanner"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|OptaPlannerEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

