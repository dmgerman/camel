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
name|ComponentVerifier
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
name|Consumer
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
name|ContextTestSupport
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
name|VerifiableComponent
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
name|DefaultComponent
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
name|JndiRegistry
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
name|verifier
operator|.
name|ResultBuilder
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
name|verifier
operator|.
name|ResultErrorHelper
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
name|RestConsumerFactory
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
DECL|class|RestComponentVerifierTest
specifier|public
class|class
name|RestComponentVerifierTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"rest"
argument_list|,
operator|new
name|RestComponent
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"rest-component"
argument_list|,
operator|new
name|MyComponent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|method|testParameters ()
specifier|public
name|void
name|testParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|RestComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"rest"
argument_list|,
name|RestComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|RestComponentVerifier
name|verifier
init|=
operator|(
name|RestComponentVerifier
operator|)
name|component
operator|.
name|getVerifier
argument_list|()
decl_stmt|;
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
literal|"componentName"
argument_list|,
literal|"rest-component"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"http://localhost:1234"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"verify"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"method"
argument_list|,
literal|"get"
argument_list|)
expr_stmt|;
comment|// This parameter does not belong to the rest component and validation
comment|// is delegated to the underlying component
name|parameters
operator|.
name|put
argument_list|(
literal|"authProxy"
argument_list|,
literal|"http://localhost:8080"
argument_list|)
expr_stmt|;
name|ComponentVerifier
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMissingParameters ()
specifier|public
name|void
name|testMissingParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|RestComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"rest"
argument_list|,
name|RestComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|RestComponentVerifier
name|verifier
init|=
operator|(
name|RestComponentVerifier
operator|)
name|component
operator|.
name|getVerifier
argument_list|()
decl_stmt|;
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
literal|"componentName"
argument_list|,
literal|"rest-component"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"http://localhost:"
operator|+
literal|1234
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"verify"
argument_list|)
expr_stmt|;
comment|// This parameter does not belong to the rest component and validation
comment|// is delegated to the underlying component
name|parameters
operator|.
name|put
argument_list|(
literal|"authProxy"
argument_list|,
literal|"http://localhost:8080"
argument_list|)
expr_stmt|;
name|ComponentVerifier
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|ERROR
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|StandardCode
operator|.
name|MISSING_PARAMETER
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"method"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ***************************************************
comment|//
comment|// ***************************************************
DECL|class|MyComponent
specifier|private
specifier|final
class|class
name|MyComponent
extends|extends
name|DefaultComponent
implements|implements
name|RestProducerFactory
implements|,
name|RestConsumerFactory
implements|,
name|VerifiableComponent
block|{
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createProducer ( CamelContext camelContext, String host, String verb, String basePath, String uriTemplate, String queryParameters, String consumes, String produces, Map<String, Object> parameters)
specifier|public
name|Producer
name|createProducer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|host
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|queryParameters
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer ( CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getVerifier ()
specifier|public
name|ComponentVerifier
name|getVerifier
parameter_list|()
block|{
return|return
parameter_list|(
name|scope
parameter_list|,
name|parameters
parameter_list|)
lambda|->
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|scope
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"authProxy"
argument_list|,
name|parameters
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

