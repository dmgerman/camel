begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|transaction
operator|.
name|TransactionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
DECL|class|HazelcastSedaRecoverableConsumerNewTransactionTest
specifier|public
class|class
name|HazelcastSedaRecoverableConsumerNewTransactionTest
extends|extends
name|HazelcastSedaRecoverableConsumerTest
block|{
annotation|@
name|Override
DECL|method|trainHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|protected
name|void
name|trainHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|TransactionContext
name|transactionContext
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|TransactionContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|hazelcastInstance
operator|.
name|newTransactionContext
argument_list|()
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|HazelcastException
argument_list|(
literal|"Could not obtain Connection!!!"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|transactionContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hazelcastInstance
operator|.
name|getQueue
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|queue
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|transactionContext
operator|.
name|getQueue
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|tqueue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verifyHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|protected
name|void
name|verifyHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|verify
argument_list|(
name|hazelcastInstance
argument_list|,
name|times
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|getQueue
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|hazelcastInstance
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|newTransactionContext
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

