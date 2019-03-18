begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_class
DECL|class|JmsFormatDateHeadersToIso8601Test
specifier|public
class|class
name|JmsFormatDateHeadersToIso8601Test
extends|extends
name|CamelTestSupport
block|{
DECL|field|DATE
specifier|private
specifier|static
specifier|final
name|Date
name|DATE
init|=
name|Date
operator|.
name|from
argument_list|(
name|Instant
operator|.
name|ofEpochMilli
argument_list|(
literal|1519672338000L
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testComponentFormatDateHeaderToIso8601 ()
specifier|public
name|void
name|testComponentFormatDateHeaderToIso8601
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|outDate
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start-isoformat"
argument_list|,
literal|"body"
argument_list|,
literal|"date"
argument_list|,
name|DATE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|outDate
argument_list|,
literal|"2018-02-26T19:12:18Z"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBindingFormatDateHeaderToIso8601 ()
specifier|public
name|void
name|testBindingFormatDateHeaderToIso8601
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|outDate
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start-nonisoformat"
argument_list|,
literal|"body"
argument_list|,
literal|"date"
argument_list|,
name|DATE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|outDate
argument_list|,
literal|"2018-02-26T19:12:18Z"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|JmsComponent
name|jms
init|=
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|jms
operator|.
name|setFormatDateHeadersToIso8601
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jms
argument_list|)
expr_stmt|;
return|return
name|camelContext
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
literal|"direct:start-isoformat"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-nonisoformat"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:foo?formatDateHeadersToIso8601=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:foo"
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${in.header.date}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

