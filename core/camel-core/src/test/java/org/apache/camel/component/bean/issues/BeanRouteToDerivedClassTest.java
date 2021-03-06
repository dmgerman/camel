begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.issues
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
operator|.
name|issues
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

begin_class
DECL|class|BeanRouteToDerivedClassTest
specifier|public
class|class
name|BeanRouteToDerivedClassTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|derived
specifier|private
name|DerivedClass
name|derived
init|=
operator|new
name|DerivedClass
argument_list|()
decl_stmt|;
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
name|Test
DECL|method|testDerivedClassCalled ()
specifier|public
name|void
name|testDerivedClassCalled
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"bean:derived?method=process"
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Derived class should have been invoked"
argument_list|,
literal|"Hello World"
argument_list|,
name|derived
operator|.
name|getAndClearBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDerivedClassCalledWithNoCustomProcessor ()
specifier|public
name|void
name|testDerivedClassCalledWithNoCustomProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|Processor
operator|.
name|class
argument_list|,
name|MyMessageListener
operator|.
name|class
argument_list|,
operator|new
name|MyMessageToProcessorConverter
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
literal|"bean:derived?method=process"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:other"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:derived"
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
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Derived class should have been invoked"
argument_list|,
literal|"Hello World"
argument_list|,
name|derived
operator|.
name|getAndClearBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:other"
argument_list|,
operator|new
name|MyMessage
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Derived class should NOT have been invoked"
argument_list|,
literal|null
argument_list|,
name|derived
operator|.
name|getAndClearBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:other"
argument_list|,
operator|new
name|MyMessage
argument_list|(
literal|"Hello Again"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Derived class should NOT have been invoked"
argument_list|,
literal|null
argument_list|,
name|derived
operator|.
name|getAndClearBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDerivedClassCalledWithCustomProcessor ()
specifier|public
name|void
name|testDerivedClassCalledWithCustomProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|Processor
operator|.
name|class
argument_list|,
name|MyMessageListener
operator|.
name|class
argument_list|,
operator|new
name|MyMessageToProcessorConverter
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
comment|// Explicit method name given so always call this
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:derived?method=process"
argument_list|)
expr_stmt|;
comment|// no explicit method name then a custom processor can kick in
name|from
argument_list|(
literal|"direct:other"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:derived"
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
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|MyMessage
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Derived class should have been invoked"
argument_list|,
literal|"Hello World"
argument_list|,
name|derived
operator|.
name|getAndClearBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:other"
argument_list|,
operator|new
name|MyMessage
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Derived class should NOT have been invoked"
argument_list|,
literal|null
argument_list|,
name|derived
operator|.
name|getAndClearBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:other"
argument_list|,
operator|new
name|MyMessage
argument_list|(
literal|"Hello Again"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Derived class should NOT have been invoked"
argument_list|,
literal|null
argument_list|,
name|derived
operator|.
name|getAndClearBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"derived"
argument_list|,
name|derived
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
block|}
end_class

end_unit

