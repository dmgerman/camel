begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|model
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
name|spring
operator|.
name|model
operator|.
name|language
operator|.
name|ExpressionType
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
name|spring
operator|.
name|model
operator|.
name|language
operator|.
name|LanguageExpression
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

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

begin_comment
comment|/**  * A useful base class for output types  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|OutputType
specifier|public
specifier|abstract
class|class
name|OutputType
extends|extends
name|ProcessorType
block|{
DECL|field|outputs
specifier|protected
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorType> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
DECL|method|filter (ExpressionType expression)
specifier|public
name|FilterType
name|filter
parameter_list|(
name|ExpressionType
name|expression
parameter_list|)
block|{
name|FilterType
name|filter
init|=
operator|new
name|FilterType
argument_list|()
decl_stmt|;
name|filter
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|filter
return|;
block|}
DECL|method|filter (String language, String expression)
specifier|public
name|FilterType
name|filter
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
return|return
name|filter
argument_list|(
operator|new
name|LanguageExpression
argument_list|(
name|language
argument_list|,
name|expression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|to (String uri)
specifier|public
name|OutputType
name|to
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ToType
name|to
init|=
operator|new
name|ToType
argument_list|()
decl_stmt|;
name|to
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
name|to
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

