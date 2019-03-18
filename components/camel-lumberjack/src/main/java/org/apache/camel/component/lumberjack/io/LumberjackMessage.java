begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack.io
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
operator|.
name|io
package|;
end_package

begin_comment
comment|/**  * A received lumberjack message, that contains it's sequence number and payload.  */
end_comment

begin_class
DECL|class|LumberjackMessage
specifier|final
class|class
name|LumberjackMessage
block|{
DECL|field|sequenceNumber
specifier|private
specifier|final
name|int
name|sequenceNumber
decl_stmt|;
DECL|field|payload
specifier|private
specifier|final
name|Object
name|payload
decl_stmt|;
DECL|method|LumberjackMessage (int sequenceNumber, Object payload)
name|LumberjackMessage
parameter_list|(
name|int
name|sequenceNumber
parameter_list|,
name|Object
name|payload
parameter_list|)
block|{
name|this
operator|.
name|sequenceNumber
operator|=
name|sequenceNumber
expr_stmt|;
name|this
operator|.
name|payload
operator|=
name|payload
expr_stmt|;
block|}
DECL|method|getSequenceNumber ()
name|int
name|getSequenceNumber
parameter_list|()
block|{
return|return
name|sequenceNumber
return|;
block|}
DECL|method|getPayload ()
name|Object
name|getPayload
parameter_list|()
block|{
return|return
name|payload
return|;
block|}
block|}
end_class

end_unit

