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

begin_comment
comment|/**  * Creates an {@link org.apache.camel.language.Simple} language builder.  *<p/>  * This builder is available in the Java DSL from the {@link RouteBuilder} which means that using  * simple language for {@link Expression}s or {@link Predicate}s is very easy with the help of this builder.  *  * @version   */
end_comment

begin_class
DECL|class|SimpleBuilder
specifier|public
class|class
name|SimpleBuilder
implements|implements
name|Predicate
implements|,
name|Expression
block|{
DECL|field|text
specifier|private
specifier|final
name|String
name|text
decl_stmt|;
DECL|method|SimpleBuilder (String text)
specifier|public
name|SimpleBuilder
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
DECL|method|simple (String text)
specifier|public
specifier|static
name|SimpleBuilder
name|simple
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|SimpleBuilder
argument_list|(
name|text
argument_list|)
return|;
block|}
DECL|method|matches (Exchange exchange)
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
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createPredicate
argument_list|(
name|text
argument_list|)
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|text
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

