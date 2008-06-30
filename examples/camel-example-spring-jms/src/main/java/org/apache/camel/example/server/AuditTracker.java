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
name|Endpoint
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
name|aspectj
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Aspect
import|;
end_import

begin_import
import|import
name|org
operator|.
name|aspectj
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Required
import|;
end_import

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_comment
comment|/**  * For audit tracking of all incoming invocations of our business (Multiplier)  */
end_comment

begin_class
annotation|@
name|Aspect
DECL|class|AuditTracker
specifier|public
class|class
name|AuditTracker
block|{
comment|// endpoint we use for backup store of audit tracks
DECL|field|store
specifier|private
name|Endpoint
name|store
decl_stmt|;
annotation|@
name|Required
DECL|method|setStore (Endpoint store)
specifier|public
name|void
name|setStore
parameter_list|(
name|Endpoint
name|store
parameter_list|)
block|{
name|this
operator|.
name|store
operator|=
name|store
expr_stmt|;
block|}
annotation|@
name|Before
argument_list|(
literal|"execution(int org.apache.camel.example.server.Multiplier.multiply(int))&& args(originalNumber)"
argument_list|)
DECL|method|audit (int originalNumber)
specifier|public
name|void
name|audit
parameter_list|(
name|int
name|originalNumber
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
literal|"Someone called us with this number "
operator|+
name|originalNumber
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
comment|// now send the message to the backup store using the Camel Message Endpoint pattern
name|Exchange
name|exchange
init|=
name|store
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|store
operator|.
name|createProducer
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

