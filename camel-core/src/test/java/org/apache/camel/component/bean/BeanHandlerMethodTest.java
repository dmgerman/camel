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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Body
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
name|Handler
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
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|BeanHandlerMethodTest
specifier|public
class|class
name|BeanHandlerMethodTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testNoHandleMethod ()
specifier|public
name|void
name|testNoHandleMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|MyNoDummyBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyNoDummyBean
name|pojo
init|=
operator|new
name|MyNoDummyBean
argument_list|()
decl_stmt|;
name|MethodInvocation
name|mi
init|=
name|info
operator|.
name|createInvocation
argument_list|(
name|pojo
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mi
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|mi
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAmbigiousMethod ()
specifier|public
name|void
name|testAmbigiousMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|MyAmbigiousBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyAmbigiousBean
name|pojo
init|=
operator|new
name|MyAmbigiousBean
argument_list|()
decl_stmt|;
try|try
block|{
name|info
operator|.
name|createInvocation
argument_list|(
name|pojo
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
DECL|method|testHandleMethod ()
specifier|public
name|void
name|testHandleMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|MyDummyBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyDummyBean
name|pojo
init|=
operator|new
name|MyDummyBean
argument_list|()
decl_stmt|;
name|MethodInvocation
name|mi
init|=
name|info
operator|.
name|createInvocation
argument_list|(
name|pojo
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mi
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|mi
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHandleAndBodyMethod ()
specifier|public
name|void
name|testHandleAndBodyMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|MyOtherDummyBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyOtherDummyBean
name|pojo
init|=
operator|new
name|MyOtherDummyBean
argument_list|()
decl_stmt|;
name|MethodInvocation
name|mi
init|=
name|info
operator|.
name|createInvocation
argument_list|(
name|pojo
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mi
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|mi
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHandleAmbigious ()
specifier|public
name|void
name|testHandleAmbigious
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|MyReallyDummyBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyReallyDummyBean
name|pojo
init|=
operator|new
name|MyReallyDummyBean
argument_list|()
decl_stmt|;
try|try
block|{
name|info
operator|.
name|createInvocation
argument_list|(
name|pojo
argument_list|,
name|exchange
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
DECL|class|MyNoDummyBean
specifier|public
specifier|static
class|class
name|MyNoDummyBean
block|{
DECL|method|hello (@ody String hi)
specifier|public
name|String
name|hello
parameter_list|(
annotation|@
name|Body
name|String
name|hi
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|hi
return|;
block|}
DECL|method|doCompute (String input)
specifier|public
name|String
name|doCompute
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoke me"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|class|MyAmbigiousBean
specifier|public
specifier|static
class|class
name|MyAmbigiousBean
block|{
DECL|method|hello (String hi)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|hi
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoke me"
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|hi
return|;
block|}
DECL|method|doCompute (String input)
specifier|public
name|String
name|doCompute
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoke me"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|class|MyDummyBean
specifier|public
specifier|static
class|class
name|MyDummyBean
block|{
annotation|@
name|Handler
DECL|method|hello (String hi)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|hi
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|hi
return|;
block|}
DECL|method|doCompute (String input)
specifier|public
name|String
name|doCompute
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoke me"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|class|MyOtherDummyBean
specifier|public
specifier|static
class|class
name|MyOtherDummyBean
block|{
annotation|@
name|Handler
DECL|method|hello (String hi)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|hi
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|hi
return|;
block|}
DECL|method|bye (@ody String input)
specifier|public
name|String
name|bye
parameter_list|(
annotation|@
name|Body
name|String
name|input
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoke me"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|class|MyReallyDummyBean
specifier|public
specifier|static
class|class
name|MyReallyDummyBean
block|{
annotation|@
name|Handler
DECL|method|hello (String hi)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|hi
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|hi
return|;
block|}
annotation|@
name|Handler
DECL|method|bye (@ody String input)
specifier|public
name|String
name|bye
parameter_list|(
annotation|@
name|Body
name|String
name|input
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoke me"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

