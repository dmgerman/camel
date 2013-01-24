begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.pojo_messaging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|pojo_messaging
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
name|Consume
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
name|Produce
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
name|ProducerTemplate
import|;
end_import

begin_comment
comment|//START SNIPPET: ex
end_comment

begin_class
DECL|class|SendFileRecordsToQueueBean
specifier|public
class|class
name|SendFileRecordsToQueueBean
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"activemq:personnel.records"
argument_list|)
DECL|field|producer
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|Consume
argument_list|(
name|uri
operator|=
literal|"file:src/data?noop=true&initialDelay=100&delay=100"
argument_list|)
DECL|method|onFileSendToQueue (String body)
specifier|public
name|void
name|onFileSendToQueue
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|producer
operator|.
name|sendBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: ex
end_comment

end_unit

