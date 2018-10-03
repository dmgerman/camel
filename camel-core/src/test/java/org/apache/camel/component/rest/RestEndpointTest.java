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
name|CamelContext
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
name|DefaultCamelContext
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
name|rest
operator|.
name|RestBindingMode
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
name|RestConfiguration
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
name|RestProducerFactory
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|RestEndpointTest
specifier|public
class|class
name|RestEndpointTest
block|{
DECL|class|MockRest
specifier|public
specifier|static
class|class
name|MockRest
extends|extends
name|DefaultComponent
implements|implements
name|RestProducerFactory
block|{
annotation|@
name|Override
DECL|method|createProducer (final CamelContext camelContext, final String host, final String verb, final String basePath, final String uriTemplate, final String queryParameters, final String consumes, final String produces, RestConfiguration configuration, final Map<String, Object> parameters)
specifier|public
name|Producer
name|createProducer
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|verb
parameter_list|,
specifier|final
name|String
name|basePath
parameter_list|,
specifier|final
name|String
name|uriTemplate
parameter_list|,
specifier|final
name|String
name|queryParameters
parameter_list|,
specifier|final
name|String
name|consumes
parameter_list|,
specifier|final
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
specifier|final
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
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
return|return
literal|null
return|;
block|}
block|}
DECL|field|restComponent
name|RestComponent
name|restComponent
decl_stmt|;
DECL|field|context
name|CamelContext
name|context
decl_stmt|;
DECL|method|RestEndpointTest ()
specifier|public
name|RestEndpointTest
parameter_list|()
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"mock-rest"
argument_list|,
operator|new
name|MockRest
argument_list|()
argument_list|)
expr_stmt|;
name|restComponent
operator|=
operator|new
name|RestComponent
argument_list|()
expr_stmt|;
name|restComponent
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureBindingMode ()
specifier|public
name|void
name|shouldConfigureBindingMode
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|RestEndpoint
name|restEndpoint
init|=
operator|new
name|RestEndpoint
argument_list|(
literal|"rest:GET:/path"
argument_list|,
name|restComponent
argument_list|)
decl_stmt|;
name|restEndpoint
operator|.
name|setComponentName
argument_list|(
literal|"mock-rest"
argument_list|)
expr_stmt|;
name|restEndpoint
operator|.
name|setParameters
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|restEndpoint
operator|.
name|setHost
argument_list|(
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|restEndpoint
operator|.
name|setBindingMode
argument_list|(
name|RestBindingMode
operator|.
name|json
argument_list|)
expr_stmt|;
specifier|final
name|RestProducer
name|producer
init|=
operator|(
name|RestProducer
operator|)
name|restEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|producer
operator|.
name|getBindingMode
argument_list|()
argument_list|,
name|RestBindingMode
operator|.
name|json
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreateQueryParametersFromUnusedEndpointParameters ()
specifier|public
name|void
name|shouldCreateQueryParametersFromUnusedEndpointParameters
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"bindingMode"
argument_list|,
literal|"json"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
specifier|final
name|RestEndpoint
name|endpoint
init|=
operator|(
name|RestEndpoint
operator|)
name|restComponent
operator|.
name|createEndpoint
argument_list|(
literal|"rest:GET:/path?host=http://localhost&bindingMode=json&foo=bar"
argument_list|,
literal|"GET:/path"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost"
argument_list|,
name|endpoint
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RestBindingMode
operator|.
name|json
argument_list|,
name|endpoint
operator|.
name|getBindingMode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|endpoint
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/path"
argument_list|,
name|endpoint
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo=bar"
argument_list|,
name|endpoint
operator|.
name|getQueryParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportQueryParametersSetViaEndpointUri ()
specifier|public
name|void
name|shouldSupportQueryParametersSetViaEndpointUri
parameter_list|()
throws|throws
name|Exception
block|{
name|RestEndpoint
name|endpoint
init|=
operator|(
name|RestEndpoint
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"rest"
argument_list|)
operator|.
name|createEndpoint
argument_list|(
literal|"rest:GET:/path?host=http://localhost&bindingMode=json&foo=bar&queryParameters=RAW(a%3Db%26c%3Dd)"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost"
argument_list|,
name|endpoint
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RestBindingMode
operator|.
name|json
argument_list|,
name|endpoint
operator|.
name|getBindingMode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|endpoint
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/path"
argument_list|,
name|endpoint
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo=bar&a=b&c=d"
argument_list|,
name|endpoint
operator|.
name|getQueryParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

