begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.testfilters
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|testfilters
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
name|Exchange
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
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|filter
operator|.
name|MessageFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessage
import|;
end_import

begin_class
DECL|class|EmptyMessageFilter
specifier|public
class|class
name|EmptyMessageFilter
implements|implements
name|MessageFilter
block|{
annotation|@
name|Override
DECL|method|filterProducer (Exchange exchange, WebServiceMessage produceResponse)
specifier|public
name|void
name|filterProducer
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|WebServiceMessage
name|produceResponse
parameter_list|)
block|{
comment|// Do nothing
block|}
annotation|@
name|Override
DECL|method|filterConsumer (Exchange exchange, WebServiceMessage consumerResponse)
specifier|public
name|void
name|filterConsumer
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|WebServiceMessage
name|consumerResponse
parameter_list|)
block|{
comment|// Do nothing
block|}
block|}
end_class

end_unit

