begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.johnzon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|johnzon
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_class
DECL|class|NumberPojo
specifier|public
class|class
name|NumberPojo
block|{
DECL|field|bg
specifier|private
name|BigDecimal
name|bg
decl_stmt|;
DECL|field|intNumber
specifier|private
name|int
name|intNumber
decl_stmt|;
DECL|field|longNumber
specifier|private
name|long
name|longNumber
decl_stmt|;
DECL|field|doubleNumber
specifier|private
name|double
name|doubleNumber
decl_stmt|;
DECL|field|floatNumber
specifier|private
name|float
name|floatNumber
decl_stmt|;
DECL|field|bool
specifier|private
name|boolean
name|bool
decl_stmt|;
DECL|method|getBg ()
specifier|public
name|BigDecimal
name|getBg
parameter_list|()
block|{
return|return
name|bg
return|;
block|}
DECL|method|setBg (final BigDecimal bg)
specifier|public
name|void
name|setBg
parameter_list|(
specifier|final
name|BigDecimal
name|bg
parameter_list|)
block|{
name|this
operator|.
name|bg
operator|=
name|bg
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
DECL|method|getLongNumber ()
specifier|public
name|long
name|getLongNumber
parameter_list|()
block|{
return|return
name|longNumber
return|;
block|}
DECL|method|setLongNumber (final long longNumber)
specifier|public
name|void
name|setLongNumber
parameter_list|(
specifier|final
name|long
name|longNumber
parameter_list|)
block|{
name|this
operator|.
name|longNumber
operator|=
name|longNumber
expr_stmt|;
block|}
DECL|method|getDoubleNumber ()
specifier|public
name|double
name|getDoubleNumber
parameter_list|()
block|{
return|return
name|doubleNumber
return|;
block|}
DECL|method|setDoubleNumber (final double doubleNumber)
specifier|public
name|void
name|setDoubleNumber
parameter_list|(
specifier|final
name|double
name|doubleNumber
parameter_list|)
block|{
name|this
operator|.
name|doubleNumber
operator|=
name|doubleNumber
expr_stmt|;
block|}
DECL|method|getFloatNumber ()
specifier|public
name|float
name|getFloatNumber
parameter_list|()
block|{
return|return
name|floatNumber
return|;
block|}
DECL|method|setFloatNumber (final float floatNumber)
specifier|public
name|void
name|setFloatNumber
parameter_list|(
specifier|final
name|float
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
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|bg
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|bg
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
name|bool
condition|?
literal|1231
else|:
literal|1237
operator|)
expr_stmt|;
name|long
name|temp
decl_stmt|;
name|temp
operator|=
name|Double
operator|.
name|doubleToLongBits
argument_list|(
name|doubleNumber
argument_list|)
expr_stmt|;
name|result
operator|=
name|prime
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
name|prime
operator|*
name|result
operator|+
name|Float
operator|.
name|floatToIntBits
argument_list|(
name|floatNumber
argument_list|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|intNumber
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
call|(
name|int
call|)
argument_list|(
name|longNumber
operator|^
operator|(
name|longNumber
operator|>>>
literal|32
operator|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|equals (final Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
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
name|NumberPojo
name|other
init|=
operator|(
name|NumberPojo
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|bg
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|bg
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|bg
operator|.
name|equals
argument_list|(
name|other
operator|.
name|bg
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|bool
operator|!=
name|other
operator|.
name|bool
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|Double
operator|.
name|doubleToLongBits
argument_list|(
name|doubleNumber
argument_list|)
operator|!=
name|Double
operator|.
name|doubleToLongBits
argument_list|(
name|other
operator|.
name|doubleNumber
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|Float
operator|.
name|floatToIntBits
argument_list|(
name|floatNumber
argument_list|)
operator|!=
name|Float
operator|.
name|floatToIntBits
argument_list|(
name|other
operator|.
name|floatNumber
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|intNumber
operator|!=
name|other
operator|.
name|intNumber
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|longNumber
operator|!=
name|other
operator|.
name|longNumber
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
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
literal|"NumberPojo [bg="
operator|+
name|bg
operator|+
literal|", intNumber="
operator|+
name|intNumber
operator|+
literal|", longNumber="
operator|+
name|longNumber
operator|+
literal|", doubleNumber="
operator|+
name|doubleNumber
operator|+
literal|", floatNumber="
operator|+
name|floatNumber
operator|+
literal|", bool="
operator|+
name|bool
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

