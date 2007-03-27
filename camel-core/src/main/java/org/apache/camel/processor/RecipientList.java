begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Endpoint
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
name|impl
operator|.
name|ServiceSupport
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
name|ExchangeHelper
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
name|notNull
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
name|ProducerCache
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * Implements a dynamic<a href="http://activemq.apache.org/camel/recipient-list.html">Recipient List</a> pattern  * where the list of actual endpoints to send a message exchange to are dependent on some dynamic expression.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RecipientList
specifier|public
class|class
name|RecipientList
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|Processor
argument_list|<
name|E
argument_list|>
block|{
DECL|field|expression
specifier|private
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
decl_stmt|;
DECL|field|producerCache
specifier|private
name|ProducerCache
argument_list|<
name|E
argument_list|>
name|producerCache
init|=
operator|new
name|ProducerCache
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|RecipientList (Expression<E> expression)
specifier|public
name|RecipientList
parameter_list|(
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
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
literal|"RecipientList["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
DECL|method|onExchange (E exchange)
specifier|public
name|void
name|onExchange
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|receipientList
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Iterator
name|iter
init|=
name|ObjectHelper
operator|.
name|iterator
argument_list|(
name|receipientList
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|recipient
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
decl_stmt|;
name|producerCache
operator|.
name|getProducer
argument_list|(
name|endpoint
argument_list|)
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveEndpoint (E exchange, Object recipient)
specifier|protected
name|Endpoint
argument_list|<
name|E
argument_list|>
name|resolveEndpoint
parameter_list|(
name|E
name|exchange
parameter_list|,
name|Object
name|recipient
parameter_list|)
block|{
return|return
name|ExchangeHelper
operator|.
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
return|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|producerCache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

