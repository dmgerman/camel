begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.expression
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|expression
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
name|Predicate
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
name|support
operator|.
name|PredicateToExpressionAdapter
import|;
end_import

begin_class
DECL|class|ExchangeExpression
specifier|public
specifier|final
class|class
name|ExchangeExpression
block|{
DECL|method|ExchangeExpression ()
specifier|private
name|ExchangeExpression
parameter_list|()
block|{     }
DECL|method|fromCamelContext (final String contextName)
specifier|public
specifier|static
name|Expression
name|fromCamelContext
parameter_list|(
specifier|final
name|String
name|contextName
parameter_list|)
block|{
return|return
operator|new
name|PredicateToExpressionAdapter
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|contextName
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

