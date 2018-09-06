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
name|util
operator|.
name|Map
import|;
end_import

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
name|ExchangeProperties
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
name|Headers
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
name|Processor
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
DECL|class|BeanWithPropertiesAndHeadersInjectionTest
specifier|public
class|class
name|BeanWithPropertiesAndHeadersInjectionTest
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
DECL|method|testSendMessage ()
specifier|public
name|void
name|testSendMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:in"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"p1"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"p2"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"h1"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"h2"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|foo
init|=
name|myBean
operator|.
name|foo
decl_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|bar
init|=
name|myBean
operator|.
name|bar
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"myBean.foo"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"myBean.bar"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.p1"
argument_list|,
literal|"abc"
argument_list|,
name|foo
operator|.
name|get
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.p2"
argument_list|,
literal|123
argument_list|,
name|foo
operator|.
name|get
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar.h1"
argument_list|,
literal|"xyz"
argument_list|,
name|bar
operator|.
name|get
argument_list|(
literal|"h1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar.h2"
argument_list|,
literal|456
argument_list|,
name|bar
operator|.
name|get
argument_list|(
literal|"h2"
argument_list|)
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
name|bean
argument_list|(
literal|"myBean"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|field|foo
specifier|public
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|foo
decl_stmt|;
DECL|field|bar
specifier|public
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|bar
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
literal|"]"
return|;
block|}
DECL|method|myMethod (@xchangeProperties Map<?, ?> foo, @Headers Map<?, ?> bar)
specifier|public
name|void
name|myMethod
parameter_list|(
annotation|@
name|ExchangeProperties
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|foo
parameter_list|,
annotation|@
name|Headers
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|bar
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
block|}
block|}
end_class

end_unit

