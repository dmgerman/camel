begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|tx
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Verify the ability to batch transactions to the consumer.  *  */
end_comment

begin_class
DECL|class|BatchTransactedQueueConsumerTest
specifier|public
class|class
name|BatchTransactedQueueConsumerTest
extends|extends
name|TransactedConsumerSupport
block|{
DECL|field|BROKER_URI
specifier|private
specifier|static
specifier|final
name|String
name|BROKER_URI
init|=
literal|"vm://btqc_test_broker?broker.persistent=false&broker.useJmx=false"
decl_stmt|;
comment|/**      * We want to verify that when consuming from a single destination with      * multiple routes that we are thread safe and behave accordingly.      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|destinationName
init|=
literal|"sjms:queue:one.consumer.one.route.batch.tx.test"
decl_stmt|;
name|int
name|routeCount
init|=
literal|1
decl_stmt|;
name|int
name|consumerCount
init|=
literal|1
decl_stmt|;
name|int
name|batchCount
init|=
literal|5
decl_stmt|;
name|int
name|messageCount
init|=
literal|20
decl_stmt|;
name|int
name|maxAttemptsCount
init|=
literal|10
decl_stmt|;
name|int
name|totalRedeliverdFalse
init|=
literal|20
decl_stmt|;
name|int
name|totalRedeliveredTrue
init|=
literal|5
decl_stmt|;
name|runTest
argument_list|(
name|destinationName
argument_list|,
name|routeCount
argument_list|,
name|messageCount
argument_list|,
name|totalRedeliverdFalse
argument_list|,
name|totalRedeliveredTrue
argument_list|,
name|batchCount
argument_list|,
name|consumerCount
argument_list|,
name|maxAttemptsCount
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBrokerUri ()
specifier|public
name|String
name|getBrokerUri
parameter_list|()
block|{
return|return
name|BROKER_URI
return|;
block|}
block|}
end_class

end_unit

