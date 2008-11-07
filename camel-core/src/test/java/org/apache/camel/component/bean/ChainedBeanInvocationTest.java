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
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|impl
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_class
DECL|class|ChainedBeanInvocationTest
specifier|public
class|class
name|ChainedBeanInvocationTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|beanMock
specifier|protected
name|MyBean
name|beanMock
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|beanMock
operator|=
name|EasyMock
operator|.
name|createStrictMock
argument_list|(
name|MyBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|context
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|beanMock
argument_list|)
expr_stmt|;
return|return
name|context
return|;
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myBean?method=a"
argument_list|)
operator|.
name|bean
argument_list|(
name|beanMock
argument_list|,
literal|"b"
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"myBean"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myBean?method=a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myBean"
argument_list|)
operator|.
name|bean
argument_list|(
name|beanMock
argument_list|,
literal|"b"
argument_list|)
operator|.
name|bean
argument_list|(
name|beanMock
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"myBean"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"myBean"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testNormalInvocation ()
specifier|public
name|void
name|testNormalInvocation
parameter_list|()
throws|throws
name|Throwable
block|{
name|beanMock
operator|.
name|a
argument_list|()
expr_stmt|;
name|beanMock
operator|.
name|b
argument_list|()
expr_stmt|;
name|beanMock
operator|.
name|c
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|beanMock
argument_list|)
expr_stmt|;
name|Exchange
name|result
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|result
operator|.
name|getException
argument_list|()
throw|;
block|}
name|EasyMock
operator|.
name|verify
argument_list|(
name|beanMock
argument_list|)
expr_stmt|;
block|}
DECL|method|testNoMethodSpecified ()
specifier|public
name|void
name|testNoMethodSpecified
parameter_list|()
throws|throws
name|Throwable
block|{
name|beanMock
operator|.
name|a
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|beanMock
argument_list|)
expr_stmt|;
name|Exchange
name|result
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start2"
argument_list|,
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|result
operator|.
name|getException
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|IllegalStateException
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|beanMock
argument_list|)
expr_stmt|;
block|}
DECL|method|testMethodHeaderSet ()
specifier|public
name|void
name|testMethodHeaderSet
parameter_list|()
throws|throws
name|Exception
block|{
name|beanMock
operator|.
name|a
argument_list|()
expr_stmt|;
name|beanMock
operator|.
name|d
argument_list|()
expr_stmt|;
name|beanMock
operator|.
name|b
argument_list|()
expr_stmt|;
name|beanMock
operator|.
name|d
argument_list|()
expr_stmt|;
name|beanMock
operator|.
name|c
argument_list|()
expr_stmt|;
name|beanMock
operator|.
name|d
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|beanMock
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"test"
argument_list|,
name|BeanProcessor
operator|.
name|METHOD_NAME
argument_list|,
literal|"d"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|beanMock
argument_list|)
expr_stmt|;
block|}
DECL|interface|MyBean
specifier|public
interface|interface
name|MyBean
block|{
DECL|method|a ()
name|void
name|a
parameter_list|()
function_decl|;
DECL|method|b ()
name|void
name|b
parameter_list|()
function_decl|;
DECL|method|c ()
name|void
name|c
parameter_list|()
function_decl|;
DECL|method|d ()
name|void
name|d
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

