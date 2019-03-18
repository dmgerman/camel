begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.exceptionpolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|exceptionpolicy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ConnectException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AlreadyStoppedException
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
name|CamelExchangeException
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
name|ExchangeTimedOutException
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
name|apache
operator|.
name|camel
operator|.
name|ValidationException
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
name|OnExceptionDefinition
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

begin_comment
comment|/**  * Unit test for DefaultExceptionPolicy   */
end_comment

begin_class
DECL|class|DefaultExceptionPolicyStrategyTest
specifier|public
class|class
name|DefaultExceptionPolicyStrategyTest
extends|extends
name|Assert
block|{
DECL|field|strategy
specifier|private
name|DefaultExceptionPolicyStrategy
name|strategy
decl_stmt|;
DECL|field|policies
specifier|private
name|HashMap
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|OnExceptionDefinition
argument_list|>
name|policies
decl_stmt|;
DECL|field|type1
specifier|private
name|OnExceptionDefinition
name|type1
decl_stmt|;
DECL|field|type2
specifier|private
name|OnExceptionDefinition
name|type2
decl_stmt|;
DECL|field|type3
specifier|private
name|OnExceptionDefinition
name|type3
decl_stmt|;
DECL|method|setupPolicies ()
specifier|private
name|void
name|setupPolicies
parameter_list|()
block|{
name|strategy
operator|=
operator|new
name|DefaultExceptionPolicyStrategy
argument_list|()
expr_stmt|;
name|policies
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|type1
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|)
expr_stmt|;
name|type2
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
name|type3
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|CamelExchangeException
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type1
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|Exception
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type2
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|IOException
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type3
argument_list|)
expr_stmt|;
block|}
DECL|method|setupPoliciesNoTopLevelException ()
specifier|private
name|void
name|setupPoliciesNoTopLevelException
parameter_list|()
block|{
comment|// without the top level exception that can be used as fallback
name|strategy
operator|=
operator|new
name|DefaultExceptionPolicyStrategy
argument_list|()
expr_stmt|;
name|policies
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|type1
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|)
expr_stmt|;
name|type3
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|CamelExchangeException
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type1
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|IOException
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type3
argument_list|)
expr_stmt|;
block|}
DECL|method|setupPoliciesCausedBy ()
specifier|private
name|void
name|setupPoliciesCausedBy
parameter_list|()
block|{
name|strategy
operator|=
operator|new
name|DefaultExceptionPolicyStrategy
argument_list|()
expr_stmt|;
name|policies
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|type1
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|FileNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|type2
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|ConnectException
operator|.
name|class
argument_list|)
expr_stmt|;
name|type3
operator|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|FileNotFoundException
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type1
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|IOException
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type2
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
operator|new
name|ExceptionPolicyKey
argument_list|(
literal|null
argument_list|,
name|ConnectException
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|,
name|type3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDirectMatch1 ()
specifier|public
name|void
name|testDirectMatch1
parameter_list|()
block|{
name|setupPolicies
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|CamelExchangeException
argument_list|(
literal|""
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type1
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDirectMatch2 ()
specifier|public
name|void
name|testDirectMatch2
parameter_list|()
block|{
name|setupPolicies
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|Exception
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type2
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDirectMatch3 ()
specifier|public
name|void
name|testDirectMatch3
parameter_list|()
block|{
name|setupPolicies
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|IOException
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type3
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClosetMatch3 ()
specifier|public
name|void
name|testClosetMatch3
parameter_list|()
block|{
name|setupPolicies
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|ConnectException
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type3
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|SocketException
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type3
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|FileNotFoundException
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type3
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClosetMatch2 ()
specifier|public
name|void
name|testClosetMatch2
parameter_list|()
block|{
name|setupPolicies
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|ClassCastException
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type2
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|NumberFormatException
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type2
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|NullPointerException
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type2
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClosetMatch1 ()
specifier|public
name|void
name|testClosetMatch1
parameter_list|()
block|{
name|setupPolicies
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|ValidationException
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type1
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|ExchangeTimedOutException
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type1
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoMatch1ThenMatchingJustException ()
specifier|public
name|void
name|testNoMatch1ThenMatchingJustException
parameter_list|()
block|{
name|setupPolicies
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|AlreadyStoppedException
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type2
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoMatch1ThenNull ()
specifier|public
name|void
name|testNoMatch1ThenNull
parameter_list|()
block|{
name|setupPoliciesNoTopLevelException
argument_list|()
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|AlreadyStoppedException
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should not find an exception policy to use"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCausedBy ()
specifier|public
name|void
name|testCausedBy
parameter_list|()
block|{
name|setupPoliciesCausedBy
argument_list|()
expr_stmt|;
name|IOException
name|ioe
init|=
operator|new
name|IOException
argument_list|(
literal|"Damm"
argument_list|)
decl_stmt|;
name|ioe
operator|.
name|initCause
argument_list|(
operator|new
name|FileNotFoundException
argument_list|(
literal|"Somefile not found"
argument_list|)
argument_list|)
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
name|ioe
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type1
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCausedByWrapped ()
specifier|public
name|void
name|testCausedByWrapped
parameter_list|()
block|{
name|setupPoliciesCausedBy
argument_list|()
expr_stmt|;
name|IOException
name|ioe
init|=
operator|new
name|IOException
argument_list|(
literal|"Damm"
argument_list|)
decl_stmt|;
name|ioe
operator|.
name|initCause
argument_list|(
operator|new
name|FileNotFoundException
argument_list|(
literal|"Somefile not found"
argument_list|)
argument_list|)
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
operator|new
name|RuntimeCamelException
argument_list|(
name|ioe
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type1
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCausedByNotConnected ()
specifier|public
name|void
name|testCausedByNotConnected
parameter_list|()
block|{
name|setupPoliciesCausedBy
argument_list|()
expr_stmt|;
name|IOException
name|ioe
init|=
operator|new
name|IOException
argument_list|(
literal|"Damm"
argument_list|)
decl_stmt|;
name|ioe
operator|.
name|initCause
argument_list|(
operator|new
name|ConnectException
argument_list|(
literal|"Not connected"
argument_list|)
argument_list|)
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
name|ioe
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type3
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCausedByOtherIO ()
specifier|public
name|void
name|testCausedByOtherIO
parameter_list|()
block|{
name|setupPoliciesCausedBy
argument_list|()
expr_stmt|;
name|IOException
name|ioe
init|=
operator|new
name|IOException
argument_list|(
literal|"Damm"
argument_list|)
decl_stmt|;
name|ioe
operator|.
name|initCause
argument_list|(
operator|new
name|MalformedURLException
argument_list|(
literal|"Bad url"
argument_list|)
argument_list|)
expr_stmt|;
name|OnExceptionDefinition
name|result
init|=
name|strategy
operator|.
name|getExceptionPolicy
argument_list|(
name|policies
argument_list|,
literal|null
argument_list|,
name|ioe
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|type2
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

