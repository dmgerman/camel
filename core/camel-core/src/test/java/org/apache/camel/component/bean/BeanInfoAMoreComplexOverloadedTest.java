begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Message
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
name|DefaultExchange
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
name|DefaultMessage
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
DECL|class|BeanInfoAMoreComplexOverloadedTest
specifier|public
class|class
name|BeanInfoAMoreComplexOverloadedTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRequestA ()
specifier|public
name|void
name|testRequestA
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|beanInfo
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|Bean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|RequestA
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|MethodInvocation
name|methodInvocation
init|=
name|beanInfo
operator|.
name|createInvocation
argument_list|(
operator|new
name|Bean
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|methodInvocation
operator|.
name|getMethod
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"doSomething"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RequestA
operator|.
name|class
argument_list|,
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRequestB ()
specifier|public
name|void
name|testRequestB
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|beanInfo
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|Bean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|RequestB
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|MethodInvocation
name|methodInvocation
init|=
name|beanInfo
operator|.
name|createInvocation
argument_list|(
operator|new
name|Bean
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|methodInvocation
operator|.
name|getMethod
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"doSomething"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RequestB
operator|.
name|class
argument_list|,
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAmbigious ()
specifier|public
name|void
name|testAmbigious
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|beanInfo
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|Bean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
try|try
block|{
name|beanInfo
operator|.
name|createInvocation
argument_list|(
operator|new
name|Bean
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmbiguousMethodCallException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e
operator|.
name|getMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Bean
class|class
name|Bean
block|{
DECL|method|doSomething (RequestA request)
specifier|public
name|void
name|doSomething
parameter_list|(
name|RequestA
name|request
parameter_list|)
block|{         }
DECL|method|doSomething (RequestB request)
specifier|public
name|void
name|doSomething
parameter_list|(
name|RequestB
name|request
parameter_list|)
block|{         }
block|}
DECL|class|BaseRequest
class|class
name|BaseRequest
block|{
DECL|field|id
specifier|public
name|long
name|id
decl_stmt|;
block|}
DECL|class|RequestA
class|class
name|RequestA
extends|extends
name|BaseRequest
block|{
DECL|field|i
specifier|public
name|int
name|i
decl_stmt|;
block|}
DECL|class|RequestB
class|class
name|RequestB
extends|extends
name|BaseRequest
block|{
DECL|field|s
specifier|public
name|String
name|s
decl_stmt|;
block|}
block|}
end_class

end_unit
