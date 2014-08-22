begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_class
DECL|class|InfinispanProducerTest
specifier|public
class|class
name|InfinispanProducerTest
extends|extends
name|InfinispanTestSupport
block|{
DECL|field|COMMAND_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|COMMAND_VALUE
init|=
literal|"commandValue"
decl_stmt|;
DECL|field|COMMAND_KEY
specifier|private
specifier|static
specifier|final
name|String
name|COMMAND_KEY
init|=
literal|"commandKey1"
decl_stmt|;
annotation|@
name|Test
DECL|method|keyAndValueArePublishedWithDefaultOperation ()
specifier|public
name|void
name|keyAndValueArePublishedWithDefaultOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|KEY_ONE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|VALUE
argument_list|,
name|VALUE_ONE
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|currentCache
argument_list|()
operator|.
name|get
argument_list|(
name|KEY_ONE
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|is
argument_list|(
name|VALUE_ONE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|publishKeyAndValueByExplicitlySpecifyingTheOperation ()
specifier|public
name|void
name|publishKeyAndValueByExplicitlySpecifyingTheOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|KEY_ONE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|VALUE
argument_list|,
name|VALUE_ONE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|OPERATION
argument_list|,
name|InfinispanConstants
operator|.
name|PUT
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|currentCache
argument_list|()
operator|.
name|get
argument_list|(
name|KEY_ONE
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|is
argument_list|(
name|VALUE_ONE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putOperationReturnsThePreviousValue ()
specifier|public
name|void
name|putOperationReturnsThePreviousValue
parameter_list|()
throws|throws
name|Exception
block|{
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
name|KEY_ONE
argument_list|,
literal|"existing value"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|KEY_ONE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|VALUE
argument_list|,
name|VALUE_ONE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|OPERATION
argument_list|,
name|InfinispanConstants
operator|.
name|PUT
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|RESULT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"existing value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|retrievesAValueByKey ()
specifier|public
name|void
name|retrievesAValueByKey
parameter_list|()
throws|throws
name|Exception
block|{
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
name|KEY_ONE
argument_list|,
name|VALUE_ONE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|KEY_ONE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|OPERATION
argument_list|,
name|InfinispanConstants
operator|.
name|GET
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|RESULT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|is
argument_list|(
name|VALUE_ONE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deletesExistingValueByKey ()
specifier|public
name|void
name|deletesExistingValueByKey
parameter_list|()
throws|throws
name|Exception
block|{
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
name|KEY_ONE
argument_list|,
name|VALUE_ONE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|KEY_ONE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|OPERATION
argument_list|,
name|InfinispanConstants
operator|.
name|REMOVE
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|RESULT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|is
argument_list|(
name|VALUE_ONE
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|currentCache
argument_list|()
operator|.
name|get
argument_list|(
name|KEY_ONE
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|value
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clearsAllValues ()
specifier|public
name|void
name|clearsAllValues
parameter_list|()
throws|throws
name|Exception
block|{
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
name|KEY_ONE
argument_list|,
name|VALUE_ONE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|currentCache
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|OPERATION
argument_list|,
name|InfinispanConstants
operator|.
name|CLEAR
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|currentCache
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUriCommandOption ()
specifier|public
name|void
name|testUriCommandOption
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:put"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|COMMAND_KEY
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|VALUE
argument_list|,
name|COMMAND_VALUE
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|String
name|result
init|=
operator|(
name|String
operator|)
name|currentCache
argument_list|()
operator|.
name|get
argument_list|(
name|COMMAND_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|COMMAND_VALUE
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
decl_stmt|;
name|exchange
operator|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:get"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|COMMAND_KEY
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|String
name|resultGet
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|RESULT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|COMMAND_VALUE
argument_list|,
name|resultGet
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:remove"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|COMMAND_KEY
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|String
name|resultRemove
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|RESULT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|COMMAND_VALUE
argument_list|,
name|resultRemove
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|currentCache
argument_list|()
operator|.
name|get
argument_list|(
name|COMMAND_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|currentCache
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
name|COMMAND_KEY
argument_list|,
name|COMMAND_VALUE
argument_list|)
expr_stmt|;
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
literal|"keyTest"
argument_list|,
literal|"valueTest"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:clear"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{              }
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|currentCache
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan://localhost?cacheContainer=#cacheContainer"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:put"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan://localhost?cacheContainer=#cacheContainer&command=PUT"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan://localhost?cacheContainer=#cacheContainer&command=GET"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:remove"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan://localhost?cacheContainer=#cacheContainer&command=REMOVE"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:clear"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan://localhost?cacheContainer=#cacheContainer&command=CLEAR"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

