begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|groovy
package|;
end_package

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Closure
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
name|ExpressionSupport
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
name|model
operator|.
name|ChoiceDefinition
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
name|model
operator|.
name|FilterDefinition
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
name|model
operator|.
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelGroovyMethods
specifier|public
specifier|final
class|class
name|CamelGroovyMethods
block|{
DECL|method|CamelGroovyMethods ()
specifier|private
name|CamelGroovyMethods
parameter_list|()
block|{
comment|// Utility Class
block|}
DECL|method|filter (ProcessorDefinition self, Closure filter)
specifier|public
specifier|static
name|FilterDefinition
name|filter
parameter_list|(
name|ProcessorDefinition
name|self
parameter_list|,
name|Closure
name|filter
parameter_list|)
block|{
return|return
name|self
operator|.
name|filter
argument_list|(
name|toExpression
argument_list|(
name|filter
argument_list|)
argument_list|)
return|;
block|}
DECL|method|when (ChoiceDefinition self, Closure filter)
specifier|public
specifier|static
name|ChoiceDefinition
name|when
parameter_list|(
name|ChoiceDefinition
name|self
parameter_list|,
name|Closure
name|filter
parameter_list|)
block|{
return|return
name|self
operator|.
name|when
argument_list|(
name|toExpression
argument_list|(
name|filter
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toExpression (final Closure filter)
specifier|public
specifier|static
name|ExpressionSupport
name|toExpression
parameter_list|(
specifier|final
name|Closure
name|filter
parameter_list|)
block|{
return|return
operator|new
name|ExpressionSupport
argument_list|()
block|{
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|filter
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|filter
operator|.
name|call
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Groovy["
operator|+
name|filter
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

