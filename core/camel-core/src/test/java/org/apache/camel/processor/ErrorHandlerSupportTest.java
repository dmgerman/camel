begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Test
import|;
end_import

begin_class
DECL|class|ErrorHandlerSupportTest
specifier|public
class|class
name|ErrorHandlerSupportTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testOnePolicyChildFirst ()
specifier|public
name|void
name|testOnePolicyChildFirst
parameter_list|()
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|exceptions
operator|.
name|add
argument_list|(
name|ChildException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exceptions
operator|.
name|add
argument_list|(
name|ParentException
operator|.
name|class
argument_list|)
expr_stmt|;
name|ErrorHandlerSupport
name|support
init|=
operator|new
name|ShuntErrorHandlerSupport
argument_list|()
decl_stmt|;
name|support
operator|.
name|addExceptionPolicy
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
argument_list|,
operator|new
name|OnExceptionDefinition
argument_list|(
name|exceptions
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ChildException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ChildException
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ParentException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ParentException
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnePolicyChildLast ()
specifier|public
name|void
name|testOnePolicyChildLast
parameter_list|()
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|exceptions
operator|.
name|add
argument_list|(
name|ParentException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exceptions
operator|.
name|add
argument_list|(
name|ChildException
operator|.
name|class
argument_list|)
expr_stmt|;
name|ErrorHandlerSupport
name|support
init|=
operator|new
name|ShuntErrorHandlerSupport
argument_list|()
decl_stmt|;
name|support
operator|.
name|addExceptionPolicy
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
argument_list|,
operator|new
name|OnExceptionDefinition
argument_list|(
name|exceptions
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ChildException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ChildException
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ParentException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ParentException
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoPolicyChildFirst ()
specifier|public
name|void
name|testTwoPolicyChildFirst
parameter_list|()
block|{
name|ErrorHandlerSupport
name|support
init|=
operator|new
name|ShuntErrorHandlerSupport
argument_list|()
decl_stmt|;
name|support
operator|.
name|addExceptionPolicy
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
argument_list|,
operator|new
name|OnExceptionDefinition
argument_list|(
name|ChildException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|support
operator|.
name|addExceptionPolicy
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
argument_list|,
operator|new
name|OnExceptionDefinition
argument_list|(
name|ParentException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ChildException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ChildException
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ParentException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ParentException
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoPolicyChildLast ()
specifier|public
name|void
name|testTwoPolicyChildLast
parameter_list|()
block|{
name|ErrorHandlerSupport
name|support
init|=
operator|new
name|ShuntErrorHandlerSupport
argument_list|()
decl_stmt|;
name|support
operator|.
name|addExceptionPolicy
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
argument_list|,
operator|new
name|OnExceptionDefinition
argument_list|(
name|ParentException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|support
operator|.
name|addExceptionPolicy
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
argument_list|,
operator|new
name|OnExceptionDefinition
argument_list|(
name|ChildException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ChildException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ChildException
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ParentException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getExceptionPolicyFor
argument_list|(
name|support
argument_list|,
operator|new
name|ParentException
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getExceptionPolicyFor (ErrorHandlerSupport support, Throwable childException, int index)
specifier|private
specifier|static
name|String
name|getExceptionPolicyFor
parameter_list|(
name|ErrorHandlerSupport
name|support
parameter_list|,
name|Throwable
name|childException
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|support
operator|.
name|getExceptionPolicy
argument_list|(
literal|null
argument_list|,
name|childException
argument_list|)
operator|.
name|getExceptions
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
DECL|class|ParentException
specifier|private
specifier|static
class|class
name|ParentException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
DECL|class|ChildException
specifier|private
specifier|static
class|class
name|ChildException
extends|extends
name|ParentException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
DECL|class|ShuntErrorHandlerSupport
specifier|private
specifier|static
class|class
name|ShuntErrorHandlerSupport
extends|extends
name|ErrorHandlerSupport
block|{
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|supportTransacted ()
specifier|public
name|boolean
name|supportTransacted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getOutput ()
specifier|public
name|Processor
name|getOutput
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{         }
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
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

