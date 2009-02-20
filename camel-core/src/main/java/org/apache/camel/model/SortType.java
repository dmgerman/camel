begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
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
name|XmlAccessorType
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
name|XmlAttribute
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
name|XmlElement
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
name|XmlRootElement
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
name|XmlTransient
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
name|Processor
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
name|processor
operator|.
name|SortProcessor
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
name|spi
operator|.
name|RouteContext
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|ExpressionBuilder
operator|.
name|bodyExpression
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;sort/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"sort"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SortType
specifier|public
class|class
name|SortType
extends|extends
name|OutputType
argument_list|<
name|SortType
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|comparator
specifier|private
name|Comparator
name|comparator
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|comparatorRef
specifier|private
name|String
name|comparatorRef
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"expression"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|expression
specifier|private
name|ExpressionSubElementType
name|expression
decl_stmt|;
DECL|method|SortType ()
specifier|public
name|SortType
parameter_list|()
block|{     }
DECL|method|SortType (Expression expression)
specifier|public
name|SortType
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|SortType (Expression expression, Comparator comparator)
specifier|public
name|SortType
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Comparator
name|comparator
parameter_list|)
block|{
name|this
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"sort["
operator|+
name|getExpression
argument_list|()
operator|+
literal|" by: "
operator|+
operator|(
name|comparatorRef
operator|!=
literal|null
condition|?
literal|"ref:"
operator|+
name|comparatorRef
else|:
name|comparator
operator|)
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"sort"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lookup in registry
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|comparatorRef
argument_list|)
condition|)
block|{
name|comparator
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|comparatorRef
argument_list|,
name|Comparator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// if no comparator then default on to string representation
if|if
condition|(
name|comparator
operator|==
literal|null
condition|)
block|{
name|comparator
operator|=
operator|new
name|Comparator
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
comment|// if no expression provided then default to body expression
if|if
condition|(
name|getExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setExpression
argument_list|(
name|bodyExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|SortProcessor
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|getComparator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
DECL|method|getComparator ()
specifier|public
name|Comparator
name|getComparator
parameter_list|()
block|{
return|return
name|comparator
return|;
block|}
DECL|method|setComparator (Comparator comparator)
specifier|public
name|void
name|setComparator
parameter_list|(
name|Comparator
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
DECL|method|getComparatorRef ()
specifier|public
name|String
name|getComparatorRef
parameter_list|()
block|{
return|return
name|comparatorRef
return|;
block|}
DECL|method|setComparatorRef (String comparatorRef)
specifier|public
name|void
name|setComparatorRef
parameter_list|(
name|String
name|comparatorRef
parameter_list|)
block|{
name|this
operator|.
name|comparatorRef
operator|=
name|comparatorRef
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|expression
operator|.
name|getExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
operator|.
name|getExpression
argument_list|()
return|;
block|}
return|return
name|expression
operator|.
name|getExpressionType
argument_list|()
return|;
block|}
DECL|method|setExpression (Expression expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
operator|new
name|ExpressionSubElementType
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the comparator to use for sorting      *      * @param comparator  the comparator to use for sorting      * @return the builder      */
DECL|method|comparator (Comparator comparator)
specifier|public
name|SortType
name|comparator
parameter_list|(
name|Comparator
name|comparator
parameter_list|)
block|{
name|setComparator
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a reference to lookup for the comparator to use for sorting      *      * @param ref reference for the comparator      * @return the builder      */
DECL|method|comparatorRef (String ref)
specifier|public
name|SortType
name|comparatorRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setComparatorRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

