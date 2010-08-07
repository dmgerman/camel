begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.queue.version
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|queue
operator|.
name|version
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
name|junit
operator|.
name|Assert
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
name|AbstractApplicationContext
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

begin_class
DECL|class|LoanBrokerServerTest
specifier|public
class|class
name|LoanBrokerServerTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|startLoanBroker ()
specifier|public
name|void
name|startLoanBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/META-INF/spring/queueCamelContext.xml"
block|}
argument_list|)
decl_stmt|;
name|CamelContext
name|camelContext
init|=
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"myCamel"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The camel context should not be null"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|applicationContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

