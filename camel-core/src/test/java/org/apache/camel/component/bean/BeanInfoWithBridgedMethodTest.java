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
name|support
operator|.
name|DefaultExchange
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
comment|/**  * Unit test for bridged methods.  */
end_comment

begin_class
DECL|class|BeanInfoWithBridgedMethodTest
specifier|public
class|class
name|BeanInfoWithBridgedMethodTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBridgedMethod ()
specifier|public
name|void
name|testBridgedMethod
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
name|MyService
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
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|MyService
name|myService
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|"MyService"
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mi
operator|.
name|getMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|myService
argument_list|,
operator|new
name|Request
argument_list|(
literal|1
argument_list|)
argument_list|)
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
DECL|method|testPackagePrivate ()
specifier|public
name|void
name|testPackagePrivate
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
name|MyPackagePrivateService
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
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|MyPackagePrivateService
name|myService
init|=
operator|new
name|MyPackagePrivateService
argument_list|()
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|"Service"
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
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|mi
operator|.
name|getMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|myService
argument_list|,
operator|new
name|Request
argument_list|(
literal|2
argument_list|)
argument_list|)
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
DECL|method|Request (int x)
specifier|public
name|Request
parameter_list|(
name|int
name|x
parameter_list|)
block|{
name|this
operator|.
name|x
operator|=
name|x
expr_stmt|;
block|}
block|}
DECL|interface|Service
specifier|public
interface|interface
name|Service
parameter_list|<
name|R
parameter_list|>
block|{
DECL|method|process (R request)
name|int
name|process
parameter_list|(
name|R
name|request
parameter_list|)
function_decl|;
block|}
DECL|class|MyService
specifier|public
specifier|static
class|class
name|MyService
implements|implements
name|Service
argument_list|<
name|Request
argument_list|>
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
name|request
operator|.
name|x
operator|+
literal|1
return|;
block|}
block|}
DECL|class|MyPackagePrivateService
specifier|static
class|class
name|MyPackagePrivateService
implements|implements
name|Service
argument_list|<
name|Request
argument_list|>
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
name|request
operator|.
name|x
operator|+
literal|2
return|;
block|}
block|}
block|}
end_class

end_unit

