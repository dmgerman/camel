begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
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
name|CamelContext
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
name|impl
operator|.
name|JndiRegistry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|apache
operator|.
name|activemq
operator|.
name|camel
operator|.
name|component
operator|.
name|ActiveMQComponent
operator|.
name|activeMQComponent
import|;
end_import

begin_comment
comment|/**  * Unit test from an user request on the forum.  */
end_comment

begin_class
DECL|class|JmsInOutPipelineWithBeanTest
specifier|public
class|class
name|JmsInOutPipelineWithBeanTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testA ()
specifier|public
name|void
name|testA
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:testA"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Reply"
argument_list|,
literal|"Hello World,From Bean,From A,From B"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testB ()
specifier|public
name|void
name|testB
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:testB"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Reply"
argument_list|,
literal|"Hello World,From A,From Bean,From B"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testC ()
specifier|public
name|void
name|testC
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:testC"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Reply"
argument_list|,
literal|"Hello World,From A,From B,From Bean"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|activeMQComponent
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"dummyBean"
argument_list|,
operator|new
name|MyDummyBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|reg
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"activemq:testA"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:dummyBean"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:testB"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:dummyBean"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:testC"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:dummyBean"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:a"
argument_list|)
operator|.
name|process
argument_list|(
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
operator|+
literal|",From A"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:b"
argument_list|)
operator|.
name|process
argument_list|(
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
operator|+
literal|",From B"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyDummyBean
specifier|public
specifier|static
class|class
name|MyDummyBean
block|{
DECL|method|doSomething (Exchange exchange)
specifier|public
name|void
name|doSomething
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
operator|+
literal|",From Bean"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

