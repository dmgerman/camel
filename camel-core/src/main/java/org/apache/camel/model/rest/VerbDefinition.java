begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
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
name|XmlElements
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
name|model
operator|.
name|OptionalIdentifiedDefinition
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
name|RouteDefinition
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
name|ToDefinition
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
comment|/**  * Rest command  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"rest"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"verb"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|VerbDefinition
specifier|public
class|class
name|VerbDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|VerbDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|params
specifier|private
name|List
argument_list|<
name|RestOperationParamDefinition
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|RestOperationParamDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|responseMsgs
specifier|private
name|List
argument_list|<
name|RestOperationResponseMsgDefinition
argument_list|>
name|responseMsgs
init|=
operator|new
name|ArrayList
argument_list|<
name|RestOperationResponseMsgDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|consumes
specifier|private
name|String
name|consumes
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|produces
specifier|private
name|String
name|produces
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"auto"
argument_list|)
DECL|field|bindingMode
specifier|private
name|RestBindingMode
name|bindingMode
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|skipBindingOnErrorCode
specifier|private
name|Boolean
name|skipBindingOnErrorCode
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|enableCORS
specifier|private
name|Boolean
name|enableCORS
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|outType
specifier|private
name|String
name|outType
decl_stmt|;
comment|// used by XML DSL to either select a<to> or<route>
comment|// so we need to use the common type OptionalIdentifiedDefinition
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"to"
argument_list|,
name|type
operator|=
name|ToDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"route"
argument_list|,
name|type
operator|=
name|RouteDefinition
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|toOrRoute
specifier|private
name|OptionalIdentifiedDefinition
argument_list|<
name|?
argument_list|>
name|toOrRoute
decl_stmt|;
comment|// the Java DSL uses the to or route definition directory
annotation|@
name|XmlTransient
DECL|field|to
specifier|private
name|ToDefinition
name|to
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|route
specifier|private
name|RouteDefinition
name|route
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|rest
specifier|private
name|RestDefinition
name|rest
decl_stmt|;
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|method
return|;
block|}
else|else
block|{
return|return
literal|"verb"
return|;
block|}
block|}
DECL|method|getParams ()
specifier|public
name|List
argument_list|<
name|RestOperationParamDefinition
argument_list|>
name|getParams
parameter_list|()
block|{
return|return
name|params
return|;
block|}
comment|/**      * To specify the REST operation parameters using Swagger.      */
DECL|method|setParams (List<RestOperationParamDefinition> params)
specifier|public
name|void
name|setParams
parameter_list|(
name|List
argument_list|<
name|RestOperationParamDefinition
argument_list|>
name|params
parameter_list|)
block|{
name|this
operator|.
name|params
operator|=
name|params
expr_stmt|;
block|}
DECL|method|getResponseMsgs ()
specifier|public
name|List
argument_list|<
name|RestOperationResponseMsgDefinition
argument_list|>
name|getResponseMsgs
parameter_list|()
block|{
return|return
name|responseMsgs
return|;
block|}
comment|/**      * Sets swagger operation response messages      */
DECL|method|setResponseMsgs (List<RestOperationResponseMsgDefinition> params)
specifier|public
name|void
name|setResponseMsgs
parameter_list|(
name|List
argument_list|<
name|RestOperationResponseMsgDefinition
argument_list|>
name|params
parameter_list|)
block|{
name|this
operator|.
name|responseMsgs
operator|=
name|responseMsgs
expr_stmt|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
comment|/**      * The HTTP verb such as GET or POST      */
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
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
comment|/**      * Uri template of this REST service such as /{id}.      */
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
DECL|method|getConsumes ()
specifier|public
name|String
name|getConsumes
parameter_list|()
block|{
return|return
name|consumes
return|;
block|}
comment|/**      * To define the content type what the REST service consumes (accept as input), such as application/xml or application/json.      * This option will override what may be configured on a parent level      */
DECL|method|setConsumes (String consumes)
specifier|public
name|void
name|setConsumes
parameter_list|(
name|String
name|consumes
parameter_list|)
block|{
name|this
operator|.
name|consumes
operator|=
name|consumes
expr_stmt|;
block|}
DECL|method|getProduces ()
specifier|public
name|String
name|getProduces
parameter_list|()
block|{
return|return
name|produces
return|;
block|}
comment|/**      * To define the content type what the REST service produces (uses for output), such as application/xml or application/json      * This option will override what may be configured on a parent level      */
DECL|method|setProduces (String produces)
specifier|public
name|void
name|setProduces
parameter_list|(
name|String
name|produces
parameter_list|)
block|{
name|this
operator|.
name|produces
operator|=
name|produces
expr_stmt|;
block|}
DECL|method|getBindingMode ()
specifier|public
name|RestBindingMode
name|getBindingMode
parameter_list|()
block|{
return|return
name|bindingMode
return|;
block|}
comment|/**      * Sets the binding mode to use.      * This option will override what may be configured on a parent level      *<p/>      * The default value is auto      */
DECL|method|setBindingMode (RestBindingMode bindingMode)
specifier|public
name|void
name|setBindingMode
parameter_list|(
name|RestBindingMode
name|bindingMode
parameter_list|)
block|{
name|this
operator|.
name|bindingMode
operator|=
name|bindingMode
expr_stmt|;
block|}
DECL|method|getSkipBindingOnErrorCode ()
specifier|public
name|Boolean
name|getSkipBindingOnErrorCode
parameter_list|()
block|{
return|return
name|skipBindingOnErrorCode
return|;
block|}
comment|/**      * Whether to skip binding on output if there is a custom HTTP error code header.      * This allows to build custom error messages that do not bind to json / xml etc, as success messages otherwise will do.      * This option will override what may be configured on a parent level      */
DECL|method|setSkipBindingOnErrorCode (Boolean skipBindingOnErrorCode)
specifier|public
name|void
name|setSkipBindingOnErrorCode
parameter_list|(
name|Boolean
name|skipBindingOnErrorCode
parameter_list|)
block|{
name|this
operator|.
name|skipBindingOnErrorCode
operator|=
name|skipBindingOnErrorCode
expr_stmt|;
block|}
DECL|method|getEnableCORS ()
specifier|public
name|Boolean
name|getEnableCORS
parameter_list|()
block|{
return|return
name|enableCORS
return|;
block|}
comment|/**      * Whether to enable CORS headers in the HTTP response.      * This option will override what may be configured on a parent level      *<p/>      * The default value is false.      */
DECL|method|setEnableCORS (Boolean enableCORS)
specifier|public
name|void
name|setEnableCORS
parameter_list|(
name|Boolean
name|enableCORS
parameter_list|)
block|{
name|this
operator|.
name|enableCORS
operator|=
name|enableCORS
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Sets the class name to use for binding from input to POJO for the incoming data      * This option will override what may be configured on a parent level      */
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getOutType ()
specifier|public
name|String
name|getOutType
parameter_list|()
block|{
return|return
name|outType
return|;
block|}
comment|/**      * Sets the class name to use for binding from POJO to output for the outgoing data      * This option will override what may be configured on a parent level      */
DECL|method|setOutType (String outType)
specifier|public
name|void
name|setOutType
parameter_list|(
name|String
name|outType
parameter_list|)
block|{
name|this
operator|.
name|outType
operator|=
name|outType
expr_stmt|;
block|}
DECL|method|getRest ()
specifier|public
name|RestDefinition
name|getRest
parameter_list|()
block|{
return|return
name|rest
return|;
block|}
DECL|method|setRest (RestDefinition rest)
specifier|public
name|void
name|setRest
parameter_list|(
name|RestDefinition
name|rest
parameter_list|)
block|{
name|this
operator|.
name|rest
operator|=
name|rest
expr_stmt|;
block|}
DECL|method|getRoute ()
specifier|public
name|RouteDefinition
name|getRoute
parameter_list|()
block|{
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
return|return
name|route
return|;
block|}
elseif|else
if|if
condition|(
name|toOrRoute
operator|instanceof
name|RouteDefinition
condition|)
block|{
return|return
operator|(
name|RouteDefinition
operator|)
name|toOrRoute
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|setRoute (RouteDefinition route)
specifier|public
name|void
name|setRoute
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
block|{
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|toOrRoute
operator|=
name|route
expr_stmt|;
block|}
DECL|method|getTo ()
specifier|public
name|ToDefinition
name|getTo
parameter_list|()
block|{
if|if
condition|(
name|to
operator|!=
literal|null
condition|)
block|{
return|return
name|to
return|;
block|}
elseif|else
if|if
condition|(
name|toOrRoute
operator|instanceof
name|ToDefinition
condition|)
block|{
return|return
operator|(
name|ToDefinition
operator|)
name|toOrRoute
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|setTo (ToDefinition to)
specifier|public
name|void
name|setTo
parameter_list|(
name|ToDefinition
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
name|this
operator|.
name|toOrRoute
operator|=
name|to
expr_stmt|;
block|}
DECL|method|getToOrRoute ()
specifier|public
name|OptionalIdentifiedDefinition
argument_list|<
name|?
argument_list|>
name|getToOrRoute
parameter_list|()
block|{
return|return
name|toOrRoute
return|;
block|}
comment|/**      * To route from this REST service to a Camel endpoint, or an inlined route      */
DECL|method|setToOrRoute (OptionalIdentifiedDefinition<?> toOrRoute)
specifier|public
name|void
name|setToOrRoute
parameter_list|(
name|OptionalIdentifiedDefinition
argument_list|<
name|?
argument_list|>
name|toOrRoute
parameter_list|)
block|{
name|this
operator|.
name|toOrRoute
operator|=
name|toOrRoute
expr_stmt|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
DECL|method|get ()
specifier|public
name|RestDefinition
name|get
parameter_list|()
block|{
return|return
name|rest
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|get (String uri)
specifier|public
name|RestDefinition
name|get
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|rest
operator|.
name|get
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|post ()
specifier|public
name|RestDefinition
name|post
parameter_list|()
block|{
return|return
name|rest
operator|.
name|post
argument_list|()
return|;
block|}
DECL|method|post (String uri)
specifier|public
name|RestDefinition
name|post
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|rest
operator|.
name|post
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|put ()
specifier|public
name|RestDefinition
name|put
parameter_list|()
block|{
return|return
name|rest
operator|.
name|put
argument_list|()
return|;
block|}
DECL|method|put (String uri)
specifier|public
name|RestDefinition
name|put
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|rest
operator|.
name|put
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|delete ()
specifier|public
name|RestDefinition
name|delete
parameter_list|()
block|{
return|return
name|rest
operator|.
name|delete
argument_list|()
return|;
block|}
DECL|method|delete (String uri)
specifier|public
name|RestDefinition
name|delete
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|rest
operator|.
name|delete
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|head ()
specifier|public
name|RestDefinition
name|head
parameter_list|()
block|{
return|return
name|rest
operator|.
name|head
argument_list|()
return|;
block|}
DECL|method|head (String uri)
specifier|public
name|RestDefinition
name|head
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|rest
operator|.
name|head
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|verb (String verb)
specifier|public
name|RestDefinition
name|verb
parameter_list|(
name|String
name|verb
parameter_list|)
block|{
return|return
name|rest
operator|.
name|verb
argument_list|(
name|verb
argument_list|)
return|;
block|}
DECL|method|verb (String verb, String uri)
specifier|public
name|RestDefinition
name|verb
parameter_list|(
name|String
name|verb
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
return|return
name|rest
operator|.
name|verb
argument_list|(
name|verb
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|asVerb ()
specifier|public
name|String
name|asVerb
parameter_list|()
block|{
comment|// we do not want the jaxb model to repeat itself, by outputting<get method="get">
comment|// so we defer the verb from the instance type
if|if
condition|(
name|this
operator|instanceof
name|GetVerbDefinition
condition|)
block|{
return|return
literal|"get"
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|instanceof
name|PostVerbDefinition
condition|)
block|{
return|return
literal|"post"
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|instanceof
name|PutVerbDefinition
condition|)
block|{
return|return
literal|"put"
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|instanceof
name|DeleteVerbDefinition
condition|)
block|{
return|return
literal|"delete"
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|instanceof
name|HeadVerbDefinition
condition|)
block|{
return|return
literal|"head"
return|;
block|}
else|else
block|{
return|return
name|method
return|;
block|}
block|}
block|}
end_class

end_unit

