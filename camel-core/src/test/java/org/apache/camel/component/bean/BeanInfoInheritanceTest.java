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
name|junit
operator|.
name|Test
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * Unit test for overridden methods in an inheritance.  */
end_comment

begin_class
DECL|class|BeanInfoInheritanceTest
specifier|public
class|class
name|BeanInfoInheritanceTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInheritance ()
specifier|public
name|void
name|testInheritance
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
name|Y
operator|.
name|class
argument_list|)
decl_stmt|;
name|DefaultExchange
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|Request
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|MethodInvocation
name|mi
init|=
name|beanInfo
operator|.
name|createInvocation
argument_list|(
literal|null
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
literal|"process"
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
name|assertEquals
argument_list|(
literal|"Y"
argument_list|,
name|mi
operator|.
name|getMethod
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmbiguousMethodCallException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"This should not be ambiguous!"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testNoInheritance ()
specifier|public
name|void
name|testNoInheritance
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
name|A
operator|.
name|class
argument_list|)
decl_stmt|;
name|DefaultExchange
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|Request
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|MethodInvocation
name|mi
init|=
name|beanInfo
operator|.
name|createInvocation
argument_list|(
literal|null
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
literal|"process"
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
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|mi
operator|.
name|getMethod
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmbiguousMethodCallException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"This should not be ambiguous!"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInheritanceAndOverload ()
specifier|public
name|void
name|testInheritanceAndOverload
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
name|Z
operator|.
name|class
argument_list|)
decl_stmt|;
name|DefaultExchange
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|Request
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|beanInfo
operator|.
name|createInvocation
argument_list|(
literal|null
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"This should be ambiguous!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmbiguousMethodCallException
name|e
parameter_list|)
block|{
comment|// expected (currently not supported in camel)
block|}
block|}
DECL|class|Request
specifier|public
specifier|static
class|class
name|Request
block|{
DECL|field|x
name|int
name|x
decl_stmt|;
block|}
DECL|class|X
specifier|public
specifier|static
class|class
name|X
block|{
DECL|method|process (Request request)
specifier|public
name|int
name|process
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|class|Y
specifier|public
specifier|static
class|class
name|Y
extends|extends
name|X
block|{
DECL|method|process (Request request)
specifier|public
name|int
name|process
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
literal|1
return|;
block|}
DECL|method|compute (String body)
specifier|public
name|int
name|compute
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
literal|2
return|;
block|}
block|}
DECL|class|Z
specifier|public
specifier|static
class|class
name|Z
extends|extends
name|Y
block|{
DECL|method|compute (Request request)
specifier|public
name|int
name|compute
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
literal|2
return|;
block|}
DECL|method|process (Request request, String body)
specifier|public
name|int
name|process
parameter_list|(
name|Request
name|request
parameter_list|,
name|String
name|body
parameter_list|)
block|{
return|return
literal|3
return|;
block|}
block|}
DECL|class|A
specifier|public
specifier|static
class|class
name|A
block|{
DECL|method|doSomething (String body)
specifier|public
name|void
name|doSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|process (Request request)
specifier|public
name|int
name|process
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit

