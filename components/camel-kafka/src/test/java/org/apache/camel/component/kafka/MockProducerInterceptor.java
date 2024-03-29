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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|ProducerInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|ProducerRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|RecordMetadata
import|;
end_import

begin_class
DECL|class|MockProducerInterceptor
specifier|public
class|class
name|MockProducerInterceptor
implements|implements
name|ProducerInterceptor
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{
DECL|field|recordsCaptured
specifier|public
specifier|static
name|ArrayList
argument_list|<
name|ProducerRecord
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|recordsCaptured
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|onSend (ProducerRecord<String, String> producerRecord)
specifier|public
name|ProducerRecord
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|onSend
parameter_list|(
name|ProducerRecord
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|producerRecord
parameter_list|)
block|{
name|recordsCaptured
operator|.
name|add
argument_list|(
name|producerRecord
argument_list|)
expr_stmt|;
return|return
name|producerRecord
return|;
block|}
annotation|@
name|Override
DECL|method|onAcknowledgement (RecordMetadata recordMetadata, Exception e)
specifier|public
name|void
name|onAcknowledgement
parameter_list|(
name|RecordMetadata
name|recordMetadata
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|configure (Map<String, ?> map)
specifier|public
name|void
name|configure
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

