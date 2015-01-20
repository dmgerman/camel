begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|XmlRootElement
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
name|XmlTransient
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
name|Expression
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
name|processor
operator|.
name|idempotent
operator|.
name|IdempotentConsumer
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
name|IdempotentRepository
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
name|Label
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
name|RouteContext
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Filters out duplicate messages  */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"eip,endpoints"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"idempotentConsumer"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|IdempotentConsumerDefinition
specifier|public
class|class
name|IdempotentConsumerDefinition
extends|extends
name|ExpressionNode
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|messageIdRepositoryRef
specifier|private
name|String
name|messageIdRepositoryRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|eager
specifier|private
name|Boolean
name|eager
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|skipDuplicate
specifier|private
name|Boolean
name|skipDuplicate
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|removeOnFailure
specifier|private
name|Boolean
name|removeOnFailure
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|idempotentRepository
specifier|private
name|IdempotentRepository
argument_list|<
name|?
argument_list|>
name|idempotentRepository
decl_stmt|;
DECL|method|IdempotentConsumerDefinition ()
specifier|public
name|IdempotentConsumerDefinition
parameter_list|()
block|{     }
DECL|method|IdempotentConsumerDefinition (Expression messageIdExpression, IdempotentRepository<?> idempotentRepository)
specifier|public
name|IdempotentConsumerDefinition
parameter_list|(
name|Expression
name|messageIdExpression
parameter_list|,
name|IdempotentRepository
argument_list|<
name|?
argument_list|>
name|idempotentRepository
parameter_list|)
block|{
name|super
argument_list|(
name|messageIdExpression
argument_list|)
expr_stmt|;
name|this
operator|.
name|idempotentRepository
operator|=
name|idempotentRepository
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
literal|"IdempotentConsumer["
operator|+
name|getExpression
argument_list|()
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"idempotentConsumer["
operator|+
name|getExpression
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * Sets the reference name of the message id repository      *      * @param messageIdRepositoryRef the reference name of message id repository      * @return builder      */
DECL|method|messageIdRepositoryRef (String messageIdRepositoryRef)
specifier|public
name|IdempotentConsumerDefinition
name|messageIdRepositoryRef
parameter_list|(
name|String
name|messageIdRepositoryRef
parameter_list|)
block|{
name|setMessageIdRepositoryRef
argument_list|(
name|messageIdRepositoryRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the the message id repository for the idempotent consumer      *      * @param idempotentRepository the repository instance of idempotent      * @return builder      */
DECL|method|messageIdRepository (IdempotentRepository<?> idempotentRepository)
specifier|public
name|IdempotentConsumerDefinition
name|messageIdRepository
parameter_list|(
name|IdempotentRepository
argument_list|<
name|?
argument_list|>
name|idempotentRepository
parameter_list|)
block|{
name|setMessageIdRepository
argument_list|(
name|idempotentRepository
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to eagerly add the key to the idempotent repository or wait until the exchange      * is complete. Eager is default enabled.      *      * @param eager<tt>true</tt> to add the key before processing,<tt>false</tt> to wait until      *              the exchange is complete.      * @return builder      */
DECL|method|eager (boolean eager)
specifier|public
name|IdempotentConsumerDefinition
name|eager
parameter_list|(
name|boolean
name|eager
parameter_list|)
block|{
name|setEager
argument_list|(
name|eager
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to remove or keep the key on failure.      *<p/>      * The default behavior is to remove the key on failure.      *      * @param removeOnFailure<tt>true</tt> to remove the key,<tt>false</tt> to keep the key      *                        if the exchange fails.      * @return builder      */
DECL|method|removeOnFailure (boolean removeOnFailure)
specifier|public
name|IdempotentConsumerDefinition
name|removeOnFailure
parameter_list|(
name|boolean
name|removeOnFailure
parameter_list|)
block|{
name|setRemoveOnFailure
argument_list|(
name|removeOnFailure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to skip duplicates or not.      *<p/>      * The default behavior is to skip duplicates.      *<p/>      * A duplicate message would have the Exchange property {@link org.apache.camel.Exchange#DUPLICATE_MESSAGE} set      * to a {@link Boolean#TRUE} value. A none duplicate message will not have this property set.      *      * @param skipDuplicate<tt>true</tt> to skip duplicates,<tt>false</tt> to allow duplicates.      * @return builder      */
DECL|method|skipDuplicate (boolean skipDuplicate)
specifier|public
name|IdempotentConsumerDefinition
name|skipDuplicate
parameter_list|(
name|boolean
name|skipDuplicate
parameter_list|)
block|{
name|setSkipDuplicate
argument_list|(
name|skipDuplicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getMessageIdRepositoryRef ()
specifier|public
name|String
name|getMessageIdRepositoryRef
parameter_list|()
block|{
return|return
name|messageIdRepositoryRef
return|;
block|}
DECL|method|setMessageIdRepositoryRef (String messageIdRepositoryRef)
specifier|public
name|void
name|setMessageIdRepositoryRef
parameter_list|(
name|String
name|messageIdRepositoryRef
parameter_list|)
block|{
name|this
operator|.
name|messageIdRepositoryRef
operator|=
name|messageIdRepositoryRef
expr_stmt|;
block|}
DECL|method|getMessageIdRepository ()
specifier|public
name|IdempotentRepository
argument_list|<
name|?
argument_list|>
name|getMessageIdRepository
parameter_list|()
block|{
return|return
name|idempotentRepository
return|;
block|}
DECL|method|setMessageIdRepository (IdempotentRepository<?> idempotentRepository)
specifier|public
name|void
name|setMessageIdRepository
parameter_list|(
name|IdempotentRepository
argument_list|<
name|?
argument_list|>
name|idempotentRepository
parameter_list|)
block|{
name|this
operator|.
name|idempotentRepository
operator|=
name|idempotentRepository
expr_stmt|;
block|}
DECL|method|getEager ()
specifier|public
name|Boolean
name|getEager
parameter_list|()
block|{
return|return
name|eager
return|;
block|}
DECL|method|setEager (Boolean eager)
specifier|public
name|void
name|setEager
parameter_list|(
name|Boolean
name|eager
parameter_list|)
block|{
name|this
operator|.
name|eager
operator|=
name|eager
expr_stmt|;
block|}
DECL|method|isEager ()
specifier|public
name|boolean
name|isEager
parameter_list|()
block|{
comment|// defaults to true if not configured
return|return
name|eager
operator|!=
literal|null
condition|?
name|eager
else|:
literal|true
return|;
block|}
DECL|method|getSkipDuplicate ()
specifier|public
name|Boolean
name|getSkipDuplicate
parameter_list|()
block|{
return|return
name|skipDuplicate
return|;
block|}
DECL|method|setSkipDuplicate (Boolean skipDuplicate)
specifier|public
name|void
name|setSkipDuplicate
parameter_list|(
name|Boolean
name|skipDuplicate
parameter_list|)
block|{
name|this
operator|.
name|skipDuplicate
operator|=
name|skipDuplicate
expr_stmt|;
block|}
DECL|method|isSkipDuplicate ()
specifier|public
name|boolean
name|isSkipDuplicate
parameter_list|()
block|{
comment|// defaults to true if not configured
return|return
name|skipDuplicate
operator|!=
literal|null
condition|?
name|skipDuplicate
else|:
literal|true
return|;
block|}
DECL|method|getRemoveOnFailure ()
specifier|public
name|Boolean
name|getRemoveOnFailure
parameter_list|()
block|{
return|return
name|removeOnFailure
return|;
block|}
DECL|method|setRemoveOnFailure (Boolean removeOnFailure)
specifier|public
name|void
name|setRemoveOnFailure
parameter_list|(
name|Boolean
name|removeOnFailure
parameter_list|)
block|{
name|this
operator|.
name|removeOnFailure
operator|=
name|removeOnFailure
expr_stmt|;
block|}
DECL|method|isRemoveOnFailure ()
specifier|public
name|boolean
name|isRemoveOnFailure
parameter_list|()
block|{
comment|// defaults to true if not configured
return|return
name|removeOnFailure
operator|!=
literal|null
condition|?
name|removeOnFailure
else|:
literal|true
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|idempotentRepository
init|=
operator|(
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
operator|)
name|resolveMessageIdRepository
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|idempotentRepository
argument_list|,
literal|"idempotentRepository"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// add as service to CamelContext so we can managed it and it ensures it will be shutdown when camel shutdowns
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|idempotentRepository
argument_list|)
expr_stmt|;
name|Expression
name|expression
init|=
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
return|return
operator|new
name|IdempotentConsumer
argument_list|(
name|expression
argument_list|,
name|idempotentRepository
argument_list|,
name|isEager
argument_list|()
argument_list|,
name|isSkipDuplicate
argument_list|()
argument_list|,
name|isRemoveOnFailure
argument_list|()
argument_list|,
name|childProcessor
argument_list|)
return|;
block|}
comment|/**      * Strategy method to resolve the {@link org.apache.camel.spi.IdempotentRepository} to use      *      * @param routeContext route context      * @return the repository      */
DECL|method|resolveMessageIdRepository (RouteContext routeContext)
specifier|protected
name|IdempotentRepository
argument_list|<
name|?
argument_list|>
name|resolveMessageIdRepository
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|messageIdRepositoryRef
operator|!=
literal|null
condition|)
block|{
name|idempotentRepository
operator|=
name|routeContext
operator|.
name|mandatoryLookup
argument_list|(
name|messageIdRepositoryRef
argument_list|,
name|IdempotentRepository
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|idempotentRepository
return|;
block|}
block|}
end_class

end_unit

