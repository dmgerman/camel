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
name|XmlElementRef
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;completedPredicate/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"completedPredicate"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CompletedPredicate
specifier|public
class|class
name|CompletedPredicate
block|{
annotation|@
name|XmlElementRef
DECL|field|completePredicate
specifier|private
name|ExpressionType
name|completePredicate
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|predicate
specifier|private
name|Predicate
name|predicate
decl_stmt|;
DECL|method|CompletedPredicate ()
specifier|public
name|CompletedPredicate
parameter_list|()
block|{     }
DECL|method|CompletedPredicate (Predicate predicate)
specifier|public
name|CompletedPredicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
block|}
DECL|method|getCompletePredicate ()
specifier|public
name|ExpressionType
name|getCompletePredicate
parameter_list|()
block|{
return|return
name|completePredicate
return|;
block|}
DECL|method|setCompletePredicate (ExpressionType completePredicate)
specifier|public
name|void
name|setCompletePredicate
parameter_list|(
name|ExpressionType
name|completePredicate
parameter_list|)
block|{
name|this
operator|.
name|completePredicate
operator|=
name|completePredicate
expr_stmt|;
block|}
DECL|method|getPredicate ()
specifier|public
name|Predicate
name|getPredicate
parameter_list|()
block|{
return|return
name|predicate
return|;
block|}
DECL|method|setPredicate (Predicate predicate)
specifier|public
name|void
name|setPredicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
block|}
DECL|method|createPredicate (RouteContext routeContext)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|ExpressionType
name|predicateType
init|=
name|getCompletePredicate
argument_list|()
decl_stmt|;
if|if
condition|(
name|predicateType
operator|!=
literal|null
operator|&&
name|predicate
operator|==
literal|null
condition|)
block|{
name|predicate
operator|=
name|predicateType
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
return|return
name|predicate
return|;
block|}
block|}
end_class

end_unit

