begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|NoTypeConversionAvailableException
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
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|CxfHeaderHelper
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
name|HeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|support
operator|.
name|ContextPropertiesMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * The binding/mapping of Camel messages to Apache CXF and back again  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfBinding
specifier|public
specifier|final
class|class
name|CxfBinding
block|{
DECL|method|CxfBinding ()
specifier|private
name|CxfBinding
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|extractBodyFromCxf (CxfExchange exchange, Message message)
specifier|public
specifier|static
name|Object
name|extractBodyFromCxf
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
comment|// TODO how do we choose a format?
return|return
name|getBody
argument_list|(
name|message
argument_list|)
return|;
block|}
DECL|method|getBody (Message message)
specifier|protected
specifier|static
name|Object
name|getBody
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|contentFormats
init|=
name|message
operator|.
name|getContentFormats
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentFormats
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|contentFormat
range|:
name|contentFormats
control|)
block|{
name|Object
name|answer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|contentFormat
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @deprecated please use {@link #createCxfMessage(HeaderFilterStrategy, CxfExchange)} instead      */
DECL|method|createCxfMessage (CxfExchange exchange)
specifier|public
specifier|static
name|Message
name|createCxfMessage
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|)
block|{
return|return
name|CxfBinding
operator|.
name|createCxfMessage
argument_list|(
operator|new
name|CxfHeaderFilterStrategy
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|createCxfMessage (HeaderFilterStrategy strategy, CxfExchange exchange)
specifier|public
specifier|static
name|Message
name|createCxfMessage
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|CxfExchange
name|exchange
parameter_list|)
block|{
name|Message
name|answer
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|CxfMessage
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// Check the body if the POJO parameter list first
try|try
block|{
name|List
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// just set the operation's parameter
name|answer
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|CxfHeaderHelper
operator|.
name|propagateCamelToCxf
argument_list|(
name|strategy
argument_list|,
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
comment|// CXF uses StAX which is based on the stream API to parse the XML,
comment|// so the CXF transport is also based on the stream API.
comment|// And the interceptors are also based on the stream API,
comment|// so let's use an InputStream to host the CXF on wire message.
try|try
block|{
name|InputStream
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex2
parameter_list|)
block|{
comment|// ignore
block|}
comment|// TODO do we propagate header the same way in non-POJO mode?
comment|// CxfHeaderHelper.propagateCamelToCxf(strategy, in.getHeaders(), answer);
block|}
comment|//Ensure there is a request context, which is needed by propogateContext() below
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|answer
operator|.
name|get
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestContext
operator|==
literal|null
condition|)
block|{
name|requestContext
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|requestContext
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//Allows other components to pass properties into cxf request context
name|requestContext
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|put
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * @deprecated please use {@link #storeCxfResponse(HeaderFilterStrategy, CxfExchange, Message)} instead.      */
DECL|method|storeCxfResponse (CxfExchange exchange, Message response)
specifier|public
specifier|static
name|void
name|storeCxfResponse
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Message
name|response
parameter_list|)
block|{
name|CxfBinding
operator|.
name|storeCxfResponse
argument_list|(
operator|new
name|CxfHeaderFilterStrategy
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
DECL|method|storeCxfResponse (HeaderFilterStrategy strategy, CxfExchange exchange, Message response)
specifier|public
specifier|static
name|void
name|storeCxfResponse
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|CxfExchange
name|exchange
parameter_list|,
name|Message
name|response
parameter_list|)
block|{
name|CxfMessage
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|CxfHeaderHelper
operator|.
name|propagateCxfToCamel
argument_list|(
name|strategy
argument_list|,
name|response
argument_list|,
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setMessage
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|DataFormat
name|dataFormat
init|=
operator|(
name|DataFormat
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|CxfExchange
operator|.
name|DATA_FORMAT
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|MESSAGE
argument_list|)
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|response
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @deprecated Please use {@link #copyMessage(HeaderFilterStrategy, org.apache.camel.Message, Message)} instead.      */
DECL|method|copyMessage (org.apache.camel.Message camelMessage, org.apache.cxf.message.Message cxfMessage)
specifier|public
specifier|static
name|void
name|copyMessage
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
parameter_list|)
block|{
name|CxfBinding
operator|.
name|copyMessage
argument_list|(
operator|new
name|CxfHeaderFilterStrategy
argument_list|()
argument_list|,
name|camelMessage
argument_list|,
name|cxfMessage
argument_list|)
expr_stmt|;
block|}
comment|// Copy the Camel message to CXF message
DECL|method|copyMessage (HeaderFilterStrategy strategy, org.apache.camel.Message camelMessage, org.apache.cxf.message.Message cxfMessage)
specifier|public
specifier|static
name|void
name|copyMessage
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
parameter_list|)
block|{
name|CxfHeaderHelper
operator|.
name|propagateCamelToCxf
argument_list|(
name|strategy
argument_list|,
name|camelMessage
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|cxfMessage
argument_list|)
expr_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|camelMessage
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|cxfMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
name|Object
name|result
init|=
name|camelMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|instanceof
name|InputStream
condition|)
block|{
name|cxfMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cxfMessage
operator|.
name|setContent
argument_list|(
name|result
operator|.
name|getClass
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|storeCXfResponseContext (Message response, Map<String, Object> context)
specifier|public
specifier|static
name|void
name|storeCXfResponseContext
parameter_list|(
name|Message
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|ContextPropertiesMapping
operator|.
name|mapResponsefromCxf2Jaxws
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|response
operator|.
name|put
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeCxfResponse (CxfExchange exchange, Object response)
specifier|public
specifier|static
name|void
name|storeCxfResponse
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Object
name|response
parameter_list|)
block|{
name|CxfMessage
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeCxfFault (CxfExchange exchange, Message message)
specifier|public
specifier|static
name|void
name|storeCxfFault
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|CxfMessage
name|fault
init|=
name|exchange
operator|.
name|getFault
argument_list|()
decl_stmt|;
if|if
condition|(
name|fault
operator|!=
literal|null
condition|)
block|{
name|fault
operator|.
name|setBody
argument_list|(
name|getBody
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|propogateContext (Message message, Map<String, Object> context)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|propogateContext
parameter_list|(
name|Message
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|message
operator|.
name|get
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|message
operator|.
name|get
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
comment|// TODO map the JAXWS properties to cxf
if|if
condition|(
name|requestContext
operator|!=
literal|null
condition|)
block|{
name|ContextPropertiesMapping
operator|.
name|mapRequestfromJaxws2Cxf
argument_list|(
name|requestContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|responseContext
operator|==
literal|null
condition|)
block|{
name|responseContext
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// clear the response context
name|responseContext
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|context
operator|.
name|put
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
return|return
name|responseContext
return|;
block|}
block|}
end_class

end_unit

