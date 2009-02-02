begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|server
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * Service for stopping the server.  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
name|value
operator|=
literal|"shutdown"
argument_list|)
DECL|class|GracefulShutdownService
specifier|public
class|class
name|GracefulShutdownService
block|{
DECL|method|shutdown (String payload)
specifier|public
name|void
name|shutdown
parameter_list|(
name|String
name|payload
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Stopping Server as we recieved a "
operator|+
name|payload
operator|+
literal|" command"
argument_list|)
expr_stmt|;
name|Main
operator|.
name|getInstance
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

