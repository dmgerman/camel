begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|Exchange
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
name|impl
operator|.
name|DefaultAsyncProducer
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
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|CollectionStringBuffer
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
name|AsyncProcessorConverterHelper
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
name|FileUtil
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
name|ServiceHelper
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Rest producer for calling remote REST services.  */
end_comment

begin_class
DECL|class|RestProducer
specifier|public
class|class
name|RestProducer
extends|extends
name|DefaultAsyncProducer
block|{
comment|// the producer of the Camel component that is used as the HTTP client to call the REST service
DECL|field|producer
specifier|private
name|AsyncProcessor
name|producer
decl_stmt|;
DECL|field|prepareUriTemplate
specifier|private
name|boolean
name|prepareUriTemplate
init|=
literal|true
decl_stmt|;
DECL|method|RestProducer (Endpoint endpoint, Producer producer)
specifier|public
name|RestProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// TODO: request bind to consumes context-type
comment|// TODO: response bind to content-type returned in response
comment|// TODO: binding
try|try
block|{
name|prepareExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|RestEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RestEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|isPrepareUriTemplate ()
specifier|public
name|boolean
name|isPrepareUriTemplate
parameter_list|()
block|{
return|return
name|prepareUriTemplate
return|;
block|}
comment|/**      * Whether to prepare the uri template and replace {key} with values from the exchange, and set      * as {@link Exchange#HTTP_URI} header with the resolved uri to use instead of uri from endpoint.      */
DECL|method|setPrepareUriTemplate (boolean prepareUriTemplate)
specifier|public
name|void
name|setPrepareUriTemplate
parameter_list|(
name|boolean
name|prepareUriTemplate
parameter_list|)
block|{
name|this
operator|.
name|prepareUriTemplate
operator|=
name|prepareUriTemplate
expr_stmt|;
block|}
DECL|method|prepareExchange (Exchange exchange)
specifier|protected
name|void
name|prepareExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|hasPath
init|=
literal|false
decl_stmt|;
comment|// uri template with path parameters resolved
comment|// uri template may be optional and the user have entered the uri template in the path instead
name|String
name|resolvedUriTemplate
init|=
name|getEndpoint
argument_list|()
operator|.
name|getUriTemplate
argument_list|()
operator|!=
literal|null
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getUriTemplate
argument_list|()
else|:
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|prepareUriTemplate
condition|)
block|{
if|if
condition|(
name|resolvedUriTemplate
operator|.
name|contains
argument_list|(
literal|"{"
argument_list|)
condition|)
block|{
comment|// resolve template and replace {key} with the values form the exchange
comment|// each {} is a parameter (url templating)
name|String
index|[]
name|arr
init|=
name|resolvedUriTemplate
operator|.
name|split
argument_list|(
literal|"\\/"
argument_list|)
decl_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|a
range|:
name|arr
control|)
block|{
if|if
condition|(
name|a
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|a
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|a
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|a
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|hasPath
operator|=
literal|true
expr_stmt|;
name|csb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|csb
operator|.
name|append
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|csb
operator|.
name|append
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
name|resolvedUriTemplate
operator|=
name|csb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
comment|// resolve uri parameters
name|String
name|query
init|=
name|getEndpoint
argument_list|()
operator|.
name|getQueryParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|v
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|String
name|a
init|=
name|v
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// decode the key as { may be decoded to %NN
name|a
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|a
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
if|if
condition|(
name|a
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|a
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|a
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|a
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|params
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|params
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|query
operator|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
comment|// the query parameters for the rest call to be used
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REST_HTTP_QUERY
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasPath
condition|)
block|{
name|String
name|host
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|String
name|basePath
init|=
name|getEndpoint
argument_list|()
operator|.
name|getUriTemplate
argument_list|()
operator|!=
literal|null
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
else|:
literal|null
decl_stmt|;
name|basePath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
name|resolvedUriTemplate
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|resolvedUriTemplate
argument_list|)
expr_stmt|;
comment|// if so us a header for the dynamic uri template so we reuse same endpoint but the header overrides the actual url to use
name|String
name|overrideUri
decl_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
condition|)
block|{
name|overrideUri
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s/%s"
argument_list|,
name|host
argument_list|,
name|basePath
argument_list|,
name|resolvedUriTemplate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|overrideUri
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s"
argument_list|,
name|host
argument_list|,
name|resolvedUriTemplate
argument_list|)
expr_stmt|;
block|}
comment|// the http uri for the rest call to be used
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REST_HTTP_URI
argument_list|,
name|overrideUri
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

