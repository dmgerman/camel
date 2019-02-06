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
name|spi
operator|.
name|AsPredicate
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

begin_comment
comment|/**  * Intercepts messages being sent to an endpoint  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"interceptSendToEndpoint"
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
DECL|field|skipSendToOriginalEndpoint
specifier|private
name|Boolean
name|skipSendToOriginalEndpoint
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
literal|"interceptSendToEndpoint["
operator|+
name|uri
operator|+
literal|"]"
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
DECL|method|isTopLevelOnly ()
specifier|public
name|boolean
name|isTopLevelOnly
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Applies this interceptor only if the given predicate is true      *      * @param predicate  the predicate      * @return the builder      */
DECL|method|when (@sPredicate Predicate predicate)
specifier|public
name|InterceptSendToEndpointDefinition
name|when
parameter_list|(
annotation|@
name|AsPredicate
name|Predicate
name|predicate
parameter_list|)
block|{
name|WhenDefinition
name|when
init|=
operator|new
name|WhenDefinition
argument_list|(
name|predicate
argument_list|)
decl_stmt|;
name|addOutput
argument_list|(
name|when
argument_list|)
expr_stmt|;
return|return
name|this
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
comment|/**      * This method is<b>only</b> for handling some post configuration      * that is needed since this is an interceptor, and we have to do      * a bit of magic logic to fixup to handle predicates      * with or without proceed/stop set as well.      */
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
comment|// if there is a when definition at first, then its a predicate for this interceptor
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
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
operator|&&
operator|!
operator|(
name|first
operator|instanceof
name|WhenSkipSendToEndpointDefinition
operator|)
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
comment|// create a copy of when to use as replacement
name|WhenSkipSendToEndpointDefinition
name|newWhen
init|=
operator|new
name|WhenSkipSendToEndpointDefinition
argument_list|()
decl_stmt|;
name|newWhen
operator|.
name|setExpression
argument_list|(
name|when
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|newWhen
operator|.
name|setId
argument_list|(
name|when
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|newWhen
operator|.
name|setInheritErrorHandler
argument_list|(
name|when
operator|.
name|isInheritErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|newWhen
operator|.
name|setParent
argument_list|(
name|when
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|newWhen
operator|.
name|setOtherAttributes
argument_list|(
name|when
operator|.
name|getOtherAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|newWhen
operator|.
name|setDescription
argument_list|(
name|when
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
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
argument_list|<
name|?
argument_list|>
name|out
init|=
name|outputs
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|newWhen
operator|.
name|addOutput
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
comment|// remove the moved from the original output, by just keeping the first one
name|clearOutput
argument_list|()
expr_stmt|;
name|outputs
operator|.
name|add
argument_list|(
name|newWhen
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
comment|/**      * If set to true then the message is not sent to the original endpoint.      * By default (false) the message is both intercepted and then sent to the original endpoint.      */
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
comment|/**      * Intercept sending to the uri or uri pattern.      */
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

