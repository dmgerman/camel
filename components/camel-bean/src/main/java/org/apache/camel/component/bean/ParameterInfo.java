begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * Parameter information to be used for method invocation.  */
end_comment

begin_class
DECL|class|ParameterInfo
specifier|final
class|class
name|ParameterInfo
block|{
DECL|field|index
specifier|private
specifier|final
name|int
name|index
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|annotations
specifier|private
specifier|final
name|Annotation
index|[]
name|annotations
decl_stmt|;
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|method|ParameterInfo (int index, Class<?> type, Annotation[] annotations, Expression expression)
name|ParameterInfo
parameter_list|(
name|int
name|index
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|annotations
operator|=
name|annotations
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getAnnotations ()
specifier|public
name|Annotation
index|[]
name|getAnnotations
parameter_list|()
block|{
return|return
name|annotations
return|;
block|}
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
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
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
name|expression
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
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"ParameterInfo"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"[index="
argument_list|)
operator|.
name|append
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", type="
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", annotations="
argument_list|)
operator|.
name|append
argument_list|(
name|annotations
operator|==
literal|null
condition|?
literal|"null"
else|:
name|Arrays
operator|.
name|asList
argument_list|(
name|annotations
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", expression="
argument_list|)
operator|.
name|append
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

