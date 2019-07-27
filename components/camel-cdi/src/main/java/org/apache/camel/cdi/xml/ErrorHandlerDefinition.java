begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|LoggingLevel
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
name|model
operator|.
name|IdentifiedType
import|;
end_import

begin_comment
comment|/**  * The&lt;errorHandler&gt; tag element.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"errorHandler"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ErrorHandlerDefinition
specifier|public
class|class
name|ErrorHandlerDefinition
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
DECL|field|type
specifier|private
name|ErrorHandlerType
name|type
init|=
name|ErrorHandlerType
operator|.
name|DefaultErrorHandler
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|deadLetterUri
specifier|private
name|String
name|deadLetterUri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|deadLetterHandleNewException
specifier|private
name|Boolean
name|deadLetterHandleNewException
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|rollbackLoggingLevel
specifier|private
name|LoggingLevel
name|rollbackLoggingLevel
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useOriginalMessage
specifier|private
name|Boolean
name|useOriginalMessage
decl_stmt|;
comment|// TODO: add useOriginalBody
annotation|@
name|XmlAttribute
DECL|field|transactionTemplateRef
specifier|private
name|String
name|transactionTemplateRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|transactionManagerRef
specifier|private
name|String
name|transactionManagerRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onRedeliveryRef
specifier|private
name|String
name|onRedeliveryRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onExceptionOccurredRef
specifier|private
name|String
name|onExceptionOccurredRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onPrepareFailureRef
specifier|private
name|String
name|onPrepareFailureRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|retryWhileRef
specifier|private
name|String
name|retryWhileRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|redeliveryPolicyRef
specifier|private
name|String
name|redeliveryPolicyRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicyFactoryBean
name|redeliveryPolicy
decl_stmt|;
DECL|method|getType ()
specifier|public
name|ErrorHandlerType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (ErrorHandlerType type)
specifier|public
name|void
name|setType
parameter_list|(
name|ErrorHandlerType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getDeadLetterUri ()
specifier|public
name|String
name|getDeadLetterUri
parameter_list|()
block|{
return|return
name|deadLetterUri
return|;
block|}
DECL|method|setDeadLetterUri (String deadLetterUri)
specifier|public
name|void
name|setDeadLetterUri
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
name|this
operator|.
name|deadLetterUri
operator|=
name|deadLetterUri
expr_stmt|;
block|}
DECL|method|getDeadLetterHandleNewException ()
specifier|public
name|Boolean
name|getDeadLetterHandleNewException
parameter_list|()
block|{
return|return
name|deadLetterHandleNewException
return|;
block|}
DECL|method|setDeadLetterHandleNewException (Boolean deadLetterHandleNewException)
specifier|public
name|void
name|setDeadLetterHandleNewException
parameter_list|(
name|Boolean
name|deadLetterHandleNewException
parameter_list|)
block|{
name|this
operator|.
name|deadLetterHandleNewException
operator|=
name|deadLetterHandleNewException
expr_stmt|;
block|}
DECL|method|getRollbackLoggingLevel ()
specifier|public
name|LoggingLevel
name|getRollbackLoggingLevel
parameter_list|()
block|{
return|return
name|rollbackLoggingLevel
return|;
block|}
DECL|method|setRollbackLoggingLevel (LoggingLevel rollbackLoggingLevel)
specifier|public
name|void
name|setRollbackLoggingLevel
parameter_list|(
name|LoggingLevel
name|rollbackLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|rollbackLoggingLevel
operator|=
name|rollbackLoggingLevel
expr_stmt|;
block|}
DECL|method|getUseOriginalMessage ()
specifier|public
name|Boolean
name|getUseOriginalMessage
parameter_list|()
block|{
return|return
name|useOriginalMessage
return|;
block|}
DECL|method|setUseOriginalMessage (Boolean useOriginalMessage)
specifier|public
name|void
name|setUseOriginalMessage
parameter_list|(
name|Boolean
name|useOriginalMessage
parameter_list|)
block|{
name|this
operator|.
name|useOriginalMessage
operator|=
name|useOriginalMessage
expr_stmt|;
block|}
DECL|method|getTransactionTemplateRef ()
specifier|public
name|String
name|getTransactionTemplateRef
parameter_list|()
block|{
return|return
name|transactionTemplateRef
return|;
block|}
DECL|method|setTransactionTemplateRef (String transactionTemplateRef)
specifier|public
name|void
name|setTransactionTemplateRef
parameter_list|(
name|String
name|transactionTemplateRef
parameter_list|)
block|{
name|this
operator|.
name|transactionTemplateRef
operator|=
name|transactionTemplateRef
expr_stmt|;
block|}
DECL|method|getTransactionManagerRef ()
specifier|public
name|String
name|getTransactionManagerRef
parameter_list|()
block|{
return|return
name|transactionManagerRef
return|;
block|}
DECL|method|setTransactionManagerRef (String transactionManagerRef)
specifier|public
name|void
name|setTransactionManagerRef
parameter_list|(
name|String
name|transactionManagerRef
parameter_list|)
block|{
name|this
operator|.
name|transactionManagerRef
operator|=
name|transactionManagerRef
expr_stmt|;
block|}
DECL|method|getOnRedeliveryRef ()
specifier|public
name|String
name|getOnRedeliveryRef
parameter_list|()
block|{
return|return
name|onRedeliveryRef
return|;
block|}
DECL|method|setOnRedeliveryRef (String onRedeliveryRef)
specifier|public
name|void
name|setOnRedeliveryRef
parameter_list|(
name|String
name|onRedeliveryRef
parameter_list|)
block|{
name|this
operator|.
name|onRedeliveryRef
operator|=
name|onRedeliveryRef
expr_stmt|;
block|}
DECL|method|getOnExceptionOccurredRef ()
specifier|public
name|String
name|getOnExceptionOccurredRef
parameter_list|()
block|{
return|return
name|onExceptionOccurredRef
return|;
block|}
DECL|method|setOnExceptionOccurredRef (String onExceptionOccurredRef)
specifier|public
name|void
name|setOnExceptionOccurredRef
parameter_list|(
name|String
name|onExceptionOccurredRef
parameter_list|)
block|{
name|this
operator|.
name|onExceptionOccurredRef
operator|=
name|onExceptionOccurredRef
expr_stmt|;
block|}
DECL|method|getOnPrepareFailureRef ()
specifier|public
name|String
name|getOnPrepareFailureRef
parameter_list|()
block|{
return|return
name|onPrepareFailureRef
return|;
block|}
DECL|method|setOnPrepareFailureRef (String onPrepareFailureRef)
specifier|public
name|void
name|setOnPrepareFailureRef
parameter_list|(
name|String
name|onPrepareFailureRef
parameter_list|)
block|{
name|this
operator|.
name|onPrepareFailureRef
operator|=
name|onPrepareFailureRef
expr_stmt|;
block|}
DECL|method|getRetryWhileRef ()
specifier|public
name|String
name|getRetryWhileRef
parameter_list|()
block|{
return|return
name|retryWhileRef
return|;
block|}
DECL|method|setRetryWhileRef (String retryWhileRef)
specifier|public
name|void
name|setRetryWhileRef
parameter_list|(
name|String
name|retryWhileRef
parameter_list|)
block|{
name|this
operator|.
name|retryWhileRef
operator|=
name|retryWhileRef
expr_stmt|;
block|}
DECL|method|getRedeliveryPolicyRef ()
specifier|public
name|String
name|getRedeliveryPolicyRef
parameter_list|()
block|{
return|return
name|redeliveryPolicyRef
return|;
block|}
DECL|method|setRedeliveryPolicyRef (String redeliveryPolicyRef)
specifier|public
name|void
name|setRedeliveryPolicyRef
parameter_list|(
name|String
name|redeliveryPolicyRef
parameter_list|)
block|{
name|this
operator|.
name|redeliveryPolicyRef
operator|=
name|redeliveryPolicyRef
expr_stmt|;
block|}
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
expr_stmt|;
block|}
DECL|method|getRedeliveryPolicy ()
specifier|public
name|RedeliveryPolicyFactoryBean
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
DECL|method|setRedeliveryPolicy (RedeliveryPolicyFactoryBean redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicyFactoryBean
name|redeliveryPolicy
parameter_list|)
block|{
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
block|}
block|}
end_class

end_unit

