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

begin_comment
comment|/**  * A helper class for including portions of the<a  * href="http://activemq.apache.org/camel/expression.html">expression</a> and  *<a href="http://activemq.apache.org/camel/predicate.html">predicate</a><a  * href="http://activemq.apache.org/camel/dsl.html">Java DSL</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Builder
specifier|public
specifier|final
class|class
name|Builder
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|Builder ()
specifier|private
name|Builder
parameter_list|()
block|{     }
comment|/**      * Returns a constant expression      */
DECL|method|constant (Object value)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|constant
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for headers on an exchange      */
DECL|method|header (String name)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound body on an exchange      */
DECL|method|body ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|body
parameter_list|()
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound message body as a      * specific type      */
DECL|method|bodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|,
name|T
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|bodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound body on an      * exchange      */
DECL|method|outBody ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|outBody
parameter_list|()
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound message body as a      * specific type      */
DECL|method|outBodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|,
name|T
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|outBodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault body on an      * exchange      */
DECL|method|faultBody ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|faultBody
parameter_list|()
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|faultBodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault message body as a      * specific type      */
DECL|method|faultBodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|,
name|T
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|faultBodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|faultBodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns an expression for the given system property      */
DECL|method|systemProperty (final String name)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|systemProperty
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|systemProperty
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns an expression for the given system property      */
DECL|method|systemProperty (final String name, final String defaultValue)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|systemProperty
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|defaultValue
parameter_list|)
block|{
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|ExpressionBuilder
operator|.
expr|<
name|E
operator|>
name|systemProperty
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

