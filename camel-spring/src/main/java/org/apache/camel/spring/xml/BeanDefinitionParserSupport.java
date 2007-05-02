begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|AbstractSimpleBeanDefinitionParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * A base class for a parser for a bean.  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|BeanDefinitionParserSupport
specifier|public
class|class
name|BeanDefinitionParserSupport
extends|extends
name|AbstractSimpleBeanDefinitionParser
block|{
DECL|field|type
specifier|private
name|Class
name|type
decl_stmt|;
DECL|method|BeanDefinitionParserSupport (Class type)
specifier|public
name|BeanDefinitionParserSupport
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getBeanClass (Element element)
specifier|protected
name|Class
name|getBeanClass
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
DECL|method|isEligibleAttribute (String attributeName)
specifier|protected
name|boolean
name|isEligibleAttribute
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|super
operator|.
name|isEligibleAttribute
argument_list|(
name|attributeName
argument_list|)
operator|&&
operator|!
name|attributeName
operator|.
name|equals
argument_list|(
literal|"xmlns"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

