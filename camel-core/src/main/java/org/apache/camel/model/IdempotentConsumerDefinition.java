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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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
name|Metadata
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
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing"
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
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|eager
specifier|private
name|Boolean
name|eager
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|completionEager
specifier|private
name|Boolean
name|completionEager
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|skipDuplicate
specifier|private
name|Boolean
name|skipDuplicate
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
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
name|idempotentRepository
decl_stmt|;
DECL|method|IdempotentConsumerDefinition ()
specifier|public
name|IdempotentConsumerDefinition
parameter_list|()
block|{     }
DECL|method|IdempotentConsumerDefinition (Expression messageIdExpression, IdempotentRepository idempotentRepository)
specifier|public
name|IdempotentConsumerDefinition
parameter_list|(
name|Expression
name|messageIdExpression
parameter_list|,
name|IdempotentRepository
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
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"idempotentConsumer"
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
comment|/**      * Sets the message id repository for the idempotent consumer      *      * @param idempotentRepository the repository instance of idempotent      * @return builder      */
DECL|method|messageIdRepository (IdempotentRepository idempotentRepository)
specifier|public
name|IdempotentConsumerDefinition
name|messageIdRepository
parameter_list|(
name|IdempotentRepository
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
comment|/**      * Sets whether to complete the idempotent consumer eager or when the exchange is done.      *<p/>      * If this option is<tt>true</tt> to complete eager, then the idempotent consumer will trigger its completion      * when the exchange reached the end of the block of the idempotent consumer pattern. So if the exchange      * is continued routed after the block ends, then whatever happens there does not affect the state.      *<p/>      * If this option is<tt>false</tt> (default) to<b>not</b> complete eager, then the idempotent consumer      * will complete when the exchange is done being routed. So if the exchange is continued routed after the block ends,      * then whatever happens there<b>also</b> affect the state.      * For example if the exchange failed due to an exception, then the state of the idempotent consumer will be a rollback.      *      * @param completionEager   whether to complete eager or complete when the exchange is done      * @return builder      */
DECL|method|completionEager (boolean completionEager)
specifier|public
name|IdempotentConsumerDefinition
name|completionEager
parameter_list|(
name|boolean
name|completionEager
parameter_list|)
block|{
name|setCompletionEager
argument_list|(
name|completionEager
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
comment|/**      * Expression used to calculate the correlation key to use for duplicate check.      * The Exchange which has the same correlation key is regarded as a duplicate and will be rejected.      */
annotation|@
name|Override
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// override to include javadoc what the expression is used for
name|super
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
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
name|getMessageIdRepository
parameter_list|()
block|{
return|return
name|idempotentRepository
return|;
block|}
DECL|method|setMessageIdRepository (IdempotentRepository idempotentRepository)
specifier|public
name|void
name|setMessageIdRepository
parameter_list|(
name|IdempotentRepository
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
DECL|method|getCompletionEager ()
specifier|public
name|Boolean
name|getCompletionEager
parameter_list|()
block|{
return|return
name|completionEager
return|;
block|}
DECL|method|setCompletionEager (Boolean completionEager)
specifier|public
name|void
name|setCompletionEager
parameter_list|(
name|Boolean
name|completionEager
parameter_list|)
block|{
name|this
operator|.
name|completionEager
operator|=
name|completionEager
expr_stmt|;
block|}
annotation|@
name|Override
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
name|idempotentRepository
init|=
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
comment|// these boolean should be true by default
name|boolean
name|eager
init|=
name|getEager
argument_list|()
operator|==
literal|null
operator|||
name|getEager
argument_list|()
decl_stmt|;
name|boolean
name|duplicate
init|=
name|getSkipDuplicate
argument_list|()
operator|==
literal|null
operator|||
name|getSkipDuplicate
argument_list|()
decl_stmt|;
name|boolean
name|remove
init|=
name|getRemoveOnFailure
argument_list|()
operator|==
literal|null
operator|||
name|getRemoveOnFailure
argument_list|()
decl_stmt|;
comment|// these boolean should be false by default
name|boolean
name|completionEager
init|=
name|getCompletionEager
argument_list|()
operator|!=
literal|null
operator|&&
name|getCompletionEager
argument_list|()
decl_stmt|;
return|return
operator|new
name|IdempotentConsumer
argument_list|(
name|expression
argument_list|,
name|idempotentRepository
argument_list|,
name|eager
argument_list|,
name|completionEager
argument_list|,
name|duplicate
argument_list|,
name|remove
argument_list|,
name|childProcessor
argument_list|)
return|;
block|}
comment|/**      * Strategy method to resolve the {@link org.apache.camel.spi.IdempotentRepository} to use      *      * @param routeContext route context      * @return the repository      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|resolveMessageIdRepository (RouteContext routeContext)
specifier|protected
name|IdempotentRepository
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

