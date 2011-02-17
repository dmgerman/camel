begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.javaspace
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|javaspace
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Consumer
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JavaSpaceEndpoint
specifier|public
class|class
name|JavaSpaceEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|remaining
specifier|private
specifier|final
name|String
name|remaining
decl_stmt|;
DECL|field|parameters
specifier|private
specifier|final
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|parameters
decl_stmt|;
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
DECL|field|spaceName
specifier|private
name|String
name|spaceName
decl_stmt|;
DECL|field|transactional
specifier|private
name|boolean
name|transactional
decl_stmt|;
DECL|field|transactionTimeout
specifier|private
name|long
name|transactionTimeout
init|=
name|Long
operator|.
name|MAX_VALUE
decl_stmt|;
DECL|field|verb
specifier|private
name|String
name|verb
init|=
literal|"take"
decl_stmt|;
DECL|field|templateId
specifier|private
name|String
name|templateId
decl_stmt|;
DECL|method|JavaSpaceEndpoint (String endpointUri, String remaining, Map<?, ?> parameters, JavaSpaceComponent component)
specifier|public
name|JavaSpaceEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|,
name|JavaSpaceComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|remaining
operator|=
name|remaining
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
DECL|method|isTransactional ()
specifier|public
name|boolean
name|isTransactional
parameter_list|()
block|{
return|return
name|transactional
return|;
block|}
DECL|method|getVerb ()
specifier|public
name|String
name|getVerb
parameter_list|()
block|{
return|return
name|verb
return|;
block|}
DECL|method|setVerb (String verb)
specifier|public
name|void
name|setVerb
parameter_list|(
name|String
name|verb
parameter_list|)
block|{
name|this
operator|.
name|verb
operator|=
name|verb
expr_stmt|;
block|}
DECL|method|setTransactional (boolean transactional)
specifier|public
name|void
name|setTransactional
parameter_list|(
name|boolean
name|transactional
parameter_list|)
block|{
name|this
operator|.
name|transactional
operator|=
name|transactional
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JavaSpaceProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange ()
specifier|public
name|DefaultExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getRemaining ()
specifier|public
name|String
name|getRemaining
parameter_list|()
block|{
return|return
name|remaining
return|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|JavaSpaceConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|getSpaceName ()
specifier|public
name|String
name|getSpaceName
parameter_list|()
block|{
return|return
name|spaceName
return|;
block|}
DECL|method|setSpaceName (String spaceName)
specifier|public
name|void
name|setSpaceName
parameter_list|(
name|String
name|spaceName
parameter_list|)
block|{
name|this
operator|.
name|spaceName
operator|=
name|spaceName
expr_stmt|;
block|}
DECL|method|getTemplateId ()
specifier|public
name|String
name|getTemplateId
parameter_list|()
block|{
return|return
name|templateId
return|;
block|}
DECL|method|setTemplateId (String templateId)
specifier|public
name|void
name|setTemplateId
parameter_list|(
name|String
name|templateId
parameter_list|)
block|{
name|this
operator|.
name|templateId
operator|=
name|templateId
expr_stmt|;
block|}
DECL|method|getTransactionTimeout ()
specifier|public
name|long
name|getTransactionTimeout
parameter_list|()
block|{
return|return
name|transactionTimeout
return|;
block|}
DECL|method|setTransactionTimeout (long transactionTimeout)
specifier|public
name|void
name|setTransactionTimeout
parameter_list|(
name|long
name|transactionTimeout
parameter_list|)
block|{
name|this
operator|.
name|transactionTimeout
operator|=
name|transactionTimeout
expr_stmt|;
block|}
block|}
end_class

end_unit

