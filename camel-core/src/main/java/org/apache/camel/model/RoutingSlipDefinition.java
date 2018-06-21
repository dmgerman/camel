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
name|Collections
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
name|AsyncProcessor
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
name|ErrorHandlerFactory
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
name|Expression
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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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
name|model
operator|.
name|language
operator|.
name|HeaderExpression
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
name|RoutingSlip
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
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Routes a message through a series of steps that are pre-determined (the slip)  */
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
literal|"routingSlip"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RoutingSlipDefinition
specifier|public
class|class
name|RoutingSlipDefinition
parameter_list|<
name|Type
extends|extends
name|ProcessorDefinition
parameter_list|<
name|Type
parameter_list|>
parameter_list|>
extends|extends
name|NoOutputExpressionNode
block|{
DECL|field|DEFAULT_DELIMITER
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DELIMITER
init|=
literal|","
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|","
argument_list|)
DECL|field|uriDelimiter
specifier|private
name|String
name|uriDelimiter
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreInvalidEndpoints
specifier|private
name|Boolean
name|ignoreInvalidEndpoints
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|cacheSize
specifier|private
name|Integer
name|cacheSize
decl_stmt|;
DECL|method|RoutingSlipDefinition ()
specifier|public
name|RoutingSlipDefinition
parameter_list|()
block|{
name|this
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
name|DEFAULT_DELIMITER
argument_list|)
expr_stmt|;
block|}
DECL|method|RoutingSlipDefinition (String headerName)
specifier|public
name|RoutingSlipDefinition
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
argument_list|(
name|headerName
argument_list|,
name|DEFAULT_DELIMITER
argument_list|)
expr_stmt|;
block|}
DECL|method|RoutingSlipDefinition (String headerName, String uriDelimiter)
specifier|public
name|RoutingSlipDefinition
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|uriDelimiter
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|HeaderExpression
argument_list|(
name|headerName
argument_list|)
argument_list|)
expr_stmt|;
name|setUriDelimiter
argument_list|(
name|uriDelimiter
argument_list|)
expr_stmt|;
block|}
DECL|method|RoutingSlipDefinition (Expression expression, String uriDelimiter)
specifier|public
name|RoutingSlipDefinition
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|String
name|uriDelimiter
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|setUriDelimiter
argument_list|(
name|uriDelimiter
argument_list|)
expr_stmt|;
block|}
DECL|method|RoutingSlipDefinition (Expression expression)
specifier|public
name|RoutingSlipDefinition
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
argument_list|(
name|expression
argument_list|,
name|DEFAULT_DELIMITER
argument_list|)
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
literal|"RoutingSlip["
operator|+
name|getExpression
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
literal|"routingSlip"
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
literal|"routingSlip["
operator|+
name|getExpression
argument_list|()
operator|+
literal|"]"
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
name|Expression
name|expression
init|=
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|String
name|delimiter
init|=
name|getUriDelimiter
argument_list|()
operator|!=
literal|null
condition|?
name|getUriDelimiter
argument_list|()
else|:
name|DEFAULT_DELIMITER
decl_stmt|;
name|RoutingSlip
name|routingSlip
init|=
operator|new
name|RoutingSlip
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|expression
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
if|if
condition|(
name|getIgnoreInvalidEndpoints
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|routingSlip
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|getIgnoreInvalidEndpoints
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getCacheSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|routingSlip
operator|.
name|setCacheSize
argument_list|(
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// and wrap this in an error handler
name|ErrorHandlerFactory
name|builder
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
comment|// create error handler (create error handler directly to keep it light weight,
comment|// instead of using ProcessorDefinition.wrapInErrorHandler)
name|AsyncProcessor
name|errorHandler
init|=
operator|(
name|AsyncProcessor
operator|)
name|builder
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|routingSlip
operator|.
name|newRoutingSlipProcessorForErrorHandler
argument_list|()
argument_list|)
decl_stmt|;
name|routingSlip
operator|.
name|setErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
return|return
name|routingSlip
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
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * Expression to define the routing slip, which defines which endpoints to route the message in a pipeline style.      * Notice the expression is evaluated once, if you want a more dynamic style, then the dynamic router eip is a better choice.      */
annotation|@
name|Override
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// override to include javadoc what the expression is used for
name|super
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|setUriDelimiter (String uriDelimiter)
specifier|public
name|void
name|setUriDelimiter
parameter_list|(
name|String
name|uriDelimiter
parameter_list|)
block|{
name|this
operator|.
name|uriDelimiter
operator|=
name|uriDelimiter
expr_stmt|;
block|}
DECL|method|getUriDelimiter ()
specifier|public
name|String
name|getUriDelimiter
parameter_list|()
block|{
return|return
name|uriDelimiter
return|;
block|}
DECL|method|setIgnoreInvalidEndpoints (Boolean ignoreInvalidEndpoints)
specifier|public
name|void
name|setIgnoreInvalidEndpoints
parameter_list|(
name|Boolean
name|ignoreInvalidEndpoints
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoints
operator|=
name|ignoreInvalidEndpoints
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidEndpoints ()
specifier|public
name|Boolean
name|getIgnoreInvalidEndpoints
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoints
return|;
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
comment|// Fluent API
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|end ()
specifier|public
name|Type
name|end
parameter_list|()
block|{
comment|// allow end() to return to previous type so you can continue in the DSL
return|return
operator|(
name|Type
operator|)
name|super
operator|.
name|end
argument_list|()
return|;
block|}
comment|/**      * Ignore the invalidate endpoint exception when try to create a producer with that endpoint      *      * @return the builder      */
DECL|method|ignoreInvalidEndpoints ()
specifier|public
name|RoutingSlipDefinition
argument_list|<
name|Type
argument_list|>
name|ignoreInvalidEndpoints
parameter_list|()
block|{
name|setIgnoreInvalidEndpoints
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the uri delimiter to use      *      * @param uriDelimiter the delimiter      * @return the builder      */
DECL|method|uriDelimiter (String uriDelimiter)
specifier|public
name|RoutingSlipDefinition
argument_list|<
name|Type
argument_list|>
name|uriDelimiter
parameter_list|(
name|String
name|uriDelimiter
parameter_list|)
block|{
name|setUriDelimiter
argument_list|(
name|uriDelimiter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum size used by the {@link org.apache.camel.impl.ProducerCache} which is used      * to cache and reuse producers when using this routing slip, when uris are reused.      *      * @param cacheSize  the cache size, use<tt>0</tt> for default cache size, or<tt>-1</tt> to turn cache off.      * @return the builder      */
DECL|method|cacheSize (int cacheSize)
specifier|public
name|RoutingSlipDefinition
argument_list|<
name|Type
argument_list|>
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
block|}
end_class

end_unit

