begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|errorhandler
operator|.
name|RedeliveryPolicy
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Error handler settings  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"spring,configuration,error"
argument_list|)
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
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"DefaultErrorHandler"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
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
name|String
name|deadLetterHandleNewException
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"ERROR"
argument_list|)
DECL|field|level
specifier|private
name|LoggingLevel
name|level
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"WARN"
argument_list|)
DECL|field|rollbackLoggingLevel
specifier|private
name|LoggingLevel
name|rollbackLoggingLevel
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|logName
specifier|private
name|String
name|logName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useOriginalMessage
specifier|private
name|Boolean
name|useOriginalMessage
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useOriginalBody
specifier|private
name|Boolean
name|useOriginalBody
decl_stmt|;
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
name|CamelRedeliveryPolicyFactoryBean
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
comment|/**      * The type of the error handler      */
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
comment|/**      * The dead letter endpoint uri for the Dead Letter error handler.      */
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
name|String
name|getDeadLetterHandleNewException
parameter_list|()
block|{
return|return
name|deadLetterHandleNewException
return|;
block|}
comment|/**      * Whether the dead letter channel should handle (and ignore) any new exception that may been thrown during sending the      * message to the dead letter endpoint.      *<p/>      * The default value is<tt>true</tt> which means any such kind of exception is handled and ignored. Set this to<tt>false</tt>      * to let the exception be propagated back on the {@link org.apache.camel.Exchange}. This can be used in situations      * where you use transactions, and want to use Camel's dead letter channel to deal with exceptions during routing,      * but if the dead letter channel itself fails because of a new exception being thrown, then by setting this to<tt>false</tt>      * the new exceptions is propagated back and set on the {@link org.apache.camel.Exchange}, which allows the transaction      * to detect the exception, and rollback.      */
DECL|method|setDeadLetterHandleNewException (String deadLetterHandleNewException)
specifier|public
name|void
name|setDeadLetterHandleNewException
parameter_list|(
name|String
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
DECL|method|getLevel ()
specifier|public
name|LoggingLevel
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
comment|/**      * Logging level to use when using the logging error handler type.      */
DECL|method|setLevel (LoggingLevel level)
specifier|public
name|void
name|setLevel
parameter_list|(
name|LoggingLevel
name|level
parameter_list|)
block|{
name|this
operator|.
name|level
operator|=
name|level
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
comment|/**      * Sets the logging level to use for logging transactional rollback.      *<p/>      * This option is default WARN.      */
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
DECL|method|getLogName ()
specifier|public
name|String
name|getLogName
parameter_list|()
block|{
return|return
name|logName
return|;
block|}
comment|/**      * Name of the logger to use for the logging error handler      */
DECL|method|setLogName (String logName)
specifier|public
name|void
name|setLogName
parameter_list|(
name|String
name|logName
parameter_list|)
block|{
name|this
operator|.
name|logName
operator|=
name|logName
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
comment|/**      * Will use the original input {@link org.apache.camel.Message} (original body and headers) when an {@link org.apache.camel.Exchange}      * is moved to the dead letter queue.      *<p/>      *<b>Notice:</b> this only applies when all redeliveries attempt have failed and the {@link org.apache.camel.Exchange}      * is doomed for failure.      *<br/>      * Instead of using the current inprogress {@link org.apache.camel.Exchange} IN message we use the original      * IN message instead. This allows you to store the original input in the dead letter queue instead of the inprogress      * snapshot of the IN message.      * For instance if you route transform the IN body during routing and then failed. With the original exchange      * store in the dead letter queue it might be easier to manually re submit the {@link org.apache.camel.Exchange}      * again as the IN message is the same as when Camel received it.      * So you should be able to send the {@link org.apache.camel.Exchange} to the same input.      *<p/>      * The difference between useOriginalMessage and useOriginalBody is that the former includes both the original      * body and headers, where as the latter only includes the original body. You can use the latter to enrich      * the message with custom headers and include the original message body. The former wont let you do this, as its      * using the original message body and headers as they are.      *<p/>      * You cannot enable both useOriginalMessage and useOriginalBody.      *<p/>      * By default this feature is off.      *      * @see #setUseOriginalBody(Boolean)      */
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
DECL|method|getUseOriginalBody ()
specifier|public
name|Boolean
name|getUseOriginalBody
parameter_list|()
block|{
return|return
name|useOriginalBody
return|;
block|}
comment|/**      * Will use the original input {@link org.apache.camel.Message} body (original body only) when an {@link org.apache.camel.Exchange}      * is moved to the dead letter queue.      *<p/>      *<b>Notice:</b> this only applies when all redeliveries attempt have failed and the {@link org.apache.camel.Exchange}      * is doomed for failure.      *<br/>      * Instead of using the current inprogress {@link org.apache.camel.Exchange} IN message we use the original      * IN message instead. This allows you to store the original input in the dead letter queue instead of the inprogress      * snapshot of the IN message.      * For instance if you route transform the IN body during routing and then failed. With the original exchange      * store in the dead letter queue it might be easier to manually re submit the {@link org.apache.camel.Exchange}      * again as the IN message is the same as when Camel received it.      * So you should be able to send the {@link org.apache.camel.Exchange} to the same input.      *<p/>      * The difference between useOriginalMessage and useOriginalBody is that the former includes both the original      * body and headers, where as the latter only includes the original body. You can use the latter to enrich      * the message with custom headers and include the original message body. The former wont let you do this, as its      * using the original message body and headers as they are.      *<p/>      * You cannot enable both useOriginalMessage and useOriginalBody.      *<p/>      * By default this feature is off.      *      * @see #setUseOriginalMessage(Boolean)      */
DECL|method|setUseOriginalBody (Boolean useOriginalBody)
specifier|public
name|void
name|setUseOriginalBody
parameter_list|(
name|Boolean
name|useOriginalBody
parameter_list|)
block|{
name|this
operator|.
name|useOriginalBody
operator|=
name|useOriginalBody
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
comment|/**      * References to the {@link org.springframework.transaction.support.TransactionTemplate} to use with the transaction error handler.      */
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
comment|/**      * References to the {@link org.springframework.transaction.PlatformTransactionManager} to use with the transaction error handler.      */
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
comment|/**      * Sets a reference to a processor that should be processed<b>before</b> a redelivery attempt.      *<p/>      * Can be used to change the {@link org.apache.camel.Exchange}<b>before</b> its being redelivered.      */
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
comment|/**      * Sets a reference to a processor that should be processed<b>just after</b> an exception occurred.      * Can be used to perform custom logging about the occurred exception at the exact time it happened.      *<p/>      * Important: Any exception thrown from this processor will be ignored.      */
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
comment|/**      * Sets a reference to a processor to prepare the {@link org.apache.camel.Exchange} before      * handled by the failure processor / dead letter channel. This allows for example to enrich the message      * before sending to a dead letter queue.      */
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
comment|/**      * Sets a reference to an retry while expression.      *<p/>      * Will continue retrying until expression evaluates to<tt>false</tt>.      */
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
comment|/**      * Sets a reference to a {@link RedeliveryPolicy} to be used for redelivery settings.      */
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
comment|/**      * Sets a reference to a thread pool to be used by the error handler      */
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
name|CamelRedeliveryPolicyFactoryBean
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
comment|/**      * Sets the redelivery settings      */
DECL|method|setRedeliveryPolicy (CamelRedeliveryPolicyFactoryBean redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|CamelRedeliveryPolicyFactoryBean
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

