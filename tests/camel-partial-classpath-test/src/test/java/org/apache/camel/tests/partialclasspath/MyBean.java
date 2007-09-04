begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tests.partialclasspath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tests
operator|.
name|partialclasspath
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|MyBean
specifier|public
class|class
name|MyBean
block|{
DECL|field|a
specifier|private
name|String
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|String
name|b
decl_stmt|;
DECL|method|MyBean ()
specifier|public
name|MyBean
parameter_list|()
block|{     }
DECL|method|MyBean (String a, String b)
specifier|public
name|MyBean
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|)
block|{
name|this
operator|.
name|a
operator|=
name|a
expr_stmt|;
name|this
operator|.
name|b
operator|=
name|b
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|MyBean
condition|)
block|{
name|MyBean
name|that
init|=
operator|(
name|MyBean
operator|)
name|o
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|equals
argument_list|(
name|this
operator|.
name|a
argument_list|,
name|that
operator|.
name|a
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equals
argument_list|(
name|this
operator|.
name|b
argument_list|,
name|that
operator|.
name|b
argument_list|)
return|;
block|}
return|return
literal|false
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
name|answer
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|answer
operator|+=
name|a
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
expr_stmt|;
block|}
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|answer
operator|+=
name|b
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
expr_stmt|;
block|}
return|return
name|answer
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
literal|"MyBean[a="
operator|+
name|a
operator|+
literal|" b="
operator|+
name|b
operator|+
literal|"]"
return|;
block|}
DECL|method|getA ()
specifier|public
name|String
name|getA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
DECL|method|setA (String a)
specifier|public
name|void
name|setA
parameter_list|(
name|String
name|a
parameter_list|)
block|{
name|this
operator|.
name|a
operator|=
name|a
expr_stmt|;
block|}
DECL|method|getB ()
specifier|public
name|String
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
DECL|method|setB (String b)
specifier|public
name|void
name|setB
parameter_list|(
name|String
name|b
parameter_list|)
block|{
name|this
operator|.
name|b
operator|=
name|b
expr_stmt|;
block|}
block|}
end_class

end_unit

