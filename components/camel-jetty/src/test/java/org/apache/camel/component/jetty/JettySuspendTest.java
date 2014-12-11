begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|component
operator|.
name|http
operator|.
name|HttpOperationFailedException
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JettySuspendTest
specifier|public
class|class
name|JettySuspendTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|serverUri
specifier|private
name|String
name|serverUri
init|=
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/cool"
decl_stmt|;
annotation|@
name|Test
DECL|method|testJettySuspend ()
specifier|public
name|void
name|testJettySuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
comment|// now suspend jetty
name|HttpConsumer
name|consumer
init|=
operator|(
name|HttpConsumer
operator|)
name|context
operator|.
name|getRoute
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
comment|// suspend
name|consumer
operator|.
name|suspend
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri
argument_list|,
literal|"Moon"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|503
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
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
name|from
argument_list|(
literal|"jetty://"
operator|+
name|serverUri
argument_list|)
operator|.
name|id
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Bye "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

