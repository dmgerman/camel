begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
package|;
end_package

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|dsl
operator|.
name|ProducerType
import|;
end_import

begin_comment
comment|/**  * This enumeration re-enumerated the values of the {@link ProducerType} according to the Camel Case convention used  * in Camel.  * Multi is the default {@link ProducerType}.  */
end_comment

begin_enum
DECL|enum|DisruptorProducerType
specifier|public
enum|enum
name|DisruptorProducerType
block|{
comment|/**      * Create a RingBuffer with a single event publisher to the Disruptors RingBuffer      */
DECL|enumConstant|Single
name|Single
parameter_list|(
name|ProducerType
operator|.
name|SINGLE
parameter_list|)
operator|,
comment|/**      * Create a RingBuffer supporting multiple event publishers to the Disruptors RingBuffer      */
DECL|enumConstant|Multi
constructor|Multi(ProducerType.MULTI
block|)
enum|;
end_enum

begin_decl_stmt
DECL|field|producerType
specifier|private
specifier|final
name|ProducerType
name|producerType
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|DisruptorProducerType (ProducerType producerType)
name|DisruptorProducerType
argument_list|(
name|ProducerType
name|producerType
argument_list|)
block|{
name|this
operator|.
name|producerType
operator|=
name|producerType
block|;     }
DECL|method|getProducerType ()
specifier|public
name|ProducerType
name|getProducerType
argument_list|()
block|{
return|return
name|producerType
return|;
block|}
end_expr_stmt

unit|}
end_unit

