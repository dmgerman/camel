begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
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
name|util
operator|.
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_comment
comment|/**  * The client that will invoke the loan broker service  */
end_comment

begin_comment
comment|//START SNIPPET: client
end_comment

begin_class
DECL|class|Client
specifier|public
specifier|final
class|class
name|Client
block|{
comment|//Change the port to the one on which Loan broker is listening.
DECL|field|url
specifier|private
specifier|static
name|String
name|url
init|=
literal|"http://localhost:9008/loanBroker"
decl_stmt|;
DECL|method|Client ()
specifier|private
name|Client
parameter_list|()
block|{     }
DECL|method|getProxy (String address)
specifier|public
specifier|static
name|LoanBrokerWS
name|getProxy
parameter_list|(
name|String
name|address
parameter_list|)
block|{
comment|// Now we use the simple front API to create the client proxy
name|ClientProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|ClientFactoryBean
name|clientBean
init|=
name|proxyFactory
operator|.
name|getClientFactoryBean
argument_list|()
decl_stmt|;
name|clientBean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setServiceClass
argument_list|(
name|LoanBrokerWS
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// just create a new bus for use
name|clientBean
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|LoanBrokerWS
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|LoanBrokerWS
name|loanBroker
init|=
name|getProxy
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|loanBroker
operator|.
name|getLoanQuote
argument_list|(
literal|"SSN"
argument_list|,
literal|5000.00
argument_list|,
literal|24
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Took "
operator|+
name|watch
operator|.
name|stop
argument_list|()
operator|+
literal|" milliseconds to call the loan broker service"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: client
end_comment

end_unit

