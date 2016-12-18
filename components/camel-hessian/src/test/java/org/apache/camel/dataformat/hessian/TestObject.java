begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.hessian
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|hessian
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/** A simple object used used by {@link HessianDataFormatMarshallingTest}. */
end_comment

begin_class
DECL|class|TestObject
class|class
name|TestObject
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|bool
specifier|private
name|boolean
name|bool
decl_stmt|;
DECL|field|intNumber
specifier|private
name|int
name|intNumber
decl_stmt|;
DECL|field|floatNumber
specifier|private
name|double
name|floatNumber
decl_stmt|;
DECL|field|character
specifier|private
name|char
name|character
decl_stmt|;
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|method|isBool ()
specifier|public
name|boolean
name|isBool
parameter_list|()
block|{
return|return
name|bool
return|;
block|}
DECL|method|setBool (final boolean bool)
specifier|public
name|void
name|setBool
parameter_list|(
specifier|final
name|boolean
name|bool
parameter_list|)
block|{
name|this
operator|.
name|bool
operator|=
name|bool
expr_stmt|;
block|}
DECL|method|getIntNumber ()
specifier|public
name|int
name|getIntNumber
parameter_list|()
block|{
return|return
name|intNumber
return|;
block|}
DECL|method|setIntNumber (final int intNumber)
specifier|public
name|void
name|setIntNumber
parameter_list|(
specifier|final
name|int
name|intNumber
parameter_list|)
block|{
name|this
operator|.
name|intNumber
operator|=
name|intNumber
expr_stmt|;
block|}
DECL|method|getFloatNumber ()
specifier|public
name|double
name|getFloatNumber
parameter_list|()
block|{
return|return
name|floatNumber
return|;
block|}
DECL|method|setFloatNumber (final double floatNumber)
specifier|public
name|void
name|setFloatNumber
parameter_list|(
specifier|final
name|double
name|floatNumber
parameter_list|)
block|{
name|this
operator|.
name|floatNumber
operator|=
name|floatNumber
expr_stmt|;
block|}
DECL|method|getCharacter ()
specifier|public
name|char
name|getCharacter
parameter_list|()
block|{
return|return
name|character
return|;
block|}
DECL|method|setCharacter (final char character)
specifier|public
name|void
name|setCharacter
parameter_list|(
specifier|final
name|char
name|character
parameter_list|)
block|{
name|this
operator|.
name|character
operator|=
name|character
expr_stmt|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|setText (final String text)
specifier|public
name|void
name|setText
parameter_list|(
specifier|final
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
annotation|@
name|Override
DECL|method|equals (final Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|TestObject
name|that
init|=
operator|(
name|TestObject
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|bool
argument_list|,
name|that
operator|.
name|bool
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|intNumber
argument_list|,
name|that
operator|.
name|intNumber
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|floatNumber
argument_list|,
name|that
operator|.
name|floatNumber
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|character
argument_list|,
name|that
operator|.
name|character
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|text
argument_list|,
name|that
operator|.
name|text
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
decl_stmt|;
name|long
name|temp
decl_stmt|;
name|result
operator|=
name|bool
condition|?
literal|1
else|:
literal|0
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|intNumber
expr_stmt|;
name|temp
operator|=
name|Double
operator|.
name|doubleToLongBits
argument_list|(
name|floatNumber
argument_list|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
call|(
name|int
call|)
argument_list|(
name|temp
operator|^
operator|(
name|temp
operator|>>>
literal|32
operator|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|character
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|text
operator|!=
literal|null
condition|?
name|text
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

