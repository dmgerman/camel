begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|GeneratedValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Table
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Temporal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TemporalType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|UniqueConstraint
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|Entity
annotation|@
name|Table
argument_list|(
name|name
operator|=
literal|"CAMEL_MESSAGEPROCESSED"
argument_list|,
name|uniqueConstraints
operator|=
annotation|@
name|UniqueConstraint
argument_list|(
name|columnNames
operator|=
block|{
literal|"processorName"
block|,
literal|"messageId"
block|}
argument_list|)
argument_list|)
DECL|class|MessageProcessed
specifier|public
class|class
name|MessageProcessed
implements|implements
name|Serializable
block|{
DECL|field|createdAt
specifier|protected
name|Date
name|createdAt
decl_stmt|;
DECL|field|id
specifier|private
name|Long
name|id
decl_stmt|;
DECL|field|messageId
specifier|private
name|String
name|messageId
decl_stmt|;
DECL|field|processorName
specifier|private
name|String
name|processorName
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MessageProcessed[processorName: "
operator|+
name|getProcessorName
argument_list|()
operator|+
literal|" messageId: "
operator|+
name|getMessageId
argument_list|()
operator|+
literal|" createdAt: "
operator|+
name|getCreatedAt
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Id
annotation|@
name|GeneratedValue
DECL|method|getId ()
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Long id)
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getMessageId ()
specifier|public
name|String
name|getMessageId
parameter_list|()
block|{
return|return
name|messageId
return|;
block|}
DECL|method|setMessageId (String messageId)
specifier|public
name|void
name|setMessageId
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
name|this
operator|.
name|messageId
operator|=
name|messageId
expr_stmt|;
block|}
DECL|method|getProcessorName ()
specifier|public
name|String
name|getProcessorName
parameter_list|()
block|{
return|return
name|processorName
return|;
block|}
DECL|method|setProcessorName (String processorName)
specifier|public
name|void
name|setProcessorName
parameter_list|(
name|String
name|processorName
parameter_list|)
block|{
name|this
operator|.
name|processorName
operator|=
name|processorName
expr_stmt|;
block|}
annotation|@
name|Temporal
argument_list|(
name|TemporalType
operator|.
name|TIMESTAMP
argument_list|)
DECL|method|getCreatedAt ()
specifier|public
name|Date
name|getCreatedAt
parameter_list|()
block|{
return|return
name|createdAt
return|;
block|}
DECL|method|setCreatedAt (Date createdAt)
specifier|public
name|void
name|setCreatedAt
parameter_list|(
name|Date
name|createdAt
parameter_list|)
block|{
name|this
operator|.
name|createdAt
operator|=
name|createdAt
expr_stmt|;
block|}
block|}
end_class

end_unit

