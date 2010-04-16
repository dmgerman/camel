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
name|CamelServlet
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
name|util
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
name|ObjectHelper
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
DECL|field|camelServlet
specifier|private
name|CamelServlet
name|camelServlet
decl_stmt|;
DECL|method|setCamelServlet (CamelServlet servlet)
specifier|public
name|void
name|setCamelServlet
parameter_list|(
name|CamelServlet
name|servlet
parameter_list|)
block|{
name|camelServlet
operator|=
name|servlet
expr_stmt|;
block|}
DECL|method|getCamelServlet (String servletName)
specifier|public
name|CamelServlet
name|getCamelServlet
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|CamelServlet
name|answer
decl_stmt|;
if|if
condition|(
name|camelServlet
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|CamelHttpTransportServlet
operator|.
name|getCamelServlet
argument_list|(
name|servletName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|camelServlet
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find the deployed servlet, please configure the ServletComponent"
operator|+
literal|" or configure a org.apache.camel.component.servlet.CamelHttpTransportServlet servlet in web.xml "
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
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
name|uri
operator|=
name|uri
operator|.
name|startsWith
argument_list|(
literal|"servlet:"
argument_list|)
condition|?
name|remaining
else|:
name|uri
expr_stmt|;
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
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
name|uri
operator|=
name|httpUri
operator|.
name|toString
argument_list|()
expr_stmt|;
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
name|setEndpointHeaderFilterStrategy
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
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
name|ServletEndpoint
name|endpoint
init|=
operator|(
name|ServletEndpoint
operator|)
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|CamelServlet
name|servlet
init|=
name|getCamelServlet
argument_list|(
name|endpoint
operator|.
name|getServletName
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|servlet
argument_list|,
literal|"CamelServlet"
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
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
name|ServletEndpoint
name|endpoint
init|=
operator|(
name|ServletEndpoint
operator|)
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|CamelServlet
name|servlet
init|=
name|getCamelServlet
argument_list|(
name|endpoint
operator|.
name|getServletName
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|servlet
argument_list|,
literal|"CamelServlet"
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|disconnect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

