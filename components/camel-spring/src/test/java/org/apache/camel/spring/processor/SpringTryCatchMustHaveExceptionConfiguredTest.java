begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
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
name|FailedToCreateRouteException
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
name|RuntimeCamelException
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|SpringTestHelper
operator|.
name|createSpringCamelContext
import|;
end_import

begin_class
DECL|class|SpringTryCatchMustHaveExceptionConfiguredTest
specifier|public
class|class
name|SpringTryCatchMustHaveExceptionConfiguredTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|createSpringCamelContext
argument_list|(
name|this
argument_list|,
literal|"org/apache/camel/spring/processor/SpringTryCatchMustHaveExceptionConfiguredTest.xml"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|FailedToCreateRouteException
name|ftcre
init|=
name|assertIsInstanceOf
argument_list|(
name|FailedToCreateRouteException
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|IllegalArgumentException
name|iae
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|ftcre
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"At least one Exception must be configured to catch"
argument_list|,
name|iae
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// return a working context instead, to let this test pass
return|return
name|createSpringCamelContext
argument_list|(
name|this
argument_list|,
literal|"org/apache/camel/spring/processor/convertBody.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testTryCatchMustHaveExceptionConfigured ()
specifier|public
name|void
name|testTryCatchMustHaveExceptionConfigured
parameter_list|()
block|{
comment|// noop
block|}
block|}
end_class

end_unit

