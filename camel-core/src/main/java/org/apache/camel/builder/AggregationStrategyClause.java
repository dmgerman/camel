begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
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
name|AggregationStrategy
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
name|Message
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

begin_class
DECL|class|AggregationStrategyClause
specifier|public
class|class
name|AggregationStrategyClause
parameter_list|<
name|T
parameter_list|>
implements|implements
name|AggregationStrategy
block|{
DECL|field|parent
specifier|private
specifier|final
name|T
name|parent
decl_stmt|;
DECL|field|strategy
specifier|private
name|AggregationStrategy
name|strategy
decl_stmt|;
DECL|method|AggregationStrategyClause (T parent)
specifier|public
name|AggregationStrategyClause
parameter_list|(
name|T
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|strategy
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
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
return|return
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|strategy
argument_list|,
literal|"AggregationStrategy"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
return|;
block|}
comment|// *******************************
comment|// Exchange
comment|// *******************************
comment|/**      * Define an aggregation strategy which targets the exchnage.      */
DECL|method|exchange (final BiFunction<Exchange, Exchange, Exchange> function)
specifier|public
name|T
name|exchange
parameter_list|(
specifier|final
name|BiFunction
argument_list|<
name|Exchange
argument_list|,
name|Exchange
argument_list|,
name|Exchange
argument_list|>
name|function
parameter_list|)
block|{
name|strategy
operator|=
name|function
operator|::
name|apply
expr_stmt|;
return|return
name|parent
return|;
block|}
comment|// *******************************
comment|// Message
comment|// *******************************
comment|/**      * Define an aggregation strategy which targets Exchanges In Message.      *      *<blockquote><pre>{@code      * from("direct:aggregate")      *     .aggregate()      *         .message((old, new) -> {      *             if (old == null) {      *                 return new;      *             }      *      *             String oldBody = old.getBody(String.class);      *             String newBody = new.getBody(String.class);      *      *             old.setBody(oldBody + "+" + newBody);      *      *             return old;      *         });      * }</pre></blockquote>      */
DECL|method|message (final BiFunction<Message, Message, Message> function)
specifier|public
name|T
name|message
parameter_list|(
specifier|final
name|BiFunction
argument_list|<
name|Message
argument_list|,
name|Message
argument_list|,
name|Message
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|exchange
argument_list|(
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
lambda|->
block|{
name|Message
name|oldMessage
init|=
name|oldExchange
operator|!=
literal|null
condition|?
name|oldExchange
operator|.
name|getIn
argument_list|()
else|:
literal|null
decl_stmt|;
name|Message
name|newMessage
init|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|newExchange
argument_list|,
literal|"NewExchange"
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|result
init|=
name|function
operator|.
name|apply
argument_list|(
name|oldMessage
argument_list|,
name|newMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|oldExchange
operator|.
name|setIn
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
else|else
block|{
name|newExchange
operator|.
name|setIn
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|// *******************************
comment|// Body
comment|// *******************************
comment|/**      * Define an aggregation strategy which targets Exchanges In Body.      *      *<blockquote><pre>{@code      * from("direct:aggregate")      *     .aggregate()      *         .body((old, new) -> {      *             if (old == null) {      *                 return new;      *             }      *      *             return old.toString() + new.toString();      *         });      * }</pre></blockquote>      */
DECL|method|body (final BiFunction<Object, Object, Object> function)
specifier|public
name|T
name|body
parameter_list|(
specifier|final
name|BiFunction
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|body
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|function
argument_list|)
return|;
block|}
comment|/**      * Define an aggregation strategy which targets Exchanges In Body.      *      *<blockquote><pre>{@code      * from("direct:aggregate")      *     .aggregate()      *         .body(String.class, (old, new) -> {      *             if (old == null) {      *                 return new;      *             }      *      *             return old + new;      *         });      * }</pre></blockquote>      */
DECL|method|body (final Class<B> type, final BiFunction<B, B, Object> function)
specifier|public
parameter_list|<
name|B
parameter_list|>
name|T
name|body
parameter_list|(
specifier|final
name|Class
argument_list|<
name|B
argument_list|>
name|type
parameter_list|,
specifier|final
name|BiFunction
argument_list|<
name|B
argument_list|,
name|B
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|body
argument_list|(
name|type
argument_list|,
name|type
argument_list|,
name|function
argument_list|)
return|;
block|}
comment|/**      * Define an aggregation strategy which targets Exchanges In Body.      */
DECL|method|body (final Class<O> oldType, final Class<N> newType, final BiFunction<O, N, Object> function)
specifier|public
parameter_list|<
name|O
parameter_list|,
name|N
parameter_list|>
name|T
name|body
parameter_list|(
specifier|final
name|Class
argument_list|<
name|O
argument_list|>
name|oldType
parameter_list|,
specifier|final
name|Class
argument_list|<
name|N
argument_list|>
name|newType
parameter_list|,
specifier|final
name|BiFunction
argument_list|<
name|O
argument_list|,
name|N
argument_list|,
name|Object
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|exchange
argument_list|(
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
lambda|->
block|{
name|Message
name|oldMessage
init|=
name|oldExchange
operator|!=
literal|null
condition|?
name|oldExchange
operator|.
name|getIn
argument_list|()
else|:
literal|null
decl_stmt|;
name|Message
name|newMessage
init|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|newExchange
argument_list|,
literal|"NewExchange"
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|function
operator|.
name|apply
argument_list|(
name|oldMessage
operator|!=
literal|null
condition|?
name|oldMessage
operator|.
name|getBody
argument_list|(
name|oldType
argument_list|)
else|:
literal|null
argument_list|,
name|newMessage
operator|!=
literal|null
condition|?
name|newMessage
operator|.
name|getBody
argument_list|(
name|newType
argument_list|)
else|:
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
else|else
block|{
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

