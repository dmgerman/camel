begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,circuitbreaker"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"circuitBreaker"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CircuitBreakerDefinition
specifier|public
class|class
name|CircuitBreakerDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|CircuitBreakerDefinition
argument_list|>
implements|implements
name|OutputNode
block|{
annotation|@
name|XmlElement
DECL|field|hystrixConfiguration
specifier|private
name|HystrixConfigurationDefinition
name|hystrixConfiguration
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|resilience4jConfiguration
specifier|private
name|Resilience4jConfigurationDefinition
name|resilience4jConfiguration
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|configurationRef
specifier|private
name|String
name|configurationRef
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|onFallback
specifier|private
name|OnFallbackDefinition
name|onFallback
decl_stmt|;
DECL|method|CircuitBreakerDefinition ()
specifier|public
name|CircuitBreakerDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CircuitBreaker["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"circuitBreaker"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"circuitBreaker"
return|;
block|}
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition<?>> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
if|if
condition|(
name|outputs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|outputs
control|)
block|{
name|configureChild
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|addOutput (ProcessorDefinition<?> output)
specifier|public
name|void
name|addOutput
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
parameter_list|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|OnFallbackDefinition
condition|)
block|{
name|onFallback
operator|=
operator|(
name|OnFallbackDefinition
operator|)
name|output
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|onFallback
operator|!=
literal|null
condition|)
block|{
name|onFallback
operator|.
name|addOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|addOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|end ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|end
parameter_list|()
block|{
if|if
condition|(
name|onFallback
operator|!=
literal|null
condition|)
block|{
comment|// end fallback as well
name|onFallback
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
return|return
name|super
operator|.
name|end
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|preCreateProcessor ()
specifier|public
name|void
name|preCreateProcessor
parameter_list|()
block|{
comment|// move the fallback from outputs to fallback which we need to ensure
comment|// such as when using the XML DSL
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|it
init|=
name|outputs
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|out
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|OnFallbackDefinition
condition|)
block|{
name|onFallback
operator|=
operator|(
name|OnFallbackDefinition
operator|)
name|out
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// Getter/Setter
comment|// -------------------------------------------------------------------------
DECL|method|getHystrixConfiguration ()
specifier|public
name|HystrixConfigurationDefinition
name|getHystrixConfiguration
parameter_list|()
block|{
return|return
name|hystrixConfiguration
return|;
block|}
DECL|method|setHystrixConfiguration (HystrixConfigurationDefinition hystrixConfiguration)
specifier|public
name|void
name|setHystrixConfiguration
parameter_list|(
name|HystrixConfigurationDefinition
name|hystrixConfiguration
parameter_list|)
block|{
name|this
operator|.
name|hystrixConfiguration
operator|=
name|hystrixConfiguration
expr_stmt|;
block|}
DECL|method|getResilience4jConfiguration ()
specifier|public
name|Resilience4jConfigurationCommon
name|getResilience4jConfiguration
parameter_list|()
block|{
return|return
name|resilience4jConfiguration
return|;
block|}
DECL|method|setResilience4jConfiguration (Resilience4jConfigurationDefinition resilience4jConfiguration)
specifier|public
name|void
name|setResilience4jConfiguration
parameter_list|(
name|Resilience4jConfigurationDefinition
name|resilience4jConfiguration
parameter_list|)
block|{
name|this
operator|.
name|resilience4jConfiguration
operator|=
name|resilience4jConfiguration
expr_stmt|;
block|}
DECL|method|getConfigurationRef ()
specifier|public
name|String
name|getConfigurationRef
parameter_list|()
block|{
return|return
name|configurationRef
return|;
block|}
comment|/**      * Refers to a circuit breaker configuration (such as hystrix, resillience4j, or microprofile-fault-tolerance)      * to use for configuring the circuit breaker EIP.      */
DECL|method|setConfigurationRef (String configurationRef)
specifier|public
name|void
name|setConfigurationRef
parameter_list|(
name|String
name|configurationRef
parameter_list|)
block|{
name|this
operator|.
name|configurationRef
operator|=
name|configurationRef
expr_stmt|;
block|}
DECL|method|getOnFallback ()
specifier|public
name|OnFallbackDefinition
name|getOnFallback
parameter_list|()
block|{
return|return
name|onFallback
return|;
block|}
DECL|method|setOnFallback (OnFallbackDefinition onFallback)
specifier|public
name|void
name|setOnFallback
parameter_list|(
name|OnFallbackDefinition
name|onFallback
parameter_list|)
block|{
name|this
operator|.
name|onFallback
operator|=
name|onFallback
expr_stmt|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Configures the circuit breaker to use Hystrix.      *<p/>      * Use<tt>end</tt> when configuration is complete, to return back to the      * Circuit Breaker EIP.      */
DECL|method|hystrixConfiguration ()
specifier|public
name|HystrixConfigurationDefinition
name|hystrixConfiguration
parameter_list|()
block|{
name|hystrixConfiguration
operator|=
name|hystrixConfiguration
operator|==
literal|null
condition|?
operator|new
name|HystrixConfigurationDefinition
argument_list|(
name|this
argument_list|)
else|:
name|hystrixConfiguration
expr_stmt|;
return|return
name|hystrixConfiguration
return|;
block|}
comment|/**      * Configures the circuit breaker to use Hystrix with the given configuration.      */
DECL|method|hystrixConfiguration (HystrixConfigurationDefinition configuration)
specifier|public
name|CircuitBreakerDefinition
name|hystrixConfiguration
parameter_list|(
name|HystrixConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|hystrixConfiguration
operator|=
name|configuration
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the circuit breaker to use Resilience4j.      *<p/>      * Use<tt>end</tt> when configuration is complete, to return back to the      * Circuit Breaker EIP.      */
DECL|method|resilience4jConfiguration ()
specifier|public
name|Resilience4jConfigurationDefinition
name|resilience4jConfiguration
parameter_list|()
block|{
name|resilience4jConfiguration
operator|=
name|resilience4jConfiguration
operator|==
literal|null
condition|?
operator|new
name|Resilience4jConfigurationDefinition
argument_list|(
name|this
argument_list|)
else|:
name|resilience4jConfiguration
expr_stmt|;
return|return
name|resilience4jConfiguration
return|;
block|}
comment|/**      * Configures the circuit breaker to use Resilience4j with the given configuration.      */
DECL|method|resilience4jConfiguration (Resilience4jConfigurationDefinition configuration)
specifier|public
name|CircuitBreakerDefinition
name|resilience4jConfiguration
parameter_list|(
name|Resilience4jConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|resilience4jConfiguration
operator|=
name|configuration
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Refers to a configuration to use for configuring the circuit breaker.      */
DECL|method|configuration (String ref)
specifier|public
name|CircuitBreakerDefinition
name|configuration
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|configurationRef
operator|=
name|ref
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The fallback route path to execute that does<b>not</b> go over      * the network.      *<p>      * This should be a static or cached result that can immediately be returned      * upon failure. If the fallback requires network connection then use      * {@link #onFallbackViaNetwork()}.      */
DECL|method|onFallback ()
specifier|public
name|CircuitBreakerDefinition
name|onFallback
parameter_list|()
block|{
name|onFallback
operator|=
operator|new
name|OnFallbackDefinition
argument_list|()
expr_stmt|;
name|onFallback
operator|.
name|setParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The fallback route path to execute that will go over the network.      *<p/>      * If the fallback will go over the network it is another possible point of failure.      */
DECL|method|onFallbackViaNetwork ()
specifier|public
name|CircuitBreakerDefinition
name|onFallbackViaNetwork
parameter_list|()
block|{
name|onFallback
operator|=
operator|new
name|OnFallbackDefinition
argument_list|()
expr_stmt|;
name|onFallback
operator|.
name|setFallbackViaNetwork
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|onFallback
operator|.
name|setParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

