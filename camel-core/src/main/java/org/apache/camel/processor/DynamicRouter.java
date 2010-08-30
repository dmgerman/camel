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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Implements a<a href="http://camel.apache.org/dynamic-router.html">Dynamic Router</a> pattern  * where the destination(s) is computed at runtime.  *<p/>  * This implementation builds on top of {@link org.apache.camel.processor.RoutingSlip} which contains  * the most logic.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DynamicRouter
specifier|public
class|class
name|DynamicRouter
extends|extends
name|RoutingSlip
block|{
DECL|method|DynamicRouter (CamelContext camelContext)
specifier|public
name|DynamicRouter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|DynamicRouter (CamelContext camelContext, Expression expression, String uriDelimiter)
specifier|public
name|DynamicRouter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|String
name|uriDelimiter
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
name|uriDelimiter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRoutingSlipIterator (Exchange exchange)
specifier|protected
name|RoutingSlipIterator
name|createRoutingSlipIterator
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|DynamicRoutingSlipIterator
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * The dynamic routing slip iterator.      */
DECL|class|DynamicRoutingSlipIterator
specifier|private
specifier|final
class|class
name|DynamicRoutingSlipIterator
implements|implements
name|RoutingSlipIterator
block|{
DECL|field|slip
specifier|private
specifier|final
name|Expression
name|slip
decl_stmt|;
DECL|field|current
specifier|private
name|Iterator
name|current
decl_stmt|;
DECL|method|DynamicRoutingSlipIterator (Expression slip)
specifier|private
name|DynamicRoutingSlipIterator
parameter_list|(
name|Expression
name|slip
parameter_list|)
block|{
name|this
operator|.
name|slip
operator|=
name|slip
expr_stmt|;
block|}
DECL|method|hasNext (Exchange exchange)
specifier|public
name|boolean
name|hasNext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|current
operator|!=
literal|null
operator|&&
name|current
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// evaluate next slip
name|Object
name|routingSlip
init|=
name|slip
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|routingSlip
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|current
operator|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|routingSlip
argument_list|,
name|uriDelimiter
argument_list|)
expr_stmt|;
return|return
name|current
operator|!=
literal|null
operator|&&
name|current
operator|.
name|hasNext
argument_list|()
return|;
block|}
DECL|method|next (Exchange exchange)
specifier|public
name|Object
name|next
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|current
operator|.
name|next
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

