begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|MessageId
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
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
DECL|class|PulsarNegativeAcknowledgementTest
specifier|public
class|class
name|PulsarNegativeAcknowledgementTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testNegativeAcknowledgement ()
specifier|public
name|void
name|testNegativeAcknowledgement
parameter_list|()
block|{
name|PulsarMessageReceipt
name|receipt
init|=
operator|new
name|DefaultPulsarMessageReceipt
argument_list|(
name|mock
argument_list|(
name|Consumer
operator|.
name|class
argument_list|)
argument_list|,
name|mock
argument_list|(
name|MessageId
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|receipt
operator|.
name|negativeAcknowledge
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

