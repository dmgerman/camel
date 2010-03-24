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
name|ProducerTemplate
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
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsInOutWithSpringRestartIssueTest
specifier|public
class|class
name|JmsInOutWithSpringRestartIssueTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/jms/issues/JmsInOutWithSpringRestartIssueTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Test
DECL|method|testRestartSpringIssue ()
specifier|public
name|void
name|testRestartSpringIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|producer
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Object
name|out
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"activemq:queue:foo"
argument_list|,
literal|"Foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Foo"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// on purpose forget to stop the producer and it should still work
comment|//producer.stop();
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
comment|// TODO: Does not work properly with AMQ 5.3.1
comment|// context.start();
comment|//producer = context.createProducerTemplate();
comment|// out = producer.requestBody("activemq:queue:foo", "Bar");
comment|//assertEquals("Bye Bar", out);
comment|//producer.stop();
comment|//context.stop();
block|}
block|}
end_class

end_unit

