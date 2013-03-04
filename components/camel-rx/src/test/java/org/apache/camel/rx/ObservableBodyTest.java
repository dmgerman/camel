begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|util
operator|.
name|functions
operator|.
name|Action1
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|util
operator|.
name|functions
operator|.
name|Func1
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
DECL|class|ObservableBodyTest
specifier|public
class|class
name|ObservableBodyTest
extends|extends
name|RxTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ObservableBodyTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testConsume ()
specifier|public
name|void
name|testConsume
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MockEndpoint
name|mockEndpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"b"
argument_list|,
literal|"d"
argument_list|)
expr_stmt|;
comment|// lets consume, filter and map events
name|Observable
argument_list|<
name|Order
argument_list|>
name|observable
init|=
name|reactiveCamel
operator|.
name|toObservable
argument_list|(
literal|"seda:orders"
argument_list|,
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
name|Observable
argument_list|<
name|String
argument_list|>
name|largeOrderIds
init|=
name|observable
operator|.
name|filter
argument_list|(
operator|new
name|Func1
argument_list|<
name|Order
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|call
parameter_list|(
name|Order
name|order
parameter_list|)
block|{
return|return
name|order
operator|.
name|getAmount
argument_list|()
operator|>
literal|100.0
return|;
block|}
block|}
argument_list|)
operator|.
name|map
argument_list|(
operator|new
name|Func1
argument_list|<
name|Order
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|(
name|Order
name|order
parameter_list|)
block|{
return|return
name|order
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// lets route the largeOrderIds to the mock endpoint for testing
name|largeOrderIds
operator|.
name|take
argument_list|(
literal|2
argument_list|)
operator|.
name|subscribe
argument_list|(
operator|new
name|Action1
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Processing  "
operator|+
name|body
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|mockEndpoint
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// now lets send some orders in
name|Order
index|[]
name|orders
init|=
block|{
operator|new
name|Order
argument_list|(
literal|"a"
argument_list|,
literal|49.95
argument_list|)
block|,
operator|new
name|Order
argument_list|(
literal|"b"
argument_list|,
literal|125.50
argument_list|)
block|,
operator|new
name|Order
argument_list|(
literal|"c"
argument_list|,
literal|22.95
argument_list|)
block|,
operator|new
name|Order
argument_list|(
literal|"d"
argument_list|,
literal|259.95
argument_list|)
block|,
operator|new
name|Order
argument_list|(
literal|"e"
argument_list|,
literal|1.25
argument_list|)
block|}
decl_stmt|;
for|for
control|(
name|Order
name|order
range|:
name|orders
control|)
block|{
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"seda:orders"
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

