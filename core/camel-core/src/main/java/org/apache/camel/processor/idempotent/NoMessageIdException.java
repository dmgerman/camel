begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
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
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * An exception thrown if no message ID could be found on a message which is to be used with the  *<a href="http://camel.apache.org/idempotent-consumer.html">Idempotent Consumer</a> pattern.  */
end_comment

begin_class
DECL|class|NoMessageIdException
specifier|public
class|class
name|NoMessageIdException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5755929795399134568L
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|method|NoMessageIdException (Exchange exchange, Expression expression)
specifier|public
name|NoMessageIdException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
literal|"No message ID could be found using expression: "
operator|+
name|expression
operator|+
literal|" on message exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
comment|/**      * The exchange which caused this failure      */
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
comment|/**      * The expression which was used      */
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
block|}
end_class

end_unit

