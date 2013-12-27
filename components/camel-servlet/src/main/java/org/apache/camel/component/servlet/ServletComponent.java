begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|component
operator|.
name|http
operator|.
name|AuthMethod
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
name|http
operator|.
name|HttpBinding
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
name|http
operator|.
name|HttpClientConfigurer
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
name|http
operator|.
name|HttpComponent
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
name|http
operator|.
name|HttpConsumer
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
name|camel
operator|.
name|util
operator|.
name|IntrospectionSupport
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|params
operator|.
name|HttpClientParams
import|;
end_import

begin_class
DECL|class|ServletComponent
specifier|public
class|class
name|ServletComponent
extends|extends
name|HttpComponent
block|{
DECL|field|servletName
specifier|private
name|String
name|servletName
init|=
literal|"CamelServlet"
decl_stmt|;
DECL|field|httpRegistry
specifier|private
name|HttpRegistry
name|httpRegistry
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpClientParams
name|params
init|=
operator|new
name|HttpClientParams
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|params
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
comment|// create the configurer to use for this endpoint
specifier|final
name|Set
argument_list|<
name|AuthMethod
argument_list|>
name|authMethods
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|AuthMethod
argument_list|>
argument_list|()
decl_stmt|;
name|HttpClientConfigurer
name|configurer
init|=
name|createHttpClientConfigurer
argument_list|(
name|parameters
argument_list|,
name|authMethods
argument_list|)
decl_stmt|;
comment|// must extract well known parameters before we create the endpoint
name|Boolean
name|throwExceptionOnFailure
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"throwExceptionOnFailure"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|transferException
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"transferException"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|bridgeEndpoint
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"bridgeEndpoint"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpBinding
name|binding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpBindingRef"
argument_list|,
name|HttpBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|matchOnUriPrefix
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"matchOnUriPrefix"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|servletName
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"servletName"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|getServletName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|httpMethodRestrict
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpMethodRestrict"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"headerFilterStrategy"
argument_list|,
name|HeaderFilterStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// restructure uri to be based on the parameters left as we dont want to include the Camel internal options
name|URI
name|httpUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|uri
argument_list|)
argument_list|)
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|ServletEndpoint
name|endpoint
init|=
name|createServletEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|httpUri
argument_list|,
name|params
argument_list|,
name|getHttpConnectionManager
argument_list|()
argument_list|,
name|configurer
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setServletName
argument_list|(
name|servletName
argument_list|)
expr_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setEndpointHeaderFilterStrategy
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|// prefer to use endpoint configured over component configured
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
comment|// fallback to component configured
name|binding
operator|=
name|getHttpBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
comment|// should we use an exception for failed error codes?
if|if
condition|(
name|throwExceptionOnFailure
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setThrowExceptionOnFailure
argument_list|(
name|throwExceptionOnFailure
argument_list|)
expr_stmt|;
block|}
comment|// should we transfer exception as serialized object
if|if
condition|(
name|transferException
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setTransferException
argument_list|(
name|transferException
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bridgeEndpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBridgeEndpoint
argument_list|(
name|bridgeEndpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|matchOnUriPrefix
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setMatchOnUriPrefix
argument_list|(
name|matchOnUriPrefix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpMethodRestrict
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setHttpMethodRestrict
argument_list|(
name|httpMethodRestrict
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Strategy to create the servlet endpoint.      */
DECL|method|createServletEndpoint (String endpointUri, ServletComponent component, URI httpUri, HttpClientParams params, HttpConnectionManager httpConnectionManager, HttpClientConfigurer clientConfigurer)
specifier|protected
name|ServletEndpoint
name|createServletEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ServletComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|,
name|HttpClientParams
name|params
parameter_list|,
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|,
name|HttpClientConfigurer
name|clientConfigurer
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ServletEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|httpUri
argument_list|,
name|params
argument_list|,
name|httpConnectionManager
argument_list|,
name|clientConfigurer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|ServletConsumer
name|sc
init|=
operator|(
name|ServletConsumer
operator|)
name|consumer
decl_stmt|;
name|String
name|name
init|=
name|sc
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getServletName
argument_list|()
decl_stmt|;
name|HttpRegistry
name|registry
init|=
name|httpRegistry
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|DefaultHttpRegistry
operator|.
name|getHttpRegistry
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|registry
operator|.
name|register
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|ServletConsumer
name|sc
init|=
operator|(
name|ServletConsumer
operator|)
name|consumer
decl_stmt|;
name|String
name|name
init|=
name|sc
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getServletName
argument_list|()
decl_stmt|;
name|HttpRegistry
name|registry
init|=
name|httpRegistry
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|DefaultHttpRegistry
operator|.
name|getHttpRegistry
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|registry
operator|.
name|unregister
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|getServletName ()
specifier|public
name|String
name|getServletName
parameter_list|()
block|{
return|return
name|servletName
return|;
block|}
DECL|method|setServletName (String servletName)
specifier|public
name|void
name|setServletName
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|this
operator|.
name|servletName
operator|=
name|servletName
expr_stmt|;
block|}
DECL|method|getHttpRegistry ()
specifier|public
name|HttpRegistry
name|getHttpRegistry
parameter_list|()
block|{
return|return
name|httpRegistry
return|;
block|}
DECL|method|setHttpRegistry (HttpRegistry httpRegistry)
specifier|public
name|void
name|setHttpRegistry
parameter_list|(
name|HttpRegistry
name|httpRegistry
parameter_list|)
block|{
name|this
operator|.
name|httpRegistry
operator|=
name|httpRegistry
expr_stmt|;
block|}
block|}
end_class

end_unit

