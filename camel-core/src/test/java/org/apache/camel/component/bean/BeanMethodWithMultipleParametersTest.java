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
name|processor
operator|.
name|BeanRouteTest
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|BeanMethodWithMultipleParametersTest
specifier|public
class|class
name|BeanMethodWithMultipleParametersTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BeanRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|myBean
specifier|protected
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testDummy ()
specifier|public
name|void
name|testDummy
parameter_list|()
throws|throws
name|Exception
block|{      }
annotation|@
name|Test
DECL|method|testSendMessageWithURI ()
specifier|public
name|void
name|testSendMessageWithURI
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
index|[]
name|args
init|=
block|{
literal|"abc"
block|,
literal|5
block|,
literal|"def"
block|}
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"bean:myBean?method=myMethod&multiParameterArray=true"
argument_list|,
name|args
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean.foo"
argument_list|,
literal|"abc"
argument_list|,
name|myBean
operator|.
name|foo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean.bar"
argument_list|,
literal|5
argument_list|,
name|myBean
operator|.
name|bar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean.x"
argument_list|,
literal|"def"
argument_list|,
name|myBean
operator|.
name|x
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageWithSettingHeader ()
specifier|public
name|void
name|testSendMessageWithSettingHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
index|[]
name|args
init|=
block|{
literal|"hello"
block|,
literal|123
block|,
literal|"world"
block|}
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
name|args
argument_list|,
name|Exchange
operator|.
name|BEAN_MULTI_PARAMETER_ARRAY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean.foo"
argument_list|,
literal|"hello"
argument_list|,
name|myBean
operator|.
name|foo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean.bar"
argument_list|,
literal|123
argument_list|,
name|myBean
operator|.
name|bar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean.x"
argument_list|,
literal|"world"
argument_list|,
name|myBean
operator|.
name|x
argument_list|)
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
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myBean?method=myMethod"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
class|class
name|MyBean
block|{
DECL|field|foo
specifier|public
name|String
name|foo
decl_stmt|;
DECL|field|bar
specifier|public
name|int
name|bar
decl_stmt|;
DECL|field|x
specifier|public
name|String
name|x
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MyBean[foo: "
operator|+
name|foo
operator|+
literal|" bar: "
operator|+
name|bar
operator|+
literal|" x: "
operator|+
name|x
operator|+
literal|"]"
return|;
block|}
DECL|method|myMethod (String foo, int bar, String x)
specifier|public
name|void
name|myMethod
parameter_list|(
name|String
name|foo
parameter_list|,
name|int
name|bar
parameter_list|,
name|String
name|x
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
name|this
operator|.
name|x
operator|=
name|x
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"myMethod() method called on "
operator|+
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|anotherMethod (Object body)
specifier|public
name|void
name|anotherMethod
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not have called this method!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

