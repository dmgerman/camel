begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
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
name|CamelContext
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
name|ExchangePattern
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
name|ProducerTemplate
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
name|DefaultProducerTemplate
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
name|ProducerTemplateProcessor
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A JMS {@link MessageListener} which converts an incoming JMS message into a Camel message {@link Exchange} then  * processing it by Camel; either using a custom {@link Processor} or if you use one of the static<code>newInstance()</code>  * methods such as {@link #newInstance(org.apache.camel.CamelContext, String)} or  * {@link #newInstance(org.apache.camel.CamelContext, org.apache.camel.ProducerTemplate)}  * you can send the message exchange into a Camel endpoint for processing.  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|CamelMessageListener
specifier|public
class|class
name|CamelMessageListener
implements|implements
name|MessageListener
implements|,
name|Processor
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
init|=
operator|new
name|JmsBinding
argument_list|()
decl_stmt|;
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
init|=
name|ExchangePattern
operator|.
name|InOnly
decl_stmt|;
DECL|method|CamelMessageListener (CamelContext camelContext, Processor processor)
specifier|public
name|CamelMessageListener
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new CamelMessageListener which will invoke a Camel endpoint      *      * @param camelContext the context to use      * @param endpointUri  the endpoint to invoke with the JMS message {@link Exchange}      * @return a newly created JMS MessageListener      */
DECL|method|newInstance (CamelContext camelContext, String endpointUri)
specifier|public
specifier|static
name|CamelMessageListener
name|newInstance
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|endpointUri
parameter_list|)
block|{
name|DefaultProducerTemplate
name|producerTemplate
init|=
name|DefaultProducerTemplate
operator|.
name|newInstance
argument_list|(
name|camelContext
argument_list|,
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|newInstance
argument_list|(
name|camelContext
argument_list|,
name|producerTemplate
argument_list|)
return|;
block|}
comment|/**      * Creates a new CamelMessageListener which will invoke the default Camel endpoint of the given      * {@link ProducerTemplate}      *      * @param camelContext the context to use      * @param producerTemplate  the template used to send the exchange      * @return a newly created JMS MessageListener      */
DECL|method|newInstance (CamelContext camelContext, ProducerTemplate producerTemplate)
specifier|public
specifier|static
name|CamelMessageListener
name|newInstance
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ProducerTemplate
name|producerTemplate
parameter_list|)
block|{
return|return
operator|new
name|CamelMessageListener
argument_list|(
name|camelContext
argument_list|,
operator|new
name|ProducerTemplateProcessor
argument_list|(
name|producerTemplate
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Processes the incoming JMS message      */
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Processes the Camel message {@link Exchange}      */
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|exchange
argument_list|,
literal|"exchange"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|JmsBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
comment|/**      * Sets the JMS binding used to adapt the incoming JMS message into a Camel message {@link Exchange}      */
DECL|method|setBinding (JmsBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JmsBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
comment|/**      * Sets the message exchange pattern that will be used on the Camel message {@link Exchange}      */
DECL|method|setPattern (ExchangePattern pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * Returns a newly created Camel message {@link Exchange} from an inbound JMS message      */
DECL|method|createExchange (Message message)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|JmsExchange
argument_list|(
name|camelContext
argument_list|,
name|pattern
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

