begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|Predicate
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
name|Processor
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
name|processor
operator|.
name|exceptionpolicy
operator|.
name|ExceptionPolicyStrategy
import|;
end_import

begin_comment
comment|/**  * Implements a<a  * href="http://camel.apache.org/dead-letter-channel.html">Dead Letter  * Channel</a> after attempting to redeliver the message using the  * {@link RedeliveryPolicy}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DeadLetterChannel
specifier|public
class|class
name|DeadLetterChannel
extends|extends
name|RedeliveryErrorHandler
block|{
comment|/**      * Creates the dead letter channel.      *      * @param output                    outer processor that should use this dead letter channel      * @param logger                    logger to use for logging failures and redelivery attempts      * @param redeliveryProcessor       an optional processor to run before redelivery attempt      * @param redeliveryPolicy          policy for redelivery      * @param handledPolicy             policy for handling failed exception that are moved to the dead letter queue      * @param exceptionPolicyStrategy   strategy for onException handling      * @param deadLetter                the failure processor to send failed exchanges to      * @param deadLetterUri             an optional uri for logging purpose      * @param useOriginalBodyPolicy     should the original IN body be moved to the dead letter queue or the current exchange IN body?      */
DECL|method|DeadLetterChannel (Processor output, Logger logger, Processor redeliveryProcessor, RedeliveryPolicy redeliveryPolicy, Predicate handledPolicy, ExceptionPolicyStrategy exceptionPolicyStrategy, Processor deadLetter, String deadLetterUri, boolean useOriginalBodyPolicy)
specifier|public
name|DeadLetterChannel
parameter_list|(
name|Processor
name|output
parameter_list|,
name|Logger
name|logger
parameter_list|,
name|Processor
name|redeliveryProcessor
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|,
name|Predicate
name|handledPolicy
parameter_list|,
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|,
name|Processor
name|deadLetter
parameter_list|,
name|String
name|deadLetterUri
parameter_list|,
name|boolean
name|useOriginalBodyPolicy
parameter_list|)
block|{
name|super
argument_list|(
name|output
argument_list|,
name|logger
argument_list|,
name|redeliveryProcessor
argument_list|,
name|redeliveryPolicy
argument_list|,
name|handledPolicy
argument_list|,
name|deadLetter
argument_list|,
name|deadLetterUri
argument_list|,
name|useOriginalBodyPolicy
argument_list|)
expr_stmt|;
name|setExceptionPolicy
argument_list|(
name|exceptionPolicyStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|supportDeadLetterQueue ()
specifier|public
name|boolean
name|supportDeadLetterQueue
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// just to let the stacktrace reveal that this is a dead letter channel
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DeadLetterChannel["
operator|+
name|output
operator|+
literal|", "
operator|+
operator|(
name|deadLetterUri
operator|!=
literal|null
condition|?
name|deadLetterUri
else|:
name|deadLetter
operator|)
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

