begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
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
name|support
operator|.
name|DefaultHeaderFilterStrategy
import|;
end_import

begin_class
DECL|class|KafkaHeaderFilterStrategy
specifier|public
class|class
name|KafkaHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|KafkaHeaderFilterStrategy ()
specifier|public
name|KafkaHeaderFilterStrategy
parameter_list|()
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
DECL|method|initialize ()
specifier|protected
name|void
name|initialize
parameter_list|()
block|{
comment|// filter out kafka record metadata
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"org.apache.kafka.clients.producer.RecordMetadata"
argument_list|)
expr_stmt|;
comment|// filter headers begin with "Camel" or "org.apache.camel"
name|setOutFilterPattern
argument_list|(
literal|"(?i)(Camel|org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*"
argument_list|)
expr_stmt|;
name|setInFilterPattern
argument_list|(
literal|"(?i)(Camel|org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

