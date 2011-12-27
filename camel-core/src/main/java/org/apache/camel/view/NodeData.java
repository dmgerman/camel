begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.view
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|view
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
name|model
operator|.
name|AggregateDefinition
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
name|BeanDefinition
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
name|FromDefinition
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
name|OtherwiseDefinition
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
name|RecipientListDefinition
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
name|ResequenceDefinition
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
name|RoutingSlipDefinition
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
name|SplitDefinition
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
name|ToDefinition
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
name|WhenDefinition
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isEmpty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_comment
comment|/**  * Represents a node in the EIP diagram tree  *  * @version   */
end_comment

begin_class
DECL|class|NodeData
specifier|public
class|class
name|NodeData
block|{
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|image
specifier|public
name|String
name|image
decl_stmt|;
DECL|field|label
specifier|public
name|String
name|label
decl_stmt|;
DECL|field|shape
specifier|public
name|String
name|shape
decl_stmt|;
DECL|field|edgeLabel
specifier|public
name|String
name|edgeLabel
decl_stmt|;
DECL|field|tooltop
specifier|public
name|String
name|tooltop
decl_stmt|;
DECL|field|nodeType
specifier|public
name|String
name|nodeType
decl_stmt|;
DECL|field|nodeWritten
specifier|public
name|boolean
name|nodeWritten
decl_stmt|;
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|outputs
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
decl_stmt|;
DECL|field|association
specifier|public
name|String
name|association
init|=
literal|"property"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|NodeData (String id, Object node, String imagePrefix)
specifier|public
name|NodeData
parameter_list|(
name|String
name|id
parameter_list|,
name|Object
name|node
parameter_list|,
name|String
name|imagePrefix
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|ProcessorDefinition
condition|)
block|{
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorType
init|=
operator|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|node
decl_stmt|;
name|this
operator|.
name|edgeLabel
operator|=
name|processorType
operator|.
name|getLabel
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|instanceof
name|FromDefinition
condition|)
block|{
name|FromDefinition
name|fromType
init|=
operator|(
name|FromDefinition
operator|)
name|node
decl_stmt|;
name|this
operator|.
name|tooltop
operator|=
name|fromType
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|removeQueryString
argument_list|(
name|this
operator|.
name|tooltop
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
literal|"http://camel.apache.org/message-endpoint.html"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|ToDefinition
condition|)
block|{
name|ToDefinition
name|toType
init|=
operator|(
name|ToDefinition
operator|)
name|node
decl_stmt|;
name|this
operator|.
name|tooltop
operator|=
name|toType
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|removeQueryString
argument_list|(
name|this
operator|.
name|tooltop
argument_list|)
expr_stmt|;
name|this
operator|.
name|edgeLabel
operator|=
literal|""
expr_stmt|;
name|this
operator|.
name|url
operator|=
literal|"http://camel.apache.org/message-endpoint.html"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|FilterDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"MessageFilterIcon.png"
expr_stmt|;
name|this
operator|.
name|label
operator|=
literal|"Filter"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Message Filter"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|WhenDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"MessageFilterIcon.png"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"When Filter"
expr_stmt|;
name|this
operator|.
name|label
operator|=
literal|"When"
expr_stmt|;
name|this
operator|.
name|url
operator|=
literal|"http://camel.apache.org/content-based-router.html"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|OtherwiseDefinition
condition|)
block|{
name|this
operator|.
name|nodeType
operator|=
literal|"Otherwise"
expr_stmt|;
name|this
operator|.
name|edgeLabel
operator|=
literal|""
expr_stmt|;
name|this
operator|.
name|url
operator|=
literal|"http://camel.apache.org/content-based-router.html"
expr_stmt|;
name|this
operator|.
name|tooltop
operator|=
literal|"Otherwise"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|ChoiceDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"ContentBasedRouterIcon.png"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Content Based Router"
expr_stmt|;
name|this
operator|.
name|label
operator|=
literal|"Choice"
expr_stmt|;
name|this
operator|.
name|edgeLabel
operator|=
literal|""
expr_stmt|;
name|ChoiceDefinition
name|choice
init|=
operator|(
name|ChoiceDefinition
operator|)
name|node
decl_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|(
name|choice
operator|.
name|getWhenClauses
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|choice
operator|.
name|getOtherwise
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|outputs
operator|.
name|add
argument_list|(
name|choice
operator|.
name|getOtherwise
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|RecipientListDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"RecipientListIcon.png"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Recipient List"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|RoutingSlipDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"RoutingTableIcon.png"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Routing Slip"
expr_stmt|;
name|this
operator|.
name|url
operator|=
literal|"http://camel.apache.org/routing-slip.html"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|SplitDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"SplitterIcon.png"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Splitter"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|AggregateDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"AggregatorIcon.png"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Aggregator"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|ResequenceDefinition
condition|)
block|{
name|this
operator|.
name|image
operator|=
name|imagePrefix
operator|+
literal|"ResequencerIcon.png"
expr_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Resequencer"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|BeanDefinition
condition|)
block|{
name|BeanDefinition
name|beanRef
init|=
operator|(
name|BeanDefinition
operator|)
name|node
decl_stmt|;
name|this
operator|.
name|nodeType
operator|=
literal|"Bean Ref"
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|beanRef
operator|.
name|getLabel
argument_list|()
operator|+
literal|" Bean"
expr_stmt|;
name|this
operator|.
name|shape
operator|=
literal|"box"
expr_stmt|;
block|}
comment|// lets auto-default as many values as we can
if|if
condition|(
name|isEmpty
argument_list|(
name|this
operator|.
name|nodeType
argument_list|)
operator|&&
name|node
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|node
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"Type"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|nodeType
operator|=
name|insertSpacesBetweenCamelCase
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|label
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isEmpty
argument_list|(
name|this
operator|.
name|image
argument_list|)
condition|)
block|{
name|this
operator|.
name|label
operator|=
name|this
operator|.
name|nodeType
expr_stmt|;
name|this
operator|.
name|shape
operator|=
literal|"box"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isNotEmpty
argument_list|(
name|this
operator|.
name|edgeLabel
argument_list|)
condition|)
block|{
name|this
operator|.
name|label
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|label
operator|=
name|node
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isEmpty
argument_list|(
name|this
operator|.
name|tooltop
argument_list|)
condition|)
block|{
if|if
condition|(
name|isNotEmpty
argument_list|(
name|this
operator|.
name|nodeType
argument_list|)
condition|)
block|{
name|String
name|description
init|=
name|isNotEmpty
argument_list|(
name|this
operator|.
name|edgeLabel
argument_list|)
condition|?
name|this
operator|.
name|edgeLabel
else|:
name|this
operator|.
name|label
decl_stmt|;
name|this
operator|.
name|tooltop
operator|=
name|this
operator|.
name|nodeType
operator|+
literal|": "
operator|+
name|description
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|tooltop
operator|=
name|this
operator|.
name|label
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isEmpty
argument_list|(
name|this
operator|.
name|url
argument_list|)
operator|&&
name|isNotEmpty
argument_list|(
name|this
operator|.
name|nodeType
argument_list|)
condition|)
block|{
name|this
operator|.
name|url
operator|=
literal|"http://camel.apache.org/"
operator|+
name|this
operator|.
name|nodeType
operator|.
name|toLowerCase
argument_list|()
operator|.
name|replace
argument_list|(
literal|' '
argument_list|,
literal|'-'
argument_list|)
operator|+
literal|".html"
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|instanceof
name|ProcessorDefinition
operator|&&
name|this
operator|.
name|outputs
operator|==
literal|null
condition|)
block|{
name|ProcessorDefinition
name|processorType
init|=
operator|(
name|ProcessorDefinition
operator|)
name|node
decl_stmt|;
name|this
operator|.
name|outputs
operator|=
name|processorType
operator|.
name|getOutputs
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|removeQueryString (String text)
specifier|protected
name|String
name|removeQueryString
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|int
name|idx
init|=
name|text
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<=
literal|0
condition|)
block|{
return|return
name|text
return|;
block|}
else|else
block|{
return|return
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
return|;
block|}
block|}
comment|/**      * Inserts a space before each upper case letter after a lowercase      */
DECL|method|insertSpacesBetweenCamelCase (String name)
specifier|public
specifier|static
name|String
name|insertSpacesBetweenCamelCase
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|boolean
name|lastCharacterLowerCase
init|=
literal|false
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|size
init|=
name|name
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|name
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|ch
argument_list|)
condition|)
block|{
if|if
condition|(
name|lastCharacterLowerCase
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|lastCharacterLowerCase
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|lastCharacterLowerCase
operator|=
literal|true
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

