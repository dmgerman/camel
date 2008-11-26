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
name|processor
operator|.
name|idempotent
operator|.
name|MessageIdRepository
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

begin_comment
comment|/**  * Represents an XML&lt;idempotentConsumer/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
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
DECL|class|IdempotentConsumerType
specifier|public
class|class
name|IdempotentConsumerType
extends|extends
name|ExpressionNode
block|{
annotation|@
name|XmlAttribute
DECL|field|messageIdRepositoryRef
specifier|private
name|String
name|messageIdRepositoryRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|messageIdRepository
specifier|private
name|MessageIdRepository
name|messageIdRepository
decl_stmt|;
DECL|method|IdempotentConsumerType ()
specifier|public
name|IdempotentConsumerType
parameter_list|()
block|{     }
DECL|method|IdempotentConsumerType (Expression messageIdExpression, MessageIdRepository messageIdRepository)
specifier|public
name|IdempotentConsumerType
parameter_list|(
name|Expression
name|messageIdExpression
parameter_list|,
name|MessageIdRepository
name|messageIdRepository
parameter_list|)
block|{
name|super
argument_list|(
name|messageIdExpression
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageIdRepository
operator|=
name|messageIdRepository
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
name|MessageIdRepository
name|getMessageIdRepository
parameter_list|()
block|{
return|return
name|messageIdRepository
return|;
block|}
DECL|method|setMessageIdRepository (MessageIdRepository messageIdRepository)
specifier|public
name|void
name|setMessageIdRepository
parameter_list|(
name|MessageIdRepository
name|messageIdRepository
parameter_list|)
block|{
name|this
operator|.
name|messageIdRepository
operator|=
name|messageIdRepository
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
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|MessageIdRepository
name|messageIdRepository
init|=
name|resolveMessageIdRepository
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
return|return
operator|new
name|IdempotentConsumer
argument_list|(
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
argument_list|,
name|messageIdRepository
argument_list|,
name|childProcessor
argument_list|)
return|;
block|}
comment|/**      * Strategy method to resolve the {@link org.apache.camel.processor.idempotent.MessageIdRepository} to use      *      * @param routeContext  route context      * @return the repository      */
DECL|method|resolveMessageIdRepository (RouteContext routeContext)
specifier|protected
name|MessageIdRepository
name|resolveMessageIdRepository
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|messageIdRepository
operator|==
literal|null
condition|)
block|{
name|messageIdRepository
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|messageIdRepositoryRef
argument_list|,
name|MessageIdRepository
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|messageIdRepository
return|;
block|}
block|}
end_class

end_unit

