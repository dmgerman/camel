begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_comment
comment|/**  * The {@link MessageProducer} resources for all {@link SjmsProducer}  * classes.  */
end_comment

begin_class
DECL|class|MessageProducerResources
specifier|public
class|class
name|MessageProducerResources
block|{
DECL|field|session
specifier|private
specifier|final
name|Session
name|session
decl_stmt|;
DECL|field|messageProducer
specifier|private
specifier|final
name|MessageProducer
name|messageProducer
decl_stmt|;
DECL|field|commitStrategy
specifier|private
specifier|final
name|TransactionCommitStrategy
name|commitStrategy
decl_stmt|;
DECL|method|MessageProducerResources (Session session, MessageProducer messageProducer)
specifier|public
name|MessageProducerResources
parameter_list|(
name|Session
name|session
parameter_list|,
name|MessageProducer
name|messageProducer
parameter_list|)
block|{
name|this
argument_list|(
name|session
argument_list|,
name|messageProducer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MessageProducerResources (Session session, MessageProducer messageProducer, TransactionCommitStrategy commitStrategy)
specifier|public
name|MessageProducerResources
parameter_list|(
name|Session
name|session
parameter_list|,
name|MessageProducer
name|messageProducer
parameter_list|,
name|TransactionCommitStrategy
name|commitStrategy
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|messageProducer
operator|=
name|messageProducer
expr_stmt|;
name|this
operator|.
name|commitStrategy
operator|=
name|commitStrategy
expr_stmt|;
block|}
comment|/**      * Gets the Session value of session for this instance of      * MessageProducerResources.      *      * @return the session      */
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
comment|/**      * Gets the QueueSender value of queueSender for this instance of      * MessageProducerResources.      *      * @return the queueSender      */
DECL|method|getMessageProducer ()
specifier|public
name|MessageProducer
name|getMessageProducer
parameter_list|()
block|{
return|return
name|messageProducer
return|;
block|}
comment|/**      * Gets the TransactionCommitStrategy value of commitStrategy for this      * instance of SjmsProducer.MessageProducerResources.      *      * @return the commitStrategy      */
DECL|method|getCommitStrategy ()
specifier|public
name|TransactionCommitStrategy
name|getCommitStrategy
parameter_list|()
block|{
return|return
name|commitStrategy
return|;
block|}
block|}
end_class

end_unit

