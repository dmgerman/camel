begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spring
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * Main class to start the loan broker server  */
end_comment

begin_class
DECL|class|LoanBroker
specifier|public
specifier|final
class|class
name|LoanBroker
block|{
DECL|method|LoanBroker ()
specifier|private
name|LoanBroker
parameter_list|()
block|{     }
comment|// START SNIPPET: starting
DECL|method|main (String... args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
comment|// configure the location of the Spring XML file
name|main
operator|.
name|setApplicationContextUri
argument_list|(
literal|"META-INF/spring/server.xml"
argument_list|)
expr_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|LoanBrokerRoute
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
comment|// END SNIPPET: starting
block|}
end_class

end_unit

