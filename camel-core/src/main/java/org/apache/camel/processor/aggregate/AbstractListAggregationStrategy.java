begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * Aggregate all exchanges into a {@link List} of values defined by the {@link #getValue(Exchange)} call.  * The combined Exchange will hold all the aggregated exchanges in a {@link java.util.List}  * as a exchange property with the key {@link org.apache.camel.Exchange#GROUPED_EXCHANGE}.  *<p/>  * The method {@link #isStoreAsBodyOnCompletion()} determines if the aggregated {@link List} should  * be stored on the {@link org.apache.camel.Message#setBody(Object)} or be kept as a property  * on the exchange.  *<br/>  * The default behavior to store as message body, allows to more easily group together a list of values  * and have its result stored as a {@link List} on the completed {@link Exchange}.  *  * @since 2.11  */
end_comment

begin_class
DECL|class|AbstractListAggregationStrategy
specifier|public
specifier|abstract
class|class
name|AbstractListAggregationStrategy
parameter_list|<
name|V
parameter_list|>
implements|implements
name|CompletionAwareAggregationStrategy
block|{
comment|/**      * This method is implemented by the sub-class and is called to retrieve      * an instance of the value that will be aggregated and forwarded to the      * receiving end point.      *<p/>      * If<tt>null</tt> is returned, then the value is<b>not</b> added to the {@link List}.      *      * @param exchange  The exchange that is used to retrieve the value from      * @return An instance of V that is the associated value of the passed exchange      */
DECL|method|getValue (Exchange exchange)
specifier|public
specifier|abstract
name|V
name|getValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Whether to store the completed aggregated {@link List} as message body, or to keep as property on the exchange.      *<p/>      * The default behavior is<tt>true</tt> to store as message body.      *      * @return<tt>true</tt> to store as message body,<tt>false</tt> to keep as property on the exchange.      */
DECL|method|isStoreAsBodyOnCompletion ()
specifier|public
name|boolean
name|isStoreAsBodyOnCompletion
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|onCompletion (Exchange exchange)
specifier|public
name|void
name|onCompletion
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|isStoreAsBodyOnCompletion
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|V
argument_list|>
operator|)
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|GROUPED_EXCHANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This method will aggregate the old and new exchange and return the result.      *      * @param oldExchange The oldest exchange, can be null      * @param newExchange The newest exchange, can be null      * @return a composite exchange of the old and/or new exchanges      */
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|list
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
name|list
operator|=
name|getList
argument_list|(
name|newExchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|list
operator|=
name|getList
argument_list|(
name|oldExchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newExchange
operator|!=
literal|null
condition|)
block|{
name|V
name|value
init|=
name|getValue
argument_list|(
name|newExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|oldExchange
operator|!=
literal|null
condition|?
name|oldExchange
else|:
name|newExchange
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getList (Exchange exchange)
specifier|private
name|List
argument_list|<
name|V
argument_list|>
name|getList
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|list
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|GROUPED_EXCHANGE
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|GROUPED_EXCHANGE
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

