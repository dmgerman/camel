begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.webhook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|webhook
package|;
end_package

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
name|CamelExecutionException
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
name|RoutesBuilder
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
name|RouteBuilder
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
name|webhook
operator|.
name|support
operator|.
name|TestComponent
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
name|Registry
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

begin_class
DECL|class|WebhookBasePathTest
specifier|public
class|class
name|WebhookBasePathTest
extends|extends
name|WebhookTestBase
block|{
annotation|@
name|Test
DECL|method|testComponentPath ()
specifier|public
name|void
name|testComponentPath
parameter_list|()
block|{
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/base/uri0"
argument_list|,
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"msg: webhook"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUriPath ()
specifier|public
name|void
name|testUriPath
parameter_list|()
block|{
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/base/uri"
argument_list|,
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"uri: webhook"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAutoPath ()
specifier|public
name|void
name|testAutoPath
parameter_list|()
block|{
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/base"
operator|+
name|WebhookConfiguration
operator|.
name|computeDefaultPath
argument_list|(
literal|"wb-delegate://auto"
argument_list|)
argument_list|,
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"auto: webhook"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelExecutionException
operator|.
name|class
argument_list|)
DECL|method|testRootPathError ()
specifier|public
name|void
name|testRootPathError
parameter_list|()
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
argument_list|,
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelExecutionException
operator|.
name|class
argument_list|)
DECL|method|testRootBasePathError ()
specifier|public
name|void
name|testRootBasePathError
parameter_list|()
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/base/"
argument_list|,
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|public
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|WebhookComponent
name|comp
init|=
operator|(
name|WebhookComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"webhook"
argument_list|)
decl_stmt|;
name|comp
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setWebhookBasePath
argument_list|(
literal|"/base"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"wb-delegate-component"
argument_list|,
operator|new
name|TestComponent
argument_list|(
name|endpoint
lambda|->
block|{
name|endpoint
operator|.
name|setWebhookHandler
argument_list|(
name|proc
lambda|->
name|ex
lambda|->
block|{
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"webhook"
argument_list|)
argument_list|;
name|proc
operator|.
name|process
argument_list|(
name|ex
argument_list|)
argument_list|;
block|}
argument_list|)
argument_list|;
block|}
block|)
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}      @
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|restConfiguration
argument_list|()
operator|.
name|host
argument_list|(
literal|"0.0.0.0"
argument_list|)
operator|.
name|port
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"webhook:wb-delegate://xx?webhookPath=uri0"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"msg: "
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"webhook:wb-delegate://xx?webhookPath=/uri"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"uri: "
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"webhook:wb-delegate://auto"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"auto: "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
end_function

unit|}
end_unit
