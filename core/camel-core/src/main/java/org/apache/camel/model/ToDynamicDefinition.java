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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Sends the message to a dynamic endpoint  *<p/>  * You can specify multiple languages in the uri separated by the plus sign,  * such as<tt>mock:+language:xpath:/order/@uri</tt> where<tt>mock:</tt> would  * be a prefix to a xpath expression.  *<p/>  * For more dynamic behavior use  *<a href="http://camel.apache.org/recipient-list.html">Recipient List</a> or  *<a href="http://camel.apache.org/dynamic-router.html">Dynamic Router</a> EIP  * instead.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,endpoint,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"toD"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ToDynamicDefinition
specifier|public
class|class
name|ToDynamicDefinition
extends|extends
name|NoOutputDefinition
argument_list|<
name|ToDynamicDefinition
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|endpointProducerBuilder
specifier|protected
name|EndpointProducerBuilder
name|endpointProducerBuilder
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
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
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|cacheSize
specifier|private
name|Integer
name|cacheSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreInvalidEndpoint
specifier|private
name|Boolean
name|ignoreInvalidEndpoint
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|allowOptimisedComponents
specifier|private
name|Boolean
name|allowOptimisedComponents
decl_stmt|;
DECL|method|ToDynamicDefinition ()
specifier|public
name|ToDynamicDefinition
parameter_list|()
block|{     }
DECL|method|ToDynamicDefinition (String uri)
specifier|public
name|ToDynamicDefinition
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
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"toD"
return|;
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
literal|"DynamicTo["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"]"
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
name|uri
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the optional {@link ExchangePattern} used to invoke this endpoint      */
DECL|method|pattern (ExchangePattern pattern)
specifier|public
name|ToDynamicDefinition
name|pattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum size used by the      * {@link org.apache.camel.spi.ConsumerCache} which is used to cache and      * reuse producers.      *      * @param cacheSize the cache size, use<tt>0</tt> for default cache size,      *            or<tt>-1</tt> to turn cache off.      * @return the builder      */
DECL|method|cacheSize (int cacheSize)
specifier|public
name|ToDynamicDefinition
name|cacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|setCacheSize
argument_list|(
name|cacheSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Ignore the invalidate endpoint exception when try to create a producer      * with that endpoint      *      * @return the builder      */
DECL|method|ignoreInvalidEndpoint ()
specifier|public
name|ToDynamicDefinition
name|ignoreInvalidEndpoint
parameter_list|()
block|{
name|setIgnoreInvalidEndpoint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to allow components to optimise toD if they are      * {@link org.apache.camel.spi.SendDynamicAware}.      *      * @return the builder      */
DECL|method|allowOptimisedComponents (boolean allowOptimisedComponents)
specifier|public
name|ToDynamicDefinition
name|allowOptimisedComponents
parameter_list|(
name|boolean
name|allowOptimisedComponents
parameter_list|)
block|{
name|setAllowOptimisedComponents
argument_list|(
name|allowOptimisedComponents
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
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
comment|/**      * The uri of the endpoint to send to. The uri can be dynamic computed using      * the {@link org.apache.camel.language.simple.SimpleLanguage} expression.      */
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
DECL|method|getEndpointProducerBuilder ()
specifier|public
name|EndpointProducerBuilder
name|getEndpointProducerBuilder
parameter_list|()
block|{
return|return
name|endpointProducerBuilder
return|;
block|}
DECL|method|setEndpointProducerBuilder (EndpointProducerBuilder endpointProducerBuilder)
specifier|public
name|void
name|setEndpointProducerBuilder
parameter_list|(
name|EndpointProducerBuilder
name|endpointProducerBuilder
parameter_list|)
block|{
name|this
operator|.
name|endpointProducerBuilder
operator|=
name|endpointProducerBuilder
expr_stmt|;
block|}
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|setPattern (ExchangePattern pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|getCacheSize ()
specifier|public
name|Integer
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (Integer cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|Integer
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidEndpoint ()
specifier|public
name|Boolean
name|getIgnoreInvalidEndpoint
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoint
return|;
block|}
DECL|method|setIgnoreInvalidEndpoint (Boolean ignoreInvalidEndpoint)
specifier|public
name|void
name|setIgnoreInvalidEndpoint
parameter_list|(
name|Boolean
name|ignoreInvalidEndpoint
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoint
operator|=
name|ignoreInvalidEndpoint
expr_stmt|;
block|}
DECL|method|getAllowOptimisedComponents ()
specifier|public
name|Boolean
name|getAllowOptimisedComponents
parameter_list|()
block|{
return|return
name|allowOptimisedComponents
return|;
block|}
DECL|method|setAllowOptimisedComponents (Boolean allowOptimisedComponents)
specifier|public
name|void
name|setAllowOptimisedComponents
parameter_list|(
name|Boolean
name|allowOptimisedComponents
parameter_list|)
block|{
name|this
operator|.
name|allowOptimisedComponents
operator|=
name|allowOptimisedComponents
expr_stmt|;
block|}
block|}
end_class

end_unit

