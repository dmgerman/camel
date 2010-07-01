begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|XmlRootElement
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
name|Endpoint
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
name|Predicate
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
name|impl
operator|.
name|InterceptSendToEndpoint
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
name|processor
operator|.
name|InterceptEndpointProcessor
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
name|EndpointStrategy
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
name|RouteContext
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
name|util
operator|.
name|EndpointHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;interceptToEndpoint/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"interceptToEndpoint"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|InterceptSendToEndpointDefinition
specifier|public
class|class
name|InterceptSendToEndpointDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
block|{
comment|// TODO: Support lookup endpoint by ref (requires a bit more work)
comment|// TODO: interceptSendToEndpoint needs to proxy the endpoints at very first
comment|// so when other processors uses an endpoint its already proxied, see workaround in SendProcessor
comment|// needed when we haven't proxied beforehand. This requires some work in the route builder in Camel
comment|// to implement so that should be a part of a bigger rework/improvement in the future
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|skipSendToOriginalEndpoint
specifier|private
name|Boolean
name|skipSendToOriginalEndpoint
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
DECL|method|InterceptSendToEndpointDefinition ()
specifier|public
name|InterceptSendToEndpointDefinition
parameter_list|()
block|{     }
DECL|method|InterceptSendToEndpointDefinition (String uri)
specifier|public
name|InterceptSendToEndpointDefinition
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"InterceptSendToEndpoint["
operator|+
name|uri
operator|+
literal|" -> "
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
literal|"interceptSendToEndpoint"
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
literal|"interceptEndpoint"
return|;
block|}
annotation|@
name|Override
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create the detour
specifier|final
name|Processor
name|detour
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// register endpoint callback so we can proxy the endpoint
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addRegisterEndpointCallback
argument_list|(
operator|new
name|EndpointStrategy
argument_list|()
block|{
specifier|public
name|Endpoint
name|registerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|instanceof
name|InterceptSendToEndpoint
condition|)
block|{
comment|// endpoint already decorated
return|return
name|endpoint
return|;
block|}
elseif|else
if|if
condition|(
name|getUri
argument_list|()
operator|==
literal|null
operator|||
name|EndpointHelper
operator|.
name|matchEndpoint
argument_list|(
name|uri
argument_list|,
name|getUri
argument_list|()
argument_list|)
condition|)
block|{
comment|// only proxy if the uri is matched decorate endpoint with our proxy
name|boolean
name|skip
init|=
name|skipSendToOriginalEndpoint
operator|!=
literal|null
condition|?
name|skipSendToOriginalEndpoint
else|:
literal|false
decl_stmt|;
name|InterceptSendToEndpoint
name|proxy
init|=
operator|new
name|InterceptSendToEndpoint
argument_list|(
name|endpoint
argument_list|,
name|skip
argument_list|)
decl_stmt|;
name|proxy
operator|.
name|setDetour
argument_list|(
name|detour
argument_list|)
expr_stmt|;
return|return
name|proxy
return|;
block|}
else|else
block|{
comment|// no proxy so return regular endpoint
return|return
name|endpoint
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// remove the original intercepted route from the outputs as we do not intercept as the regular interceptor
comment|// instead we use the proxy endpoints producer do the triggering. That is we trigger when someone sends
comment|// an exchange to the endpoint, see InterceptSendToEndpoint for details.
name|RouteDefinition
name|route
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
name|route
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|outputs
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|InterceptEndpointProcessor
argument_list|(
name|uri
argument_list|,
name|detour
argument_list|)
return|;
block|}
comment|/**      * Applies this interceptor only if the given predicate is true      *      * @param predicate  the predicate      * @return the builder      */
DECL|method|when (Predicate predicate)
specifier|public
name|ChoiceDefinition
name|when
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
return|return
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|predicate
argument_list|)
return|;
block|}
comment|/**      * Skip sending the {@link org.apache.camel.Exchange} to the original intended endpoint      *      * @return the builder      */
DECL|method|skipSendToOriginalEndpoint ()
specifier|public
name|InterceptSendToEndpointDefinition
name|skipSendToOriginalEndpoint
parameter_list|()
block|{
name|setSkipSendToOriginalEndpoint
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * This method is<b>only</b> for handling some post configuration      * that is needed from the Spring DSL side as JAXB does not invoke the fluent      * builders, so we need to manually handle this afterwards, and since this is      * an interceptor it has to do a bit of magic logic to fixup to handle predicates      * with or without proceed/stop set as well.      */
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
block|{
comment|// okay the intercept endpoint works a bit differently than the regular interceptors
comment|// so we must fix the route definition yet again
if|if
condition|(
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// no outputs
return|return;
block|}
name|ProcessorDefinition
name|first
init|=
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|instanceof
name|WhenDefinition
condition|)
block|{
name|WhenDefinition
name|when
init|=
operator|(
name|WhenDefinition
operator|)
name|first
decl_stmt|;
comment|// move this outputs to the when, expect the first one
comment|// as the first one is the interceptor itself
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|outputs
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ProcessorDefinition
name|out
init|=
name|outputs
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|when
operator|.
name|addOutput
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
comment|// remove the moved from the original output, by just keeping the first one
name|ProcessorDefinition
name|keep
init|=
name|outputs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|clearOutput
argument_list|()
expr_stmt|;
name|outputs
operator|.
name|add
argument_list|(
name|keep
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSkipSendToOriginalEndpoint ()
specifier|public
name|Boolean
name|getSkipSendToOriginalEndpoint
parameter_list|()
block|{
return|return
name|skipSendToOriginalEndpoint
return|;
block|}
DECL|method|setSkipSendToOriginalEndpoint (Boolean skipSendToOriginalEndpoint)
specifier|public
name|void
name|setSkipSendToOriginalEndpoint
parameter_list|(
name|Boolean
name|skipSendToOriginalEndpoint
parameter_list|)
block|{
name|this
operator|.
name|skipSendToOriginalEndpoint
operator|=
name|skipSendToOriginalEndpoint
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
block|}
end_class

end_unit

