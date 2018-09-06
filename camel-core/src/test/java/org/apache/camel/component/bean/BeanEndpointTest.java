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
name|JndiRegistry
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
DECL|class|BeanEndpointTest
specifier|public
class|class
name|BeanEndpointTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|FooBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testBeanEndpointCtr ()
specifier|public
name|void
name|testBeanEndpointCtr
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BeanEndpoint
name|endpoint
init|=
operator|new
name|BeanEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setEndpointUriIfNotSpecified
argument_list|(
literal|"bean:foo"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBeanName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|isCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getBeanHolder
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean:foo"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
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
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanEndpointCtrComponent ()
specifier|public
name|void
name|testBeanEndpointCtrComponent
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BeanComponent
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"bean"
argument_list|,
name|BeanComponent
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|BeanEndpoint
name|endpoint
init|=
operator|new
name|BeanEndpoint
argument_list|(
literal|"bean:foo"
argument_list|,
name|comp
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBeanName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|isCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getBeanHolder
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean:foo"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
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
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanEndpointCtrComponentBeanProcessor ()
specifier|public
name|void
name|testBeanEndpointCtrComponentBeanProcessor
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BeanComponent
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"bean"
argument_list|,
name|BeanComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|BeanHolder
name|holder
init|=
operator|new
name|RegistryBean
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
specifier|final
name|BeanProcessor
name|bp
init|=
operator|new
name|BeanProcessor
argument_list|(
name|holder
argument_list|)
decl_stmt|;
specifier|final
name|BeanEndpoint
name|endpoint
init|=
operator|new
name|BeanEndpoint
argument_list|(
literal|"bean:foo"
argument_list|,
name|comp
argument_list|,
name|bp
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setBeanName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|isCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getBeanHolder
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean:foo"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
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
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanEndpointCtrWithMethod ()
specifier|public
name|void
name|testBeanEndpointCtrWithMethod
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BeanEndpoint
name|endpoint
init|=
operator|new
name|BeanEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBeanName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setMethod
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|isCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getBeanHolder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|endpoint
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean:foo?method=hello"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
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
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanEndpointCtrWithMethodAndCache ()
specifier|public
name|void
name|testBeanEndpointCtrWithMethodAndCache
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BeanEndpoint
name|endpoint
init|=
operator|new
name|BeanEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setMultiParameterArray
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBeanName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setMethod
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getBeanHolder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|endpoint
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean:foo?method=hello"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
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
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Moon"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Moon"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanEndpointCtrWithBeanHolder ()
specifier|public
name|void
name|testBeanEndpointCtrWithBeanHolder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BeanEndpoint
name|endpoint
init|=
operator|new
name|BeanEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|BeanHolder
name|holder
init|=
operator|new
name|RegistryBean
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setBeanHolder
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|isCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|holder
argument_list|,
name|endpoint
operator|.
name|getBeanHolder
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
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
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|class|FooBean
specifier|public
class|class
name|FooBean
block|{
DECL|method|hello (String hello)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|hello
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|hello
return|;
block|}
block|}
block|}
end_class

end_unit

